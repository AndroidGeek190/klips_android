package com.erginus.klips;

import android.app.ActionBar;
import android.app.ProgressDialog;

import android.content.Intent;
import android.os.Bundle;

import android.support.v4.view.ViewPager;
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

import com.erginus.klips.Adapter.QuotesAdapter;


import com.erginus.klips.Adapter.Recycle_Adapter_Quotes;
import com.erginus.klips.Adapter.Recycler_Adapter_Images;
import com.erginus.klips.Commons.MapAppConstant;
import com.erginus.klips.Commons.Prefshelper;
import com.erginus.klips.Commons.VolleySingleton;

import com.erginus.klips.Model.QuoteModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class QuoteActivity extends AppCompatActivity {
  
    Prefshelper prefshelper;
    List<QuoteModel> list;
    LinearLayout ll_back;
    TextView title_catgry;
    ImageView imageh;
    LinearLayout lm;
    LinearLayout.LayoutParams params;
    int page=0;
    QuotesAdapter horizontalListAdapter;
    int pos=7;
    ViewPager viewPager;
    RelativeLayout layout;
    ImageView img_home,back;

    List viewslist, categoryList;
    Recycle_Adapter_Quotes adapter;
    TextView bt;
    int count=0;
    ImageView img_music,img_video,img_quote, img_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);
        viewslist=new ArrayList();
        lm = (LinearLayout) findViewById(R.id.ll_main);
        img_home=(ImageView)findViewById(R.id.imageView_home);
        params = new LinearLayout.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 10, 10, 10);
        categoryList=new ArrayList<>();
        prefshelper=new Prefshelper(QuoteActivity.this);
        back=(ImageView)findViewById(R.id.imageView_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prefshelper.getLoginWithFromPreference().equals("0"))
                {
                    finish();
                    Intent intent=new Intent(QuoteActivity.this, GuestHomeActivity.class);
                    startActivity(intent);
                }
                else
                {
                    finish();
                    Intent intent=new Intent(QuoteActivity.this, HomeActivity.class);
                    startActivity(intent);
                }

            }
        });

        //ll_back=(LinearLayout)findViewById(R.id.ll_navi);
        img_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent=new Intent(QuoteActivity.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });



        getQuoteImages(page);
       
    }

  public void getQuoteImages(final int page) {
      try {
          final ProgressDialog pDialog = new ProgressDialog(QuoteActivity.this);
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
                      //  Toast.makeText(QuoteActivity.this, "" + serverMessage, Toast.LENGTH_SHORT).show();
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
                                          list = new ArrayList<>();
                                          getList().clear();
                                          JSONObject object1 = jsonArray.getJSONObject(i);

                                          id = object1.getString("quote_category_id");
                                          name = object1.getString("quote_category_name");

                                          viewslist.add(count);
                                          categoryList.add(name);
                                          count++;
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

                                                  list.add(quotemodel(quote_id, qute_name, quote_file, quote, fav_status));
                                              }
                                              setList(list);
//                                              horizontalScrollView=new HorizontalScrollView(QuoteActivity.this);
//                                              horizontalScrollView.isFillViewport();
//                                              ll2 = new LinearLayout(QuoteActivity.this);
//                                              ll2.setOrientation(LinearLayout.HORIZONTAL);
//
//                                              listView=new EndlessListView(QuoteActivity.this);
//
//                                              ll2.addView(listView);
//                                              horizontalScrollView.addView(ll2);
//                                              listView.isHorizontalScrollBarEnabled();
//                                              if(listView.getAdapter()==null){
//                                                  horizontalListAdapter=new GalleryAdapter(QuoteActivity.this,list);
//                                                  listView.setAdapter(horizontalListAdapter);
//
//                                              }
//                                              else{
//                                                  listView.loadMoreCompleat();
//                                                  horizontalListAdapter.notifyDataSetChanged();
//
//                                              }
//                                              listView.setOnLoadMoreListener(new EndlessListView.OnLoadMoreListener() {
//                                                  @Override
//                                                  public boolean onLoadMore() {
//                                                      if (mHaveMoreDataToLoad) {
//                                                          loadMoreData();
//                                                      } else {
//                                                          Toast.makeText(QuoteActivity.this, "No more data to load",
//                                                                  Toast.LENGTH_SHORT).show();
//                                                      }
//
//                                                      return mHaveMoreDataToLoad;
//
//                                                  }
//                                              });
                                            //viewdata();
                                          }

                                          View mView = LayoutInflater.from(QuoteActivity.this).inflate(R.layout.resycle, null);
                                          RecyclerView recyclerView2= (RecyclerView)mView.findViewById(R.id.my_recycler_view12);
                                          img_image=(ImageView)mView.findViewById(R.id.img_image);
                                          img_music=(ImageView)mView.findViewById(R.id.img_music);
                                          img_video=(ImageView)mView.findViewById(R.id.img_video);
                                          img_quote=(ImageView)mView.findViewById(R.id.img_quote);

                                          img_image.setVisibility(View.GONE);
                                          img_music.setVisibility(View.GONE);
                                          img_quote.setVisibility(View.VISIBLE);
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
                                                  Intent intent = new Intent(QuoteActivity.this, FullListActivity.class);
                                                  intent.putExtra("category", "quote");
                                                  intent.putExtra("index","" + viewslist.get(finalI1));
                                                  intent.putExtra("title",""+categoryList.get(finalI1));
                                                  startActivity(intent);
                                              }
                                          });


                                          adapter=new Recycle_Adapter_Quotes(QuoteActivity.this,list);
                                          recyclerView2.setAdapter(adapter);

                                          recyclerView2.setHasFixedSize(true);
                                          LinearLayoutManager llm = new LinearLayoutManager(QuoteActivity.this);
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
                      Toast.makeText(QuoteActivity.this, "Timeout Error",
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
          VolleySingleton.getInstance(QuoteActivity.this.getApplicationContext()).addToRequestQueue(sr);

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
    public List<QuoteModel> getList() {
        return list;
    }

    public void setList(List<QuoteModel> list) {
        this.list = list;
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        if (prefshelper.getLoginWithFromPreference().equals("0")) {
            finish();
            Intent intent = new Intent(QuoteActivity.this, GuestHomeActivity.class);
            startActivity(intent);
        } else {
            finish();
            Intent intent = new Intent(QuoteActivity.this, HomeActivity.class);
            startActivity(intent);
        }

    }
}
