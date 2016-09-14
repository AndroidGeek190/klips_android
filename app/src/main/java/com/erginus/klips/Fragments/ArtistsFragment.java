package com.erginus.klips.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.erginus.klips.Adapter.ArtistsAdapter;
import com.erginus.klips.ArtistDetailActivity;
import com.erginus.klips.Commons.EndlessListView;
import com.erginus.klips.Commons.MapAppConstant;
import com.erginus.klips.Commons.Prefshelper;
import com.erginus.klips.Commons.VolleySingleton;
import com.erginus.klips.GuestHomeActivity;
import com.erginus.klips.HomeActivity;
import com.erginus.klips.Model.ArtistModel;
import com.erginus.klips.R;
import com.erginus.klips.SearchActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArtistsFragment extends Fragment {

    List<ArtistModel> list;
    Prefshelper prefshelper;
    int page =0;
    ArtistsAdapter horizontalListAdapter;
    private EndlessListView listView_artists;
    private boolean mHaveMoreDataToLoad=true;

    public ArtistsFragment() {
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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview= inflater.inflate(R.layout.fragment_artists, container, false);

        list=new ArrayList<>();
        prefshelper=new Prefshelper(getActivity());
        if (prefshelper.getLoginWithFromPreference().equals("0")) {
            GuestHomeActivity.txt_title.setText("Artists");

        }
        else{
            HomeActivity.txt_title.setText("Artists");

        }
       listView_artists=(EndlessListView)rootview.findViewById(R.id.listView_artist);
        listView_artists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //  getActivity().finish();
                Intent intent = new Intent(getActivity(), ArtistDetailActivity.class);
                prefshelper.storeArtistIdToPreference(list.get(position).getId());
                intent.putExtra("name",  list.get(position).getName());
                intent.putExtra("desc",  list.get(position).getDescription());
                intent.putExtra("image", list.get(position).getImage());
                startActivity(intent);
                getActivity().overridePendingTransition(0,0);
            }
        });

        getArtistList(page);
        listView_artists.setOnLoadMoreListener(new EndlessListView.OnLoadMoreListener() {
            @Override
            public boolean onLoadMore() {
                if (mHaveMoreDataToLoad==true) {
                    loadMoreData();
                } else {
                    Toast.makeText(getActivity(), "No more data to load",Toast.LENGTH_SHORT).show();
                }
                return mHaveMoreDataToLoad;

            }
        });

        return  rootview;
    }
    private void loadMoreData() {
        page++;
        getArtistList(page);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                // app icon in action bar clicked; goto parent activity.

                if (prefshelper.getLoginWithFromPreference().equals("0")) {
                    Intent intent = new Intent(getActivity(), GuestHomeActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(intent);
                }
                //overridePendingTransition(0, 0);
               // return true;
            default:
                return super.onOptionsItemSelected(item);
        }
}

    public void getArtistList(final int page) {
        try {
            final ProgressDialog pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "countries " + MapAppConstant.API + "artists");
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "artists", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.d("", ".......response====" + response.toString());

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                       // Toast.makeText(getActivity(), "" + serverMessage, Toast.LENGTH_SHORT).show();
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {
                                    JSONArray jsonArray = object.getJSONArray("data");
                                    String id = "", desc = "", artist_name="", image="", thumbnail="";
                                    String nameArtist="";
                                    if(jsonArray.length()>0)
                                    {
                                        for(int i=0; i<jsonArray.length(); i++) {
                                            JSONObject object1 = jsonArray.getJSONObject(i);

                                            id = object1.getString("artist_id");
                                            artist_name = object1.getString("artist_name");
                                            desc = object1.getString("artist_description");

                                            image = object1.getString("artist_thumbnail_url");
                                            list.add(artistModel(id, artist_name,desc, image));
                                        }
                                        setList(list);


                                    } else
                                    {
                                        mHaveMoreDataToLoad=false;
                                    }
                                    if(listView_artists.getAdapter()==null){
                                        horizontalListAdapter=new ArtistsAdapter(getActivity(),list);
                                        listView_artists.setAdapter(horizontalListAdapter);

                                    }
                                    else{
                                        listView_artists.loadMoreCompleat();
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

                    params.put("page", ""+page);
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
    private ArtistModel artistModel(String id,String name, String desc,String image )
    {
        ArtistModel m = new ArtistModel();
        m.setId(id);
        m.setName(name);
        m.setDescription(desc);
        m.setImage(image);

        return m;
    }
    public List<ArtistModel> getList() {
        return list;
    }

    public void setList(List<ArtistModel> list) {
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
