package com.example.dm.myapplication.customviews;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.dm.myapplication.R;

/**
 * Created by dm on 16-4-19.
 * 头像修改器
 */
public class CustomAvatarDialog extends Dialog {
    private Button btn_take_photo, btn_pick_photo, btn_cancel;

    public CustomAvatarDialog(Context context, View.OnClickListener itemsOnClick) {
        super(context, R.style.selectDataDialog);
        setContentView(R.layout.me_avatar_pop);

        btn_take_photo = (Button) findViewById(R.id.me_editor_avatar_takephoto_btn);
        btn_pick_photo = (Button) findViewById(R.id.me_editor_avatar_album_btn);
        btn_cancel = (Button) findViewById(R.id.me_editor_avatar_cancel_btn);

        // 取消按钮
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 销毁弹出框
                dismiss();
            }
        });
        // 设置按钮监听
        btn_pick_photo.setOnClickListener(itemsOnClick);
        btn_take_photo.setOnClickListener(itemsOnClick);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int height = this.findViewById(R.id.pop_layout).getTop();

        int y = (int) event.getY();
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (y < height) {
                dismiss();
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(true);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();

        layoutParams.x = 0;
        layoutParams.y = window.getWindowManager().getDefaultDisplay().getHeight();
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;

        window.setAttributes(layoutParams);
    }
}
