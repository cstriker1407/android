package yeah.cstriker1407.android.rider.storage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Bitmaps {
	/*
	 * 先计算出新生成的bitmap的大小，然后根据不同的坐标使用canvas来绘制。
	 */
	public static Bitmap joinTopBottomBitmap(Bitmap top, Bitmap bottom) {
		if (null == top || null == bottom) {
			return null;
		}

		int topW = top.getWidth();
		int topH = top.getHeight();

		int bottomW = top.getWidth();
		int bottomH = top.getHeight();
		int resultW = topW > bottomW ? topW : bottomW;
		int resultH = topH + bottomH;
		Bitmap result = Bitmap.createBitmap(resultW, resultH,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(result);
		canvas.drawBitmap(top, 0, 0, null);
		canvas.drawBitmap(bottom, 0, topH, null);

		return result;
	}

	public static boolean writeBitmapToFile(Bitmap map, String filePath) {
		if (null == map || null == filePath) {
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
		} catch (IOException e) {
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
