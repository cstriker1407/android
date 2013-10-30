package yeah.cstriker1407.android.baseapp;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import android.util.Log;

import de.mindpipe.android.logging.log4j.LogConfigurator;


//https://code.google.com/p/android-logging-log4j/
//http://logging.apache.org/log4j/1.2/download.html

//http://code.google.com/p/microlog4android/downloads/list


public class SDLog
{
	private static final Logger log = Logger.getLogger(SDLog.class);

	private static boolean bLogToFile = false;
	
	public static void initLog()
	{
        final LogConfigurator logConfigurator = new LogConfigurator();
        
        logConfigurator.setFileName("/mnt/sdcard/sss.log");

        /*
         * http://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j/PatternLayout.html
         */
        logConfigurator.setLogCatPattern("%m%n");
        logConfigurator.setFilePattern("%d - [%p] - %m%n");
        logConfigurator.setRootLevel(Level.DEBUG);
        logConfigurator.setLevel("org.apache", Level.DEBUG);
    
        try
		{
			logConfigurator.configure();
		}
		catch (Exception e)
		{
			bLogToFile = false;
			e.printStackTrace();
		}
        bLogToFile = true;

	
	}
	
	private static String logFmt(String tag,String msg)
	{
		return String.format("%s==>%s", tag, msg);
	}
	
    public static int d(String tag, String msg) 
    {
    	if (bLogToFile)
    	{
    		log.debug(logFmt(tag,msg));
    	}else
    	{
    		Log.d(tag, msg);
    	}
    	return 1; 
    }
    public static int e(String tag, String msg) 
    {
    	if (bLogToFile)
		{
    		log.error(logFmt(tag,msg));
		}
		else
		{
			Log.e(tag, msg);
		}
    	return 1;
    }
}