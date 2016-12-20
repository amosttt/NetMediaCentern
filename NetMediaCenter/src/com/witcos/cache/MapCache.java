package com.witcos.cache;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.witcos.tool.DownloadFile;

public class MapCache {
	
	private static Map<String, Object> map=new HashMap<String, Object>();
	
	public static Object get(String key){
		return map.get(key);
	}
	
	public static void put(String key,Object value){
		map.put(key, value);
	}
	
	//获取图片
	public static Bitmap getBitmap(String fileName){
		Bitmap bitmap = (Bitmap)get(fileName);
		if(null == bitmap){
			BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inSampleSize = 1;
	        bitmap = BitmapFactory.decodeFile(DownloadFile.FILEPATH + fileName, options);
//			put(fileName, bitmap); //放入缓存
		}
		return bitmap;
	}

}
