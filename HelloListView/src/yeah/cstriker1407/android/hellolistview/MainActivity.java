package yeah.cstriker1407.android.hellolistview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	private Button button1;
	private Button button2;
	private Button button3;
	private Button button4;
	private Button button5;
	private Button button6;
	private Button button7;
	private Button button8;
	private Button button9;
	private Button button10;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        button1 = (Button)findViewById(R.id.button1);
        button2 = (Button)findViewById(R.id.button2);
        button3 = (Button)findViewById(R.id.button3);
        button4 = (Button)findViewById(R.id.button4);
        button5 = (Button)findViewById(R.id.button5);
        button6 = (Button)findViewById(R.id.button6);
        button7 = (Button)findViewById(R.id.button7);
        button8 = (Button)findViewById(R.id.button8);
        button9 = (Button)findViewById(R.id.button9);
        button10 = (Button)findViewById(R.id.button10);
        
        
        button1.setOnClickListener(this);        
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        button10.setOnClickListener(this);
    }
	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.button1:
			{
				Intent intent = new Intent(this, Activity1.class);
				startActivity(intent);
				break;
			}
			case R.id.button2:
			{
				Intent intent = new Intent(this, Activity2.class);
				startActivity(intent);
				break;
			}
			case R.id.button3:
			{
				Intent intent = new Intent(this, Activity3.class);
				startActivity(intent);
				break;
			}
			case R.id.button4:
			{
				Intent intent = new Intent(this, Activity4.class);
				startActivity(intent);
				break;
			}
			case R.id.button5:
			{
				Intent intent = new Intent(this, Activity5.class);
				startActivity(intent);
				break;
			}
			case R.id.button6:
			{
				Intent intent = new Intent(this, Activity6.class);
				startActivity(intent);
				break;
			}

			case R.id.button7:
			{
				Intent intent = new Intent(this, Activity7.class);
				startActivity(intent);
				break;
			}
			case R.id.button8:
			{
				Intent intent = new Intent(this, Activity8.class);
				startActivity(intent);
				break;
			}
			case R.id.button9:
			{
				Intent intent = new Intent(this, Activity9.class);
				startActivity(intent);
				break;
			}
			case R.id.button10:
			{
				Intent intent = new Intent(this, Activity10.class);
				startActivity(intent);
				break;
			}
			
			default:
				break;
		}
	}
}
