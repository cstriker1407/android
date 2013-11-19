package yeah.cstriker1407.android.hellolistview;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Activity4 extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity4);
		
	    ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();  
	    for(int i=0;i<20;i++)  
	    {  
	        HashMap<String, String> map = new HashMap<String, String>();  
	        map.put("ItemTitle", "This is Title....." + i);  
	        map.put("ItemText", "This is text....." + i);  
	        map.put("visible","1");  
	        mylist.add(map);
	    }
	    ListView listView2 = (ListView)findViewById(R.id.listView1);
	    listView2.setAdapter(new Activity4Adapter(this, mylist));
	    
	    listView2.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
	    	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	    	{
	    		if( "1".equals(((Activity4Adapter)parent.getAdapter()).mylist.get(position).get("visible")) )
	    		{
	    			((Activity4Adapter)parent.getAdapter()).mylist.get(position).put("visible", "0");
	    		}else
	    		{
	    			((Activity4Adapter)parent.getAdapter()).mylist.get(position).put("visible", "1");
	    		}
	    		
	    		((Activity4Adapter)parent.getAdapter()).notifyDataSetChanged();
	    	}
		});
	    
	}
}
class ViewHolder
{
	public TextView title;
	public TextView info;
}
class Activity4Adapter extends BaseAdapter
{
	private LayoutInflater mInflater;
	public ArrayList<HashMap<String, String>> mylist;
	
	public Activity4Adapter(Context context, ArrayList<HashMap<String, String>> mylist)
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
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
		
		if("1".equals((String)mylist.get(position).get("visible")))
		{
			holder.info.setVisibility(View.VISIBLE);
		}else 
		{
			holder.info.setVisibility(View.GONE);
		}
		return convertView;
	}
}
