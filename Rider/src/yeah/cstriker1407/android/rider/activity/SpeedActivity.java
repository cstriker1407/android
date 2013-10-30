package yeah.cstriker1407.android.rider.activity;

import java.lang.ref.WeakReference;
import java.util.Date;

import org.w3c.dom.Text;

import yeah.cstriker1407.android.rider.R;
import yeah.cstriker1407.android.rider.receiver.LocationBroadcast;
import yeah.cstriker1407.android.rider.receiver.WeatherBroadcast;
import yeah.cstriker1407.android.rider.service.LocationService;
import yeah.cstriker1407.android.rider.storage.Bitmaps;
import yeah.cstriker1407.android.rider.storage.Locations;
import yeah.cstriker1407.android.rider.storage.Locations.LocDesc;
import yeah.cstriker1407.android.rider.storage.Locations.LocDesc.LocTypeEnum;
import yeah.cstriker1407.android.rider.storage.Locations.SpeedInfo;
import yeah.cstriker1407.android.rider.utils.BDUtils;
import yeah.cstriker1407.android.rider.utils.TimeUtils;
import yeah.cstriker1407.android.rider.utils.WeatherUtils;
import yeah.cstriker1407.android.rider.utils.WeatherUtils.WeatherInfo;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.MyLocationOverlay.LocationMode;
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
	
	
	
	private MainActHandler handler = new MainActHandler(this);
	private SpeedSelEnum speedSelEnum = SpeedSelEnum.CUR;
	private WeatherUtils.WeatherInfo weatherInfo = null;
	
	
	
	private BMapManager mBMapMan = null;  
	private MapView mMapView = null;  
	private MapController mMapController = null;
	private MyLocationOverlay myLocationOverlay = null; 
	private LocationData locData = null;
	private LocationMode locationMode = LocationMode.COMPASS;
	
	
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

		Log.d(TAG, locDesc.toString());
		Log.d(TAG, speedInfo.toString());
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
	
	private MKMapViewListener mKMapViewListener = new MKMapViewListener() {
		@Override
		public void onMapMoveFinish() {}
		
		@Override
		public void onMapLoadFinish() {}
		
		@Override
		public void onMapAnimationFinish() {}
		
		@Override
		public void onGetCurrentMap(Bitmap arg0) 
		{
			Bitmaps.writeBitmapToFile(arg0, "/mnt/sdcard/map.png");
		}
		
		@Override
		public void onClickMapPoi(MapPoi arg0) {}
	};

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//注意：请在试用setContentView前初始化BMapManager对象，否则会报错  
		mBMapMan=new BMapManager(getApplicationContext());  
		mBMapMan.init(BDUtils.KEY, null);    
		
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
		GeoPoint point =new GeoPoint((int)(39.915* 1E6),(int)(116.404* 1E6));  
		//用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)  
		mMapController.setCenter(point);//设置地图中心点  
		mMapController.setZoom(15);//设置地图zoom级别  
		
		mMapView.regMapViewListener(mBMapMan, mKMapViewListener);
		
		
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
	}

	private void resumeBDMap() {
		mMapView.onResume();  
		if(mBMapMan!=null){  
		        mBMapMan.start();  
		}
	}
	private void pauseBDMap() {
		mMapView.onPause();  
		if(mBMapMan!=null){  
		       mBMapMan.stop();  
		}
	}  
	private void destroyBDMap() {
		mMapView.destroy();  
		if (mBMapMan != null) 
		{
			mBMapMan.destroy();
			mBMapMan = null;
		}
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
						Log.d(TAG, info.toString());
						
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

	
	private void quitApp()
	{
		Log.e(TAG, "!! QUIT APP CALLED !!");
		LocationService.stopLocationService(this);
		LocationBroadcast.unRegisterReceiver(this, locationReceiver);
		WeatherBroadcast.unRegisterReceiver(this, weatherReceiver);
		destroyBDMap();
		this.finish();
	}

	@Override
	public void onBackPressed() 
	{
		// do nth
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.image_weather:
		case R.id.tv_weather:
		{
			if (weatherInfo != null)
			{
				Toast.makeText(this, weatherInfo.toString(), Toast.LENGTH_SHORT).show();
			}else
			{
				Toast.makeText(this, R.string.weather_no_info, Toast.LENGTH_SHORT).show();
			}
			
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
			break;
		}
		case R.id.btn_morefun:
		{
			break;
		}
		case R.id.btn_quit:
		{
			quitApp();
			break;
		}
		default:
			break;
		}
	}
}
