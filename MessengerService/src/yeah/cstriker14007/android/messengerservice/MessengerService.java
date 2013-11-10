package yeah.cstriker14007.android.messengerservice;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class MessengerService extends Service 
{
    
	private class ServiceHandler extends Handler 
	{
        @Override
        public void handleMessage(Message msg)
        {
        	Log.d("Service", msg.toString());
        	mActivityMessenger = msg.replyTo;
        	
            Message msgToAct = new Message();
            msgToAct.obj = "this is from service";
        	try
			{
        		mActivityMessenger.send(msgToAct);
			}
			catch (RemoteException e)
			{
				e.printStackTrace();
			}
        }
    }
    private Messenger mServiceMessenger = new Messenger(new ServiceHandler());
    private Messenger mActivityMessenger = null;
    
    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(getApplicationContext(), "binding", Toast.LENGTH_SHORT).show();
        return mServiceMessenger.getBinder();
    }
}
