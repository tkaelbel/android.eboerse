package com.com.android.eboerse.webrip;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.com.android.eboerse.main.MyErrorToast;
import com.com.android.eboerse.R;
import com.com.android.eboerse.webrip.webrip.model.WebripStockNews;
import com.com.android.eboerse.webrip.webrip.model.WebripStockNewsInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by tok on 10.12.2014.
 */
public class WebripYqlTopNews extends AsyncTask<String, String, String> {

    private ProgressDialog pdia;
    private Activity act = null;
    private WebripStockNews news = new WebripStockNews();
    private WebripStockNewsInfo info;
    private static final String first = "<p>";
    private static final String last = "</p>";
    private static final String imgSrcFirst = "<img";
    private static final String imgSrcLast = "\" width";
    private static final String b = "<b>";
    private static final String endb = "</b>";

    private int response = 0;


    public WebripYqlTopNews(Activity mainact, WebripStockNewsInfo info) {
        this.act = mainact;
        this.info = info;
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

            response = responseCode;
            Log.v("YQL", "HTTP Response Code:" + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {

                InputStream in = httpConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
                String nachricht = new String();

                try {
                    while ((line = br.readLine()) != null) {
                        Log.v("Webrip", "Now Reading: " + line);
                        if(line.contains("<p")) {
                            if (line.contains(imgSrcFirst) && news.getImg() == null) {
                                int idxSrc = 0;
                                int idxSrcEnd = 0;
                                if(line.contains("style=\"background-image:url('")){
                                    idxSrc = line.indexOf("style=\"background-image:url('");
                                    if(line.contains(".JPG');")){
                                        idxSrcEnd = line.indexOf(".JPG');");
                                    }else {
                                        idxSrcEnd = line.indexOf(".JPG');");
                                    }
                                    news.setImg(line.substring(idxSrc + "style=\"background-image:url('".length(), idxSrcEnd + ".JPG');".length()));
                                }else{
                                    idxSrc = line.indexOf("src=\"");
                                    idxSrcEnd = line.indexOf(".jpg");
                                }

                                news.setImg(line.substring(idxSrc + "src=\"".length(), idxSrcEnd + ".jpg".length()));
                                Log.v("Webrip", "Found Image for News: " + news.getImg());
                            }

                            if (line.contains(b)) {
                                int intFirstIdx = line.indexOf(b);
                                int intLastIdx = line.indexOf(endb);

                                news.setHeader(line.substring(intFirstIdx + b.length(), intLastIdx));
                                Log.v("Webrip", "Found Header for News: " + news.getHeader());
                            }



                            if (line.contains(first) && !line.contains("div") && !line.contains("<a href")) {
                                int idxFirst = line.indexOf(first);
                                int idxLast = line.indexOf(last);

                                if (idxFirst < idxLast) {
                                    nachricht = nachricht + line.substring(idxFirst + first.length(), idxLast) + "\n";
                                    news.setNews(nachricht);
                                    Log.v("Webrip", "Found Body for News: " + news.getNews());
                                }

                            }
                        }


                        if(line.contains("id=\"mediaarticlebody\"")){
                            String newsNews = new String();
                            while(line.contains("<p>")) {
                                System.out.println(line);
                                int idxFirst = line.indexOf("<p>");
                                int idxLast = line.indexOf("</p>");

                                if (idxFirst < idxLast) {
                                    newsNews = newsNews + line.substring(idxFirst + "<p>".length(), idxLast) + "\n";
                                    line = line.replaceFirst("<p>", "");
                                    line = line.replaceFirst("</p>", "");
                                } else {
                                    line = line.replaceFirst("</p>", "");
                                }

                                if(idxLast == -1){
                                    newsNews = newsNews + line.substring(idxFirst + "<p>".length(), line.length());
                                    break;
                                }
                                System.out.println(newsNews);
                            }
                            news.setNews(newsNews);
                        }
                        if(line.contains("<h1 class=\"headline\">")){
                            news.setHeaderFull(line.substring(line.indexOf("<h1 class=\"headline\">") + "<h1 class=\"headline\">".length() , line.indexOf("</h1>")));
                        }

                        if(line.contains("<span class=\"provider org\">")){
                            while(line.contains("</span>")){
                                if(line.indexOf("<span class=\"provider org\">") < line.indexOf("</span>")){
                                    news.setFrom(line.substring(line.indexOf("<span class=\"provider org\">") + "<span class=\"provider org\">".length() , line.indexOf("</span>")));
                                    break;
                                }else{
                                    line = line.replaceFirst("</span>", "");
                                }
                            }

                        }

                        if(news.getNews() != null && news.getImg() != null && news.getHeaderFull() != null){
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else{
                Looper.prepare();
                dismissDialog();
                error("Fehler beim Zugriff auf die Webseite!");
            }

        } catch (MalformedURLException e) {
            Log.d("WebRipAsyncTask", "MalformedURLException", e);
            dismissDialog();
            error("Fehler in der URL!");
        } catch (IOException e) {
            Log.d("WebRipAsyncTask", "IOException", e);
            dismissDialog();
            error("Fehler beim Lesen der Webseite!");
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        Log.v("YQL", "Calling AsyncTask for this YqlQuery ");

        dismissDialog();
        news.setTime(info.getTimeAndSite());

        if(response != HttpURLConnection.HTTP_OK){
            error("Nachricht existiert nicht mehr!");
        }else{
            showDetailNewsView();
        }
    }

    private void showDetailNewsView(){
        Intent intent = new Intent(act, DetailNewsActivity.class);
        intent.putExtra("header", news.getHeader());
        intent.putExtra("img", news.getImg());
        intent.putExtra("body", news.getNews());
        intent.putExtra("fullheader", news.getHeaderFull());
        intent.putExtra("time", news.getTime());
        act.startActivity(intent);
    }

    private void error(String message){
        String error = message;
        MyErrorToast.doToast(act, error, Toast.LENGTH_SHORT);
    }

    private void dismissDialog(){
        if(act != null){
            pdia.dismiss();
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        dismissDialog();
    }
}
