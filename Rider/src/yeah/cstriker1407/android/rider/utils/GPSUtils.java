package yeah.cstriker1407.android.rider.utils;

import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;

public class GPSUtils
{
	public static boolean isGPSOpen(Context context)
	{
		LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
	
	public static boolean isAGPSOpen(Context context)
	{
		LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		return lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}

	public static void openLocatoinSettings(Context context)
	{
		Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		context.startActivity(intent);
	}
	
	/* 返回米 */
	public static float calcDistance(double startLatitude, double startLongitude, double endLatitude, double endLongitude)
	{
		float [] result = new float[]{-1f,0f,0f};
		Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude, result);
		return result[0];
	}
	
	/* 返回米 */
	public static float calcDistanceByBD(double startLatitude, double startLongitude, double endLatitude, double endLongitude)
	{
		return (float)DistanceUtil.getDistance(new GeoPoint((int)(startLatitude * 1e6), (int)(startLongitude * 1e6)), 
										new GeoPoint((int)(endLatitude * 1e6), (int) (endLongitude * 1e6)));
	}
}
