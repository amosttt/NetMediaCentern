package com.witcos.xml;

import java.util.UUID;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.witcos.entity.task.Media;
import com.witcos.entity.task.Surface;
import com.witcos.entity.task.Task;
import com.witcos.entity.task.TaskAndSurface;


public class PlayListInfoXML extends DefaultHandler{
	
	private Task task=null;
	private Media media=null;
	private Surface surface=null;
	private TaskAndSurface ts=null;
	private StringBuffer buffer = new StringBuffer();
	
	public TaskAndSurface getTs() {
		return ts;
	}
	
	@Override
	public void startDocument() throws SAXException {
		ts = new TaskAndSurface();
		ts.setIsEmpty(0);
		super.startDocument();
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		if(localName.equals("State")){
			ts.setIsEmpty(1);
		}else if (localName.equals("task")){
			task = new Task();
			task.setTid(attributes.getValue("id"));
			task.setSid(attributes.getValue("sid"));
			task.setIndex(Integer.valueOf(attributes.getValue("index")));	
			task.setHeight(Integer.valueOf(attributes.getValue("height")));
			task.setWidth(Integer.valueOf(attributes.getValue("width")));
			task.setStartDate(attributes.getValue("startDate"));
			task.setStopDate(attributes.getValue("stopDate"));
			task.setTaskType(attributes.getValue("taskType"));
			task.setOperate(attributes.getValue("operate"));
			task.setStartTime(attributes.getValue("startTime"));
			task.setPlaySize(Integer.valueOf(attributes.getValue("playsize")));
			task.setPlaylevel(attributes.getValue("playlevel"));
			task.setReadtext(attributes.getValue("readtext"));
		}else if(localName.equals("media")){
			media=new Media();
			media.setMid(UUID.randomUUID().toString());
			media.setBid(attributes.getValue(0));
			media.setTid(task.getTid());
		}else if(localName.equals("surface")){
			surface = new Surface();
			surface.setId(attributes.getValue("id"));
			surface.setSid(attributes.getValue("sid"));
			surface.setType(attributes.getValue("type"));
			surface.setX(Integer.valueOf(attributes.getValue("x")));
			surface.setY(Integer.valueOf(attributes.getValue("y")));
			surface.setWidth(Integer.valueOf(attributes.getValue("width")));
			surface.setHeight(Integer.valueOf(attributes.getValue("height")));
			surface.setIndex(Integer.valueOf(attributes.getValue("index")));
			surface.setIsMain(Integer.valueOf(attributes.getValue("isMain")));
			surface.setTxt(attributes.getValue("txt"));
			surface.setBackPic(attributes.getValue("backPic"));
			surface.setAlpha(attributes.getValue("alpha"));
			surface.setFont(attributes.getValue("font"));
			surface.setFontSize(attributes.getValue("fontSize"));
			surface.setBackColor(attributes.getValue("backColor"));
			surface.setBackAlpha(attributes.getValue("backAlpha"));
			surface.setForeColor(attributes.getValue("foreColor"));
			surface.setForeAlpha(attributes.getValue("foreAlpha"));	
			ts.getSurfaceList().add(surface);
			surface = null ;
		}
		super.startElement(uri, localName, qName, attributes);
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)throws SAXException {
		if(localName.equals("media")){
			media.setMedias(buffer.toString().trim());
			task.getMediaList().add(media);
			media=null;
			buffer.setLength(0);
		}else if(localName.equals("task")){
			ts.getTaskList().add(task);
			task = null ;
			buffer.setLength(0);
		}
		super.endElement(uri, localName, qName);
	}
	
	@Override
	public void characters(char[] ch, int start, int length)throws SAXException {
		buffer.append(ch, start, length);
		super.characters(ch, start, length);
	}

}
