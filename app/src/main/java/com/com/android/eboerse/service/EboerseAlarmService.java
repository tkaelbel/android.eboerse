package com.com.android.eboerse.service;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import com.com.android.eboerse.main.MainActivity;
import com.com.android.eboerse.R;
import com.com.android.eboerse.main.SymbolsGoodToKnow;
import com.com.android.eboerse.database.DatabaseHandler;
import com.com.android.eboerse.database.Favorit;
import com.com.android.eboerse.stock.StockInfo;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
/**
 * Service fuer die Favoriten zur Anzeige der Notification wenn Kurs Intervall ueberschritten/unterschritten ist
 * @author Tok
 *
 */
public class EboerseAlarmService extends Service{

	private DatabaseHandler db;
	private NotificationManager mManager;
	private ArrayList<Favorit> list;
	private String yqlQuery;
	private EboerseYqlStockTask yql;
	private Intent intent1;

	private int count = 0;

	private static final String OVER = "ist über";
	private static final String KURS = "Kurs";
	private static final String UNDER = "ist unter";

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.v("EboerseAlarmService", System.currentTimeMillis()
				+ ": EboerseAlarmService erstellt.");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.v("EboerseAlarmService", System.currentTimeMillis()
				+ ": EboerseAlarmService gestartet.");

		mManager = (NotificationManager) this.getApplicationContext().getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);
		intent1 = new Intent(this.getApplicationContext(),MainActivity.class);

		yql = new EboerseYqlStockTask();
		db = new DatabaseHandler(this);
		if(db.isTableExists()){
			list = (ArrayList<Favorit>) db.getAllKurs();

			yqlQuery = SymbolsGoodToKnow.YAHOO_URL_FIRST;

			Favorit favorit = null;
			for(int i = 0; i < list.size(); i++){
				favorit = list.get(i);
				if(i != list.size()){
					yqlQuery = yqlQuery + favorit.getKurs_Symbol() + ",";
				}else{
					yqlQuery = yqlQuery + favorit.getKurs_Symbol();
				}
			}

			yqlQuery = yqlQuery + SymbolsGoodToKnow.YAHOO_URL_SECOND;
			String str = null;
			try {
				str = yql.execute(yqlQuery).get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ArrayList<StockInfo> favoritStocks = null;

			StockInfo stock = null;


			favoritStocks = yql.getStocks();

			Log.v("EboerseAlarmService", System.currentTimeMillis()
					+ ": EboerseAlarmService iteriere.");
			Log.v("EboerseAlarmService", System.currentTimeMillis()
					+ ": EboerseAlarmService yql array gr\\u00f6\\u00dfe" + favoritStocks.size() +".");
			for(int i = 0; i < favoritStocks.size(); i++){
				stock = favoritStocks.get(i);
				Log.v("EboerseAlarmService", System.currentTimeMillis()
						+ ": EboerseAlarmService iteriere.");
				if(i < list.size()){
					Log.v("EboerseAlarmService", System.currentTimeMillis()
							+ ": EboerseAlarmService " + i + "<" + list.size()+ ".");
					favorit = list.get(i);
					String act = stock.getLastTradePriceOnly();
					String unterGrenze = favorit.getUntereGrenze();
					String obereGrenze = favorit.getObereGrenze();

					Double doubleAct = 0.0;
					Double doubleUnterGrenze = 0.0;
					Double doubleObereGrenze = 0.0;

					if(act != null){
						doubleAct = Double.valueOf(act);
					}

					if(unterGrenze != null && !unterGrenze.equals("")){
						doubleUnterGrenze = Double.valueOf(unterGrenze);
					}

					if(obereGrenze != null  && !obereGrenze.equals("")){
						doubleObereGrenze = Double.valueOf(obereGrenze);
					}

					Log.v("EboerseAlarmService", System.currentTimeMillis()
							+ ": EboerseAlarmService vergleiche.");

					if(doubleObereGrenze <= doubleAct && doubleObereGrenze != 0.0){
						Log.v("EboerseAlarmService", System.currentTimeMillis()
								+ ": EboerseAlarmService erzeuge Notification.");
						setupNotification(KURS + " " + stock.getName() + " " + OVER + " " + favorit.getObereGrenze());
					}

					if(doubleUnterGrenze >= doubleAct && doubleUnterGrenze != 0.0){
						Log.v("EboerseAlarmService", System.currentTimeMillis()
								+ ": EboerseAlarmService erzeuge Notification.");
						setupNotification(KURS + " " + stock.getName() + " " + UNDER + " " + favorit.getUntereGrenze());
					}
				}
			}	


		}	
		db.close();
		return START_STICKY;
	}

	private void setupNotification(String msg){
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
		.setSmallIcon(R.drawable.ic_launcher).setContentTitle("eBörse").setContentText(msg).setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(MainActivity.class);
		stackBuilder.addNextIntent(intent1);

		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
		mNotificationManager.notify(count, mBuilder.build());
		count++;
	}

	@Override
	public void onDestroy() {
		Log.v("EboerseAlarmService", System.currentTimeMillis()
				+ ": EboerseAlarmService zerstoert.");
	}

}
