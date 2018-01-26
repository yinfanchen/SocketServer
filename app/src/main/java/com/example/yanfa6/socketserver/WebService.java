package com.example.yanfa6.socketserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by yanfa6 on 2017/3/13.
 */
public class WebService extends Service{

    public static final int PORT = 7766;
    public static final String TAG = "WebService";
    private WebServer webServer;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        webServer=new WebServer(PORT);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        webServer.setDaemon(true);
        webServer.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
    }
}
