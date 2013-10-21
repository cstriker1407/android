package yeah.cstriker1407.android.rider.storage;

import java.util.Date;

import yeah.cstriker1407.android.rider.R;
import yeah.cstriker1407.android.rider.storage.Locations.LocDesc.LocTypeEnum;
import yeah.cstriker1407.android.rider.utils.GPSUtils;
import yeah.cstriker1407.android.rider.utils.TimeUtils;
import android.util.Log;

public class Locations 
{
	private static final String TAG = "Locations";
	//单例===
	private static Locations instance = new Locations();
	public static Locations getInstance()
	{
		return instance;
	}
	private Locations()
	{
		//do nth
	}
	//单例===
	
	private InternLocation lastLoc = new InternLocation();
	
	private int totalDistanceM = 0;//总里程(米)
	private Date startDate = null;//起始的时间
	
	/* 上次的位置是否有效 */
	public void addToTotalDistanceM(int newM)
	{
		if (newM < 0)
		{
			Log.e(TAG, "addToTotalDistanceM Error: " + newM);
			return;
		}
		totalDistanceM += newM;
	}
	
	public LocDesc getLocDes(String addrStr, LocTypeEnum locTypeEnum, float radius)
	{
		return new LocDesc(addrStr, locTypeEnum, radius);
	}
	
	public SpeedInfo calcSpeedInfo(double latitude, double longitude)
	{
		Date currDate = new Date();
		if (null == startDate)
		{//第一次更新位置的时候保存时间
			startDate = currDate;
		}
		if (lastLoc.isInValid())
		{//上次是无效地址，将本次地址复制进去。应该只进去一次
			lastLoc.update(latitude, longitude, currDate);
		}
		
		//计算单次速度
		float singleDistanceM = GPSUtils.calcDistance(lastLoc.latitude,lastLoc.longitude,latitude,longitude);
		int singleMS = TimeUtils.msBetweenDates(lastLoc.locDate, currDate);
		int singleSpeedM = 0;
		if (singleMS > 0)
		{
			singleSpeedM = (int) (singleDistanceM * 1000 / singleMS);
		}
		Log.d(TAG, "distace:" + singleDistanceM + "  ms:" + singleMS + "  speedinM:"+ singleSpeedM);
		
		//计算平均速度
		totalDistanceM += singleDistanceM;
		int totalMS = TimeUtils.msBetweenDates(startDate, currDate);
		int averageSpeedM = 0;
		if (totalMS > 0)
		{
			averageSpeedM = (int) (totalDistanceM * 1000 / totalMS);
		}		
		//更新
		lastLoc.update(latitude, longitude, currDate);
		
		return new SpeedInfo(singleSpeedM, 0, 0, averageSpeedM, totalDistanceM, totalMS/1000);
	}
	
	
	public static class LocDesc
	{
		public static enum LocTypeEnum
		{
			GPS(R.drawable.loc_gps),
			NET(R.drawable.loc_net),
			FAIL(R.drawable.loc_fail);
			private LocTypeEnum(int imageId) {
				this.imageId = imageId;
			}
			private int imageId;
			public int getImageId() {
				return imageId;
			}
		};
		public String locAddr = null;
		public float radius = 0;
		public LocTypeEnum typeEnum = LocTypeEnum.FAIL;
		
		private LocDesc(String locAddr,LocTypeEnum typeEnum, float radius) {
			super();
			this.locAddr = locAddr;
			this.typeEnum = typeEnum;
			this.radius = radius;
		}

		@Override
		public String toString() {
			return "LocDesc [locAddr=" + locAddr + ", radius=" + radius
					+ ", typeEnum=" + typeEnum + "]";
		}
	}
	
	public static class SpeedInfo
	{
		public int singleSpeedM = 0;//当前速度 m/s
		public int maxSpeedM = 0;//最大速度 m/s
		public int minSpeedM = 0;//最小速度 m/s
		public int averageSpeedM = 0;//平均速度 m/s
		public int totalDistanceM = 0;//总里程 m
		public int totalSeconds = 0;//总时间 s
		
		private SpeedInfo(int singleSpeedM, int maxSpeedM, int minSpeedM,
				int averageSpeedM, int totalDistanceM, int totalSeconds) {
			super();
			this.singleSpeedM = singleSpeedM;
			this.maxSpeedM = maxSpeedM;
			this.minSpeedM = minSpeedM;
			this.averageSpeedM = averageSpeedM;
			this.totalDistanceM = totalDistanceM;
			this.totalSeconds = totalSeconds;
		}

		@Override
		public String toString() {
			return "SpeedInfo [singleSpeedM=" + singleSpeedM + ", maxSpeedM="
					+ maxSpeedM + ", minSpeedM=" + minSpeedM
					+ ", averageSpeedM=" + averageSpeedM + ", totalDistanceM="
					+ totalDistanceM + ", totalSeconds=" + totalSeconds + "]";
		}
	}
	
	private static class InternLocation
	{
		public boolean isInValid()
		{
			return 0 == latitude && 0 == longitude;
		}
		public double latitude = 0.0;
		public double longitude = 0.0;
		public Date   locDate = null;
		
		public InternLocation()
		{
		}
		public void update(double latitude, double longitude, Date locDate) 
		{
			this.latitude = latitude;
			this.longitude = longitude;
			this.locDate = locDate;
		}
	}
}
