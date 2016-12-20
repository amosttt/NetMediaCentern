package com.witcos;

import com.witcos.data.ConnectionProvider;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {

	public static ConnectionProvider pro = null;
	private final IBinder mBinder = new LocalBinder();
	public static BackThread back = null;

	@Override
	public void onCreate() {
		super.onCreate();
		pro = new ConnectionProvider(getApplicationContext());
		back = new BackThread(getApplicationContext(), pro);
		Log.d("//////////////////", "service-onCreate");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		try {
			Bundle bundle = intent.getExtras();
			String id = bundle.getString("id");
			int x = bundle.getInt("x");
			int y = bundle.getInt("y");
			String server = bundle.getString("server");
			String media = bundle.getString("media");

			// pro = new
			// ConnectionProvider(getApplicationContext(),id,x,y,server,media);
			// new BackThread(getApplicationContext(),pro).start();
			pro.setParameter(id, x, y, server, media);

			if (!back.isInterrupted()) {
				try {
					back.start();
				} catch (Exception e) {
					// TODO: handle exception
				}
				Log.d("/////////Thread", "start backThread");
			}

			Log.d("*********//start service", id + ";" + x + ";" + y + ";"
					+ server + ";" + media);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}

	public class LocalBinder extends Binder {
		MyService getService() {
			return MyService.this;
		}
	}
}
