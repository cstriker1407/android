package yeah.cstriker1407.android.rider;

import yeah.cstriker1407.android.rider.utils.BDUtils;

import com.baidu.mapapi.BMapManager;

import android.app.Application;
import android.util.Log;

public class RiderApplication extends Application
{
	public static RiderApplication APP = null;
	
	
	
	
	public BMapManager mBMapMan = null; 
	@Override
	public void onCreate() 
	{
		Log.e("", "!! THE RIDER APPLICATION IS CREATED !!");
		super.onCreate();
		APP = this;
		
		mBMapMan = new BMapManager(getApplicationContext());  
		mBMapMan.init(BDUtils.KEY, null);    
		
	}
}
