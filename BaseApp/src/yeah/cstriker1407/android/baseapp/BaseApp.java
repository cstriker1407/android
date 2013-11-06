package yeah.cstriker1407.android.baseapp;

import android.app.Application;

public class BaseApp extends Application
{
		@Override
		public void onCreate()
		{
			super.onCreate();
			
			SDLog.initLog();
			SDLog.d("", "hello world");
			
			Thread.setDefaultUncaughtExceptionHandler(new ThreadCrashHandler("BaseApp"));
		}
}
