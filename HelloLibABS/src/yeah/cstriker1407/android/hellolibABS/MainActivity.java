package yeah.cstriker1407.android.hellolibABS;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener 
{
	private Button button1;
	private Button button2;
	private Button button3;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        button1 = (Button)findViewById(R.id.button1);
        button2 = (Button)findViewById(R.id.button2);
        button3 = (Button)findViewById(R.id.button3);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
    }

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.button1:
			{
				Intent intent = new Intent(this, FirstActivity.class);
				startActivity(intent);
				break;
			}
			case R.id.button2:
			{
				Intent intent = new Intent(this, SecondActivity.class);
				startActivity(intent);
				break;
			}
			case R.id.button3:
			default:
			{
				Intent intent = new Intent(this, ThirdActivity.class);
				startActivity(intent);
				break;
			}
		}
	}
}
