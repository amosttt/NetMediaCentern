package com.witcos.tool;

import com.witcos.cache.MapCache;
import com.witcos.entity.SystemInfo;

import android.view.ViewGroup.LayoutParams;
import android.widget.AbsoluteLayout;

@SuppressWarnings({ "unused", "deprecation" })
public class MyLayout{
	
	private static float SCALE = 1.0f;
	
	public static int getScale(int temp){
		return Math.round(temp/SCALE);
	}

	public AbsoluteLayout.LayoutParams getLayout(int width,int height,int x, int y ){
		SystemInfo systemInfo = (SystemInfo)MapCache.get("systemInfo");		
		return new AbsoluteLayout.LayoutParams(getScale(width), getScale(height), 
				getScale(x) + systemInfo.getX(), getScale(y) + systemInfo.getY()
			);
	}
}
