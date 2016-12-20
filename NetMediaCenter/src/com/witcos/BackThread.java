package com.witcos;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.witcos.cache.MapCache;
import com.witcos.data.ConnectionProvider;
import com.witcos.entity.ConnectHeart;
import com.witcos.entity.SystemInfo;
import com.witcos.entity.task.Media;
import com.witcos.entity.task.Surface;
import com.witcos.entity.task.Task;
import com.witcos.entity.task.TaskAndSurface;
import com.witcos.tool.DownloadFile;
import com.witcos.tool.MyUtil;

public class BackThread extends Thread{
	
	int currentTime = 0;
	private Context context = null ;   
	private ConnectionProvider pro = null;
	
	//瀹氭椂浠诲姟闆嗗悎
	private List<Task> tasklistTime = new ArrayList<Task>();
	//濯掍綋鍚嶇О闆嗗悎
	private List<String> mediaName = new ArrayList<String>();
	
	public BackThread(Context context,ConnectionProvider pro){
		this.context = context ; 
		this.pro = pro;  
	}
	
	@Override
	public void run() {
		initSystem();
		while(true){
			try {
				Thread.sleep(1000*20);
				heartBeat();
				Thread.sleep(1000*20);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/** 鍒濆鍖栫郴缁�**/
	public synchronized void initSystem(){
		//鍒ゆ柇濯掍綋鏂囦欢璺緞鏄惁瀛樺湪
		DownloadFile.isExist(DownloadFile.FILEPATH); 
		//鏌ヨ鏁版嵁搴�systemInfo 閲岄潰鏄惁鏈夋暟鎹紝濡傛灉鏈夛紝灏哠ystemInfo鏀剧疆缂撳瓨涓�
		SystemInfo systemInfo = pro.getSysteminfo();//鏌ヨ鏁版嵁搴�
		if(null == systemInfo){
			pro.addSystemInfo(pro.getDefaultInfo());//鍔犺浇榛樿鐨勭郴缁熸暟鎹�
			systemInfo=pro.getSysteminfo();
		}
		MapCache.put("systemInfo", systemInfo);//灏嗙郴缁熶俊鎭斁鍒扮紦瀛橀噷闈㈠幓			
		MyUtil.updateAudio(context, systemInfo.getVolume()); //璁剧疆闊抽噺
		getSystemConfig();//鍚屾涓�鏈嶅姟鍣�閰嶇疆淇℃伅
		tasklistTime = pro.findTaskAllTime();//鑾峰彇瀹氭椂浠诲姟闆嗗悎
	}	
	
	/** 璇锋眰澶╂皵棰勬姤 **/	
	public synchronized void getWeather() {
		try {
			MapCache.put("weatherInfo", DownloadFile.getWeatherInfo());
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	/** 鏇存柊瀹㈡埛鏈洪厤缃枃浠�**/
	public synchronized void getSystemConfig(){
		try {
			SystemInfo systemInfo = DownloadFile.getSystemInfo();//璇锋眰鏈嶅姟鍣ㄨ幏鍙栭厤缃俊鎭�
			if(null != systemInfo){
				pro.updateSystemInfo(systemInfo);
				MyUtil.updateAudio(context, systemInfo.getVolume()); //璁剧疆闊抽噺
				MyUtil.updatetime(context, systemInfo.getSystime()); //璁剧疆鏃堕棿
				systemInfo = pro.getSysteminfo();   //鏌ヨ鏁版嵁搴�
				MapCache.put("systemInfo", systemInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}			
	}
	
	/** 蹇冭烦涓诲嚱鏁�**/
	private synchronized void heartBeat()throws Exception{
		//鑾峰緱缂撳瓨閲岀粓绔紑鍏虫満鏃堕棿鍙婅嚜鍔ㄦ洿鏂版椂闂�
		SystemInfo info = (SystemInfo)MapCache.get("systemInfo");
		ConnectHeart ch = null;
		try {
			ch = DownloadFile.getState(); //鍚戞湇鍔″櫒璇锋眰蹇冭烦杩炴帴
		} catch (Exception e) {
			throw e;
		}
		
		if(null != ch){
			if(ch.getState() == 1){
				/////////////////////////鎵嬪姩鏇存柊
				if(ch.getUpdate() == 1){
					try {
						getSystemConfig();      
						downloadFileAndSaveData();
					} catch (Exception e) {
						throw e;
					}
				}
				/////////////////////////鑷姩鏇存柊
				int min = MyUtil.getMinute();
				if( min != currentTime){
					currentTime = min;
					if(MyUtil.getMinute(info.getAutoupdate()) == min){
						try { 
							getSystemConfig(); 
							downloadFileAndSaveData();
						} catch (Exception e) {
							throw e;
						}
					}
					if(tasklistTime.size() > 0){
						//瀹氭椂浠诲姟
						for(Task t : tasklistTime){
							if((min + 2) == MyUtil.getMinute(t.getStartTime())){
								BroadcastReceiverHelps.sendTimeTaskInfo(t);
							}
						}
					}					
				}
				
			}
		} 
	}
	
	//涓嬭浇濯掍綋鎻掑叆鏁版嵁搴�
	private synchronized void downloadFileAndSaveData()throws Exception{ 
		mediaName.clear(); //娓呯┖鐢ㄤ簬淇濆瓨濯掍綋鐨勯泦鍚�
		TaskAndSurface ts = null;
		try {
			ts = DownloadFile.getPlayListInfo();
		} catch (Exception e1) {
			throw e1;
		}
		if(ts.getIsEmpty() == 0){
			for(Task t:ts.getTaskList()){
				for(Media m : t.getMediaList()){
					for(String str:m.getMedias().split(";")){
						try {
							Log.e("///////涓嬭浇鎾斁濯掍綋", str);
							DownloadFile.downFile(str.split(",")[0],str.split(",")[1], 1);
							mediaName.add(str.split(",")[0]);
						} catch (Exception e) {
							e.printStackTrace();
							throw e;
						}
					}
				}
			}
			for(Surface s: ts.getSurfaceList()){
				if(!s.getBackPic().equals("")){
					try{
						Log.e("////////涓嬭浇鏍峰紡绱犳潗", s.getBackPic());
						DownloadFile.downFile(s.getBackPic().split(",")[0],s.getBackPic().split(",")[1],2);
						mediaName.add(s.getBackPic().split(",")[0]);
					}catch(Exception e){
						e.printStackTrace();
						throw e;
					}
				}
			}
		}
		
		//鎻掑叆鏁版嵁搴�
		if(ConnectionProvider.lock){
			ConnectionProvider.lock = false;//鍔犻攣
			if(ts.getIsEmpty() == 0){
				try {
					pro.deleteTable(); 
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				for(Task t: ts.getTaskList()){
					pro.addTask(t);
					for(Media m : t.getMediaList()){
						StringBuffer sb = new StringBuffer();
						for(String str : m.getMedias().split(";")){
//							sb.append(str.split(",")[0] + ";");
							sb.append(str.split(",")[0] + "," + str.split(",")[2] + ";");
						}
						sb.deleteCharAt(sb.length()-1);
						m.setMedias(sb.toString());
						pro.addMedia(m);
					}
				}
				for(Surface s:ts.getSurfaceList()){
					if(!s.getBackPic().equals("")){
						s.setBackPic(s.getBackPic().split(",")[0]+",null");
					}
					pro.addSurface(s);
				}
			}
			ConnectionProvider.lock = true;//瑙ｉ攣
			
			DownloadFile.submitFinish();//鎻愪氦涓嬭浇宸茬粡瀹屾垚
			int countLoopSize = pro.findTaskAllLoop().size();
			BroadcastReceiverHelps.sendLoopTaskSize(countLoopSize);//鍙戦�骞挎挱 鍛婄煡寰幆浠诲姟 鏁伴噺
			DownloadFile.clearMedia(mediaName);//鍒犻櫎鏃犻渶鐨勫獟浣撹祫婧�
			tasklistTime = pro.findTaskAllTime(); //鑾峰彇鏇存柊鍚庣殑浠诲姟闆嗗悎
		}
	}
	
}


