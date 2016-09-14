package com.erginus.klips.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;

import android.widget.TextView;

import com.erginus.klips.Adapter.MyPlayListAdapter;
import com.erginus.klips.Commons.SlidingTabLayout;

import com.erginus.klips.HomeActivity;
import com.erginus.klips.R;


public class PlayListFragment extends Fragment {
    TextView txt_title, txt_artist, txt_duration;
    ImageView img_album, img_backward, img_forward, img_play, img_share, img_like;
    CharSequence Titles[] = {"Music", "Videos"};
    int Numboftabs = 2;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ViewPager pager;
    MyPlayListAdapter adapter;
    public static SlidingTabLayout tabs;
    public PlayListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                // app icon in action bar clicked; goto parent activity.

                Intent intent=new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
                //overridePendingTransition(0, 0);
                // return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_home).setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview= inflater.inflate(R.layout.fragment_playlist, container, false);
        HomeActivity.txt_title.setText("My Playlists");
        adapter = new MyPlayListAdapter(getFragmentManager(), Titles, Numboftabs);
        mSwipeRefreshLayout=(SwipeRefreshLayout)rootview.findViewById(R.id.swipeRefreshLayout);
        pager = (ViewPager)rootview.findViewById(R.id.pager);
        pager.setAdapter(adapter);
        tabs = (SlidingTabLayout)rootview.findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);

        txt_title=(TextView)rootview.findViewById(R.id.textView_title);
        txt_artist=(TextView)rootview.findViewById(R.id.textView_artist);
        txt_duration=(TextView)rootview.findViewById(R.id.textView_duration);
        img_album=(ImageView)rootview.findViewById(R.id.imageView_albm);
        img_backward=(ImageView)rootview.findViewById(R.id.imageView_back);
        img_forward=(ImageView)rootview.findViewById(R.id.imageView_forward);
        img_play=(ImageView)rootview.findViewById(R.id.imageView_play);
        img_share=(ImageView)rootview.findViewById(R.id.imageView_share);
        img_like=(ImageView)rootview.findViewById(R.id.imageView_fav);

        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.white);
            }


        });
        tabs.setViewPager(pager);


        return  rootview;
    }
    @Override
    public void onResume() {

        super.onResume();
        new PlayListFragment();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    if(getFragmentManager().getBackStackEntryCount() > 0) {
                        HomeActivity.txt_title.setText("Home");
                        getFragmentManager().popBackStack();
                    }

                    return true;

                }

                return false;
            }
        });
    }
}
