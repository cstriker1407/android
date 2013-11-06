package yeah.cstriker1407.android.unkillservice;

import yeah.cstriker1407.unkillservice.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity 
{
	
	
	private Button btn_start;
	private Button btn_stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        btn_start = (Button)findViewById(R.id.btn_start);
        btn_stop = (Button)findViewById(R.id.btn_stop);
        
        btn_start.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(MainActivity.this, TargetService.class);
				MainActivity.this.startService(intent);
			}
		});
        
        btn_stop.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
			}
		});
        
        
        
        
        
        
        
        
        
        
        
    }
}
