package yeah.cstriker1407.android.baseapp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import android.util.Log;

//http://blog.csdn.net/liuhe688/article/details/6584143
//http://blog.csdn.net/zkw12358/article/details/11097649


public class ThreadCrashHandler implements Thread.UncaughtExceptionHandler
{
	private static final String tag = "ThreadCrashHandler";
	private static final int MAX_STREAM_LENGTH = 1024;
	
	private String threadName;

	public ThreadCrashHandler(String threadName)
	{
		super();
		this.threadName = threadName;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex)
	{
		Log.e(tag, String.format("Thread:%s is crashed", threadName));
		if (null == ex)
		{
			return;
		}
		Log.e(tag, ex.toString());
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream(MAX_STREAM_LENGTH);
		PrintStream ps = new PrintStream(stream);
		ex.printStackTrace(ps);
		ps.flush();
		ps.close();
		Log.e(tag, stream.toString());
		try
		{
			stream.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public String toString()
	{
		return threadName + super.toString();
	}

}
