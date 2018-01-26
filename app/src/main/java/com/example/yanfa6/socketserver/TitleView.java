package com.example.yanfa6.socketserver;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by yanfa6 on 2017/4/13.
 */
public class TitleView extends RelativeLayout{
    private Button left_button;
    private TextView title_tv;


    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //加载布局
        LayoutInflater.from(context).inflate(R.layout.tital_bar, this);

        //初始化控件
        title_tv = (TextView) findViewById(R.id.tv_title);
        left_button = (Button) findViewById(R.id.bt_left);


    }
    //为控件添加方法
    public void setOnLeftButtonListen(OnClickListener listen){
        left_button.setOnClickListener(listen);
    }

    public void setTitleName(String title){
        title_tv.setText(title);

    }

}
