package yeah.cstriker1407.android.hellolistview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


//http://iaiai.iteye.com/blog/1188668
public class Activity3 extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity3);
		
	    List<Map<String, String>> mylist = new ArrayList<Map<String, String>>();  
	    List<Map<String, String>> splitList = new ArrayList<Map<String, String>>(); 
        // 组织数据源  
        Map<String, String> mp = new HashMap<String, String>();  
        mp.put("itemTitle", "A");  
        mylist.add(mp);  
        splitList.add(mp);  
  
        for (int i = 0; i < 3; i++) {  
            Map<String, String> map = new HashMap<String, String>();  
            map.put("itemTitle", "文章1-" + i);  
            mylist.add(map);  
        }  
  
        mp = new HashMap<String, String>();  
        mp.put("itemTitle", "B");  
        mylist.add(mp);  
        splitList.add(mp);
  
        for (int i = 0; i < 6; i++) {  
            Map<String, String> map = new HashMap<String, String>();  
            map.put("itemTitle", "文章2-" + i);  
            mylist.add(map);  
        }  
        ListView listView1 = (ListView) findViewById(R.id.listView1);  
        listView1.setAdapter(new Activity3Adapter(this, mylist, splitList));
	}
}
class Activity3Adapter extends BaseAdapter {  
	  
    private LayoutInflater mInflater;  
  
    private List<Map<String, String>> listData;  
  
    private List<Map<String, String>> splitData;  
  
    public Activity3Adapter(Context context,  
            List<Map<String, String>> listData,  
            List<Map<String, String>> splitData) {  
        this.mInflater = LayoutInflater.from(context);  
        this.listData = listData;  
        this.splitData = splitData;  
    }  
  
    @Override  
    public int getCount() {  
        return listData.size();  
    }  
  
    @Override  
    public Object getItem(int position) {  
        return listData.get(position);  
    }  
  
    @Override  
    public long getItemId(int position) {  
        return position;  
    }  
  
    @Override  
    public boolean isEnabled(int position) {  
        if (splitData.contains(listData.get(position))) {  
            return false;  
        }  
        return super.isEnabled(position);  
    }  
  
    @Override  
    public View getView(final int position, View convertView, ViewGroup parent) {  
        if (splitData.contains(listData.get(position))) {  
            convertView = mInflater.inflate(R.layout.my_listitem_tag, null);  
        } else {  
            convertView = mInflater.inflate(R.layout.my_listitem, null);  
        }  
  
        TextView textView = (TextView) convertView.findViewById(R.id.ItemTitle);  
        textView.setText(listData.get(position).get("itemTitle"));  
  
        return convertView;  
    }  
  
}  