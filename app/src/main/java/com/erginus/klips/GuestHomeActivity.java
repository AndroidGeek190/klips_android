package com.erginus.klips;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import android.widget.Button;
import android.widget.FrameLayout;
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
import com.erginus.klips.Commons.MapAppConstant;
import com.erginus.klips.Commons.Prefshelper;

import com.erginus.klips.Commons.VolleySingleton;
import com.erginus.klips.Fragments.AboutUS;
import com.erginus.klips.Fragments.ArtistsFragment;
import com.erginus.klips.Fragments.ContactUsFragment;
import com.erginus.klips.Fragments.TrendingSongsFragment;
import com.erginus.klips.Model.VideoModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GuestHomeActivity extends AppCompatActivity {
    public  static TextView text_new, txt_title;
    ImageView img_slide, img_video, img_audio, img_quotes, img_gallery, img_play,img_trending_txt,img_search_txt,img_favourites_txt,img_playlist_txt;
    LinearLayout ll_slide;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private CharSequence mDrawerTitle;
    FrameLayout f;
    ActionBarDrawerToggle mDrawerToggle;
    Toolbar toolbar;
    List<VideoModel> list;
    Button btn_english, btn_arabic;
    Prefshelper prefshelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_home);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        txt_title=(TextView)toolbar.findViewById(R.id.toolbar_title);
        txt_title.setText("Home");

        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);

        }
        prefshelper=new Prefshelper(GuestHomeActivity.this);

        img_trending_txt=(ImageView)findViewById(R.id.downloads);
        img_search_txt=(ImageView)findViewById(R.id.search);

        text_new=(TextView)findViewById(R.id.textView);
       img_slide=(ImageView)findViewById(R.id.image_product);
        ll_slide=(LinearLayout)findViewById(R.id.dragView);
        img_video=(ImageView)findViewById(R.id.video);
        img_audio=(ImageView)findViewById(R.id.audio);
        img_quotes=(ImageView)findViewById(R.id.quotes);
        img_gallery=(ImageView)findViewById(R.id.gallry);
        img_play=(ImageView)findViewById(R.id.play_random);
        img_favourites_txt=(ImageView)findViewById(R.id.favourites);
        img_favourites_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                            Toast.makeText(GuestHomeActivity.this, "First Login Your Account", Toast.LENGTH_SHORT).show();

            }
        });
        img_playlist_txt=(ImageView)findViewById(R.id.playlist);
        img_playlist_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GuestHomeActivity.this, "First Login Your Account", Toast.LENGTH_SHORT).show();
            }
        });
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView=(NavigationView)findViewById(R.id.navigation);
        list=new ArrayList<>();
        f=(FrameLayout)findViewById(R.id.content_frame);
        btn_arabic=(Button)findViewById(R.id.textView_arabic);
        btn_english=(Button)findViewById(R.id.textView_english);
        ll_slide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogLanguage();
                ll_slide.setVisibility(View.GONE);
            }
        });
        if(prefshelper.getIstTimefromPrefrences())
        {
            dialog();


            prefshelper.storeIstTimeHomeToPreference(false);
        }
        LinearLayout linearLayout=(LinearLayout) LayoutInflater.from(this).inflate(R.layout.drawer_guest_header, null);
        navigationView.addHeaderView(linearLayout);

        if (drawerLayout != null) {
            drawerLayout.setDrawerShadow(R.drawable.list_back, GravityCompat.START);

            mDrawerToggle = new ActionBarDrawerToggle(GuestHomeActivity.this, drawerLayout,
                    toolbar, R.string.drawer_open, R.string.drawer_close) {
                public void onDrawerClosed(View view) {
                    super.onDrawerClosed(view);
                    invalidateOptionsMenu();
                }

                public void onDrawerOpened(View drawerView) {
                    getSupportActionBar().setTitle(mDrawerTitle);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    super.onDrawerOpened(drawerView);

                    invalidateOptionsMenu();

                }
            };
            drawerLayout.setDrawerListener(mDrawerToggle);

        }
        img_trending_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_title.setText("Trending");
                android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, new TrendingSongsFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        img_search_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(GuestHomeActivity.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        img_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                prefshelper.storeWheelItemToPreference("1");
                Intent intent=new Intent(GuestHomeActivity.this, VideoActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        img_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                prefshelper.storeWheelItemToPreference("1");
                Intent intent=new Intent(GuestHomeActivity.this, MusicActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        img_quotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                prefshelper.storeWheelItemToPreference("1");
                Intent intent=new Intent(GuestHomeActivity.this, QuoteActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        img_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                prefshelper.storeWheelItemToPreference("1");
                Intent intent=new Intent(GuestHomeActivity.this, ImageActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        img_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVideos();

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {


                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.drawer_home:
                        txt_title.setText("Home");
                        Intent intent=new Intent(GuestHomeActivity.this, GuestHomeActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    // For rest of the options we just show a toast on click

                    case R.id.drawer_artist:
                        txt_title.setText("Artists");
                        android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, new ArtistsFragment());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        return true;
                    case R.id.contactus:
                        txt_title.setText("Contact US");
                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, new ContactUsFragment());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        return true;
                    case R.id.drawer_share:
                        Toast.makeText(GuestHomeActivity.this, "First Login Your Account for Share", Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.about_us:
                        txt_title.setText("About Us");
                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, new AboutUS());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        return true;
                    case R.id.drawer_logout:
                        prefshelper.getPreferences().edit().clear().commit();

                      /*  Intent intentlog = new Intent(GuestHomeActivity.this, LoginActivity.class);
                        intentlog.putExtra("finish", true);
                        intentlog.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
                        startActivity(intentlog);
                        finish();*/
                        ExitActivity.exitApplication(GuestHomeActivity.this);
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });


    }
    public void dialog()
    {
        final Dialog dialog = new Dialog(GuestHomeActivity.this, R.style.cust_dialog);
        dialog.setTitle("Select Content Language");
        dialog.setContentView(R.layout.dialog_ist_time_popup);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        Button  button=(Button)dialog.findViewById(R.id.textView_camera);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ll_slide.setVisibility(View.GONE);

                dialogLanguage();
            }
        });

        dialog.show();
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void dialogLanguage()
    {
        final Dialog dialog = new Dialog(GuestHomeActivity.this, R.style.DialogSlideAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_language_select);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        ImageView img_arrow=(ImageView)dialog.findViewById(R.id.image_arrow);
        final Button btn_arabic=(Button)dialog.findViewById(R.id.textView_arabic);
        final Button btn_english=(Button)dialog.findViewById(R.id.textView_english);

        if(prefshelper.getUserLanguageFromPreference().equals("1")) {
            //btn_english.setBackground(getResources().getDrawable(R.drawable.round_corner));
            btn_english.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_corner));
        }
        else if(prefshelper.getUserLanguageFromPreference().equals("2"))
        {
            btn_arabic.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_corner));
        }
            btn_arabic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    prefshelper.storeUserLanguageToPreference("2");
                    dialog.dismiss();
                    ll_slide.setVisibility(View.VISIBLE);
                    btn_arabic.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_corner));

                   // getTrendingSongs();
                }
            });
            btn_english.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    prefshelper.storeUserLanguageToPreference("1");
                    dialog.dismiss();
                    ll_slide.setVisibility(View.VISIBLE);
                    btn_english.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_corner));
                    //getTrendingSongs();
                }
            });


            img_arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    ll_slide.setVisibility(View.VISIBLE);
                }
            });
        dialog.show();


    }

    @Override

    protected void onPostCreate(Bundle savedInstanceState) {

        super.onPostCreate(savedInstanceState);

        // Sync the toggle state after onRestoreInstanceState has occurred.

        mDrawerToggle.syncState();

    }


    @Override

    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);

        // Pass any configuration change to the drawer toggles

        mDrawerToggle.onConfigurationChanged(newConfig);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_home, menu);
        menu.findItem(R.id.action_home).setVisible(false);
        // Retrieve the SearchView and plug it into SearchManager
       /* final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        //searchView.setQueryHint(getString(R.string.search));
        if (null != searchView) {
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }
*/



        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Intent intent=new Intent(GuestHomeActivity.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                // app icon in action bar clicked; goto parent activity.

              /*  android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, new SearchFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();*/
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

 public void getVideos() {
     try {
         final ProgressDialog pDialog = new ProgressDialog(GuestHomeActivity.this);
         pDialog.setMessage("Loading...");
         pDialog.show();

         Log.e("", "countries " + MapAppConstant.API + "random_musics");
         StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "random_musics", new Response.Listener<String>() {
             @Override
             public void onResponse(String response) {
                 pDialog.dismiss();
                 Log.d("", ".......response====" + response.toString());

                 ////////
                 try {
                     JSONObject object = new JSONObject(response);
                     String serverCode = object.getString("code");
                     String serverMessage = object.getString("message");
                     //  Toast.makeText(HomeActivity.this, "" + serverMessage, Toast.LENGTH_SHORT).show();
                     if (serverCode.equalsIgnoreCase("0")) {

                     }
                     if (serverCode.equalsIgnoreCase("1")) {
                         try {
                             getVideoList().clear();
                             if ("1".equals(serverCode)) {

                                 JSONArray jsonArray = object.getJSONArray("data");
                                 String id = "", name = "", v_id = "", video_name = "", artist_name = "", image = "", video_file = "";
                                 String nameArtist = "", fav_status,playlist_status;
                                 if (jsonArray.length() > 0) {
                                     for (int i = 0; i < jsonArray.length(); i++) {
                                         JSONObject object1 = jsonArray.getJSONObject(i);
                                         v_id = object1.getString("music_id");
                                         video_name = object1.getString("music_name");
                                         video_file = object1.getString("music_file_url");
                                         image = object1.getString("music_thumbnail_url");
                                         JSONArray jsonArray2 = object1.getJSONArray("artist_details");
                                         if (jsonArray2.length() > 0) {
                                             for (int k = 0; k < jsonArray2.length(); k++) {
                                                 JSONObject object3 = jsonArray2.getJSONObject(k);
                                                 artist_name = object3.getString("artist_name");

                                                 nameArtist = nameArtist + " " + artist_name + ", ";
                                             }

                                         }
                                         fav_status=object1.getString("favourite_status");
                                         playlist_status=object1.getString("playlist_status");

                                         list.add(videoModel(v_id, video_name, nameArtist, image, video_file, fav_status, playlist_status));
                                     }
                                     setVideoList(list);

                                 }
                                 Log.e("listtttttttttt",list.toString());
                                 Intent intent = new Intent(GuestHomeActivity.this, VideoDetailActivity.class);
                                 intent.putExtra("list", (Serializable) list);
                                 intent.putExtra("id", "music");
                                 intent.putExtra("status","");
                                 intent.putExtra("play_status","" );
                                 startActivity(intent);
                                 overridePendingTransition(0, 0);



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
                     Toast.makeText(GuestHomeActivity.this, "Timeout Error",
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
                 params.put("user_id", "");
                 params.put("user_security_hash", "");

                 return params;
             }
         };
         sr.setShouldCache(true);

         sr.setRetryPolicy(new DefaultRetryPolicy(50000 * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                 DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
         VolleySingleton.getInstance(GuestHomeActivity.this.getApplicationContext()).addToRequestQueue(sr);

     } catch (Exception e) {
         e.printStackTrace();
     }
 }


    private VideoModel videoModel(String id,String name,String artist ,String image, String video_file, String fav, String play)
    {
        VideoModel video = new VideoModel();
        video.setId(id);
        video.setName(name);
        video.setArtistName(artist);
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
    public  void onResume()
    {
        super.onResume();
    }
    @Override
    public  void onPause()
    {
        super.onPause();
    }
    @Override
    public  void onBackPressed()
    {
        new AlertDialog.Builder(this, R.style.cust_dialog)
                .setTitle("Alert !")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);

                    }
                }).create().show();
    }
    @Override
    public  void onStop()
    {
        super.onStop();

    }
    @Override
    public  void onDestroy()
    {
        super.onDestroy();
          finish();


    }

}
