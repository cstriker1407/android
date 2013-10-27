package yeah.cstriker1407.android.rider.receiver;

import java.lang.ref.WeakReference;

import yeah.cstriker1407.android.rider.storage.Locations;
import yeah.cstriker1407.android.rider.utils.WeatherUtils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class WeatherBroadcast
{
	private static final String ACTION_STR = "WeatherReceiver";
	private static final String WEATHERINFO_STR = "WeatherUtils.WeatherInfo";
	
	private static class WeatherReceiver extends BroadcastReceiver
	{
		private WeakReference<onWeatherChangedListener> weak = null;
		private WeatherReceiver(onWeatherChangedListener listener) {
			super();
			this.weak = new WeakReference<WeatherBroadcast.onWeatherChangedListener>(listener);
		}
		@Override
		public void onReceive(Context context, Intent intent) 
		{
			onWeatherChangedListener listener = weak.get();
			if (null == listener)
			{
				return;
			}
			listener.onWeatherChanged((WeatherUtils.WeatherInfo)intent.getSerializableExtra(WEATHERINFO_STR));
		}
	}
	
	public static interface onWeatherChangedListener
	{
		public void onWeatherChanged(WeatherUtils.WeatherInfo info);
	}
	
	public static void sendBroadcast(Context context,WeatherUtils.WeatherInfo info) 
	{
		if (null == context || null == info) 
		{
			return;
		}
		Intent intent = new Intent();
		intent.setAction(ACTION_STR);
		intent.putExtra(WEATHERINFO_STR, info);
		context.sendBroadcast(intent);
	}

	public static <T extends Context & onWeatherChangedListener> BroadcastReceiver registerReceiver(T context) 
	{
		if (null == context)
		{
			return null;
		}
		WeatherReceiver receiver = new WeatherReceiver(context);
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_STR);
		context.registerReceiver(receiver, filter);
		return receiver;
	}
	public static <T extends Context & onWeatherChangedListener> void unRegisterReceiver(T context, BroadcastReceiver rev) 
	{
		if (null == context || null == rev)
		{
			return;
		}
		try {
			context.unregisterReceiver(rev);
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
