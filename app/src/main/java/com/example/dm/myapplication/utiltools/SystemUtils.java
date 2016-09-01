package com.example.dm.myapplication.utiltools;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dm on 16-4-7.
 * 系统工具类
 */
public class SystemUtils {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static boolean checkNetworkConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activityNetwork = cm.getActiveNetworkInfo();
        return activityNetwork != null && activityNetwork.isConnected();
    }

    public static void noNetworkAlert(Context context) {
        Toast.makeText(context, "No Network", Toast.LENGTH_SHORT).show();
    }

    /**
     * 鏍￠獙鏄惁鏄墜鏈哄彿
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);

        return m.matches();
    }


    /**
     * 鏍￠獙鏄惁鏄偖绠卞湴鍧�
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    /**
     * 鏍￠獙鏄惁鏄疷RL鍦板潃
     *
     * @param urlStr
     * @return
     */
    public static boolean isUrl(String urlStr) {
        String str = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(urlStr);

        return m.matches();
    }


    /**
     * 妫�鏌ユ槸鍚︽湁缃戠粶杩炴帴
     *
     * @param context
     */
    public static void checkNetWork(Context context) {
        ConnectivityManager con = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        boolean internet = con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();

        if (!wifi && !internet) {
            Toast.makeText(context.getApplicationContext(), "亲， 检测到网络有问题，请设置！", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context.getApplicationContext(), "网络连接正常！", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 妫�鏌ユ槸鍚ifi缃戣矾
     *
     * @param context
     * @return true/false
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager con = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();

        if (wifi) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 妫�鏌ユ槸鍚︽墜鏈烘祦閲忕綉缁�
     *
     * @param context
     * @return true/false
     */
    public static boolean isInternet(Context context) {
        ConnectivityManager con = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        boolean internet = con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();

        if (internet) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 高的地图定位API的SHA值获取
     *
     * @param context context
     * @return String
     */
    public static String aMapSHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }

            String result = hexString.toString();

            return result.substring(0, result.length() - 1);
        } catch (PackageManager.NameNotFoundException e) {
            Log.i("LOG", e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            Log.i("LOG", e.getMessage());
        }

        return null;
    }

    /**
     * 设置listView回滚至顶部
     * <p/>
     * 缺点是：滑动数据量很大时，回滚时间会比较长。改进办法是将handler里的这两行代码：
     * listView.smoothScrollToPosition(0);
     * handler.postDelayed(this, 100);
     * <p/>
     * 换成：
     * listView.setSelection(0);再根据需要修改间隔时间（100毫秒），
     *
     * @param listView tem单一样式的，多种样式的都可以
     */
    public static void scrollToListviewTop(final AbsListView listView) {
        listView.smoothScrollToPosition(0);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (listView.getFirstVisiblePosition() > 0) {
                    listView.smoothScrollToPosition(0);
//                    listView.setSelection(0);
                    handler.postDelayed(this, 100);
                }
            }
        }, 100);
    }

    /**
     * 异步执行toast
     * @param context context
     * @param message Sting
     */
    public static void showHandlerToast(final Context context, final String message) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        }, 100);
    }

}
