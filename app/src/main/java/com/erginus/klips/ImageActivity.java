package com.erginus.klips;

import android.app.ActionBar;
import android.app.ProgressDialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;

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
import com.erginus.klips.Adapter.GalleryAdapter;
import com.erginus.klips.Adapter.RecyclerAdapter;
import com.erginus.klips.Adapter.Recycler_Adapter_Images;
import com.erginus.klips.Commons.MapAppConstant;
import com.erginus.klips.Commons.Prefshelper;
import com.erginus.klips.Commons.VolleySingleton;
import com.erginus.klips.Model.ImageModel;

import org.json.JSONArray;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class ImageActivity extends AppCompatActivity {

    View view;
    List<ImageModel> list;
    Prefshelper prefshelper;

    TextView  title_catgry;
ImageView imageh,back;
    LinearLayout ll_back;
    LinearLayout lm;
    LinearLayout.LayoutParams params;
    ImageView img_music,img_video,img_quote, img_image;
    int page=0;

    RelativeLayout layout;
    ImageView img_home;
    List viewslist, categoryList;
    Recycler_Adapter_Images adapter;
    TextView bt;
    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        categoryList=new ArrayList<>();
        viewslist=new ArrayList();
        img_home=(ImageView)findViewById(R.id.imageView_home);
        back=(ImageView)findViewById(R.id.imageView_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prefshelper.getLoginWithFromPreference().equals("0"))
                {
                    finish();
                    Intent intent=new Intent(ImageActivity.this, GuestHomeActivity.class);
                    startActivity(intent);
                }
                else
                {
                    finish();
                    Intent intent=new Intent(ImageActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            }
        });
        img_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent=new Intent(ImageActivity.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


        lm = (LinearLayout) findViewById(R.id.ll_main);
        params = new LinearLayout.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        params.setMargins(10,10,10,10);

        prefshelper=new Prefshelper(ImageActivity.this);


        getImages(page);
    }
    private void loadMoreData() {
        page++;
        getImages(page);

    }
    public void getImages(final int page) {
        try {
            final ProgressDialog pDialog = new ProgressDialog(ImageActivity.this);
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
                        //  Toast.makeText(ImageActivity.this, "" + serverMessage, Toast.LENGTH_SHORT).show();
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
                                            list = new ArrayList<>(i);
                                            getImageList().clear();
                                            JSONObject object1 = jsonArray.getJSONObject(i);

                                            id = object1.getString("image_category_id");
                                            name = object1.getString("image_category_name");
                                            categoryList.add(name);
                                            viewslist.add(count);
                                            count++;
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

                                                    list.add(imageModel(image_id, image_name, image_file, image, fav_status));

                                                }
                                                setImageList(list);
                                              //  viewdata();
                                            }

                                            View mView = LayoutInflater.from(ImageActivity.this).inflate(R.layout.resycle, null);
                                            RecyclerView recyclerView2= (RecyclerView)mView.findViewById(R.id.my_recycler_view12);
                                            img_image=(ImageView)mView.findViewById(R.id.img_image);
                                            img_music=(ImageView)mView.findViewById(R.id.img_music);
                                            img_video=(ImageView)mView.findViewById(R.id.img_video);
                                            img_quote=(ImageView)mView.findViewById(R.id.img_quote);

                                            img_image.setVisibility(View.VISIBLE);
                                            img_music.setVisibility(View.GONE);
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
                                                    Intent intent = new Intent(ImageActivity.this, FullListActivity.class);
                                                    intent.putExtra("category", "image");
                                                    intent.putExtra("index","" + viewslist.get(finalI1));
                                                    intent.putExtra("title",""+categoryList.get(finalI1));
                                                    startActivity(intent);
                                                    //  Log.e("Category id", "" + viewslist.get(finalI1).toString());
                                                    Log.e("Category id", "" + viewslist.get(finalI1));
                                                }
                                            });


                                            adapter=new Recycler_Adapter_Images(ImageActivity.this,list);
                                            recyclerView2.setAdapter(adapter);

                                            recyclerView2.setHasFixedSize(true);
                                            LinearLayoutManager llm = new LinearLayoutManager(ImageActivity.this);
                                            llm.setOrientation(LinearLayoutManager.HORIZONTAL);
                                            recyclerView2.setLayoutManager(llm);



                                            lm.addView(mView);






//                                            LinearLayout ll = new LinearLayout(ImageActivity.this);
//                                            ll.setOrientation(LinearLayout.VERTICAL);
//
//                                            LinearLayout llh = new LinearLayout(ImageActivity.this);
//                                            llh.setOrientation(LinearLayout.HORIZONTAL);
//                                            llh.setBackgroundColor(getResources().getColor(R.color.dark));
//                                            llh.setGravity(Gravity.CENTER_VERTICAL);
//                                            //llh.setGravity();
//                                            imageh=new ImageView(ImageActivity.this);
//                                            imageh.setImageResource(R.drawable.galery_small);
//                                            imageh.setPadding(20, 12, 10, 12);
//                                            imageh.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
//
//                                            title_catgry = new TextView(ImageActivity.this);
//                                            title_catgry.setTextColor(getResources().getColor(R.color.white));
//                                            //title_catgry.setBackgroundColor(getResources().getColor(R.color.dark));
//                                            title_catgry.setPadding(20, 12, 10, 12);
//                                            //title_catgry.setGravity(Gravity.CENTER_VERTICAL);
//
//                                            title_catgry.setTextSize(16);
//                                            //title_catgry.setLayoutParams(params);
//                                            title_catgry.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
//                                            // ll.addView(title_catgry);
//
//                                            llh.addView(imageh);
//                                            llh.addView(title_catgry);
//                                            lm.addView(llh);
//                                            //lm.addView(horizontalScrollView);
//                                            lm.addView(viewdata());
//                                            title_catgry.setText(name);
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
                        Toast.makeText(ImageActivity.this, "Timeout Error",
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
            VolleySingleton.getInstance(ImageActivity.this.getApplicationContext()).addToRequestQueue(sr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    public LinearLayout viewdata()
//    {
//        LinearLayout viewpagerlayout = new LinearLayout(ImageActivity.this);
//        params = new LinearLayout.LayoutParams(
//                MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
//        params.setMargins(10, 10, 10, 10);
//
//       // viewpagerlayout.setGravity(Gravity.CENTER);
//
//        viewPager  = new ViewPager(ImageActivity.this);
//
//        viewPager.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, 300));
//        int pagerPadding = 16;
//        viewPager.setClipToPadding(false);
//        viewPager.setPadding(pagerPadding, 16, 0, 0);
//        //viewPager.setBackgroundColor(Color.WHITE);
//        viewpagerlayout.addView(viewPager);
//        if (viewPager.getAdapter() == null) {
//            horizontalListAdapter = new GalleryAdapter(ImageActivity.this, list);
//            viewPager.setAdapter(horizontalListAdapter);
//
//        } else {
//            // listView.loadMoreCompleat();
//            horizontalListAdapter.notifyDataSetChanged();
//
//        }
//      //  viewPager.setPageMargin(-320);
//
//        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                if(position==pos)
//                {
//                    loadMoreData();
//                }
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
        return list;

    }

    public void setImageList(List<ImageModel> list) {
        this.list = list;

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        if (prefshelper.getLoginWithFromPreference().equals("0")) {
            Intent intent = new Intent(ImageActivity.this, GuestHomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(ImageActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

    }
}

