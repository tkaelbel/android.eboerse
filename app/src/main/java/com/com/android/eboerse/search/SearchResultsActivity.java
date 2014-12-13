package com.com.android.eboerse.search;


import com.com.android.eboerse.main.ConnectionDetector;
import com.com.android.eboerse.R;
import com.com.android.search.json.JsonUtils;
import com.com.android.search.json.Result;


import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * such activity handelt die suche und deren ergebnisse
 * @author Tok
 *
 */
public class SearchResultsActivity extends Activity{

	private static final String QUERY_URL_FIRST = "http://d.yimg.com/autoc.finance.yahoo.com/autoc?query=";
	private static final String QUERY_URL_SEC = "&callback=YAHOO.Finance.SymbolSuggest.ssCallback";
	private ListView list;
	private Bundle bundle;
//	private ConnectionDetector ConnectionDetector = new ConnectionDetector(this);


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bundle = savedInstanceState;
		initComponents();
	}


	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);

			if(query.contains(" ")){
				String[] newQueryArr = query.split(" ");
				String newQuery = newQueryArr[0];
				query = newQuery;
			}

			String yqlQuery = QUERY_URL_FIRST + query + QUERY_URL_SEC;
			JsonUtils js = new JsonUtils(this,list);
			if(ConnectionDetector.isConnectingToInternet(this)){
				js.execute(yqlQuery);
			}
			

			list.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					list.setSelection(arg2);
					Object obj = list.getItemAtPosition(arg2);
					Result rst = (Result) obj;
					showDetailView(rst.getSymbol());
				}
			});
		}

	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void showDetailView(String symbol){
		Intent intent = new Intent(SearchResultsActivity.this, DetailView.class);
		intent.putExtra("symbol", symbol);
		startActivity(intent);
	}

	private void initComponents(){
		setContentView(R.layout.search_symbols);

		list = (ListView) findViewById(R.id.list_view_search_symbols);

		// get the action bar
		ActionBar actionBar = getActionBar();

		// Enabling Back navigation on Action Bar icon
		actionBar.setDisplayHomeAsUpEnabled(true);

		handleIntent(getIntent());
	}
}
