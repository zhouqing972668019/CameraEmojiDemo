package com.zhouqing.chatproject.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.zhouqing.chatproject.R;
import com.zhouqing.chatproject.addFriend.AddFriendActivity;
import com.zhouqing.chatproject.chat.DetailActivity;
import com.zhouqing.chatproject.common.ui.BaseActivity;
import com.zhouqing.chatproject.common.ui.BaseFragment;
import com.zhouqing.chatproject.common.util.ToastUtil;
import com.zhouqing.chatproject.db.ContactOpenHelper;
import com.zhouqing.chatproject.main.util.FragmentFactory;
import com.zhouqing.chatproject.provider.ContactProvider;
import com.zhouqing.chatproject.service.IMService;



public class MainActivity extends BaseActivity {

    private ViewPager mViewPage;
    private RadioGroup mRadioGroup;

    @Override
    protected void initUi() {
        setContentView(R.layout.activity_main);
        addActionBar(getString(R.string.main_message_title), false);

        mViewPage = (ViewPager) findViewById(R.id.viewPage);
        mRadioGroup = (RadioGroup) findViewById(R.id.rg_main);
    }

    @Override
    protected void initData() {
        mViewPage.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
    }

    @Override
    protected void initListener() {
        mRadioGroup.setOnCheckedChangeListener(new MyOnCheckedChageListener());
        mViewPage.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.scan:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager
                        .PERMISSION_GRANTED) {
                    startActivityForResult(new Intent(this, CaptureActivity.class), 0);
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
                }

                break;
            case R.id.add_friend:
                startActivity(new Intent(MainActivity.this, AddFriendActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            String result = data.getStringExtra("result");

            if (result.startsWith("http://")) {
                Uri uri = Uri.parse(result);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
            if (result.endsWith("@" + IMService.conn.getServiceName())) {
                String username = result.substring(0, result.indexOf("@"));
                Cursor cursor = getContentResolver().
                        query(ContactProvider.URI_CONTACT, null,
                                "my_account = ?",
                                new String[]{IMService.ACCOUNT}, null);
                if (cursor == null) {
                    //没有添加过该好友

                    Intent intent = new Intent(this, AddFriendActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                } else {
                    while (cursor.moveToNext()) {
                        String nickname = cursor.getString(cursor.getColumnIndex(ContactOpenHelper.ContactTable
                                .NICKNAME));
                        if (nickname.equals(username)) {
                            //已经添加过 跳转到详细信息界面
                            Intent intent = new Intent(this, DetailActivity.class);
                            intent.putExtra(ContactOpenHelper.ContactTable.ACCOUNT, username + "@" + IMService.conn
                                    .getServiceName());
                            intent.putExtra(ContactOpenHelper.ContactTable.NICKNAME, username);
                            startActivity(intent);
                            return;
                        }
                    }
                    //没有添加过该好友
                    Intent intent = new Intent(this, AddFriendActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
            } else {
                ToastUtil.showToast(this, "扫描结果为：" + result);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0 && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            startActivityForResult(new Intent(this, CaptureActivity.class), 0);
        }else{
            ToastUtil.showToast(this,"没有访问相机权限");
        }
    }

    /**
     * 当页面改变的监听器
     */
    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            RadioButton button = null;
            switch (position) {
                case 0:
                    button = (RadioButton) findViewById(R.id.rb_message);
                    mActionBar.setTitle(getString(R.string.main_message_title));
                    break;
                case 1:
                    button = (RadioButton) findViewById(R.id.rb_contact);
                    mActionBar.setTitle(getString(R.string.main_contact_title));
                    break;
                case 2:
                    button = (RadioButton) findViewById(R.id.rb_me);
                    mActionBar.setTitle(getString(R.string.main_me_title));
                    break;
            }
            button.setChecked(true);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * RadioGroup的点击监听
     */
    private class MyOnCheckedChageListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_message:
                    mViewPage.setCurrentItem(0, false);
                    mActionBar.setTitle(getString(R.string.main_message_title));
                    break;
                case R.id.rb_contact:
                    mViewPage.setCurrentItem(1, false);
                    mActionBar.setTitle(R.string.main_contact_title);
                    break;

                case R.id.rb_me:
                    mViewPage.setCurrentItem(2, false);
                    mActionBar.setTitle(R.string.main_me_title);
                    break;

            }
        }
    }

    /**
     * ViewPager的适配器
     */
    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public BaseFragment getItem(int position) {
            return FragmentFactory.getFragment(position);
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    /**
     * 主页面返回的时候 回到home界面
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}




