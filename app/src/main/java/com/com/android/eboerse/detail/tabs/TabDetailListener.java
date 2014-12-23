package com.com.android.eboerse.detail.tabs;


import com.com.android.eboerse.main.ConnectionDetector;
import com.com.android.eboerse.R;
import com.com.android.eboerse.main.SymbolsGoodToKnow;
import com.com.android.eboerse.search.DetailView;
import com.com.android.eboerse.search.YqlStockInformation;
import com.com.android.eboerse.webrip.WebripYqlDetailNews;
import com.com.android.eboerse.webrip.WebripYqlExtendedInfos;
import com.com.android.eboerse.webrip.WebripYqlStockIndiceInformation;
import com.com.android.eboerse.webrip.WebripYqlStockTopNews;
import com.com.android.eboerse.webrip.webrip.model.WebripExtendedInfos;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Tabdetaillistener
 * handelt alle tab selektierungen in der Detail-Ansicht
 * @author Tok
 *
 * @param <T>
 */
public class TabDetailListener <T extends Fragment> implements ActionBar.TabListener{

	private DetailView act;
	private String tag;
//	private Class<T> mclass;
	private String symbol;

    //Profil views
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

    //Extended Infos Views
    private TextView branche;
    private TextView industrie;
    private ListView extendedInfos;

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
        WebripYqlStockTopNews ripNews;
        WebripYqlDetailNews detailNews;
        WebripYqlExtendedInfos webripYqlExtendedInfos;

		//		Detail View ansicht - Profil
		if(tag.equals(act.getResources().getString(R.string.detail_profil_tab))){

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
		if(tag.equals(act.getResources().getString(R.string.detail_topflop_tab))){
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
		if(tag.equals(act.getResources().getString(R.string.detail_zusam_tab))){
			act.setContentView(R.layout.detail_zusammen_list_view);
			ListView list = (ListView) act.findViewById(R.id.list_view_detail_zusammenfassen);
			yqlQuery = SymbolsGoodToKnow.YAHOO_URL_IDX_BESTANDTEILE_FIRST + symbol;
			rip = new WebripYqlStockIndiceInformation(act, list, tag);

			if(ConnectionDetector.isConnectingToInternet(act)){
				rip.execute(yqlQuery);
			}
			
		}

        if(tag.equals(act.getResources().getString(R.string.news_tab))){
            act.setContentView(R.layout.list_view);
            ListView list = (ListView) act.findViewById(R.id.list_view);
            detailNews = new WebripYqlDetailNews(act,list, tag);

            if(ConnectionDetector.isConnectingToInternet(act)){
                detailNews.execute(SymbolsGoodToKnow.YAHOO_URL_NEWS + symbol);
            }

        }

        if(tag.equals(act.getResources().getString(R.string.detail_extended_infos))){
            act.setContentView(R.layout.extended_infos_layout);
            branche = (TextView) act.findViewById(R.id.textBranche);
            industrie = (TextView) act.findViewById(R.id.textIndustrie);
            extendedInfos = (ListView) act.findViewById(R.id.listViewExtendedInfos);

            webripYqlExtendedInfos = new WebripYqlExtendedInfos(act, branche, industrie, extendedInfos);

            if(ConnectionDetector.isConnectingToInternet(act)){
                webripYqlExtendedInfos.execute(SymbolsGoodToKnow.YAHOO_URL_BRANCHE + symbol, SymbolsGoodToKnow.YAHOO_URL_BESTANDTEIL +symbol);
            }
        }
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

}
