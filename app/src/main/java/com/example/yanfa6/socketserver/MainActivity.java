package com.example.yanfa6.socketserver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements CompoundButton.OnCheckedChangeListener{
    public static final String TAG = "MainActivity";
    ToggleButton toggleButton;
    TextView textView;
    Intent intent;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        new customView(this);

         intent = new Intent(this, WebService.class);
        Log.i(TAG, "------->>>"+"初始化完毕"+intent.toString());
    }

    private void initView() {
        toggleButton= (ToggleButton) findViewById(R.id.togbtnOpen);
        toggleButton.setOnCheckedChangeListener(this);
        textView= (TextView) findViewById(R.id.txtUrl);
    }
    public void clickButton(View v){
        switch (v.getId()){
            case R.id.goCombinationActivity:
                Intent intent = new Intent(MainActivity.this,CombinationActivity.class);
                startActivity(intent);
                break;

        }
//        Toast.makeText(this,"ddd"+Looper.getMainLooper(),Toast.LENGTH_LONG).show();
//        Message msg=Message.obtain();
//        msg.what=111;
//        handler.sendMessage(msg);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Looper.prepare();
//                Looper.myLooper().quit();
//                Looper.loop();
//            }
//        }).start();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (isChecked) {
            String ip = getLocalIpAddress();
            if (ip == null) {
                textView.setText("");
            } else {
                startService(intent);
                Log.i(TAG, "------->>>"+"WebService起动");
                textView.setText("http://" + ip + ":" + WebService.PORT + "/");
            }
        } else {
            stopService(intent);
            Log.i(TAG, "------->>>"+"WebService关闭"+intent.toString());
            textView.setText("");
        }
    }

    private String getLocalIpAddress() {
        WifiManager wifimanager = (WifiManager)this.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiinfo = (WifiInfo)wifimanager.getConnectionInfo();
        try {
            int i = wifiinfo.getIpAddress();
            //返回IP 地址
            return (i&0xff)+"."+((i>>8)&0xff)+"."+((i>>16)&0xff)+"."+((i>>24)&0xff);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
