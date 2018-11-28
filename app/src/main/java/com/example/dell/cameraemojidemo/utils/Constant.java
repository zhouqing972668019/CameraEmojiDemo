package com.example.dell.cameraemojidemo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DELL on 2018/11/28.
 */

public class Constant {

    public static String IMG_PATH
            = Environment.getExternalStorageDirectory() + "/CameraEmojiDemo/Images/";
    //摄像头id（通常0代表后置摄像头，1代表前置摄像头）
    public static String mCameraId = "1";

    public static final String SPNAME = "initializationInfo";


    //写设置界面的选项到sharedPreferences
    public static void saveSettingSelectionToFile(Context context, Integer picResolutionPos){
        SharedPreferences sp = context.getSharedPreferences(SPNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if(picResolutionPos != null)editor.putInt("picResolutionPos",picResolutionPos);
        editor.commit();
    }

    //从sharedPreferences中读取设置界面选项
    public static Map<String,Integer> getSettingSelection(Context context){
        Map<String, Integer> data = new HashMap<>();
        SharedPreferences sp = context.getSharedPreferences(SPNAME, Context.MODE_PRIVATE);
        data.put("picResolutionPos",sp.getInt("picResolutionPos",0));
        return data;
    }
}
