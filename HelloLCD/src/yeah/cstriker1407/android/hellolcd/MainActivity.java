package yeah.cstriker1407.android.hellolcd;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	
	private TextView textView;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        textView = (TextView)findViewById(R.id.textView_custom);
        textView.setTextColor(Color.DKGRAY);

        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/LCD.ttf");
		textView.setTypeface(tf);
		textView.setTextSize(50f);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true; 
    }
    
}
