package com.com.android.eboerse.service;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.util.Log;
/**
 * wird nicht benutzt(ansatz war das starten des service bei der installation der anwendung bzw. beim starten des device)
 * @author Tok
 *
 */
@Deprecated
public class EboerseReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context arg0, Intent arg1) {
//		if(arg1.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
			Log.v("EboerseAlarmService", " Boot komplett.");

			Intent intent = new Intent(arg0, EboerseAlarmService.class);

			PendingIntent pendingIntent = PendingIntent.getService(arg0, 0, intent, 0);

			long interval = DateUtils.MINUTE_IN_MILLIS * 5;
			long firstStart = System.currentTimeMillis() + interval;


			AlarmManager am = (AlarmManager) arg0 .getSystemService(Context.ALARM_SERVICE);
            am.setInexactRepeating(AlarmManager.RTC, firstStart, 
                    interval, pendingIntent);
 
            Log.v("EboerseAlarmService", "AlarmManager gesetzt");
//		}
	}


}
