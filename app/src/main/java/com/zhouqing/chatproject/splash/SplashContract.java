package com.zhouqing.chatproject.splash;

import com.zhouqing.chatproject.common.ui.BasePresenter;
import com.zhouqing.chatproject.common.ui.BaseView;

/**
 * Created by liqingfeng on 2017/5/22.
 */

public class SplashContract {
    public interface View extends BaseView<Presenter> {

    }

    public interface Presenter extends BasePresenter {
        void endSplash();
    }
}
