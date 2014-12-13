package com.com.android.eboerse.tabs;


import java.util.ArrayList;

import com.com.android.eboerse.ConnectionDetector;
import com.com.android.eboerse.MainActivity;
import com.com.android.eboerse.R;
import com.com.android.eboerse.SymbolsGoodToKnow;
import com.com.android.eboerse.database.DatabaseHandler;
import com.com.android.eboerse.database.Favorit;
import com.com.android.eboerse.search.YqlStockInformation;
import com.com.android.eboerse.webrip.WebripYqlStockNews;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.ActionBar.Tab;
import android.widget.ListView;
/**
 * handelt alle tabs 
 * @author Tok
 *
 * @param <T>
 */
public class TabListener<T extends Fragment> implements ActionBar.TabListener {

//	private ConnectionDetector ConnectionDetector;

//	private Fragment mFragment;
	private Activity mActivity;
	private String mTag;
//	private Class<T> mClass;
	private ListView view;
	private String menuName;

	private DatabaseHandler db;

	@SuppressWarnings("unused")
	public TabListener(Activity activity, String tag, Class<T> clz, ListView view, String menuName) {
		this.mActivity = activity;
		this.mTag = tag;
//		this.mClass = clz;
		this.view = view;
		this.menuName = menuName;
	}


	@Override
	public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {

	}

	@Override
	public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {

//		ConnectionDetector = new ConnectionDetector(mActivity);

		String yqlQuery = null;
		YqlStockInformation yql;
		WebripYqlStockNews yqlNews;

		if(menuName.equals(MainActivity.MENU_ITEM_UEBERSICHT)){
			if(mTag.equals(mActivity.getResources().getString(R.string.uebersicht_tab))){
				yqlQuery = SymbolsGoodToKnow.YAHOO_URL_FIRST + SymbolsGoodToKnow.DAX +","
						+ SymbolsGoodToKnow.TEXDAX + "," + SymbolsGoodToKnow.GOLD + "," + SymbolsGoodToKnow.OEL + "," 
						+ SymbolsGoodToKnow.EUR_USD + SymbolsGoodToKnow.YAHOO_URL_SECOND;
				yql = new YqlStockInformation(mActivity, view);
				if(ConnectionDetector.isConnectingToInternet(mActivity)){
					yql.execute(yqlQuery);
				}
			}
			if(mTag.equals(mActivity.getResources().getString(R.string.watch_tab))){
				initDb();
				ArrayList<Favorit> favorits = (ArrayList<Favorit>) db.getAllKurs();
				yql = new YqlStockInformation(mActivity, view, mActivity.getResources().getString(R.string.watch_tab));
				yqlQuery = SymbolsGoodToKnow.YAHOO_URL_FIRST;

				Favorit favorit = null;
				for(int i = 0; i < favorits.size(); i++){
					favorit = favorits.get(i);
					if(i != favorits.size()){
						yqlQuery = yqlQuery + favorit.getKurs_Symbol() + ",";
					}else{
						yqlQuery = yqlQuery + favorit.getKurs_Symbol();
					}
				}
				db.close();
				yqlQuery = yqlQuery + SymbolsGoodToKnow.YAHOO_URL_SECOND;
				if(ConnectionDetector.isConnectingToInternet(mActivity)){
					yql.execute(yqlQuery);
				}
			}
			if(mTag.equals(mActivity.getResources().getString(R.string.news_tab))){
				yqlQuery = SymbolsGoodToKnow.YAHOO_URL_AKT_NEWS;
				yqlNews = new WebripYqlStockNews(mActivity, view, mTag);
				if(ConnectionDetector.isConnectingToInternet(mActivity)){
					yqlNews.execute(yqlQuery);
				}
				
			}
			
		}

		if(menuName.equals(MainActivity.MENU_ITEM_AKTIEN)){
			if(mTag.equals(mActivity.getResources().getString(R.string.de_tab))){
				yqlQuery = SymbolsGoodToKnow.YAHOO_URL_FIRST + SymbolsGoodToKnow.DAX + "," + SymbolsGoodToKnow.TEXDAX 
						+ "," + SymbolsGoodToKnow.MDAX + "," + SymbolsGoodToKnow.SDAX + "," + 
						SymbolsGoodToKnow.HDAX + "," + SymbolsGoodToKnow.CDAX + SymbolsGoodToKnow.YAHOO_URL_SECOND;
				yql = new YqlStockInformation(mActivity, view);
				if(ConnectionDetector.isConnectingToInternet(mActivity)){
					yql.execute(yqlQuery);
				}
			}
			if(mTag.equals(mActivity.getResources().getString(R.string.eu_tab))){
				yqlQuery = SymbolsGoodToKnow.YAHOO_URL_FIRST + SymbolsGoodToKnow.EUR_STOXX + "," + SymbolsGoodToKnow.FTSE 
						+ "," + SymbolsGoodToKnow.CAC + "," + SymbolsGoodToKnow.ATX + "," + 
						SymbolsGoodToKnow.RTS + "," + SymbolsGoodToKnow.AEX + "," + SymbolsGoodToKnow.BEL 
						+ "," + SymbolsGoodToKnow.IBEX + SymbolsGoodToKnow.YAHOO_URL_SECOND;
				yql = new YqlStockInformation(mActivity, view);
				if(ConnectionDetector.isConnectingToInternet(mActivity)){
					yql.execute(yqlQuery);
				}
			}
			if(mTag.equals(mActivity.getResources().getString(R.string.we_tab))){
				yqlQuery = SymbolsGoodToKnow.YAHOO_URL_FIRST + SymbolsGoodToKnow.EUR_STOXX + "," + SymbolsGoodToKnow.DOW 
						+ "," + SymbolsGoodToKnow.SP + "," + SymbolsGoodToKnow.NASDAQ + "," + 
						SymbolsGoodToKnow.NIKKEI + "," + SymbolsGoodToKnow.HANG + "," + SymbolsGoodToKnow.BSE 
						+ "," + SymbolsGoodToKnow.BVSP +  "," + SymbolsGoodToKnow.IPC +  "," + SymbolsGoodToKnow.MERV + SymbolsGoodToKnow.YAHOO_URL_SECOND;
				yql = new YqlStockInformation(mActivity, view);
				if(ConnectionDetector.isConnectingToInternet(mActivity)){
					yql.execute(yqlQuery);
				}
			}
		}

		if(menuName.equals(MainActivity.MENU_ITEM_ROH)){
			if(mTag.equals(mActivity.getResources().getString(R.string.uebersicht_tab))){
				yqlQuery = SymbolsGoodToKnow.YAHOO_URL_FIRST + SymbolsGoodToKnow.GOLD + "," + SymbolsGoodToKnow.OEL + "," + SymbolsGoodToKnow.SILBER + "," +SymbolsGoodToKnow.KUPFER + "," + SymbolsGoodToKnow.PALLADIUM + "," + SymbolsGoodToKnow.PLATINUM 
						+ "," + SymbolsGoodToKnow.CRUDE_OEL + "," + SymbolsGoodToKnow.HEIZ_OEL + "," + SymbolsGoodToKnow.GAS + "," + SymbolsGoodToKnow.GASOLINE + SymbolsGoodToKnow.YAHOO_URL_SECOND;
				yql = new YqlStockInformation(mActivity, view);
				if(ConnectionDetector.isConnectingToInternet(mActivity)){
					yql.execute(yqlQuery);
				}
			}
		}

		if(menuName.equals(MainActivity.MENU_ITEM_WAEH)){
			if(mTag.equals(mActivity.getResources().getString(R.string.uebersicht_tab))){
				yqlQuery = SymbolsGoodToKnow.YAHOO_URL_FIRST + SymbolsGoodToKnow.EUR_USD + "," + SymbolsGoodToKnow.GBP_USD + "," + SymbolsGoodToKnow.EUR_CHF + "," + SymbolsGoodToKnow.USD_CHF + "," + SymbolsGoodToKnow.USD_JPY + "," + SymbolsGoodToKnow.USD_CAD 
						+ "," + SymbolsGoodToKnow.EUR_GBR + "," + SymbolsGoodToKnow.AUD_USD + SymbolsGoodToKnow.YAHOO_URL_SECOND;
				yql = new YqlStockInformation(mActivity, view);
				if(ConnectionDetector.isConnectingToInternet(mActivity)){
					yql.execute(yqlQuery);
				}
			}
		}
	}

	@Override
	public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {

	}

	private void initDb(){
		db = new DatabaseHandler(mActivity);
	}

}
