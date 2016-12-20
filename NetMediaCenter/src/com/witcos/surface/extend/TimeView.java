package com.witcos.surface.extend;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class TimeView extends TextView {
	
	private Timer timer = null ;

	public TimeView(Context context) {
		super(context);
	}
	
	Handler handler = new Handler() {
        public void handleMessage(Message msg) {
        	updateDisplay();
        }
    };
    
    private void updateDisplay(){
    	Calendar c = Calendar.getInstance();
		this.setText(new StringBuilder()
				.append(format(c.get(Calendar.HOUR_OF_DAY)))
				.append(":").append(format(c.get(Calendar.MINUTE)))
				.append(":").append(format(c.get(Calendar.SECOND))));	
    }
    
    /* 日期时间显示两位数的方法 */
	private String format(int x) {
		String s = "" + x;
		if (s.length() == 1)
			s = "0" + s;
		return s;
	}
    
    public void startThread(){
		timer=new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				handler.sendEmptyMessage(0);   
			}
		}, 0, 1000);
	}
    
    @Override
	public void destroyDrawingCache() {
		if(null != timer){
			timer.cancel();
			timer = null ;
		}
		super.destroyDrawingCache();
	}
}
