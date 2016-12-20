package com.witcos.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DataHandle extends SQLiteOpenHelper {
	
	public static final int VERSION = 1 ; 
	public static final String DATABASE = "witcos.db";  
	
	//ÏµÍ³±í
	public static String system="create table if not exists systeminfo(id integer primary key ," +
			"tid varchar(50),opentime varchar(255),closetime varchar(255),nettime varchar(255),mobile varchar(255),volume integer,bright integer," +
			"rom varchar(255),jpegshowtime integer,x integer,y integer,urlpath varchar(255),urlmediapath varchar(255))";
	//ÈÎÎñ±í
	public static String task="create table if not exists task(id varchar(50) primary key ," +
			"indexs integer, sid varchar(50),width interger,height interger,startdate varchar(50),stopdate varchar(50)," +
			"tasktype varchar(50),operate varchar(50),starttime varchar(50),playsize integer,playlevel varchar(50),readtext varchar(50) )" ;
	
	//Ã½Ìå±í
	public static String media="create table if not exists media(id varchar(50) primary key ," +
			"tid varchar(50),bid varchar(50), mid text)";
	//ÑùÊ½°å¿é±í
	public static String surface="create table if not exists surface(id varchar(50) primary key ," +
			"sid varchar(50),type varchar(50),x integer,y integer,width integer,height integer,indexs integer,ismain integer,txt text," +
			"alpha varchar(255),font varchar(255),fontsize varchar(255),backcolor varchar(255),backalpha varchar(255)," +
			"forecolor varchar(255),forealpha varchar(255),backpic varchar(255))";
	
	public DataHandle(Context context, String name){
		this(context, name, VERSION);
	}
	
	public DataHandle(Context context, String name,int version){
		this(context, name, null, version);
	}

	public DataHandle(Context context, String name, CursorFactory factory,int version) {
		super(context, name, factory, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(system);
		db.execSQL(task);
		db.execSQL(media);
		db.execSQL(surface);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
	
}
