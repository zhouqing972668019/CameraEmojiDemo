package com.zhouqing.chatproject.addFriend;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.zhouqing.chatproject.R;
import com.zhouqing.chatproject.common.ui.BaseActivity;
import com.zhouqing.chatproject.common.ui.view.MyEditText;
import com.zhouqing.chatproject.common.util.ToastUtil;
import com.zhouqing.chatproject.service.IMService;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;

public class AddFriendActivity extends BaseActivity {

    private MyEditText searchAccount;
    private Button btnSearch;

    @Override
    protected void initUi() {
        setContentView(R.layout.activity_add_friend);
        addActionBar(getString(R.string.main_search_title),true);

        searchAccount = ((MyEditText) findViewById(R.id.search_account));
        btnSearch = (Button) findViewById(R.id.btn_search);
        Intent intent = getIntent();
        if(intent != null){
            String username = intent.getStringExtra("username");
            if(!TextUtils.isEmpty(username)){
                searchAccount.setText(username);
                searchAccount.getEditText().setSelection(username.length());
            }
        }
    }

    @Override
    protected void initListener() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = searchAccount.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    ToastUtil.showToast(AddFriendActivity.this, getString(R.string.main_search_account_empty));
                    return;
                }
                addFriend(name);
            }
        });
    }

    private void addFriend(String name) {

        XMPPConnection conn = IMService.conn;
        String username = name + "@" + conn.getServiceName();

        Presence subscription = new Presence(Presence.Type.subscribe);
        subscription.setTo(username);
        conn.sendPacket(subscription);


        finish();
    }

    public void back(View view) {
        finish();
    }
}
