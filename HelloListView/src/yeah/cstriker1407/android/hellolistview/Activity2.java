package yeah.cstriker1407.android.hellolistview;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


//http://www.cnblogs.com/allin/archive/2010/05/11/1732200.html
//http://blog.csdn.net/hellogv/article/details/4542668
public class Activity2 extends Activity
{
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity2);
		
		ListView listView1 = (ListView)findViewById(R.id.listView1);

		Cursor cursor = getContentResolver().query(People.CONTENT_URI, null, null, null, null);
		startManagingCursor(cursor);
		ListAdapter listAdapter = new SimpleCursorAdapter(this, R.layout.my_listitem, 
                cursor,
                new String[]{People.NAME}, 
                new int[]{R.id.ItemTitle});
        listView1.setAdapter(listAdapter);
        
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        	{
        		Toast.makeText(Activity2.this, "position:" + position, Toast.LENGTH_SHORT).show();
        	}
        });
        
	    ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();  
	    for(int i=0;i<20;i++)  
	    {  
	        HashMap<String, String> map = new HashMap<String, String>();  
	        map.put("ItemTitle", "This is Title....." + i);  
	        map.put("ItemText", "This is text.....");  
	        mylist.add(map);
	    }
	    ListView listView2 = (ListView)findViewById(R.id.listView2);
	    listView2.setAdapter(new Activity2Adapter(this, mylist));
	}
}




class Activity2Adapter extends BaseAdapter
{
	class ViewHolder
	{
		public TextView title;
		public TextView info;
	}

	private LayoutInflater mInflater;
	private ArrayList<HashMap<String, String>> mylist;
	
	public Activity2Adapter(Context context, ArrayList<HashMap<String, String>> mylist)
	{
		this.mInflater = LayoutInflater.from(context);
		this.mylist = mylist;
	}
	@Override
	public int getCount() 
	{
		return mylist.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder = null;
		if (convertView == null) 
		{
			holder=new ViewHolder();  
			convertView = mInflater.inflate(R.layout.my_listitem, null);
			holder.title = (TextView)convertView.findViewById(R.id.ItemTitle);
			holder.info = (TextView)convertView.findViewById(R.id.ItemText);
			convertView.setTag(holder);
		}else 
		{
			holder = (ViewHolder)convertView.getTag();
		}

		holder.title.setText((String)mylist.get(position).get("ItemTitle"));
		holder.info.setText((String)mylist.get(position).get("ItemText"));
		return convertView;
	}
}
