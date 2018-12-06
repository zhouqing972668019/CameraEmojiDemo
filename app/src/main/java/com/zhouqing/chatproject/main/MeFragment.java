package com.zhouqing.chatproject.main;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xys.libzxing.zxing.encoding.EncodingUtils;
import com.zhouqing.chatproject.R;
import com.zhouqing.chatproject.common.ui.BaseFragment;
import com.zhouqing.chatproject.common.ui.view.SettingView;
import com.zhouqing.chatproject.common.util.DensityUtil;
import com.zhouqing.chatproject.service.IMService;
import com.zhouqing.chatproject.setting.SettingActivity;


public class MeFragment extends BaseFragment implements View.OnClickListener {
    private LinearLayout ll_account;
    private TextView mNickname;
    private TextView mAccount;
    private SettingView mBtnSetting;
    private ImageView ivQRCode;
    private String account;
    private String nickname;

    @Override
    protected View initUi() {
        View view = View.inflate(mActivity, R.layout.fragment_me, null);
        ll_account = (LinearLayout) view.findViewById(R.id.ll_account);
        mNickname = (TextView) view.findViewById(R.id.nickname);
        mAccount = (TextView) view.findViewById(R.id.account);
        mBtnSetting = (SettingView) view.findViewById(R.id.setting);
        ivQRCode = (ImageView) view.findViewById(R.id.iv_qr_code);
        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        account = IMService.ACCOUNT;
        nickname = account.substring(0, account.indexOf("@"));
        mNickname.setText(getString(R.string.main_me_message_nickname) + nickname);
        mAccount.setText(getString(R.string.main_me_message_account) + account);
    }

    @Override
    protected void initListener() {
        mBtnSetting.setOnClickListener(this);
        ivQRCode.setOnClickListener(this);
        ll_account.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting:
                startActivity(new Intent(mActivity, SettingActivity.class));
                break;
            case R.id.iv_qr_code:
            case R.id.ll_account:
                showQrCodeDialog();
                break;
        }
    }

    private void showQrCodeDialog() {
        View view = View.inflate(mActivity, R.layout.layout_qrcode, null);
        ImageView iv_qr_code = (ImageView) view.findViewById(R.id.iv_qr_code);
        TextView iv_nickname = (TextView) view.findViewById(R.id.nickname);
        TextView iv_account = (TextView) view.findViewById(R.id.account);

        iv_nickname.setText(getString(R.string.main_me_message_nickname) + nickname);
        iv_account.setText(getString(R.string.main_me_message_account) + account);
        Bitmap qrCode = EncodingUtils.createQRCode(account,
                DensityUtil.dp2px(mActivity, 250),
                DensityUtil.dp2px(mActivity, 250),
                BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        iv_qr_code.setImageBitmap(qrCode);

        final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setView(view);
        builder.setCancelable(true);
        final AlertDialog dialog = builder.show();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}
