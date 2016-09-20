package com.example.dm.myapplication.utiltools;

import android.os.Environment;

/**
 * AbsentMConstants
 * Created by dm on 16-9-13.
 */
public class AbsentMConstants {
    // SharedPreferences属性信息文件
    public static final String EXTRA_ABSENTM_SHARE = "extra_absentm_shared_preferences_file";

    // 保存的自定义二维码的logo path
    public static final String QRCODE_LOGO_PATH = "qrcode_logo_path";

    // 保存的二维码前景色
    public static final String FORE_COLOR = "foreground_color";

    // 保存的二维码背景色
    public static final String BACK_COLOR = "background_color";

    // 话题图片临时上传路径
    public static final String TEMP_IMAGES_UPLOAD_DIR = Environment.getExternalStorageDirectory() + "/AbsentM/tempImagesDir/";
}
