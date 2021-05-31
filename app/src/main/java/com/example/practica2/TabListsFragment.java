package com.example.practica2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.practica2.Adapters.ListsPagerAdapter;
import com.google.android.material.tabs.TabLayout;


public class TabListsFragment extends Fragment {

    private int startIndex = 0;

    private ViewPager mViewPager;

    public TabListsFragment(){};

    public TabListsFragment(int start){
        this.startIndex = start;
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_tab_lists, container, false);
    }

    public void setCurrentItem(int pos){
        mViewPager.setCurrentItem(pos);
    }

    @Override
    public void onStart() {
        super.onStart();

        View v = getView();

        if(v != null){
            ListsPagerAdapter adapter = new ListsPagerAdapter(
                    getChildFragmentManager(), getActivity());
            ViewPager pager = v.findViewById(R.id.listsViewPager);
            mViewPager = pager;
            pager.setAdapter(adapter);
            TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tablayout);
            tabLayout.setupWithViewPager(pager);
            //tabLayout.setScrollPosition(this.startIndex,0f,true);
            pager.setCurrentItem(this.startIndex);
            //pager.setOffscreenPageLimit(1);

        }
    }
}