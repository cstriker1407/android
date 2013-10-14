package net.yeah.cstriker1407;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	 IServicePlayer iPlayer;
	
	private ServiceConnection conn = new ServiceConnection()
	{
		public void onServiceConnected(ComponentName className, IBinder service)
		{
			iPlayer = IServicePlayer.Stub.asInterface(service);
		}

		public void onServiceDisconnected(ComponentName className)
		{
			iPlayer = null;
		};
	};
	
	protected void onDestroy()
	{
		super.onDestroy();
		unbindService(conn);  
	}




	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        bindService(new Intent(MainActivity.this, MusicService.class), conn, Context.BIND_AUTO_CREATE);
        startService(new Intent(MainActivity.this, MusicService.class));
        
        ((Button)findViewById(R.id.button1)).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				try
				{
					iPlayer.play();
					iPlayer.setLoop(true);
					iPlayer.seekTo(100);
				}
				catch (RemoteException e)
				{
					e.printStackTrace();
				}
			}
		});
    }
}
