package com.com.android.eboerse.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.com.android.eboerse.R;
import com.com.android.eboerse.search.YqlStockInformation;
/**
 * dient zum handeln des menus
 * @author Tok
 *
 */
public class MenuFragment extends Fragment {
	public static final String MENU_NUMBER = "menu_number";
	
	public MenuFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_main, container, false);
//		list = (ListView) rootView.findViewById(R.id.listView1);
//		act = getActivity();
//		String yqlQuery = null;
//		YqlStockInformation yql = new YqlStockInformation(act, list);
		int i = getArguments().getInt(MENU_NUMBER);
//		switch (i) {
//		case 2:
//			yqlQuery = SymbolsGoodToKnow.YAHOO_URL_FIRST + SymbolsGoodToKnow.GOLD + "," + SymbolsGoodToKnow.OEL + "," + SymbolsGoodToKnow.SILBER + "," +SymbolsGoodToKnow.KUPFER + "," + SymbolsGoodToKnow.PALLADIUM + "," + SymbolsGoodToKnow.PLATINUM 
//			+ "," + SymbolsGoodToKnow.CRUDE_OEL + "," + SymbolsGoodToKnow.HEIZ_OEL + "," + SymbolsGoodToKnow.GAS + "," + SymbolsGoodToKnow.GASOLINE + SymbolsGoodToKnow.YAHOO_URL_SECOND;
//			yql.execute(yqlQuery);
//			break;
//		case 3:
//			yqlQuery = SymbolsGoodToKnow.YAHOO_URL_FIRST + SymbolsGoodToKnow.EUR_USD + "," + SymbolsGoodToKnow.GBP_USD + "," + SymbolsGoodToKnow.EUR_CHF + "," + SymbolsGoodToKnow.USD_CHF + "," + SymbolsGoodToKnow.USD_JPY + "," + SymbolsGoodToKnow.USD_CAD 
//			+ "," + SymbolsGoodToKnow.EUR_GBR + "," + SymbolsGoodToKnow.AUD_USD + SymbolsGoodToKnow.YAHOO_URL_SECOND;
//			yql.execute(yqlQuery);
//			break;
//		default:
//			break;
//		}
		String[] mmenu = rootView.getResources().getStringArray((R.array.drawer_list_items));
		getActivity().setTitle(mmenu[i]);
		return rootView;
	}
}
