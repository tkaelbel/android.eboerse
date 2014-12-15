package com.com.android.eboerse.graph;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.achartengine.GraphicalView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.com.android.eboerse.main.MyErrorToast;
import com.com.android.eboerse.R;
import com.com.android.eboerse.search.DetailView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;
/**
 * async task zum holen der historischen daten von YQL
 * @author Tok
 *
 */
public class YqlHistoricalStockInformation extends AsyncTask<String, String, String>{

	private static final String TAG = "STOCKQUOTE";

	static final String KEY_ITEM = "quote";
	static final String KEY_DATE = "Date";
	static final String KEY_OPEN = "Open";
	static final String KEY_HIGH = "High";
	static final String KEY_CLOSE = "Close";
	static final String KEY_VOLUME = "Volume";
	static final String KEY_LOW = "Low";
	static final String KEY_ADJ_CLOSE = "Adj_Close";

	String date = "";
	String open = "";
	String low = "";
	String high = "";
	String close = "";
	String volume = "";
	String adjClose = "";

	private ProgressDialog pdia;

	private Activity act;
	private ArrayList<HistoricalStockInfo> histStockInfo = new ArrayList<HistoricalStockInfo>();


	public YqlHistoricalStockInformation(Activity act) {
		this.act = act;
	}

	public ArrayList<HistoricalStockInfo> getStockInformations(){
		if(histStockInfo != null)
			return histStockInfo;
		return null;
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
			
			Log.v("YQLHistory", "HTTP Response Code:" + responseCode);
			
			if (responseCode == HttpURLConnection.HTTP_OK) {

				InputStream in = httpConnection.getInputStream();
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document dom = db.parse(in);
				Element docEle = dom.getDocumentElement();
				NodeList nl = docEle.getElementsByTagName(KEY_ITEM);
				Log.v("YQLHistory", "Nodelist:" + nl  + " " + nl.getLength());
				if (nl != null && nl.getLength() > 0) {
					for (int i = 0 ; i < nl.getLength(); i++) {
						HistoricalStockInfo theStock = getStockInformation((Element) nl.item(i));

						date = theStock.getDate();
						open = theStock.getOpen();
						low = theStock.getLow();
						high = theStock.getHigh();
						close = theStock.getClose();
						volume = theStock.getVolume();
						adjClose = theStock.getAdjClose();

						Log.d(TAG, "Stock Date " + date);
						Log.d(TAG, "Stock Year High " + open);
						Log.d(TAG, "Stock Year Low " + close);

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

	private HistoricalStockInfo getStockInformation(Element entry){

		String date = getTextValue(entry, KEY_DATE);
		String open = getTextValue(entry, KEY_OPEN);
		String low = getTextValue(entry, KEY_LOW);
		String high = getTextValue(entry, KEY_HIGH);
		String close = getTextValue(entry, KEY_CLOSE);
		String volume = getTextValue(entry, KEY_VOLUME);
		String adjClose = getTextValue(entry, KEY_ADJ_CLOSE);

		HistoricalStockInfo theStock = new HistoricalStockInfo(date, open, high, low, close, volume, adjClose);
		histStockInfo.add(theStock);

		return theStock;

	}

	@Override
	protected void onPostExecute(String result) {
		pdia.dismiss();
		if(act instanceof DetailView){
			setUpGraph();
		}
		
	//	if(histStockInfo.size() == 0){
	//		error();
	//	}
	}

	private void setUpGraph(){
		GraphSettings line = new GraphSettings();
		FrameLayout viewLayout = (FrameLayout) act.findViewById(R.id.Diagramm);
		if(viewLayout != null){
			viewLayout.removeAllViews();
			GraphicalView view = (GraphicalView) line.getIntent(act, histStockInfo);
			((ViewGroup) viewLayout).addView(view);
		}

	}

	private void error(){
		String error = act.getResources().getString(R.string.errorMsg);
		MyErrorToast.doToast(act, error, Toast.LENGTH_SHORT);		
	}


}
