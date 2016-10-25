package com.example.liuzhuang.mywuziqi;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
    private WuziqiPanel wuziqiPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wuziqiPanel = (WuziqiPanel) findViewById(R.id.wuziqi);

    }

    @Override
    public void onBackPressed() {

//        super.onBackPressed();
        wuziqiPanel.reStart();
    }
}
