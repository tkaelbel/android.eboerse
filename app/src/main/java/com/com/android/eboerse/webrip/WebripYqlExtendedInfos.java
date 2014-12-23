package com.com.android.eboerse.webrip;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.com.android.eboerse.R;
import com.com.android.eboerse.main.MainActivity;
import com.com.android.eboerse.main.MyErrorToast;
import com.com.android.eboerse.search.DetailView;
import com.com.android.eboerse.stock.ArrayAdapterable;
import com.com.android.eboerse.stock.StockInfoAdapter;
import com.com.android.eboerse.webrip.adapter.ExtendedInfosAdapter;
import com.com.android.eboerse.webrip.webrip.model.WebripExtendedInfos;
import com.com.android.eboerse.webrip.webrip.model.WebripStockComponentOf;
import com.com.android.eboerse.webrip.webrip.model.WebripStockNewsInfo;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by tok on 20.12.2014.
 */
public class WebripYqlExtendedInfos extends AsyncTask<String, String, String>{

    private ProgressDialog pdia;
    private Activity act = null;
    private TextView brancheValue = null;
    private TextView industrieValue = null;
    private ListView extendedInfos = null;
    private WebripExtendedInfos infos = new WebripExtendedInfos();
    private ArrayList<WebripStockComponentOf> componentOfs = new ArrayList<WebripStockComponentOf>();

    public WebripYqlExtendedInfos(Activity act, TextView brancheValue, TextView industrieValue, ListView extendedInfos){
        this.act = act;
        this.brancheValue = brancheValue;
        this.industrieValue = industrieValue;
        this.extendedInfos = extendedInfos;
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
    protected String doInBackground(String... strings) {
        try {
            String line = null;
            String sub = null;

            URL url = new URL(strings[0]);
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

                        if(line.contains("<td nowrap class=\"yfnc_tabledata1\">")) {
                            int count = 0;
                            while (line.contains("</td>")) {

                                int idxBranchFirst = line.indexOf("<td nowrap class=\"yfnc_tabledata1\">");
                                int idxBranchLast = line.indexOf("</td>");

                                if (idxBranchFirst > idxBranchLast) {
                                    line = line.replaceFirst("</td>", "");
                                    continue;
                                } else {
                                    if (count == 0) {
                                        infos.setBranche(line.substring(line.indexOf("<td nowrap class=\"yfnc_tabledata1\">") + "<td nowrap class=\"yfnc_tabledata1\">".length(), line.indexOf("</td>")));
                                    }

                                    if (count == 1) {
                                        infos.setIndustrie(line.substring(line.indexOf("<td nowrap class=\"yfnc_tabledata1\">") + "<td nowrap class=\"yfnc_tabledata1\">".length(), line.indexOf("</td>")));
                                    }
                                    line = line.replaceFirst("<td nowrap class=\"yfnc_tabledata1\">", "");
                                    line = line.replaceFirst("</td>", "");
                                    count++;
                                    if (count == 2)
                                        break;
                                }
                               // break;
                            }
                        }
                        if(infos.getBranche() != null && infos.getIndustrie() != null)
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.v("Webrip", "Finished formatting!");

            }else{
                dismissDialog();
                error();
            }
            ((HttpURLConnection) connection).disconnect();

            url = new URL(strings[1]);
            connection = url.openConnection();
            httpConnection = (HttpURLConnection)connection;
            responseCode = httpConnection.getResponseCode();

            Log.v("YQL", "HTTP Response Code:" + responseCode);


            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream in = httpConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                try {
                    while ((line = br.readLine()) != null) {
                        int count = 0;

                        if(line.contains("<td class=\"yfnc_tabledata1\">")) {
                            WebripStockComponentOf componentOf = null;
                            while (line.contains("<td class=\"yfnc_tabledata1\">") || line.contains("<td class=\"yfnc_tabledata1\" align=\"right\" nowrap><b><span id=\"") || line.contains("<b style=\"")){
                                int idxBranchFirst = line.indexOf("<td class=\"yfnc_tabledata1\">");
                                int idxBranchLast = line.indexOf("</td>");

                                if (idxBranchFirst > idxBranchLast) {
                                    line = line.replaceFirst("</td>", "");
                                    continue;
                                } else {
                                    if (count % 4 == 0) {
                                        componentOf = new WebripStockComponentOf();
                                        componentOf.setSymbol(line.substring(line.indexOf("<td class=\"yfnc_tabledata1\">") + "<td class=\"yfnc_tabledata1\">".length(), line.indexOf("</td>")));
                                        line = line.replaceFirst("<td class=\"yfnc_tabledata1\">", "");
                                        line = line.replaceFirst("</td>", "");
                                    }

                                    if (count % 4 == 1) {
                                        componentOf.setName(line.substring(line.indexOf("<td class=\"yfnc_tabledata1\">") + "<td class=\"yfnc_tabledata1\">".length(), line.indexOf("</td>")));
                                        line = line.replaceFirst("<td class=\"yfnc_tabledata1\">", "");
                                        line = line.replaceFirst("</td>", "");
                                    }

                                    if (count % 4 == 2) {
                                        componentOf.setLetzterKurs(line.substring(line.indexOf("<td class=\"yfnc_tabledata1\" align=\"right\" nowrap><b><span id=\"") + "<td class=\"yfnc_tabledata1\" align=\"right\" nowrap><b><span id=\"".length(), line.indexOf("</td>")));
                                        line = line.replaceFirst("<td class=\"yfnc_tabledata1\" align=\"right\" nowrap><b><span id=\"", "");
                                        line = line.replaceFirst("</td>", "");
                                    }

                                    if (count % 4 == 3) {
                                        int idxB = 0;
                                        int idxBlast = 0;

                                        while(line.contains("</b>")){

                                            idxB = line.indexOf("<b style=\"");
                                            idxBlast = line.indexOf("</b>");

                                            if(idxB > idxBlast){
                                                line = line.replaceFirst("</b>", "");
                                            }else{
                                                break;
                                            }
                                        }
                                        componentOf.setVeraenderung(line.substring(line.indexOf("<b style=\"") + "<b style=\"".length(), line.indexOf("</b>")));
                                        line = line.replaceFirst("<b style=\"", "");
                                        line = line.replaceFirst("</b>", "");
                                        componentOfs.add(componentOf);
                                    }
                                    count++;
                                }
                                //break;
                            }
                        }
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
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
        return null;
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        dismissDialog();

        infos.setComponentOf(componentOfs);

        brancheValue.setTextSize(12);
        industrieValue.setTextSize(12);
        brancheValue.setText(StockInfoAdapter.utf8Shit(infos.getBranche()));
        industrieValue.setText(StockInfoAdapter.utf8Shit(infos.getIndustrie()));

        ExtendedInfosAdapter adapter3 = (ExtendedInfosAdapter) extendedInfos.getAdapter();
        if(adapter3 != null)
            adapter3.clear();

        ExtendedInfosAdapter adapter = new ExtendedInfosAdapter(act, android.R.layout.simple_list_item_1, infos.getComponentOf(), act, extendedInfos);

        extendedInfos.setAdapter(adapter);
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
