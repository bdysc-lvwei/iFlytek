package com.ahjz.deveb;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.media.AudioManager;

import com.ahjz.deveb.R;
import com.ahjz.deveb.R.menu;
import com.ahjz.deveb.app.AllApplication;
import com.ahjz.deveb.app.AppInfo;
import com.ahjz.deveb.service.MusicServer;
import com.ahjz.deveb.service.PingyinUtils;
import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechError;
import com.iflytek.speech.SynthesizerPlayer;
import com.iflytek.ui.RecognizerDialog;
import com.iflytek.ui.RecognizerDialogListener;
import com.iflytek.ui.SynthesizerDialog;
import com.iflytek.ui.SynthesizerDialogListener;

@SuppressWarnings("deprecation")
public class MainActivity extends Activity {

	// 声明控件
	private EditText et;
	private Button bt1;
	private Button bt2;
	private Button bt3;
	private ToggleButton tb;
	private String appLabel;
	private String appPac;
	private Camera m_Camera = null;
	private AudioManager audio;
	public static final int RINGER_MODE_SILENT = 0;
	public static final int RINGER_MODE_VIBRATE = 1;
	public static final int RINGER_MODE_NORMAL = 2;
	// 全局只设一个String，因为String为final类型，这样做节省内存
	String text = "";
	private static final String APPID = "appid=519328ab";

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// 这是后台朗读，实例化一个SynthesizerPlayer
		SynthesizerPlayer player = SynthesizerPlayer.createSynthesizerPlayer(
				MainActivity.this, APPID);
		// 设置语音朗读者
		player.setVoiceName("vivixiaoyan");
		// 在此设置语音播报的人选
		player.playText("欢迎进入语音助手！", "ent=vivi21,bft=5", null);

		bt1 = (Button) findViewById(R.id.bt_recognize);
		bt2 = (Button) findViewById(R.id.bt_speek);
		bt3 = (Button) findViewById(R.id.tl_chat);
		et = (EditText) findViewById(R.id.et);
		tb = (ToggleButton) findViewById(R.id.tb);
		// 初始化监听器
		initListener();

	}

	private void initListener() {
		bt1.setOnClickListener(myListener);
		bt2.setOnClickListener(myListener);
		bt3.setOnClickListener(myListener);

	}

	OnClickListener myListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 根据不同View的id调用不同方法
			switch (v.getId()) {
			case R.id.bt_recognize:
				// 这是语言识别部分，最重要的实例化一个
				// RecognizerDialog并把你在官方网站申请的appid填入进去，非法id不能进行识别
				RecognizerDialog isrDialog = new RecognizerDialog(
						MainActivity.this, APPID);
				isrDialog.setEngine("sms", null, null);
				isrDialog.setListener(recoListener);
				isrDialog.show();
				break;

			case R.id.bt_speek:
				// 这是语言合成部分 同样需要实例化一个SynthesizerDialog ，并输入appid
				SynthesizerDialog syn = new SynthesizerDialog(
						MainActivity.this, APPID);
				syn.setListener(new SynthesizerDialogListener() {

					@Override
					public void onEnd(SpeechError arg0) {

					}
				});
				// 根据EditText里的内容实现语音合成
				syn.setText(et.getText().toString(), null);
				syn.show();
				break;

			case R.id.tl_chat:
				Intent i = new Intent(MainActivity.this, ChatActivity.class);
				startActivity(i);
				break;
			default:
				break;
			}

		}
	};
	// 语言识别监听器，有两个方法
	RecognizerDialogListener recoListener = new RecognizerDialogListener() {

		@Override
		public void onResults(ArrayList<RecognizerResult> results,
				boolean isLast) {
			// 新增加了一个ToggleButton tb，首先检查tb是否被按下，如果被按下才进行语言控制，没被按下就进行文字识别
			if (tb.isChecked()) {
				// doVoice方法就是进行识别
				doVoice(results);
			} else {
				// 服务器识别完成后会返回集合，我们这里就只得到最匹配的那一项
				text += results.get(0).text;
				System.out.println(text);
			}

		}

		/*
		 * 首先迭代结果，然后获取每个结果，并进行对比， 如果包含有特定字符串，那么就执行相应Intent跳转。
		 */
		private void doVoice(ArrayList<RecognizerResult> results) {
			Intent i = new Intent();
			for (RecognizerResult result : results) {
				if (result.text.contains("唱歌")) {
					// 音乐界面的跳转
					Intent intent = new Intent(MainActivity.this,
							MusicServer.class);
					startService(intent);
				} else if (result.text.contains("停止唱歌")) {
					onDestroy();
				} else if (result.text.contains("打开短信")) {
					// 短信界面的跳转
					i.setAction(Intent.ACTION_VIEW);
					i.setType("vnd.android-dir/mms-sms");
					startActivity(i);
				} else if (result.text.contains("打开微信")) {
					// 微信界面的跳转
					ComponentName com = new ComponentName("com.tencent.mm",
							"com.tencent.mm.ui.LauncherUI");
					i.setAction(Intent.ACTION_MAIN);
					i.addCategory(Intent.CATEGORY_LAUNCHER);
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					i.setComponent(com);
					startActivity(i);
				} else if (result.text.contains("打开QQ")) {
					// QQ界面的跳转
					ComponentName com = new ComponentName(
							"com.tencent.mobileqq",
							"com.tencent.mobileqq.activity.SplashActivity");
					i.setAction(Intent.ACTION_MAIN);
					i.addCategory(Intent.CATEGORY_LAUNCHER);
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					i.setComponent(com);
					startActivity(i);
				} else if (result.text.contains("打开手电筒")) {
					try {
						m_Camera = Camera.open();
						Camera.Parameters mParameters;
						mParameters = m_Camera.getParameters();
						mParameters
								.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
						m_Camera.setParameters(mParameters);
					} catch (Exception ex) {
						System.out.print("你的手机没有手电筒！");
					}
				} else if (result.text.contains("关闭手电筒")) {
					try {
						Camera.Parameters mParameters;
						mParameters = m_Camera.getParameters();
						mParameters
								.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
						m_Camera.setParameters(mParameters);
						m_Camera.release();
					} catch (Exception ex) {
					}
				} else if (result.text.contains("静音")) {
					// audio=(AudioManager)Context.getSystemService(Context.AUDIO_SERVICE);
					audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);
				} else if (result.text.contains("打开")) {
					// Intent innt=new Intent();
					String dictation = result.text;
					// TODO Auto-generated method stub
					// System.out.println("本机所用应用程序："
					// + AllApplication.getAllApplications(speechMain));

					if (dictation.endsWith("。"))
						dictation = dictation.substring(0,
								dictation.length() - 1);
					// 如果仅仅是打开两个字，返回主会话
					if (dictation.equals("打开")) {
						Toast.makeText(MainActivity.this, "您要打开什么应用呢？",
								Toast.LENGTH_SHORT).show();
					}
					String regx = "打开(\\S+)";
					Matcher matcher = Pattern.compile(regx).matcher(dictation);
					if (matcher.find()) {
						appLabel = matcher.group(1);
						List<AppInfo> appInfos = AllApplication
								.getAllApplications(MainActivity.this);
						// 采用首次匹配原则
						for (AppInfo appInfo : appInfos) {
							String label = appInfo.getLabel();
							String pacName = appInfo.getPackageName();
							if (PingyinUtils.converterToSpell(label).contains(
									PingyinUtils.converterToSpell(appLabel))
									|| PingyinUtils
											.converterToSpell(appLabel)
											.contains(
													PingyinUtils
															.converterToSpell(label))) {
								appPac = pacName;
								break;
							}
						}
						Toast.makeText(getApplicationContext(),
								"正在为您打开：" + appLabel, Toast.LENGTH_SHORT)
								.show();
						Intent intent = getPackageManager()
								.getLaunchIntentForPackage(appPac);
						startActivity(intent);
					} else {
						// 这是后台朗读，实例化一个SynthesizerPlayer
						SynthesizerPlayer player = SynthesizerPlayer
								.createSynthesizerPlayer(MainActivity.this,
										APPID);
						// 设置语音朗读者
						player.setVoiceName("vivixiaoyan");
						// 在此设置语音播报的人选
						player.playText("您的手机未安装此应用！", "ent=vivi21,bft=5", null);
					}
				} else if (result.text.contains("笑话")) {
					AlertDialog.Builder builder = new Builder(MainActivity.this);
					builder.setTitle("笑话");
					builder.setIcon(R.drawable.ic_launcher);
					switch ((int) (Math.random() * 3)) {
					case 0: {
						builder.setMessage("一男和一女天天斗嘴，一天女的对男的说：我们停战吧! 男：停战?怎么停?女：我们依照历史惯例吧! 男：和亲?");
						AlertDialog dialog = builder.create();
						dialog.show();
					}
						return;
					case 1: {
						builder.setMessage("回老家，帮大伯家干活，他问我专业课学的什么，我才大二，专业课没接触到太多，思前想后就学了个电路和专业有关，就说电路。");
						AlertDialog dialog = builder.create();
						dialog.show();
					}
						return;
					case 2: {
						builder.setMessage("吕伟是帅哥！");
						AlertDialog dialog = builder.create();
						dialog.show();
					}
						return;
					default:
					}
				}/*
				 * else { // 如果没有相应指令就用Toast提示用户
				 * Toast.makeText(MainActivity.this, "无法识别的指令。",
				 * Toast.LENGTH_SHORT).show(); }
				 */
			}

		}

		@Override
		public void onEnd(SpeechError error) {
			if (error == null) {
				// 完成后就把结果显示在EditText上
				et.setText(text);
			}
		}
	};

	protected void onStop() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(MainActivity.this, MusicServer.class);
		stopService(intent);
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		setUpSearchView(menu);
		return true;
	}

	private void setUpSearchView(Menu menu) {
		// 获取SearchView对象
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();
		// showInfo("searchView : " + searchView);
		if (searchView == null) {
			Toast.makeText(getApplicationContext(), "Fail to get Search View.",
					Toast.LENGTH_SHORT).show();
			return;
		}
		// 缺省值就是true，可能不专门进行设置，false和true的效果图如下，true的输入框更大
		searchView.setIconifiedByDefault(true);
		// 获取搜索服务管理器
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		// showInfo("searchManager : " + searchManager);
		// searchable activity的component name，由此系统可通过intent进行唤起
		ComponentName cn = new ComponentName(this, SearchResultActvity.class);
		// showInfo("ComponentName : " + cn);
		// 通过搜索管理器，从searchable
		// activity中获取相关搜索信息，就是searchable的xml设置。如果返回null，表示该activity不存在，或者不是searchable
		SearchableInfo info = searchManager.getSearchableInfo(cn);
		if (info == null) {
			Toast.makeText(getApplicationContext(), "Fail to get search info.",
					Toast.LENGTH_SHORT).show();
		} else {
			// showInfo(info.toString());
			// 将searchable activity的搜索信息与search view关联
			searchView.setSearchableInfo(info);
			// progressDialog.dismiss();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		/*
		 * if (id == R.id.action_settings) { return true; }
		 */
		switch (id) {
		case R.id.action_about:
			startActivity(new Intent(MainActivity.this, AboutActivity.class));
			break;
		case R.id.action_exit:
			finish();
			break;
		case R.id.action_settings:

			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
