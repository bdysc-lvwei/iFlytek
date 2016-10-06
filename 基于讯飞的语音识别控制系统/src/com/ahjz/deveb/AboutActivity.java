package com.ahjz.deveb;

import com.ahjz.deveb.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class AboutActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	     final View view = View.inflate(this, R.layout.activity_about, null);
	        setContentView(view);
	     
	}
	   public void onClick(View v){
		   startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.ahjzu.edu.cn")));
         finish();
       } 
}
