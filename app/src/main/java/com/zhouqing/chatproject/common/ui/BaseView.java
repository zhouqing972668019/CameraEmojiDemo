package com.zhouqing.chatproject.common.ui;


public interface BaseView<T extends BasePresenter> {

    public void setPresenter(T presenter);
}
