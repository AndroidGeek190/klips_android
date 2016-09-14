package com.erginus.klips;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.erginus.klips.Adapter.HorizontalListAdapter;
import com.erginus.klips.Adapter.RecyclerAdapter;
import com.erginus.klips.Adapter.RecyclerVideoAdapter;
import com.erginus.klips.Commons.MapAppConstant;
import com.erginus.klips.Commons.Prefshelper;
import com.erginus.klips.Commons.VolleySingleton;
import com.erginus.klips.Model.VideoModel;


import org.json.JSONArray;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class MusicActivity extends AppCompatActivity {
    List<VideoModel> list;
    Prefshelper prefshelper;
    LinearLayout ll_back;
    ImageView imageh;
    TextView title_catgry;
    LinearLayout lm;
    LinearLayout.LayoutParams params;
    HorizontalListAdapter horizontalListAdapter;
    ImageView img_music,img_video,img_quote, img_image;
    int page=0,size;
    private boolean mHaveMoreDataToLoad;
    ImageView back,img_home;
    int pos=7;
    ViewPager viewPager;
    RelativeLayout layout;

    List viewslist,categoryList;
    RecyclerVideoAdapter adapter;
    TextView bt;
int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        viewslist=new ArrayList<>();
        lm = (LinearLayout) findViewById(R.id.ll_main);
        //ll_back = (LinearLayout) findViewById(R.id.ll_navi);
        back = (ImageView) findViewById(R.id.imageView_back);
        categoryList=new ArrayList<>();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefshelper.getLoginWithFromPreference().equals("0")) {
                    Intent intent = new Intent(MusicActivity.this, GuestHomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(MusicActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
//                Intent intent=new Intent(MusicActivity.this, HomeActivity.class);
//                startActivity(intent);

            }
        });
        img_home = (ImageView) findViewById(R.id.imageView_home);
        img_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent=new Intent(MusicActivity.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        prefshelper = new Prefshelper(this);
        params = new LinearLayout.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 10, 10, 10);
        ll_back = (LinearLayout) findViewById(R.id.ll_navi);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

            getVideos(page);

    }



    public void getVideos(final int page) {
        try {
            final ProgressDialog pDialog = new ProgressDialog(MusicActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "countries " + MapAppConstant.API + "get_musics");
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "get_musics", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.d("", ".......response====" + response.toString());

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                        //  Toast.makeText(MusicActivity.this, "" + serverMessage, Toast.LENGTH_SHORT).show();
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {

                                    JSONArray jsonArray = object.getJSONArray("data");
                                    size=jsonArray.length();
                                    Log.e("array size",""+jsonArray.length());

                                    String id = "", name = "", v_id = "", video_name = "", description = "",artist_name = "", image = "", video_file = "";
                                    String nameArtist = "", fav_status = "", playlist_status = "";
                                    if (jsonArray.length() > 0) {
                                        for ( int i = 0; i < jsonArray.length(); i++) {
                                            list = new ArrayList<>();
                                            getVideoList().clear();
                                            JSONObject object1 = jsonArray.getJSONObject(i);

                                            id = object1.getString("music_category_id");
                                            name = object1.getString("music_category_name");
                                           // viewslist.add(i,id);
                                            viewslist.add(count);
                                            categoryList.add(name);
                                            count++;
                                            JSONArray jsonArray1 = object1.getJSONArray("music_details");
                                            if (jsonArray1.length() > 0) {

                                                for (int j = 0; j < jsonArray1.length(); j++) {
                                                    JSONObject object2 = jsonArray1.getJSONObject(j);
                                                    v_id = object2.getString("music_id");
                                                    video_name = object2.getString("music_name");
                                                    video_file = object2.getString("music_file_url");
                                                    image = object2.getString("music_thumbnail_url");
                                                    fav_status = object2.getString("favourite_status");
                                                    playlist_status = object2.getString("playlist_status");
                                                    description=object2.getString("music_description");

                                                    JSONArray jsonArray2 = object2.getJSONArray("artists_details");
                                                    if (jsonArray2.length() > 0) {
                                                        for (int k = 0; k < jsonArray2.length(); k++) {
                                                            JSONObject object3 = jsonArray2.getJSONObject(k);
                                                            artist_name = object3.getString("artist_name");

                                                            nameArtist = nameArtist + " " + artist_name + ", ";
                                                        }

                                                    }
                                                    list.add(videoModel(v_id, video_name, nameArtist, image, video_file, fav_status, playlist_status,description));
                                                }
                                                setVideoList(list);
                                               Log.d("listttttttttttt",list+"");

                                                    //viewdata();


                                            }
                                            View mView = LayoutInflater.from(MusicActivity.this).inflate(R.layout.resycle, null);
                                            RecyclerView recyclerView2= (RecyclerView)mView.findViewById(R.id.my_recycler_view12);
                                            img_image=(ImageView)mView.findViewById(R.id.img_image);
                                            img_music=(ImageView)mView.findViewById(R.id.img_music);
                                            img_video=(ImageView)mView.findViewById(R.id.img_video);
                                            img_quote=(ImageView)mView.findViewById(R.id.img_quote);

                                            img_image.setVisibility(View.GONE);
                                            img_music.setVisibility(View.VISIBLE);
                                            img_quote.setVisibility(View.GONE);
                                            img_video.setVisibility(View.GONE);
                                            TextView caty=(TextView)mView.findViewById(R.id.catid);
                                            bt = (TextView) mView.findViewById(R.id.click);
                                           // bt.setTag(i);
                                            caty.setText(name);


                                            Log.e("i value",""+i);
                                            Log.e("view value",viewslist+"");

                                            final int finalI1 = i;
                                            bt.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                  //  Log.e("Category id", "" + viewslist.get(finalI1).toString());
                                                    Log.e("Category id", "" + viewslist.get(finalI1));
                                                    Intent intent = new Intent(MusicActivity.this, FullListActivity.class);
                                                    intent.putExtra("category", "music");
                                                    intent.putExtra("index","" + viewslist.get(finalI1));
                                                    intent.putExtra("title",""+categoryList.get(finalI1));
                                                    startActivity(intent);
                                                }
                                            });


                                            adapter=new RecyclerVideoAdapter(MusicActivity.this,list);
                                            recyclerView2.setAdapter(adapter);

                                            recyclerView2.setHasFixedSize(true);
                                            LinearLayoutManager llm = new LinearLayoutManager(MusicActivity.this);
                                            llm.setOrientation(LinearLayoutManager.HORIZONTAL);
                                            recyclerView2.setLayoutManager(llm);



                                            lm.addView(mView);

                                            }

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
                        Toast.makeText(MusicActivity.this, "Timeout Error",
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

                    params.put("user_language", prefshelper.getUserLanguageFromPreference());
                    params.put("page", ""+page);
                    params.put("user_id", prefshelper.getUserIdFromPreference());
                    params.put("user_security_hash", prefshelper.getUserSecHashFromPreference());

                    return params;
                }
            };
            sr.setShouldCache(true);

            sr.setRetryPolicy(new DefaultRetryPolicy(50000 * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(MusicActivity.this.getApplicationContext()).addToRequestQueue(sr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    public LinearLayout viewdata()
//    {
//
//        LinearLayout viewpagerlayout = new LinearLayout(MusicActivity.this);
//        params = new LinearLayout.LayoutParams(
//                MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
//        params.setMargins(10, 10, 10, 10);
//
//        viewPager  = new ViewPager(MusicActivity.this);
//
//        viewPager.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, 350));
//        int pagerPadding = 16;
//        viewPager.setClipToPadding(false);
//        viewPager.setPadding(pagerPadding, 16, 0, 0);
//        //viewPager.setBackgroundColor(Color.WHITE);
//        viewpagerlayout.addView(viewPager);
//
//        if (viewPager.getAdapter() == null) {
//            horizontalListAdapter = new HorizontalListAdapter(MusicActivity.this, list);
//            viewPager.setAdapter(horizontalListAdapter);
//            Log.e("horizontal in view", "" + horizontalListAdapter.getCount());
//
//        } else {
//
//            horizontalListAdapter.notifyDataSetChanged();
//            Log.e("horizontal in view....", "" + horizontalListAdapter.getCount());
//        }
//
//        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//               /* if(position==pos)
//                {
//                    loadMoreData();
//                }*/
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//        return viewpagerlayout;
//    }
    @Override
    public void onPause() {

        super.onPause();

    }

    private VideoModel videoModel(String id,String name, String artist ,String image, String video_file, String fav, String play,String desc) {
        VideoModel video = new VideoModel();
        video.setId(id);
        video.setName(name);
        video.setArtistName(artist);
        video.setdescription(desc);
        video.setImage(image);
        video.setVideo(video_file);
        video.setFavStatus(fav);
        video.setPlayStatus(play);
        return video;
    }
    public List<VideoModel> getVideoList() {
        return list;
    }

    public void setVideoList(List<VideoModel> list) {
        this.list = list;
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        if (prefshelper.getLoginWithFromPreference().equals("0")) {
            Intent intent = new Intent(MusicActivity.this, GuestHomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(MusicActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

    }
}

