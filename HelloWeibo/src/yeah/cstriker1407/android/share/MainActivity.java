package yeah.cstriker1407.android.share;

import yeah.cstriker1407.android.weibo.R;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

class Person
{
	public int age;
	public String name;
}


public class MainActivity extends Activity {

	
	private TextView textView;
	private Button button;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
//        textView = (TextView)findViewById(R.id.editText1);
//        button = (Button)findViewById(R.id.button1);
//        
//        button.setOnClickListener(new View.OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				ShareUtil.simpleShare(MainActivity.this, "Hello", "/mnt/sdcard/DCIM/Camera/1334120088159.jpg");
//			}
//		});
        
        
        SQLiteDatabase db = this.openOrCreateDatabase("test.db", Context.MODE_PRIVATE, null);
        db.execSQL("DROP TABLE IF EXISTS person");
        db.execSQL("CREATE TABLE person (_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, age SMALLINT)");
        
        Person person = new Person();
        person.name = "John";
        person.age = 30;
        db.execSQL("insert into person values(NuLL,?,?)", new Object[]{person.name,person.age});
  
        person.name = "dava";
        person.age = 20;
        ContentValues cv = new ContentValues();
        cv.put("name", person.name);
        cv.put("age", person.age);
        db.insert("person", null, cv);
        
        cv = new ContentValues();
        cv.put("age", 55);
        db.update("person", cv, "name = ?", new String[]{"John"});
        
        Cursor c = db.rawQuery("Select * From person Where age >= ?", new String[]{"10"});
        
        while (c.moveToNext())
		{
        	int _id = c.getInt(c.getColumnIndex("_id"));
        	String name = c.getString(c.getColumnIndex("name"));
			int age = c.getInt(c.getColumnIndex("age"));

			Log.e("db", "id = " +_id + "  name = " + name + "  age=" + age );
		}
        
        c.close();
        db.delete("person", "age < ?", new String[]{"35"});  
        
        //关闭当前数据库  
        db.close(); 
        deleteDatabase("test.db");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
