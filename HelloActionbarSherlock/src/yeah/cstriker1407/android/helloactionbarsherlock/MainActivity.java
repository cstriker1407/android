package yeah.cstriker1407.android.helloactionbarsherlock;

import android.os.Bundle;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends SherlockActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu)
	{
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.delete:
			{
				Toast.makeText(this, "delete", Toast.LENGTH_SHORT).show();
				break;
			}
			case R.id.add:
			{
				Toast.makeText(this, "add", Toast.LENGTH_SHORT).show();
				break;
			}
//			case R.id.homeAsUp:
//			{
//				Toast.makeText(this, "homeAsUp", Toast.LENGTH_SHORT).show();
//				break;
//			}
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}
}
