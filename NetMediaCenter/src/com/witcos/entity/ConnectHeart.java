package com.witcos.entity;

public class ConnectHeart {
	
	private Integer state;  //�ն�״̬ 1ע�� 0δע��
	private Integer ledOpen;//������Ļ 1���� 0����
	private Integer update; //�ն˸���״̬   0:��ʾ������ 1:��ʾ���ڸ���   2:��ʾ�������
	
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
