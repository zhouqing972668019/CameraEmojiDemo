package com.zhouqing.chatproject.common.constant;


import com.zhouqing.chatproject.common.AppApplication;
import com.zhouqing.chatproject.common.util.SPUtil;
public class Global {
    public static final String HOST = (String) SPUtil.get(AppApplication.getInstance(), "ip", "47.107.244.194");
    public static final int PORT = 5222;
}
