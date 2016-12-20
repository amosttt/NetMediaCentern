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
	//�ն˳���汾
	public static String COPYRIGHT = "1.0.1" ;
	//�ļ�����λ��
	public static String FILEPATH = "/sdcard/witcos/";
	//������������
	private static String CONNECT  = "/getConnection?id=";	
	//����ͻ�������
	private static String CONFIG   = "/getConfig?id=";	
	//����������Ϣ
	private static String WEATHER  = "/getWeather?id=";	
	//���󲥷��б�
	private static String PLAYLIST = "/getPlayList?id=";
	//��ƽ̨�ύ���ؽ���
	private static String SUBMIT   = "/submit?id=";	
	//�������������ɺ��ύ���
	private static String FINISH   = "/sendFinish?id=";	
	
	/** �ж��ļ����Ƿ����,����������򴴽��ļ��� **/
	public static void isExist(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdir();
		}
	}
	
	/** �ж�һ���ļ��Ƿ����,���������ļ�ʱ���ظ����أ����Ч�� **/
	public static boolean fileIsExists(String path){
		 File f=new File(path);
		 return f.exists();
	}
	
	//���ݲ�ͬ���󣬷��ض�Ӧ��URL
	private static String converturl(int temp1,int temp2){
		SystemInfo info = (SystemInfo)MapCache.get("systemInfo");
		if(null == info){
			return "";
		}		
		String url = "";
		if(temp1 == 1){
			//����ƽ̨������
			switch(temp2){
				case 1: //������������
					url="http://"+info.getServer() + CONNECT + info.getSerial() + "&cp=" + COPYRIGHT;
					break;
				case 2: //����������Ϣ
					url="http://"+info.getServer() + CONFIG + info.getSerial();
					break;
				case 3: //��������Ԥ��
					url="http://"+info.getServer() + WEATHER + info.getSerial();
					break;
				case 4: //���󲥷��б�
					url="http://"+info.getServer() + PLAYLIST + info.getSerial();
					break;
				case 5: //�ύ���ؽ���
					url="http://"+info.getServer() + SUBMIT + info.getSerial() ;
					break;
				case 6: //�ύ�������
					url="http://"+info.getServer() + FINISH + info.getSerial();
					break;
				default:
					break;
			}
		}else if(temp1 == 2){
			//����������Դ
			switch (temp2) {
				case 1: //����ý�������������ز���ý��
					url = "http://"+info.getMedia()+"/upload/";
					break;
				case 2://����ý����������������ʽ�ز�
					url = "http://"+info.getMedia()+"/stuff/";
					break;
				case 3://����ý���������������ն�������
					url = "http://"+info.getMedia()+"/rom/";
					break;
				default:
					break;
			}
		}
		return url ;
	}
	
	/** ����ý���ļ�   type=1 Ϊ�����ļ�   type=2 Ϊ��ʽ�ز�  type=3������**/
	public synchronized static void downFile(String fileName,String size ,int type) throws Exception{
		String target = FILEPATH + fileName ;  //���ص�Ŀ��·��
		if(fileIsExists(target)){
			return ;
		}
		
		String url="";
		if(type == 1){
			url = converturl(2,1) + fileName;//����ý��
		}else if(type == 2){
			url = converturl(2,2) + fileName;//������ʽ�ز�
		}else if(type == 3){
			url = converturl(2,3) + fileName;//����������
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
				submitNode(targetbakLength,mediaSize,fileName,type);//�ύ����				
			}
			temp = true;
			submitNode(targetbakLength,mediaSize,fileName,type);//�ύ����
			new File(targetbak).renameTo(new File(target)); //ȥ��bak,�������ļ�
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
	
	/** �ύ���ؽ��� **/
	private static void submitNode(long downsize,long mediaSize,String file,int type)throws Exception{
		//http://*:*/Telecom/submit?id=2011010001&file=bbfa-80e5e899e72f.avi&node=70&type=1
		//id�ն˱�� ;file���ص��ļ�;node%���� ;type(1 ����ý�壬2��ʽý�� 3 ������);
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
	
	/** ����ý��ȫ��������ϣ�ȫ��������� **/
	public static void submitFinish()throws Exception{
		String url = converturl(1,6) ; 
		connectserver(url);
	}
	
	/** ���ʷ��������� **/
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
	
	/** �������Ŀ¼�����ļ� **/
	public static void clearMedia(List<String> names){
		File root = new File(FILEPATH);
		 for(File file:root.listFiles()){     
			 if(!names.contains(file.getName())){
				 file.delete();
			 }
		 }
	}
	
////////////////////////////////////////////////////////////////////////
	
	/** ��ȡ�ն�״̬ **/
	public static ConnectHeart getState() throws Exception{
		ConnectHeart connectHeart = null; //������Ϣ��װ
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
			throw new Exception("��ȡϵͳ��Ϣʧ��"); 
		}finally{
			try {
				inputStream.close();
				inputStream = null ;
			} catch (IOException e) {
			}
		}
		return connectHeart;
	}
	
	/** ��ȡϵͳ������Ϣ **/
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
			throw new Exception("��ȡϵͳ��Ϣʧ��"); 
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
	
	/** ��ȡ����Ԥ����Ϣ **/
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
			throw new Exception("��ȡϵͳ��Ϣʧ��"); 
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

	/**��ȡ�����б�**/
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
			throw new Exception("��ȡ�����б�ʧ��"); 
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

	/** ��ȡý���ļ���С **/
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
