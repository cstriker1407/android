package net.yeah.cstriker1407;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Receiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context arg0, Intent intent)
	{
		Log.d("", intent.getAction());
		
		Log.d("","" + Boolean.toString(intent.getBooleanExtra("boolean", true)));
		Log.d("","" + intent.getByteArrayExtra("byte[]").toString() );
		
		Log.d("","" + intent.getIntExtra("int", 1000) );
		Log.d("","" + intent.getStringExtra("string") );
	}
}
