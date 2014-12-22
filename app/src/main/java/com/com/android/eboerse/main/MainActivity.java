package com.com.android.eboerse.main;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.com.android.eboerse.R;
import com.com.android.eboerse.search.SearchResultsActivity;
import com.com.android.eboerse.service.EboerseAlarmService;
import com.com.android.eboerse.tabs.TabDE;
import com.com.android.eboerse.tabs.TabEU;
import com.com.android.eboerse.tabs.TabListener;
import com.com.android.eboerse.tabs.TabNews;
import com.com.android.eboerse.tabs.TabUebersicht;
import com.com.android.eboerse.tabs.TabUebersichtRoh;
import com.com.android.eboerse.tabs.TabUebersichtWaeh;
import com.com.android.eboerse.tabs.TabWO;
import com.com.android.eboerse.tabs.TabWatchlist;
/**
 * MainActivity
 * handelt die home ansicht
 * sowie das drawermenu
 * startet den service fuer die favoriten
 * @author Tok
 *
 */
public class MainActivity extends Activity{

	public static final String MENU_ITEM_UEBERSICHT = "Übersicht";
	public static final String MENU_ITEM_AKTIEN = "Aktien";
	public static final String MENU_ITEM_ROH = "Rohstoffe";
	public static final String MENU_ITEM_WAEH = "Währungen";

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;


	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mMenuItems;

	private ListView list;

	private ActionBar bar;
	private static int menuPos = 0;

	private Tab uebersicht;
	private Tab watchlist;
	private Tab de;
	private Tab we;
	private Tab eu;
	private Tab rohstoffeTab;
	private Tab waehrungenTab;
	private Tab news;
	
	private Bundle bundle;


	@Override
	protected void onStart() {
		super.onStart();
		setupService();
	}

	private void setupService() {
		Log.v("EboerseAlarmService", "Service wird gestartet");
		Intent intent = new Intent(this, EboerseAlarmService.class);
		PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
		long interval = DateUtils.MINUTE_IN_MILLIS * 15;
		long firstStart = System.currentTimeMillis() + interval;


		AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.setInexactRepeating(AlarmManager.RTC, firstStart, 
                interval, pendingIntent);

        Log.v("EboerseAlarmService", "AlarmManager gesetzt");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bundle = savedInstanceState;
		initComponents();
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.options_menu_main_search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));


		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.options_menu_main_search).setVisible(!drawerOpen);

		if(drawerOpen)
			bar.removeAllTabs();

		return super.onPrepareOptionsMenu(menu);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		switch(item.getItemId()) {
		case R.id.options_menu_main_search:
			//showResult();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void showResult(){
		Intent intent = new Intent(MainActivity.this, SearchResultsActivity.class);
		startActivity(intent);
	}


	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		Fragment fragment = new MenuFragment();
		Bundle args = new Bundle();
		args.putInt(MenuFragment.MENU_NUMBER, position);
		fragment.setArguments(args);

		menuPos = position;

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
		doTabs(mMenuItems[position]);

		mDrawerList.setItemChecked(position, true);
		setTitle(mMenuItems[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}


	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}


	private void doTabs(String view){

		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		bar.removeAllTabs();

		if(view.equals(MENU_ITEM_UEBERSICHT)){

			String label1 = getResources().getString(R.string.uebersicht_tab);
			uebersicht = bar.newTab();
			uebersicht.setText(label1);
			TabListener<?> tl = new TabListener<TabUebersicht>(this,label1, TabUebersicht.class, list,view);
			uebersicht.setTabListener(tl);
			bar.addTab(uebersicht);

			String label2 = getResources().getString(R.string.watch_tab);
			watchlist = bar.newTab();
			watchlist.setText(label2);
			TabListener<?> tl2 = new TabListener<TabWatchlist>(this,label2, TabWatchlist.class, list,view);
			watchlist.setTabListener(tl2);
			bar.addTab(watchlist);
			
			String label3 = getResources().getString(R.string.news_tab);
			news = bar.newTab();
			news.setText(label3);
			TabListener<?> tl3 = new TabListener<TabNews>(this, label3, TabNews.class, list, view);
			news.setTabListener(tl3);
			bar.addTab(news);
		}

		if(view.equals(MENU_ITEM_AKTIEN)){
			String label1 = getResources().getString(R.string.de_tab);
			de = bar.newTab();
			de.setText(label1);
			TabListener<?> tl = new TabListener<TabDE>(this,label1, TabDE.class, list,view);
			de.setTabListener(tl);
			bar.addTab(de);

			String label2 = getResources().getString(R.string.eu_tab);
			eu = bar.newTab();
			eu.setText(label2);
			TabListener<?> tl2 = new TabListener<TabEU>(this,label2, TabEU.class, list,view);
			eu.setTabListener(tl2);
			bar.addTab(eu);

			String lbl3 = getResources().getString(R.string.we_tab);
			we = bar.newTab();
			we.setText(lbl3 );
			TabListener<?> tl3 = new TabListener<TabWO>(this,lbl3, TabWO.class, list,view);
			we.setTabListener(tl3);
			bar.addTab(we);
		}
		
		if(view.equals(MENU_ITEM_ROH)){
			String label1 = getResources().getString(R.string.uebersicht_tab);
			rohstoffeTab = bar.newTab();
			rohstoffeTab.setText(label1);
			TabListener<?> tl = new TabListener<TabUebersichtRoh>(this,label1, TabUebersichtRoh.class, list,view);
			rohstoffeTab.setTabListener(tl);
			bar.addTab(rohstoffeTab);
		}
		
		if(view.equals(MENU_ITEM_WAEH)){
			String label1 = getResources().getString(R.string.uebersicht_tab);
			waehrungenTab = bar.newTab();
			waehrungenTab.setText(label1);
			TabListener<?> tl = new TabListener<TabUebersichtWaeh>(this,label1, TabUebersichtWaeh.class, list,view);
			waehrungenTab.setTabListener(tl);
			bar.addTab(waehrungenTab);
		}

	}
	
	private void initComponents(){
		setContentView(R.layout.activity_main);
		
		list = (ListView) findViewById(R.id.listView1);
		bar = getActionBar();

		mTitle = mDrawerTitle = getTitle();
		mMenuItems = getResources().getStringArray(R.array.drawer_list_items);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mMenuItems));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(
				this,                  
				mDrawerLayout,         
				R.drawable.ic_drawer,  
				R.string.drawer_open,  
				R.string.drawer_close  
				) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				doTabs(mMenuItems[menuPos]);
				invalidateOptionsMenu(); 
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); 
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (bundle == null) {
			selectItem(menuPos);
		}
		
	}
	
//	@Override
//	protected void onRestart() {
//		super.onRestart();
//		initComponents();
//	}
	
}