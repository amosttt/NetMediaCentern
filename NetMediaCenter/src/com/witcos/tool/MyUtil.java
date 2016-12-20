package com.witcos.tool;

import java.util.Calendar;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

public class MyUtil {
	
	private static final String action_time = "android.intent.action.time";
	private static final String action_apk  = "android.intent.action.apk";

	/**
	 * 设置音量
	 * @param context
	 * @param volume  0-10 值
	 */
	public static void updateAudio(Context context,int volume){
		//设置音量
		AudioManager mAudioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);  
		int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);  //获取当前罪大音量值
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume * volume/10, 0);	
		//mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC,false); //设置静音
	}
	
	/** 转换分钟方法 **/
	public static synchronized int getMinute(){
		Calendar calendar=Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int min = calendar.get(Calendar.MINUTE);
		return hour * 60 + min;
	}
	
	/** 转换分钟方法 **/
	public static synchronized int getMinute(String time){
		String hourStr = time.split(":")[0];
		String minStr  = time.split(":")[1];
		int hour = Integer.parseInt(hourStr);
		int min =  Integer.parseInt(minStr);
		return hour * 60 + min;
	}	
	
	/**
	 * 设定系统时间
	 * @param context
	 * @param longtime 20141212183050
	 */
	public static void updatetime(Context context,String longtime){
		Intent intent = new Intent();
		intent.setAction(action_time);
		intent.putExtra("time_system", longtime);
		context.sendBroadcast(intent);
	}
	
	/**
	 * 静默安装升级APK
	 * @param context
	 * @param path  APK路径
	 * @param type  0安装完不启动，1安装完立即启动，2安装完重启系统 3安装完自动关机
	 * @param packages   APK包名
	 * @param packageall 全形级包名+类名
	 */
	public static void updateAPK(Context context,String path,int type,String packages,String packageall){
		Intent intent = new Intent();
		intent.setAction(action_apk);
		intent.putExtra("path_apk",path);//apk存放的目录
		intent.putExtra("start_apk", type);//0安装完不启动，1安装完立即启动，2安装完重启系统 3安装完自动关机
		intent.putExtra("package_apk",packages);   //APK包名    
		intent.putExtra("class_apk",  packageall); //全形级包名+类名
		context.sendBroadcast(intent);
	}

}
