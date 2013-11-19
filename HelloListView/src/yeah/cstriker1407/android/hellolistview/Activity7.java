package yeah.cstriker1407.android.hellolistview;

import java.util.ArrayList;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;


//XListView-Android-master
//https://github.com/Maxwin-z/XListView-Android

/*
 * layout 
 * string
 * drawable
 */

public class Activity7 extends Activity
{
	private XListView mListView;
	private ArrayAdapter<String> mAdapter;
	
	private ArrayList<String> items = new ArrayList<String>();
	private int start = 0;
	private void geneItems() {
		for (int i = 0; i != 20; ++i) {
			items.add("refresh cnt " + (++start));
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity7);
		
		mListView = (XListView) findViewById(R.id.xListView1);
		mListView.setPullLoadEnable(true);
		mAdapter = new ArrayAdapter<String>(this, R.layout.list_item, items);
		mListView.setAdapter(mAdapter);
//		mListView.setPullLoadEnable(false);
//		mListView.setPullRefreshEnable(false);
		mListView.setXListViewListener(new IXListViewListener()
		{
			@Override
			public void onRefresh()
			{
				geneItems();
				mAdapter.notifyDataSetChanged();
				mListView.stopRefresh();
				mListView.stopLoadMore();
				mListView.setRefreshTime("刚刚");
			}
			
			@Override
			public void onLoadMore()
			{
				geneItems();
				mAdapter.notifyDataSetChanged();
				mListView.stopRefresh();
				mListView.stopLoadMore();
				mListView.setRefreshTime("刚刚");
			}
		});
	}
}
