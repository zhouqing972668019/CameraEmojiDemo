package com.zhouqing.chatproject.manage;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.zhouqing.chatproject.R;
import com.zhouqing.chatproject.common.constant.Global;
import com.zhouqing.chatproject.common.ui.BaseActivity;
import com.zhouqing.chatproject.common.util.EmotionUtil;
import com.zhouqing.chatproject.common.util.SpanStringUtil;
import com.zhouqing.chatproject.common.util.XmppUtil;
import com.zhouqing.chatproject.db.SmsOpenHelper;
import com.zhouqing.chatproject.service.IMService;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ManageActivity extends BaseActivity implements ManageContract.View{

    private Spinner spSession;
    private ListView mListView;
    private ManageContract.Presenter mPresenter;
    private String[] sessions;
    private Cursor sessionCursor = null;
    private MyCursorAdapter mCursorAdapter;

    int currentUserAvatarId;
    String otherUserAvatarId;

    //为Activity添加ToolBar
    protected void addActionBar(String title, boolean isBackable) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        if (!TextUtils.isEmpty(title)) {
            mActionBar.setTitle(title);
        }
        if (isBackable) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void initUi() {
        setContentView(R.layout.activity_manage);
        addActionBar(" ", true);
        spSession = findViewById(R.id.sp_session);
        mPresenter = new ManagePresenter(mActivity, this);
        mListView = findViewById(R.id.listview);
    }

    @Override
    protected void initListener() {
        super.initListener();
        spSession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //toast("you selected:"+position);
                if(sessionCursor != null){
                    sessionCursor.moveToPosition(position);
                    // 拿到jid(账号)-->发送消息的时候需要
                    String account = sessionCursor.getString(sessionCursor.getColumnIndex(SmsOpenHelper.SmsTable.SESSION_ACCOUNT));
                    // 拿到nickName-->显示效果
                    mPresenter.getDialogueMessage(account);
                    otherUserAvatarId = XmppUtil.getOtherUserAvatar(account);
                    currentUserAvatarId = XmppUtil.getCurrentUserAvatar();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.getContact();
    }

    @Override
    public void showSession(Cursor cursor) {
        if(cursor!=null&&cursor.moveToFirst()){
            sessionCursor = cursor;
            sessions = new String[cursor.getCount()];
            int index = 0;
            do{
                String account = cursor.getString(cursor.getColumnIndex(SmsOpenHelper.SmsTable.SESSION_ACCOUNT));
                sessions[index++] = Global.accountToNickName(account);
            }while(cursor.moveToNext());
            //ToastUtil.showToast(ManageActivity.this, Arrays.toString(sessions));
            ArrayAdapter<String> shopAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,sessions);
            spSession.setAdapter(shopAdapter);
        }


    }

    @Override
    public void showDialogueMessage(Cursor cursor) {
//        if (mCursorAdapter != null) {
//            mCursorAdapter.getCursor().requery();
//            mListView.setSelection(cursor.getCount() - 1);//让ListView到达最后一列
//            return;
//        }

        mCursorAdapter = new MyCursorAdapter(ManageActivity.this, cursor);
        mListView.setAdapter(mCursorAdapter);
        mListView.setSelection(mCursorAdapter.getCount() - 1);

    }

    @Override
    public void setPresenter(ManageContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    /**
     * ListView的适配器
     */
    private class MyCursorAdapter extends CursorAdapter {
        private static final int SEND = 0;
        private static final int RECEIVE = 1;
        private Cursor cursor;

        public MyCursorAdapter(Context context, Cursor c) {
            super(context, c);
            cursor = c;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return null;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
        }

        //获取view的类型
        @Override
        public int getItemViewType(int position) {
            cursor.moveToPosition(position);
            // 取出消息的创建者
            String fromAccount = cursor.getString(cursor.getColumnIndex(SmsOpenHelper.SmsTable.FROM_ACCOUNT));
            if (IMService.ACCOUNT.equals(fromAccount)) {// 接收
                return SEND;
            } else {// 发送
                return RECEIVE;
            }
            // return super.getItemViewType(position);// 0 1
            // 接收--->如果当前的账号 不等于 消息的创建者
            // 发送
        }

        //总共有几种类型的View
        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount() + 1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyCursorAdapter.ViewHolder holder;
            if (getItemViewType(position) == RECEIVE) {
                if (convertView == null) {
                    convertView = View.inflate(ManageActivity.this, R.layout.item_chat_receiver, null);
                    holder = new MyCursorAdapter.ViewHolder();
                    convertView.setTag(holder);

                    // holder赋值
                    holder.time = (TextView) convertView.findViewById(R.id.time);
                    holder.body = (TextView) convertView.findViewById(R.id.content);
                    holder.head = (ImageView) convertView.findViewById(R.id.head);
                    holder.emotion = (TextView) convertView.findViewById(R.id.emotion);
                } else {
                    holder = (MyCursorAdapter.ViewHolder) convertView.getTag();
                }

                // 得到数据,展示数据
                if(otherUserAvatarId != null){
                    holder.head.setImageResource(Global.AVATARS[Integer.parseInt(otherUserAvatarId)]);
                }
            } else {// 发送
                if (convertView == null) {
                    convertView = View.inflate(ManageActivity.this, R.layout.item_chat_send, null);
                    holder = new MyCursorAdapter.ViewHolder();
                    convertView.setTag(holder);

                    // holder赋值
                    holder.time = (TextView) convertView.findViewById(R.id.time);
                    holder.body = (TextView) convertView.findViewById(R.id.content);
                    holder.head = (ImageView) convertView.findViewById(R.id.head);
                    holder.emotion = (TextView)convertView.findViewById(R.id.emotion);
                } else {
                    holder = (MyCursorAdapter.ViewHolder) convertView.getTag();
                }
                //得到数据,展示数据
                if(currentUserAvatarId != -1){
                    holder.head.setImageResource(Global.AVATARS[currentUserAvatarId]);
                }
            }
            // 得到数据,展示数据
            cursor.moveToPosition(position);

            String time = cursor.getString(cursor.getColumnIndex(SmsOpenHelper.SmsTable.TIME));
            String body = cursor.getString(cursor.getColumnIndex(SmsOpenHelper.SmsTable.BODY));
            String emotion = cursor.getString(cursor.getColumnIndex(SmsOpenHelper.SmsTable.EMOTION));
            String facePic = cursor.getString(cursor.getColumnIndex(SmsOpenHelper.SmsTable.FACE_PIC));

            String formatTime = new SimpleDateFormat("HH:mm").format(new Date(Long
                    .parseLong(time)));

            holder.time.setText(formatTime);

            //使用SpanImage代替特殊字符
            System.out.println("time:"+time+",body:"+body);
            SpannableString emotionContent = SpanStringUtil.getEmotionContent(EmotionUtil.EMOTION_CLASSIC_TYPE,
                    ManageActivity.this, holder.body, body);
            holder.body.setText(emotionContent);
            holder.emotion.setVisibility(View.VISIBLE);
            holder.emotion.setText(emotion);

            return convertView;
        }

        private class ViewHolder {
            TextView body;
            TextView time;
            ImageView head;
            TextView emotion;
        }
    }
}
