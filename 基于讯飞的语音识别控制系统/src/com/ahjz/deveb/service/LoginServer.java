package com.ahjz.deveb.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class LoginServer {
	public static void saveUserInfo(Context context, String username,
			String password) {
		//SharedPreferences接口
		SharedPreferences	sp=context.getSharedPreferences("config", Context.MODE_PRIVATE);
		//得到一个Sp编辑器
		Editor editor=sp.edit();
		editor.putString("username", username);
		editor.putString("password", password);
		//类似于数据库的事务 保证数据同时被提交
		editor.commit();
	}
}
