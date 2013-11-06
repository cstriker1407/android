package yeah.cstriker1407.android.unkillservice;
//http://blog.csdn.net/arui319/article/details/7040980

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

public class TargetService extends Service
{
	private static final String tag = "TargetService";
	
	
	@Override
	public IBinder onBind(Intent arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate()
	{
		Log.e(tag, "onCreate callded");
		
		AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		//包装需要执行Service的Intent
		Intent intent = new Intent(this, this.getClass());
		PendingIntent pendingIntent = PendingIntent.getService(this, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		//触发服务的起始时间
		long triggerAtTime = SystemClock.elapsedRealtime();
		//使用AlarmManger的setRepeating方法设置定期执行的时间间隔（seconds秒）和需要执行的Service
		manager.setRepeating(AlarmManager.ELAPSED_REALTIME, triggerAtTime, 3 * 1000, pendingIntent);

		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		Log.e(tag, "onStartCommand called");

		return START_STICKY;
	}
	
	@Override
	public void onDestroy()
	{
		Log.e(tag, "onDestroy callded");
		super.onDestroy();
	}
}
