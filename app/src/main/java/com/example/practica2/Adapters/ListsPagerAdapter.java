package com.example.practica2.Adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.practica2.ListFragment;
import com.example.practica2.R;

public class ListsPagerAdapter extends FragmentStatePagerAdapter {
    private Context mContext;

    public ListsPagerAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
    }

    @Override
    public CharSequence getPageTitle(int position) { switch (position)
    {
        case 0:
            return mContext.getResources().getText(R.string.newMenuTitle);
        case 1:
            return mContext.getResources().getText(R.string.discountsMenuTitle);
        case 2:
            return mContext.getResources().getText(R.string.xboxMenuTitle);
        case 3:
            return mContext.getResources().getText(R.string.ps4MenuTitle);
    }
        return null;
    }

    @Override
    public int getCount () {
        return 4;
    }

    @Override
    public Fragment getItem (int position) {
        switch (position)
        {
            case 0:
                return new ListFragment(ListFragment.SECTION_NEW);
            case 1:
                return new ListFragment(ListFragment.SECTION_DISCOUNTS);
            case 2:
                return new ListFragment(ListFragment.SECTION_XBOX);
            case 3:
                return new ListFragment(ListFragment.SECTION_PS4);
        }
        return null;
    }
}
