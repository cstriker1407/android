package yeah.cstriker1407.android.hellolistview;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.AvoidXfermode.Mode;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class Activity8 extends Activity
{
	private ArrayList<String> mListItems;
	private ArrayAdapter<String> mAdapter;
	private PullToRefreshListView pullToRefreshView;
	private int idx = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity8);
		
		mListItems = new ArrayList<String>();
		for (int i = 0; i < 10; i++)
		{
			mListItems.add("string" + idx++);
		}
		
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mListItems);
		pullToRefreshView = (PullToRefreshListView) findViewById(R.id.pull_to_refresh_listview);
		pullToRefreshView.setOnRefreshListener(new OnRefreshListener2<ListView>()
		{
			public void onPullDownToRefresh(final PullToRefreshBase<ListView> refreshView)
			{
				for (int i = 0; i < 5; i++)
				{
					mListItems.add("Down string" + idx++);
				}
				Toast.makeText(Activity8.this, "Pull Down!", Toast.LENGTH_SHORT).show();
				new GetDataTask().execute();
			}
			public void onPullUpToRefresh(final PullToRefreshBase<ListView> refreshView)
			{
				for (int i = 0; i < 5; i++)
				{
					mListItems.add("Up string" + idx++);
				}
				Toast.makeText(Activity8.this, "Pull Up!", Toast.LENGTH_SHORT).show();
				new GetDataTask().execute();
			}
		});
		pullToRefreshView.setMode(PullToRefreshBase.Mode.BOTH);
		pullToRefreshView.getRefreshableView().setAdapter(mAdapter);
		
	}
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			return null;
		}
		@Override
		protected void onPostExecute(String[] result) {
			// Call onRefreshComplete when the list has been refreshed.
			pullToRefreshView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}
}
