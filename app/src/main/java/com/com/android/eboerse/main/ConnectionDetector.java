package com.com.android.eboerse.main;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.com.android.eboerse.R;

/**
 * checkt ob das Device ueberhaupt Internet faehig ist
 * @author Tok
 *
 */
public class ConnectionDetector {
			
	public static boolean isConnectingToInternet(Activity context){
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(cm != null){
			NetworkInfo[] info = cm.getAllNetworkInfo();
			if(info != null)
				for(int i = 0; i < info.length; i++){
					if(info[i].getState() == NetworkInfo.State.CONNECTED){
						return true;
					}
				}
			}
		
		MyErrorToast.doToast(context, context.getResources().getString(R.string.errorMsgNoInet), Toast.LENGTH_LONG);
		return false;
	}
}
