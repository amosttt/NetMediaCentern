package com.witcos.entity.task;

public class Media {

	private String mid;  	//UUID
	private String tid;		//任务编号
	private String bid;		//版块编号
	private String medias;	//媒体集合 按照播放顺序，用分号分隔
	
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getMedias() {
		return medias;
	}
	public void setMedias(String medias) {
		this.medias = medias;
	}
}
