package yeah.cstriker1407.android.rider.utils;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import yeah.cstriker1407.android.rider.R;

import android.util.Log;

public class WeatherUtils
{
	public static final int WEATHER_SPAN = 5 * 1000;
	
	public static final String URL_Service = "http://www.weather.com.cn/data/sk/101190101.html";
//	public static final String URL_Activity = "http://www.weather.com.cn/data/cityinfo/101190101.html";
	
	public static class WeatherInfo implements Serializable
	{
		private static final long serialVersionUID = 9142206968689527358L;
		
		public String city = null;
		public int cityId = 0;
		public int curr_temp = 0;
		public String wind = null;
		public String sd = null;//Êª¶È
		public int weatherid = 0;

		@Override
		public String toString() {
			return "WeatherInfo [city=" + city + ", cityId=" + cityId
					+ ", curr_temp=" + curr_temp + ", wind=" + wind + ", sd="
					+ sd + ", weatherid=" + weatherid + "]";
		}
	}
	
	public static WeatherInfo FmtWeatherFromJson(String jsonStr)
	{
		if (null == jsonStr || jsonStr.trim().length() == 0)
			return null;
		
		WeatherInfo info = new WeatherInfo();
		
		try
		{
			JSONObject jsonObj = new JSONObject(jsonStr).getJSONObject("weatherinfo");
			info.city = jsonObj.getString("city");
			info.cityId = jsonObj.getInt("cityid");
			info.curr_temp = jsonObj.getInt("temp");
			info.sd = jsonObj.getString("SD");
			info.weatherid = jsonObj.getInt("WSE");
			info.wind = jsonObj.getString("WD") + jsonObj.getString("WS"); 
		} 
		catch (JSONException e)
		{
			info = null;
			Log.e("", jsonStr + "parse error");
			e.printStackTrace();
		}
		return info;
	}
	
	public static int GetWeatherIcon(int weathertype)
	{
		switch (weathertype) 
		{
		case 2: {
			return R.drawable.weather_02;
		}
		case 3: {
			return R.drawable.weather_03;
		}
		case 5: {
			return R.drawable.weather_05;
		}
		case 10: {
			return R.drawable.weather_10;
		}
		case 15: {
			return R.drawable.weather_15;
		}
		case 1:
		default: {
			return R.drawable.weather_01;
		}
		}
	}
	
}
