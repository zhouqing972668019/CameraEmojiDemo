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
import com.zhouqing.chatproject.avatar.AvatarActivity;
import com.zhouqing.chatproject.common.constant.Global;
import com.zhouqing.chatproject.common.ui.BaseFragment;
import com.zhouqing.chatproject.common.ui.view.SettingView;
import com.zhouqing.chatproject.common.util.DensityUtil;
import com.zhouqing.chatproject.common.util.ToastUtil;
import com.zhouqing.chatproject.common.util.XmppUtil;
import com.zhouqing.chatproject.service.IMService;
import com.zhouqing.chatproject.setting.SettingActivity;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.packet.VCard;

import static android.app.Activity.RESULT_OK;


public class MeFragment extends BaseFragment implements View.OnClickListener {
    private LinearLayout ll_account;
    private TextView mNickname;
    private TextView mAccount;
    private SettingView avatar_pic;
    private SettingView mBtnSetting;
    private ImageView ivQRCode;
    private ImageView avatar;
    private SettingView manage;
    private String account;
    private String nickname;


    private int selectedAvatar;

    @Override
    protected View initUi() {
        View view = View.inflate(mActivity, R.layout.fragment_me, null);
        ll_account = (LinearLayout) view.findViewById(R.id.ll_account);
        mNickname = (TextView) view.findViewById(R.id.nickname);
        mAccount = (TextView) view.findViewById(R.id.account);
        mBtnSetting = (SettingView) view.findViewById(R.id.setting);
        ivQRCode = (ImageView) view.findViewById(R.id.iv_qr_code);
        manage = view.findViewById(R.id.manage);
        avatar_pic = view.findViewById(R.id.avatar_pic);
        avatar = view.findViewById(R.id.avatar);
        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        account = IMService.ACCOUNT;
        nickname = account.substring(0, account.indexOf("@"));
        mNickname.setText(getString(R.string.main_me_message_nickname) + nickname);
        mAccount.setText(getString(R.string.main_me_message_account) + account);
        if(XmppUtil.getCurrentUserAvatar() != -1){
            avatar.setImageResource(Global.AVATARS[XmppUtil.getCurrentUserAvatar()]);
        }
    }

    @Override
    protected void initListener() {
        mBtnSetting.setOnClickListener(this);
        avatar_pic.setOnClickListener(this);
        ivQRCode.setOnClickListener(this);
        ll_account.setOnClickListener(this);
        manage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ToastUtil.showToast(getContext(),"触发");
                return true;
            }
        });
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
            case R.id.avatar_pic:
                selectAvatar();
                break;

        }
    }

    private void showQrCodeDialog() {
        View view = View.inflate(mActivity, R.layout.layout_qrcode, null);
        ImageView iv_qr_code = (ImageView) view.findViewById(R.id.iv_qr_code);
        TextView iv_nickname = (TextView) view.findViewById(R.id.nickname);
        TextView iv_account = (TextView) view.findViewById(R.id.account);
        ImageView avatar = view.findViewById(R.id.avatar);

        iv_nickname.setText(getString(R.string.main_me_message_nickname) + nickname);
        iv_account.setText(getString(R.string.main_me_message_account) + account);
        int avatarId = -1;
        if(XmppUtil.getCurrentUserAvatar() != -1){
            avatarId = Global.AVATARS[XmppUtil.getCurrentUserAvatar()];
        }
        Bitmap qrCode = EncodingUtils.createQRCode(account,
                DensityUtil.dp2px(mActivity, 250),
                DensityUtil.dp2px(mActivity, 250),
                BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        if(avatarId != -1){
            qrCode = EncodingUtils.createQRCode(account,
                    DensityUtil.dp2px(mActivity, 250),
                    DensityUtil.dp2px(mActivity, 250),
                    BitmapFactory.decodeResource(getResources(), avatarId));
            avatar.setImageResource(avatarId);
        }
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

    //选择头像
    public void selectAvatar(){
        Intent intent = new Intent(getActivity(),AvatarActivity.class);
        startActivityForResult(intent,10001);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 10001:
                if(resultCode == RESULT_OK){
                    selectedAvatar = data.getIntExtra("selectedAvatar",-1);
                    if(selectedAvatar != -1){
                        avatar.setImageResource(Global.AVATARS[selectedAvatar]);
                        XMPPConnection connection = XmppUtil.getConnection();
                        if(connection != null){
                            //Toast.makeText(getActivity(),selectedAvatar+"",Toast.LENGTH_SHORT).show();
                            VCard vCard = new VCard();
                            vCard.setField("avatarId",String.valueOf(selectedAvatar));
                            try {
                                vCard.save(connection);
                            } catch (XMPPException e) {
                                e.printStackTrace();
                            }
                        }
                }
                    //Toast.makeText(getActivity(),selectedAvatar+"",Toast.LENGTH_SHORT).show();
                }
        }
    }


}
