package com.witcos.surface.extend;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

import com.witcos.BroadcastReceiverHelps;
import com.witcos.cache.MapCache;

public class ImageView extends android.widget.ImageView {
	
	private String id ; //模块ID
	
	private int temp = 0;
	private Timer timer;
	private Bitmap bitmap = null;
	private String[] fileNames;  //图片文件集合
	
	///////////////////
	//是否是主播放模块
	private boolean isMainPlayer = false;
	public void setMainPlayer(boolean isMainPlayer) {
		this.isMainPlayer = isMainPlayer;
	}
	/////////////////////////
	//////////////////////////
	//播放结束监听器 回调函数
	private VTextViewStopListener listener;
	public void setOnVTextViewStopListener(VTextViewStopListener listener) {
		this.listener = listener;
	}
	public interface VTextViewStopListener {
		void stoped();
	}
	/////////////////////////////
	//构造器
	public ImageView(Context context,String[] fileNames,String id) {
		super(context);
		this.fileNames = fileNames;
		this.id = id;
	}
	
	//handler
	private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
        	setImageRun();
        }
    };
    
    //设置显示的位图
    private void setImagePath(int i){
    	if(null != bitmap && !bitmap.isRecycled()){
			bitmap.recycle(); //释放资源
			bitmap = null;
		}
    	bitmap = MapCache.getBitmap(fileNames[i].split(",")[0]);
    	BroadcastReceiverHelps.sendTargetInfo(id,fileNames[i].split(",")[1]); //发送广播
    	setImageBitmap(bitmap);
    }
	
    private void setImageRun(){
    	if(fileNames.length == 1){
    		if(null != listener && isMainPlayer){
    			listener.stoped();
    			return ;
    		}
    	}
    	if(temp == fileNames.length){
    		temp = 0;
    		if(null != listener && isMainPlayer){
    			listener.stoped();
    			return ;
    		}
    	}
    	setImagePath(temp);
    	temp++;
    }
    
    //显示图片 传入秒数
    public void showImage(int sec){
    	if(fileNames.length == 1){
    		//如果有一个图片
    		setImagePath(0);
    		if(null != listener && isMainPlayer){
    			timer=new Timer();
    			timer.schedule(new TimerTask() {
					@Override
					public void run() {
						handler.sendEmptyMessage(0);
					}
				}, 1000*sec);
    		}
    	}else{
    		//如果有多个图片，则启动定时任务
        	this.timer=new Timer();
            timer.schedule(new TimerTask() {
    			@Override
    			public void run() {
    		         handler.sendEmptyMessage(0);
    			}
    		}, 0,1000*sec);	
    	}
    }
    
    @Override
    public void destroyDrawingCache() {
    	if(null != timer){
			timer.cancel();
			timer=null;
    	}
    	if(null != bitmap && !bitmap.isRecycled()){
			bitmap.recycle();//释放资源
			bitmap=null;
		}
    	super.destroyDrawingCache();
    }
}
