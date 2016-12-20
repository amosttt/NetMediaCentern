package com.witcos.entity.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ´ËÀà¿É×÷Îª»ñÈ¡Æ½Ì¨Êý¾Ý·â×°¶ÔÏó£¬Ò²¿ÉÒÔÌá¹©²¥·ÅÁÐ±í²éÑ¯±¾µØÊý¾Ý·â×°¶ÔÏó
 * @author JieHua
 *
 */
public class Task {
	
	private String tid;  	//ÈÎÎñ±àºÅ
	private String sid;		//ÑùÊ½±àºÅ
	private Integer index;	//²¥·ÅË³Ðò
	
	private Integer height; //´ËÈÎÎñ½çÃæµÄ¸ß¶È
	private Integer width;  //ÈÎÎñ½çÃæµÄ¿í¶È
	
	private String  startDate;
	private String  stopDate;
	private String  taskType;
	private String  operate;
	private String  startTime;
	private Integer playSize;
	private String  playlevel;
	private String  readtext;
	
	private List<Media> mediaList = new ArrayList<Media>();					//Ã½Ìå¼¯ºÏ
	private Map<String, String> mediaMap = new HashMap<String, String>();	//´æ·ÅÃ½ÌåµÄ¼¯ºÏ£¨key °æ¿éID; value Ã½Ìå¼¯ºÏ·ÖºÅ·Ö¸ô£©
	private List<Surface> surfaceList = new ArrayList<Surface>();			//ÑùÊ½°æ¿é¼¯ºÏ
	
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getStopDate() {
		return stopDate;
	}
	public void setStopDate(String stopDate) {
		this.stopDate = stopDate;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public String getOperate() {
		return operate;
	}
	public void setOperate(String operate) {
		this.operate = operate;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public Integer getPlaySize() {
		return playSize;
	}
	public void setPlaySize(Integer playSize) {
		this.playSize = playSize;
	}
	public String getPlaylevel() {
		return playlevel;
	}
	public void setPlaylevel(String playlevel) {
		this.playlevel = playlevel;
	}
	public String getReadtext() {
		return readtext;
	}
	public void setReadtext(String readtext) {
		this.readtext = readtext;
	}
	public List<Media> getMediaList() {
		return mediaList;
	}
	public void setMediaList(List<Media> mediaList) {
		this.mediaList = mediaList;
	}
	public Map<String, String> getMediaMap() {
		return mediaMap;
	}
	public void setMediaMap(Map<String, String> mediaMap) {
		this.mediaMap = mediaMap;
	}
	public List<Surface> getSurfaceList() {
		return surfaceList;
	}
	public void setSurfaceList(List<Surface> surfaceList) {
		this.surfaceList = surfaceList;
	}
}
