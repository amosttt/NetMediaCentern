package com.witcos.entity.task;

import java.util.ArrayList;
import java.util.List;

/**
 * �������ڻ�ȡƽ̨�������ݷ�װ�Ķ���
 * @author JieHua
 */
public class TaskAndSurface {
	
	private Integer isEmpty = 0 ;		//ɾ�����ݿ��ʾ��1Ϊ�ն�δע�ᣬ��ʱ����ɾ�����ݿ����ݣ�0Ϊ��������ɾ�����ݿ����ݸ��²����б�
	private List<Task> taskList = new ArrayList<Task>();			//���񼯺�
	private List<Surface> surfaceList = new ArrayList<Surface>();	//��鼯��
	
	public Integer getIsEmpty() {
		return isEmpty;
	}
	public void setIsEmpty(Integer isEmpty) {
		this.isEmpty = isEmpty;
	}
	public List<Task> getTaskList() {
		return taskList;
	}
	public void setTaskList(List<Task> taskList) {
		this.taskList = taskList;
	}
	public List<Surface> getSurfaceList() {
		return surfaceList;
	}
	public void setSurfaceList(List<Surface> surfaceList) {
		this.surfaceList = surfaceList;
	}
}
