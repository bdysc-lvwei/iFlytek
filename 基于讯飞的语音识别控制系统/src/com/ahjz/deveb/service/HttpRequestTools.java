package com.ahjz.deveb.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.ahjz.deveb.R;

import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

public class HttpRequestTools extends Thread{
    //因为是网络请求(耗时任务)所以需要在新线程中完成数据的获取,所以这个工具类需要继承自Thread
    String httpUrl, httpArg ,apikey;
    TextView show;
    ImageView weatherImg;
    Handler handler=null;
    
   public HttpRequestTools(String httpUrl, String httpArg, String apikey,TextView tv,ImageView weatherImg,Handler handler)
    {
        this.httpUrl=httpUrl;
        this.httpArg=httpArg;
        this.apikey=apikey;
        this.show=tv;
        this.weatherImg=weatherImg;
        this.handler=handler;
    }
    @Override
    public void run() {
        super.run();
        //用来做数据缓冲
        StringBuffer sb=new StringBuffer();
        BufferedReader reader=null;
        String result=null;
        //安装api组合URL
        httpUrl=httpUrl+"?"+httpArg;
        try {

            URL url= new URL(httpUrl);
            //向服务器发送获取天气数据请求
            HttpURLConnection httpConnection=(HttpURLConnection)url.openConnection();
            httpConnection.setRequestMethod("GET");
            httpConnection.setRequestProperty("apikey",apikey);
            httpConnection.connect();
            //链接完成后就将数据保存到数据流中,这里是怕回传的数据如果直接按文本输出有可能是乱码,所以这里要重新编码一下
            InputStream in=httpConnection.getInputStream();
            reader=new BufferedReader(new InputStreamReader(in,"UTF-8"));
            String str;
            while((str=reader.readLine())!=null)
            {
                sb.append(str + "\n");
            }
            //数据接收完毕,先关闭上次流
            reader.close();
            //再关闭底层流
            in.close();
            result=sb.toString();
            //回传回来的数据是Json格式我们需要将关键信息提取出来
            AnalyseTools analyseTools=new AnalyseTools(result);
            final String finalResult = analyseTools.AnalyseText();
            //System.out.println(finalResult);
            //更新数据
            handler.post(new Runnable() {
                @Override
                public void run() {
                    show.setText(finalResult);
                    //按照天气配置图片
                    if(finalResult.contains("多云"))
                    {
                        weatherImg.setImageResource(R.drawable.cloud);
                    }else if(finalResult.contains("晴"))
                    {
                        weatherImg.setImageResource(R.drawable.sunny);
                    }else if(finalResult.contains("雨"))
                    {
                        weatherImg.setImageResource(R.drawable.rain);
                    }else if(finalResult.contains("阴"))
                    {
                        weatherImg.setImageResource(R.drawable.cloudy);
                    }else if(finalResult.contains("雪"))
                    {
                        weatherImg.setImageResource(R.drawable.snow);
                    }
                }
            });

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
