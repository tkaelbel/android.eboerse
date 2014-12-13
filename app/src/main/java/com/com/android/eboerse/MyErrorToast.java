package com.com.android.eboerse;

import android.content.Context;
import android.widget.Toast;
/**
 * zeigt einen Error Toast auf dem Context
 * @author Tok
 *
 */
public class MyErrorToast{
	
	public static void doToast(Context context, String text, int duration){
		Toast.makeText(context, text, duration).show();
	}
	

}
