package com.zhouqing.chatproject.register;

import com.zhouqing.chatproject.common.AppApplication;
import com.zhouqing.chatproject.common.util.SPUtil;
import com.zhouqing.chatproject.common.util.ThreadUtil;
import com.zhouqing.chatproject.common.util.XmppUtil;


public class RegisterPresenter implements RegisterContract.Presenter {
    private RegisterContract.View mView;

    public RegisterPresenter(RegisterContract.View view) {
        this.mView = view;
        this.mView.setPresenter(this);
    }

    @Override
    public void register(final String username, final String password, final String ip) {
        ThreadUtil.runOnThread(new Runnable() {
            @Override
            public void run() {
                SPUtil.put(AppApplication.getInstance(),"ip",ip);
                boolean b = XmppUtil.conServer();
                if (!b) {
                    mView.serverError();
                    return;
                }
                int id = XmppUtil.regist(username, password);
                switch (id) {
                    case 0:
                        //服务器出现异常
                        mView.serverError();
                        break;
                    case 1:
                        ThreadUtil.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mView.registerSuccess(username, password,ip);
                            }
                        });
                        break;
                    case 2:
                        //用户已经存在
                        mView.userExit();
                        break;
                    case 3:
                        //注册失败
                        mView.registerFailure();
                        break;
                }
            }
        });
    }
}
