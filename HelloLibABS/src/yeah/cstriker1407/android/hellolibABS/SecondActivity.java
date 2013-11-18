package yeah.cstriker1407.android.hellolibABS;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class SecondActivity extends SlidingFragmentActivity
{
	private FragmentTabHost tabHost;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setSlidingActionBarEnabled(false);
		
		setContentView(R.layout.activity_second);
		setBehindContentView(R.layout.activity_leftmenu);

		SlidingMenu sm = getSlidingMenu();
		sm.setBehindWidth(300);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
		tabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
		tabHost.setup(this, getSupportFragmentManager(), R.id.base);
		
		tabHost.addTab(tabHost.newTabSpec("left").setIndicator("dd"),LeftFrag.class, null);
		tabHost.addTab(tabHost.newTabSpec("right").setIndicator("xx"),RightFrag.class, null);
	}
}
