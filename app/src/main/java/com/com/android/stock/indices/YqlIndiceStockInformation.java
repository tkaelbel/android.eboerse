package com.com.android.stock.indices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.com.android.eboerse.ConnectionDetector;
import com.com.android.eboerse.MyErrorToast;
import com.com.android.eboerse.R;
import com.com.android.eboerse.stock.StockInfo;
import com.com.android.search.json.JsonUtils;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
/**
 * indice yql asynk task für indices
 *@author Tok
 *
 */
@Deprecated
public class YqlIndiceStockInformation  extends AsyncTask<String, String, String>{

	static final String KEY_ITEM = "row"; // parent node
	static final String KEY_COL_0 = "col0";
	static final String KEY_COL_1 = "col1";
	static final String KEY_COL_2 = "col2";
	static final String KEY_COL_3 = "col3";
	static final String KEY_COL_4 = "col4";
	static final String KEY_COL_5 = "col5";
	static final String KEY_COL_6 = "col6";
	static final String KEY_COL_7 = "col7";
	static final String KEY_COL_8 = "col8";

	static final String TAG = "INDICES";

	private Activity act;
	private ListView view;
	private ListView topView;
	private ListView flopView;

	private ArrayList<StockIndiceInfo> array = new ArrayList<StockIndiceInfo>();
	private ArrayList<StockIndiceInfo> flopArray = new ArrayList<StockIndiceInfo>();
	private ArrayList<StockIndiceInfo> topArray = new ArrayList<StockIndiceInfo>();

	private ProgressDialog pdia;

	private String col0;
	private String col1;
	private String col2;
	private String col3;
	private String col4;
	private String col5;
	private String col6;
	private String col7;
	private String col8;


	public YqlIndiceStockInformation(Activity act, ListView view) {
		this.act = act;
		this.view = view;
	}

	public YqlIndiceStockInformation(Activity act, ListView topView, ListView flopView) {
		this.act = act;
		this.topView = topView;
		this.flopView = flopView;
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
		pdia = new ProgressDialog(act);
		pdia.setMessage("Loading...");
		pdia.setCancelable(false);
		pdia.show();
	}



	@Override
	protected String doInBackground(String... params) {
		try {

			URL url = new URL(params[0]);

			URLConnection connection;
			connection = url.openConnection();

			HttpURLConnection httpConnection = (HttpURLConnection)connection;

			int responseCode = httpConnection.getResponseCode();
			Log.v("YQLIndice", "HTTP Response Code:" + responseCode);
			if (responseCode == HttpURLConnection.HTTP_OK) {

				InputStream in = httpConnection.getInputStream();
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document dom = db.parse(in);
				Element docEle = dom.getDocumentElement();
				NodeList nl = docEle.getElementsByTagName(KEY_ITEM);

				Log.v("YQLIndice", "Nodelist:" + nl  + " " + nl.getLength());
				if (nl != null && nl.getLength() > 0) {
					for (int i = 0 ; i < nl.getLength(); i++) {
						StockIndiceInfo theStock = getStockInformation((Element) nl.item(i));

						col0 = theStock.getCol0();
						col1 = theStock.getCol1();
						col2 = theStock.getCol2();
						col3 = theStock.getCol3();
						col4 = theStock.getCol4();
						col5 = theStock.getCol5();
						col6 = theStock.getCol6();
						col7 = theStock.getCol7();
						col8 = theStock.getCol8();

						Log.d(TAG, "Stock symbol" + col0);
						Log.d(TAG, "Stock name" + col1);
						Log.d(TAG, "Stock change" + col2);
						Log.d(TAG, "Stock lastTrade" + col3);
					}			
//				}else if(nl.getLength() == 0){
//					error();
				}
			}else{
				error();
			}
		} catch (MalformedURLException e) {
			Log.d(TAG, "MalformedURLException", e);
			error();
		} catch (IOException e) {
			Log.d(TAG, "IOException", e);
			error();
		} catch (ParserConfigurationException e) {
			Log.d(TAG, "Parser Configuration Exception", e);
			error();
		} catch (SAXException e) {
			Log.d(TAG, "SAX Exception", e);
			error();
		}
		finally {
		}
		return null;
	}

	private StockIndiceInfo getStockInformation(Element entry){

		String col0 = getTextValue(entry, KEY_COL_0);
		String col1 = getTextValue(entry, KEY_COL_1);
		String col2 = getTextValue(entry, KEY_COL_2);
		String col3 = getTextValue(entry, KEY_COL_3);
		String col4 = getTextValue(entry, KEY_COL_4);
		String col5 = getTextValue(entry, KEY_COL_5);
		String col6 = getTextValue(entry, KEY_COL_6);
		String col7 = getTextValue(entry, KEY_COL_7);
		String col8 = getTextValue(entry, KEY_COL_8);
		String dateTime = null;

		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		DateFormat formatter = new SimpleDateFormat();
		dateTime = formatter.format(date);

		StockIndiceInfo theStock = new StockIndiceInfo(col0, col1, col2, col3, col4, col5, col6, col7, col8, dateTime);

		if(theStock.getCol0() != null && theStock.getCol1() != null && theStock.getCol2() != null && theStock.getCol3() != null){
			array.add(theStock);
		}

		if(theStock.getCol2() != null && !theStock.getCol2().contains("N/A")){
			Log.d("Indice", "OK");
			if(theStock.getCol2().contains("+")){
				String col2WithOutPlus = theStock.getCol2().replace("+", "");
				if(Double.valueOf(col2WithOutPlus) > 0){
					topArray.add(theStock);
				}
			}
		}

		if(theStock.getCol2() != null && !theStock.getCol2().contains("+") && !theStock.getCol2().contains("N/A")){
			Log.d("Indice", "OK");
			if(Double.valueOf(theStock.getCol2()) < 0){
				flopArray.add(theStock);
			}
		}


		return theStock;

	}


	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		
		Log.v("YQLIndice", "Stock Size: " + array.size());
		
		pdia.dismiss();

		if(view != null){
			StockIndiceInfoAdapter adapter = new StockIndiceInfoAdapter(act, android.R.layout.simple_list_item_1, array, act, view);
			view.setAdapter(adapter);
			view.setOnItemClickListener(adapter);
		}
		if(topView != null && flopView != null && topArray != null && flopArray != null){	
			Collections.sort(topArray, new Comparator<StockIndiceInfo>() {
				@Override
				public int compare(StockIndiceInfo lhs, StockIndiceInfo rhs) {
					return lhs.getCol2().compareTo(rhs.getCol2());
				}
			});

			Collections.sort(flopArray, new Comparator<StockIndiceInfo>() {
				@Override
				public int compare(StockIndiceInfo lhs, StockIndiceInfo rhs) {
					return rhs.getCol2().compareTo(lhs.getCol2());
				}
			});

			ArrayList<StockIndiceInfo> topFiveArray = new ArrayList<StockIndiceInfo>();
			ArrayList<StockIndiceInfo> flopFiveArray = new ArrayList<StockIndiceInfo>();

			for(int i = 0; i < 6; i++){
				if(i == topArray.size()){
					break;
				}else{
					topFiveArray.add(topArray.get(i));
					Log.d("TEST", topFiveArray.get(i).getCol2());
				}
			}

			for(int i = 0; i < 6; i++){
				if(i == flopArray.size()){
					break;
				}else{
					flopFiveArray.add(flopArray.get(i));
					Log.d("TEST", flopFiveArray.get(i).getCol2());
				}
			}

			StockIndiceInfoAdapter adapterTop = new StockIndiceInfoAdapter(act, android.R.layout.simple_list_item_1, topFiveArray, act, topView);
			StockIndiceInfoAdapter adapterFlop = new StockIndiceInfoAdapter(act, android.R.layout.simple_list_item_1, flopFiveArray, act, flopView);

			topView.setAdapter(adapterTop);
			topView.setOnItemClickListener(adapterTop);

			flopView.setAdapter(adapterFlop);
			flopView.setOnItemClickListener(adapterFlop);
		}
		
		if(array.size() == 0){
			error();
		}

	}

	private void error(){
		String error = act.getResources().getString(R.string.errorMsg);
		MyErrorToast.doToast(act, error, Toast.LENGTH_SHORT);		
	}


}
