package com.zhouqing.chatproject.main.util;

import com.zhouqing.chatproject.common.ui.BaseFragment;
import com.zhouqing.chatproject.main.ContactFragment;
import com.zhouqing.chatproject.main.MeFragment;
import com.zhouqing.chatproject.main.MessageFragment;

import java.util.HashMap;
import java.util.Map;


public class FragmentFactory {
    private static Map<Integer,BaseFragment> map = new HashMap<>();
    public static BaseFragment getFragment(int position){
        BaseFragment fragment = map.get(position);
        if (fragment == null) {
            switch (position) {
                case 0:
                    fragment = new MessageFragment();
                    map.put(0, fragment);
                    break;
                case 1:
                    fragment = new ContactFragment();
                    map.put(1, fragment);
                    break;
                case 2:
                    fragment = new MeFragment();
                    map.put(3, fragment);
                    break;
            }
        }
        return fragment;
    }
    public static void clearAll(){
        map.clear();
    }
}
