package com.ahjz.deveb;

import java.util.List;

import com.ahjz.deveb.app.AllApplication;
import com.ahjz.deveb.app.AppInfo;
import com.ahjz.deveb.service.PingyinUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class SearchResultActvity extends Activity {
	// private TextView tv = null;
	private String appPac;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_test);
		/*
		 * tv = (TextView) findViewById(R.id.textViewId); tv.setText("");
		 */

		showInfo("onCreate() is called");
		doSearchQuery(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		showInfo("onNewIntent() is called");
		super.onNewIntent(intent);
		doSearchQuery(intent);
	}

	private void doSearchQuery(Intent intent) {
		// showInfo(" doSearchQuery() is called");
		if (intent == null)
			return;
		String queryAction = intent.getAction();
		if (Intent.ACTION_SEARCH.equals(queryAction)) {
			String queryString = intent.getStringExtra(SearchManager.QUERY);
			// showInfo("搜索内容：" + queryString);
			openApp(queryString);
		
		}
	}

	private void openApp(String queryString) {
		// TODO Auto-generated method stub
		ProgressDialog progressDialog = new ProgressDialog(
				SearchResultActvity.this);
		progressDialog.setTitle("正在为您打开应用");
		progressDialog.setMessage("Loading...");
		progressDialog.setCancelable(false);
		progressDialog.show();
		// 打开应用
		List<AppInfo> appInfos = AllApplication
				.getAllApplications(SearchResultActvity.this);
		// 采用首次匹配原则
		for (AppInfo appInfo : appInfos) {
			String label = appInfo.getLabel();
			String pacName = appInfo.getPackageName();
			if (PingyinUtils.converterToSpell(label).contains(
					PingyinUtils.converterToSpell(queryString))
					|| PingyinUtils.converterToSpell(queryString).contains(
							PingyinUtils.converterToSpell(label))) {
				appPac = pacName;
				//break;
				Intent inte = getPackageManager().getLaunchIntentForPackage(appPac);
				startActivity(inte);
				finish();
			}/*else {
				Toast.makeText(getApplicationContext(), "未安装您搜索的应用："+label,Toast.LENGTH_SHORT).show();
			}*/
		}
		
	}

	private void showInfo(String s) {
		/* tv.setText(s + "\n" + tv.getText()); */
		Log.d(getLocalClassName(), s);
	}
}