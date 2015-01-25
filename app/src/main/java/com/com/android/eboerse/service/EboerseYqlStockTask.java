package com.com.android.eboerse.service;


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
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.os.AsyncTask;
import android.util.Log;

import com.com.android.eboerse.stock.StockInfo;

/**
 * eigener async task zum holen der daten fuer den service, somit kann es nicht
 * dazu kommen das der task gestartet ist und nochmal gestartet werden soll
 * @author Tok
 *
 */
public class EboerseYqlStockTask extends AsyncTask<String, String, String>{

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
    static final String KEY_CURRENCY = "Currency";

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

	private ArrayList<StockInfo> stock = new ArrayList<StockInfo>();

	private StockInfo detailStock;

	public EboerseYqlStockTask(){

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
	protected String doInBackground(String... params) {
		try {

			URL url = new URL(params[0]);

			URLConnection connection;
			connection = url.openConnection();

			HttpURLConnection httpConnection = (HttpURLConnection)connection;

			int responseCode = httpConnection.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK) {

				InputStream in = httpConnection.getInputStream();
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document dom = db.parse(in);
				Element docEle = dom.getDocumentElement();
				NodeList nl = docEle.getElementsByTagName(KEY_ITEM);

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
				}
			}
		} catch (MalformedURLException e) {
			Log.d(TAG, "MalformedURLException", e);
		} catch (IOException e) {
			Log.d(TAG, "IOException", e);
		} catch (ParserConfigurationException e) {
			Log.d(TAG, "Parser Configuration Exception", e);
		} catch (SAXException e) {
			Log.d(TAG, "SAX Exception", e);
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
        String currency = getTextValue(entry, KEY_CURRENCY);


		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		DateFormat formatter = new SimpleDateFormat();
		dateTime = formatter.format(date);

		StockInfo theStock = new StockInfo(stockDaysLow, stockDaysHigh, stockYearLow,
				stockYearHigh, stockName, stocklastTradePriceOnlyTextView,
				stockChange, stockDaysRange, dateTime, stockExchange, symbol, currency);

		stock.add(theStock);

		return theStock;

	}
	
	public ArrayList<StockInfo> getStocks(){
		return stock;
	}


}
