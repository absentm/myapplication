package com.example.dm.myapplication.find;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.example.dm.myapplication.R;
import com.example.dm.myapplication.customviews.RoundImageView;
import com.example.dm.myapplication.find.zxing.encoding.EncodingUtils;
import com.example.dm.myapplication.utiltools.AbsentMConstants;
import com.example.dm.myapplication.utiltools.BimpUtil;
import com.example.dm.myapplication.utiltools.SystemUtils;

/**
 * FindGenerateCodeAty
 * Created by dm on 16-9-13.
 */
public class FindGenerateCodeAty extends Activity implements View.OnClickListener {
    private static final String LOG_TAG = "FindGenerateCodeAty";

    private ImageButton mBackIbtn;
    private ImageButton mAddSettingsIbtn;
    private EditText mQrCodeInfoEt;
    private RoundImageView mQrLogoImv;
    private ToggleButton mToggleButton;
    private Button mGenereteBtn;
    private ImageView mGenerateQrImv;

    private int mForeColor;     // 二维码前景色：默认黑色
    private int mBackColor;     // 二维码背景色：默认白色
    private String mLogoPathStr;    // 二维码片保存地址
    private boolean isQRcodeGenerated;  // 是否生成

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_layout);

        initQrBaseValues();
        initView();
    }

    /**
     * 获取保存的自定义二维码logo地址、前景色、背景色
     */
    private void initQrBaseValues() {
        SharedPreferences share = getSharedPreferences(
                AbsentMConstants.EXTRA_ABSENTM_SHARE, Activity.MODE_PRIVATE);
        mLogoPathStr = share.getString(AbsentMConstants.QRCODE_LOGO_PATH, null);
        mForeColor = share.getInt(AbsentMConstants.FORE_COLOR, 0xff000000);
        mBackColor = share.getInt(AbsentMConstants.BACK_COLOR, 0xffffffff);
    }

    private void initView() {
        mBackIbtn = (ImageButton) findViewById(R.id.generate_title_left_ibtn);
        mAddSettingsIbtn = (ImageButton) findViewById(R.id.generate_title_right_ibtn);
        mQrCodeInfoEt = (EditText) findViewById(R.id.generate_qr_code_et);
        mQrLogoImv = (RoundImageView) findViewById(R.id.generate_logo_imv);
        mToggleButton = (ToggleButton) findViewById(R.id.generate_logo_tgbtn);
        mGenereteBtn = (Button) findViewById(R.id.generate_qr_code_btn);
        mGenerateQrImv = (ImageView) findViewById(R.id.generate_result_imv);

        if (mLogoPathStr != null) {
            mQrLogoImv.setImageBitmap(BitmapFactory.decodeFile(mLogoPathStr));
        }

        mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isQRcodeGenerated) {
                    generateQRcode();
                }
            }
        });

        mBackIbtn.setOnClickListener(FindGenerateCodeAty.this);
        mAddSettingsIbtn.setOnClickListener(FindGenerateCodeAty.this);
        mGenereteBtn.setOnClickListener(FindGenerateCodeAty.this);
        mGenereteBtn.setClickable(false);

        mQrCodeInfoEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressWarnings("deprecation")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    mGenereteBtn.setClickable(false);
                    mGenereteBtn.setBackgroundResource(R.drawable.item_teal_selector);
                    mGenereteBtn.setTextColor(getResources().getColor(R.color.gray700));
                } else {
                    mGenereteBtn.setClickable(true);
                    mGenereteBtn.setBackgroundResource(R.drawable.item_teal_selector);
                    mGenereteBtn.setTextColor(getResources().getColor(R.color.white));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void generateQRcode() {
        String contentString = mQrCodeInfoEt.getText().toString();
        Bitmap logoBitmap = null;

        if (mToggleButton.isChecked()) {
            if (mLogoPathStr != null) {
                // 自定义logo图标
                logoBitmap = BitmapFactory.decodeFile(mLogoPathStr);
            }

            if (logoBitmap == null) {
                // 默认logo为应用图标
                logoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.app_icon);
            }

            // 生成圆形Logo
            logoBitmap = BimpUtil.toRoundBitmap(logoBitmap);
        }

        int size = SystemUtils.dip2px(this, 320);
        //根据字符串生成二维码图片并显示在界面上，第2, 3个参数为图片宽高
        Bitmap qrCodeBitmap = EncodingUtils.createQRCode(contentString, size, size, logoBitmap, mForeColor, mBackColor);
        mGenerateQrImv.setImageBitmap(qrCodeBitmap);
        isQRcodeGenerated = true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.generate_title_left_ibtn:
                FindGenerateCodeAty.this.finish();
                break;
            case R.id.generate_title_right_ibtn:
                break;
            case R.id.generate_qr_code_btn:
                generateQRcode();
                break;
        }
    }
}
