package com.example.yanfa6.socketserver;

import android.util.Log;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.RequestLine;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by yanfa6 on 2017/3/13.
 */
public class LightCtrl implements HttpRequestHandler {
    private static final String TAG = "HttpJsonlogin";

    public LightCtrl(){

    }
    @Override
    public void handle(HttpRequest request, HttpResponse response,
                       HttpContext context) throws HttpException, IOException {

        RequestLine requestLine = request.getRequestLine();
        String method=requestLine.getMethod();
        if("GET".equals(method)){
            doGet(request, response);
        }else{
            Log.i(TAG, "method is error:"+method.toString());
        }
    }
    /****************************************************************
     * urlCmd格式：http://loaclhost:port/DEVICE?device=num&operation=open(close)&clientId=phone
     * @param request
     * @param response
     */
    public void doGet(HttpRequest request, HttpResponse response){
        response.setStatusCode(HttpStatus.SC_OK);//响应成功
        response.setHeader("Content-Type", "text/json");
        response.setHeader("Content-Type", "charset=gbk");
        JSONObject json = new JSONObject();
        //************解析参数*************************************
        String urlCmd = request.getRequestLine().getUri();
        Log.i(TAG, "url："+urlCmd);
        int a = urlCmd.indexOf("device");
        a=a+7;
        int b = urlCmd.indexOf("&",a);
        String device = urlCmd.substring(a, b);

        a = urlCmd.indexOf("operation");
        a=a+10;
        b = urlCmd.indexOf("&",a);
        String operation = urlCmd.substring(a, b);

        a = urlCmd.indexOf("clientId");
        a=a+9;
        b = urlCmd.length();
        String clientId = urlCmd.substring(a, b);
        Log.i(TAG, "请求命令为："+device+operation+clientId);
        //根据命令做出响应

        StringEntity entity = null;
        try {
            json.put("device", device);
            json.put("operation", operation);
            json.put("clientId", clientId);
            json.put("result", "ok");
            Log.i(TAG,"响应JSON数据为："+json.toString());
            entity = new StringEntity(json.toString(),"UTF-8");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        response.setEntity(entity);
    }
}
