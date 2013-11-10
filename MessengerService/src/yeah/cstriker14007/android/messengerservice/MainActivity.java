package yeah.cstriker14007.android.messengerservice;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
	private class ActivityHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			Log.d("activity", msg.toString());
		}
	}

	private Messenger mServiceMessenger = null;
	private Messenger mActivityMessenger = new Messenger(new ActivityHandler());

	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			mServiceMessenger = new Messenger(service);

			Message msgToService = new Message();
			msgToService.replyTo = mActivityMessenger;
			msgToService.obj = "this is from activiyt";

			try {
				mServiceMessenger.send(msgToService);
			} catch (RemoteException e) {
				e.printStackTrace();
			}

		}

		public void onServiceDisconnected(ComponentName className) {
			mServiceMessenger = null;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void onStart() {
		super.onStart();
		// 绑定到service
		bindService(new Intent(this, MessengerService.class), mConnection,
				Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onStop() {
		super.onStop();
		unbindService(mConnection);
	}
}
