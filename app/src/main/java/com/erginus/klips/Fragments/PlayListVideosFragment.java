package com.erginus.klips.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
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
import com.erginus.klips.Adapter.PlayListAdapter;

import com.erginus.klips.Adapter.VideoPlayListAdapter;
import com.erginus.klips.Commons.EndlessListView;
import com.erginus.klips.Commons.MapAppConstant;
import com.erginus.klips.Commons.Prefshelper;
import com.erginus.klips.Commons.VolleySingleton;
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


public class PlayListVideosFragment extends Fragment {

   public static List<VideoModel> list;

    Prefshelper prefshelper;

    int id;
    String name;
    EndlessListView endlessListView;
    private boolean mHaveMoreDataToLoad=true;
    int page=0;
    public  static VideoPlayListAdapter horizontalListAdapter;

    public PlayListVideosFragment() {
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
       View rootview= inflater.inflate(R.layout.fragment_top_songs, container, false);
       prefshelper=new Prefshelper(getActivity());
        list=new ArrayList<>();
        endlessListView=(EndlessListView)rootview.findViewById(R.id.listView);
        endlessListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                prefshelper.storeWheelItemToPreference("2");
                Intent intent = new Intent(getActivity(), VideoDetailActivity.class);
                intent.putExtra("list", (Serializable) list);
                intent.putExtra("currentIndex",position);
                intent.putExtra("title", list.get(position).getName());
                intent.putExtra("image", list.get(position).getImage());
                intent.putExtra("id", "video");
                intent.putExtra("play_status", list.get(position).getPlayStatus());
                intent.putExtra("status",list.get(position).getFavStatus());
                startActivity(intent);
                getActivity().overridePendingTransition(0, 0);
            }
        });

        endlessListView.setOnLoadMoreListener(new EndlessListView.OnLoadMoreListener() {
            @Override
            public boolean onLoadMore() {
                if (mHaveMoreDataToLoad==true) {
                    loadMoreData();
                } else {
                    Toast.makeText(getActivity(), "No more data to load",
                            Toast.LENGTH_SHORT).show();
                }

                return mHaveMoreDataToLoad;

            }
        });
getMyVideos(page);
        return  rootview;
    }
    private void loadMoreData() {
        page++;
        getMyVideos(page);

    }
    public void getMyVideos(final int page) {
        try {
            final ProgressDialog pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "countries " + MapAppConstant.API + "video_playlist");
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "video_playlist", new Response.Listener<String>() {
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
                                    String id = "", video_name = "", artist_name="", image="", video_file="",playlist_status;
                                    String fav_status="", desc;
                                    if(jsonArray.length()>0)
                                    {       getVideoList().clear();
                                        for(int i=0; i<jsonArray.length(); i++) {
                                            JSONObject object1 = jsonArray.getJSONObject(i);

                                            id = object1.getString("video_id");
                                            video_name = object1.getString("video_name");
                                            desc=object1.getString("video_description");
                                            fav_status=object1.getString("favourite_status");
                                            playlist_status=object1.getString("playlist_status");
                                            video_file = object1.getString("video_file_url");
                                            image = object1.getString("video_thumbnail_url");
                                            list.add(videoModel(id, video_name, image, video_file, fav_status,playlist_status, desc));
                                        }
                                        setVideoList(list);
                                    } else
                                    {
                                        mHaveMoreDataToLoad=false;
                                    }
                                    if(endlessListView.getAdapter()==null){
                                        horizontalListAdapter=new VideoPlayListAdapter(getActivity(),list);
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
                    params.put("user_id", prefshelper.getUserIdFromPreference());
                    params.put("user_security_hash", prefshelper.getUserSecHashFromPreference());
                    params.put("page",page+"");
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
    private VideoModel  videoModel(String id,String name, String image, String video_file, String fav, String play, String desc)
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
    public List<VideoModel> getVideoList() {
        return list;
    }

    public void setVideoList(List<VideoModel> list) {
        PlayListVideosFragment.list = list;
    }
    @Override
    public void onResume()
    {
        super.onResume();
        getMyVideos(page);
    }

}
