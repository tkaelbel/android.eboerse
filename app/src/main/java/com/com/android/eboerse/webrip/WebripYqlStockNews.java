package com.com.android.eboerse.webrip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import com.com.android.eboerse.main.MainActivity;
import com.com.android.eboerse.main.MyErrorToast;
import com.com.android.eboerse.R;
import com.com.android.eboerse.search.DetailView;
import com.com.android.eboerse.stock.ArrayAdapterable;
import com.com.android.eboerse.stock.StockInfoAdapter;
import com.com.android.eboerse.webrip.webrip.model.WebripStockNewsInfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

public class WebripYqlStockNews extends AsyncTask<String, String, String>{

	private String yqlQuery = null;
	private ProgressDialog pdia;
	private Activity act = null;
	private ListView view = null;

	private ListView viewTop = null;
	private ListView viewBot = null;

	private String tab = null;
	private ArrayList<ArrayAdapterable> news = null;

	public WebripYqlStockNews(Activity mainact, ListView view, String tab) {
		this.act = mainact;
		this.view = view;
		this.tab = tab;
	}

	public WebripYqlStockNews(Activity act, ListView top, ListView flop, String tab){
		this.act = act;
		this.viewTop = top;
		this.viewBot = flop;
		this.tab = tab;	
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if(act != null){
			pdia = new ProgressDialog(act);
			pdia.setMessage("Loading...");
			pdia.setCancelable(false);
			pdia.show();
		}
	}

	private ArrayList<ArrayAdapterable> formatRippedString(String ripped){
		ArrayList<ArrayAdapterable> webRipStockNewsInfoList = new ArrayList<ArrayAdapterable>();
		WebripStockNewsInfo info = null;

		String htmlLast = ".html\\\"";
		String first = "///";
		String last = "a>";
		String newsFirst = "i;\\\">";
		String citeLast = "<\\/cite>";
		String citeFirst = "<cite>";
		String imgSrc = "alt=\\";
		
		Log.v("Test", last);

		String newsub = ripped.replace("<a href=\\\"\\/", first);
		newsub = newsub.replace(citeLast, "###");

		while(newsub.contains(first)){
			int firstidx = newsub.indexOf(first);
			int lastidx = newsub.indexOf(last);
			
			int cideFirstidx = newsub.indexOf(citeFirst);
			int cideLastidx = newsub.indexOf("###");

			if(firstidx > lastidx){
				newsub = newsub.replaceFirst(last, "");
				continue;
			}
			
			if(cideFirstidx > cideLastidx){
				newsub = newsub.replaceFirst("###", "");
				continue;
			}

			info = new WebripStockNewsInfo();
			String symbol = newsub.substring(firstidx+first.length(), lastidx);
			
			if(cideFirstidx != -1 && cideLastidx != -1){
				String cite = newsub.substring(cideFirstidx + citeFirst.length()+1, cideLastidx);
				info.setTimeAndSite(cite);	
			}
			
			String htmlStr = symbol.substring(0, symbol.indexOf(htmlLast) + htmlLast.length()-2);
			if(htmlStr.contains("\\")){
				htmlStr = htmlStr.replace("\\", "");
			}
			info.setHtmlLink(htmlStr);
			Log.v("WebripNews", "Found HTML-Link for News: " + htmlStr);
			
			String newsNews = null;
			
			if(symbol.contains(imgSrc)){
//				newsNews = symbol.substring(symbol.indexOf(imgSrc)+ imgSrc.length()-1, symbol.length()-8);
//				Log.v("WebripNews", "Found News: " + newsNews);
//				info.setNews(newsNews);
			}else{
				newsNews = symbol.substring(symbol.indexOf(newsFirst)+ newsFirst.length(), symbol.length()-3);				
				Log.v("WebripNews", "Found News: " + newsNews);
				info.setNews(newsNews);
			}
			

			webRipStockNewsInfoList.add(info);

			Log.v("WebripNews", "Found News: " + symbol);

			String subs = newsub.replaceFirst(first, "");
			String subsss = subs.replaceFirst(last, "");
			subsss = subsss.replaceFirst(citeFirst, "");
			subsss = subsss.replaceFirst("###", "");

			newsub = subsss;
		}

		return webRipStockNewsInfoList;
	}



	@Override
	protected String doInBackground(String... params) {
		try {
			String line = null;
			String sub = null;

			URL url = new URL(params[0]);
			URLConnection connection;
			connection = url.openConnection();

			HttpURLConnection httpConnection = (HttpURLConnection)connection;
			int responseCode = httpConnection.getResponseCode();

			Log.v("YQL", "HTTP Response Code:" + responseCode);

			if (responseCode == HttpURLConnection.HTTP_OK) {

				InputStream in = httpConnection.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
				try {
					while ((line = br.readLine()) != null) {
//						Log.v("Webrip", "Got this String with Data: " + "<div class=\\\"yom-mod yom-top-story\\\" id=\\\"mediatopstorytemp\\\">");
						
						if(line.contains("<div class=\\\"yom-mod yom-top-story\\\" id=\\\"mediatopstory\\\">")){
							sub = line;
							Log.v("Webrip", "Got this String with Data: " + sub);
							break;
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				news = formatRippedString(sub);
				Log.v("Webrip", "Finished formatting!");

			}else{
				dismissDialog();
				error();
			}

		} catch (MalformedURLException e) {
			Log.d("WebRipAsyncTask", "MalformedURLException", e);
			dismissDialog();
			error();
		} catch (IOException e) {
			Log.d("WebRipAsyncTask", "IOException", e);
			dismissDialog();
			error();
		}finally{

		}
		return this.yqlQuery;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);

		Log.v("YQL", "Calling AsyncTask for this YqlQuery " + yqlQuery);

		dismissDialog();

		StockInfoAdapter secAdapter = null;
		
		ArrayList<ArrayAdapterable> toRemove = new ArrayList<ArrayAdapterable>();
		
		news.remove(news.size()-1);
		for(ArrayAdapterable adapterable : news){
			WebripStockNewsInfo infos = (WebripStockNewsInfo) adapterable;
			if(infos.getNews() == null){
				toRemove.add(infos);
			}
		}
		
		for(ArrayAdapterable adapterable : toRemove){
			WebripStockNewsInfo infos = (WebripStockNewsInfo) adapterable;
			if(news.contains(infos)){
				news.remove(infos);
			}
		}

		if(news != null){
			if(act instanceof MainActivity){
				StockInfoAdapter adapter = (StockInfoAdapter) view.getAdapter();
				if(adapter != null)
					adapter.clear();
				
				for(ArrayAdapterable infos : news){
					WebripStockNewsInfo web = (WebripStockNewsInfo) infos;
					Log.v("WebRip PostExecute", "Die tollen Objekte mit diesen Werten: " + web.getNews() + "   " + web.getHtmlLink());
				}
				
				secAdapter = new StockInfoAdapter(act, android.R.layout.simple_list_item_1, news, act, view);

			}else if(act instanceof DetailView){

			}
		}
		
		view.setOnItemClickListener(secAdapter);
		view.setAdapter(secAdapter);

	}

	private void error(){
		String error = act.getResources().getString(R.string.errorMsg);
		MyErrorToast.doToast(act, error, Toast.LENGTH_SHORT);		
	}

	private void dismissDialog(){
		if(act != null){
			pdia.dismiss();
		}
	}


}
