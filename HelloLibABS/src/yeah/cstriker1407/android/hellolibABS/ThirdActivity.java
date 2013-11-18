package yeah.cstriker1407.android.hellolibABS;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;


//http://stackoverflow.com/questions/11190169/nullpointerexception-when-adding-a-tablistener
public class ThirdActivity extends SherlockFragmentActivity implements TabListener
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_third);
		
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		ActionBar.Tab newTab = getSupportActionBar().newTab();
		newTab.setText("left");
	    newTab.setTabListener(this);
		getSupportActionBar().addTab(newTab);
		
		ActionBar.Tab newTab2 = getSupportActionBar().newTab();
		newTab2.setText("right");
	    newTab2.setTabListener(this);
		getSupportActionBar().addTab(newTab2);
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft)
	{
		if (tab.getText().equals("left"))
		{
			ft.replace(R.id.frame, new LeftFrag());
		}
		if (tab.getText().equals("right"))
		{
			ft.replace(R.id.frame, new RightFrag());
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft)
	{
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft)
	{
		
	}
}
