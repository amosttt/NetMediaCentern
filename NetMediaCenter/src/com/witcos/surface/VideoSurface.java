package com.witcos.surface;

import java.io.IOException;

import com.witcos.BroadcastReceiverHelps;
import com.witcos.ChangeAllViewsListener;
import com.witcos.tool.DownloadFile;
import com.witcos.tool.MyLayout;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.AbsoluteLayout;

public class VideoSurface implements OnCompletionListener,
		MediaPlayer.OnPreparedListener, SurfaceHolder.Callback {

	private String id; // ģ��ID

	private int temp;
	private String[] fileNames;

	private Context context;
	private AbsoluteLayout abs;
	private MediaPlayer mediaPlayer;
	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;

	// ������
	public VideoSurface(Context context, AbsoluteLayout abs) {
		this.abs = abs;
		this.context = context;
	}

	/* ������Ƶ���Ž���������л���ͼ */
	private ChangeAllViewsListener listener;

	public void setOnChangeAllViewsListener(ChangeAllViewsListener listener) {
		this.listener = listener;
	}

	public void addVideo(int w, int h, int x, int y, String[] fileNames) {
		id = x + "," + y;
		this.temp = 0;
		this.fileNames = fileNames;
		surfaceView = new SurfaceView(context);
		this.surfaceHolder = this.surfaceView.getHolder();
		this.surfaceHolder.addCallback(this);
		this.surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		abs.addView(surfaceView, new MyLayout().getLayout(w, h, x, y));
	}

	/** ��һ�ε��ã�ֻ����һ�� **/
	private void playVideo(String fileName) throws IllegalArgumentException,
			IllegalStateException, IOException {
		this.mediaPlayer = new MediaPlayer();
		this.mediaPlayer.setDataSource(DownloadFile.FILEPATH + fileName);
		this.surfaceHolder.setFixedSize(this.surfaceView.getWidth(),
				this.surfaceView.getHeight());
		this.mediaPlayer.setDisplay(this.surfaceHolder);
		this.mediaPlayer.setOnPreparedListener(this);
		this.mediaPlayer.setOnCompletionListener(this);
		this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		this.mediaPlayer.prepareAsync();
		Log.e("amos", "first");
	}

	/** �ڶ��β���ʱ���ã��Ժ�ѭ������ **/
	private void playVideos(String fileName) throws IllegalArgumentException,
			IllegalStateException, IOException {
		this.mediaPlayer.reset();
		this.mediaPlayer.setDataSource(DownloadFile.FILEPATH + fileName);
		this.mediaPlayer.prepareAsync();
		Log.e("amos", "second");
	}

	// /////////////////////////////////////////////////////////////////////////
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			String names = fileNames[0];
			this.playVideo(names.split(",")[0]);
			BroadcastReceiverHelps.sendTargetInfo(id, names.split(",")[1]); // ���͹㲥
		} catch (Exception e) {

		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (this.mediaPlayer != null) {
			this.mediaPlayer.release();
			this.mediaPlayer = null;
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	@Override
	public void onPrepared(MediaPlayer mp) {
		this.mediaPlayer.start();
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		temp++;
		if (temp == fileNames.length) {
			if (null != listener) {
				listener.changeAllView();
			}
		} else {
			try {
				String names = fileNames[temp];
				this.playVideos(names.split(",")[0]);
				BroadcastReceiverHelps.sendTargetInfo(id, names.split(",")[1]); // ���͹㲥
			} catch (Exception e) {
			}
		}
	}
}