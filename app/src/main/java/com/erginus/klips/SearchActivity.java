package com.erginus.klips;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.erginus.klips.Adapter.FullImageListAdapter;
import com.erginus.klips.Adapter.FullListAdapter;
import com.erginus.klips.Adapter.FullQuoteListAdapter;
import com.erginus.klips.Adapter.ImageListAdapter;
import com.erginus.klips.Adapter.MyImagesAdapter;
import com.erginus.klips.Adapter.MyQuotesAdapter;
import com.erginus.klips.Adapter.PlayListAdapter;
import com.erginus.klips.Adapter.QuoteListAdapter;
import com.erginus.klips.Commons.EndlessListView;
import com.erginus.klips.Commons.MapAppConstant;
import com.erginus.klips.Commons.Prefshelper;
import com.erginus.klips.Commons.VolleySingleton;
import com.erginus.klips.Model.ImageModel;
import com.erginus.klips.Model.QuoteModel;
import com.erginus.klips.Model.VideoModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {
   
    Prefshelper prefshelper;
    String name;
    public  EndlessListView endlessListView;

    EditText edt_search;
    ImageView img_cross;
    ImageView  img_back, img_search;
    public   List<VideoModel> list, mlist;
    public  List<ImageModel> image_list;
    public List<QuoteModel> qList;
    FullListAdapter horizontalListAdapter, madapter;
    FullQuoteListAdapter adapter;
    FullImageListAdapter listAdapter;
    TextView txt_vdo, txt_music, txt_img, txt_quote;
    public  int  category=2, page1=0, page2=0, page3=0, page4=0;
    private boolean mIsLoading=true;
    String finalQuery;
    String searchtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_avtivity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        img_cross=(ImageView)toolbar.findViewById(R.id.cross);
        img_back=(ImageView)toolbar.findViewById(R.id.back);
        edt_search=(EditText)toolbar.findViewById(R.id.toolbar_title);
         prefshelper=new Prefshelper(this);
        txt_vdo=(TextView)findViewById(R.id.text_video);
        txt_music=(TextView)findViewById(R.id.text_music);
        txt_img=(TextView)findViewById(R.id.text_images);
        txt_quote=(TextView)findViewById(R.id.text_quotes);
        endlessListView=(EndlessListView)findViewById(R.id.listView);

        list=new ArrayList<>();
        mlist=new ArrayList<>();
        qList=new ArrayList<>();
        image_list=new ArrayList<>();
        txt_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category=1;
                txt_music.setTextColor(getResources().getColor(R.color.white));
                txt_music.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.music_white, 0, 0, 0);
                txt_vdo.setTextColor(getResources().getColor(R.color.darkpurple));
                txt_vdo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.video_color, 0, 0, 0);
                txt_img.setTextColor(getResources().getColor(R.color.darkpurple));
                txt_img.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.galry, 0, 0, 0);
                txt_quote.setTextColor(getResources().getColor(R.color.darkpurple));
                txt_quote.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.quot_color, 0, 0, 0);
                if(edt_search.getText().toString().equals(""))
                {
                    getMList().clear();
                    getList().clear();
                    getVideoList().clear();
                    getQList().clear();

                    Toast.makeText(SearchActivity.this,"Please Enter some text to search", Toast.LENGTH_SHORT).show();
                }

            }
        });
        txt_vdo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = 2;
                txt_music.setTextColor(getResources().getColor(R.color.darkpurple));
                txt_music.setCompoundDrawablesWithIntrinsicBounds(R.drawable.music_color, 0, 0, 0);
                txt_vdo.setTextColor(getResources().getColor(R.color.white));
                txt_vdo.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.video_white, 0, 0, 0);
                txt_img.setTextColor(getResources().getColor(R.color.darkpurple));
                txt_img.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.galry, 0, 0, 0);
                txt_quote.setTextColor(getResources().getColor(R.color.darkpurple));
                txt_quote.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.quot_color, 0, 0, 0);
                if(edt_search.getText().toString().equals(""))
                {
                    getMList().clear();
                    getList().clear();
                    getVideoList().clear();
                    getQList().clear();

                    Toast.makeText(SearchActivity.this,"Please Enter some text to search", Toast.LENGTH_SHORT).show();
                }

            }
        });
        txt_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                category = 3;
                txt_music.setTextColor(getResources().getColor(R.color.darkpurple));
                txt_music.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.music_color, 0, 0, 0);
                txt_vdo.setTextColor(getResources().getColor(R.color.darkpurple));
                txt_vdo.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.video_color, 0, 0, 0);
                txt_img.setTextColor(getResources().getColor(R.color.white));
                txt_img.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.galery_small, 0, 0, 0);
                txt_quote.setTextColor(getResources().getColor(R.color.darkpurple));
                txt_quote.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.quot_color, 0, 0, 0);

                if(edt_search.getText().toString().equals(""))
                {
                    getMList().clear();
                    getList().clear();
                    getVideoList().clear();
                    getQList().clear();
                    Toast.makeText(SearchActivity.this,"Please Enter some text to search", Toast.LENGTH_SHORT).show();
                }

            }
        });
        txt_quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category=4;
                txt_music.setTextColor(getResources().getColor(R.color.darkpurple));
                txt_music.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.music_color, 0, 0, 0);
                txt_vdo.setTextColor(getResources().getColor(R.color.darkpurple));
                txt_vdo.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.video_color, 0, 0, 0);
                txt_img.setTextColor(getResources().getColor(R.color.darkpurple));
                txt_img.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.galry, 0, 0, 0);
                txt_quote.setTextColor(getResources().getColor(R.color.white));
                txt_quote.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.quote_white, 0, 0, 0);

                if(edt_search.getText().toString().equals(""))
                {
                    getMList().clear();
                    getList().clear();
                    getVideoList().clear();
                    getQList().clear();

                    Toast.makeText(SearchActivity.this,"Please Enter some text to search", Toast.LENGTH_SHORT).show();
                }

            }
        });
img_cross.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);


        if(edt_search.getText().toString().equals(""))
        {
            getMList().clear();
            getList().clear();
            getVideoList().clear();
            getQList().clear();
            Toast.makeText(SearchActivity.this,"Please Enter some text to search", Toast.LENGTH_SHORT).show();
        }
        else {
            getMList().clear();
            getList().clear();
            getVideoList().clear();
            getQList().clear();
            if (txt_vdo.getCurrentTextColor() == getResources().getColor(R.color.white)) {
                InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                img_back.setVisibility(View.VISIBLE);
                //img_search.setVisibility(View.GONE);
                category = 2;
                finalQuery = edt_search.getText().toString();
                getVideoList().clear();
                search(finalQuery, page2);

                endlessListView.setOnLoadMoreListener(new EndlessListView.OnLoadMoreListener() {
                    @Override
                    public boolean onLoadMore() {
                        if (mIsLoading) {
                            page2++;
                            search(finalQuery, page2);
                        } else {
                            Toast.makeText(SearchActivity.this, "No more data to load",
                                    Toast.LENGTH_SHORT).show();
                        }
                        return mIsLoading;

                    }
                });

            }
            if (txt_music.getCurrentTextColor() == getResources().getColor(R.color.white)) {
                InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                img_back.setVisibility(View.VISIBLE);
                //img_search.setVisibility(View.GONE);
                category = 1;
                finalQuery = edt_search.getText().toString();
                getMList().clear();
                search(finalQuery, page1);
                endlessListView.setOnLoadMoreListener(new EndlessListView.OnLoadMoreListener() {
                    @Override
                    public boolean onLoadMore() {
                        if (mIsLoading) {
                            page1++;
                            search(finalQuery, page1);
                        } else {
                            Toast.makeText(SearchActivity.this, "No more data to load",
                                    Toast.LENGTH_SHORT).show();
                        }
                        return mIsLoading;

                    }
                });

            }
            if (txt_img.getCurrentTextColor() == getResources().getColor(R.color.white)) {
                InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                img_back.setVisibility(View.VISIBLE);
                //img_search.setVisibility(View.GONE);
                category = 3;
                finalQuery = edt_search.getText().toString();
                getList().clear();
                search(finalQuery, page3);
                endlessListView.setOnLoadMoreListener(new EndlessListView.OnLoadMoreListener() {
                    @Override
                    public boolean onLoadMore() {
                        if (mIsLoading) {
                            page3++;
                            search(finalQuery, page3);
                        } else {
                            Toast.makeText(SearchActivity.this, "No more data to load",
                                    Toast.LENGTH_SHORT).show();
                        }
                        return mIsLoading;

                    }
                });

            }
            if (txt_quote.getCurrentTextColor() == getResources().getColor(R.color.white)) {
                InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                img_back.setVisibility(View.VISIBLE);
                //img_search.setVisibility(View.GONE);
                category = 4;
                finalQuery = edt_search.getText().toString();
                getQList().clear();
                search(finalQuery, page4);
                endlessListView.setOnLoadMoreListener(new EndlessListView.OnLoadMoreListener() {
                    @Override
                    public boolean onLoadMore() {
                        if (mIsLoading) {
                            page4++;
                            search(finalQuery, page4);
                        } else {
                            Toast.makeText(SearchActivity.this, "No more data to load",
                                    Toast.LENGTH_SHORT).show();
                        }
                        return mIsLoading;

                    }
                });

            }
        }
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
});
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
              /*  Intent intent = new Intent(SearchActivity.this, HomeActivity.class);
                startActivity(intent);*/
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });

        edt_search.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    img_back.setVisibility(View.VISIBLE);
                    //img_search.setVisibility(View.GONE);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    if(edt_search.getText().toString().equals(""))
                    {
                        getMList().clear();
                        getList().clear();
                        getVideoList().clear();
                        getQList().clear();
                        Toast.makeText(SearchActivity.this,"Please Enter some text to search", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        getMList().clear();
                        getList().clear();
                        getVideoList().clear();
                        getQList().clear();
                        if (txt_vdo.getCurrentTextColor() == getResources().getColor(R.color.white)) {
                            InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm2.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                            img_back.setVisibility(View.VISIBLE);
                            //img_search.setVisibility(View.GONE);
                            //category = 2;
                            finalQuery = edt_search.getText().toString();
                            getVideoList().clear();
                            search(finalQuery, page2);

                            endlessListView.setOnLoadMoreListener(new EndlessListView.OnLoadMoreListener() {
                                @Override
                                public boolean onLoadMore() {
                                    if (mIsLoading) {
                                        page2++;
                                        search(finalQuery, page2);
                                    } else {
                                        Toast.makeText(SearchActivity.this, "No more data to load",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    return mIsLoading;

                                }
                            });

                        }
                        if (txt_music.getCurrentTextColor() == getResources().getColor(R.color.white)) {
                            InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm2.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                            img_back.setVisibility(View.VISIBLE);
                            //img_search.setVisibility(View.GONE);
                            category = 1;
                            finalQuery = edt_search.getText().toString();
                            getMList().clear();
                            search(finalQuery, page1);
                            endlessListView.setOnLoadMoreListener(new EndlessListView.OnLoadMoreListener() {
                                @Override
                                public boolean onLoadMore() {
                                    if (mIsLoading) {
                                        page1++;
                                        search(finalQuery, page1);
                                    } else {
                                        Toast.makeText(SearchActivity.this, "No more data to load",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    return mIsLoading;

                                }
                            });

                        }
                        if (txt_img.getCurrentTextColor() == getResources().getColor(R.color.white)) {
                            InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm2.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                            img_back.setVisibility(View.VISIBLE);
                            //img_search.setVisibility(View.GONE);
                            category = 3;
                            finalQuery = edt_search.getText().toString();
                            getList().clear();
                            search(finalQuery, page3);
                            endlessListView.setOnLoadMoreListener(new EndlessListView.OnLoadMoreListener() {
                                @Override
                                public boolean onLoadMore() {
                                    if (mIsLoading) {
                                        page3++;
                                        search(finalQuery, page3);
                                    } else {
                                        Toast.makeText(SearchActivity.this, "No more data to load",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    return mIsLoading;

                                }
                            });

                        }
                        if (txt_quote.getCurrentTextColor() == getResources().getColor(R.color.white)) {
                            InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm2.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                            img_back.setVisibility(View.VISIBLE);
                            //img_search.setVisibility(View.GONE);
                            category = 4;
                            finalQuery = edt_search.getText().toString();
                            getQList().clear();
                            search(finalQuery, page4);
                            endlessListView.setOnLoadMoreListener(new EndlessListView.OnLoadMoreListener() {
                                @Override
                                public boolean onLoadMore() {
                                    if (mIsLoading) {
                                        page4++;
                                        search(finalQuery, page4);
                                    } else {
                                        Toast.makeText(SearchActivity.this, "No more data to load",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    return mIsLoading;

                                }
                            });

                        }
                    }
                    return true;
                }


                return false;
            }

          });

        endlessListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (category == 2) {
                    Intent intent = new Intent(SearchActivity.this, VideoDetailActivity.class);
                    intent.putExtra("list", (Serializable) list);
                    intent.putExtra("currentIndex", position);
                    intent.putExtra("title", list.get(position).getName());
                    intent.putExtra("artist",list.get(position).getArtistName());
                    intent.putExtra("id", "video");
                    intent.putExtra("status", list.get(position).getFavStatus());
                    intent.putExtra("play_status", list.get(position).getPlayStatus());
                    intent.putExtra("image", list.get(position).getImage());

                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    Log.d("listtttttttttttttttt", list.toString());
                }
                if (category == 1) {

                    Intent intent = new Intent(SearchActivity.this, VideoDetailActivity.class);
                    intent.putExtra("list", (Serializable) mlist);
                    intent.putExtra("currentIndex", position);
                    intent.putExtra("title", mlist.get(position).getName());
                    intent.putExtra("artist",mlist.get(position).getArtistName());
                    intent.putExtra("id", "music");
                    intent.putExtra("status", mlist.get(position).getFavStatus());
                    intent.putExtra("play_status", mlist.get(position).getPlayStatus());
                    intent.putExtra("image", mlist.get(position).getImage());
                    startActivity(intent);
                    overridePendingTransition(0, 0);

                }

                if (category == 3) {
                    Intent intent = new Intent(SearchActivity.this, ImageDetailActivity.class);
                    intent.putExtra("list1", (Serializable) image_list);
                    intent.putExtra("title", image_list.get(position).getName());
                    intent.putExtra("id", image_list.get(position).getId());
                    intent.putExtra("image", image_list.get(position).getImage());
                    intent.putExtra("status", image_list.get(position).getFavStatus());

                    startActivity(intent);
                    overridePendingTransition(0, 0);

                }
                if (category == 4) {
                    Intent intent = new Intent(SearchActivity.this, QuoteDetailActivity.class);
                    intent.putExtra("list1", (Serializable) qList);
                    intent.putExtra("title", qList.get(position).getName());
                    intent.putExtra("id", qList.get(position).getId());
                    intent.putExtra("cat_id", "quotes");
                    intent.putExtra("status", qList.get(position).getFavStatus());
                    intent.putExtra("image", qList.get(position).getImage());
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }

            }
        });

    }
    public void search(final String query, final int page) {
        try {

            final ProgressDialog pDialog = new ProgressDialog(this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "countries " + MapAppConstant.API + "search");
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "search", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.d("", ".......response====" + response.toString());
                    ////////
                    try {


                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String category = object.getString("category");
                        String serverMessage = object.getString("message");
                        Toast.makeText(SearchActivity.this, "" + serverMessage, Toast.LENGTH_SHORT).show();
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {

                            try {
                                String id = "", fav_status = "", v_id = "",description = "", video_name = "", artist_name = "", image = "", video_file = "";
                                String nameArtist = "",playlist_status="";
                                if ("1".equals(serverCode)) {
                                    if (category.equals("1")) {

                                        JSONArray jsonArray = object.getJSONArray("data");


                                        if (jsonArray.length() > 0) {
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject object1 = jsonArray.getJSONObject(i);

                                                v_id = object1.getString("music_id");
                                                video_name = object1.getString("music_name");
                                                video_file = object1.getString("music_file_url");
                                                image = object1.getString("music_thumbnail_url");
                                                fav_status=object1.getString("favourite_status");
                                                playlist_status=object1.getString("playlist_status");
                                                description=object1.getString("music_description");

                                                mlist.add(videoModel(v_id, video_name, nameArtist, image, video_file, fav_status,playlist_status,description));

                                            }

                                            setMList(mlist);

                                        }else  {
                                            endlessListView.loadMoreCompleat();
                                            mIsLoading = false;
                                        }
                                        madapter = new FullListAdapter(SearchActivity.this, mlist);
                                           endlessListView.setAdapter(madapter);
                                         madapter.notifyDataSetChanged();

                                    }
                                }
                                if  (category.equals("2"))  {

                                    JSONArray jsonArray = object.getJSONArray("data");
                                    if (jsonArray.length() > 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object1 = jsonArray.getJSONObject(i);

                                            v_id = object1.getString("video_id");
                                            video_name = object1.getString("video_name");
                                            video_file = object1.getString("video_file_url");
                                            image = object1.getString("video_thumbnail_url");
                                            fav_status=object1.getString("favourite_status");
                                            playlist_status=object1.getString("playlist_status");
                                            description=object1.getString("video_description");
                                            list.add(videoModel2(v_id, video_name, image, video_file, fav_status,playlist_status,description));
                                        }

                                        setVideoList(list);

                                    } else  {
                                        endlessListView.loadMoreCompleat();
                                        mIsLoading = false;
                                    }
                                    horizontalListAdapter = new FullListAdapter(SearchActivity.this, list);
                                    endlessListView.setAdapter(horizontalListAdapter);
                                     horizontalListAdapter.notifyDataSetChanged();

                                    Log.d("listtttttttttttttttt", list.toString());

                                }

                                if  (category.equals("3"))  {

                                    JSONArray jsonArray = object.getJSONArray("data");
                                    if (jsonArray.length() > 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object1 = jsonArray.getJSONObject(i);

                                            v_id = object1.getString("image_id");
                                            video_name = object1.getString("image_title");
                                            video_file = object1.getString("image_file_url");
                                            image = object1.getString("image_thumbnail_url");
                                            fav_status=object1.getString("favourite_status");
                                           description=object1.getString("image_description");
                                            image_list.add(imageModel(v_id, video_name, image, video_file, fav_status, description));
                                        }

                                        setList(image_list);

                                    } else  {
                                        endlessListView.loadMoreCompleat();
                                        mIsLoading = false;
                                    }
                                    listAdapter = new FullImageListAdapter(SearchActivity.this, image_list);
                                    endlessListView.setAdapter(listAdapter);

                                    listAdapter.notifyDataSetChanged();

                                }

                                if (category.equals("4"))  {

                                    JSONArray jsonArray = object.getJSONArray("data");
                                    if (jsonArray.length() > 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object1 = jsonArray.getJSONObject(i);

                                            v_id = object1.getString("quote_id");
                                            video_name = object1.getString("quote_title");
                                            video_file = object1.getString("quote_image_url");
                                            image = object1.getString("quote_thumbnail_url");
                                            fav_status=object1.getString("favourite_status");
description= object1.getString("quote_description");
                                            qList.add(model(v_id, video_name, image, video_file, fav_status,description));
                                        }

                                        setQList(qList);

                                    } else  {
                                        endlessListView.loadMoreCompleat();
                                        mIsLoading = false;
                                    }

                                    adapter = new FullQuoteListAdapter(SearchActivity.this, qList);
                                    endlessListView.setAdapter(adapter);

                                    adapter.notifyDataSetChanged();

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
                        Toast.makeText(SearchActivity.this, "Timeout Error",
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

                    params.put("page", ""+page);
                    params.put("search", query);
                    params.put("category",""+category );
                    params.put("user_id", prefshelper.getUserIdFromPreference());
                    params.put("user_security_hash", prefshelper.getUserSecHashFromPreference());

                    return params;
                }
            };
            sr.setShouldCache(true);

            sr.setRetryPolicy(new DefaultRetryPolicy(50000 * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(sr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
  @Override
  public  void onBackPressed()
  {
      super.onBackPressed();
      finish();
     /* Intent intent=new Intent(SearchActivity.this, HomeActivity.class);
      startActivity(intent);*/
      overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
  }
    private VideoModel videoModel2(String id,String name ,String image, String video_file, String fav, String play,String desc)
    {
        VideoModel video = new VideoModel();
        video.setId(id);
        video.setName(name);
video.setdescription(desc);
        video.setImage(image);
        video.setVideo(video_file);
video.setFavStatus(fav);
        video.setPlayStatus(play);
        return video;
    }
    private VideoModel videoModel(String id,String name,String artist ,String image, String video_file, String fav, String play,String desc)
    {
        VideoModel video = new VideoModel();
        video.setId(id);
        video.setName(name);
        video.setArtistName(artist);
        video.setImage(image);
        video.setVideo(video_file);
        video.setFavStatus(fav);
        video.setPlayStatus(play);
        video.setdescription(desc);
        return video;
    }
    public List<VideoModel> getVideoList() {
        return list;
    }

    public void setVideoList(List<VideoModel> list) {
        this.list = list;
    }
    public List<VideoModel> getMList() {
        return mlist;
    }

    public void setMList(List<VideoModel> list) {
        this.mlist = list;
    }


    private ImageModel imageModel(String id,String name,String thumbnail,String image, String fav,String desc )
    {
        ImageModel imageModel = new ImageModel();
        imageModel.setId(id);
        imageModel.setdescription(desc);
        imageModel.setName(name);
        imageModel.setThumbnail(thumbnail);
        imageModel.setImage(image);
imageModel.setFavStatus(fav);
        return imageModel;
    }
    public List<ImageModel> getList() {
        return image_list;
    }

    public void setList(List<ImageModel> list) {
        this.image_list = list;
    }
    private QuoteModel model(String id,String name,String thumbnail,String image, String fav,String desc )
    {
        QuoteModel imageModel = new QuoteModel();
        imageModel.setId(id);
        imageModel.setdescription(desc);
        imageModel.setName(name);
        imageModel.setThumbnail(thumbnail);
        imageModel.setImage(image);
        imageModel.setFavStatus(fav);
        return imageModel;
    }
    public List<QuoteModel> getQList() {
        return qList;
    }

    public void setQList(List<QuoteModel> list) {
        this.qList = list;
    }
}
