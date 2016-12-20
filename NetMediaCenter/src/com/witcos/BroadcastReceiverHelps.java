package com.witcos;

import java.util.ArrayList;
import java.util.Vector;

import com.jz.bean.FilterBean;
import com.witcos.entity.task.Task;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class BroadcastReceiverHelps extends BroadcastReceiver {

	private static Context contexts;

	private static String taskAction = "witcos.intent.action.taskaction";

	private static ArrayList<FilterBean> filterBeans;

	@Override
	public void onReceive(Context context, Intent intent) {
		contexts = context;
		Bundle bundle = intent.getExtras();
		int type = bundle.getInt("type");
		switch (type) {
		case 1: // 启动service
			String id = bundle.getString("id",""); 
			int x = bundle.getInt("x",0);
			int y = bundle.getInt("y",0);
			String server = bundle.getString("server","");
			String media = bundle.getString("media","");
			startServer(id, x, y, server, media);
			break;
		case 2: // 启动activity
			boolean isTime = bundle.getBoolean("isTime",false);
		    String taskid = bundle.getString("tid","");
		    int size = bundle.getInt("playsize",1);
			filterBeans = (ArrayList<FilterBean>) bundle
					.getSerializable("LIST");
			startPlayTask(isTime, taskid, size);
			break;
		case 3: // 启动activity
			sendStopPlay();
			break;
		default:
			break;
		}
	}

	/** 启动后台服务 **/
	public static void startServer(String id, int x, int y, String server,
			String media) {
		Intent intent = new Intent(contexts, MyService.class);
		// 用Bundle携带数据
		Bundle bundle = new Bundle();
		bundle.putString("id", id);
		bundle.putInt("x", x);
		bundle.putInt("y", y);
		bundle.putString("server", server);
		bundle.putString("media", media);
		intent.putExtras(bundle);
		contexts.startService(intent);
	}

	/** 启动activity 播放任务 **/
	public static void startPlayTask(boolean isTime, String tid, int size) {
		Intent intent = new Intent(contexts, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction("android.intent.action.MAIN");
		// 用Bundle携带数据
		Bundle bundle = new Bundle();
		bundle.putBoolean("isTime", isTime);
		bundle.putString("tid", tid);
		bundle.putInt("playsize", size);
		bundle.putSerializable("LIST", filterBeans);
		intent.putExtras(bundle);
		contexts.startActivity(intent);
	}

	/** 发送循环播放任务总数 **/
	public static void sendLoopTaskSize(int size) {
		Intent intent = new Intent();
		intent.addFlags(Intent. FLAG_INCLUDE_STOPPED_PACKAGES);
		intent.setAction(taskAction);
		intent.putExtra("type", 1);
		intent.putExtra("loopsize", size);
		contexts.sendBroadcast(intent);
	}

	/** 发送定时任务信息 **/
	public static void sendTimeTaskInfo(Task t){
		Intent intent = new Intent();
		intent.addFlags(Intent. FLAG_INCLUDE_STOPPED_PACKAGES);
		intent.setAction(taskAction);
		intent.putExtra("type", 2);
		intent.putExtra("tid",       t.getTid());  
		intent.putExtra("readtxt",   t.getReadtext());  
		intent.putExtra("playsize",  t.getPlaySize());
		intent.putExtra("playlevel", t.getPlaylevel());
		contexts.sendBroadcast(intent);
	}

	/** 发送停止播放信号 **/
	public static void sendStopPlay() {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
		intent.setAction("com.test.broadcast");
		contexts.sendBroadcast(intent);
	}

	/** 发送关联信息 **/
	public static void sendTargetInfo(String targetname, String targetvalue) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
		intent.setAction(taskAction);
		intent.putExtra("type", 3);
		intent.putExtra("targetname", targetname);
		intent.putExtra("targetvalue", targetvalue);
		contexts.sendBroadcast(intent);
	}
}
