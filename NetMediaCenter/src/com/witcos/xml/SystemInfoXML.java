package com.witcos.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.witcos.entity.SystemInfo;

public class SystemInfoXML extends DefaultHandler{

	private SystemInfo systemInfo = null ;
	private StringBuffer buffer = new StringBuffer();
	
	public SystemInfo getSystemInfo() {
		return systemInfo;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		if (localName.equals("root")){
			systemInfo = new SystemInfo();
		}
		super.startElement(uri, localName, qName, attributes);
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)throws SAXException {
		if(localName.equals("serial")){
			systemInfo.setSerial(buffer.toString().trim());
			buffer.setLength(0);
		}else if (localName.equals("opentime")){
			systemInfo.setOpentime(buffer.toString().trim());
			buffer.setLength(0);
		}else if (localName.equals("closetime")){
			systemInfo.setClosetime(buffer.toString().trim());
			buffer.setLength(0);
		}else if (localName.equals("autoupdate")){
			systemInfo.setAutoupdate(buffer.toString().trim());
			buffer.setLength(0);
		}else if (localName.equals("systime")){
			systemInfo.setSystime(buffer.toString().trim());
			buffer.setLength(0);
		}else if (localName.equals("volume")){
			systemInfo.setVolume(Integer.valueOf(buffer.toString().trim()));
			buffer.setLength(0);
		}else if (localName.equals("bright")){
			systemInfo.setBright(Integer.valueOf(buffer.toString().trim()));
			buffer.setLength(0);
		}else if (localName.equals("weather")){
			systemInfo.setWeather(buffer.toString().trim());
			buffer.setLength(0);
		}else if (localName.equals("romid")){
			systemInfo.setRomid(buffer.toString().trim());
			buffer.setLength(0);
		}else if (localName.equals("server")){
			systemInfo.setServer(buffer.toString().trim());
			buffer.setLength(0);
		}else if (localName.equals("media")){
			systemInfo.setMedia(buffer.toString().trim());
			buffer.setLength(0);
		}else if (localName.equals("mobilephone")){
			systemInfo.setMobilephone(buffer.toString().trim());
			buffer.setLength(0);
		}else if (localName.equals("jpegshowtime")){
			systemInfo.setJpegshowtime(Integer.valueOf(buffer.toString().trim()));
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
