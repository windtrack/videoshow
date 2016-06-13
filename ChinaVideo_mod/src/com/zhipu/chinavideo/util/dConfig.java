package com.zhipu.chinavideo.util;


public class dConfig {

	public static final int Default_Width = 720 ;
	public static final int Default_Height = 1280 ;
	
	public static int Screen_Width;
	public static int Screen_Height;
	
	public static float ScaleX;
	public static float ScaleY;
	public static float curScale ;

	public static void initScale(int width,int height){
		Screen_Width = width ;
		Screen_Height = height;
		
		ScaleX = (float)Screen_Width/Default_Width ;
		ScaleY = (float)Screen_Height/Default_Height ;
		curScale = ScaleX>ScaleY?ScaleX:ScaleY;
		
	}
	
}
