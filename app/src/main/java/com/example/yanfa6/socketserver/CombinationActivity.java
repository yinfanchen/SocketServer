package com.example.yanfa6.socketserver;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class CombinationActivity extends Activity {
    private TitleView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combination);
        titleView = (TitleView) findViewById(R.id.title_bar);
        titleView.setOnLeftButtonListen(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CombinationActivity.this,"你点击了返回键",Toast.LENGTH_LONG).show();
            }
        });
    }
}
