package com.zhouqing.chatproject.common.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

public abstract class BaseFragment extends Fragment {
    private static final String TAG = "BaseFragment";
    private boolean isViewCreate;//判断view是否已经创建
    private boolean isDataLoad;//判断数据是否加载过
    protected AppCompatActivity mActivity;
    protected View mContentView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, this+ "onAttach: ");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mActivity == null) {
            mActivity = (AppCompatActivity) getActivity();
        }
        Log.i(TAG, this+ "onCreate: ");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mContentView == null) {
            mContentView = initUi();
        } else {
            removeContentView(mContentView);
        }
        isViewCreate = true;
        Log.i(TAG, this+ "onCreateView: ");
        return mContentView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isViewCreate && !isDataLoad) {
            initData();
            initListener();
            Log.i(TAG, this+ "setUserVisibleHint: 加载数据");
        }
    }


    /**
     * 第一页的数据的创建的时候不用被调用
     * 我们在这让其调用
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getUserVisibleHint() && isViewCreate && !isDataLoad) {
            initData();
            initListener();
            Log.i(TAG, this + "onActivityCreated: 加载数据");
        }

    }


    protected abstract View initUi();

    protected void initData() {
        isDataLoad = true;
    }

    protected void initListener() {
    }

    private void removeContentView(View view) {
        ViewParent viewParent = mContentView.getParent();
        if (viewParent instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) viewParent;
            viewGroup.removeView(mContentView);
        }
    }
}
