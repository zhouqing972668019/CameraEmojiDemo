package com.zhouqing.chatproject.main;

import android.database.Cursor;

import com.zhouqing.chatproject.common.ui.BasePresenter;
import com.zhouqing.chatproject.common.ui.BaseView;


public class ContactContract {
    public interface Presenter extends BasePresenter {
        void getContact();
    }

    public interface View extends BaseView<Presenter> {
        void showContact(Cursor cursor);
    }
}
