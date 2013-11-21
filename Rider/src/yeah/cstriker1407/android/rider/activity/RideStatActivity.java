package yeah.cstriker1407.android.rider.activity;

import java.lang.ref.WeakReference;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.BasicStroke;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import yeah.cstriker1407.android.rider.R;
import yeah.cstriker1407.android.rider.RiderApplication;
import yeah.cstriker1407.android.rider.receiver.LocationBroadcast;
import yeah.cstriker1407.android.rider.storage.Bitmaps;
import yeah.cstriker1407.android.rider.storage.DBManager;
import yeah.cstriker1407.android.rider.storage.Locations;
import yeah.cstriker1407.android.rider.storage.Locations.LocDesc;
import yeah.cstriker1407.android.rider.storage.Locations.SpeedInfo;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.baidu.mapapi.map.Geometry;
import com.baidu.mapapi.map.Graphic;
import com.baidu.mapapi.map.GraphicsOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.Symbol;
import com.baidu.mapapi.map.MyLocationOverlay.LocationMode;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.UMImage;

public class RideStatActivity extends SherlockActivity implements LocationBroadcast.onLocationChangedListener, OnClickListener
{
	final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share",
            RequestType.SOCIAL);

	
	private static final String TAG = "RideStatActivity";
	
	private FrameLayout speed_graph;
	private XYMultipleSeriesDataset mSpeedDataset = new XYMultipleSeriesDataset();
	private XYMultipleSeriesRenderer mSpeedRenderer = new XYMultipleSeriesRenderer();
	private GraphicalView mChartView;
	
	
	
	
	private ImageButton btn_gotomypos;
	private ImageButton btn_switchlocmode;
	private ImageView   image_locstatus;
	
	private Button btn_sharestat;
	private Button btn_resetstat;
	
	private RideStatActHandler handler = new RideStatActHandler(this);
	
	private MapView mMapView = null;  
	private MapController mMapController = null;
	private MyLocationOverlay myLocationOverlay = null;
	private GraphicsOverlay graphicsOverlay = null;
	
	
	private LocationData locData = null;
	private LocationMode locationMode = LocationMode.COMPASS;
	
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
	
	private BroadcastReceiver locationReceiver;
	@Override
	public void onLocationChanged(LocDesc locDesc, SpeedInfo speedInfo)
	{
		Message locDescMsg = new Message();
		locDescMsg.what = MSG_LOCDESUPDATE;
		locDescMsg.obj = locDesc;
		handler.sendMessage(locDescMsg);
		
		refreshDBMapLocation(locDesc);
//		Log.d(TAG, locDesc.toString());
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
//			mController.setShareMedia(new UMImage(RideStatActivity.this, arg0));
			 mController.postShare(RideStatActivity.this, SHARE_MEDIA.SINA,
	                    new SnsPostListener() {
	 
	                    @Override
	                    public void onStart() {
	                        Toast.makeText(RideStatActivity.this, "分享开始",Toast.LENGTH_SHORT).show();
	                    }
	 
	                    @Override
	                    public void onComplete(SHARE_MEDIA platform,int eCode, SocializeEntity entity) {
	                        if(eCode == StatusCode.ST_CODE_SUCCESSED){
	                            Toast.makeText(RideStatActivity.this, "分享成功",Toast.LENGTH_SHORT).show();
	                        }else{
	                            Toast.makeText(RideStatActivity.this, "分享失败",Toast.LENGTH_SHORT).show();
	                        }
	                    }
	            });
		}
		@Override
		public void onClickMapPoi(MapPoi arg0) {}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(TAG, "onCreate");
		setContentView(R.layout.activity_ridestat);
		
		speed_graph = (FrameLayout) findViewById(R.id.speed_graph);
		
		btn_gotomypos = (ImageButton)findViewById(R.id.btn_gotomypos);
		btn_switchlocmode = (ImageButton)findViewById(R.id.btn_switchlocmode);
		btn_gotomypos.setOnClickListener(this);
		btn_switchlocmode.setOnClickListener(this);
		
		btn_sharestat = (Button)findViewById(R.id.btn_sharestat);
		btn_resetstat = (Button)findViewById(R.id.btn_resetstat);
		btn_sharestat.setOnClickListener(this);
		btn_resetstat.setOnClickListener(this);
		
		image_locstatus = (ImageView)findViewById(R.id.image_locstatus);
		
		locationReceiver = LocationBroadcast.registerReceiver(this);
		initBDMapSettings();
		
		TextView title = new TextView(getSupportActionBar().getThemedContext());
		title.setText(R.string.btn_ridestat);
		title.setTextSize(30f);
		title.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
		title.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		getSupportActionBar().setCustomView(title);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		
		handler.sendEmptyMessageDelayed(MSG_SPEEDGRAPHIC, 2000);
	}

	private void initSpeedGraphic()
	{
		mSpeedDataset.clear();
		XYSeries speedSeries = new XYSeries("SPEED");
		
		List<Locations.SpeedInfo> speeds = DBManager.Instance.querySpeedByBelongId(0);
		int numberSpeed = speeds.size();
		if (numberSpeed > 100)
			numberSpeed = 100;
		
		for (int i = 0; i < numberSpeed; i++)
		{
			speedSeries.add(i, speeds.get(i).singleSpeedM);
		}
		mSpeedDataset.addSeries(speedSeries);

		int maxSpeed = ((int) (speedSeries.getMaxY()) / 5 + 1) * 5;
		for (int i = 5; i <= maxSpeed; i = i + 5) {
			XYSeries series = new XYSeries("" + i);
			for (int j = 0; j < numberSpeed; j++) {
				series.add(j, i);
			}
			mSpeedDataset.addSeries(series);
		}

		mSpeedRenderer.setApplyBackgroundColor(false);
		mSpeedRenderer.setLabelsTextSize(15);
		mSpeedRenderer.setMargins(new int[] { 5, 5, 5, 5 });
		mSpeedRenderer.setZoomButtonsVisible(false);//不显示ZOOM button
		mSpeedRenderer.setXLabels(0);
//		mSpeedRenderer.setXTitle("SPEED 曲线                  ");
		mSpeedRenderer.setXLabelsAlign(Align.LEFT);
		mSpeedRenderer.setAxisTitleTextSize(35);// 标签大小
		mSpeedRenderer.setYAxisMax(maxSpeed + 3);
		mSpeedRenderer.setYAxisMin(0);
		mSpeedRenderer.setLabelsTextSize(20);

		mSpeedRenderer.setYAxisAlign(Align.LEFT, 0);// Y轴在左边
		mSpeedRenderer.setYLabelsAlign(Align.RIGHT, 0);// Y轴标签在左边
		mSpeedRenderer.removeAllRenderers();
		XYSeriesRenderer speedRenderer = new XYSeriesRenderer();
		speedRenderer.setPointStyle(PointStyle.POINT);
		speedRenderer.setFillPoints(false);
		speedRenderer.setLineWidth(speedRenderer.getLineWidth() * 3);
		speedRenderer.setStroke(BasicStroke.SOLID);// 实线
		speedRenderer.setShowLegendItem(false);// 不显示图例
		speedRenderer.setDisplayChartValues(false);// 不显示数据
		speedRenderer.setHighlighted(true);

		mSpeedRenderer.addSeriesRenderer(speedRenderer);

		for (int i = 5; i <= maxSpeed; i = i + 5) {
			XYSeriesRenderer renderer = new XYSeriesRenderer();
			renderer.setPointStyle(PointStyle.POINT);
			renderer.setFillPoints(false);
			renderer.setStroke(BasicStroke.DASHED);// 虚线
			renderer.setShowLegendItem(false);// 不显示图例
			renderer.setDisplayChartValues(false);// 不显示数据
			renderer.setColor(Color.RED);

			mSpeedRenderer.addSeriesRenderer(renderer);
		}
		mChartView = ChartFactory.getLineChartView(this, mSpeedDataset,
				mSpeedRenderer);
//		speed_graph.removeAllViews();
		speed_graph.addView(mChartView, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		mChartView.repaint();
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
		mMapView.regMapViewListener(RiderApplication.APP.mBMapMan, mKMapViewListener);
		
		
        locData = new LocationData();
        //定位图层初始化
		myLocationOverlay = new MyLocationOverlay(mMapView);
		//设置定位数据
	    myLocationOverlay.setData(locData);
	    setLocMode(locationMode);

		//添加定位图层
		mMapView.getOverlays().add(myLocationOverlay);
		
		graphicsOverlay = new GraphicsOverlay(mMapView);
		graphicsOverlay.setData(getRouteLine());
		mMapView.getOverlays().add(graphicsOverlay);
		
		//修改定位数据后刷新图层生效
		mMapView.refresh();
		
	}
	
	
    public Graphic getRouteLine(){
    	
    	List<Locations.LocDesc> locations = DBManager.Instance.queryLocByBelongId(0);
    	
    	int numberLoc = locations.size();
    	if (numberLoc > 10)
    		numberLoc = 10;
    	 
    	//构建线
  		Geometry lineGeometry = new Geometry();
  		//设定折线点坐标
  		GeoPoint[] linePoints = new GeoPoint[numberLoc];
  		for (int i = 0; i < numberLoc; i++) 
    	{
    	  	double mLat = locations.get(i).latitude;
           	double mLon = locations.get(i).longitude;
        	int lat = (int) (mLat*1E6);
    	   	int lon = (int) (mLon*1E6);   	
    	   	linePoints[i] = new GeoPoint(lat, lon);
		}
  		lineGeometry.setPolyLine(linePoints);
  		
  		//设定样式
  		Symbol lineSymbol = new Symbol();
  		Symbol.Color lineColor = lineSymbol.new Color();
  		lineColor.red = 255;
  		lineColor.green = 0;
  		lineColor.blue = 0;
  		lineColor.alpha = 255;
  		lineSymbol.setLineSymbol(lineColor, 10);
  		//生成Graphic对象
  		Graphic lineGraphic = new Graphic(lineGeometry, lineSymbol);
  		return lineGraphic;
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
	}
	
	private void destroyRes()
	{
		LocationBroadcast.unRegisterReceiver(this, locationReceiver);
		destroyBDMap();
	}
		
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
			case android.R.id.home:
			{
				onBackPressed();
				break;
			}
			default:
				break;
		}
		return true;
	}
	
	/* MSG_INDEX */
	private static final int MSG_SPEEDGRAPHIC = 1;
	private static final int MSG_LOCDESUPDATE = 2;
	
	private static class RideStatActHandler extends Handler 
	{
		private WeakReference<RideStatActivity> activity = null;

		public RideStatActHandler(RideStatActivity act) {
			super();
			this.activity = new WeakReference<RideStatActivity>(act);
		}

		@Override
		public void handleMessage(Message msg) 
		{
			RideStatActivity act = activity.get();
			if (null == act) 
			{
				return;
			}
			switch (msg.what) 
			{
				case MSG_SPEEDGRAPHIC:
				{
					act.initSpeedGraphic();	
					break;
				}
				case MSG_LOCDESUPDATE:
				{
					Locations.LocDesc locDesc = (Locations.LocDesc)msg.obj;
					act.image_locstatus.setImageResource(locDesc.typeEnum.getImageId());
					break;
				}
			}
		}
	}
	
	@Override
	public void onBackPressed() 
	{
		Intent intent = new Intent(this, SpeedActivity.class);
		destroyRes();
		startActivity(intent);
		finish();
	}
	@Override
	public void onClick(View v) 
	{
		switch (v.getId())
		{
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
			case R.id.btn_resetstat:
			{
				break;
			}
			case R.id.btn_sharestat:
			{
	          // 设置分享内容
	            mController.setShareContent("");
	            mController.setShareMedia(new UMImage(RideStatActivity.this, mChartView.toBitmap()));

	            mMapView.getCurrentMap();
			}
			default:
				break;
		}
	}
}
