package yeah.cstriker1407.android.rider.activity;

import yeah.cstriker1407.android.rider.R;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class RideStatActivity extends SherlockActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ridestat);
		
		TextView title = new TextView(getSupportActionBar().getThemedContext());
		title.setText(R.string.btn_ridestat);
		title.setTextSize(30f);
		title.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
		title.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		getSupportActionBar().setCustomView(title);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
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
}
