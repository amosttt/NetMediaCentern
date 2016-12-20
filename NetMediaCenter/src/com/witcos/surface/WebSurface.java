package com.witcos.surface;

import java.util.Timer;
import java.util.TimerTask;

import com.witcos.ChangeAllViewsListener;
import com.witcos.tool.MyLayout;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;
import android.widget.AbsoluteLayout.LayoutParams;

public class WebSurface {
	
	private Context context;
	private AbsoluteLayout abs;
	private WebView web;
	private Timer timer;
	
	private int width;
	private int height;
	
	public WebSurface(Context context,AbsoluteLayout abs,WebView web,int width,int height){
		this.context=context;
		this.abs=abs;
		this.web = web;
		this.width= width;
		this.height = height;
	}
	
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (!Thread.currentThread().isInterrupted()) {
				switch (msg.what) {
				case 0:
		        	if(null != listener){
		        		timer.cancel();
			        	timer = null ;
		        		listener.changeAllView();
		        	}
					break;
				}
			}
			super.handleMessage(msg);
		}
	};
	
	/* 监听视频播放结束后调用切换视图 */
	private ChangeAllViewsListener listener;
	public void setOnChangeAllViewsListener(ChangeAllViewsListener listener) {
		this.listener = listener;
	}

	//启动显示flash网页
	public void startWebView(){
		abs.addView(web,new MyLayout().getLayout(width,height,0,0));
		this.timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				handler.sendEmptyMessage(0);
			}
		}, 60*1000*2, 60*1000*2);
	}
}
