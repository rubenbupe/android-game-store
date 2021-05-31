package com.example.practica2;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

public class ContactMainFragment extends Fragment {

    private Integer contactOption;

    public static final int CONTACT_FORM = 1;
    public static final int SUPPORT_FORM = 2;

    private Listener mListener;

    interface Listener {
        void onNextClick(int option);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        mListener = (Listener) context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact_main, container, false);

        RadioGroup rg = (RadioGroup) rootView.findViewById(R.id.contactType);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.contactRadio:
                        contactOption = CONTACT_FORM;
                        break;
                    case R.id.supportRadio:
                        contactOption = SUPPORT_FORM;
                        break;
                }
            }
        });

        Button button = (Button) rootView.findViewById(R.id.nextContact);
        button.setOnClickListener(view -> {
            if(contactOption != null){
                mListener.onNextClick(contactOption);
            }else{
                new AlertDialog.Builder(getActivity())
                        .setTitle(getResources().getString(R.string.form_incomplete))
                        .setPositiveButton(android.R.string.yes, null)
                        .show();
            }
        });

        return rootView;
    }
}