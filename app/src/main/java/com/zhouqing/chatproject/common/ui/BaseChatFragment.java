package com.zhouqing.chatproject.common.ui;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhouqing.chatproject.R;
import com.zhouqing.chatproject.common.util.DensityUtil;
import com.zhouqing.chatproject.model.EmotionModel;

import java.util.ArrayList;
import java.util.List;

import static com.zhouqing.chatproject.R.drawable.dot_default;

public abstract class BaseChatFragment extends BaseFragment {
    protected ViewPager viewPager;
    protected LinearLayout ll_dot;

    protected int pageCount;

    protected List<EmotionModel> mDatas = new ArrayList<>();
    protected List<GridView> gridViews = new ArrayList<>();//存放GridView

    @Override
    protected View initUi() {
        View view = View.inflate(mActivity, R.layout.fragment_base_chat, null);
        viewPager = (ViewPager) view.findViewById(R.id.vp);
        ll_dot = (LinearLayout) view.findViewById(R.id.ll_dot);
        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        initGridViewsAndDatas();
        viewPager.setAdapter(new ViewPageAdapter(gridViews));
        initDot();
    }

    protected abstract void initGridViewsAndDatas();


    @Override
    protected void initListener() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initDot() {
        int size = DensityUtil.dp2px(getContext(), 6);
        for (int i = 0; i < pageCount; i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(dot_default);
            ll_dot.addView(imageView, size, size);
            if (i == 0) {
                imageView.setImageResource(R.drawable.dot_pressed);
                continue;
            }
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
            layoutParams.leftMargin = size;
            imageView.setLayoutParams(layoutParams);
        }
    }

    private void changeDot(int position) {
        for (int i = 0; i < ll_dot.getChildCount(); i++) {
            ImageView imageView = (ImageView) ll_dot.getChildAt(i);
            imageView.setImageResource(dot_default);
            if (position == i) {
                imageView.setImageResource(R.drawable.dot_pressed);
            }
        }

    }


    /**
     * ViewPager的适配器
     */
    protected class ViewPageAdapter extends PagerAdapter {
        private List<GridView> gridViews;

        public ViewPageAdapter(List<GridView> gridViews) {
            this.gridViews = gridViews;
        }

        @Override
        public int getCount() {
            return gridViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            GridView gridView = gridViews.get(position);
            container.addView(gridView);
            return gridView;
        }
    }
}
