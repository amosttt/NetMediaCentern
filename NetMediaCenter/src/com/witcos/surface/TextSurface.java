package com.witcos.surface;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.witcos.BroadcastReceiverHelps;
import com.witcos.ChangeAllViewsListener;
import com.witcos.cache.MapCache;
import com.witcos.entity.WeatherInfo;
import com.witcos.surface.extend.HScrollView;
import com.witcos.surface.extend.ReadText;
import com.witcos.surface.extend.TimeView;
import com.witcos.surface.extend.VScrollView;
import com.witcos.tool.MyLayout;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.widget.AbsoluteLayout;
import android.widget.TextView;

public class TextSurface {
	
	private Context context;
	private AbsoluteLayout abs;
	
	//监听器
	private ChangeAllViewsListener listener;
	public void setonChangeAllViewListener(ChangeAllViewsListener listener){
		this.listener=listener;
	}
	
	public TextSurface(Context context,AbsoluteLayout abs){
		this.abs=abs;
		this.context=context;
	}
	
	/* 读取水平滚动字幕文本文件  */
	private String getShortText(String[] fileNames,int width,String id){
		StringBuffer sb=new StringBuffer();
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < width/10; i++) {
			sb.append(" ");
		}
		sb.append("                      ");
		for(String fileName : fileNames){
			sb.append(ReadText.getHStringForTxt(fileName.split(",")[0]));
			sb.append("                  ");
			list.add(fileName.split(",")[1]);
		}
		for (int j = 0; j < width/10; j++) {
			sb.append(" ");
		}
		BroadcastReceiverHelps.sendTargetInfo(id,list.toString()); //发送广播
		return sb.toString();
	}
	
	/* 读取垂直滚动字幕文本文件  */
	private String getLongText(String[] fileNames,String id){
		StringBuffer sb=new StringBuffer();
		List<String> list = new ArrayList<String>(); 
		sb.append("\n\n\n\n\n");
		for(String fileName : fileNames){
			sb.append(ReadText.getVStringForTxt(fileName.split(",")[0]));	
			sb.append("\n");
			list.add(fileName.split(",")[1]);
		}
		sb.append("\n\n\n\n\n");
		BroadcastReceiverHelps.sendTargetInfo(id,list.toString()); //发送广播
		return sb.toString();
	}
	
	/* 日期时间显示两位数的方法 */
	private String format(int x) {
		String s = "" + x;
		if (s.length() == 1)
			s = "0" + s;
		return s;
	}
	
	/* 颜色16进制转换为10进制  */
	private static int[] convert16(String colorString){
		int[] temp = new int[3] ;
		temp[0] = Integer.parseInt(colorString.substring(1, 3),16);   
		temp[1] = Integer.parseInt(colorString.substring(3, 5),16);   
		temp[2] = Integer.parseInt(colorString.substring(5, 7),16);   
		return temp;
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////
	/* 显示水平滚动字幕  */
	public void addHTextView(int w,int h,int x,int y,int fontsize,String[] fileNames,
			String backColor,int backAlpha,String foreColor,int foreAlpha){
		HScrollView hs=new HScrollView(context);
		TextView textView=hs.getTextView();
        textView.setText(getShortText(fileNames,w,x+","+y));
        textView.setTextSize(MyLayout.getScale(fontsize));
        textView.setGravity(Gravity.TOP);
        textView.setBackgroundColor(Color.parseColor(backColor));	//背景颜色
        textView.getBackground().setAlpha(backAlpha);				//背景透明度
        int temp[] = convert16(foreColor); 
        textView.setTextColor(Color.argb(foreAlpha, temp[0], temp[1], temp[2])); //字体颜色及透明度
        
        textView.layout(x,y,x+w,y+h);//获的焦点
        hs.startThread();//启动滚动线程
        abs.addView(hs,new MyLayout().getLayout(w,h+10,x,y-5));
	}
	
	/* 显示垂直滚动字幕  */
	public void addVTextView(int w,int h,int x,int y,int fontsize,boolean isMain,String[] fileNames,
			String backColor,int backAlpha,String foreColor,int foreAlpha){
		VScrollView sv=new VScrollView(context);
		sv.setMainPlayer(isMain);//设置是否为主播放区
		sv.setOnVTextViewStopListener(new VScrollView.VTextViewStopListener() {
			@Override
			public void stoped() {
				//播放结束回调函数
				if(null != listener){
					listener.changeAllView();
				}
			}
		});
		TextView textView = sv.getTextView();
		textView.setText(getLongText(fileNames,x+","+y));
        textView.setTextSize(MyLayout.getScale(fontsize-8));
        
        textView.setBackgroundColor(Color.parseColor(backColor));	//背景颜色
        textView.getBackground().setAlpha(backAlpha);				//背景透明度
        int temp[] = convert16(foreColor); 
        textView.setTextColor(Color.argb(foreAlpha, temp[0], temp[1], temp[2])); //字体颜色及透明度
        textView.layout(x,y,x+w,y+h); //获的焦点
        sv.startThread(); //启动滚动线程
        abs.addView(sv,new MyLayout().getLayout(w,h,x,y));
	}

	/* 显示固定文本信息 */
	public void addTextView(int w,int h,int x,int y,int fontsize,String msg,String backColor,int backAlpha,String foreColor,int foreAlpha){
		TextView textView=new TextView(context);
        textView.setText(msg.trim());
        textView.setTextSize(MyLayout.getScale(fontsize));
        textView.setGravity(Gravity.TOP);
        textView.setBackgroundColor(Color.parseColor(backColor));	//背景颜色
        textView.getBackground().setAlpha(backAlpha);				//背景透明度
        int temp[] = convert16(foreColor); 
        textView.setTextColor(Color.argb(foreAlpha, temp[0], temp[1], temp[2])); //字体颜色及透明度
        abs.addView(textView,new MyLayout().getLayout(w,h+10,x,y-5));
	}
	
	/* 显示时间信息 */
	public void addTimeView(int w,int h,int x,int y,int fontsize,String backColor,int backAlpha,String foreColor,int foreAlpha){
		TimeView timeView=new TimeView(context);
		timeView.setTextSize(MyLayout.getScale(fontsize));
		timeView.setGravity(Gravity.TOP);
		timeView.setBackgroundColor(Color.parseColor(backColor));	//背景颜色
		timeView.getBackground().setAlpha(backAlpha);				//背景透明度
		int temp[] = convert16(foreColor); 
		timeView.setTextColor(Color.argb(foreAlpha, temp[0], temp[1], temp[2])); //字体颜色及透明度
		timeView.startThread();//刷新时间显示
		abs.addView(timeView,new MyLayout().getLayout(w,h+10,x,y-5));
	}
	
	/* 显示日期信息 */
	public void addDateView(int w,int h,int x,int y,int fontsize,String backColor,int backAlpha,String foreColor,int foreAlpha){
		TextView textView=new TextView(context);
		Calendar c = Calendar.getInstance();
		textView.setText(new StringBuilder()
				.append(c.get(Calendar.YEAR))
				.append("/").append(format(c.get(Calendar.MONTH) + 1))
				.append("/").append(format(c.get(Calendar.DAY_OF_MONTH))));
		textView.setTextSize(MyLayout.getScale(fontsize));
		textView.setGravity(Gravity.TOP);
		textView.setBackgroundColor(Color.parseColor(backColor));	//背景颜色
        textView.getBackground().setAlpha(backAlpha);				//背景透明度
        int temp[] = convert16(foreColor); 
        textView.setTextColor(Color.argb(foreAlpha, temp[0], temp[1], temp[2])); //字体颜色及透明度
		abs.addView(textView,new MyLayout().getLayout(w,h+10,x,y-5));
	}
	
	/* 显示星期几  */
	public void addWeekView(int w,int h,int x,int y,int fontsize,String backColor,int backAlpha,String foreColor,int foreAlpha){
		TextView textView=new TextView(context);
		Calendar c = Calendar.getInstance();
		String msg="";
		switch(c.get(Calendar.DAY_OF_WEEK)){
			case 1: msg="星期日"; break;
			case 2: msg="星期一"; break;
			case 3: msg="星期二"; break;
			case 4: msg="星期三"; break;
			case 5: msg="星期四"; break;
			case 6: msg="星期五"; break;
			case 7: msg="星期六"; break;
		}
		textView.setText(msg);
//		AssetManager mgr=context.getAssets();//得到AssetManager
//		Typeface tf=Typeface.createFromAsset(mgr, "fonts/ttf.ttf");//根据路径得到Typeface
//		tv.setTypeface(tf);//设置字体
		textView.setTextSize(MyLayout.getScale(fontsize));
		textView.setGravity(Gravity.TOP);
		textView.setBackgroundColor(Color.parseColor(backColor));	//背景颜色
        textView.getBackground().setAlpha(backAlpha);				//背景透明度
        int temp[] = convert16(foreColor); 
        textView.setTextColor(Color.argb(foreAlpha, temp[0], temp[1], temp[2])); //字体颜色及透明度
		abs.addView(textView,new MyLayout().getLayout(w,h+10,x,y-5));
	}
	
	/* 显示天气信息 */
	public void addWeatherView(int w,int h,int x,int y,int fontsize,String backColor,int backAlpha,String foreColor,int foreAlpha){
		WeatherInfo info = (WeatherInfo)MapCache.get("weatherInfo");
		StringBuffer sb = new StringBuffer();
		if(null != info){
			sb.append("湿度："+info.getHumidity()).append("；温度："+info.getTemperature())
				.append("； 风向："+info.getWindWay()).append("；天气："+info.getWeather());
		}else{
			sb.append("暂无天气信息");
		}
		HScrollView hs=new HScrollView(context);
		TextView textView=hs.getTextView();
        textView.setText("     	       "+sb.toString());
        textView.setTextSize(MyLayout.getScale(fontsize));
        textView.setGravity(Gravity.TOP);
        textView.setBackgroundColor(Color.parseColor(backColor));	//背景颜色
        textView.getBackground().setAlpha(backAlpha);				//背景透明度
        int temp[] = convert16(foreColor); 
        textView.setTextColor(Color.argb(foreAlpha, temp[0], temp[1], temp[2])); //字体颜色及透明度
        textView.layout(x,y,x+w,y+h);	//获的焦点
        hs.startThread();				//启动滚动线程
        abs.addView(hs,new MyLayout().getLayout(w,h+10,x,y-5));
	}
}
