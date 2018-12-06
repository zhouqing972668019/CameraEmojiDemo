package com.zhouqing.chatproject.common.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    public static void showToastSafe(final Context context, final String text){
        ThreadUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public static void showToast(Context context, String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
