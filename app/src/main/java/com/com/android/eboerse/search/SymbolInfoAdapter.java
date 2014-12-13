package com.com.android.eboerse.search;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.com.android.eboerse.R;
import com.com.android.search.json.Result;
/**
 * Listview Adapter fuer die Ergbnisse der Suche
 * @author Tok
 *
 */
public class SymbolInfoAdapter extends ArrayAdapter<Result> {
	private ArrayList<Result> items;
	private SymbolInfoHolder stockInfoHolder;
	private Activity act;

	private class SymbolInfoHolder {
		TextView name;
		TextView symbol;
		TextView value;
	}

	public SymbolInfoAdapter(Context context, int tvResId, ArrayList<Result> items, Activity activity) {
		super(context, tvResId, items);
		this.items = items;
		this.act = activity;
	}


	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) act.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.search_symbols_list_view, null);
			stockInfoHolder = new SymbolInfoHolder();
			stockInfoHolder.name = (TextView)v.findViewById(R.id.stock_name);
			stockInfoHolder.symbol = (TextView)v.findViewById(R.id.stock_symbol);
			v.setTag(stockInfoHolder);
		} else stockInfoHolder = (SymbolInfoHolder)v.getTag(); 

		Result result = items.get(pos);
		if(result != null){
			stockInfoHolder.name.setText(result.getName());
			stockInfoHolder.symbol.setText(result.getSymbol());

		}

		return v;
	}
}

