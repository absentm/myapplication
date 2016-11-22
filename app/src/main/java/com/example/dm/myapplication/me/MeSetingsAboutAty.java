package com.example.dm.myapplication.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.dm.myapplication.R;

/**
 * MeSetingsAboutAty: about页面
 * Created by dm on 16-9-1.
 */
public class MeSetingsAboutAty extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_settings_about_layout);

        initView();
    }

    private void initView() {
        ImageButton titleLeftImv = (ImageButton) findViewById(R.id.title_about_imv);
        ImageButton titleShareImv = (ImageButton) findViewById(R.id.title_about_right_ibtn);

        titleLeftImv.setOnClickListener(MeSetingsAboutAty.this);
        titleShareImv.setOnClickListener(MeSetingsAboutAty.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_about_imv:
                MeSetingsAboutAty.this.finish();
                break;
            case R.id.title_about_right_ibtn:
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT,
                        "AbsentM - App代码已开源: https://github.com/absentm/myapplication\n" +
                                "我的Github: https://github.com/absentm\n" +
                                "我的Blog: https://absentm.github.io\n");
                shareIntent.setType("text/plain");
                //设置分享列表的标题，并且每次都显示分享列表
                startActivity(Intent.createChooser(shareIntent, "分享到"));
                break;
        }
    }
}
