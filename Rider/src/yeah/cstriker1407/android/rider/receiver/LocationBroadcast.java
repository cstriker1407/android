package yeah.cstriker1407.android.rider.receiver;

import java.lang.ref.WeakReference;

import yeah.cstriker1407.android.rider.storage.Locations;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class LocationBroadcast 
{
	private static final String ACTION_STR = "LocationReceiver";
	private static final String SPEEDINFO_STR = "Locations.SpeedInfo";
	private static final String LOCDESC_STR = "Locations.LocDesc";
	
	private static class LocationReceiver extends BroadcastReceiver
	{
		private WeakReference<onLocationChangedListener> weak = null;
		private LocationReceiver(onLocationChangedListener listener) {
			super();
			this.weak = new WeakReference<LocationBroadcast.onLocationChangedListener>(listener);
		}
		@Override
		public void onReceive(Context context, Intent intent) 
		{
			onLocationChangedListener listener = weak.get();
			if (null == listener)
			{
				return;
			}
			listener.onLocationChanged((Locations.LocDesc)intent.getSerializableExtra(LOCDESC_STR), 
										(Locations.SpeedInfo)intent.getSerializableExtra(SPEEDINFO_STR));
		}
	}
	
	public static interface onLocationChangedListener
	{
		public void onLocationChanged(Locations.LocDesc locDesc, Locations.SpeedInfo speedInfo);
	}
	
	public static void sendBroadcast(Context context,
			Locations.LocDesc locDesc, Locations.SpeedInfo speedInfo) 
	{
		if (null == context || null == locDesc || null == speedInfo) {
			return;
		}
		Intent intent = new Intent();
		intent.setAction(ACTION_STR);
		intent.putExtra(LOCDESC_STR, locDesc);
		intent.putExtra(SPEEDINFO_STR, speedInfo);
		context.sendBroadcast(intent);
	}

	public static <T extends Context & onLocationChangedListener> void registerReceiver(T context) 
	{
		if (null == context)
		{
			return;
		}
		LocationReceiver receiver = new LocationReceiver(context);
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_STR);
		context.registerReceiver(receiver, filter);
	}
	public static <T extends Context & onLocationChangedListener> void unRegisterReceiver(T context) 
	{
		if (null == context)
		{
			return;
		}
		context.unregisterReceiver(new LocationReceiver(context));
	}
}
