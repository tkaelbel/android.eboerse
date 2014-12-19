package com.com.android.eboerse.webrip;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.com.android.eboerse.R;
import com.com.android.eboerse.main.MainActivity;
import com.com.android.eboerse.main.MyErrorToast;
import com.com.android.eboerse.search.DetailView;
import com.com.android.eboerse.stock.ArrayAdapterable;
import com.com.android.eboerse.stock.StockInfoAdapter;
import com.com.android.eboerse.webrip.webrip.model.WebripStockNewsInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by tok on 15.12.2014.
 */
public class WebripYqlDetailNews extends AsyncTask<String, String, String>{

    private String yqlQuery = null;
    private ProgressDialog pdia;
    private Activity act = null;
    private ListView view = null;

    private ListView viewTop = null;
    private ListView viewBot = null;

    private String tab = null;
    private ArrayList<ArrayAdapterable> news = null;

    public WebripYqlDetailNews(Activity mainact, ListView view, String tab) {
        this.act = mainact;
        this.view = view;
        this.tab = tab;
    }

    public WebripYqlDetailNews(Activity act, ListView top, ListView flop, String tab){
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

        while(ripped.contains("<span>")){
            info = new WebripStockNewsInfo();

            int idxTimeFirst = ripped.indexOf("<h3>");
            int idxTimeLast = ripped.indexOf("</h3>");

            int idxTimeFirstDay = ripped.indexOf("<span>");
            int idxTimeLastDay = ripped.indexOf("</span>");

            int idxHtmlFirst = ripped.indexOf("<a href=\"");
            int idxHtmlLast = ripped.indexOf(".html\">");

            int idxNewsFirst = ripped.indexOf(".html\">");
            int idxNewsLast = ripped.indexOf("</a>");

            info.setTimeAndSite(ripped.substring(idxTimeFirst + "<h3>".length() + "<span>".length() , idxTimeLast - "</span>".length()));
            Log.v("Webrip", "Got this String with Data: " + info.getTimeAndSite());
            info.setHtmlLink(ripped.substring(idxHtmlFirst + "<a href=\"".length(), idxHtmlLast + ".html\">".length()-2));
            Log.v("Webrip", "Got this String with Data: " + info.getHtmlLink());
            info.setNews(ripped.substring(idxNewsFirst + ".html\">".length(), idxNewsLast));
            Log.v("Webrip", "Got this String with Data: " + info.getNews());

            ripped = ripped.replaceFirst("<h3>", "");
            ripped = ripped.replaceFirst("</h3>", "");
            ripped = ripped.replaceFirst("<a href=\"", "");
            ripped = ripped.replaceFirst(".html\">", "");
            ripped = ripped.replaceFirst("</a>", "");
            ripped = ripped.replaceFirst("<span>", "");
            ripped = ripped.replaceFirst("</span>", "");

            webRipStockNewsInfoList.add(info);
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
                        if(line.contains("<div class=\"mod yfi_quote_headline withsky\">")){
                            sub = line.substring(line.indexOf("<div class=\"mod yfi_quote_headline withsky\">" ), line.length());
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

        if(news != null){
                StockInfoAdapter adapter = (StockInfoAdapter) view.getAdapter();
                if(adapter != null)
                    adapter.clear();

                secAdapter = new StockInfoAdapter(act, android.R.layout.simple_list_item_1, news, act, view);
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
