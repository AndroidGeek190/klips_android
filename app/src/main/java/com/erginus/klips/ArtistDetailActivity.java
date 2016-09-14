package com.erginus.klips;

import android.app.ProgressDialog;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.erginus.klips.Adapter.FullListAdapter;
import com.erginus.klips.Adapter.PlayListAdapter;

import com.erginus.klips.Commons.EndlessListView;
import com.erginus.klips.Commons.MapAppConstant;
import com.erginus.klips.Commons.Prefshelper;
import com.erginus.klips.Commons.SlidingTabLayout;
import com.erginus.klips.Commons.VolleySingleton;

import com.erginus.klips.Model.VideoModel;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArtistDetailActivity extends AppCompatActivity {
  /*  CharSequence Titles[] = {"Top Music", "Top Videos"};
    int Numboftabs = 1;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ViewPager pager;
    ViewPagerAdapter adapter;
    */public static SlidingTabLayout tabs;
    public  static LinearLayout ll_back;
    TextView txt_name, txt_description,txt_top;
    ImageView imageView,imageView_home1;
    List<VideoModel> list;

    Prefshelper prefshelper;

    String name;
    EndlessListView endlessListView;
    private boolean mHaveMoreDataToLoad=true;
    int page=0;
    FullListAdapter horizontalListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_detail);
        list=new ArrayList<VideoModel>();

      /*  adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs);
        mSwipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
     */  ll_back=(LinearLayout)findViewById(R.id.ll_navi);
        prefshelper=new Prefshelper(this);
        txt_description=(TextView)findViewById(R.id.textView_desc);
        txt_name=(TextView)findViewById(R.id.textView_name);
        txt_top=(TextView)findViewById(R.id.textView8);
        imageView=(ImageView)findViewById(R.id.profile_img);
        // Assigning ViewPager View and setting the adapter
      /*  pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adapter);
      *//*  tabs = (SlidingTabLayout)findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);
      */  txt_name.setText(getIntent().getStringExtra("name"));
        txt_description.setText(getIntent().getStringExtra("desc"));
        Picasso.with(this).load(getIntent().getStringExtra("image")).into(imageView);
       /* CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Artist");
       */ // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
      /*  tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.white);
            }


        });*/
        imageView_home1 = (ImageView) findViewById(R.id.imageView_home);


        imageView_home1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefshelper.getLoginWithFromPreference().equals("0")) {
                    Intent intent = new Intent(ArtistDetailActivity.this, GuestHomeActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ArtistDetailActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            }
        });
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
       // tabs.setViewPager(pager);

        endlessListView=(EndlessListView)findViewById(R.id.listView);
        endlessListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                prefshelper.storeWheelItemToPreference("2");
                Intent intent = new Intent(ArtistDetailActivity.this, VideoDetailActivity.class);
                intent.putExtra("list", (Serializable) list);
                intent.putExtra("currentIndex", position);
                intent.putExtra("title", list.get(position).getName());
                intent.putExtra("id", "music");
                intent.putExtra("artist", name);
                intent.putExtra("image", list.get(position).getImage());
                intent.putExtra("status", "0");
                intent.putExtra("play_status", "0");

                //    intent.putExtra("duration", list.get(position).getDuration());

                startActivity(intent);
                ArtistDetailActivity.this.overridePendingTransition(0, 0);
            }
        });
        endlessListView.setOnLoadMoreListener(new EndlessListView.OnLoadMoreListener() {
            @Override
            public boolean onLoadMore() {
                if (mHaveMoreDataToLoad==true) {
                    loadMoreData();
                } else {
                    Toast.makeText(ArtistDetailActivity.this, "No more data to load",
                            Toast.LENGTH_SHORT).show();
                }

                return mHaveMoreDataToLoad;

            }
        });
getArtistSongList(page);
       
    }
    private void loadMoreData() {
        page++;
        getArtistSongList(page);

    }
    public void getArtistSongList(final int page) {
        try {
            final ProgressDialog pDialog = new ProgressDialog(ArtistDetailActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "countries " + MapAppConstant.API + "artist_musics");
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "artist_musics", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.d("", ".......response====" + response.toString());

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                        //     Toast.makeText(ArtistDetailActivity.this, "" + serverMessage, Toast.LENGTH_SHORT).show();
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {
                                    JSONArray jsonArray = object.getJSONArray("data");
                                    String id = "", song_name = "", fav_status="", image="", song, playlist_status;
                                    String desc="";
                                    if(jsonArray.length()>0)
                                    {
                                        getList().clear();
                                        for(int i=0; i<jsonArray.length(); i++) {
                                            JSONObject object1 = jsonArray.getJSONObject(i);

                                            id = object1.getString("music_id");
                                            song_name = object1.getString("music_name");
                                            desc=object1.getString("music_description");
                                            song=object1.getString("music_file_url");
                                            image = object1.getString("music_thumbnail_url");
                                            fav_status=object1.getString("favourite_status");
                                            playlist_status=object1.getString("playlist_status");
                                            list.add(songsModel(id, song_name,image, song,fav_status,playlist_status, desc));
                                        }
                                        setList(list);
                                    }
                                    else
                                    {
                                        mHaveMoreDataToLoad=false;
                                    }
                                    if(endlessListView.getAdapter()==null){
                                        horizontalListAdapter=new FullListAdapter(ArtistDetailActivity.this,list);
                                        endlessListView.setAdapter(horizontalListAdapter);

                                    }
                                    else{
                                        endlessListView.loadMoreCompleat();
                                        horizontalListAdapter.notifyDataSetChanged();

                                    }
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
                    , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.dismiss();
                    //  VolleyLog.d("", "Error: " + error.getMessage());
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(ArtistDetailActivity.this, "Timeout Error",
                                Toast.LENGTH_LONG).show();
                    } else if (error instanceof AuthFailureError) {
                        VolleyLog.d("", "" + error.getMessage() + "," + error.toString());
                    } else if (error instanceof ServerError) {
                        VolleyLog.d("", "" + error.getMessage() + "," + error.toString());
                    } else if (error instanceof NetworkError) {
                        VolleyLog.d("", "" + error.getMessage() + "," + error.toString());
                    } else if (error instanceof ParseError) {
                        VolleyLog.d("", "" + error.getMessage() + "," + error.toString());
                    }
                }
            }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    Log.d("id", prefshelper.getArtistIdFromPreference());
                    params.put("artist_id", prefshelper.getArtistIdFromPreference());
                    params.put("page",page+"");
                    params.put("user_id", prefshelper.getUserIdFromPreference());
                    params.put("user_security_hash", prefshelper.getUserSecHashFromPreference());

                    return params;
                }
            };
            sr.setShouldCache(true);

            sr.setRetryPolicy(new DefaultRetryPolicy(50000 * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(ArtistDetailActivity.this.getApplicationContext()).addToRequestQueue(sr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private VideoModel songsModel(String id, String name, String image , String songUrl, String fav, String play, String desc) {
        VideoModel song = new VideoModel();
        song.setId(id);
        song.setName(name);
        song.setImage(image);
        song.setVideo(songUrl);
        song.setFavStatus(fav);
        song.setPlayStatus(play);
        song.setdescription(desc);
        return song;
    }
    public List<VideoModel> getList() {
        return list;
    }

    public void setList(List<VideoModel> list) {
        this.list = list;
    }



    @Override

    public void onBackPressed()
    {
        super.onBackPressed();

        finish();

    }

    @Override
    public  void onResume()
    {
        super.onResume();
    }
    @Override
    public  void onPause()
    {
        super.onPause();
    }
}
