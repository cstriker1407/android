package yeah.cstriker1407.android.rider.utils;

import yeah.cstriker1407.android.rider.storage.Locations;
import yeah.cstriker1407.android.rider.storage.Locations.LocDesc.LocTypeEnum;

import com.baidu.location.BDLocation;

public class BDUtils 
{
	public static final String KEY = "F0488f2ee7d14e2bba215419efb9bff3";
	public static final int  SCANSPAN = 5000;
	
	public static Locations.LocDesc.LocTypeEnum BD2LocTypeEnum(BDLocation location) 
	{
		Locations.LocDesc.LocTypeEnum locTypeEnum;
		switch (location.getLocType()) 
		{
			case BDLocation.TypeGpsLocation:
			{
				locTypeEnum = LocTypeEnum.GPS;
				break;
			}
			case BDLocation.TypeNetWorkLocation:
			{
				locTypeEnum = LocTypeEnum.NET;
				break;
			}
			default:
			{
				locTypeEnum = LocTypeEnum.FAIL;
				break;
			}
		}
		return locTypeEnum;
	}
}
