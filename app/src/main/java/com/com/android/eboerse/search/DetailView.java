package com.com.android.eboerse.search;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.achartengine.GraphicalView;

import com.com.android.eboerse.ConnectionDetector;
import com.com.android.eboerse.MainActivity;
import com.com.android.eboerse.R;
import com.com.android.eboerse.SymbolsGoodToKnow;
import com.com.android.eboerse.database.DatabaseHandler;
import com.com.android.eboerse.database.Favorit;
import com.com.android.eboerse.detail.tabs.TabDetailListener;
import com.com.android.eboerse.detail.tabs.TabDetailProfil;
import com.com.android.eboerse.detail.tabs.TabDetailTopFlop;
import com.com.android.eboerse.detail.tabs.TabDetailZusammensetzung;
import com.com.android.eboerse.graph.GraphSettings;
import com.com.android.eboerse.graph.YqlHistoricalStockInformation;
import com.com.android.eboerse.stock.StockInfo;
import com.com.android.eboerse.tabs.TabListener;
import com.com.android.eboerse.tabs.TabUebersicht;

import android.R.array;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.AsyncTask;
/**
 * Detail view der kurse als neue activity
 * @author Tok
 *
 */
public class DetailView extends Activity{

	private TextView symbolNameAndName;
	private TextView pointsAndDate;
	private TextView pointsAndPercent;
	private TextView eroeffnung;
	private TextView hoch;
	private TextView hoch52w;
	private TextView vortag;
	private TextView tief;
	private TextView tief52w;

	private Button monBut;
	private Button weBut;
	private Button yearBut;

	private YqlStockInformation yql;
	private YqlHistoricalStockInformation yqlHistory;

	private String symbol = null;
	private ActionBar bar;

	private Tab tabProfil;
	private Tab tabTopFlop;
	private Tab tabZusam;

	private SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
	private DatabaseHandler db;
	private Menu topMenu;

	//Dialog komponenten	
	private Dialog alarmDialog;
	private TextView aktuell;
	private EditText oberGrenze;
	private EditText unterGrenze;
	private Button btnOk;
	private Button btnCancel;

//	private ConnectionDetector ConnectionDetector = new ConnectionDetector(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initComponents();
	}

	private void handleIntent(Intent intent) {
		Bundle bundleSymbol = intent.getExtras();

		if(bundleSymbol != null){
			symbol = bundleSymbol.getString("symbol");
		}

		if(symbol.contains("^")){
			doTabs();
		}

		String yqlQuery = SymbolsGoodToKnow.YAHOO_URL_FIRST + symbol + SymbolsGoodToKnow.YAHOO_URL_SECOND;
		yql = new YqlStockInformation(this, symbolNameAndName, pointsAndDate, pointsAndPercent, eroeffnung, hoch,
				hoch52w, vortag, tief, tief52w);
		if(ConnectionDetector.isConnectingToInternet(this)){
			yql.execute(yqlQuery);
		}

		doGraph((String) weBut.getText());
	}

	public void doGraph(String btnName){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 0);

		String end = format1.format(cal.getTime());

		yqlHistory = new YqlHistoricalStockInformation(this);

		if(btnName.equals(weBut.getText())){	
			Date sevenDay = new Date(System.currentTimeMillis() - 7L * 24 * 3600 * 1000);
			String start = format1.format(sevenDay);

			String yqlHistoryQuery = SymbolsGoodToKnow.YAHOO_HISTORICAL_FIRST_URL + symbol + 
					SymbolsGoodToKnow.YAHOO_HISTORICAL_SEC_URL + start + SymbolsGoodToKnow.YAHOO_HISTORICAL_THIRD_URL + end + SymbolsGoodToKnow.YAHOO_HISTORICAL_FOURTH_URL;

			Log.d("DetailView", yqlHistoryQuery);
			if(ConnectionDetector.isConnectingToInternet(this)){
				yqlHistory.execute(yqlHistoryQuery);
			}
		}

		if(btnName.equals(monBut.getText())){
			Date sevenDay = new Date(System.currentTimeMillis() - 31L * 24 * 3600 * 1000);
			String start = format1.format(sevenDay);

			String yqlHistoryQuery = SymbolsGoodToKnow.YAHOO_HISTORICAL_FIRST_URL + symbol + 
					SymbolsGoodToKnow.YAHOO_HISTORICAL_SEC_URL + start + SymbolsGoodToKnow.YAHOO_HISTORICAL_THIRD_URL + end + SymbolsGoodToKnow.YAHOO_HISTORICAL_FOURTH_URL;

			Log.d("DetailView", yqlHistoryQuery);
			if(ConnectionDetector.isConnectingToInternet(this)){
				yqlHistory.execute(yqlHistoryQuery);
			}
		}

		if(btnName.equals(yearBut.getText())){
			Date sevenDay = new Date(System.currentTimeMillis() - 365L * 24 * 3600 * 1000);
			String start = format1.format(sevenDay);

			String yqlHistoryQuery = SymbolsGoodToKnow.YAHOO_HISTORICAL_FIRST_URL + symbol + 
					SymbolsGoodToKnow.YAHOO_HISTORICAL_SEC_URL + start + SymbolsGoodToKnow.YAHOO_HISTORICAL_THIRD_URL + end + SymbolsGoodToKnow.YAHOO_HISTORICAL_FOURTH_URL;

			Log.d("DetailView", yqlHistoryQuery);
			if(ConnectionDetector.isConnectingToInternet(this)){
				yqlHistory.execute(yqlHistoryQuery);
			}
		}
	}



	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		MenuItem item = null;

		item = menu.findItem(R.id.options_menu_main_search);
		item.setVisible(false);

		topMenu = menu;

		refreshActionBar(topMenu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_main_favorit:
			addFavorit();
			break;
		case R.id.menu_main_is_favorit:
			deleteFavorit();
			refreshActionBar(topMenu);
			break;
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	private void doTabs(){

		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		String label1 = getResources().getString(R.string.detail_profil_tab);
		tabProfil = bar.newTab();
		tabProfil.setText(label1);


		TabDetailListener<?> tl = new TabDetailListener<TabDetailProfil>(this,label1, TabDetailProfil.class, symbol);
		tabProfil.setTabListener(tl);
		bar.addTab(tabProfil);

		label1 = getResources().getString(R.string.detail_topflop_tab);
		tabTopFlop = bar.newTab();
		tabTopFlop.setText(label1);

		TabDetailListener<?> tl1 = new TabDetailListener<TabDetailTopFlop>(this,label1, TabDetailTopFlop.class, symbol);
		tabTopFlop.setTabListener(tl1);
		bar.addTab(tabTopFlop);


		label1 = getResources().getString(R.string.detail_zusam_tab);
		tabZusam = bar.newTab();
		tabZusam.setText(label1);

		TabDetailListener<?> tl2 = new TabDetailListener<TabDetailZusammensetzung>(this,label1, TabDetailZusammensetzung.class, symbol);
		tabZusam.setTabListener(tl2);
		bar.addTab(tabZusam);

	}

	public void setupBtnListeners(){

		monBut = (Button) findViewById(R.id.btn_1m);
		weBut = (Button) findViewById(R.id.btn_1w);
		yearBut = (Button) findViewById(R.id.btn_1j);

		monBut.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doGraph((String) monBut.getText());
			}
		});

		weBut.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doGraph((String) weBut.getText());
			}
		});

		yearBut.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doGraph((String) yearBut.getText());
			}
		});
	}

	private void addFavorit(){
		String r = (String) symbolNameAndName.getText();
		String[] arr = r.split("\r\n");

		showTheDialog(arr);
	}

	private void showTheDialog(final String[] nameAndSymbol){
		alarmDialog = new Dialog(this);
		alarmDialog.setContentView(R.layout.dialog_layout);

		aktuell = (TextView) alarmDialog.findViewById(R.id.textViewAktuell);
		oberGrenze = (EditText) alarmDialog.findViewById(R.id.editTextObergrenze);
		unterGrenze = (EditText) alarmDialog.findViewById(R.id.editTextUntergrenze);
		btnOk = (Button) alarmDialog.findViewById(R.id.btn_ok);
		btnCancel = (Button) alarmDialog.findViewById(R.id.btn_cancel);

		alarmDialog.setTitle(nameAndSymbol[0]);
		String r = (String) pointsAndDate.getText();
		String[] arr = r.split("\r\n");
		aktuell.setText(arr[0]);

		btnOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String ober = oberGrenze.getText().toString();
				String unter = unterGrenze.getText().toString();
				String akt = aktuell.getText().toString();

				Double unterDouble;
				Double oberDouble;
				Double aktuellDouble;

				if(akt.equals("")){
					aktuellDouble = 0.0;
				}else{
					aktuellDouble = Double.valueOf(akt);
				}

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
					if(nameAndSymbol.length > 0){
						db.addKurs(new Favorit(nameAndSymbol[0], nameAndSymbol[1], "Y", ober, unter));
						refreshActionBar(topMenu);
						alarmDialog.dismiss();
					}
				}

			}
		});

		btnCancel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				alarmDialog.dismiss();
				refreshActionBar(topMenu);
			}

		});

		alarmDialog.show();
	}

	private void deleteFavorit(){
		Favorit favorit = new Favorit();
		favorit.setKurs_Symbol(symbol);
		db.deleteKurs(favorit);
	}

	private boolean selectIfItIsFavorite(){
		Favorit favorit = null;
		if(db.isTableExists()){
			favorit = db.getFavorit(symbol);
		}
		boolean ret = false;

		if(favorit != null){
			if(favorit.getFlag_Favorit().equals("Y")){
				return ret = true;
			}
		}

		return ret;
	}

	private void initDb(){
		db = new DatabaseHandler(this);
	}

	private void refreshActionBar(Menu menu){
		MenuItem item;

		if(selectIfItIsFavorite()){
			item = menu.findItem(R.id.menu_main_is_favorit);
			item.setVisible(true);
			item = menu.findItem(R.id.menu_main_favorit);
			item.setVisible(false);
		}else{
			item = menu.findItem(R.id.menu_main_favorit);
			item.setVisible(true);
			item = menu.findItem(R.id.menu_main_is_favorit);
			item.setVisible(false);
		}

	}

//	@Override
//	protected void onRestart() {
//		super.onRestart();
//		initComponents();
//	}

	private void initComponents(){
		setContentView(R.layout.detail_list_view);

		symbolNameAndName = (TextView) findViewById(R.id.text_view_symbol_name);
		pointsAndDate = (TextView) findViewById(R.id.text_view_punkte_datum);
		pointsAndPercent = (TextView) findViewById(R.id.text_punkte_prozent);
		hoch = (TextView) findViewById(R.id.text_stat_hoch);
		hoch52w = (TextView) findViewById(R.id.text_stat_hoch_52w);
		tief = (TextView) findViewById(R.id.text_view_stat_tief);
		tief52w = (TextView) findViewById(R.id.text_view_stat_tief_52W);


		setupBtnListeners();

		// get the action bar
		bar = getActionBar();

		// Enabling Back navigation on Action Bar icon
		bar.setDisplayHomeAsUpEnabled(true);

		initDb();

		handleIntent(getIntent());
	}

	@Override
	protected void onStop() {
		super.onStop();
		db.close();
	}



}
