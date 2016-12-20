package com.witcos.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.witcos.entity.WeatherInfo;

public class WeatherInfoXML extends DefaultHandler{
	
	private WeatherInfo weatherInfo=null;
	private StringBuffer buffer = new StringBuffer();
	
	public WeatherInfo getWeatherInfo() {
		return weatherInfo;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (localName.equals("root")){
			weatherInfo = new WeatherInfo();
		}
		super.startElement(uri, localName, qName, attributes);
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)throws SAXException {
		if (localName.equals("temperature")){
			weatherInfo.setTemperature(buffer.toString().trim());
			buffer.setLength(0);
		}else if (localName.equals("humidity")){
			weatherInfo.setHumidity(buffer.toString().trim());
			buffer.setLength(0);
		}else if (localName.equals("windWay")){
			weatherInfo.setWindWay(buffer.toString().trim());
			buffer.setLength(0);
		}else if (localName.equals("weather")){
			weatherInfo.setWeather(buffer.toString().trim());
			buffer.setLength(0);
		}else if (localName.equals("icon")){
			weatherInfo.setIcon(buffer.toString().trim());
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
