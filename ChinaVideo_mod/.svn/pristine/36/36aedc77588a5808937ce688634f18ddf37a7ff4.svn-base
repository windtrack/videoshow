package com.zhipu.chinavideo.util;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;

public class Tools {

	public static void drawText(String str,Canvas canvas,Paint paint,float x,float y ,int inColor,int outColor,int textSize,int alpah){
		paint.reset();
		paint.setStrokeWidth(5);
		Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
		
		paint.setTypeface( font );
		paint.setTextSize(textSize);
		paint.setColor(outColor);
		paint.setAlpha(alpah);
		int px = 3 ;
		
		canvas.drawText(str, x+px, y, paint);
		canvas.drawText(str, x-px, y, paint);
		canvas.drawText(str, x, y+px, paint);
		canvas.drawText(str, x, y-px, paint);
		
		
		canvas.drawText(str, x+px, y+px, paint);
		canvas.drawText(str, x+px, y-px, paint);
		canvas.drawText(str, x-px, y+px, paint);
		canvas.drawText(str, x-px, y-px, paint);
				
		paint.reset();
		paint.setStrokeWidth(5);
		paint.setTypeface( font );
		paint.setTextSize(textSize);
		paint.setColor(inColor);
		paint.setAlpha(alpah);
		canvas.drawText(str, x, y, paint);
	
	}
	
	public static String ToSBC(String input) { 
        char c[] = input.toCharArray(); 
        for (int i = 0; i < c.length; i++) { 
            if (c[i] == ' ') { 
                c[i] = '\u3000'; 
            } else if (c[i] < '\177') { 
                c[i] = (char) (c[i] + 65248); 
            } 
        } 
        return new String(c); 
    } 
}
