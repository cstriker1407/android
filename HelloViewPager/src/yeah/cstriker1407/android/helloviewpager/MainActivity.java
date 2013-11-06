package yeah.cstriker1407.android.helloviewpager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends Activity 
{
	private static final int page_size = 5000;
	
	private View view1, view2, view3, view4;
	private ViewPager viewPager;
	private List<View> viewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();  
    }  
    private void initView() { 
        viewPager = (ViewPager) findViewById(R.id.viewpager); 
        view1 = View.inflate(this,R.layout.layout1, null);  
        view2 = View.inflate(this,R.layout.layout2, null);  
        view3 = View.inflate(this,R.layout.layout3, null);  
        view4 = View.inflate(this,R.layout.layout4, null);  
  
        viewList = new ArrayList<View>();// 将要分页显示的View装入数组中  
        viewList.add(view1);  
        viewList.add(view2);  
        viewList.add(view3);  
        viewList.add(view4);  
  
        PagerAdapter pagerAdapter = new PagerAdapter() {  
  
            @Override  
            public boolean isViewFromObject(View arg0, Object arg1)
            {  
                return arg0 == arg1;  
            }  
  
            @Override  
            public int getCount() {  
            	return page_size;
            }  
  
            @Override  
            public void destroyItem(ViewGroup container, int position,  
                    Object object) 
            {
            	Log.d("", "destroy item called:" + position);
            	
            	View view = viewList.get(position % viewList.size());
            	container.removeView(viewList.get(position % viewList.size()));
            	view.setTag("remove");
            }  
            
            @Override  
            public Object instantiateItem(ViewGroup container, int position) 
            {
            	Log.d("", "instantiateItem called:" + position);
            	
            	
            	View view = viewList.get(position % viewList.size());
            	if (view.getTag()== null || view.getTag().toString().equals("remove"))
            	{
            		Log.d("", "view added called:" + position + "    viewpos:" + position % viewList.size());
            		container.addView(view);
            		view.setTag("add");
            	}
                return view;
            }  
  
        };  
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
		{
			@Override
			public void onPageSelected(int arg0)
			{
				Log.d("", "onPageSelected called:" + arg0);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2)
			{
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0)
			{
			}
		});
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(page_size/2, false);
    }   
}
