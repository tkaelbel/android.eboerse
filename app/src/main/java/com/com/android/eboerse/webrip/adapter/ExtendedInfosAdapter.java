package com.com.android.eboerse.webrip.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.com.android.eboerse.R;
import com.com.android.eboerse.stock.ArrayAdapterable;
import com.com.android.eboerse.webrip.webrip.model.WebripExtendedInfos;
import com.com.android.eboerse.webrip.webrip.model.WebripStockComponentOf;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by tok on 23.12.2014.
 */
public class ExtendedInfosAdapter extends ArrayAdapter<WebripStockComponentOf>{

    private ArrayList<WebripStockComponentOf> items;
    private Activity act;
    private ListView list;
    private StockInfoViewHolder stockInfoHolder;

    public ExtendedInfosAdapter(Context context, int tvResId, ArrayList<WebripStockComponentOf> items, Activity activity, ListView list) {
        super(context, tvResId, items);
        this.items = items;
        this.act = activity;
        this.list = list;
    }

    private class StockInfoViewHolder {
        TextView name;
        TextView symbol;
        TextView letzerKurs;
        TextView veraendert;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (items != null && items.size() > 0) {
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) act.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.extended_infos_views, null);
                stockInfoHolder = new StockInfoViewHolder();
                stockInfoHolder.name = (TextView) v.findViewById(R.id.textViewNameContent);
                stockInfoHolder.symbol = (TextView) v.findViewById(R.id.textViewSymbolContent);
                stockInfoHolder.letzerKurs = (TextView) v.findViewById(R.id.textViewLetztKursContent);
                stockInfoHolder.veraendert = (TextView) v.findViewById(R.id.textViewVeraendContent);

                v.setTag(stockInfoHolder);
            }else stockInfoHolder = (StockInfoViewHolder) v.getTag();

            WebripStockComponentOf infos = items.get(position);
            if(infos != null){
                stockInfoHolder.name.setText(infos.getName().substring(infos.getName().indexOf(">") + ">".length(), infos.getName().indexOf("</")));
                stockInfoHolder.symbol.setText(infos.getSymbol().substring(infos.getSymbol().indexOf("\">") + "\">".length(), infos.getSymbol().indexOf("</")));
                stockInfoHolder.veraendert.setText(infos.getVeraenderung().substring(infos.getVeraenderung().indexOf(">") + ">".length(), infos.getVeraenderung().length()));
                stockInfoHolder.letzerKurs.setText(infos.getLetzterKurs().substring(infos.getLetzterKurs().indexOf(">") + ">".length(), infos.getLetzterKurs().indexOf("</")));
            }

        }
        return v;
    }
}
