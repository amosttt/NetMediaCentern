package com.witcos.surface;

import com.witcos.ChangeAllViewsListener;
import com.witcos.surface.extend.ImageView;
import com.witcos.tool.MyLayout;

import android.content.Context;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsoluteLayout;

public class ImageSurface {
	
	private Context context;
	private AbsoluteLayout abs;
	
	private ChangeAllViewsListener listener;
	
	public void setonChangeAllViewListener(ChangeAllViewsListener listener){
		this.listener=listener;
	}
	
	public ImageSurface(Context context,AbsoluteLayout abs){
		this.abs=abs;
		this.context=context;
	}
	
	public void addImageView(int w,int h,int x,int y, int alpha, boolean isMain,String[] fileNames,int sec){
        ImageView image=new ImageView(context,fileNames,x+","+y);
        image.setMainPlayer(isMain);
        image.setOnVTextViewStopListener(new ImageView.VTextViewStopListener() {
			@Override
			public void stoped() {
				if(null != listener){
					listener.changeAllView(); 
				}
			}
		});
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        image.showImage(sec);
        image.setAlpha(alpha);//透明度
        abs.addView(image, new MyLayout().getLayout(w,h,x,y)); 
	}
	
	//暂无任务的背景图片
	public void addImageView(int w,int h,int x,int y,int id){
        android.widget.ImageView image=new android.widget.ImageView(this.context);
        image.setImageResource(id);
		image.setScaleType(ImageView.ScaleType.FIT_XY);
		abs.addView(image, new MyLayout().getLayout(w,h,x,y));
	}
	
}
