package com.witcos.surface.extend;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

public class HScrollView extends HorizontalScrollView{
	
	private Timer timer;
	
	private TextView textView;
	public TextView getTextView() {
		return textView;
	}

	public HScrollView(Context context) {
		super(context);
		setHorizontalScrollBarEnabled(false); 	//����ʾ������
		setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;   
			}
		});
		textView = new TextView(context);
		textView.setSingleLine(false); 			//����Ϊ������ʾ
		this.addView(textView);
	}
	
	//���������߳�
	public void startThread() {
		timer = new Timer(); 
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				message.sendEmptyMessage(0);
			}
		}, 0, 10);  //10msִ��һ��
	}
	
	private Handler message = new Handler() {
		public void handleMessage(Message msg) {
				doScrow();
		}
	};
	
	private int ori = 0;
	private int now = 0;
	 
	public void doScrow() {
		now = getScrollX() + textView.getWidth() ;
		if (ori == now) {
			ori = 0;
			scrollTo(0,now);
		} else {
			smoothScrollBy(1, 0);
			ori ++;
		}
		if(ori == 2){
			ori = now - 5;
		}
	}
	
	@Override
	public void destroyDrawingCache() {
		if (null != timer) {
			timer.cancel();
			timer = null ;
		}
		super.destroyDrawingCache();
	}

}
