package com.ahjz.deveb;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.ahjz.deveb.login.LoginActivity;
import com.ahjz.deveb.service.HttpRequestTools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class WeatherActivity extends Activity {

    //注册完成后获取到的apikey
    private static final String apiKey="fa9fe7b1dee53f98eb0ad5ad3145e1ba";
    //上传请求的地址信息
    private String httpUrl = "http://apis.baidu.com/apistore/weatherservice/cityname";
    //用户输入城市名称信息
    private String httpArg = null;
    private Handler handler=null;
    private EditText cityET;
    private Button search;
    private TextView showInfo;
    private ImageView weatherImg;
    private String cityName=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        InitEvents();
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                try {
                    //注意在api文档中他没有提及直接用汉字传输信息是不行的,这里需要将用户输入的城市信息进行网络编码
                    //例如北京编码完成后就变成了%E5%8C%97%E4%BA%AC
                    cityName=(String)URLEncoder.encode(cityET.getText().toString(),"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                httpArg="cityname="+cityName;
                //System.out.println(httpArg);
                //发送获取天气请求
                HttpRequestTools httpRequestTools=new HttpRequestTools(httpUrl,httpArg,apiKey,showInfo,weatherImg,handler);
                httpRequestTools.start();
             // 关闭软键盘
        		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        		// 得到InputMethodManager的实例
        		if (imm.isActive())
        		{
        			// 如果开启
        			imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
        					InputMethodManager.HIDE_NOT_ALWAYS);
        			// 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
        		}
            }
        });
    }
	public void back(View view){
		Intent i=new Intent();
		i.setClass(this,MainActivity.class);
		startActivity(i);
		overridePendingTransition(R.anim.activity_open,0);
		finish();
	}
    public void InitEvents()
    {
        cityET=(EditText)findViewById(R.id.city_et);
        search=(Button)findViewById(R.id.search);
        showInfo=(TextView)findViewById(R.id.weatherInfo_tv);
        weatherImg=(ImageView)findViewById(R.id.weatherInfo_img);
        handler= new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };
    }
}