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
	
	//������
	private ChangeAllViewsListener listener;
	public void setonChangeAllViewListener(ChangeAllViewsListener listener){
		this.listener=listener;
	}
	
	public TextSurface(Context context,AbsoluteLayout abs){
		this.abs=abs;
		this.context=context;
	}
	
	/* ��ȡˮƽ������Ļ�ı��ļ�  */
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
		BroadcastReceiverHelps.sendTargetInfo(id,list.toString()); //���͹㲥
		return sb.toString();
	}
	
	/* ��ȡ��ֱ������Ļ�ı��ļ�  */
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
		BroadcastReceiverHelps.sendTargetInfo(id,list.toString()); //���͹㲥
		return sb.toString();
	}
	
	/* ����ʱ����ʾ��λ���ķ��� */
	private String format(int x) {
		String s = "" + x;
		if (s.length() == 1)
			s = "0" + s;
		return s;
	}
	
	/* ��ɫ16����ת��Ϊ10����  */
	private static int[] convert16(String colorString){
		int[] temp = new int[3] ;
		temp[0] = Integer.parseInt(colorString.substring(1, 3),16);   
		temp[1] = Integer.parseInt(colorString.substring(3, 5),16);   
		temp[2] = Integer.parseInt(colorString.substring(5, 7),16);   
		return temp;
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////
	/* ��ʾˮƽ������Ļ  */
	public void addHTextView(int w,int h,int x,int y,int fontsize,String[] fileNames,
			String backColor,int backAlpha,String foreColor,int foreAlpha){
		HScrollView hs=new HScrollView(context);
		TextView textView=hs.getTextView();
        textView.setText(getShortText(fileNames,w,x+","+y));
        textView.setTextSize(MyLayout.getScale(fontsize));
        textView.setGravity(Gravity.TOP);
        textView.setBackgroundColor(Color.parseColor(backColor));	//������ɫ
        textView.getBackground().setAlpha(backAlpha);				//����͸����
        int temp[] = convert16(foreColor); 
        textView.setTextColor(Color.argb(foreAlpha, temp[0], temp[1], temp[2])); //������ɫ��͸����
        
        textView.layout(x,y,x+w,y+h);//��Ľ���
        hs.startThread();//���������߳�
        abs.addView(hs,new MyLayout().getLayout(w,h+10,x,y-5));
	}
	
	/* ��ʾ��ֱ������Ļ  */
	public void addVTextView(int w,int h,int x,int y,int fontsize,boolean isMain,String[] fileNames,
			String backColor,int backAlpha,String foreColor,int foreAlpha){
		VScrollView sv=new VScrollView(context);
		sv.setMainPlayer(isMain);//�����Ƿ�Ϊ��������
		sv.setOnVTextViewStopListener(new VScrollView.VTextViewStopListener() {
			@Override
			public void stoped() {
				//���Ž����ص�����
				if(null != listener){
					listener.changeAllView();
				}
			}
		});
		TextView textView = sv.getTextView();
		textView.setText(getLongText(fileNames,x+","+y));
        textView.setTextSize(MyLayout.getScale(fontsize-8));
        
        textView.setBackgroundColor(Color.parseColor(backColor));	//������ɫ
        textView.getBackground().setAlpha(backAlpha);				//����͸����
        int temp[] = convert16(foreColor); 
        textView.setTextColor(Color.argb(foreAlpha, temp[0], temp[1], temp[2])); //������ɫ��͸����
        textView.layout(x,y,x+w,y+h); //��Ľ���
        sv.startThread(); //���������߳�
        abs.addView(sv,new MyLayout().getLayout(w,h,x,y));
	}

	/* ��ʾ�̶��ı���Ϣ */
	public void addTextView(int w,int h,int x,int y,int fontsize,String msg,String backColor,int backAlpha,String foreColor,int foreAlpha){
		TextView textView=new TextView(context);
        textView.setText(msg.trim());
        textView.setTextSize(MyLayout.getScale(fontsize));
        textView.setGravity(Gravity.TOP);
        textView.setBackgroundColor(Color.parseColor(backColor));	//������ɫ
        textView.getBackground().setAlpha(backAlpha);				//����͸����
        int temp[] = convert16(foreColor); 
        textView.setTextColor(Color.argb(foreAlpha, temp[0], temp[1], temp[2])); //������ɫ��͸����
        abs.addView(textView,new MyLayout().getLayout(w,h+10,x,y-5));
	}
	
	/* ��ʾʱ����Ϣ */
	public void addTimeView(int w,int h,int x,int y,int fontsize,String backColor,int backAlpha,String foreColor,int foreAlpha){
		TimeView timeView=new TimeView(context);
		timeView.setTextSize(MyLayout.getScale(fontsize));
		timeView.setGravity(Gravity.TOP);
		timeView.setBackgroundColor(Color.parseColor(backColor));	//������ɫ
		timeView.getBackground().setAlpha(backAlpha);				//����͸����
		int temp[] = convert16(foreColor); 
		timeView.setTextColor(Color.argb(foreAlpha, temp[0], temp[1], temp[2])); //������ɫ��͸����
		timeView.startThread();//ˢ��ʱ����ʾ
		abs.addView(timeView,new MyLayout().getLayout(w,h+10,x,y-5));
	}
	
	/* ��ʾ������Ϣ */
	public void addDateView(int w,int h,int x,int y,int fontsize,String backColor,int backAlpha,String foreColor,int foreAlpha){
		TextView textView=new TextView(context);
		Calendar c = Calendar.getInstance();
		textView.setText(new StringBuilder()
				.append(c.get(Calendar.YEAR))
				.append("/").append(format(c.get(Calendar.MONTH) + 1))
				.append("/").append(format(c.get(Calendar.DAY_OF_MONTH))));
		textView.setTextSize(MyLayout.getScale(fontsize));
		textView.setGravity(Gravity.TOP);
		textView.setBackgroundColor(Color.parseColor(backColor));	//������ɫ
        textView.getBackground().setAlpha(backAlpha);				//����͸����
        int temp[] = convert16(foreColor); 
        textView.setTextColor(Color.argb(foreAlpha, temp[0], temp[1], temp[2])); //������ɫ��͸����
		abs.addView(textView,new MyLayout().getLayout(w,h+10,x,y-5));
	}
	
	/* ��ʾ���ڼ�  */
	public void addWeekView(int w,int h,int x,int y,int fontsize,String backColor,int backAlpha,String foreColor,int foreAlpha){
		TextView textView=new TextView(context);
		Calendar c = Calendar.getInstance();
		String msg="";
		switch(c.get(Calendar.DAY_OF_WEEK)){
			case 1: msg="������"; break;
			case 2: msg="����һ"; break;
			case 3: msg="���ڶ�"; break;
			case 4: msg="������"; break;
			case 5: msg="������"; break;
			case 6: msg="������"; break;
			case 7: msg="������"; break;
		}
		textView.setText(msg);
//		AssetManager mgr=context.getAssets();//�õ�AssetManager
//		Typeface tf=Typeface.createFromAsset(mgr, "fonts/ttf.ttf");//����·���õ�Typeface
//		tv.setTypeface(tf);//��������
		textView.setTextSize(MyLayout.getScale(fontsize));
		textView.setGravity(Gravity.TOP);
		textView.setBackgroundColor(Color.parseColor(backColor));	//������ɫ
        textView.getBackground().setAlpha(backAlpha);				//����͸����
        int temp[] = convert16(foreColor); 
        textView.setTextColor(Color.argb(foreAlpha, temp[0], temp[1], temp[2])); //������ɫ��͸����
		abs.addView(textView,new MyLayout().getLayout(w,h+10,x,y-5));
	}
	
	/* ��ʾ������Ϣ */
	public void addWeatherView(int w,int h,int x,int y,int fontsize,String backColor,int backAlpha,String foreColor,int foreAlpha){
		WeatherInfo info = (WeatherInfo)MapCache.get("weatherInfo");
		StringBuffer sb = new StringBuffer();
		if(null != info){
			sb.append("ʪ�ȣ�"+info.getHumidity()).append("���¶ȣ�"+info.getTemperature())
				.append("�� ����"+info.getWindWay()).append("��������"+info.getWeather());
		}else{
			sb.append("����������Ϣ");
		}
		HScrollView hs=new HScrollView(context);
		TextView textView=hs.getTextView();
        textView.setText("     	       "+sb.toString());
        textView.setTextSize(MyLayout.getScale(fontsize));
        textView.setGravity(Gravity.TOP);
        textView.setBackgroundColor(Color.parseColor(backColor));	//������ɫ
        textView.getBackground().setAlpha(backAlpha);				//����͸����
        int temp[] = convert16(foreColor); 
        textView.setTextColor(Color.argb(foreAlpha, temp[0], temp[1], temp[2])); //������ɫ��͸����
        textView.layout(x,y,x+w,y+h);	//��Ľ���
        hs.startThread();				//���������߳�
        abs.addView(hs,new MyLayout().getLayout(w,h+10,x,y-5));
	}
}
