package com.witcos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsoluteLayout.LayoutParams;

import com.android.volley.toolbox.NetworkImageView;
import com.jz.bean.FilterBean;
import com.witcos.data.ConnectionProvider;
import com.witcos.entity.task.Media;
import com.witcos.entity.task.Surface;
import com.witcos.entity.task.Task;
import com.witcos.surface.ImageSurface;
import com.witcos.surface.TextSurface;
import com.witcos.surface.VideoSurface;
import com.witcos.tool.VolleyTool;

public class MainActivity extends Activity implements ChangeAllViewsListener {

	private int width; // 屏幕的宽度
	private int height; // 屏幕的高度

	private TextSurface text;
	private ImageSurface image;
	private VideoSurface video;

	private AbsoluteLayout mainLayout; // 主界面
	private ConnectionProvider pro = null; // 数据库访问

	private int playsize = 1; // 播放次数
	private boolean isTime = false; // 是否为定时任务
	private String taskid = ""; // 任务ID
	private int K = 0; // 用于播放控制变量
	private List<Task> taskList = new ArrayList<Task>();// 用于播放的缓存列表
	private ArrayList<FilterBean> filterBeans;

	private LinearLayout top, bottom;
	// private View bottom;
	private AbsoluteLayout.LayoutParams paramst, paramsb;

	private TimeCount time;

	private SoundPool soundPool;
	private boolean loaded;
	private int soundID;

	private String targetname = "", targetvalue = "", operate = "";

	private Map<String, String> map = new HashMap<String, String>();
	private ArrayList<TextView> views = new ArrayList<TextView>();

	public Handler cwjHandler = new Handler();

	// 释放所有模块
	private void destroyView() {
		for (int i = 0; i < mainLayout.getChildCount(); i++) {
			mainLayout.getChildAt(i).destroyDrawingCache();
		}
		mainLayout.removeAllViews();
	}

	@Override
	protected void onDestroy() {
		destroyView();
		unRegisterBroadcast();
		unregistBroadcast();
		unregisttBroadcast();
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 让屏幕一直开着
																	// 不进入待机黑屏

		width = getWindowManager().getDefaultDisplay().getWidth();
		height = getWindowManager().getDefaultDisplay().getHeight();

		mainLayout = new AbsoluteLayout(this);// 主界面
		setContentView(mainLayout, new AbsoluteLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0, 0));

		// 新页面接收数据
		Bundle bundle = this.getIntent().getExtras();
		filterBeans = (ArrayList<FilterBean>) bundle.getSerializable("LIST");

		// top = LayoutInflater.from(this).inflate(R.layout.include_top, null);
		// bottom = LayoutInflater.from(this).inflate(R.layout.include_bottom,
		// null);
		paramst = new AbsoluteLayout.LayoutParams(width, 170, 0, 0);
		paramsb = new AbsoluteLayout.LayoutParams(width, 170, 0, 550);

		top = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.include_view_top, null);
		bottom = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.include_view_bottom, null);

		if (filterBeans != null && filterBeans.size() != 0) {
			for (int i = 0; i < filterBeans.size(); i++) {
				final int m = i;
				View view = (View) LayoutInflater.from(MainActivity.this)
						.inflate(R.layout.sd_item, null);
				Button button = (Button) view.findViewById(R.id.button);
				NetworkImageView imageView = (NetworkImageView) view
						.findViewById(R.id.image);
				imageView.setDefaultImageResId(R.drawable.xxgk);
				imageView.setImageUrl(filterBeans.get(i).getImage(),
						VolleyTool.getImageLoader(this));
				button.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						intent(filterBeans.get(m));
					}
				});
				if (i <= 6) {
					top.addView(view);
				} else {
					bottom.addView(view);
				}
			}
		}

		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId,
					int status) {
				loaded = true;
			}
		});
		soundID = soundPool.load(this, R.raw.sound, 1);

		image = new ImageSurface(getApplicationContext(), mainLayout);
		image.setonChangeAllViewListener(this);
		text = new TextSurface(getApplicationContext(), mainLayout);
		text.setonChangeAllViewListener(this);
		video = new VideoSurface(getApplicationContext(), mainLayout);
		video.setOnChangeAllViewsListener(this);

		registerBroadcast();
		registBroadcast();
		registtBroadcast();

		pro = MyService.pro;
		if (null == pro) {
			this.finish();
			return;
		}

		try {
			isTime = bundle.getBoolean("isTime");
			taskid = bundle.getString("tid");
			playsize = bundle.getInt("playsize");

			K = 0;
			changeAllView();
			System.out.println("isTime=" + isTime + "-taskid=" + taskid
					+ "-playsize=" + playsize + "-list" + filterBeans.size());
		} catch (Exception e) {
			Log.e("amos_e", e + "");
			try {
				changeAllView();
			} catch (Exception e2) {
				// TODO: handle exception
				Log.e("amos_e2", e2 + "");
			}
		}

		// displaySurface(null);
	}

	@Override
	public void changeAllView() {
		destroyView();// 释放view
		if (K == 0) {
			taskList = selectTask();
			if (taskList.size() == 0) {
				// 显示暂无任务 可以放置一张图片
				image.addImageView(width, height, 0, 0, R.drawable.nono);
				text.addTextView(500, 100, 0, 0, 50, "暂无播放任务！", "#FFFFFF", 0,
						"#FFFFFF", 255);
			} else {
				displaySurface(taskList.get(0));
				K++;
			}
		} else if (K == playsize) {
			K = 0;
			if (!isTime) {
				changeAllView();
			} else {
				destroyView();
				exit();
			}
		} else {
			if (isTime) {
				displaySurface(taskList.get(0));
			} else {
				displaySurface(taskList.get(K));
			}
			K++;
		}
	}

	// 查询数据库
	private List<Task> selectTask() {
		if (ConnectionProvider.lock) {
			ConnectionProvider.lock = false;// 加锁
			taskList.clear();
			if (isTime) {
				taskList = pro.findTaskByTid(taskid);
			} else {
				taskList = pro.findTaskAllLoop();
				playsize = taskList.size();
			}
			for (Task t : taskList) {
				for (Media m : pro.findMediaByTid(t.getTid())) {
					t.getMediaMap().put(m.getBid(), m.getMedias());
				}
				t.getSurfaceList().addAll(pro.findSurfaceBySid(t.getSid()));
			}
			ConnectionProvider.lock = true;// 解锁
		}
		return taskList;
	}

	// 显示布局
	private void displaySurface(Task t) {
		if (t == null) {
			mainLayout.addView(top, paramst);
			mainLayout.addView(bottom, paramsb);

			top.setVisibility(View.GONE);
			bottom.setVisibility(View.GONE);
			return;
		}

		Log.e("amos", "displaySurface");

		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for (Surface s : t.getSurfaceList()) {
			if (s.getType().equals("playZone")) {
				// 视频播放区
				sb.append("{\"id\":\" " + s.getX() + s.getY()
						+ "\",\"type\":\"video\",\"x\":\"" + s.getX()
						+ "\",\"y\":\"" + s.getY() + "\"},");
				video.addVideo(s.getWidth(), s.getHeight(), s.getX(), s.getY(),
						t.getMediaMap().get(s.getId()).split(";"));
			} else if (s.getType().equals("picZone")) {
				// 图片播放区
				sb.append("{\"id\":\" " + s.getX() + s.getY()
						+ "\",\"type\":\"image\",\"x\":\"" + s.getX()
						+ "\",\"y\":\"" + s.getY() + "\"},");
				image.addImageView(s.getWidth(), s.getHeight(), s.getX(),
						s.getY(), Integer.parseInt(s.getAlpha()),
						s.getIsMain() == 1 ? true : false,
						t.getMediaMap().get(s.getId()).split(";"), 20);
			} else if (s.getType().equals("vcaptionZone")) {
				// 垂直滚动字幕播放区
				sb.append("{\"id\":\" " + s.getX() + s.getY()
						+ "\",\"type\":\"text\",\"x\":\"" + s.getX()
						+ "\",\"y\":\"" + s.getY() + "\"},");
				text.addVTextView(s.getWidth(), s.getHeight(), s.getX(),
						s.getY(), Integer.valueOf(s.getFontSize()),
						s.getIsMain() == 1 ? true : false,
						t.getMediaMap().get(s.getId()).split(";"),
						s.getBackColor(), Integer.valueOf(s.getBackAlpha()),
						s.getForeColor(), Integer.valueOf(s.getForeAlpha()));
			} else if (s.getType().equals("hcaptionZone")) {
				// 水平滚动字幕播放区
				sb.append("{\"id\":\" " + s.getX() + s.getY()
						+ "\",\"type\":\"text\",\"x\":\"" + s.getX()
						+ "\",\"y\":\"" + s.getY() + "\"},");
				text.addHTextView(s.getWidth(), s.getHeight(), s.getX(),
						s.getY(), Integer.valueOf(s.getFontSize()), t
								.getMediaMap().get(s.getId()).split(";"),
						s.getBackColor(), Integer.valueOf(s.getBackAlpha()),
						s.getForeColor(), Integer.valueOf(s.getForeAlpha()));
			} else if (s.getType().equals("staticpicZone")) {
				// 静态图片显示区
				image.addImageView(s.getWidth(), s.getHeight(), s.getX(),
						s.getY(), Integer.parseInt(s.getAlpha()), false,
						new String[] { s.getBackPic() }, 0);
			} else if (s.getType().equals("statictxtZone")) {
				// 静态文本区域
				text.addTextView(s.getWidth(), s.getHeight(), s.getX(),
						s.getY(), Integer.valueOf(s.getFontSize()), s.getTxt(),
						s.getBackColor(), Integer.valueOf(s.getBackAlpha()),
						s.getForeColor(), Integer.valueOf(s.getForeAlpha()));
			} else if (s.getType().equals("dateZone")) {
				// 日期区域
				text.addDateView(s.getWidth(), s.getHeight(), s.getX(),
						s.getY(), Integer.valueOf(s.getFontSize()),
						s.getBackColor(), Integer.valueOf(s.getBackAlpha()),
						s.getForeColor(), Integer.valueOf(s.getForeAlpha()));
			} else if (s.getType().equals("timeZone")) {
				// 时间区域
				text.addTimeView(s.getWidth(), s.getHeight(), s.getX(),
						s.getY(), Integer.valueOf(s.getFontSize()),
						s.getBackColor(), Integer.valueOf(s.getBackAlpha()),
						s.getForeColor(), Integer.valueOf(s.getForeAlpha()));
			} else if (s.getType().equals("weekZone")) {
				// 星期区域
				text.addWeekView(s.getWidth(), s.getHeight(), s.getX(),
						s.getY(), Integer.valueOf(s.getFontSize()),
						s.getBackColor(), Integer.valueOf(s.getBackAlpha()),
						s.getForeColor(), Integer.valueOf(s.getForeAlpha()));
			} else if (s.getType().equals("weatherZone")) {
				// 天气区域
				text.addWeatherView(s.getWidth(), s.getHeight(), s.getX(),
						s.getY(), Integer.valueOf(s.getFontSize()),
						s.getBackColor(), Integer.valueOf(s.getBackAlpha()),
						s.getForeColor(), Integer.valueOf(s.getForeAlpha()));
			}
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("]");
		// BroadcastReceiverHelps.sendTargetInfo("zone",sb.toString()); //发送广播
		// targetname = "zone";
		// targetvalue = sb.toString();

		operate = t.getOperate();

		mainLayout.addView(top, paramst);
		mainLayout.addView(bottom, paramsb);
		top.setVisibility(View.GONE);
		bottom.setVisibility(View.GONE);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (!top.isShown()) {
			if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
				System.out.println("targetname=" + targetname + "-targetvalue="
						+ targetvalue);
				if (targetname.equals("targetpath")) {
					System.out.println(targetname + "-" + targetvalue);
					FilterBean bean = new FilterBean();
					bean.setAction(targetvalue.split("#")[0]);
					bean.setDataString(targetvalue.replace("",
							targetvalue.split("#")[0] + "#"));
					intent(bean);
				} else {
					try {
						Toast.makeText(MainActivity.this,
								"正在跳转到网页：" + targetvalue, Toast.LENGTH_LONG)
								.show();
						Uri uri = Uri.parse(targetvalue);
						Intent it = new Intent(Intent.ACTION_VIEW, uri);
						startActivity(it);
					} catch (Exception e) {
						// TODO: handle exception
					}

				}
			} else {
				Animation animation = AnimationUtils.loadAnimation(
						MainActivity.this, R.anim.push_uphalf_in);
				Animation animationd = AnimationUtils.loadAnimation(
						MainActivity.this, R.anim.push_downhalf_in);
				top.setAnimation(animation);
				bottom.setAnimation(animationd);
				top.setVisibility(View.VISIBLE);
				bottom.setVisibility(View.VISIBLE);
				top.requestFocus();

				for (int i = 0; i < views.size(); i++) {
					views.get(i).setVisibility(View.GONE);
					System.out.println("GONE==" + i);
				}

				time = new TimeCount(5000, 1000);// 构造CountDownTimer对象
				time.start();
			}
		} else {
			time.cancel();
			time.start();
		}

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			System.out.println("operate==" + operate);
			if (operate.equals("yesoperate")) {
				exit();
				return true;
			} else {
				return false;
			}
		} else if (keyCode == KeyEvent.KEYCODE_1) {
			intent(0);
			return false;
		} else if (keyCode == KeyEvent.KEYCODE_2) {
			intent(1);
			return false;
		} else if (keyCode == KeyEvent.KEYCODE_3) {
			intent(2);
			return false;
		} else if (keyCode == KeyEvent.KEYCODE_4) {
			intent(3);
			return false;
		} else if (keyCode == KeyEvent.KEYCODE_5) {
			intent(4);
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			Animation animation = AnimationUtils.loadAnimation(
					MainActivity.this, R.anim.push_uphalf_out);
			Animation animationd = AnimationUtils.loadAnimation(
					MainActivity.this, R.anim.push_downhalf_out);
			top.setAnimation(animation);
			bottom.setAnimation(animationd);
			top.setVisibility(View.GONE);
			bottom.setVisibility(View.GONE);

			for (int i = 0; i < views.size(); i++) {
				views.get(i).setVisibility(View.VISIBLE);
				System.out.println("VISIBLE==" + i);
			}
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示

		}
	}

	public void exit() {
		AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		float actualVolume = (float) audioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		float maxVolume = (float) audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volume = actualVolume / maxVolume;
		if (loaded) {
			soundPool.play(soundID, volume, volume, 1, 0, 1f);
		}
		finish();
		overridePendingTransition(R.anim.flip_vertical_in,
				R.anim.flip_vertical_out);
	}

	private void intent(FilterBean idFilter) {
		System.out.println("INTENT==" + idFilter.getAction() + "--"
				+ idFilter.getDataString());
		Intent p = new Intent(idFilter.getAction());
		try {
			p.putExtra("INFO", idFilter.getDataString() + "#@");
			startActivity(p);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("e==" + e);
			try {
				Intent pa = new Intent();
				pa.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				pa = getPackageManager().getLaunchIntentForPackage(
						idFilter.getAction());
				startActivity(pa);
			} catch (Exception e1) {
				System.out.println("e1==" + e1);
				Toast.makeText(this, "暂未安装此应用！", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private TestReceiver receiver = null;

	private void registerBroadcast() {
		receiver = new TestReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.test.broadcast");
		registerReceiver(receiver, filter);
	}

	private void unRegisterBroadcast() {
		if (null != receiver) {
			unregisterReceiver(receiver);
		}
	}

	private class TestReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			exit();
		}
	}

	private CoorReceive receive;

	private void registBroadcast() {
		receive = new CoorReceive();
		IntentFilter filter = new IntentFilter(
				"witcos.intent.action.taskaction");
		registerReceiver(receive, filter);
	}

	private void unregistBroadcast() {
		try {
			if (receive != null) {
				unregisterReceiver(receive);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	class CoorReceive extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			int type = bundle.getInt("type");
			switch (type) {

			case 3: // 接收当前播放任务关联属性属性
				String coor = bundle.getString("targetname"); // 关联类型(urlpath,targetpath）
				String value = bundle.getString("targetvalue");// 关联对应的

				if (value != null && !value.equals("null")
						&& !value.equals("[null]")) {
					map.put(coor, value);
				}

				for (int j = 0; j < views.size(); j++) {
					mainLayout.removeView(views.get(j));
				}
				views.clear();

				int index = 1;
				Iterator i = map.entrySet().iterator();
				while (i.hasNext()) {
					Map.Entry<String, String> entry = (Map.Entry<String, String>) i
							.next();
					String[] str = entry.getKey().split(",");
					int x = Integer.valueOf(str[0]);
					int y = Integer.valueOf(str[1]);

					TextView textView = new TextView(MainActivity.this);
					textView.setText("按 " + index + " 》");
					textView.setTextColor(Color.parseColor("#ffffff"));
					textView.setBackgroundColor(Color.parseColor("#20000000"));
					textView.setGravity(Gravity.CENTER);
					textView.setPadding(10, 0, 10, 0);
					textView.setTextSize(25);
					AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.WRAP_CONTENT, x + 10,
							y + 10);
					textView.setLayoutParams(params);
					textView.setTag(entry.getValue());

					views.add(textView);
					index++;
				}
				for (int j = 0; j < views.size(); j++) {
					mainLayout.addView(views.get(j));
				}
			default:
				break;
			}
		}
	}

	private void intent(int i) {
		if (views.size() < i) {
			return;
		}
		if (views.get(i).getTag() == null) {
			return;
		}
		String targetvalue = views.get(i).getTag().toString();
		System.out.println("intent-targetvalue=" + targetvalue);
		if (targetvalue != null && !targetvalue.equals("")) {
			String[] strings = targetvalue.split("#");
			FilterBean bean = new FilterBean();
			bean.setAction(strings[0]);
			bean.setDataString(targetvalue.substring(strings[0].length() + 1,
					targetvalue.length() - 1));
			intent(bean);
		}
	}

	private TaskReceive receivet;

	private void registtBroadcast() {
		receivet = new TaskReceive();
		IntentFilter filter = new IntentFilter(
				"com.vas.smartcommunity.controltaskplay");
		registerReceiver(receivet, filter);
	}

	private void unregisttBroadcast() {
		try {
			if (receivet != null) {
				unregisterReceiver(receivet);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	class TaskReceive extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			String uri = bundle.getString("uri");
			if (uri.equals("0")) {
				exit();
			}
		}
	}

}