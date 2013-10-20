package yeah.cstriker1407.android.rider.storage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;

public class Bitmaps 
{
	public static boolean writeBitmapToFile(Bitmap map, String filePath)
	{
		if (null == map || null == filePath)
		{
			return false;
		}
		FileOutputStream fileOutputStream = null; 
				
		try {
			fileOutputStream = new FileOutputStream(filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		map.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);

		boolean result = true;

		
		try {
			fileOutputStream.flush();
		} catch (IOException e) 
		{
			e.printStackTrace();
			result = false;
		}
		try {
			fileOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			result = false;
		}
		
		return result;
	}

}
