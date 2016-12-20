package com.witcos.tool;

import java.util.Calendar;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

public class MyUtil {
	
	private static final String action_time = "android.intent.action.time";
	private static final String action_apk  = "android.intent.action.apk";

	/**
	 * ��������
	 * @param context
	 * @param volume  0-10 ֵ
	 */
	public static void updateAudio(Context context,int volume){
		//��������
		AudioManager mAudioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);  
		int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);  //��ȡ��ǰ�������ֵ
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume * volume/10, 0);	
		//mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC,false); //���þ���
	}
	
	/** ת�����ӷ��� **/
	public static synchronized int getMinute(){
		Calendar calendar=Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int min = calendar.get(Calendar.MINUTE);
		return hour * 60 + min;
	}
	
	/** ת�����ӷ��� **/
	public static synchronized int getMinute(String time){
		String hourStr = time.split(":")[0];
		String minStr  = time.split(":")[1];
		int hour = Integer.parseInt(hourStr);
		int min =  Integer.parseInt(minStr);
		return hour * 60 + min;
	}	
	
	/**
	 * �趨ϵͳʱ��
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
	 * ��Ĭ��װ����APK
	 * @param context
	 * @param path  APK·��
	 * @param type  0��װ�겻������1��װ������������2��װ������ϵͳ 3��װ���Զ��ػ�
	 * @param packages   APK����
	 * @param packageall ȫ�μ�����+����
	 */
	public static void updateAPK(Context context,String path,int type,String packages,String packageall){
		Intent intent = new Intent();
		intent.setAction(action_apk);
		intent.putExtra("path_apk",path);//apk��ŵ�Ŀ¼
		intent.putExtra("start_apk", type);//0��װ�겻������1��װ������������2��װ������ϵͳ 3��װ���Զ��ػ�
		intent.putExtra("package_apk",packages);   //APK����    
		intent.putExtra("class_apk",  packageall); //ȫ�μ�����+����
		context.sendBroadcast(intent);
	}

}
