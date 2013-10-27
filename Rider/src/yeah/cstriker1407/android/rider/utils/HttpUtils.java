package yeah.cstriker1407.android.rider.utils;

import java.lang.ref.WeakReference;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpUtils 
{
	public static interface onHttpResultListener
	{
		public void onHttpResult(String result, int id);
	}
	
	public static void sendGetRequest(String url, onHttpResultListener listener, int id)
	{
		if (null == url || url.trim().length() == 0 || null == listener)
		{
			return;
		}
		new GetThread(url, listener, id).start();
	}
	
	private static class GetThread extends Thread
	{
		private WeakReference<onHttpResultListener> listener = null;
		private String url;
		private int id;
		
		private GetThread(String url, onHttpResultListener p,int id) 
		{
			this.id = id;
			this.url = url;
			this.listener = new WeakReference<HttpUtils.onHttpResultListener>(p);
		}
		@Override
		public void run() 
		{
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(url.trim());

			try {
				HttpResponse response = client.execute(request);
				if (response.getStatusLine().getStatusCode() == 200)
				{
					String result = EntityUtils.toString(response.getEntity());
					
					onHttpResultListener p = listener.get();
					if (p != null)
					{
						p.onHttpResult(result, id);
					}
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				onHttpResultListener p = listener.get();
				if (p != null)
				{
					p.onHttpResult(null, id);
				}
			}
		}
	}
}
