package com.erginus.klips.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.erginus.klips.Commons.Prefshelper;
import com.erginus.klips.GuestHomeActivity;
import com.erginus.klips.HomeActivity;
import com.erginus.klips.R;


public class AboutUS extends Fragment {
View view;
    Prefshelper prefshelper;
    public AboutUS() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View rootview= inflater.inflate(R.layout.fragment_faq, container, false);
       prefshelper=new Prefshelper(getActivity());
        if (prefshelper.getLoginWithFromPreference().equals("0")) {
            GuestHomeActivity.txt_title.setText("About Us");

        }
        else{
            HomeActivity.txt_title.setText("About Us");

        }
        return  rootview;
    }

    @Override
    public void onResume() {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){

                    if(getFragmentManager().getBackStackEntryCount() > 0) {
                        if (prefshelper.getLoginWithFromPreference().equals("0")) {
                            GuestHomeActivity.txt_title.setText("Home");
                            getFragmentManager().popBackStack();
                        }
                        else{
                            HomeActivity.txt_title.setText("Home");
                            getFragmentManager().popBackStack();
                        }
                    }

                    return true;

                }

                return false;
            }
        });
    }

}
