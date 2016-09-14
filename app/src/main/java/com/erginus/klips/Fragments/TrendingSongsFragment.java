package com.erginus.klips.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.erginus.klips.Adapter.FullListAdapter;
import com.erginus.klips.Adapter.PlayListAdapter;
import com.erginus.klips.Commons.EndlessListView;
import com.erginus.klips.Commons.MapAppConstant;
import com.erginus.klips.Commons.Prefshelper;
import com.erginus.klips.Commons.VolleySingleton;
import com.erginus.klips.GuestHomeActivity;
import com.erginus.klips.HomeActivity;
import com.erginus.klips.Model.VideoModel;
import com.erginus.klips.R;
import com.erginus.klips.VideoDetailActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TrendingSongsFragment extends Fragment {
    List<VideoModel> list;
    Prefshelper prefshelper;
    String name, cat_id;
    EndlessListView endlessListView;
    private boolean mHaveMoreDataToLoad;
    int page=0, pos;
    FullListAdapter horizontalListAdapter;
    public TrendingSongsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_home).setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                // app icon in action bar clicked; goto parent activity.

                if(prefshelper.getLoginWithFromPreference().equals("0"))
                {
                    Intent intent=new Intent(getActivity(), GuestHomeActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent=new Intent(getActivity(), HomeActivity.class);
                    startActivity(intent);
                }
                //overridePendingTransition(0, 0);
                // return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View rootview= inflater.inflate(R.layout.fragment_trending, container, false);

        list=new ArrayList<VideoModel>();
       prefshelper=new Prefshelper(getActivity());
       getSongList();
        if(prefshelper.getLoginWithFromPreference().equalsIgnoreCase("0"))
        {
            GuestHomeActivity.txt_title.setText("Trending");
        }else {
            HomeActivity.txt_title.setText("Trending");
        }
        endlessListView=(EndlessListView)rootview.findViewById(R.id.listView);
        endlessListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                prefshelper.storeWheelItemToPreference("2");
                Intent intent = new Intent(getActivity(), VideoDetailActivity.class);
                intent.putExtra("list", (Serializable) list);
                intent.putExtra("currentIndex", position);
                intent.putExtra("title", list.get(position).getName());
                intent.putExtra("id", "music");
                intent.putExtra("status", "1");
                intent.putExtra("image", list.get(position).getImage());
                intent.putExtra("play_status", list.get(position).getPlayStatus());

                startActivity(intent);
                getActivity().overridePendingTransition(0, 0);
            }
        });
  /*      endlessListView.setOnLoadMoreListener(new EndlessListView.OnLoadMoreListener() {
            @Override
            public boolean onLoadMore() {
                if (mHaveMoreDataToLoad) {
                    loadMoreData();
                } else {
                    Toast.makeText(getActivity(), "No more data to load",
                            Toast.LENGTH_SHORT).show();
                }

                return mHaveMoreDataToLoad;

            }
        });*/
        return  rootview;
    }
  /*  private void loadMoreData() {
        page++;
        getSongList(page);

    }*/


    public void getSongList() {
        try {
            final ProgressDialog pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "countries " + MapAppConstant.API + "trending_musics");
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "trending_musics", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.d("", ".......response====" + response.toString());

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                      //  Toast.makeText(getActivity(), "" + serverMessage, Toast.LENGTH_SHORT).show();
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {
                                    JSONArray jsonArray = object.getJSONArray("data");
                                    String id = "", song_name = "", artist_name="", image="", song, fav_status,playlist_status;
                                    String nameArtist="", desc="";
                                    if(jsonArray.length()>0)
                                    {
                                        getList().clear();
                                        for(int i=0; i<jsonArray.length(); i++) {
                                            JSONObject object1 = jsonArray.getJSONObject(i);
                                            cat_id=object1.getString("music_categories_id");
                                            id = object1.getString("musics_id");
                                            song_name = object1.getString("music_name");
                                            JSONArray jsonArray1=object1.getJSONArray("artists_details");
                                            if(jsonArray1.length()>0)
                                            {
                                                for(int j=0; j<jsonArray1.length(); j++) {
                                                    JSONObject object2 = jsonArray1.getJSONObject(j);
                                                    artist_name = object2.getString("artist_name");

                                                    nameArtist=nameArtist+" "+artist_name+", ";
                                                }

                                            }
                                            song=object1.getString("music_file_url");
                                            image = object1.getString("music_thumbnail_url");
                                            fav_status=object1.getString("favourite_status");
                                            playlist_status=object1.getString("playlist_status");
                                            desc=object1.getString("music_description");
                                            list.add(songsModel(id, song_name,nameArtist, image, song, fav_status,playlist_status,desc));
                                        }
                                        setList(list);
                                    }
                                }
                                if(endlessListView.getAdapter()==null){
                                    horizontalListAdapter=new FullListAdapter(getActivity(),list);
                                    endlessListView.setAdapter(horizontalListAdapter);

                                }
                                else{
                                    endlessListView.loadMoreCompleat();
                                    horizontalListAdapter.notifyDataSetChanged();

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
                        Toast.makeText(getActivity(), "Timeout Error",
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
                    params.put("user_language", prefshelper.getUserLanguageFromPreference());
                    params.put("user_id", prefshelper.getUserIdFromPreference());
                    params.put("user_security_hash", prefshelper.getUserSecHashFromPreference());

                    return params;
                }
            };
            sr.setShouldCache(true);

            sr.setRetryPolicy(new DefaultRetryPolicy(50000 * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(sr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private VideoModel songsModel(String id, String name,String artist, String image , String songUrl, String fav, String play, String desc) {
        VideoModel song = new VideoModel();
        song.setId(id);
        song.setName(name);
        song.setArtistName(artist);
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
