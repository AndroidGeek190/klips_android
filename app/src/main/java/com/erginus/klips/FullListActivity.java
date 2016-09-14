package com.erginus.klips;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
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
import com.erginus.klips.Adapter.FavListAdapter;
import com.erginus.klips.Adapter.FullImageListAdapter;
import com.erginus.klips.Adapter.FullListAdapter;
import com.erginus.klips.Adapter.FullQuoteListAdapter;
import com.erginus.klips.Adapter.GalleryAdapter;
import com.erginus.klips.Adapter.ImageListAdapter;
import com.erginus.klips.Adapter.QuoteListAdapter;
import com.erginus.klips.Adapter.QuotesAdapter;
import com.erginus.klips.Adapter.VideoListAdapter;
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

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class FullListActivity extends AppCompatActivity {
    List<VideoModel> list,list_new;
    List<QuoteModel> qlist;
    List<ImageModel> ilist;
    Prefshelper prefshelper;
    EndlessListView endlessListView;
    LinearLayout ll_back;
    public static TextView txtTitle;
    String category, title;
    int page=0,index ;
    private boolean mHaveMoreDataToLoad=true;
    FullListAdapter horizontalListAdapter, adapter;
    FullQuoteListAdapter quoteListAdapter;
    FullImageListAdapter imageListAdapter;
    ImageView img_home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_list);
        endlessListView=(EndlessListView)findViewById(R.id.listView);
        ll_back=(LinearLayout)findViewById(R.id.ll_navi);
        txtTitle=(TextView)findViewById(R.id.toolbar_title);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        prefshelper = new Prefshelper(this);
        list = new ArrayList<>();
        list_new = new ArrayList<>();
        qlist = new ArrayList<>();
        ilist = new ArrayList<>();
        category=getIntent().getStringExtra("category");
        index= Integer.parseInt(getIntent().getStringExtra("index"));
        title=getIntent().getStringExtra("title");
        img_home = (ImageView) findViewById(R.id.imageView_home);
        img_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
                if (prefshelper.getLoginWithFromPreference().equals("0")) {
                    Intent intent = new Intent(FullListActivity.this, GuestHomeActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(FullListActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            }
        });
        if(category.equalsIgnoreCase("music"))
        {
            getMusic(page);
            txtTitle.setText("Music"+" - "+title);
        }
        else  if(category.equalsIgnoreCase("video"))
        {
            getVideos(page);
            txtTitle.setText("Videos"+" - "+title);
        }
        else  if(category.equalsIgnoreCase("quote"))
        {
            getQuoteImages(page);
            txtTitle.setText("Quotes"+" - "+title);
        }
        else if(category.equalsIgnoreCase("image"))
        {
            getImages(page);
            txtTitle.setText("Images"+" - "+title);
        }
        endlessListView.setOnLoadMoreListener(new EndlessListView.OnLoadMoreListener() {
            @Override
            public boolean onLoadMore() {
                if (mHaveMoreDataToLoad==true) {
                    loadMoreData();
                } else {
                    Toast.makeText(FullListActivity.this, "No more data to load",
                            Toast.LENGTH_SHORT).show();
                }

                return mHaveMoreDataToLoad;

            }
        });
        endlessListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(category.equalsIgnoreCase("music"))
                {
                    prefshelper.storeWheelItemToPreference("2");
                    Intent intent = new Intent(FullListActivity.this, VideoDetailActivity.class);
                    intent.putExtra("list", (Serializable) list);
                    intent.putExtra("currentIndex", position);
                    intent.putExtra("title", list.get(position).getName());
                    intent.putExtra("id", "music");
                    intent.putExtra("status", list.get(position).getFavStatus());
                    intent.putExtra("image", list.get(position).getImage());
                    intent.putExtra("play_status", list.get(position).getPlayStatus());

                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
                else  if(category.equalsIgnoreCase("video"))
                {
                    prefshelper.storeWheelItemToPreference("2");
                    Intent intent = new Intent(FullListActivity.this, VideoDetailActivity.class);
                    intent.putExtra("list", (Serializable) list_new);
                    intent.putExtra("currentIndex", position);
                    intent.putExtra("title", list_new.get(position).getName());
                    intent.putExtra("id", "video");
                    intent.putExtra("image", list_new.get(position).getImage());
                    intent.putExtra("status", list_new.get(position).getFavStatus());
                    intent.putExtra("play_status", list_new.get(position).getPlayStatus());

                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
                else  if(category.equalsIgnoreCase("quote"))
                {
                    prefshelper.storeWheelItemToPreference("2");
                    Intent intent = new Intent(FullListActivity.this, QuoteDetailActivity.class);
                    intent.putExtra("title", qlist.get(position).getName());
                    intent.putExtra("list1", (Serializable) qlist);
                    intent.putExtra("id", qlist.get(position).getId());
                    intent.putExtra("cat_id","quotes");
                    intent.putExtra("status", "1");
                    intent.putExtra("image", qlist.get(position).getImage());
                    startActivity(intent);
                   overridePendingTransition(0, 0);
                }
                else if(category.equalsIgnoreCase("image"))
                {
                    prefshelper.storeWheelItemToPreference("2");
                    Intent intent = new Intent(FullListActivity.this, ImageDetailActivity.class);
                    intent.putExtra("list1", (Serializable) ilist);
                    intent.putExtra("title", ilist.get(position).getName());
                    intent.putExtra("id", ilist.get(position).getId());
                    intent.putExtra("image", ilist.get(position).getImage());
                    intent.putExtra("status", ilist.get(position).getFavStatus());

                    startActivity(intent);
                   overridePendingTransition(0, 0);
                }

            }
        });
    }
    private void loadMoreData() {

        if(category.equalsIgnoreCase("music"))
        {
            page++;
            getMusic(page);
        }
        else  if(category.equalsIgnoreCase("video"))
        {
            page++;
            getVideos(page);
        }
        else  if(category.equalsIgnoreCase("quote"))
        {
            page++;
            getQuoteImages(page);
        }
        else if(category.equalsIgnoreCase("image"))
        {
            page++;
            getImages(page);
        }

    }
    public void getMusic(final int page) {
        try {
            final ProgressDialog pDialog = new ProgressDialog(FullListActivity.this);
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
                                  
                                    Log.e("array size",""+jsonArray.length());

                                    String id = "", name = "", v_id = "", video_name = "", description = "",artist_name = "", image = "", video_file = "";
                                    String nameArtist = "", fav_status = "", playlist_status = "";
                                    if (jsonArray.length() > 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                           
                                         //   getVideoList().clear();
                                            JSONObject object1 = jsonArray.getJSONObject(i);

                                            id = object1.getString("music_category_id");
                                            name = object1.getString("music_category_name");
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
                                                    if(i==index){
                                                        list.add(videoModel(v_id, video_name, nameArtist, image, video_file, fav_status, playlist_status,description));
                                                    }
                                                       }
                                                setVideoList(list);
                                                Log.d("listttttttttttt",list+"");

                                            }else
                                            {
                                                mHaveMoreDataToLoad=false;
                                                      }
                                            if(endlessListView.getAdapter()==null){
                                                horizontalListAdapter=new FullListAdapter(FullListActivity.this,list);
                                                endlessListView.setAdapter(horizontalListAdapter);

                                            }
                                            else{
                                                endlessListView.loadMoreCompleat();
                                                horizontalListAdapter.notifyDataSetChanged();

                                            }
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
                        Toast.makeText(FullListActivity.this, "Timeout Error",
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
            VolleySingleton.getInstance(FullListActivity.this.getApplicationContext()).addToRequestQueue(sr);

        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public void getVideos(final int page) {
        try {
            final ProgressDialog pDialog = new ProgressDialog(FullListActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "countries " + MapAppConstant.API + "get_videos");
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "get_videos", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.d("", ".......response====" + response.toString());

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                        //  Toast.makeText(FullListActivity.this, "" + serverMessage, Toast.LENGTH_SHORT).show();
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {

                                    JSONArray jsonArray = object.getJSONArray("data");
                                    String  id,name = "", v_id = "", video_name = "",description = "" ,playlist_status = "", image = "", video_file = "";
                                    String fav_status = "";
                                    if (jsonArray.length() > 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                             JSONObject object1 = jsonArray.getJSONObject(i);

                                            id = object1.getString("video_category_id");
                                            name = object1.getString("video_category_name");
                                            JSONArray jsonArray1 = object1.getJSONArray("video_details");
                                            if (jsonArray1.length() > 0) {

                                                for (int j = 0; j < jsonArray1.length(); j++) {
                                                    JSONObject object2 = jsonArray1.getJSONObject(j);
                                                    v_id = object2.getString("video_id");
                                                    video_name = object2.getString("video_name");
                                                    video_file = object2.getString("video_file_url");
                                                    image = object2.getString("video_thumbnail_url");
                                                    fav_status=object2.getString("favourite_status");
                                                    playlist_status=object2.getString("playlist_status");
                                                    description=object2.getString("video_description");
                                                    if(i==index){

                                                    list_new.add(videoModel2(id,v_id, video_name, image, video_file, fav_status,playlist_status,description));
                                                }}

                                                setList(list_new);

                                            }else
                                            {
                                                mHaveMoreDataToLoad=false;
                                            }
                                            if(endlessListView.getAdapter()==null){
                                                adapter=new FullListAdapter(FullListActivity.this,list_new);
                                                endlessListView.setAdapter(adapter);

                                            }
                                            else{
                                                endlessListView.loadMoreCompleat();
                                                adapter.notifyDataSetChanged();

                                            }

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
                        Toast.makeText(FullListActivity.this, "Timeout Error",
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
            VolleySingleton.getInstance(FullListActivity.this.getApplicationContext()).addToRequestQueue(sr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
  


    private VideoModel videoModel2(String cat_id,String id,String name,String image, String video_file, String fav, String play,String desc)
    {
        VideoModel video = new VideoModel();
        video.setcat_Id(cat_id);
        video.setId(id);
        video.setName(name);
        video.setdescription(desc);
        //    video.setArtistName(artist);
        video.setImage(image);
        video.setVideo(video_file);
        video.setFavStatus(fav);
        video.setPlayStatus(play);
        return video;
    }
  
    
    public List<VideoModel> getList() {
        return list_new;
    }

    public void setList(List<VideoModel> list) {
        this.list_new = list;
    }
    public void getQuoteImages(final int page) {
        try {
            final ProgressDialog pDialog = new ProgressDialog(FullListActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "countries " + MapAppConstant.API + "get_quotes");
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "get_quotes", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.d("", ".......response====" + response.toString());

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                        //  Toast.makeText(FullListActivity.this, "" + serverMessage, Toast.LENGTH_SHORT).show();
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {

                                    JSONArray jsonArray = object.getJSONArray("data");
                                    String id = "",name="", quote_id="", qute_name = "", quote="", quote_file="";
                                    String fav_status="";
                                    if(jsonArray.length()>0)
                                    {

                                        for(int i=0; i<jsonArray.length(); i++) {
                                             JSONObject object1 = jsonArray.getJSONObject(i);

                                            id = object1.getString("quote_category_id");
                                            name = object1.getString("quote_category_name");
                                            JSONArray jsonArray1=object1.getJSONArray("quote_details");
                                            if(jsonArray1.length()>0)
                                            {
                                                for(int j=0; j<jsonArray1.length(); j++) {
                                                    JSONObject object2 = jsonArray1.getJSONObject(j);
                                                    quote_id = object2.getString("quote_id");
                                                    qute_name=object2.getString("quote_title");
                                                    quote_file = object2.getString("quote_file_url");
                                                    quote = object2.getString("quote_thumbnail_url");
                                                    fav_status=object2.getString("favourite_status");
                                                    if(i==index){

                                                    qlist.add(quotemodel(quote_id, qute_name, quote_file, quote, fav_status));
                                                }
                                                }
                                                setQList(qlist);

                                            }else
                                            {
                                                mHaveMoreDataToLoad=false;
                                            }
                                            if(endlessListView.getAdapter()==null){
                                                quoteListAdapter=new FullQuoteListAdapter(FullListActivity.this,qlist);
                                                endlessListView.setAdapter(quoteListAdapter);

                                            }
                                            else{
                                                endlessListView.loadMoreCompleat();
                                                quoteListAdapter.notifyDataSetChanged();

                                            }
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
                        Toast.makeText(FullListActivity.this, "Timeout Error",
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
            VolleySingleton.getInstance(FullListActivity.this.getApplicationContext()).addToRequestQueue(sr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
  
    private QuoteModel quotemodel(String id, String title, String quote, String thumb, String fav)
    {
        QuoteModel i= new QuoteModel();
        i.setId(id);
        i.setName(title);
        i.setImage(quote);
        i.setThumbnail(thumb);
        i.setFavStatus(fav);
        return i;
    }
    public List<QuoteModel> getQList() {
        return qlist;
    }

    public void setQList(List<QuoteModel> list) {
        this.qlist = list;
    }



    public void getImages(final int page) {
        try {
            final ProgressDialog pDialog = new ProgressDialog(FullListActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "countries " + MapAppConstant.API + "get_images");
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "get_images", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.d("", ".......response====" + response.toString());

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                        //  Toast.makeText(FullImageActivity.this, "" + serverMessage, Toast.LENGTH_SHORT).show();
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {

                                    JSONArray jsonArray = object.getJSONArray("data");
                                    String id = "",name="", image_id="", image_name = "", image="", image_file="";
                                    String fav_status="";
                                    if(jsonArray.length()>0)
                                    {

                                        for(int i=0; i<jsonArray.length(); i++) {
                                             JSONObject object1 = jsonArray.getJSONObject(i);

                                            id = object1.getString("image_category_id");
                                            name = object1.getString("image_category_name");
                                            JSONArray jsonArray1=object1.getJSONArray("image_details");
                                            if(jsonArray1.length()>0)
                                            {
                                                for(int j=0; j<jsonArray1.length(); j++) {
                                                    JSONObject object2 = jsonArray1.getJSONObject(j);
                                                    image_id = object2.getString("image_id");
                                                    image_name=object2.getString("image_title");
                                                    image_file = object2.getString("image_file_url");
                                                    image = object2.getString("image_thumbnail_url");
                                                    fav_status=object2.getString("favourite_status");
                                                    if(i==index) {
                                                        ilist.add(imageModel(image_id, image_name, image_file, image, fav_status));
                                                    }
                                                }
                                                setImageList(ilist);

                                            } else
                                            {
                                                mHaveMoreDataToLoad=false;
                                            }
                                            if(endlessListView.getAdapter()==null){
                                                imageListAdapter=new FullImageListAdapter(FullListActivity.this,ilist);
                                                endlessListView.setAdapter(imageListAdapter);

                                            }
                                            else{
                                                endlessListView.loadMoreCompleat();
                                                imageListAdapter.notifyDataSetChanged();

                                            }
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
                        Toast.makeText(FullListActivity.this, "Timeout Error",
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

                    params.put("current_timestamp", "");
                    params.put("page", ""+page);
                    params.put("user_id", prefshelper.getUserIdFromPreference());
                    params.put("user_security_hash", prefshelper.getUserSecHashFromPreference());

                    return params;
                }
            };
            sr.setShouldCache(true);

            sr.setRetryPolicy(new DefaultRetryPolicy(50000 * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(FullListActivity.this.getApplicationContext()).addToRequestQueue(sr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
  

    private ImageModel imageModel(String id,String name,String image,String thumbnail, String fav )
    {
        ImageModel imageModel = new ImageModel();
        imageModel.setId(id);
        imageModel.setName(name);
        imageModel.setImage(image);
        imageModel.setThumbnail(thumbnail);
        imageModel.setFavStatus(fav);
        return imageModel;
    }
    public List<ImageModel> getImageList() {
        return ilist;

    }

    public void setImageList(List<ImageModel> list) {
        this.ilist = list;

    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();

    }
}
