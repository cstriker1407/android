package yeah.cstriker1407.android.gps;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements LocationListener
{
	private Button btn_GetPosition;
	private Button btn_ChkGPS;
	
	LocationManager lm;
	String provider;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
//		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, this);
		Criteria criteria = new Criteria();   
		criteria.setPowerRequirement(Criteria.POWER_HIGH);
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setSpeedAccuracy(Criteria.ACCURACY_HIGH);
		provider = lm.getBestProvider(criteria, false);
		lm.requestLocationUpdates(provider, 1000, 10, this);
		
		
		
		btn_GetPosition = (Button)findViewById(R.id.GetPosition);
		btn_ChkGPS = (Button)findViewById(R.id.ChkGPS);

		btn_ChkGPS.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Log.d("", "GPS:" + GPSUtils.isGPSOpen(MainActivity.this) );
				Log.d("", "AGPS:" + GPSUtils.isAGPSOpen(MainActivity.this) );
				GPSUtils.openLocatoinSettings(MainActivity.this);
			}
		});
		
		btn_GetPosition.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				
				Log.d("", "best:" + provider);
				
				Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				if (null != loc)
				{
					Log.d("", "GPS:" + loc.toString() );
				}
				
				loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				if (null != loc)
				{
					Log.d("", "netWork:" + loc.toString() );
				}
			}
		});
	}
	@Override
	public void onLocationChanged(Location location)
	{
		Log.d("", "onLocationChanged:" + location.toString() );
	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras)
	{
	}
	@Override
	public void onProviderEnabled(String provider)
	{
	}
	@Override
	public void onProviderDisabled(String provider)
	{
		
	}
}
