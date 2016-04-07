package com.serenegiant.vo.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class BitmapUtil {
	//使用Bitmap加Matrix来缩放,缩放到指定宽高
    public static Bitmap resizeImage(Bitmap bitmap, int w, int h) 
    {  
        Bitmap BitmapOrg = bitmap;  
        int width = BitmapOrg.getWidth();  
        int height = BitmapOrg.getHeight();  
        int newWidth = w;  
        int newHeight = h;  
 
        float scaleWidth = ((float) newWidth) / width;  
        float scaleHeight = ((float) newHeight) / height;  
 
        Matrix matrix = new Matrix();  
        matrix.postScale(scaleWidth, scaleHeight);  
        return Bitmap.createBitmap(BitmapOrg, 0, 0, width,  
                height, matrix, true); 
    }
}
