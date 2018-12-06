package com.zhouqing.chatproject.main;

import android.database.Cursor;

import com.zhouqing.chatproject.common.ui.BasePresenter;
import com.zhouqing.chatproject.common.ui.BaseView;

/**
 * Created by liqingfeng on 2017/5/22.
 */

public class MessageContract {
    public interface Presenter extends BasePresenter {
        void getContact();

        void chat(String account);

        void deleteSession(String clickAccount);
    }

    public interface View extends BaseView<Presenter> {
        void showContact(Cursor cursor);
    }
}
