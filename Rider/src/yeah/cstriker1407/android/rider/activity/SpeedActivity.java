package yeah.cstriker1407.android.rider.activity;

import java.lang.ref.WeakReference;
import java.util.Date;

import yeah.cstriker1407.android.rider.R;
import yeah.cstriker1407.android.rider.RiderApplication;
import yeah.cstriker1407.android.rider.receiver.LocationBroadcast;
import yeah.cstriker1407.android.rider.receiver.WeatherBroadcast;
import yeah.cstriker1407.android.rider.service.LocationService;
import yeah.cstriker1407.android.rider.storage.Bitmaps;
import yeah.cstriker1407.android.rider.storage.DBManager;
import yeah.cstriker1407.android.rider.storage.Locations;
import yeah.cstriker1407.android.rider.storage.Locations.LocDesc;
import yeah.cstriker1407.android.rider.storage.Locations.SpeedInfo;
import yeah.cstriker1407.android.rider.utils.BDUtils;
import yeah.cstriker1407.android.rider.utils.TimeUtils;
import yeah.cstriker1407.android.rider.utils.WeatherUtils;
import yeah.cstriker1407.android.rider.utils.WeatherUtils.WeatherInfo;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.MyLocationOverlay.LocationMode;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class SpeedActivity extends Activity implements OnClickListener, LocationBroadcast.onLocationChangedListener,
WeatherBroadcast.onWeatherChangedListener
{
	private static final String TAG = "SpeedActivity";
	
	private TextView tv_weather;
	private ImageButton image_weather;
	
	private TextView tv_speedx;
	private TextView tv_speedy;
	private TextView tv_speedsel;
	private TextView tv_speedunit;
	private TextView tv_totaldistance;
	private TextView tv_currtime;
	
	private ImageButton btn_gotomypos;
	private ImageButton btn_switchlocmode;
	private ImageView   image_locstatus;
	
	
	private Button btn_mapenable;
	private Button btn_posquery;
	private Button btn_morefun;
	private Button btn_quit;
	
	private PopupWindow popupWindow = null;
	
	
	private MainActHandler handler = new MainActHandler(this);
	private SpeedSelEnum speedSelEnum = SpeedSelEnum.CUR;
	private WeatherUtils.WeatherInfo weatherInfo = null;
	
	
	
	private MapView mMapView = null;  
	private MapController mMapController = null;
	private MyLocationOverlay myLocationOverlay = null; 
	private LocationData locData = null;
	private LocationMode locationMode = LocationMode.COMPASS;
	
	private MKSearch mSearch = null;
	
	
	
	
	private RelativeLayout bdMapLayout;
	private ImageView image_zhinanzhen;
	private boolean bdMapVisible = true;/* 默认打开百度地图 */
	private float currentDegree=0f;

	private void setLocMode(LocationMode locMode)
	{
		int imageId;
		switch (locMode) 
		{
			case NORMAL:
			{
				imageId = R.drawable.normal;break;
			}
			case FOLLOWING:
			{
				imageId = R.drawable.following;break;
			}		
			case COMPASS:
			default:
			{
				imageId = R.drawable.compass;break;
			}
		}
		btn_switchlocmode.setImageResource(imageId);
		
		if (myLocationOverlay != null)
		{
			myLocationOverlay.setLocationMode(locMode);
			
			if(LocationMode.COMPASS == locMode)
			{
				myLocationOverlay.enableCompass();
			}else
			{
				myLocationOverlay.disableCompass();
			}
		}
		
		if (mMapController != null)
		{
			if(LocationMode.COMPASS == locMode)
			{
				mMapController.setOverlooking(-45);
			}else
			{
				mMapController.setOverlooking(0);
			}
		}
	}
	
	private BroadcastReceiver weatherReceiver;
	@Override
	public void onWeatherChanged(WeatherInfo info) 
	{
		this.weatherInfo = info;
		tv_weather.setText("" + info.curr_temp +"℃");
		image_weather.setImageResource(WeatherUtils.GetWeatherIcon(info.weatherid));
		if (weatherInfo != null)
		{
			Toast.makeText(this, weatherInfo.toString(), Toast.LENGTH_SHORT).show();
		}else
		{
			Toast.makeText(this, R.string.weather_no_info, Toast.LENGTH_SHORT).show();
		}
	}
	
	
	private BroadcastReceiver locationReceiver;
	@Override
	public void onLocationChanged(LocDesc locDesc, SpeedInfo speedInfo)
	{
		Message locDescMsg = new Message();
		locDescMsg.what = MSG_LOCDESUPDATE;
		locDescMsg.obj = locDesc;
		handler.sendMessage(locDescMsg);
		
		if (bdMapVisible)
		{
			refreshDBMapLocation(locDesc);
		}else
		{
			float degree = locDesc.direction;
	        RotateAnimation ra=new RotateAnimation(currentDegree,-degree,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);  
	        ra.setDuration(200);
	        ra.setFillAfter(true);
	        image_zhinanzhen.startAnimation(ra);  
	        currentDegree=-degree;  
		}
		
		Message speedInfoMsg = new Message();
		speedInfoMsg.what = MSG_SPEEDUPDATE;
		speedInfoMsg.obj = speedInfo;
		handler.sendMessage(speedInfoMsg);

//		Log.d(TAG, locDesc.toString());
//		Log.d(TAG, speedInfo.toString());
	}

	private void refreshDBMapLocation(LocDesc locDesc) {
		locData.latitude = locDesc.latitude;
		locData.longitude = locDesc.longitude;
		// 如果不显示定位精度圈，将accuracy赋值为0即可
		locData.accuracy = locDesc.accuracy;
		locData.direction =locDesc.direction;
		// 更新定位数据
		myLocationOverlay.setData(locData);
		// 更新图层数据执行刷新后生效
		mMapView.refresh();
	}
	
	private MKSearchListener mkSearchListener = new MKSearchListener() {
		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {}
		
		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {}
		
		@Override
		public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {}
		
		@Override
		public void onGetShareUrlResult(MKShareUrlResult arg0, int arg1, int arg2) {}
		
		@Override
		public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {}
		
		@Override
		public void onGetPoiDetailSearchResult(int arg0, int arg1) {}
		
		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult arg0, int arg1) {}
		
		@Override
		public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {}
		
		@Override
		public void onGetAddrResult(MKAddrInfo res, int error) {
			if (error != 0) 
			{
				Log.e(TAG, "GEO查询失败：" + error);
				Toast.makeText(SpeedActivity.this, "查询失败：" + error, Toast.LENGTH_SHORT).show();
				return;
			}
			if (res.type == MKAddrInfo.MK_REVERSEGEOCODE)
			{
				String strInfo = res.strAddr;
				Log.d(TAG, "GEO查询成功：" + strInfo);
				Toast.makeText(SpeedActivity.this, strInfo, Toast.LENGTH_SHORT).show();
				return;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(TAG, "onCreate");
		setContentView(R.layout.activity_speed);
		
		//===
		image_weather = (ImageButton)findViewById(R.id.image_weather);
		tv_weather = (TextView)findViewById(R.id.tv_weather);
		tv_speedx = (TextView)findViewById(R.id.tv_speedx);
		tv_speedy = (TextView)findViewById(R.id.tv_speedy);
		tv_speedsel = (TextView)findViewById(R.id.tv_speedsel);
		tv_speedunit = (TextView)findViewById(R.id.tv_speedunit);

		image_weather.setOnClickListener(this);
		tv_weather.setOnClickListener(this);
		
		tv_speedx.setOnClickListener(this);
		tv_speedy.setOnClickListener(this);
		tv_speedsel.setOnClickListener(this);
		tv_speedunit.setOnClickListener(this);
		
		tv_totaldistance = (TextView)findViewById(R.id.tv_totaldistance);
		tv_currtime = (TextView)findViewById(R.id.tv_currtime);
		
		Typeface lcdTf = Typeface.createFromAsset(getAssets(),"fonts/LCD.ttf");
		tv_speedx.setTypeface(lcdTf);
		tv_speedy.setTypeface(lcdTf);
		tv_speedunit.setTypeface(lcdTf);
		tv_totaldistance.setTypeface(lcdTf);
		
		
		btn_gotomypos = (ImageButton)findViewById(R.id.btn_gotomypos);
		btn_switchlocmode = (ImageButton)findViewById(R.id.btn_switchlocmode);
		btn_gotomypos.setOnClickListener(this);
		btn_switchlocmode.setOnClickListener(this);
		
		image_locstatus = (ImageView)findViewById(R.id.image_locstatus);
		
		
		btn_mapenable = (Button)findViewById(R.id.btn_mapenable);
		btn_posquery = (Button)findViewById(R.id.btn_posquery);
		btn_morefun = (Button)findViewById(R.id.btn_morefun);
		btn_quit = (Button)findViewById(R.id.btn_quit);
		
		btn_mapenable.setOnClickListener(this);
		btn_posquery.setOnClickListener(this);
		btn_morefun.setOnClickListener(this);
		btn_quit.setOnClickListener(this);
		
		image_zhinanzhen = (ImageView)findViewById(R.id.image_zhinanzhen);
		image_zhinanzhen.setVisibility(View.INVISIBLE);
		bdMapLayout = (RelativeLayout)findViewById(R.id.bdmaplayout);
		
		
		//===

		locationReceiver = LocationBroadcast.registerReceiver(this);
		weatherReceiver = WeatherBroadcast.registerReceiver(this);
		bindService();
		
		initBDMapSettings();
		
		handler.sendEmptyMessage(MSG_TIMEUPDATE);
	}

	private void initBDMapSettings() {
		mMapView=(MapView)findViewById(R.id.bdmapview);  
		mMapView.setBuiltInZoomControls(true);
		mMapView.showScaleControl(true);
		//设置启用内置的缩放控件  
		mMapController=mMapView.getController();  
		// 得到mMapView的控制权,可以用它控制和驱动平移和缩放  
		
		Locations.LocDesc lastLoc= DBManager.Instance.queryLastLocation();
		GeoPoint point =new GeoPoint((int)(lastLoc.latitude* 1E6),(int)(lastLoc.longitude* 1E6));  
		//用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)  
		mMapController.setCenter(point);//设置地图中心点  
		mMapController.setZoom(15);//设置地图zoom级别  
		
		
        locData = new LocationData();
        //定位图层初始化
		myLocationOverlay = new MyLocationOverlay(mMapView);
		//设置定位数据
	    myLocationOverlay.setData(locData);
	    setLocMode(locationMode);

		//添加定位图层
		mMapView.getOverlays().add(myLocationOverlay);
		//修改定位数据后刷新图层生效
		mMapView.refresh();
		
		
		/* 初始化搜索 */
		mSearch = new MKSearch();
		mSearch.init(RiderApplication.APP.mBMapMan, mkSearchListener);
		
	}

	private void resumeBDMap() {
		mMapView.onResume();  
		if(RiderApplication.APP.mBMapMan != null){  
			RiderApplication.APP.mBMapMan.start();  
		}
	}
	private void pauseBDMap() {
		mMapView.onPause();  
		if(RiderApplication.APP.mBMapMan != null){  
			RiderApplication.APP.mBMapMan.stop();  
		}
	}  
	private void destroyBDMap() {
		mMapView.destroy();
	}

	@Override  
	protected void onResume(){  
	        resumeBDMap();  
	       super.onResume();  
	}

	@Override  
	protected void onPause(){  
	        pauseBDMap();  
	        super.onPause();  
	}


	@Override
	protected void onDestroy() {
		Log.e(TAG, "onDestroy");
		super.onDestroy();
//		System.exit(0);
	}
	
	private static enum SpeedSelEnum
	{
		MIN("MIN"),
		MAX("MAX"),
		AVG("AVG"),
		CUR("CUR");
		private String speedDes;
		private SpeedSelEnum(String speedDes)
		{
			this.speedDes = speedDes;
		}
		public String getSpeedDes() {
			return speedDes;
		}
		
		public SpeedSelEnum getNext()
		{
			int len = SpeedSelEnum.values().length;
			int newIdx = (this.ordinal() + 1) % len;
			return SpeedSelEnum.values()[newIdx];
		}
		
	}
	
	/* MSG_INDEX */
	private static final int MSG_SPEEDUPDATE = 0;
	private static final int MSG_TIMEUPDATE = 1;
	private static final int MSG_LOCDESUPDATE = 2;
	
	private static class MainActHandler extends Handler 
	{
		private WeakReference<SpeedActivity> activity = null;

		public MainActHandler(SpeedActivity act) {
			super();
			this.activity = new WeakReference<SpeedActivity>(act);
		}

		@Override
		public void handleMessage(Message msg) 
		{
			SpeedActivity act = activity.get();
			if (null == act) {
				return;
			}
			switch (msg.what) 
			{
				case MSG_TIMEUPDATE:
				{
					act.tv_currtime.setText(TimeUtils.fmtDate2Str(new Date(), " yyyy-MM-dd hh:mm:ss a"));
					this.sendEmptyMessageDelayed(MSG_TIMEUPDATE, 1000);
					break;
				}
				case MSG_LOCDESUPDATE:
				{
					Locations.LocDesc locDesc = (Locations.LocDesc)msg.obj;
					act.image_locstatus.setImageResource(locDesc.typeEnum.getImageId());
					break;
				}
				
				case MSG_SPEEDUPDATE:
				{
					Locations.SpeedInfo info = (SpeedInfo) msg.obj;
					if (info != null)
					{
//						Log.d(TAG, info.toString());
						
						float speedM = 0.0f;
						switch (act.speedSelEnum) 
						{
							case MIN: 
							{
								speedM = info.minSpeedM * 3.6f;break;
							}
							case MAX: 
							{
								speedM = info.maxSpeedM * 3.6f;break;
							}
							case AVG: 
							{
								speedM = info.averageSpeedM * 3.6f;break;
							}
							case CUR:
							default:
							{
								speedM = info.singleSpeedM * 3.6f;break;
							}
						}
						int x = (int)speedM;
						int y = (int) ((speedM - x)*100);
						act.tv_speedx.setText(String.format("%02d", x));
						act.tv_speedy.setText(String.format("%02d", y));
						
						float totalDiatanceKM = info.totalDistanceM/1000.0f;
						String totalDistance = String.format("%.3fKM/%s", totalDiatanceKM, TimeUtils.fmtMs2Str(info.totalSeconds*1000, "HH:mm:ss"));
						act.tv_totaldistance.setText(totalDistance);
						act.tv_speedsel.setText(act.speedSelEnum.getSpeedDes());//当前速度描述
					}
					
					break;
				}
			}
		}
	}

	
	private void destroyRes()
	{
		unbindService();
		LocationBroadcast.unRegisterReceiver(this, locationReceiver);
		WeatherBroadcast.unRegisterReceiver(this, weatherReceiver);
		destroyBDMap();
	}
	
	
	private void quitApp()
	{
		Log.e(TAG, "!! QUIT APP CALLED !!");

		destroyRes();
		LocationService.stopLocationService(this);
		DBManager.Instance.closeDB();
//		RiderApplication.APP.mBMapMan.destroy();
		this.finish();
	}

	@Override
	public void onBackPressed() 
	{
		showQuitDialog();
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.image_weather:
		case R.id.tv_weather:
		{
			sendWeatherReq();
			break;
		}
		
		case R.id.tv_speedx:
		case R.id.tv_speedy:
		case R.id.tv_speedsel:
		case R.id.tv_speedunit:
		{
//			mMapView.getCurrentMap();
			speedSelEnum = speedSelEnum.getNext();
			tv_speedsel.setText(speedSelEnum.getSpeedDes());
			break;
		}
		
		case R.id.btn_gotomypos:
		{
			if (mMapController != null && locData != null)
			{
				mMapController.animateTo(new GeoPoint((int)(locData.latitude* 1e6), (int)(locData.longitude *  1e6)));
				mMapController.setRotation(1);
			}
			break;
		}
		case R.id.btn_switchlocmode:
		{
			switch (locationMode) 
			{
				case COMPASS:
				{
					locationMode = LocationMode.FOLLOWING;
					break;
				}
				case FOLLOWING:
				{
					locationMode = LocationMode.NORMAL;
					break;
				}
				case NORMAL:
				default:
				{
					locationMode = LocationMode.COMPASS;
					break;
				}
			}
			setLocMode(locationMode);
			break;
		}
		
		case R.id.btn_mapenable:
		{
			if (bdMapVisible)
			{
				image_zhinanzhen.setVisibility(View.VISIBLE);
				bdMapLayout.setVisibility(View.INVISIBLE);
				pauseBDMap();
			}else
			{
				currentDegree = 0;
				image_zhinanzhen.clearAnimation();
				image_zhinanzhen.setVisibility(View.INVISIBLE);
				bdMapLayout.setVisibility(View.VISIBLE);
				resumeBDMap();
			}
			bdMapVisible = !bdMapVisible;

			break;
		}
		case R.id.btn_posquery:
		{
			GeoPoint ptCenter = new GeoPoint((int)(locData.latitude*1e6), (int)(locData.longitude*1e6));
			mSearch.reverseGeocode(ptCenter);
			break;
		}
		case R.id.btn_morefun:
		{
			showFunPop();
			break;
		}
		case R.id.btn_quit:
		{
			showQuitDialog();
			break;
		}
		case R.id.btn_beginride:
		{
			popupWindow.dismiss();
			
			Intent intent = new Intent(this, BeginRideActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.btn_ridestat:
		{
			popupWindow.dismiss();
			destroyRes();
			
			Intent intent = new Intent(this, RideStatActivity.class);
			startActivity(intent);
			finish();
			break;
		}
		case R.id.btn_setting:
		{
			popupWindow.dismiss();
			
			Intent intent = new Intent(this, SettingActivity.class);
			startActivity(intent);
			break;
		}
		default:
			break;
		}
	}
	
	private void showQuitDialog()
	{
		new AlertDialog.Builder(this)
				.setMessage(R.string.msg_surequit)
				.setPositiveButton(R.string.btn_quit, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) 
					{
						SpeedActivity.this.quitApp();
					}
				})
				.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) 
					{
						dialog.dismiss();
					}
				}).create().show();
	}
	
	private void showFunPop()
	{
		View view = View.inflate(this, R.layout.pop_menu, null);
		Button btn_beginride = (Button)view.findViewById(R.id.btn_beginride);
		btn_beginride.setOnClickListener(this);
		Button btn_ridestat = (Button)view.findViewById(R.id.btn_ridestat);
		btn_ridestat.setOnClickListener(this);
		Button btn_setting = (Button)view.findViewById(R.id.btn_setting);
		btn_setting.setOnClickListener(this);
		
		popupWindow  = new PopupWindow(view, LayoutParams.MATCH_PARENT, 150, true);
		popupWindow.setContentView(view);
		popupWindow.setTouchable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable( new BitmapDrawable(getResources(), (Bitmap) null));
		popupWindow.showAtLocation(findViewById(R.id.root_speed), Gravity.BOTTOM,0, 80);  
	}
	
	
	//绑定service
	private ServiceConnection sc = new ServiceConnection() {
		public void onServiceDisconnected(ComponentName name) 
		{
			isBind = false;
		}
		public void onServiceConnected(ComponentName name, IBinder service) 
		{
			isBind = true;
			locationBinder = (LocationService.LocationBinder)service;
		}
	};
	private boolean isBind = false; 
	private LocationService.LocationBinder locationBinder = null;

	private void bindService()
	{
		if (!isBind)
		{
			bindService(new Intent(SpeedActivity.this, LocationService.class), sc, Context.BIND_AUTO_CREATE); 
		}
	}
	private void unbindService()
	{
		if (isBind)
		{
			unbindService(sc); 
		}
	}
	private void sendWeatherReq()
	{
		if (locationBinder != null)
		{
			Log.d(TAG, "查询天气请求已发出");
			locationBinder.sendWeatherReq();
		}
		else
		{
			Log.e(TAG, "locationBinder is null");
		}
	}
}
