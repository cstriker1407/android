package yeah.cstriker1407.android.rider.utils;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class ShareUtils
{
	public static boolean simpleShare(Context context, String text, String ImagePath)
	{
		if (null == text || text.trim().length() == 0)
		{
			return false;
		}

		Intent intent=new Intent(Intent.ACTION_SEND);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
	    intent.putExtra(Intent.EXTRA_SUBJECT, "share");   
	    intent.putExtra(Intent.EXTRA_TITLE, "title");
	    intent.putExtra(Intent.EXTRA_TEXT, text);  
	    intent.setType("text/plain");
	    if (ImagePath != null && ImagePath.trim().length() != 0)
		{
	    	File file = new File(ImagePath.trim());
	    	if (file.canRead())
	    	{
	    		  intent.setType("image/*");
	    		  intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
	    	}
		}
		  context.startActivity(intent);
		  return true;
//        context.startActivity(Intent.createChooser(intent, "è¯·é?æ‹?));   
	}
}
