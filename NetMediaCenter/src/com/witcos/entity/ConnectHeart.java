package com.witcos.entity;

public class ConnectHeart {
	
	private Integer state;  //终端状态 1注册 0未注册
	private Integer ledOpen;//开关屏幕 1开屏 0关屏
	private Integer update; //终端更新状态   0:表示不更新 1:表示正在更新   2:表示更新完成
	
	public Integer getUpdate() {
		return update;
	}
	public void setUpdate(Integer update) {
		this.update = update;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Integer getLedOpen() {
		return ledOpen;
	}
	public void setLedOpen(Integer ledOpen) {
		this.ledOpen = ledOpen;
	}

}
