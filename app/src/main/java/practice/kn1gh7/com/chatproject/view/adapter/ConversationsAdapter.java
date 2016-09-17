package practice.kn1gh7.com.chatproject.view.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.lang.ref.WeakReference;

import practice.kn1gh7.com.chatproject.R;
import practice.kn1gh7.com.chatproject.presenter.TabsPresenterImpl;
import practice.kn1gh7.com.chatproject.view.ConversationFragment;

/**
 * Created by kn1gh7 on 16/9/16.
 */
public class ConversationsAdapter extends FragmentStatePagerAdapter {
    String[] tabCategories;

    public ConversationsAdapter(Activity context, FragmentManager fm) {
        super(fm);
        tabCategories = context.getResources().getStringArray(R.array.tab_categories);
    }

    @Override
    public Fragment getItem(int position) {
        ConversationFragment fragment = ConversationFragment.newInstance(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return tabCategories.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabCategories[position];
    }
}
