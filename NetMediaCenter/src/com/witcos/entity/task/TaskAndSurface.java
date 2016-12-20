package com.witcos.entity.task;

import java.util.ArrayList;
import java.util.List;

/**
 * 此类用于获取平台交互数据封装的对象
 * @author JieHua
 */
public class TaskAndSurface {
	
	private Integer isEmpty = 0 ;		//删除数据库标示，1为终端未注册，此时不能删除数据库数据，0为正常可以删除数据库数据更新播放列表
	private List<Task> taskList = new ArrayList<Task>();			//任务集合
	private List<Surface> surfaceList = new ArrayList<Surface>();	//版块集合
	
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
