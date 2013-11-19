package yeah.cstriker1407.android.hellolistview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

public class Activity6 extends Activity
{
    private static final String NAME = "NAME";
    private static final String IS_EVEN = "IS_EVEN";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity6);

		ExpandableListView expandableListView1 = (ExpandableListView) findViewById(R.id.expandableListView1);
		
		/* 父级的数据list，每个元素是一个hashmap */
        List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
        
        /* 子类数据list，每个元素也是一个list，这个list是这个子链表的元素集合。 */
        List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
        for (int i = 0; i < 20; i++) {
            Map<String, String> curGroupMap = new HashMap<String, String>();
            groupData.add(curGroupMap);
            curGroupMap.put(NAME, "Group " + i);
            curGroupMap.put(IS_EVEN, (i % 2 == 0) ? "This group is even" : "This group is odd");
            
            List<Map<String, String>> children = new ArrayList<Map<String, String>>();
            for (int j = 0; j < 15; j++) {
                Map<String, String> curChildMap = new HashMap<String, String>();
                children.add(curChildMap);
                curChildMap.put(NAME, "Child " + j);
                curChildMap.put(IS_EVEN, (j % 2 == 0) ? "This child is even" : "This child is odd");
            }
            childData.add(children);
        }
		
        expandableListView1.setAdapter(
        		new SimpleExpandableListAdapter(
                this,
                groupData,
                android.R.layout.simple_expandable_list_item_1,
                new String[] { NAME, IS_EVEN },
                new int[] { android.R.id.text1, android.R.id.text2 },
                childData,
                android.R.layout.simple_expandable_list_item_2,
                new String[] { NAME, IS_EVEN },
                new int[] { android.R.id.text1, android.R.id.text2 }
                ));
        
		ExpandableListView expandableListView2 = (ExpandableListView) findViewById(R.id.expandableListView2);
		expandableListView2.setAdapter(new MyExpandableListAdapter());
		expandableListView2.expandGroup(0);//设置第一组张开
		expandableListView2.setGroupIndicator(null);//除去自带的箭头
		
		
	}

	class MyExpandableListAdapter extends BaseExpandableListAdapter {
	    // Sample data set.  children[i] contains the children (String[]) for groups[i].
	    private String[] groups = { "People Names", "Dog Names", "Cat Names", "Fish Names" };
	    private String[][] children = {
	            { "Arnold", "Barry", "Chuck", "David" },
	            { "Ace", "Bandit", "Cha-Cha", "Deuce" },
	            { "Fluffy", "Snuggles" },
	            { "Goldy", "Bubbles" }
	    };
	    
	    public Object getChild(int groupPosition, int childPosition) {
	        return children[groupPosition][childPosition];
	    }

	    public long getChildId(int groupPosition, int childPosition) {
	        return childPosition;
	    }

	    public int getChildrenCount(int groupPosition) {
	        return children[groupPosition].length;
	    }

	    public TextView getGenericView() {
	        // Layout parameters for the ExpandableListView
	        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
	                ViewGroup.LayoutParams.MATCH_PARENT, 64);

	        TextView textView = new TextView(Activity6.this);
	        textView.setLayoutParams(lp);
	        // Center the text vertically
	        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
	        // Set the text starting position
	        textView.setPadding(36, 0, 0, 0);
	        return textView;
	    }
	    
	    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
	            View convertView, ViewGroup parent) {
	        TextView textView = getGenericView();
	        textView.setText(getChild(groupPosition, childPosition).toString());
	        return textView;
	    }

	    public Object getGroup(int groupPosition) {
	        return groups[groupPosition];
	    }

	    public int getGroupCount() {
	        return groups.length;
	    }

	    public long getGroupId(int groupPosition) {
	        return groupPosition;
	    }

	    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
	            ViewGroup parent) {
	        TextView textView = getGenericView();
	        textView.setText(getGroup(groupPosition).toString());
	        return textView;
	    }

	    public boolean isChildSelectable(int groupPosition, int childPosition) {
	        return true;
	    }

	    public boolean hasStableIds() {
	        return true;
	    }

	}

}



