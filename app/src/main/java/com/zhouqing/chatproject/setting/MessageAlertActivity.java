package com.zhouqing.chatproject.setting;

import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;

import com.zhouqing.chatproject.R;
import com.zhouqing.chatproject.common.ui.BaseActivity;
import com.zhouqing.chatproject.common.util.SPUtil;



public class MessageAlertActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener{
    private SwitchCompat scMessageDetail;
    private SwitchCompat scMessageVoice;
    private SwitchCompat scMessageShock;
    @Override
    protected void initUi() {
        setContentView(R.layout.activity_message_alert);
        addActionBar(getString(R.string.setting_new_message_title),true);

        scMessageDetail = (SwitchCompat) findViewById(R.id.sc_message_detail);
        scMessageVoice = (SwitchCompat) findViewById(R.id.sc_message_voice);
        scMessageShock = (SwitchCompat) findViewById(R.id.sc_message_shock);
    }

    @Override
    protected void initData() {
        boolean isShowMessageDetail = (boolean) SPUtil.get(this, "isShowMessageDetail", true);
        boolean isShowMessageVoice = (boolean) SPUtil.get(this, "isShowMessageVoice", true);
        boolean isShowMessageShock = (boolean) SPUtil.get(this, "isShowMessageShock", true);
        scMessageDetail.setChecked(isShowMessageDetail);
        scMessageVoice.setChecked(isShowMessageVoice);
        scMessageShock.setChecked(isShowMessageShock);
    }

    @Override
    protected void initListener() {
        scMessageDetail.setOnCheckedChangeListener(this);
        scMessageVoice.setOnCheckedChangeListener(this);
        scMessageShock.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()){
            case R.id.sc_message_detail:
                if(b){
                    SPUtil.put(this,"isShowMessageDetail",true);
                }else{
                    SPUtil.put(this,"isShowMessageDetail",false);
                }
                break;
            case R.id.sc_message_voice:
                if(b){
                    SPUtil.put(this,"isShowMessageVoice",true);
                }else{
                    SPUtil.put(this,"isShowMessageVoice",false);
                }
                break;
            case R.id.sc_message_shock:
                if(b){
                    SPUtil.put(this,"isShowMessageShock",true);
                }else{
                    SPUtil.put(this,"isShowMessageShock",false);
                }
                break;
        }

    }
}
