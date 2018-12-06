package com.zhouqing.chatproject.main.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.zhouqing.chatproject.R;
import com.zhouqing.chatproject.addFriend.AddFriendActivity;
import com.zhouqing.chatproject.main.MainActivity;



public class ListViewWidthHeader extends ListView implements AdapterView.OnItemClickListener {
    private OnItemClickListener onItemClickListener;

    public ListViewWidthHeader(Context context) {
        this(context, null);
    }

    public ListViewWidthHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListViewWidthHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.layout_header, null);
        LinearLayout llAddFriend = (LinearLayout) view.findViewById(R.id.ll_add_friend);
        llAddFriend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getContext();
                activity.startActivity(new Intent(activity, AddFriendActivity.class));
            }
        });
        addHeaderView(view, null, false);
        this.setSelector(new ColorDrawable(Color.TRANSPARENT));
        this.setCacheColorHint(Color.TRANSPARENT);
    }

    //当子类设置点击监听时候互殴
    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        super.setOnItemClickListener(this);
        this.onItemClickListener = listener;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(parent, view, position - getHeaderViewsCount(), id);
        }
    }
}
