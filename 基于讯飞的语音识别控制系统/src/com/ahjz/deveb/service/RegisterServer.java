package com.ahjz.deveb.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class RegisterServer {
	public static void saveUserInfo(Context context, String username,
			String password, String emal,
			String sex, String confirm_password) {
		//SharedPreferences接口
		SharedPreferences	spr=context.getSharedPreferences("config", Context.MODE_PRIVATE);
		//得到一个Sp编辑器
		Editor editor=spr.edit();
		editor.putString("username", username);
		editor.putString("password", password);
		editor.putString("emal", emal);
		editor.putString("sex", sex);
		editor.putString("confirm_password", confirm_password);
		//类似于数据库的事务 保证数据同时被提交
		editor.commit();
	}
}