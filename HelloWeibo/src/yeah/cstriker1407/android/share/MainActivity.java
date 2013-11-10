package yeah.cstriker1407.android.share;

import yeah.cstriker1407.android.weibo.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.db.OauthHelper;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.media.UMImage;

public class MainActivity extends Activity implements OnClickListener {
	
	private EditText editText;
	private Button button1;
	private Button button2;
	private Button button3;
	private Button button4;
	private Button button5;
	private Button button6;
	private Button button7;
	
	final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share",
            RequestType.SOCIAL);
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText)findViewById(R.id.editText);
        button1 = (Button)findViewById(R.id.button1);
        button2 = (Button)findViewById(R.id.button2);
        button3 = (Button)findViewById(R.id.button3);
        button4 = (Button)findViewById(R.id.button4);
        button5 = (Button)findViewById(R.id.button5);
        button6 = (Button)findViewById(R.id.button6);
        button7 = (Button)findViewById(R.id.button7);
        
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);    
    }

	@Override
	public void onClick(View v) 
	{
		switch (v.getId())
		{
		case R.id.button1:
		{
			// 设置分享内容
			mController.setShareContent(editText.getText().toString());
			// 设置分享图片, 参数2为图片的地址
			mController.setShareMedia(new UMImage(this,"http://www.umeng.com/images/pic/banner_module_social.png"));
			//设置分享图片，参数2为本地图片的资源引用
			//mController.setShareMedia(new UMImage(getActivity(), R.drawable.icon));
			//设置分享图片，参数2为本地图片的路径(绝对路径)
			//mController.setShareMedia(new UMImage(getActivity(), 
//			                                BitmapFactory.decodeFile("/mnt/sdcard/icon.png")));
	        
			
			// 设置分享平台选择面板的平台显示顺序
			// mController.getConfig().setPlatformOrder(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
//			                                         SHARE_MEDIA.QZONE,SHARE_MEDIA.QQ, SHARE_MEDIA.SINA);
			// 打开平台选择面板，参数2为打开分享面板时是否强制登录,false为不强制登录
	        mController.openShare(this, false);			
			
			break;
		}
		case R.id.button2:
		{
			// 设置分享内容
			mController.setShareContent(editText.getText().toString());
			mController.postShare(MainActivity.this,SHARE_MEDIA.SINA, 
			        new SnsPostListener() {
			                @Override
			                public void onStart() {
			                    Toast.makeText(MainActivity.this, "开始分享.", Toast.LENGTH_SHORT).show();
			                }

			                @Override
			                public void onComplete(SHARE_MEDIA platform, int eCode,SocializeEntity entity) {
			                     if (eCode == 200) {
			                         Toast.makeText(MainActivity.this, "分享成功.", Toast.LENGTH_SHORT).show();
			                     } else {
			                          String eMsg = "";
			                          if (eCode == -101){
			                              eMsg = "没有授权";
			                          }
			                          Toast.makeText(MainActivity.this, "分享失败[" + eCode + "] " + 
			                                             eMsg,Toast.LENGTH_SHORT).show();
			                     }
			              }
			});
			
			
			break;
		}
		case R.id.button3:
		{
			// 设置分享内容
			mController.setShareContent(editText.getText().toString());
			mController.directShare(MainActivity.this, SHARE_MEDIA.SINA,
		            new SnsPostListener() {

		            @Override
		            public void onStart() {
		                Toast.makeText(MainActivity.this, "分享开始",Toast.LENGTH_SHORT).show();
		            }

		            @Override
		            public void onComplete(SHARE_MEDIA platform,int eCode, SocializeEntity entity) {
		                if(eCode == StatusCode.ST_CODE_SUCCESSED){
		                    Toast.makeText(MainActivity.this, "分享成功",Toast.LENGTH_SHORT).show();
		                }else{
		                    Toast.makeText(MainActivity.this, "分享失败",Toast.LENGTH_SHORT).show();
		                }
		            }
		    });
			break;
		}
		case R.id.button4:
		{			
			boolean isSina = OauthHelper.isAuthenticated(this,SHARE_MEDIA.SINA);
			Toast.makeText(this, "Sina:" + Boolean.toString(isSina), Toast.LENGTH_SHORT).show();
			break;
		}
		case R.id.button5:
		{
//			OauthHelper.remove(this, SHARE_MEDIA.SINA);
			mController.deleteOauth(this, SHARE_MEDIA.SINA, new SocializeClientListener() {
				@Override
				public void onStart()
				{
					Toast.makeText(MainActivity.this, "on start", Toast.LENGTH_SHORT).show();
				}
				
				@Override
				public void onComplete(int arg0, SocializeEntity arg1) 
				{
					Toast.makeText(MainActivity.this, "on complete", Toast.LENGTH_SHORT).show();
				}
			});
			
			boolean isSina = OauthHelper.isAuthenticated(this,SHARE_MEDIA.SINA);
			Toast.makeText(this, "Sina:" + Boolean.toString(isSina), Toast.LENGTH_SHORT).show();
			
			break;
		}
		case R.id.button6:
		{
			mController.doOauthVerify(this, SHARE_MEDIA.SINA, new UMAuthListener() {
			    @Override
			    public void onStart(SHARE_MEDIA platform) {
			        Toast.makeText(MainActivity.this, "授权开始", Toast.LENGTH_SHORT).show();
			    }

			    @Override
			    public void onError(SocializeException e, SHARE_MEDIA platform) {
			        Toast.makeText(MainActivity.this, "授权错误", Toast.LENGTH_SHORT).show();
			    }

			    @Override
			    public void onComplete(Bundle value, SHARE_MEDIA platform) {
			        Toast.makeText(MainActivity.this, "授权完成", Toast.LENGTH_SHORT).show();
			        //获取相关授权信息或者跳转到自定义的分享编辑页面
			        String uid = value.getString("uid");
			        
			    }

			    @Override
			    public void onCancel(SHARE_MEDIA platform) {
			        Toast.makeText(MainActivity.this, "授权取消", Toast.LENGTH_SHORT).show();
			    }
			} );
			
			
			break;
		}
		case R.id.button7:
		{
			ShareUtil.simpleShare(this, editText.getText().toString(), null);
		}
		default:
			break;
		}
	}    
}
