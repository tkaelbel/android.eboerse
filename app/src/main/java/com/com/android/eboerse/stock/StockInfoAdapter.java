package com.com.android.eboerse.stock;
/**
 * listview adapter fuer alle anzeigen
 * bei der watchlist wird ein imagebutton zum editieren hinzugefuegt
 */
import java.nio.charset.MalformedInputException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.com.android.eboerse.main.ConnectionDetector;
import com.com.android.eboerse.main.MainActivity;
import com.com.android.eboerse.R;
import com.com.android.eboerse.main.SymbolsGoodToKnow;
import com.com.android.eboerse.database.DatabaseHandler;
import com.com.android.eboerse.database.Favorit;
import com.com.android.eboerse.search.DetailView;
import com.com.android.eboerse.webrip.WebripYqlTopNews;
import com.com.android.eboerse.webrip.webrip.model.WebripStockNewsInfo;


public class StockInfoAdapter extends ArrayAdapter<ArrayAdapterable> implements OnItemClickListener{
    private ArrayList<ArrayAdapterable> items;
    private StockInfoViewHolder stockInfoHolder;
    private Activity act;
    private ListView list;
    private String tab;
    private ListView topList;
    private ListView flopList;

    private SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

    //Dialog komponenten
    private Dialog alarmDialog;
    private TextView aktuell;
    private EditText oberGrenze;
    private EditText unterGrenze;
    private Button btnOk;
    private Button btnCancel;

    private Favorit favo;
    private int position;

    private DatabaseHandler db;

    private class StockInfoViewHolder {
        TextView name;
        TextView infos;
        ImageButton button;
    }

    public StockInfoAdapter(Context context, int tvResId, ArrayList<ArrayAdapterable> items, Activity activity, ListView list) {
        super(context, tvResId, items);
        this.items = items;
        this.act = activity;
        this.list = list;
    }

    public StockInfoAdapter(Context context, int tvResId, ArrayList<ArrayAdapterable> items, Activity activity, ListView list, String tab) {
        super(context, tvResId, items);
        this.items = items;
        this.act = activity;
        this.list = list;
        this.tab = tab;
    }

    public StockInfoAdapter(Context context, int tvResId, ArrayList<ArrayAdapterable> items, Activity activity, ListView topList, ListView flopList, String tab){
        super(context, tvResId, items);
        this.items = items;
        this.act = activity;
        this.topList = topList;
        this.flopList = flopList;
        this.tab = tab;
    }



    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        position = pos;
        View v = convertView;

        if(items != null && items.size() > 0){
            Object o = items.get(0);
            if(o.getClass().equals(StockInfo.class)){
                if(act instanceof MainActivity){
                    if(tab != null){
                        if(tab.equals(act.getResources().getString(R.string.watch_tab))){
                            if (v == null) {
                                LayoutInflater vi = (LayoutInflater) act.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                                v = vi.inflate(R.layout.listview_items_uebersicht_watchlist, null);
                                stockInfoHolder = new StockInfoViewHolder();
                                stockInfoHolder.name = (TextView)v.findViewById(R.id.stock_name);
                                stockInfoHolder.infos = (TextView)v.findViewById(R.id.stock_infos);
                                stockInfoHolder.button = (ImageButton)v.findViewById(R.id.editRange);
                                stockInfoHolder.button.setTag(stockInfoHolder);

                                stockInfoHolder.button.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        editRange(v);
                                    }
                                });

                                v.setTag(stockInfoHolder);
                            }else stockInfoHolder = (StockInfoViewHolder)v.getTag();

                            StockInfo stock = (StockInfo) items.get(pos);

                            if (stock != null) {
                                stockInfoHolder.name.setText(stock.getName());
                                stockInfoHolder.infos.setText(stock.getInfos());
                            }
                        }
                    }else{
                        if (v == null) {
                            LayoutInflater vi = (LayoutInflater) act.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                            v = vi.inflate(R.layout.listview_items_uebersicht, null);
                            stockInfoHolder = new StockInfoViewHolder();
                            stockInfoHolder.name = (TextView)v.findViewById(R.id.stock_name);
                            stockInfoHolder.infos = (TextView)v.findViewById(R.id.stock_infos);
                            v.setTag(stockInfoHolder);
                        } else stockInfoHolder = (StockInfoViewHolder)v.getTag();

                        StockInfo stock = (StockInfo) items.get(pos);

                        if (stock != null) {
                            stockInfoHolder.name.setText(stock.getName());
                            stockInfoHolder.infos.setText(stock.getInfos());
                        }
                    }
                }else if(act instanceof DetailView){
                    if(tab != null){
                        if(tab.equals(act.getResources().getString(R.string.detail_zusam_tab)) || tab.equals(act.getResources().getString(R.string.detail_topflop_tab))){
                            if(v == null){
                                LayoutInflater vi = (LayoutInflater) act.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                                v = vi.inflate(R.layout.listview_items_uebersicht, null);
                                stockInfoHolder = new StockInfoViewHolder();
                                stockInfoHolder.name = (TextView)v.findViewById(R.id.stock_name);
                                stockInfoHolder.infos = (TextView)v.findViewById(R.id.stock_infos);
                                v.setTag(stockInfoHolder);
                            }else stockInfoHolder = (StockInfoViewHolder)v.getTag();

                            StockInfo stock = (StockInfo) items.get(pos);

                            if(stock != null){
                                stockInfoHolder.name.setText(stock.getName());
                                Date d = new Date();
                                String date = format1.format(d);
                                stockInfoHolder.infos.setText(date + "       " + stock.getChange() + "      " + stock.getLastTradePriceOnly());
                            }
                        }
                    }
                }
            }else if(o.getClass().equals(WebripStockNewsInfo.class)){
                if(act instanceof MainActivity){
                    if(v == null){
                        LayoutInflater vi = (LayoutInflater) act.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                        v = vi.inflate(R.layout.listview_items_uebersicht, null);
                        stockInfoHolder = new StockInfoViewHolder();
                        stockInfoHolder.name = (TextView)v.findViewById(R.id.stock_name);
                        stockInfoHolder.infos = (TextView)v.findViewById(R.id.stock_infos);
                        v.setTag(stockInfoHolder);
                    } else stockInfoHolder = (StockInfoViewHolder)v.getTag();

                    WebripStockNewsInfo stock = (WebripStockNewsInfo) items.get(pos);

                    if (stock != null) {
                        stockInfoHolder.name.setText(utf8Shit(stock.getNews()));
                        stockInfoHolder.name.setTypeface(null, Typeface.BOLD);
                        if(stock.getTimeAndSite() == null){
                            stockInfoHolder.infos.setText("N/A");
                        }else{
                            stockInfoHolder.infos.setText(stock.getTimeAndSite());
                        }

                        stockInfoHolder.infos.setTextSize(8);
                        stockInfoHolder.infos.setTypeface(null, Typeface.ITALIC);
                    }
                }else if(act instanceof DetailView){
                    if(v == null){
                        LayoutInflater vi = (LayoutInflater) act.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                        v = vi.inflate(R.layout.listview_items_uebersicht, null);
                        stockInfoHolder = new StockInfoViewHolder();
                        stockInfoHolder.name = (TextView)v.findViewById(R.id.stock_name);
                        stockInfoHolder.infos = (TextView)v.findViewById(R.id.stock_infos);
                        v.setTag(stockInfoHolder);
                    } else stockInfoHolder = (StockInfoViewHolder)v.getTag();

                    WebripStockNewsInfo stock = (WebripStockNewsInfo) items.get(pos);

                    if (stock != null) {
                        stockInfoHolder.name.setText(utf8Shit(stock.getTimeAndSite()));
                        stockInfoHolder.name.setTypeface(null, Typeface.BOLD);
                        if(stock.getNews() == null){
                            stockInfoHolder.infos.setText("N/A");
                        }else{
                            stockInfoHolder.infos.setText(stock.getNews());
                        }

                        stockInfoHolder.infos.setTextSize(10);
                        stockInfoHolder.infos.setTypeface(null, Typeface.ITALIC);
                    }
                }
            }
        }

        return v;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Object obj = list.getItemAtPosition(arg2);
        if(obj instanceof StockInfo){
            StockInfo rst = (StockInfo) obj;
            showDetailView(rst.getSymbol());
        }else if(obj instanceof  WebripStockNewsInfo){
            if(act instanceof MainActivity){
                WebripStockNewsInfo webripStockNewsInfo = (WebripStockNewsInfo) obj;
                String url = null;
                if(webripStockNewsInfo.getHtmlLink() != null)
                    url = SymbolsGoodToKnow.YAHOO_URL_NEWS_DETAIL + webripStockNewsInfo.getHtmlLink();
                String html = utf8Shit(url);
                WebripYqlTopNews ripDetailNews = new WebripYqlTopNews(act, webripStockNewsInfo);
                if(ConnectionDetector.isConnectingToInternet(act)){
                    ripDetailNews.execute(html);
                }
            }else if(act instanceof DetailView){
                WebripStockNewsInfo webripStockNewsInfo = (WebripStockNewsInfo) obj;
                String html = null;
                if(webripStockNewsInfo.getHtmlLink() != null)
                   html = utf8Shit(webripStockNewsInfo.getHtmlLink());
                WebripYqlTopNews ripDetailNews = new WebripYqlTopNews(act, webripStockNewsInfo);
                if(ConnectionDetector.isConnectingToInternet(act)){
                    ripDetailNews.execute(html);
                }
            }

        }

    }

    private void showDetailView(String symbol){
        Intent intent = new Intent(act, DetailView.class);
        intent.putExtra("symbol", symbol);
        act.startActivity(intent);
    }


    public void editRange(View v){
        StockInfoViewHolder infoView = (StockInfoViewHolder) v.getTag();
        initDb();
        String infos = (String) infoView.infos.getText();
        String[] info = infos.split("      ");
        showTheDialog((String) infoView.name.getText(), info[1]);
    }

    private void initDb(){
        db = new DatabaseHandler(act);
    }

    private void showTheDialog(String nameAndSymbol, String infos){
        alarmDialog = new Dialog(act);
        alarmDialog.setContentView(R.layout.dialog_layout);

        aktuell = (TextView) alarmDialog.findViewById(R.id.textViewAktuell);
        oberGrenze = (EditText) alarmDialog.findViewById(R.id.editTextObergrenze);
        unterGrenze = (EditText) alarmDialog.findViewById(R.id.editTextUntergrenze);
        btnOk = (Button) alarmDialog.findViewById(R.id.btn_ok);
        btnCancel = (Button) alarmDialog.findViewById(R.id.btn_cancel);

        favo = db.getFavoritByName(nameAndSymbol);
        oberGrenze.setText(favo.getObereGrenze());
        unterGrenze.setText(favo.getUntereGrenze());

        aktuell.setText(infos);
        alarmDialog.setTitle(favo.getKurs_Name());

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ober = oberGrenze.getText().toString();
                String unter = unterGrenze.getText().toString();
                String akt = aktuell.getText().toString();

                Double unterDouble;
                Double oberDouble;
                Double aktuellDouble;

                aktuellDouble = Double.valueOf(akt);

                if(unter.equals("")){
                    unterDouble = 0.0;
                }else{
                    unterDouble = Double.parseDouble(ober);
                }

                if(ober.equals("")){
                    oberDouble = 0.0;
                }else{
                    oberDouble = Double.parseDouble(ober);
                }

                if(oberDouble >= aktuellDouble || unterDouble <= aktuellDouble){
                    favo.setObereGrenze(ober);
                    favo.setUntereGrenze(unter);
                    db.updateFavorit(favo);
                    alarmDialog.dismiss();
                    db.close();
                }
            }
        });

        btnCancel.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                alarmDialog.dismiss();
            }
        });

        alarmDialog.show();
    }

    private String utf8Shit(String stringToCheck){
        if(stringToCheck.contains(SymbolsGoodToKnow.UTF_8_ae)){
            stringToCheck = stringToCheck.replace(SymbolsGoodToKnow.UTF_8_ae, "ä");
        }
        if(stringToCheck.contains(SymbolsGoodToKnow.UTF_8_oe)){
            stringToCheck = stringToCheck.replace(SymbolsGoodToKnow.UTF_8_oe, "ö");
        }
        if(stringToCheck.contains(SymbolsGoodToKnow.UTF_8_ue)){
            stringToCheck = stringToCheck.replace(SymbolsGoodToKnow.UTF_8_ue, "ü");
        }
        if(stringToCheck.contains(SymbolsGoodToKnow.UTF_8_AE)){
            stringToCheck = stringToCheck.replace(SymbolsGoodToKnow.UTF_8_AE, "Ä");
        }
        if(stringToCheck.contains(SymbolsGoodToKnow.UTF_8_OE)){
            stringToCheck = stringToCheck.replace(SymbolsGoodToKnow.UTF_8_OE, "Ö");
        }
        if(stringToCheck.contains(SymbolsGoodToKnow.UTF_8_UE)){
            stringToCheck = stringToCheck.replace(SymbolsGoodToKnow.UTF_8_UE, "Ü");
        }
        if(stringToCheck.contains(SymbolsGoodToKnow.UTF_8_ss)){
            stringToCheck = stringToCheck.replace(SymbolsGoodToKnow.UTF_8_ss, "ß");
        }
        if(stringToCheck.contains(SymbolsGoodToKnow.UTF_8_RP)){
            stringToCheck = stringToCheck.replace(SymbolsGoodToKnow.UTF_8_RP, "«");
        }
        if(stringToCheck.contains(SymbolsGoodToKnow.UTF_8_LP)){
            stringToCheck = stringToCheck.replace(SymbolsGoodToKnow.UTF_8_LP, "»");
        }

        if(stringToCheck.contains("\\")){
            stringToCheck = stringToCheck.replace("\\", "");
        }
        return stringToCheck;
    }

}