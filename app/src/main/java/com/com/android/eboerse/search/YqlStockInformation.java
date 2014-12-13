package com.com.android.eboerse.search;
/**
 * Async task zum holen der Kursdaten von YQL
 */
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.com.android.eboerse.main.MainActivity;
import com.com.android.eboerse.main.MyErrorToast;
import com.com.android.eboerse.R;
import com.com.android.eboerse.stock.ArrayAdapterable;
import com.com.android.eboerse.stock.StockInfo;
import com.com.android.eboerse.stock.StockInfoAdapter;

public class YqlStockInformation extends AsyncTask<String, String, String>{

	private static final String TAG = "STOCKQUOTE";

	// XML node keys
	static final String KEY_ITEM = "quote"; // parent node
	static final String KEY_NAME = "Name";
	static final String KEY_YEAR_LOW = "YearLow";
	static final String KEY_YEAR_HIGH = "YearHigh";
	static final String KEY_DAYS_LOW = "DaysLow";
	static final String KEY_DAYS_HIGH = "DaysHigh";
	static final String KEY_LAST_TRADE_PRICE = "LastTradePriceOnly";
	static final String KEY_CHANGE = "Change";
	static final String KEY_DAYS_RANGE = "DaysRange";
	static final String KEY_STOCK_EXC = "StockExchange";
	static final String KEY_SYMBOL = "Symbol";

	// XML Data to Retrieve
	String name = "";
	String yearLow = "";
	String yearHigh = "";
	String daysLow = "";
	String daysHigh = "";
	String lastTradePriceOnly = "";
	String change = "";
	String daysRange = "";
	String dateTime = null;
	String stockExchange = "";
	String symbol = "";

	private ArrayList<ArrayAdapterable> stock = new ArrayList<ArrayAdapterable>();
	private ArrayList<ArrayAdapterable> flopArray = new ArrayList<ArrayAdapterable>();
	private ArrayList<ArrayAdapterable> topArray = new ArrayList<ArrayAdapterable>();
	
	private Activity act;
	private ListView list;
	
	private ListView topList;
	private ListView flopList;

	private TextView symbolNameAndName;
	private TextView pointsAndDate;
	private TextView pointsAndPercent;
	private TextView eroeffnung;
	private TextView hoch;
	private TextView hoch52w;
	private TextView vortag;
	private TextView tief;
	private TextView tief52w;

	private String tab = null;

	private ProgressDialog pdia;

	private StockInfo detailStock;

	public YqlStockInformation(Activity act, ListView list) {
		this.act = act;
		this.list = list;
	}

	public YqlStockInformation(Activity act, TextView symbolNameAndName, TextView pointsAndDate, TextView pointsAndPercent, TextView eroeffnung,
			TextView hoch, TextView hoch52w, TextView vortag, TextView tief, TextView tief52w){
		this.act = act;
		this.symbolNameAndName = symbolNameAndName;
		this.pointsAndDate = pointsAndDate;
		this.pointsAndPercent = pointsAndPercent;
		this.eroeffnung = eroeffnung;
		this.hoch = hoch;
		this.hoch52w = hoch52w;
		this.vortag = vortag;
		this.tief = tief;
		this.tief52w = tief52w;
	}

	public YqlStockInformation(Activity act, ListView list, String tab) {
		this.act = act;
		this.list = list;
		this.tab = tab;
	}
	
	public YqlStockInformation(Activity act, ListView topList, ListView flopList, String tab){
		this.act = act;
		this.topList = topList;
		this.flopList = flopList;
		this.tab = tab;		
	}


	private String getTextValue(Element entry, String tagName){

		String tagValueToReturn = null;

		NodeList nl = entry.getElementsByTagName(tagName);

		if(nl != null && nl.getLength() > 0){

			Element element = (Element) nl.item(0);

			if(element == null){
				return tagValueToReturn;
			}else{
				if(element.getFirstChild() == null){
					return tagValueToReturn;
				}else{
					tagValueToReturn = element.getFirstChild().getNodeValue();
				}
			}
		}

		return tagValueToReturn;

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

			URL url = new URL(params[0]);
			URLConnection connection;
			connection = url.openConnection();

			HttpURLConnection httpConnection = (HttpURLConnection)connection;
			int responseCode = httpConnection.getResponseCode();

			Log.v("YQL", "HTTP Response Code:" + responseCode);

			if (responseCode == HttpURLConnection.HTTP_OK) {
				InputStream in = httpConnection.getInputStream();
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();

				Document dom = db.parse(in);

				Element docEle = dom.getDocumentElement();

				NodeList nl = docEle.getElementsByTagName(KEY_ITEM);
				Log.v("YQL", "Nodelist:" + nl  + " " + nl.getLength());
				if (nl != null && nl.getLength() > 0) {
					for (int i = 0 ; i < nl.getLength(); i++) {

						StockInfo theStock = getStockInformation((Element) nl.item(i));

						daysLow = theStock.getDaysLow();
						daysHigh = theStock.getDaysHigh();
						yearLow = theStock.getYearLow();
						yearHigh = theStock.getYearHigh();
						name = theStock.getName();
						lastTradePriceOnly = theStock.getLastTradePriceOnly();
						change = theStock.getChange();
						daysRange = theStock.getDaysRange();
						stockExchange = theStock.getStockExchange();
						symbol = theStock.getSymbol();

						Log.d(TAG, "Stock Name " + name);
						Log.d(TAG, "Stock Year High " + yearHigh);
						Log.d(TAG, "Stock Year Low " + yearLow);
						Log.d(TAG, "Stock Days High " + daysHigh);
						Log.d(TAG, "Stock Days Low " + daysLow);
					}
					//				}else if(nl.getLength() == 0){
					//					error();
				}
			}else{
				dismissDialog();
				error();
			}
		} catch (MalformedURLException e) {
			Log.d(TAG, "MalformedURLException", e);
			dismissDialog();
			error();
		} catch (IOException e) {
			Log.d(TAG, "IOException", e);
			dismissDialog();
			error();
		} catch (ParserConfigurationException e) {
			Log.d(TAG, "Parser Configuration Exception", e);
			dismissDialog();
			error();
		} catch (SAXException e) {
			Log.d(TAG, "SAX Exception", e);
			dismissDialog();
			error();
		}
		finally {
		}

		return null;
	}

	private StockInfo getStockInformation(Element entry){

		String stockName = getTextValue(entry, KEY_NAME);
		String stockYearLow = getTextValue(entry, KEY_YEAR_LOW);
		String stockYearHigh = getTextValue(entry, KEY_YEAR_HIGH);
		String stockDaysLow = getTextValue(entry, KEY_DAYS_LOW);
		String stockDaysHigh = getTextValue(entry, KEY_YEAR_HIGH);
		String stocklastTradePriceOnlyTextView = getTextValue(entry, KEY_LAST_TRADE_PRICE);
		String stockChange = getTextValue(entry, KEY_CHANGE);
		String stockDaysRange = getTextValue(entry, KEY_DAYS_RANGE);
		String stockExchange = getTextValue(entry, KEY_STOCK_EXC);
		String symbol = getTextValue(entry, KEY_SYMBOL);


		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		DateFormat formatter = new SimpleDateFormat();
		dateTime = formatter.format(date);

		StockInfo theStock = new StockInfo(stockDaysLow, stockDaysHigh, stockYearLow,
				stockYearHigh, stockName, stocklastTradePriceOnlyTextView,
				stockChange, stockDaysRange, dateTime, stockExchange, symbol);

		stock.add(theStock);
		
		if(flopList != null && topList != null){
			if(theStock.getChange() != null && !theStock.getChange().contains("N/A")){
				Log.d("Indice", "OK");
				if(theStock.getChange().contains("+")){
					String col2WithOutPlus = theStock.getChange().replace("+", "");
					if(Double.valueOf(col2WithOutPlus) > 0){
						topArray.add(theStock);
					}
				}
			}

			if(theStock.getChange() != null && !theStock.getChange().contains("+") && !theStock.getChange().contains("N/A")){
				Log.d("Indice", "OK");
				if(Double.valueOf(theStock.getChange()) < 0){
					flopArray.add(theStock);
				}
			}

		}
		
		return theStock;

	}

	@Override
	protected void onPostExecute(String result) {	
		super.onPostExecute(result);
		
		Log.v("YQL", "Stock Size: " + stock.size());

		dismissDialog();

		if(act instanceof MainActivity){
			StockInfoAdapter adapter3 = (StockInfoAdapter) list.getAdapter();
			if(adapter3 != null)
				adapter3.clear();

			StockInfoAdapter adapter = null;

			if(tab != null){
				if(tab.equals(act.getResources().getString(R.string.watch_tab)))
					adapter = new StockInfoAdapter(act,android.R.layout.simple_list_item_1,stock,act,list,tab);
			}else{
				adapter = new StockInfoAdapter(act,
						android.R.layout.simple_list_item_1, stock, act, list);
			}

			list.setOnItemClickListener(adapter);
			list.setAdapter(adapter);
		}

		if(act instanceof DetailView){
			StockInfo stockInfo;
			if(stock.size() == 0){
				return;
			}

			if(stock.size() > 0 && stock.size() <= 1){
				stockInfo = (StockInfo) stock.get(0);
                act.getActionBar().setTitle(stockInfo.getName());
				symbolNameAndName.setText(stockInfo.getName() + "\r\n" + stockInfo.getSymbol());
				pointsAndDate.setText(stockInfo.getLastTradePriceOnly() + "\r\n" + stockInfo.getDateTime());

				if(stockInfo.getLastTradePriceOnly() != null && stockInfo.getChange() != null) {
					double perc = (100 / Double.valueOf(stockInfo.getLastTradePriceOnly())) * Double.valueOf(stockInfo.getChange());
					pointsAndPercent.setText(stockInfo.getChange() + "\r\n" + String.format("%.2f", perc));
				}

				if(stockInfo.getYearHigh() == null){
					//				hoch52w.setText("0");
				}else{
					hoch52w.setText(hoch52w.getText() + ": "+  stockInfo.getYearHigh());
				}

				if(stockInfo.getYearLow() == null){
					//				tief52w.setText("0");
				}else{
					tief52w.setText(tief52w.getText() + ": "+ stockInfo.getYearLow());
				}

				if(stockInfo.getDaysHigh() == null){
					//				hoch.setText("0");
				}else{
					hoch.setText(hoch.getText() + ": "+ stockInfo.getDaysHigh());
				}

				if(stockInfo.getDaysLow() == null){
					//				tief.setText("0");
				}else{
					tief.setText(tief.getText() + ": "+ stockInfo.getDaysLow());
				}

				setStockInfo(stockInfo);
			}else if(stock.size() > 1 && tab.equals(act.getResources().getString(R.string.detail_zusam_tab))){
				StockInfoAdapter adapter = new StockInfoAdapter(act, android.R.layout.simple_list_item_1, stock, act, list, tab);
				list.setAdapter(adapter);
				list.setOnItemClickListener(adapter);
			}else if(flopArray != null && topArray != null && tab.equals(act.getResources().getString(R.string.detail_topflop_tab))){
				Collections.sort(topArray, new Comparator<ArrayAdapterable>() {
					@Override
					public int compare(ArrayAdapterable lhs, ArrayAdapterable rhs) {
						return ((StockInfo) lhs).getChange().compareTo(((StockInfo) rhs).getChange());
					}
				});

				Collections.sort(flopArray, new Comparator<ArrayAdapterable>() {
					@Override
					public int compare(ArrayAdapterable lhs, ArrayAdapterable rhs) {
						return ((StockInfo) rhs).getChange().compareTo(((StockInfo) lhs).getChange());
					}
				});

				ArrayList<ArrayAdapterable> topFiveArray = new ArrayList<ArrayAdapterable>();
				ArrayList<ArrayAdapterable> flopFiveArray = new ArrayList<ArrayAdapterable>();

				for(int i = 0; i < 6; i++){
					if(i == topArray.size()){
						break;
					}else{
						topFiveArray.add(topArray.get(i));
						Log.d("TEST", ((StockInfo) topFiveArray.get(i)).getChange());
					}
				}

				for(int i = 0; i < 6; i++){
					if(i == flopArray.size()){
						break;
					}else{
						flopFiveArray.add(flopArray.get(i));
						Log.d("TEST", ((StockInfo) flopFiveArray.get(i)).getChange());
					}
				}
				
				StockInfoAdapter adapterTop = new StockInfoAdapter(act, android.R.layout.simple_list_item_1, topFiveArray, act, topList, tab);
				StockInfoAdapter adapterFlop = new StockInfoAdapter(act, android.R.layout.simple_list_item_1, flopFiveArray, act, flopList, tab);

				topList.setAdapter(adapterTop);
				topList.setOnItemClickListener(adapterTop);

				flopList.setAdapter(adapterFlop);
				flopList.setOnItemClickListener(adapterFlop);
			}
		}


		if(stock.size() == 0 && !(this.getStatus() == AsyncTask.Status.PENDING)){
			dismissDialog();
			error();
		}
	}

	public ArrayList<ArrayAdapterable> getStocks(){
		return stock;
	}

	public void setStockInfo(StockInfo stock){
		this.detailStock = stock;
	}

	public StockInfo getDetailStockInfo(){
		return detailStock;
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
