package yeah.cstriker1407.android.rider.activity;

import yeah.cstriker1407.android.rider.R;
import yeah.cstriker1407.android.rider.fragment.AboutMeFragment;
import yeah.cstriker1407.android.rider.fragment.OffLineFragment;
import yeah.cstriker1407.android.rider.fragment.ShareFragment;
import yeah.cstriker1407.android.rider.fragment.WeiboFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class SettingActivity extends SlidingFragmentActivity implements OnClickListener 
{
	private static final String TAG = "SettingActivity";
	
	private Button btn_lxdt;
	private Button btn_wbzh;
	private Button btn_fxrj;
	private Button btn_aboutme;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_setting);
		setBehindContentView(R.layout.activity_setting_menu);
		setSlidingActionBarEnabled(false);
		
		SlidingMenu sm = getSlidingMenu();
		sm.setBehindWidth(200);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		btn_lxdt = (Button)sm.findViewById(R.id.btn_lxdt);
		btn_wbzh = (Button)sm.findViewById(R.id.btn_wbzh);
		btn_fxrj = (Button)sm.findViewById(R.id.btn_fxrj);
		btn_aboutme = (Button)sm.findViewById(R.id.btn_aboutme);
		btn_lxdt.setOnClickListener(this);
		btn_wbzh.setOnClickListener(this);
		btn_fxrj.setOnClickListener(this);
		btn_aboutme.setOnClickListener(this);
		
		TextView title = new TextView(getSupportActionBar().getThemedContext());
		title.setText(R.string.btn_setting);
		title.setTextSize(30f);
		title.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
		title.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		getSupportActionBar().setCustomView(title);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		
		onClick(btn_lxdt);
	}
	
	private void cancelAndQuit()
	{
		this.finish();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
			case android.R.id.home:
			{
				cancelAndQuit();
				break;
			}
			default:
				break;
		}
		return true;
	}
	@Override
	public void onClick(View v) 
	{
		toggle();
		
		Fragment target = null;
		btn_lxdt.setEnabled(true);
		btn_wbzh.setEnabled(true);
		btn_fxrj.setEnabled(true);
		btn_aboutme.setEnabled(true);
		switch (v.getId()) 
		{
			case R.id.btn_lxdt:
			{
				btn_lxdt.setEnabled(false);
				target = new OffLineFragment();
				break;
			}
			case R.id.btn_wbzh:
			{
				btn_wbzh.setEnabled(false);
				target = new WeiboFragment();
				break;
			}
			case R.id.btn_fxrj:
			{
				btn_fxrj.setEnabled(false);
				target = new ShareFragment();
				break;
			}
			case R.id.btn_aboutme:
			{
				btn_aboutme.setEnabled(false);
				target = new AboutMeFragment();
				break;
			}
		}
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.replace(R.id.frame_setting, target);
		ft.commit();
	}
}
