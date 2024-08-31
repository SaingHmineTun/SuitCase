package com.stargazing.suitcase.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.stargazing.suitcase.fragments.FinishedFragment;
import com.stargazing.suitcase.fragments.TodoFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {


    public ViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);

    }

    private TodoFragment todoFragment;
    private FinishedFragment finishedFragment;

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            if (todoFragment == null) todoFragment = new TodoFragment();
            return todoFragment;
        }
        if (finishedFragment == null) finishedFragment = new FinishedFragment();
        return finishedFragment;
    }

    private final String[] fragmentTitle = {"Todo", "Finished"};

    public void refreshDataSetChanged() {
        todoFragment.refreshListView();
        finishedFragment.refreshListView();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitle[position];
    }

    @Override
    public int getCount() {
        return fragmentTitle.length;
    }
}
