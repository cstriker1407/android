package yeah.cstriker1407.android.helloviewflipper;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class MainActivity extends Activity implements OnGestureListener {
	
	private GestureDetector gestureDetector = null;
	private ViewFlipper viewFlipper;

	private int[] imgs = { R.drawable.img1, R.drawable.img2,  
            R.drawable.img3};  
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper1);
        gestureDetector = new GestureDetector(this, this);
        
        
        for (int i = 0; i < imgs.length; i++) 
        {
            ImageView iv = new ImageView(this);  
            iv.setId(i + 100000);
            iv.setImageResource(imgs[i]);  
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(iv, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }
        viewFlipper.setAutoStart(true);
        viewFlipper.setFlipInterval(2000);
        viewFlipper.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Log.d("id", "" + viewFlipper.getCurrentView().getId() );
			}
		});
        
        viewFlipper.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				viewFlipper.stopFlipping(); // 点击事件后，停止自动播放
				viewFlipper.setAutoStart(false);
				return gestureDetector.onTouchEvent(event); // 注册手势事件
			}
		});
    }

	@Override
	public boolean onDown(MotionEvent arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
	{
		if (e2.getX() - e1.getX() > 120)
		{ // 从左向右滑动（左进右出）
			Animation rInAnim = AnimationUtils.loadAnimation(this, R.anim.push_right_in); // 向右滑动左侧进入的渐变效果（alpha
																								// 0.1
																								// ->
																								// 1.0）
			Animation rOutAnim = AnimationUtils.loadAnimation(this, R.anim.push_right_out); // 向右滑动右侧滑出的渐变效果（alpha
																									// 1.0
																									// ->
																									// 0.1）

			viewFlipper.setInAnimation(rInAnim);
			viewFlipper.setOutAnimation(rOutAnim);
			viewFlipper.showPrevious();
			return true;
		}
		else if (e2.getX() - e1.getX() < -120)
		{ // 从右向左滑动（右进左出）
			Animation lInAnim = AnimationUtils.loadAnimation(this, R.anim.push_left_in); // 向左滑动左侧进入的渐变效果（alpha
																								// 0.1
																								// ->
																								// 1.0）
			Animation lOutAnim = AnimationUtils.loadAnimation(this, R.anim.push_left_out); // 向左滑动右侧滑出的渐变效果（alpha
																								// 1.0
																								// ->
																								// 0.1）

			viewFlipper.setInAnimation(lInAnim);
			viewFlipper.setOutAnimation(lOutAnim);
			viewFlipper.showNext();
			return true;
		}
		return true;
	}

	@Override
	public void onLongPress(MotionEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}
}
