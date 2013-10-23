package com.example.helloporgressbar;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;

public class MainActivity extends Activity implements OnClickListener {

	
	private ProgressBar bar_large;
	private ProgressBar bar_horizontal;
	private ProgressBar bar_color;
	
	
	private Button btn_left;
	private Button btn_center;
	private Button btn_right;

	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//标题栏上设置一个进度条
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_PROGRESS);
		
		setContentView(R.layout.activity_main);
		
		bar_large = (ProgressBar)findViewById(R.id.bar_large);
		bar_horizontal = (ProgressBar)findViewById(R.id.bar_horizontal);
		bar_color = (ProgressBar)findViewById(R.id.bar_color);
		
		
		btn_left = (Button)findViewById(R.id.btn_left);
		btn_center = (Button)findViewById(R.id.btn_center);
		btn_right = (Button)findViewById(R.id.btn_right);
		btn_left.setOnClickListener(this);
		btn_center.setOnClickListener(this);
		btn_right.setOnClickListener(this);
		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId())
		{
		case R.id.btn_left:
		{
			bar_large.setMax(100);
			bar_large.setProgress(50);
			bar_large.setSecondaryProgress(75);
			
			bar_horizontal.setMax(100);
			bar_horizontal.setProgress(50);
			bar_horizontal.setSecondaryProgress(75);
			
			bar_color.setMax(100);
			bar_color.setProgress(50);
			bar_color.setSecondaryProgress(75);
			
			
			break;
		}
		case R.id.btn_center:
		{
			setProgressBarVisibility(true);
			setProgress(5000);
			break;
		}

		case R.id.btn_right:
		{
			break;
		}

		default:
			break;
		}
		
		
	}

}
