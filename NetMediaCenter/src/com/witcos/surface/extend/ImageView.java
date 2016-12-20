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
	
	private String id ; //ģ��ID
	
	private int temp = 0;
	private Timer timer;
	private Bitmap bitmap = null;
	private String[] fileNames;  //ͼƬ�ļ�����
	
	///////////////////
	//�Ƿ���������ģ��
	private boolean isMainPlayer = false;
	public void setMainPlayer(boolean isMainPlayer) {
		this.isMainPlayer = isMainPlayer;
	}
	/////////////////////////
	//////////////////////////
	//���Ž��������� �ص�����
	private VTextViewStopListener listener;
	public void setOnVTextViewStopListener(VTextViewStopListener listener) {
		this.listener = listener;
	}
	public interface VTextViewStopListener {
		void stoped();
	}
	/////////////////////////////
	//������
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
    
    //������ʾ��λͼ
    private void setImagePath(int i){
    	if(null != bitmap && !bitmap.isRecycled()){
			bitmap.recycle(); //�ͷ���Դ
			bitmap = null;
		}
    	bitmap = MapCache.getBitmap(fileNames[i].split(",")[0]);
    	BroadcastReceiverHelps.sendTargetInfo(id,fileNames[i].split(",")[1]); //���͹㲥
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
    
    //��ʾͼƬ ��������
    public void showImage(int sec){
    	if(fileNames.length == 1){
    		//�����һ��ͼƬ
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
    		//����ж��ͼƬ����������ʱ����
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
			bitmap.recycle();//�ͷ���Դ
			bitmap=null;
		}
    	super.destroyDrawingCache();
    }
}
