package com.com.android.stock.indices;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.com.android.eboerse.R;
import com.com.android.eboerse.search.DetailView;

/**
 * adapter für listview der top/flops/zusammensetzung tabs
 * @author Tok
 *
 */
@Deprecated
public class StockIndiceInfoAdapter extends ArrayAdapter<StockIndiceInfo> implements OnItemClickListener{
	
	private ArrayList<StockIndiceInfo> items;
	private StockInfoViewHolder stockInfoHolder;
	private Activity act;
	private ListView list;
	private SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
	
	private class StockInfoViewHolder {
		TextView name;
		TextView infos; 
	}
	
	public StockIndiceInfoAdapter(Context context, int resource, ArrayList<StockIndiceInfo> items, Activity activity, ListView list) {
		super(context, resource, items);
		this.items = items;
		this.act = activity;
		this.list = list;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if(act instanceof DetailView){
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) act.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.listview_items_uebersicht, null);
				stockInfoHolder = new StockInfoViewHolder();
				stockInfoHolder.name = (TextView)v.findViewById(R.id.stock_name);
				stockInfoHolder.infos = (TextView)v.findViewById(R.id.stock_infos);
				v.setTag(stockInfoHolder);
			} else stockInfoHolder = (StockInfoViewHolder)v.getTag(); 

			StockIndiceInfo stock = items.get(position);

			if (stock != null) {
				Date d = new Date();
				String date = format1.format(d);
				stockInfoHolder.name.setText(stock.getCol1());
				stockInfoHolder.infos.setText(date + "       " + stock.getCol2() + "      " + stock.getCol3());
			}
		}
		
		return v;
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Object obj = list.getItemAtPosition(arg2);
		StockIndiceInfo rst = (StockIndiceInfo) obj;
		showDetailView(rst.getCol0());
	}
	
	private void showDetailView(String symbol){
		Intent intent = new Intent(act, DetailView.class);
		intent.putExtra("symbol", symbol);
		act.startActivity(intent);
	}

}
