package com.zhouqing.chatproject.chat;

import android.database.Cursor;

import com.zhouqing.chatproject.common.ui.BasePresenter;
import com.zhouqing.chatproject.common.ui.BaseView;


public class ChatContract {
    public interface Presenter extends BasePresenter {
        void getDialogueMessage(String clickAccout);

        void sendMessage(String clickAccout,String facePic);

        void bindIMService();

        void unbindIMService();
    }

    public interface View extends BaseView<Presenter> {
        void showDialogueMessage(Cursor cursor);

        String getMessage();

        void clearMessage();
    }
}
