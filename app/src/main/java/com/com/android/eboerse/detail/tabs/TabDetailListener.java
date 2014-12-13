package com.com.android.eboerse.detail.tabs;


import java.util.ArrayList;

import com.com.android.eboerse.ConnectionDetector;
import com.com.android.eboerse.MyErrorToast;
import com.com.android.eboerse.R;
import com.com.android.eboerse.SymbolsGoodToKnow;
import com.com.android.eboerse.search.DetailView;
import com.com.android.eboerse.search.YqlStockInformation;
import com.com.android.eboerse.webrip.WebripYqlStockIndiceInformation;
import com.com.android.stock.indices.YqlIndiceStockInformation;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * Tabdetaillistener
 * handelt alle tab selektierungen in der Detail-Ansicht
 * @author Tok
 *
 * @param <T>
 */
public class TabDetailListener <T extends Fragment> implements ActionBar.TabListener{

	public static final String TAB_PROFIL = "Profil";
	public static final String TAB_TOP_FLOP = "Top/Flop";
	public static final String TAB_ZUSAM = "Grundger\\u00dcst";

	private DetailView act;
	private String tag;
//	private Class<T> mclass;
	private String symbol;

	private TextView symbolNameAndName;
	private TextView pointsAndDate;
	private TextView pointsAndPercent;
	private TextView eroeffnung;
	private TextView hoch;
	private TextView hoch52w;
	private TextView vortag;
	private TextView tief;
	private TextView tief52w;

	private TextView top;
	private TextView flop;

	public TabDetailListener(DetailView act, String tabName, Class<T> clz, String symbol) {
		this.act = act;
		this.tag = tabName;
//		this.mclass = clz;
		this.symbol = symbol;
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
//		ConnectionDetector ConnectionDetector = new ConnectionDetector(act);

		String yqlQuery = null;

		YqlStockInformation yqlStock;
		WebripYqlStockIndiceInformation rip;

		//		Detail View ansicht - Profil
		if(tag.equals(TAB_PROFIL)){

			act.setContentView(R.layout.detail_list_view);
			act.setupBtnListeners();

			yqlQuery = SymbolsGoodToKnow.YAHOO_URL_FIRST + symbol + SymbolsGoodToKnow.YAHOO_URL_SECOND;

			symbolNameAndName = (TextView) act.findViewById(R.id.text_view_symbol_name);
			pointsAndDate = (TextView) act.findViewById(R.id.text_view_punkte_datum);
			pointsAndPercent = (TextView) act.findViewById(R.id.text_punkte_prozent);
			hoch = (TextView) act.findViewById(R.id.text_stat_hoch);
			hoch52w = (TextView) act.findViewById(R.id.text_stat_hoch_52w);
			tief = (TextView) act.findViewById(R.id.text_view_stat_tief);
			tief52w = (TextView) act.findViewById(R.id.text_view_stat_tief_52W);

			yqlStock = new YqlStockInformation(act, symbolNameAndName, pointsAndDate, pointsAndPercent, eroeffnung, hoch,
					hoch52w, vortag, tief, tief52w);

			if(ConnectionDetector.isConnectingToInternet(act)){
				yqlStock.execute(yqlQuery);
			}

			act.doGraph(act.getResources().getString(R.string.btn_w1));

		}

		//		Detail View ansicht - Top/Flop
		if(tag.equals(TAB_TOP_FLOP)){
			act.setContentView(R.layout.detail_top_flop_list_view);
			ListView listTop = (ListView) act.findViewById(R.id.list_view_top);
			ListView listFlop = (ListView) act.findViewById(R.id.list_view_flop);
			top = (TextView) act.findViewById(R.id.text_top);
			flop = (TextView) act.findViewById(R.id.text_flop);


//			String symbolNeu = symbol.replace("^", "");

			yqlQuery = SymbolsGoodToKnow.YAHOO_URL_IDX_BESTANDTEILE_FIRST + symbol;
			rip = new WebripYqlStockIndiceInformation(act, listTop, listFlop, tag);
			
//			yqlIndice = new YqlIndiceStockInformation(act, listTop, listFlop);
			if(ConnectionDetector.isConnectingToInternet(act)){
//				yqlIndice.execute(yqlQuery);
				rip.execute(yqlQuery);
			}
			
			top.setText(act.getResources().getString(R.string.top));
			flop.setText(act.getResources().getString(R.string.flop));

		}

		// Detail View ansicht - Zusammensetzung		
		if(tag.equals(TAB_ZUSAM)){
			act.setContentView(R.layout.detail_zusammen_list_view);
			ListView list = (ListView) act.findViewById(R.id.list_view_detail_zusammenfassen);
//			String symbolNeu = symbol.replace("^", "");

//			yqlQuery = SymbolsGoodToKnow.YAHOO_URL_IDX_FIRST + symbolNeu + SymbolsGoodToKnow.YAHOO_URL_IDX_SEC;
			
			yqlQuery = SymbolsGoodToKnow.YAHOO_URL_IDX_BESTANDTEILE_FIRST + symbol;
//			ArrayList<String> symbols = rip.ripSymbolsFromHttp(yqlQuery);
			rip = new WebripYqlStockIndiceInformation(act, list, tag);
			
//			yqlIndice = new YqlIndiceStockInformation(act, list);
			if(ConnectionDetector.isConnectingToInternet(act)){
//				yqlIndice.execute(yqlQuery);
				rip.execute(yqlQuery);
			}
			
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

}
