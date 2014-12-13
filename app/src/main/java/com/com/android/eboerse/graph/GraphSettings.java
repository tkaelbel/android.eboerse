package com.com.android.eboerse.graph;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.view.View;

/**
 * dient zur Anzeige des Graphs
 * @author Tok
 *
 */
public class GraphSettings{

	public View getIntent(Context context, ArrayList<HistoricalStockInfo> array) {
		
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		
		TimeSeries series = new TimeSeries(""); 
		for( int i = 0; i < array.size(); i++)
		{
			HistoricalStockInfo stock = array.get(i);
			Date d;
			try {
				d = format1.parse(stock.getDate());
				series.add(d, Double.valueOf(stock.getClose()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		dataset.addSeries(series);
		
		
		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer(); // Holds a collection of XYSeriesRenderer and customizes the graph
		XYSeriesRenderer renderer = new XYSeriesRenderer(); // This will be used to customize line 1
		
		mRenderer.addSeriesRenderer(renderer);
//		mRenderer.setChartTitle("Kurs123");
//		mRenderer.setXTitle("Kurs");
		mRenderer.setAxesColor(Color.BLACK);
		mRenderer.setMarginsColor(Color.WHITE);
		mRenderer.setLabelsColor(Color.BLACK);
		mRenderer.setXLabelsColor(Color.BLACK);
		mRenderer.setYLabelsColor(0, Color.BLACK);
		mRenderer.setPointSize(5);
		mRenderer.setLabelsTextSize(15);
		mRenderer.setChartTitleTextSize(15);
		mRenderer.setYAxisAlign(Align.RIGHT,0);
		//mRenderer.setMargins(new int []{ 0, 15, 20, 30});
			
		
		// Customization time for line 1!
		renderer.setColor(Color.RED);
		renderer.setPointStyle(PointStyle.CIRCLE);
		renderer.setFillPoints(true);
	
	

		View view = ChartFactory.getTimeChartView(context, dataset, mRenderer, "dd-MM-yyyy");
		return view;

	}
}
