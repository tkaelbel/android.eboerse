package com.com.android.search.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.com.android.eboerse.main.MyErrorToast;
import com.com.android.eboerse.R;
import com.com.android.eboerse.search.SymbolInfoAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * Json wird für die Suche benutzt
 * @author Tok
 *
 */
public class JsonUtils extends AsyncTask<String, String, String>{

	private ListView list;
	private Activity act;
	private ArrayList<Result> resultLis = new ArrayList<Result>();
    private ProgressDialog pdia;

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

	public JsonUtils(Activity act, ListView list) {
		this.list = list;
		this.act = act;
	}

	public static <T> T parseJson(String json, Class<T> resultType) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(json, resultType);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return null;
	}

	@Override
	protected String doInBackground(String... params) {

		String responseBody = null;
		URL url = null;
		try {
			url = new URL(params[0]);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			error();
		}

		URLConnection connection = null;
		try {
			connection = url.openConnection();
		} catch (IOException e1) {
			e1.printStackTrace();
			error();
		}

		HttpURLConnection httpConnection = (HttpURLConnection)connection;

		int responseCode= 0;
		try {
			responseCode = httpConnection.getResponseCode();
		} catch (IOException e) {
			e.printStackTrace();
			error();
		}
		
		Log.v("YQLSearch", "HTTP Response Code:" + responseCode);
		if (responseCode == HttpURLConnection.HTTP_OK) {
			try {

				System.out.println("executing request " + httpConnection.getURL());
				org.apache.http.client.ResponseHandler<String> responseHandler = new org.apache.http.impl.client.BasicResponseHandler();

				InputStream input = httpConnection.getInputStream();
				responseBody = inputStreamToString(input).toString();
			} catch (IOException e) {
				e.printStackTrace();
				error();
			} finally {
				httpConnection.disconnect();
			}

			return responseBody;
		}


		return responseBody;
	}

	private StringBuilder inputStreamToString(InputStream is) {
		String line = "";
		StringBuilder total = new StringBuilder();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));

		try {
			while ((line = rd.readLine()) != null) { 
				total.append(line); 
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return total;
	}

	@Override
	protected void onPostExecute(String results) {
		super.onPostExecute(results);

        dismissDialog();

		String sr = results.replace("YAHOO.Finance.SymbolSuggest.ssCallback", "");
		String ss = sr.replace("(", "");
		String srs = ss.replace(")", "");
		String blabla = srs.replace("{\"ResultSet\":", "");

		ResultSet rs = JsonUtils.parseJson(blabla, ResultSet.class);

		for ( Result result : rs.getResult() ) {
			resultLis.add(result);
		}

		final SymbolInfoAdapter adapter = new SymbolInfoAdapter(act,
				android.R.layout.simple_list_item_1, resultLis, act);
		list.setAdapter(adapter);
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
