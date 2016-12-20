package com.witcos.entity;

public class WeatherInfo {

	private String temperature; //温度
	private String humidity;	//湿度
	private String windWay;		//风向
	private String weather;		//天气
	private String icon;		//图标
	
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getHumidity() {
		return humidity;
	}
	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}
	public String getWindWay() {
		return windWay;
	}
	public void setWindWay(String windWay) {
		this.windWay = windWay;
	}
	public String getWeather() {
		return weather;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
}
