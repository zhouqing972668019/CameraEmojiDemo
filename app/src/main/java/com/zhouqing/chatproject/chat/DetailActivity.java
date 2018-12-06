package com.zhouqing.chatproject.chat;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.zhouqing.chatproject.R;
import com.zhouqing.chatproject.common.ui.BaseActivity;
import com.zhouqing.chatproject.db.ContactOpenHelper;


public class DetailActivity extends BaseActivity {
    private TextView tvNickname;
    private TextView tvAccount;

    private String mClickNickname;
    private String mClickAccount;
    @Override
    protected void initUi() {
        setContentView(R.layout.activity_detail);
        addActionBar("详细资料",true);

        tvNickname = (TextView) findViewById(R.id.nickname);
        tvAccount = (TextView) findViewById(R.id.account);
        Intent intent = getIntent();
        if (intent != null) {
            mClickAccount = intent.getStringExtra(ContactOpenHelper.ContactTable.ACCOUNT);
            mClickNickname = intent.getStringExtra(ContactOpenHelper.ContactTable.NICKNAME);
        }
        tvNickname.setText("昵称: "+mClickNickname);
        tvAccount.setText("账号: "+mClickAccount);

    }
    public void back(View view){
        finish();
    }
    public void sendMessage(View view){
        finish();
        Intent intent = new Intent(DetailActivity.this, ChatActivity.class);
        intent.putExtra(ContactOpenHelper.ContactTable.ACCOUNT, mClickAccount);
        intent.putExtra(ContactOpenHelper.ContactTable.NICKNAME, mClickNickname);
        startActivity(intent);
    }
}
