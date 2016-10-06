package com.ahjz.deveb.login;

import com.ahjz.deveb.MainActivity;
import com.ahjz.deveb.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class RegisterActivity extends Activity {
	private EditText username;
	private EditText password;
	private EditText confirm_password;
	private EditText email;
	private int radiobutton;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
			RadioGroup rdgGroup=(RadioGroup) findViewById(R.id.rg_sex);
		rdgGroup.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				radiobutton = arg0.getCheckedRadioButtonId();
				RadioButton rButton = (RadioButton) findViewById(radiobutton);
				}
		});
	
}
	public void back(View view){
		Intent i=new Intent();
		i.setClass(this,LoginActivity.class);
		startActivity(i);
		overridePendingTransition(R.anim.activity_close,0);
		finish();
	}
	public void reg(View view){
		username = (EditText) this.findViewById(R.id.reg_username);
		password = (EditText) this.findViewById(R.id.reg_password);
		email = (EditText) this.findViewById(R.id.reg_email);
		confirm_password = (EditText) this.findViewById(R.id.reg_confirm_password);
		String username2 = username.getText().toString().trim();
		String email2 = email.getText().toString().trim();
		String password2 = password.getText().toString().trim();
		String password3 = confirm_password.getText().toString().trim();
		if (TextUtils.isEmpty(username2) || TextUtils.isEmpty(email2) || TextUtils.isEmpty(password2) || TextUtils.isEmpty(password3)) {
			Toast.makeText(this, "请完善注册信息！", Toast.LENGTH_SHORT).show();
		}else if (password2.equals(password3)) {
			Toast.makeText(this, "注册成功！", Toast.LENGTH_SHORT).show();
			Intent it=new Intent();
			it.setClass(this,MainActivity.class);
			startActivity(it);
			overridePendingTransition(R.anim.activity_open,0);
			finish();
		}else {
			Toast.makeText(this, "两次密码输入不一致！", Toast.LENGTH_SHORT).show();
		}
	
}
}