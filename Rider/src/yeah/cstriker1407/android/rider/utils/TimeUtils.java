package yeah.cstriker1407.android.rider.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.text.format.DateFormat;

public class TimeUtils
{
	/*
	 * 将毫秒数转换为特定格式的字符串。
	 * 注意：代码中将市区设置为GMT时区，这样就可以得到正确的时间，而不是加了8小时之后的时间。
	 */
	public static String fmtMs2Str(long millisecond, String pattern)
	{
		if (null == pattern)
		{
			return null;
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat(pattern,Locale.CHINA);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		return sdf.format(new Date(millisecond));
	}

	/*
	 * 将时间转换为特定格式的字符串。
	 * 注意：android上有特定的工具类，可以简化操作。
	 */
	public static String fmtDate2Str(Date date, String pattern)
	{
		if (null == date || null == pattern)
		{
			return null;
		}
		return DateFormat.format(pattern, date).toString();
		
//		SimpleDateFormat sdf = new SimpleDateFormat(pattern,Locale.CHINA);
//		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
//		return sdf.format(date);
	}
	
	/*
	 * 将特定格式的字符串转换为时间。
	 */
	public static Date fmtStr2Date(String pattern, String dateStr)
	{
		if (null == pattern || dateStr == null)
		{
			return null;
		}
		Date result = null;
		try {
			result = new SimpleDateFormat(pattern, Locale.CHINA).parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}
	
	/* 返回毫秒 */
	public static int msBetweenDates(Date before, Date after)
	{
		if (null == before || null == after)
		{
			return 0;
		}
		return (int)(after.getTime() - before.getTime());
	}
}
