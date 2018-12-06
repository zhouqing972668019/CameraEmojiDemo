package com.zhouqing.chatproject.login;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhouqing.chatproject.R;
import com.zhouqing.chatproject.common.ui.BaseActivity;
import com.zhouqing.chatproject.common.ui.view.MyEditText;
import com.zhouqing.chatproject.common.util.SPUtil;
import com.zhouqing.chatproject.common.util.ToastUtil;




public class LoginActivity extends BaseActivity implements LoginContract.View {

    private LinearLayout linearLayout;
    private MyEditText mUsername;
    private MyEditText mPassword;
    private MyEditText mIP;
    private Button mLogin;
    private TextView mRegist;

    private LoginContract.Presenter mPresenter;

    @Override
    protected void initUi() {
        setContentView(R.layout.activity_login);
        linearLayout = (LinearLayout) findViewById(R.id.ll_login);
        mUsername = (MyEditText) findViewById(R.id.username);
        mPassword = (MyEditText) findViewById(R.id.password);
        mIP = (MyEditText) findViewById(R.id.ip);
        mLogin = (Button) findViewById(R.id.btn_login);
        mRegist = (TextView) findViewById(R.id.tv_regist);

        mPresenter = new LoginPresenter(mActivity, this);
        //开启动画
        //startAnimation();
    }

    @Override
    protected void initData() {
        //获取上次登录的账号密码
        String username = (String) SPUtil.get(LoginActivity.this, "username", "");
        String password = (String) SPUtil.get(LoginActivity.this, "password", "");
        String ip = (String) SPUtil.get(LoginActivity.this, "ip", "");
        mUsername.setText(username);
        mPassword.setText(password);
        mIP.setText(ip);


        //获取注册页面的intent 并将用户的注册信息填写
        Intent intent = getIntent();
        if (intent != null) {
            username = intent.getStringExtra("username");
            password = intent.getStringExtra("password");
            ip = intent.getStringExtra("ip");
            mUsername.setText(username);
            mPassword.setText(password);
            mIP.setText(ip);
        }

    }

    @Override
    protected void initListener() {
        //登录
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = mUsername.getText().toString();
                final String password = mPassword.getText().toString();
                final String ip = mIP.getText().toString();
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    ToastUtil.showToast(LoginActivity.this, getString(R.string.login_username_or_password_empty));
                    return;
                }
                mPresenter.login(username, password,ip);

            }
        });
        //注册
        mRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.jumpToRegisterPage();
            }
        });
    }

    /**
     * 登录界面进入动画
     */
    private void startAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_PARENT, -1, Animation.RELATIVE_TO_PARENT, 0
        );

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.3f, 1);

        AnimationSet set = new AnimationSet(true);
        set.addAnimation(translateAnimation);
        set.addAnimation(alphaAnimation);
        set.setDuration(3500);
        set.setInterpolator(new BounceInterpolator());
        linearLayout.startAnimation(set);

    }


    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showServerError() {
        ToastUtil.showToastSafe(mActivity, getString(R.string.login_server_error));
    }

    @Override
    public void showLoginError() {
        ToastUtil.showToastSafe(mActivity, getString(R.string.login_error));
    }

    @Override
    public void finishActivity() {
        finish();
    }
}
