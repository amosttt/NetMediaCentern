package com.witcos.data;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.UUID;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.witcos.entity.SystemInfo;
import com.witcos.entity.task.Media;
import com.witcos.entity.task.Surface;
import com.witcos.entity.task.Task;

public class ConnectionProvider {
	
	private String id ="";
	private Integer x = 0;
	private Integer y = 0;
	private String server ="";
	private String media = "";
	
	
	public static boolean lock = true;//访问数据库锁 

	private static SQLiteDatabase db = null;
	private static DataHandle dbHelper = null;
	
    public ConnectionProvider(Context ctx) {
        if (null == dbHelper){
            dbHelper = new DataHandle(ctx,DataHandle.DATABASE);
        }
    }
    
    /** 设置参数 **/
    public void setParameter(String id,int x,int y,String server,String media){
    	  this.id = id ;
          this.x = x ;
          this.y = y;
          this.server = server ;
          this.media = media;
          SystemInfo info = this.getSysteminfo();
          if(null != info){
        	  info.setSerial(id);
        	  info.setX(x);
        	  info.setY(y);
        	  info.setServer(server);
        	  info.setMedia(media);
        	  this.updateSystemInfo(info);
          }
    }
    
    //判断表是否存在
    public boolean isTableExits(String tablename){
        boolean result=false;
        String str="select count(*) xcount from sqlite_master where table='"+tablename+"'";
        db=dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(str,null);
        if(c.getColumnIndex("xcount") != 0){
            result=true;
        }
        return result;
    }
    
    //获的一个默认的参数
    public SystemInfo getDefaultInfo(){
    /*
    	String id = "";
    	int x = 0,y = 0;
    	String server = "";   
    	String media = "";
  
    	Properties properties = new Properties(); 
    	FileInputStream s = null ;
    	try { 
    		s = new FileInputStream("/sdcard/system.properties"); 
    		properties.load(s);
    		id = properties.getProperty("id");
        	x = Integer.parseInt(properties.getProperty("x"));
        	y = Integer.parseInt(properties.getProperty("y"));
        	server = properties.getProperty("server");
        	media = properties.getProperty("media");
    	} catch (Exception e) { 
    		
    	}finally{
    		try {
    			properties.clear();
				s.close();
			} catch (IOException e) {
			}
    	}
    */
    	SystemInfo info = new SystemInfo();
    	info.setSerial(id);
    	info.setOpentime("07:00");
    	info.setClosetime("20:00");
    	info.setAutoupdate("10:00");
    	info.setMobilephone("18918337388");
    	info.setVolume(5);
    	info.setBright(10);
    	info.setRomid("");
    	info.setJpegshowtime(10);
    	info.setX(x);
    	info.setY(y);
    	info.setServer(server);
    	info.setMedia(media);
    	return info;
    }
    
    //查询系统信息
    public SystemInfo getSysteminfo(){  
    	SystemInfo info=null;
    	String str="select * from systeminfo where id = 1 ";
    	db = dbHelper.getReadableDatabase();
    	Cursor c =db.rawQuery(str,null);
    	if(null != c && c.moveToFirst()){
    		while(! c.isAfterLast()){
    			info = new SystemInfo();
    			info.setSerial(c.getString(1));
    			info.setOpentime(c.getString(2));
    			info.setClosetime(c.getString(3));
    			info.setAutoupdate(c.getString(4));
    			info.setMobilephone(c.getString(5));
    			info.setVolume(c.getInt(6));
    			info.setBright(c.getInt(7));
    			info.setRomid(c.getString(8));
    			info.setJpegshowtime(c.getInt(9));
    			info.setX(c.getInt(10));
    			info.setY(c.getInt(11));
    			info.setServer(c.getString(12));
    			info.setMedia(c.getString(13));
    			c.moveToNext();
    		}
    	}
    	c.close();
    	db.close();
		return info;
    }
    
    //插入系统信息
    public void addSystemInfo(SystemInfo info){
    	db = dbHelper.getWritableDatabase();
    	ContentValues values = new ContentValues();
    	values.put("id", 1);
    	values.put("tid", info.getSerial());
    	values.put("opentime", info.getOpentime());
    	values.put("closetime", info.getClosetime());
    	values.put("nettime", info.getAutoupdate());
    	values.put("mobile", info.getMobilephone());
    	values.put("volume", info.getVolume());
    	values.put("bright", info.getBright());
    	values.put("rom", info.getRomid());
    	values.put("jpegshowtime", info.getJpegshowtime());
    	values.put("x", info.getX());
    	values.put("y", info.getY());
    	values.put("urlpath", info.getServer());
    	values.put("urlmediapath", info.getMedia());    	
    	db.insert("systeminfo", null, values);
    	db.close();
    }
    
    //更新系统信息
    public void updateSystemInfo(SystemInfo info){
    	db = dbHelper.getWritableDatabase();
    	ContentValues values = new ContentValues();
    	values.put("opentime", info.getOpentime());
    	values.put("closetime", info.getClosetime());
    	values.put("nettime", info.getAutoupdate());
    	values.put("mobile", info.getMobilephone());
    	values.put("volume", info.getVolume());
    	values.put("bright", info.getBright());
    	values.put("rom", info.getRomid());
    	values.put("jpegshowtime", info.getJpegshowtime());
    	values.put("urlpath", info.getServer());
    	values.put("urlmediapath", info.getMedia());
    	db.update("systeminfo", values, "id=?", new String[]{"1"});
    	db.close();
    }

    //////////////////////////////////////////////////////////
   
    //添加任务
    public void addTask(Task task){
    	db = dbHelper.getWritableDatabase();
    	ContentValues values = new ContentValues();
    	values.put("id", task.getTid());
    	values.put("indexs", task.getIndex());
    	values.put("sid", task.getSid());    	
    	values.put("width", task.getWidth());
    	values.put("height",task.getHeight());    	
    	values.put("startdate",task.getStartDate());
    	values.put("stopdate",task.getStopDate());
    	values.put("tasktype",task.getTaskType());
    	values.put("operate", task.getOperate());
    	values.put("starttime",task.getStartTime());
    	values.put("playsize",task.getPlaySize());
    	values.put("playlevel",task.getPlaylevel());
    	values.put("readtext",task.getReadtext());
    	db.insert("task", null, values);
    	db.close();
    }
    
    //查询所有任务(按照任务排列顺序进行查询)
    public List<Task> findTaskAll(){
    	Task task=null;
    	List<Task> list = new ArrayList<Task>();
    	String sql="select * from task order by indexs ASC";
    	db = dbHelper.getReadableDatabase();
    	Cursor c =db.rawQuery(sql,null);
    	if(null != c && c.moveToFirst()){
    		while(! c.isAfterLast()){
    			task=new Task();
    			task.setTid(c.getString(0));
    			task.setIndex(c.getInt(1));
    			task.setSid(c.getString(2));	
    			task.setWidth(c.getInt(3));
    			task.setHeight(c.getInt(4));
    			task.setStartDate(c.getString(5));
    			task.setStopDate(c.getString(6));
    			task.setTaskType(c.getString(7));
    			task.setOperate(c.getString(8));
    			task.setStartTime(c.getString(9));
    			task.setPlaySize(c.getInt(10));
    			task.setPlaylevel(c.getString(11));
    			task.setReadtext(c.getString(12));   
    			list.add(task);
    			c.moveToNext();
    		}
    	}
    	c.close();
    	db.close();
    	return list;
    }
	
    //查询所有循环任务(按照任务排列顺序进行查询)
    public List<Task> findTaskAllLoop(){
    	Task task=null;
    	List<Task> list = new ArrayList<Task>();
    	String sql="select * from task where tasktype = 'looptask' order by indexs ASC";
    	db = dbHelper.getReadableDatabase();
    	Cursor c =db.rawQuery(sql,null);
    	if(null != c && c.moveToFirst()){
    		while(! c.isAfterLast()){
    			task=new Task();
    			task.setTid(c.getString(0));
    			task.setIndex(c.getInt(1));
    			task.setSid(c.getString(2));	
    			task.setWidth(c.getInt(3));
    			task.setHeight(c.getInt(4));
    			task.setStartDate(c.getString(5));
    			task.setStopDate(c.getString(6));
    			task.setTaskType(c.getString(7));
    			task.setOperate(c.getString(8));
    			task.setStartTime(c.getString(9));
    			task.setPlaySize(c.getInt(10));
    			task.setPlaylevel(c.getString(11));
    			task.setReadtext(c.getString(12));  
    			list.add(task);
    			c.moveToNext();
    		}
    	}
    	c.close();
    	db.close();
    	return list;
    }
    
    //查询所有定时任务(按照任务排列顺序进行查询)
    public List<Task> findTaskAllTime(){
    	Task task=null;
    	List<Task> list = new ArrayList<Task>();
    	String sql="select * from task where tasktype = 'timetask' order by indexs ASC";
    	db = dbHelper.getReadableDatabase();
    	Cursor c = db.rawQuery(sql,null);
    	if(null != c && c.moveToFirst()){
    		while(! c.isAfterLast()){
    			task=new Task();
    			task.setTid(c.getString(0));
    			task.setIndex(c.getInt(1));
    			task.setSid(c.getString(2));	
    			task.setWidth(c.getInt(3));
    			task.setHeight(c.getInt(4));
    			task.setStartDate(c.getString(5));
    			task.setStopDate(c.getString(6));
    			task.setTaskType(c.getString(7));
    			task.setOperate(c.getString(8));
    			task.setStartTime(c.getString(9));
    			task.setPlaySize(c.getInt(10));
    			task.setPlaylevel(c.getString(11));
    			task.setReadtext(c.getString(12));    
    			list.add(task);
    			c.moveToNext();
    		}
    	}
    	c.close();
    	db.close();
    	return list;
    }
    
    //按照任务编号查询任务
    public List<Task> findTaskByTid(String tid){
    	List<Task> list = new ArrayList<Task>();
    	Task task=null;
    	String sql="select * from task where id = ? ";
    	db = dbHelper.getReadableDatabase();
    	Cursor c =db.rawQuery(sql, new String[]{tid});
    	if(null != c && c.moveToFirst()){
    		while(! c.isAfterLast()){
    			task = new Task();
    			task.setTid(c.getString(0));
    			task.setIndex(c.getInt(1));
    			task.setSid(c.getString(2));	
    			task.setWidth(c.getInt(3));
    			task.setHeight(c.getInt(4));
    			task.setStartDate(c.getString(5));
    			task.setStopDate(c.getString(6));
    			task.setTaskType(c.getString(7));
    			task.setOperate(c.getString(8));
    			task.setStartTime(c.getString(9));
    			task.setPlaySize(c.getInt(10));
    			task.setPlaylevel(c.getString(11));
    			task.setReadtext(c.getString(12));  
    			list.add(task);
    			c.moveToNext();
    		}
    	}
    	c.close();
    	db.close();
    	return list;
    }
    
    /////////////////////////////////////////////////////////
    
    //添加媒体
    public void addMedia(Media media){
    	db = dbHelper.getWritableDatabase();
    	ContentValues values = new ContentValues();
    	values.put("id", UUID.randomUUID().toString());
    	values.put("tid", media.getTid());
    	values.put("bid", media.getBid());
    	values.put("mid", media.getMedias());
    	db.insert("media", null, values);   
    	db.close();
    }
    	
    //查询媒体（按照任务编号进行查询）
    public List<Media> findMediaByTid(String tid){
    	Media media=null;
    	List<Media> list=new ArrayList<Media>();
    	String sql="select * from media where tid = ? ";
    	db = dbHelper.getReadableDatabase();
    	Cursor c =db.rawQuery(sql, new String[]{tid});
    	if(null != c && c.moveToFirst()){
    		while(! c.isAfterLast()){
    			media=new Media();
    			media.setMid(c.getString(0));
    			media.setTid(c.getString(1));
    			media.setBid(c.getString(2));
    			media.setMedias(c.getString(3));
    			list.add(media);
    			c.moveToNext();
    		}
    	}
    	c.close();
    	db.close();
    	return list;
    }
    
    /////////////////////////////////////////////////////////
    
    //添加版块
    public void addSurface(Surface surface){
    	db = dbHelper.getWritableDatabase();
    	ContentValues values = new ContentValues();
    	values.put("id", surface.getId());
    	values.put("sid", surface.getSid());
    	values.put("type", surface.getType());
    	values.put("x", surface.getX());
    	values.put("y", surface.getY());
    	values.put("width", surface.getWidth());
    	values.put("height", surface.getHeight());
    	values.put("indexs", surface.getIndex());
    	values.put("ismain", surface.getIsMain());
    	values.put("txt", surface.getTxt());
    	values.put("alpha", surface.getAlpha());
    	values.put("font", surface.getFont());
    	values.put("fontsize", surface.getFontSize());
    	values.put("backcolor", surface.getBackColor());
    	values.put("backalpha", surface.getBackAlpha());
    	values.put("forecolor", surface.getForeColor());
    	values.put("forealpha", surface.getForeAlpha());
    	values.put("backpic", surface.getBackPic());
    	db.insert("surface", null, values);  
    	db.close();
    }
    
    //查询板块（按照样式编号，根据显示的图层关系 排序）
    public List<Surface> findSurfaceBySid(String sid){
    	Surface surface = null ;
    	List<Surface> list = new ArrayList<Surface>();
    	String sql = "select * from surface where sid = ? order by indexs ASC";
    	db=dbHelper.getReadableDatabase();
    	Cursor c =db.rawQuery(sql, new String[]{sid});
    	if(null != c && c.moveToFirst()){
    		while(! c.isAfterLast()){
    			surface=new Surface();
    			surface.setId(c.getString(0));
    			surface.setSid(c.getString(1));
    			surface.setType(c.getString(2));
    			surface.setX(Integer.valueOf(c.getString(3)));
    			surface.setY(Integer.valueOf(c.getString(4)));
    			surface.setWidth(Integer.valueOf(c.getString(5)));
    			surface.setHeight(Integer.valueOf(c.getString(6)));
    			surface.setIndex(Integer.valueOf(c.getString(7)));
    			surface.setIsMain(Integer.valueOf(c.getString(8)));
    			surface.setTxt(c.getString(9));
    			surface.setAlpha(c.getString(10));
    			surface.setFont(c.getString(11));
    			surface.setFontSize(c.getString(12));
    			surface.setBackColor(c.getString(13));
    			surface.setBackAlpha(c.getString(14));
    			surface.setForeColor(c.getString(15));
    			surface.setForeAlpha(c.getString(16));
    			surface.setBackPic(c.getString(17));
    			list.add(surface);
    			c.moveToNext();
    		}
    	}
    	c.close();
    	db.close();
    	return list;
    }
    
    ////////////////////////////////////////////////////////
    
    //清空数据库
    public void deleteTable(){
    	db = dbHelper.getWritableDatabase();
    	db.execSQL("delete from task", new Object[]{});
    	db.execSQL("delete from media", new Object[]{});
    	db.execSQL("delete from surface", new Object[]{});
    	db.close();
    } 
    
}
