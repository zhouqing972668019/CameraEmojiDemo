package com.zhouqing.chatproject.manage;

import android.app.Activity;
import android.database.Cursor;

import com.zhouqing.chatproject.common.util.ThreadUtil;
import com.zhouqing.chatproject.provider.SmsProvider;
import com.zhouqing.chatproject.service.IMService;

public class ManagePresenter implements ManageContract.Presenter{
    private Activity mAcitivity;
    private ManageContract.View mView;
    public ManagePresenter(Activity acitivity, ManageContract.View view) {
        this.mAcitivity = acitivity;
        this.mView = view;
    }
    @Override
    public void getContact() {
        ThreadUtil.runOnThread(new Runnable() {
            @Override
            public void run() {
                // 对应查询记录
                final Cursor cursor = mAcitivity.getContentResolver().query(SmsProvider.URI_SESSION,
                        null, null, new String[]{IMService.ACCOUNT}, null);

                // 设置adapter,然后显示数据
                ThreadUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 假如没有数据的时候
                        mView.showSession(cursor);
                    }

                });
            }
        });
    }

    @Override
    public void getDialogueMessage(final String clickAccout) {
        ThreadUtil.runOnThread(new Runnable() {
            @Override
            public void run() {
                final Cursor c = mAcitivity.getContentResolver().query(
                        SmsProvider.URI_SMS, null,
                        "(from_account = ? and to_account = ?) or (from_account = ? and to_account = ?)",
                        new String[]{IMService.ACCOUNT, clickAccout, clickAccout, IMService.ACCOUNT}, null);

                //显示聊天消息
                ThreadUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mView.showDialogueMessage(c);
                    }
                });
            }
        });
    }
}
