package com.witcos.tool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.RandomAccess;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.text.format.Time;
import android.util.Log;
import android.util.Xml;

import com.witcos.cache.MapCache;
import com.witcos.entity.ConnectHeart;
import com.witcos.entity.SystemInfo;
import com.witcos.entity.WeatherInfo;
import com.witcos.entity.task.TaskAndSurface;
import com.witcos.xml.PlayListInfoXML;
import com.witcos.xml.SystemInfoXML;
import com.witcos.xml.WeatherInfoXML;

public class DownloadFile {	
	//终端程序版本
	public static String COPYRIGHT = "1.0.1" ;
	//文件保存位置
	public static String FILEPATH = "/sdcard/witcos/";
	//请求心跳连接
	private static String CONNECT  = "/getConnection?id=";	
	//请求客户机配置
	private static String CONFIG   = "/getConfig?id=";	
	//请求天气信息
	private static String WEATHER  = "/getWeather?id=";	
	//请求播放列表
	private static String PLAYLIST = "/getPlayList?id=";
	//向平台提交下载进度
	private static String SUBMIT   = "/submit?id=";	
	//所有任务更新完成后提交完成
	private static String FINISH   = "/sendFinish?id=";	
	
	/** 判断文件夹是否存在,如果不存在则创建文件夹 **/
	public static void isExist(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdir();
		}
	}
	
	/** 判断一个文件是否存在,用于下载文件时不重复下载，提高效率 **/
	public static boolean fileIsExists(String path){
		 File f=new File(path);
		 return f.exists();
	}
	
	//根据不同请求，返回对应的URL
	private static String converturl(int temp1,int temp2){
		SystemInfo info = (SystemInfo)MapCache.get("systemInfo");
		if(null == info){
			return "";
		}		
		String url = "";
		if(temp1 == 1){
			//请求平台服务器
			switch(temp2){
				case 1: //请求心跳连接
					url="http://"+info.getServer() + CONNECT + info.getSerial() + "&cp=" + COPYRIGHT;
					break;
				case 2: //请求配置信息
					url="http://"+info.getServer() + CONFIG + info.getSerial();
					break;
				case 3: //请求天气预报
					url="http://"+info.getServer() + WEATHER + info.getSerial();
					break;
				case 4: //请求播放列表
					url="http://"+info.getServer() + PLAYLIST + info.getSerial();
					break;
				case 5: //提交下载进度
					url="http://"+info.getServer() + SUBMIT + info.getSerial() ;
					break;
				case 6: //提交更新完成
					url="http://"+info.getServer() + FINISH + info.getSerial();
					break;
				default:
					break;
			}
		}else if(temp1 == 2){
			//请求下载资源
			switch (temp2) {
				case 1: //请求媒体库服务器，下载播放媒体
					url = "http://"+info.getMedia()+"/upload/";
					break;
				case 2://请求媒体库服务器，下载样式素材
					url = "http://"+info.getMedia()+"/stuff/";
					break;
				case 3://请求媒体库服务器，下载终端升级包
					url = "http://"+info.getMedia()+"/rom/";
					break;
				default:
					break;
			}
		}
		return url ;
	}
	
	/** 下载媒体文件   type=1 为播放文件   type=2 为样式素材  type=3升级包**/
	public synchronized static void downFile(String fileName,String size ,int type) throws Exception{
		String target = FILEPATH + fileName ;  //下载的目标路径
		if(fileIsExists(target)){
			return ;
		}
		
		String url="";
		if(type == 1){
			url = converturl(2,1) + fileName;//下载媒体
		}else if(type == 2){
			url = converturl(2,2) + fileName;//下载样式素材
		}else if(type == 3){
			url = converturl(2,3) + fileName;//下载升级包
		}
		
		String targetbak = target + ".bak";
		long targetbakLength = 0;
		if(fileIsExists(targetbak)){
			targetbakLength = new File(targetbak).length();
		}
		
		InputStream inputStream = null;
		RandomAccessFile oSavedFile = null;
		try {
			URL myURL = new URL(url);
			URLConnection conn = myURL.openConnection();
			conn.connect();	
			inputStream = conn.getInputStream();
			for(int i = 0 ; i< targetbakLength ; i++ ){
				inputStream.read();
			}
			oSavedFile = new RandomAccessFile(targetbak,"rw");  
			oSavedFile.seek(targetbakLength);
			byte buf[] = new byte[1024*1000];
			int len;
			long mediaSize = Long.parseLong(size);
			while ((len = inputStream.read(buf)) != -1) {
				oSavedFile.write(buf, 0, len); 
				targetbakLength +=len;
				submitNode(targetbakLength,mediaSize,fileName,type);//提交进度				
			}
			temp = true;
			submitNode(targetbakLength,mediaSize,fileName,type);//提交进度
			new File(targetbak).renameTo(new File(target)); //去除bak,重命名文件
		} catch (Exception e) {
			throw e;
		}finally{
			try {
				oSavedFile.close();
				inputStream.close();
				oSavedFile = null ;
				inputStream = null ;
			} catch (IOException e) {
			}
		}
	}
	
	private static boolean temp = true;
	
	/** 提交下载进度 **/
	private static void submitNode(long downsize,long mediaSize,String file,int type)throws Exception{
		//http://*:*/Telecom/submit?id=2011010001&file=bbfa-80e5e899e72f.avi&node=70&type=1
		//id终端编号 ;file下载的文件;node%进度 ;type(1 播放媒体，2样式媒体 3 升级包);
		if(temp){
			temp = false ;
			double tempresult = downsize * 1.0 / mediaSize *1.0 * 100;
		    int node = new Double(tempresult).intValue();
		    if(node > 100){
		    	node = 100 ;
		    }		
		    final String url = converturl(1,5)+"&file="+file+"&node="+node+"&type="+type;		    
		    new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					try {
						Thread.sleep(500);
						connectserver(url);
					} catch (Exception e) {
					}
				}
			}, 0);
		}
	}
	
	/** 所有媒体全部下载完毕，全部更新完毕 **/
	public static void submitFinish()throws Exception{
		String url = converturl(1,6) ; 
		connectserver(url);
	}
	
	/** 访问服务器方法 **/
	public static void connectserver(String url)throws Exception{
		InputStream inputStream = null;
		try {
			URL myURL = new URL(url);
			URLConnection conn = myURL.openConnection();
			conn.connect();
			inputStream = conn.getInputStream();
		} catch (Exception e) {
			throw e; 
		}finally{
			temp = true ;
			try {
				inputStream.close();
				inputStream = null ;
			} catch (IOException e) {
			}
		}
	}
	
	/** 清除下载目录多余文件 **/
	public static void clearMedia(List<String> names){
		File root = new File(FILEPATH);
		 for(File file:root.listFiles()){     
			 if(!names.contains(file.getName())){
				 file.delete();
			 }
		 }
	}
	
////////////////////////////////////////////////////////////////////////
	
	/** 获取终端状态 **/
	public static ConnectHeart getState() throws Exception{
		ConnectHeart connectHeart = null; //心跳信息封装
		InputStream inputStream = null;
		try {
			URL myURL = new URL(converturl(1,1));
			URLConnection conn = myURL.openConnection(); 
			conn.connect(); 
			inputStream = conn.getInputStream();
			byte[] buffer = new byte[3];
			inputStream.read(buffer, 0, 3);
			String netInfo= new String(buffer);			
			connectHeart = new ConnectHeart();
			connectHeart.setState(Integer.parseInt(netInfo.substring(0, 1)));
			connectHeart.setLedOpen(Integer.parseInt(netInfo.substring(1, 2)));
			connectHeart.setUpdate(Integer.parseInt(netInfo.substring(2, 3)));
		} catch (Exception e) {
			throw new Exception("获取系统信息失败"); 
		}finally{
			try {
				inputStream.close();
				inputStream = null ;
			} catch (IOException e) {
			}
		}
		return connectHeart;
	}
	
	/** 获取系统配置信息 **/
	public static SystemInfo getSystemInfo() throws Exception{
		SystemInfo info=null;
		InputStream inputStream=null;
		SystemInfoXML xml = null ;
		try {
			URL myURL = new URL(converturl(1,2));
			URLConnection conn = myURL.openConnection();
			conn.connect();
			inputStream = conn.getInputStream();
			xml=new SystemInfoXML();
			Xml.parse(inputStream, Xml.Encoding.UTF_8,xml);
			info=xml.getSystemInfo();
		} catch (Exception e) {
			throw new Exception("获取系统信息失败"); 
		}finally{
			try {
				inputStream.close();
				inputStream = null ;
				xml = null ;
			} catch (IOException e) {
			}
		}
		return info;
	}
	
	/** 获取天气预报信息 **/
	public static WeatherInfo getWeatherInfo() throws Exception{
		WeatherInfo info=null;
		InputStream inputStream=null;
		WeatherInfoXML xml = null ;
		try {
			URL myURL = new URL(converturl(1,3));
			URLConnection conn = myURL.openConnection();
			conn.connect();
			inputStream = conn.getInputStream();
			xml=new WeatherInfoXML();
			Xml.parse(inputStream, Xml.Encoding.UTF_8,xml);
			info=xml.getWeatherInfo();
		} catch (Exception e) {
			throw new Exception("获取系统信息失败"); 
		}finally{
			try {
				inputStream.close();
				inputStream = null ;
				xml = null ;
			} catch (IOException e) {
			}
		}
		return info;
	}

	/**获取播放列表**/
	public static TaskAndSurface getPlayListInfo() throws Exception{
		TaskAndSurface ts=new TaskAndSurface();
		InputStream inputStream=null;
		PlayListInfoXML xml = null ;
		try {
			URL myURL = new URL(converturl(1,4));
			URLConnection conn = myURL.openConnection();
			conn.connect();
			inputStream = conn.getInputStream();
			xml=new PlayListInfoXML();
			Xml.parse(inputStream, Xml.Encoding.UTF_8,xml);
			ts=xml.getTs();
		} catch (Exception e) {
			throw new Exception("获取播放列表失败"); 
		} finally{
			try {
				inputStream.close();
				inputStream = null;
				xml = null ;
			} catch (IOException e) {
			}
		}
		return ts;
	}

	/** 获取媒体文件大小 **/
	public static Long getNetFileSize(String url) {
        Long count = -1L;
        final HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setIntParameter("http.socket.timeout", 5000);
        final HttpGet httpGet = new HttpGet(url);
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            final int code = response.getStatusLine().getStatusCode();
            final HttpEntity entity = response.getEntity();
            if (entity != null && code == 200) {
                count = entity.getContentLength();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return count;
    }
	
}
