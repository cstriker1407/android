package yeah.cstriker1407.android.rider;

import java.lang.ref.WeakReference;

import yeah.cstriker1407.android.rider.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class WelcomeActivity extends Activity {
	private static final String TAG = "WelcomeActivity";
	private static final int MSG_SLEEP = 0;
	private static final int SLEEP_TIME = 1000;

	private WelcomeActHandler handler = new WelcomeActHandler(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		handler.sendEmptyMessageDelayed(MSG_SLEEP, SLEEP_TIME);
	}

	@Override
	protected void onDestroy() {
		Log.e(TAG, "on des called");
		super.onDestroy();
	}

	private static class WelcomeActHandler extends Handler {
		private WeakReference<WelcomeActivity> activity = null;

		public WelcomeActHandler(WelcomeActivity act) {
			super();
			this.activity = new WeakReference<WelcomeActivity>(act);
		}

		@Override
		public void handleMessage(Message msg) {
			WelcomeActivity act = activity.get();
			if (null == act) {
				return;
			}

			switch (msg.what) {
			case MSG_SLEEP:
			{
				act.startActivity(new Intent(act, MainActivity.class));
				act.finish();
				break;
			}
			default: {
				break;
			}
			}
		}
	}

}