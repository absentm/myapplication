package com.example.dm.myapplication.me;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.dm.myapplication.R;

/**
 * MeSetingsAboutAty: about页面
 * Created by dm on 16-9-1.
 */
public class MeSetingsAboutAty extends Activity {
    private ImageButton titleLeftImv;
    private TextView titleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_settings_about_layout);

        initView();

    }

    private void initView() {
        titleLeftImv = (ImageButton) findViewById(R.id.me_settings_title).findViewById(R.id.title_imv);
        titleTv = (TextView) findViewById(R.id.me_settings_title).findViewById(R.id.title_center_text_tv);

        titleTv.setText("关于");
        titleLeftImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MeSetingsAboutAty.this.finish();
            }
        });
    }
}
