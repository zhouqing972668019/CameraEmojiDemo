package com.zhouqing.chatproject.common.constant;


import com.zhouqing.chatproject.R;
import com.zhouqing.chatproject.common.AppApplication;
import com.zhouqing.chatproject.common.util.SPUtil;
public class Global {
    public static final String HOST = (String) SPUtil.get(AppApplication.getInstance(), "ip", "47.107.244.194");
    public static final int PORT = 5222;

    public static final int AVATAR_NUM = 25;

    public static Integer[] AVATARS = {
            R.drawable.avatar01,R.drawable.avatar02,R.drawable.avatar03,R.drawable.avatar04,R.drawable.avatar05,
            R.drawable.avatar06,R.drawable.avatar07,R.drawable.avatar08,R.drawable.avatar09,R.drawable.avatar10,
            R.drawable.avatar11,R.drawable.avatar12,R.drawable.avatar13,R.drawable.avatar14,R.drawable.avatar15,
            R.drawable.avatar16,R.drawable.avatar17,R.drawable.avatar18,R.drawable.avatar19,R.drawable.avatar20,
            R.drawable.avatar21,R.drawable.avatar22,R.drawable.avatar23,R.drawable.avatar24,R.drawable.avatar25
    };
}
