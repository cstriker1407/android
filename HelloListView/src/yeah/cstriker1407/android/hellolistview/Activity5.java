package yeah.cstriker1407.android.hellolistview;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Activity5 extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity5);
		
		String[] GENRES = new String[] {
	        "Action", "Adventure", "Animation", "Children", "Comedy", "Documentary", "Drama",
	        "Foreign", "History", "Independent", "Romance", "Sci-Fi", "Television", "Thriller"
	    };
		ListView listView1 = (ListView)findViewById(R.id.listView1);
		listView1.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_single_choice, GENRES));
		listView1.setItemsCanFocus(false);
		listView1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		ListView listView2 = (ListView)findViewById(R.id.listView2);
		listView2.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice, GENRES));
		listView2.setItemsCanFocus(false);
		listView2.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		
	}
}
