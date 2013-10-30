package yeah.cstriker1407.android.baseapp;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				String str = null;
				
				int len = str.length();
			}
		}).start();
    }
}
