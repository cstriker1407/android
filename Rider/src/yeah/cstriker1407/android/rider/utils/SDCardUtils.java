package yeah.cstriker1407.android.rider.utils;

import java.io.File;

import android.os.Environment;

public class SDCardUtils
{
	public static final String SD_Root_Path = Environment.getExternalStorageDirectory().getPath();
	private static final String APPDir = SD_Root_Path + "Rider" + File.separator;
	
	public static final String DBPath =  APPDir + "db" + File.separator;
	public static final String ImagePath = APPDir + "image" + File.separator;
	public static final String LogPath = APPDir + "log" + File.separator;
	
	public static boolean isFileExist(String filePath)
	{
		if (null == filePath || filePath.trim().length() == 0)
		{
			return false;
		}
		return new File(filePath).exists();
	}
	
	public static boolean initDirs()
	{
		new File(DBPath).mkdirs();
		new File(ImagePath).mkdirs();
		new File(LogPath).mkdirs();
		
		return true;
	}
	
}
