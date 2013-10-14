package net.yeah.cstriker1407;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class MusicService extends Service
{
	
	IServicePlayer.Stub stub = new IServicePlayer.Stub()
	{
		@Override
		public boolean setLoop(boolean loop) throws RemoteException
		{
			Log.d("", "setLoop" + Boolean.toString(loop));
			
			return false;
		}
		
		@Override
		public void seekTo(int current) throws RemoteException
		{
			Log.d("", "" + "seekTo" + current);
			
			Intent intent = new Intent("musicservice");
			intent.putExtra("boolean", false);
			intent.putExtra("int", 1);
			intent.putExtra("string", "hello");
			intent.putExtra("byte[]", new byte[]{1,2,3});
			sendBroadcast(intent);
//			sendOrderedBroadcast(intent, null);
		}
		
		@Override
		public void play() throws RemoteException
		{
			Log.d("", "" + "play");
		}
	};
	
	@Override
	public IBinder onBind(Intent arg0)
	{
		return stub;
	}
}
