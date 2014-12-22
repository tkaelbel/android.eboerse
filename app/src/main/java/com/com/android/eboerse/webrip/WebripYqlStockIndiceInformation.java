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

import com.com.android.eboerse.main.ConnectionDetector;
import com.com.android.eboerse.main.MyErrorToast;
import com.com.android.eboerse.R;
import com.com.android.eboerse.main.SymbolsGoodToKnow;
import com.com.android.eboerse.detail.tabs.TabDetailListener;
import com.com.android.eboerse.search.YqlStockInformation;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

public class WebripYqlStockIndiceInformation extends AsyncTask<String, String, String>{

	private String yqlQuery = null;
	private ProgressDialog pdia;
	private Activity act = null;
	private ListView view = null;

	private ListView viewTop = null;
	private ListView viewBot = null;

	private String tab = null;

	public WebripYqlStockIndiceInformation(Activity mainact, ListView view, String tab) {
		this.act = mainact;
		this.view = view;
		this.tab = tab;
	}

	public WebripYqlStockIndiceInformation(Activity act, ListView top, ListView flop, String tab){
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

	private ArrayList<String> formatRippedString(String ripped){
		ArrayList<String> symbols = new ArrayList<String>();

		String first = "///";
		String last = "\">";

		String newsub = ripped.replace("<a href=\"/q?s=", first);

		while(newsub.contains(first)){

			int firstIdx = newsub.indexOf(first);
			int lastIdx = newsub.indexOf(last);

			if(firstIdx > lastIdx){
				newsub = newsub.replaceFirst(last, "");
				continue;
			}

			String symbol = newsub.substring(firstIdx+first.length(), lastIdx);

			symbols.add(symbol);
			Log.v("Webrip", "Found Symbol: " + symbol);

			String subs = newsub.replaceFirst(first, "");
			String subsss = subs.replaceFirst(last, "");

			newsub = subsss;

		}

		return symbols;
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
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				try {
					while ((line = br.readLine()) != null) {
						Log.v("Webrip", "Got this String with Data: " + line);
						if(line.contains("td class=\"yfnc_tabledata1\"")){
							sub = line.substring(line.indexOf("<a href=\"/q?s="), line.lastIndexOf("</a>"));
							Log.v("Webrip", "Got this String with Data: " + sub);
							break;
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				ArrayList<String> symbols = formatRippedString(sub);
				String yqlQuery = SymbolsGoodToKnow.YAHOO_URL_FIRST;

				for(String str : symbols){
					yqlQuery = yqlQuery + str + ",";
				}

				yqlQuery = yqlQuery + SymbolsGoodToKnow.YAHOO_URL_SECOND;
				this.yqlQuery = yqlQuery;

			}else{
				dismissDialog();
				error();
			}

		} catch (MalformedURLException e) {
			Log.d("WebRipAsyncTask", "MalformedURLException", e);
            Looper.prepare();
			dismissDialog();
			error();
		} catch (IOException e) {
			Log.d("WebRipAsyncTask", "IOException", e);
            Looper.prepare();
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
		YqlStockInformation stocks;

		if(yqlQuery != null){
			if(tab.equals(act.getResources().getString(R.string.detail_zusam_tab))){
				stocks = new YqlStockInformation(act, view, tab);
				if(ConnectionDetector.isConnectingToInternet(act)){
					stocks.execute(yqlQuery);
				}
			}else if(tab.equals(act.getResources().getString(R.string.detail_topflop_tab))){
				stocks = new YqlStockInformation(act, viewTop, viewBot, tab);
				if(ConnectionDetector.isConnectingToInternet(act)){
					stocks.execute(yqlQuery);
				}
			}
		}
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