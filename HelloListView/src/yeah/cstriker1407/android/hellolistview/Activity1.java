package yeah.cstriker1407.android.hellolistview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Activity1 extends Activity
{
	private List<String> dataList;
	private ArrayList<HashMap<String, String>> mylist;
	
	private ListView listView1;
	private ListView listView2;
	private ArrayAdapter<String> adapter2;
	private SimpleAdapter mSchedule;
	private int idx = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity1);
		
	    //绑定XML中的ListView，作为Item的容器  
	    listView1 = (ListView) findViewById(R.id.listView1);  
	      
	    //生成动态数组，并且转载数据  
	    mylist = new ArrayList<HashMap<String, String>>();  
	    for(int i=0;i<20;i++)  
	    {  
	        HashMap<String, String> map = new HashMap<String, String>();  
	        map.put("ItemTitle", "This is Title....." + i);  
	        map.put("ItemText", "This is text.....");  
	        mylist.add(map);
	    }  
	    //生成适配器，数组===》ListItem  
	    mSchedule = new SimpleAdapter(this, //没什么解释  
	                                                mylist,//数据来源   
	                                                R.layout.my_listitem,//ListItem的XML实现  
	                                                //动态数组与ListItem对应的子项          
	                                                new String[] {"ItemTitle", "ItemText"},   
	                                                //ListItem的XML文件里面的两个TextView ID  
	                                                new int[] {R.id.ItemTitle,R.id.ItemText});  
	    //添加并且显示  
	    listView1.setAdapter(mSchedule);  

	    dataList = new ArrayList<String>();
	    for (int i = 0; i < 20; i++)
		{
	    	dataList.add("test " + idx++ );
		}
	    
	    listView2 = (ListView)findViewById(R.id.listView2);
        Button headButton = new Button(this);
        listView2.addHeaderView(headButton);
        adapter2 = new ArrayAdapter<String>(this, R.layout.my_listitem, R.id.ItemTitle, dataList);
        listView2.setAdapter(adapter2);
	    
        ((Button)findViewById(R.id.button1)).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
			    for(int i=20;i<25;i++)  
			    {  
			        HashMap<String, String> map = new HashMap<String, String>();  
			        map.put("ItemTitle", "This is Title....." + i);  
			        map.put("ItemText", "This is text.....");  
			        mylist.add(map);
			    } 
			    mSchedule.notifyDataSetChanged();
				
			    for (int i = 0; i < 5; i++)
				{
			    	dataList.add("test " + idx++);
				}
			    adapter2.notifyDataSetChanged();
			}
		});
	}
}
