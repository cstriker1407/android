package yeah.cstriker1407.android.gps;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class BaiduSDKActivity extends Activity
{
	private TextView textView;
	
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new BDLocationListener()
	{
		@Override
		public void onReceiveLocation(BDLocation location)
		{
			if (location == null)
				return;
			final StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation)
			{
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
			}
			else if (location.getLocType() == BDLocation.TypeNetWorkLocation)
			{
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
			}
			Log.d("", sb.toString());
			
			new Handler(getMainLooper()).post(new Runnable()
			{
				@Override
				public void run()
				{
					textView.setText(sb.toString());
				}
			});
		}

		@Override
		public void onReceivePoi(BDLocation poiLocation)
		{
			if (poiLocation == null)
			{
				return;
			}
			final StringBuffer sb = new StringBuffer(256);
			sb.append("Poi time : ");
			sb.append(poiLocation.getTime());
			sb.append("\nerror code : ");
			sb.append(poiLocation.getLocType());
			sb.append("\nlatitude : ");
			sb.append(poiLocation.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(poiLocation.getLongitude());
			sb.append("\nradius : ");
			sb.append(poiLocation.getRadius());
			if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation)
			{
				sb.append("\naddr : ");
				sb.append(poiLocation.getAddrStr());
			}
			if (poiLocation.hasPoi())
			{
				sb.append("\nPoi:");
				sb.append(poiLocation.getPoi());
			}
			else
			{
				sb.append("noPoi information");
			}
			Log.d("poi", sb.toString());
			
			new Handler(getMainLooper()).post(new Runnable()
			{
				@Override
				public void run()
				{
					textView.setText(sb.toString());
				}
			});
			
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_baidusdk);
		textView = (TextView)findViewById(R.id.tv_baidusdk);
		
		
		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
		mLocationClient.setAK("B31e70464ae93da1bce81db552c9aa6b");
		
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
		option.disableCache(true);// 禁止启用缓存定位

		option.setPoiNumber(10); // 最多返回POI个数
		option.setPoiDistance(2000); // poi查询距离
		option.setPoiExtraInfo(true); // 是否需要POI的电话和地址等详细信息

		option.setPriority(LocationClientOption.NetWorkFirst);
		mLocationClient.setLocOption(option);
		mLocationClient.start();
		if (mLocationClient != null && mLocationClient.isStarted())
		{
			mLocationClient.requestLocation();
			mLocationClient.requestPoi();
		}
	}

	@Override
	protected void onDestroy()
	{
		if (mLocationClient != null && mLocationClient.isStarted())
		{
			mLocationClient.stop();
		}
		super.onDestroy();
	}
}
