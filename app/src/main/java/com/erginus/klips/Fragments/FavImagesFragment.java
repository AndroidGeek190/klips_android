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

import android.widget.ListAdapter;
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

import com.erginus.klips.Adapter.ImageListAdapter;

import com.erginus.klips.Commons.EndlessListView;
import com.erginus.klips.Commons.MapAppConstant;
import com.erginus.klips.Commons.Prefshelper;
import com.erginus.klips.Commons.VolleySingleton;
import com.erginus.klips.ImageDetailActivity;
import com.erginus.klips.Model.ImageModel;

import com.erginus.klips.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FavImagesFragment extends Fragment {
    public  static List<ImageModel> list;
    Prefshelper prefshelper;
    String name;
    EndlessListView endlessListView;
    private boolean mHaveMoreDataToLoad=true;
    int page=0, pos=7;
   public static ImageListAdapter horizontalListAdapter;

    public FavImagesFragment() {
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
        list=new ArrayList<ImageModel>();
       prefshelper=new Prefshelper(getActivity());
       getImageList(page);



        endlessListView=(EndlessListView)rootview.findViewById(R.id.listView);
        endlessListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                prefshelper.storeWheelItemToPreference("2");
                Intent intent = new Intent(getActivity(), ImageDetailActivity.class);
                intent.putExtra("list1", (Serializable) list);
                intent.putExtra("title", list.get(position).getName());
                intent.putExtra("id", list.get(position).getId());
                intent.putExtra("image", list.get(position).getImage());
                intent.putExtra("status", "1");

                startActivity(intent);
                getActivity().overridePendingTransition(0, 0);
            }
        });
        endlessListView.setOnLoadMoreListener(new EndlessListView.OnLoadMoreListener() {
            @Override
            public boolean onLoadMore() {
                if (mHaveMoreDataToLoad == true) {
                    loadMoreData();
                } else {
                    Toast.makeText(getActivity(), "No more data to load",
                            Toast.LENGTH_SHORT).show();
                }

                return mHaveMoreDataToLoad;

            }
        });
        return  rootview;
    }
    private void loadMoreData() {
        page++;
        getImageList(page);

    }
    public void getImageList(final int page) {
        try {
            final ProgressDialog pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "countries " + MapAppConstant.API + "favourite_images");
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "favourite_images", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.d("", ".......response====" + response.toString());

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                     //   Toast.makeText(getActivity(), "" + serverMessage, Toast.LENGTH_SHORT).show();
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {
                                    JSONArray jsonArray = object.getJSONArray("data");
                                    String id = "", song_name = "", fav_status="",description="", image="", song, duration;
                                    String nameArtist="";
                                    if(jsonArray.length()>0)
                                    {
                                        getList().clear();
                                        for(int i=0; i<jsonArray.length(); i++) {
                                            JSONObject object1 = jsonArray.getJSONObject(i);

                                            id = object1.getString("image_id");
                                            String image_name = object1.getString("image_title");
                                            fav_status=object1.getString("favourite_status");

                                            String thumbnail=object1.getString("user_thumbnail_url");
                                            image = object1.getString("image_file_url");
                                            description=object1.getString("image_description");
                                            list.add(imageModel(id, image_name, thumbnail, image, fav_status,description));
                                        }
                                        setList(list);
                                    } else
                                    {
                                        mHaveMoreDataToLoad=false;
                                    }
                                    if(endlessListView.getAdapter()==null){
                                        horizontalListAdapter=new ImageListAdapter(getActivity(),list);
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
                    Log.d("id", prefshelper.getArtistIdFromPreference());
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
    private ImageModel imageModel(String id,String name,String thumbnail,String image, String fav,String desc)
    {
        ImageModel imageModel = new ImageModel();
        imageModel.setId(id);
        imageModel.setName(name);
        imageModel.setdescription(desc);
        imageModel.setThumbnail(thumbnail);
        imageModel.setImage(image);
        imageModel.setFavStatus(fav);
        return imageModel;
    }
    public List<ImageModel> getList() {
        return list;
    }

    public void setList(List<ImageModel> list) {
        FavImagesFragment.list = list;
    }
    @Override
    public void onResume()
    {
        super.onResume();
        getImageList(page);
    }

}
