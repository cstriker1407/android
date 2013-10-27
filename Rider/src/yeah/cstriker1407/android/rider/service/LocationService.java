package yeah.cstriker1407.android.rider.service;

import java.lang.ref.WeakReference;

import yeah.cstriker1407.android.rider.receiver.LocationBroadcast;
import yeah.cstriker1407.android.rider.receiver.WeatherBroadcast;
import yeah.cstriker1407.android.rider.storage.Locations;
import yeah.cstriker1407.android.rider.storage.Locations.LocDesc.LocTypeEnum;
import yeah.cstriker1407.android.rider.utils.BDUtils;
import yeah.cstriker1407.android.rider.utils.HttpUtils;
import yeah.cstriker1407.android.rider.utils.HttpUtils.onHttpResultListener;
import yeah.cstriker1407.android.rider.utils.WeatherUtils;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class LocationService extends Service implements onHttpResultListener 
{
	public static void startLocationService(Context context)
	{
		if (null == context)
		{
			return;
		}
		context = context.getApplicationContext();
		context.startService(new Intent(context, LocationService.class));
	}
	public static void stopLocationService(Context context)
	{
		if (null == context)
		{
			return;
		}
		context = context.getApplicationContext();
		context.stopService(new Intent(context, LocationService.class));
	}
	
	private static final String TAG = "LocationService";
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() 
	{
		Log.e(TAG, "The LocationService Is onCreate");
		super.onCreate();
		initBDLocationSettings();
		registerSensorMgr();
		sendWeatherReq();
		
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
//		return super.onStartCommand(intent, flags, startId);
		Log.e(TAG, "The LocationService Is onStartCommand");
		startBDLocationFunc();
		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() 
	{
		Log.e(TAG, "The LocationService Is onDestroy");
		stopBDLocationFunc();
		unregisterSensorMgr();
		cancelWeatherReq();
		
		super.onDestroy();
	}
	
	
	
	
	//---------百度定位相关函数--------//
		
	private LocationClient mLocationClient = null;
	private BDLocationListener bdLocationListener = new BDLocationListener() {
		@Override
		public void onReceiveLocation(BDLocation location)
		{
			if (location == null)
				return;
			Locations.LocDesc locDesc = Locations.getInstance().getLocDes(
					null, location.getLatitude(),location.getLongitude(),location.getRadius(),degree,BDUtils.BD2LocTypeEnum(location));
			Log.d(TAG,locDesc.toString());
			
			Locations.SpeedInfo speedInfo = Locations.getInstance().calcSpeedInfo(
					location.getLatitude(),
					location.getLongitude());
			Log.d(TAG,speedInfo.toString());
			
			LocationBroadcast.sendBroadcast(LocationService.this, locDesc, speedInfo);

		}
		@Override
		public void onReceivePoi(BDLocation poiLocation) {}
	};
	
	private void initBDLocationSettings() {
		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(bdLocationListener); // 注册监听函数
		mLocationClient.setAK(BDUtils.KEY);
	
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("nothing");// 返回的定位结果不需要地址信息，使用其他的api获取地址信息。
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(BDUtils.SCANSPAN);// 设置发起定位请求的间隔时间为5000ms
		option.disableCache(true);// 禁止启用缓存定位
		option.setPoiExtraInfo(false); // 是否需要POI的电话和地址等详细信息
		option.setPriority(LocationClientOption.GpsFirst);
		mLocationClient.setLocOption(option);
	}

	private void startBDLocationFunc() {
		if (mLocationClient != null && (!mLocationClient.isStarted()))
		{
			mLocationClient.start();
			mLocationClient.requestLocation();
		}
	}

	private void stopBDLocationFunc() {
		if (mLocationClient != null && mLocationClient.isStarted()) 
		{
			mLocationClient.stop();
		}
	}
	
	
	//-------指南针相关函数--------//
	private SensorManager mSensorManager;
	private float degree = 0.0f;
	private SensorEventListener mSensorEventListener = new SensorEventListener() {
        
        @Override
        public void onSensorChanged(SensorEvent event) {
        	degree = event.values[0];
//            Log.d("", "" + degree);
        }
        
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) { }
    };
    
    private void registerSensorMgr()
    {
    	if (mSensorManager != null)
    	{
    		return;
    	}
    	
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorEventListener,  
                mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),  
                SensorManager.SENSOR_DELAY_NORMAL);  
    }
	
    private void unregisterSensorMgr()
    {
    	if (null == mSensorManager)
    	{
    		return;
    	}
    	mSensorManager.unregisterListener(mSensorEventListener);
    }
    
    
    
    
    
    //---------天气预报相关函数--------//
    private void sendWeatherReq()
    {
    	HttpUtils.sendGetRequest(WeatherUtils.URL_Service, this, 0);
    	handler.sendEmptyMessageDelayed(MSG_WEATHER, WeatherUtils.WEATHER_SPAN);
    }
    private void cancelWeatherReq()
    {
    	handler.removeMessages(MSG_WEATHER);
    }   
    
    
    private static final int MSG_WEATHER = 0;
	public void handleMessage(Message msg) 
	{
		switch (msg.what) {
		case MSG_WEATHER:
		{
			sendWeatherReq();
			break;
		}
		default:
			break;
		}
		
	}
   
	private LocationSerHandler handler = new LocationSerHandler(this);
    private static class LocationSerHandler extends Handler
    {
    	private WeakReference<LocationService> service = null;
    	private LocationSerHandler(LocationService ls) {
			super();
			this.service = new WeakReference<LocationService>(ls);
		}
    	@Override
		public void handleMessage(Message msg) 
		{
    		LocationService ls = service.get();
    		if (ls != null)
    		{
    			ls.handleMessage(msg);
    		}
		}
    }
	@Override
	public void onHttpResult(String result, int id)
	{
		WeatherUtils.WeatherInfo info = WeatherUtils.FmtWeatherFromJson(result);
		
		if (info != null)
		{
			Log.d(TAG, info.toString());
			WeatherBroadcast.sendBroadcast(this, info);
		}
		
		
	}
    
}
