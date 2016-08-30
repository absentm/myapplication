package com.example.dm.myapplication.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dm.myapplication.R;
import com.example.dm.myapplication.beans.AppUser;
import com.example.dm.myapplication.me.MeFavouritrActivity;
import com.example.dm.myapplication.me.MeFeedbackAty;
import com.example.dm.myapplication.me.MeInfoDetailActivity;
import com.example.dm.myapplication.me.MeSettingsAty;
import com.example.dm.myapplication.utiltools.HttpUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.BmobUpdateListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateResponse;
import cn.bmob.v3.update.UpdateStatus;


/**
 * Created by dm on 16-3-29.
 * 第四个页面
 */
public class FourthFragment extends Fragment {
    private static final String LOG = "LOG";

    private static final int REQUEST_CODE = 1;

    private RelativeLayout meInfoRout;
    private ImageView meAvatarImv;
    private TextView meNickNameTv;
    private TextView meMessageTv;

    private RelativeLayout meCollectRout;
    private RelativeLayout meFeedbackRout;
    private RelativeLayout meShareRout;
    private RelativeLayout meUpdateRout;
    private RelativeLayout meSettingsRout;


    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg4, container, false);

        initView();  // 控件初始化
        fillDatas();
        eventsDeal();  // 事件处理

        return view;
    }

    private void fillDatas() {
        AppUser appUser = BmobUser.getCurrentUser(AppUser.class);
        if (appUser == null) {
            meNickNameTv.setText("未登录, 请点击登录");
            meMessageTv.setText("这位童鞋很懒, 什么都没留下...");
            meAvatarImv.setImageResource(R.drawable.app_icon);
        } else {
            meNickNameTv.setText(appUser.getUserNickName());

            if ("".equals(appUser.getUserMessage()) || null == appUser.getUserMessage()) {
                meMessageTv.setText("这位童鞋很懒, 什么都没留下...");
            } else {
                meMessageTv.setText(appUser.getUserMessage());
            }

            Log.i(LOG, "appUser.getUserAvatarUrl() = " + appUser.getUserAvatarUrl());

            if ("".equals(appUser.getUserAvatarUrl()) || null == appUser.getUserAvatarUrl()) {
                meAvatarImv.setImageResource(R.drawable.app_icon);
            } else {
                ImageLoader.getInstance().displayImage(appUser.getUserAvatarUrl(), meAvatarImv,
                        HttpUtil.DefaultOptions);
            }
        }

        // 自动更新检测
        BmobUpdateAgent.setUpdateListener(new BmobUpdateListener() {
            @Override
            public void onUpdateReturned(int updateStatus, UpdateResponse updateResponse) {
                switch (updateStatus) {
                    case UpdateStatus.Yes:
                        Log.i("LOG", "YYYYYYYY");
                        break;
                    case UpdateStatus.No:
                        Log.i("LOG", "NNNNNNNNN");
                        Toast.makeText(getActivity(), "已是最新版本", Toast.LENGTH_SHORT).show();
                        break;
                    case UpdateStatus.TimeOut:
                        Log.i("LOG", "TTTTTTTTT");
                        Toast.makeText(getActivity(), "连接超时，请稍后再试",
                                Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Log.i("LOG", "FFFFFFFFF");
                        Toast.makeText(getActivity(), "连接超时，请稍后再试",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    /**
     * 初始化
     */
    private void initView() {
        meInfoRout = (RelativeLayout) view.findViewById(R.id.me_info_rout);
        meAvatarImv = (ImageView) view.findViewById(R.id.me_avatar_imv);
        meNickNameTv = (TextView) view.findViewById(R.id.me_nickname_tv);
        meMessageTv = (TextView) view.findViewById(R.id.me_message_tv);

        meCollectRout = (RelativeLayout) view.findViewById(R.id.me_collect_rout);
        meFeedbackRout = (RelativeLayout) view.findViewById(R.id.me_feedback_rout);
        meShareRout = (RelativeLayout) view.findViewById(R.id.me_share_rout);
        meUpdateRout = (RelativeLayout) view.findViewById(R.id.me_update_rout);
        meSettingsRout = (RelativeLayout) view.findViewById(R.id.me_settings_rout);
    }

    /**
     * 界面控件事件处理
     */
    private void eventsDeal() {
        meInfoRout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nicknameStr = meNickNameTv.getText().toString();
                String messageStr = meMessageTv.getText().toString();

                AppUser appUser = BmobUser.getCurrentUser(AppUser.class);
                if (appUser == null) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("FourthFragment.nickname", nicknameStr);
                    intent.putExtra("FourthFragment.messageStr", messageStr);
                    intent.setClass(getActivity(), MeInfoDetailActivity.class);
                    FourthFragment.this.startActivityForResult(intent, REQUEST_CODE);
                }
            }
        });

        meCollectRout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "我的收藏", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), MeFavouritrActivity.class));
            }
        });

        meFeedbackRout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MeFeedbackAty.class));
            }
        });

        meShareRout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "我的分享", Toast.LENGTH_SHORT).show();
            }
        });

        meUpdateRout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUpdateAgent.forceUpdate(getActivity());
            }
        });

        meSettingsRout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MeSettingsAty.class));
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == 1) {
            String nicknameStr = data.getStringExtra("MeInfoDetailActivity.nicknameStr");
            String messageStr = data.getStringExtra("MeInfoDetailActivity.messageStr");
            String avatarUrl = data.getStringExtra("MeInfoDetailActivity.avatarUrl");

            Log.i(LOG, "Four, nicknameStr = " + nicknameStr);
            Log.i(LOG, "Four, messageStr = " + messageStr);
            Log.i(LOG, "Four, avatarUrl = " + avatarUrl);
            meNickNameTv.setText(nicknameStr);
            meMessageTv.setText(messageStr);
            ImageLoader.getInstance().displayImage(avatarUrl, meAvatarImv, HttpUtil.DefaultOptions);
        }
    }
}
