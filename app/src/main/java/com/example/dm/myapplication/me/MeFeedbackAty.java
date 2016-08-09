package com.example.dm.myapplication.me;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dm.myapplication.R;
import com.example.dm.myapplication.beans.AppUser;
import com.example.dm.myapplication.beans.Feedback;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by dm on 16-4-17.
 * 意见反馈
 */
public class MeFeedbackAty extends Activity implements View.OnClickListener {
    private ImageView titleImv;
    private EditText feedbackEt;
    private TextView feedbackHasNumTv;
    private Button feedbackSubmitBtn;
    int maxLength = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_feedback_layout);

        initView();
        setUpListener();
    }


    private void initView() {
        titleImv = (ImageView) findViewById(R.id.title_imv);
        feedbackEt = (EditText) findViewById(R.id.me_feedback_et);
        feedbackHasNumTv = (TextView) findViewById(R.id.me_feedback_hasnum_tv);
        feedbackSubmitBtn = (Button) findViewById(R.id.me_feedback_submit_btn);
    }

    private void setUpListener() {
        titleImv.setOnClickListener(MeFeedbackAty.this);
        feedbackEt.addTextChangedListener(mTextWatcher);
        feedbackEt.setSelection(feedbackEt.length());
        feedbackSubmitBtn.setOnClickListener(MeFeedbackAty.this);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        private int editStart;
        private int editEnd;
        private CharSequence charSequence;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            charSequence = s;
        }

        @Override
        public void afterTextChanged(Editable s) {
            int number = maxLength - s.length();
            feedbackHasNumTv.setText("" + number);
            editStart = feedbackEt.getSelectionStart();
            editEnd = feedbackEt.getSelectionEnd();

            if (charSequence.length() > maxLength) {
                s.delete(editStart - 1, editEnd);
                int tempSelection = editEnd;
                feedbackEt.setText(s);
                feedbackEt.setSelection(tempSelection);
            }
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_imv:
                MeFeedbackAty.this.finish();
                break;
            case R.id.me_feedback_submit_btn:
                String feedbackStr = feedbackEt.getText().toString();
                if ("".equals(feedbackStr) || feedbackStr == null) {
                    Toast.makeText(MeFeedbackAty.this, "请填写你的建议", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFeedbackDatas(feedbackStr);
                }
                break;
            default:
                break;
        }
    }

    private void uploadFeedbackDatas(String feedbackStr) {
        AppUser appUser = BmobUser.getCurrentUser(MeFeedbackAty.this, AppUser.class);
        Feedback feedback = new Feedback();

        if (appUser != null) {
            feedback.setUserId(appUser.getObjectId());
            feedback.setFeebackUserName(appUser.getUsername());
            feedback.setFeedbackUserEmail(appUser.getEmail());
            feedback.setFeedbackContent(feedbackStr);
            feedback.save(MeFeedbackAty.this, new SaveListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(MeFeedbackAty.this, "数据上传成功, 感谢你的建议!",
                            Toast.LENGTH_SHORT).show();
                    MeFeedbackAty.this.finish();
                }

                @Override
                public void onFailure(int i, String s) {
                    Toast.makeText(MeFeedbackAty.this, "上传数据失败, 请稍后再试!",
                            Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(MeFeedbackAty.this, "上传数据失败, 请稍后再试!",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
