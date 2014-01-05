package yeah.cstriker1407.android.rider.storage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import yeah.cstriker1407.android.rider.storage.Locations.LocDesc;
import yeah.cstriker1407.android.rider.storage.Locations.SpeedInfo;
import yeah.cstriker1407.android.rider.utils.TimeUtils;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBManager
{
	private static final String TAG = "DBManager";
	public static final DBManager Instance = new DBManager();
	private DBManager()
	{
		initDB();
	}
	
	private int groupid = 0;
	
	
	private SQLiteDatabase sqLiteDatabase = null;
	private static final String location = "location";
	private static final String longitude = "longitude";
	private static final String latitude = "latitude";
	private static final String belong = "belong";
	private static final String time = "time";
	private static final String speed = "speed";
	private static final String des = "des";


	private boolean initDB()
	{
		if (!isDBInited())
		{
			sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase("/mnt/sdcard/rider.db", null);
			sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+ location +" (_id INTEGER PRIMARY KEY, " 
			+ longitude +" double, "
			+ latitude +" double, "
			+ belong +" int, "
			+ time +" timestamp, "
			+ speed +" int, "
			+ des +" TEXT)");
			return true;
		}
		return false;
	}
	private boolean isDBInited()
	{
		if (sqLiteDatabase != null)
		{
			return true;
		}
		return false;
	}
	public boolean closeDB()
	{
		if (isDBInited())
		{
			sqLiteDatabase.close();
			sqLiteDatabase = null;
			return true;
		}
		return false;
	}
	
	
	
	private boolean innerInsertDB(double lon,double lat,int belongid,Date currTime,int speedM,String desc)
	{
		if (!isDBInited())
		{
			return false;
		}
		
		ContentValues cv = new ContentValues();
		cv.put(longitude, lon);
		cv.put(latitude, lat);
		cv.put(belong, belongid);
		cv.put(time, TimeUtils.fmtDate2Str(currTime, "yyyy-MM-dd kk:mm:ss"));
		cv.put(speed, speedM);
		cv.put(des, desc);
		sqLiteDatabase.insert(location, null, cv);
		return true;
	}
	public boolean updateDB()
	{
		return true;
	}
	public boolean deleteDB()
	{
		return true;
	}
	
	public List<Locations.SpeedInfo> querySpeedByBelongId(int belongid)
	{
		List<Locations.SpeedInfo> list = new ArrayList<Locations.SpeedInfo>();
		if (!isDBInited())
		{
			return list;
		}
		
		Cursor cursor = sqLiteDatabase.rawQuery(querySQL(belongid), null);
		
		int timeIdx = cursor.getColumnIndex(time);
		int speedIdx = cursor.getColumnIndex(speed);
        
		while (cursor.moveToNext()) 
		{
			Locations.SpeedInfo item = new Locations.SpeedInfo();
			item.currDate = TimeUtils.fmtStr2Date("yyyy-MM-dd HH:mm:ss" ,cursor.getString(timeIdx));
			item.singleSpeedM = cursor.getInt(speedIdx);
			list.add(item);
        }  
		cursor.close();  		

		return list;
	}
	
	private static String querySQL(int belongid)
	{
		String sql =  "SELECT * FROM " + location + " WHERE " + belong + " = " + belongid + " ORDER BY " + time +" ASC;";
		Log.d(TAG, sql);
		return sql;
	}
	
	private static String queryLoc()
	{
		String sql =  "SELECT * FROM " + location + " ORDER BY " + time +" DESC limit 1;";
		Log.d(TAG, sql);
		return sql;
	}	
	
	
	public Locations.LocDesc queryLastLocation()
	{
		Locations.LocDesc item = new Locations.LocDesc();
		if (!isDBInited())
		{
			return item;
		}
		
		Cursor cursor = sqLiteDatabase.rawQuery(queryLoc(), null);
		
		int lonIdx = cursor.getColumnIndex(longitude);
		int latIdx = cursor.getColumnIndex(latitude);
		int timeIdx = cursor.getColumnIndex(time);
        
		while (cursor.moveToNext()) 
		{
			item.longitude = cursor.getDouble(lonIdx);
			item.latitude = cursor.getDouble(latIdx);
			item.currDate = TimeUtils.fmtStr2Date("yyyy-MM-dd HH:mm:ss" ,cursor.getString(timeIdx));
        }  
		cursor.close();  		
		return item;
	}
	
	public List<Locations.LocDesc> queryLocByBelongId(int belongid)
	{
		List<Locations.LocDesc> list = new ArrayList<Locations.LocDesc>();
		if (!isDBInited())
		{
			return list;
		}
		
		Cursor cursor = sqLiteDatabase.rawQuery(querySQL(belongid), null);
		
		int lonIdx = cursor.getColumnIndex(longitude);
		int latIdx = cursor.getColumnIndex(latitude);
		int timeIdx = cursor.getColumnIndex(time);
        
		while (cursor.moveToNext()) 
		{
			Locations.LocDesc item = new Locations.LocDesc();
			item.longitude = cursor.getDouble(lonIdx);
			item.latitude = cursor.getDouble(latIdx);
			item.currDate = TimeUtils.fmtStr2Date("yyyy-MM-dd HH:mm:ss" ,cursor.getString(timeIdx));

			list.add(item);
        }  
		cursor.close();  		

		return list;
	}
	public void insertDB(LocDesc locDesc, SpeedInfo speedInfo) 
	{
		innerInsertDB(locDesc.longitude, locDesc.latitude, groupid, speedInfo.currDate, speedInfo.singleSpeedM, null);
	}
}
