package com.ahjz.deveb.login;
import com.ahjz.deveb.MainActivity;
import com.ahjz.deveb.R;
import com.ahjz.deveb.service.LoginServer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private EditText et_username;
	private EditText et_password;
	private CheckBox cb;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		et_username = (EditText) this.findViewById(R.id.et_username);
		et_password = (EditText) this.findViewById(R.id.et_password);
		cb = (CheckBox) findViewById(R.id.cb_remember_pwd);
		SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
		String username = sp.getString("username", "");
		et_username.setText(username);
	//	overridePendingTransition(R.anim.slide_in_from_right, 0);
	}
public void login(View view) {
	
		String username = et_username.getText().toString().trim();
		String password = et_password.getText().toString().trim();
		Intent i=new Intent();
		if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
			Toast.makeText(this, "请完善登录信息", Toast.LENGTH_SHORT).show();
		} else {
			if (cb.isChecked()) {
				LoginServer.saveUserInfo(this, username, password);
			}
			if ("lvwei".equals(username) && "940317".equals(password)) {
				i.setClass(this, MainActivity.class);
				startActivity(i);
				finish();
			} else {
				Toast.makeText(this, "禁止非法用户登录哦！", Toast.LENGTH_SHORT).show();
				i.setClass(this, LoginActivity.class);
				startActivity(i);
				finish();
			}
		}
	}

public void back(View view){
	overridePendingTransition(R.anim.activity_close,0);
	finish();
}
public void register(View view){
	Intent it=new Intent();
	it.setClass(this,RegisterActivity.class);
	startActivity(it);
	overridePendingTransition(R.anim.activity_open,0);
	finish();
}

}



