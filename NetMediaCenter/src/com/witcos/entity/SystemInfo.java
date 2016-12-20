package com.witcos.entity;

public class SystemInfo {
	
	private String serial;			//终端编号
	private String opentime;		//开机时间
	private String closetime;		//关机时间
	private String autoupdate;		//网络更新时间
	private String systime;			//系统同步时间
	private Integer volume;			//终端播放音量
	private Integer bright;			//终端显示亮度
	private String weather;			//终端天气区域
	private String romid;			//升级包
	private String server;			//服务器路径
	private String media;			//媒体库路径
	private String mobilephone;		//手机号码控制
	private Integer jpegshowtime;	//图片显示时间
	private Integer x ;             //显示界面的起始坐标
	private Integer y ;				//显示界面的起始坐标
	
	public String getSerial() {
		return serial;
	}
	public void setSerial(String serial) {
		this.serial = serial;
	}
	public String getOpentime() {
		return opentime;
	}
	public void setOpentime(String opentime) {
		this.opentime = opentime;
	}
	public String getClosetime() {
		return closetime;
	}
	public void setClosetime(String closetime) {
		this.closetime = closetime;
	}
	public String getAutoupdate() {
		return autoupdate;
	}
	public void setAutoupdate(String autoupdate) {
		this.autoupdate = autoupdate;
	}
	public String getSystime() {
		return systime;
	}
	public void setSystime(String systime) {
		this.systime = systime;
	}
	public Integer getVolume() {
		return volume;
	}
	public void setVolume(Integer volume) {
		this.volume = volume;
	}
	public Integer getBright() {
		return bright;
	}
	public void setBright(Integer bright) {
		this.bright = bright;
	}
	public String getWeather() {
		return weather;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
	public String getRomid() {
		return romid;
	}
	public void setRomid(String romid) {
		this.romid = romid;
	}
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public String getMedia() {
		return media;
	}
	public void setMedia(String media) {
		this.media = media;
	}
	public String getMobilephone() {
		return mobilephone;
	}
	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}
	public Integer getJpegshowtime() {
		return jpegshowtime;
	}
	public void setJpegshowtime(Integer jpegshowtime) {
		this.jpegshowtime = jpegshowtime;
	}
	public Integer getX() {
		return x;
	}
	public void setX(Integer x) {
		this.x = x;
	}
	public Integer getY() {
		return y;
	}
	public void setY(Integer y) {
		this.y = y;
	}
}
