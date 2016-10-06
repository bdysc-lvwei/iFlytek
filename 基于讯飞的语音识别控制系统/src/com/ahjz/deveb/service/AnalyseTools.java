package com.ahjz.deveb.service;

import org.json.JSONException;
import org.json.JSONObject;

public class AnalyseTools {
    String receiveInfo=null;
    AnalyseTools(String JsonText)
    {
        receiveInfo=JsonText;
    }
    public String AnalyseText()
    {

        String weatherInfo=null;
        //先判断一下天气是否获取成功,如果没成功会有errNum:-1提示
        if(receiveInfo.contains("\"errNum\":-1"))
        {
            weatherInfo="请求天气数据失败,请稍后再试";
        }
        else {
            try {
                //使用Json来解析文本数据
                JSONObject jsonObject = new JSONObject(receiveInfo);
                //定位到retData关键词
                JSONObject json = (JSONObject) jsonObject.get("retData");
                StringBuffer sb = new StringBuffer();
                sb.append("城市: " + json.getString("city") + "\n");
                sb.append("日期: " + json.getString("date") + "\n");
                sb.append("发布时间: " + json.getString("time") + "\n");
                sb.append("天气情况: " + json.getString("weather") + "\n");
                sb.append("温度: " + json.getString("temp") + "\n");
                sb.append("最低气温: " + json.getString("l_tmp") + "\n");
                sb.append("最高气温: " + json.getString("h_tmp") + "\n");
                sb.append("风向: " + json.getString("WD") + "\n");
                sb.append("风力: " + json.getString("WS") + "\n");
                sb.append("日出时间: " + json.getString("sunrise") + "\n");
                sb.append("日落时间: " + json.getString("sunset") + "\n");
                weatherInfo = sb.toString();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return weatherInfo;
    }

}
