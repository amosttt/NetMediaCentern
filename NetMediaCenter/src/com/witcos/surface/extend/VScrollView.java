package com.witcos.surface.extend;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class VScrollView extends android.widget.ScrollView {

	private int ori =-1 ;
	private Timer timer;
	private boolean flag=true;
	////////////////////////////////////////////////////
	private TextView textView;
	public TextView getTextView() {
		return textView;
	}
	//////////////////////////////////////////////////
	//是否是主播放区
	private boolean isMainPlayer = false;
	public void setMainPlayer(boolean isMainPlayer) {
		this.isMainPlayer = isMainPlayer;
	}
	//////////////////////////////////////////////////
	//播放结束定时器
	private VTextViewStopListener listener;
	public void setOnVTextViewStopListener(VTextViewStopListener listener) {
		this.listener = listener;
	}
	public interface VTextViewStopListener {
		void stoped();
	}
	//////////////////////////////////////////////////
	public VScrollView(Context context) {
		super(context);
		setVerticalScrollBarEnabled(false);
		setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		textView = new TextView(context);
		textView.setSingleLine(false); 
		this.addView(textView);
	}

	private Handler message = new Handler() {
		public void handleMessage(Message msg) {
			doScrow();
		}
	};

	public void doScrow() {
		int now = getScrollY();
		if (ori == now) {
			if(null != listener && !flag && isMainPlayer){
			    listener.stoped();
			}
			ori = -1;
			scrollTo(now, 0);
			flag=false;
		} else {
			ori = now;
			smoothScrollBy(0, 1);
		}
	}

	public void startThread() {
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				message.sendEmptyMessage(0);
			}
		}, 0, 100);
	}

	/*
	 * 重写父类方法 主要是用于释放timer定时器资源
	 */
	@Override
	public void destroyDrawingCache() {
		if (null != timer) {
			timer.cancel();
		}
		super.destroyDrawingCache();
	}
}
