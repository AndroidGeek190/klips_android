package com.erginus.klips;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.erginus.klips.Fragments.ArtistsFragment;
import com.erginus.klips.Fragments.ChangePasswordFragment;
import com.erginus.klips.Fragments.ContactUsFragment;
import com.erginus.klips.Fragments.EditProfileFragment;

import com.erginus.klips.Fragments.AboutUS;
import com.erginus.klips.Fragments.Like_us_on_Facebook;
import com.erginus.klips.Fragments.MyFavouritesFragment;
import com.erginus.klips.Fragments.PlayListFragment;
import com.erginus.klips.Fragments.TrendingSongsFragment;
import com.erginus.klips.Model.VideoModel;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;

import com.facebook.login.LoginResult;
import com.facebook.share.widget.LikeView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeActivity extends AppCompatActivity {

    public static TextView text_new, txt_title;
    public static ImageView img_slide, img_video, img_audio, img_quotes, img_gallery, img_edit, profile_image, img_play,
            img_search_txt, img_trending_txt, img_favourites_txt, img_playlist_txt;
    LinearLayout ll_slide;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private CharSequence mDrawerTitle;
    FrameLayout f;
    ActionBarDrawerToggle mDrawerToggle;
    Prefshelper prefshelper;
    Toolbar toolbar;
    public  static TextView txt_username, txt_email;
    public  static List<VideoModel> list;
    LinearLayout btnLoginToLike;
    LikeView likeView;
    CallbackManager callbackManager;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        prefshelper=new Prefshelper(HomeActivity.this);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        txt_title=(TextView)toolbar.findViewById(R.id.toolbar_title);
        txt_title.setText("Home");
        list = new ArrayList<>();
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);

        }
        text_new=(TextView)findViewById(R.id.textView);
        img_slide=(ImageView)findViewById(R.id.image_product);
        ll_slide=(LinearLayout)findViewById(R.id.dragView);
        img_video=(ImageView)findViewById(R.id.video);
        img_audio=(ImageView)findViewById(R.id.audio);
        img_quotes=(ImageView)findViewById(R.id.quotes);
        img_gallery=(ImageView)findViewById(R.id.gallry);
        img_play=(ImageView)findViewById(R.id.play_random);
        img_favourites_txt=(ImageView)findViewById(R.id.favourites);
        img_trending_txt=(ImageView)findViewById(R.id.downloads);
        img_search_txt=(ImageView)findViewById(R.id.search);
        img_playlist_txt=(ImageView)findViewById(R.id.playlist);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView=(NavigationView)findViewById(R.id.navigation);
        linearLayout=(LinearLayout) LayoutInflater.from(this).inflate(R.layout.drawer_header, null);
        navigationView.addHeaderView(linearLayout);
        txt_username=(TextView)linearLayout.findViewById(R.id.txt_usrName);
        txt_email=(TextView)linearLayout.findViewById(R.id.txt_userEmail);
        img_edit=(ImageView)linearLayout.findViewById(R.id.imageView_edit);
        profile_image=(ImageView)linearLayout.findViewById(R.id.profile_img);
        f=(FrameLayout)findViewById(R.id.content_frame);
        Menu drawerMenu = navigationView.getMenu();
        initInstances();
        initCallbackManager();
        refreshButtonsState();
     /*   LikeView likeView = (LikeView) linearLayout.findViewById(R.id.likeView);
        likeView.setLikeViewStyle(LikeView.Style.BUTTON);
        likeView.setAuxiliaryViewPosition(LikeView.AuxiliaryViewPosition.INLINE);
        likeView.setObjectIdAndType("https://www.facebook.com/sarcasmLOL/", LikeView.ObjectType.OPEN_GRAPH);*/
        txt_username.setText((prefshelper.getUserFNameFromPreference()) + " " + (prefshelper.getUserLNameFromPreference()));
        txt_email.setText(prefshelper.getUserEmailFromPreference());
        Picasso.with(HomeActivity.this).load(prefshelper.getImageFromPreference()).into(profile_image);

       img_favourites_txt.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            txt_title.setText("My Favorites");
            android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, new MyFavouritesFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    });
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
        img_playlist_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_title.setText("My Playlists");
                android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, new PlayListFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        img_search_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        if(prefshelper.getLoginWithFromPreference().equals("1"))
        {
                 drawerMenu.findItem(R.id.drawer_changePwd).setVisible(false);
        }

        if(prefshelper.getLoginWithFromPreference().equals("2"))
        {
              drawerMenu.findItem(R.id.drawer_changePwd).setVisible(true);
        }

        if(prefshelper.getIstTimefromPrefrences())
        {
            dialog();
            prefshelper.storeIstTimeHomeToPreference(false);
        }
       Log.d(prefshelper.getUserIdFromPreference(), prefshelper.getUserSecHashFromPreference());

ll_slide.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        dialogLanguage();
        ll_slide.setVisibility(View.INVISIBLE);
    }
});

        if (drawerLayout != null) {
            drawerLayout.setDrawerShadow(R.drawable.list_back, GravityCompat.START);

            mDrawerToggle = new ActionBarDrawerToggle(HomeActivity.this, drawerLayout,
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

        img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
                txt_title.setText("Edit Profile");
                android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id. content_frame,new EditProfileFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        img_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                prefshelper.storeWheelItemToPreference("1");
             //   finish();
                Intent intent=new Intent(HomeActivity.this, VideoActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        img_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                prefshelper.storeWheelItemToPreference("1");
              //  finish();
                Intent intent=new Intent(HomeActivity.this, MusicActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        img_quotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                prefshelper.storeWheelItemToPreference("1");
               // finish();
                Intent intent=new Intent(HomeActivity.this, QuoteActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        img_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                prefshelper.storeWheelItemToPreference("1");
              //  finish();
                Intent intent=new Intent(HomeActivity.this, ImageActivity.class);
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
                        Intent intent=new Intent(HomeActivity.this, HomeActivity.class);
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

                    case R.id.drawer_myplaylist:
                        txt_title.setText("My Playlists");
                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, new PlayListFragment());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        return true;
                    case R.id.drawer_myfav:
                        txt_title.setText("My Favorites");
                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, new MyFavouritesFragment());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        return true;
                    case R.id.contactus:
                        txt_title.setText("Contact Us");
                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, new ContactUsFragment());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        return true;
                    case R.id.about_us:
                        txt_title.setText("About Us");
                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, new AboutUS());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        return true;
                    case R.id.drawer_changePwd:

                        txt_title.setText("Change Password");
                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, new ChangePasswordFragment());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        return true;

                    case R.id.drawer_share:
                            shareapp();
                        return true;
                    case R.id.drawer_faq:

                        ExitActivity.exitApplication(HomeActivity.this);

                        if(prefshelper.getLoginWithFromPreference().equals("3"))
                        {
                            LoginManager.getInstance().logOut();
                           /* SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
                            SharedPreferences.Editor editor = sharedPrefs.edit();
                            editor.clear();
                            editor.commit();
                            Intent intentlog = new Intent(HomeActivity.this, LoginActivity.class);
                            intentlog.putExtra("finish", true);
                            intentlog.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
                            startActivity(intentlog);
                            finish();*/

                        }
                        else if(prefshelper.getLoginWithFromPreference().equals("1")){
                            LoginManager.getInstance().logOut();
                            prefshelper.getPreferences().edit().clear().commit();
                          /*  Intent intentlog = new Intent(HomeActivity.this, LoginActivity.class);
                            intentlog.putExtra("finish", true);
                            intentlog.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
                            startActivity(intentlog);
                            finish();*/
                        }
                        else
                        {
                            if(isLoggedIn())
                            {
                                LoginManager.getInstance().logOut();

                            }

                            prefshelper.getPreferences().edit().clear().commit();

                          /*  Intent intentlog = new Intent(HomeActivity.this, LoginActivity.class);
                            intentlog.putExtra("finish", true);
                            intentlog.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
                            startActivity(intentlog);
                            finish();*/
                        }

                        return true;
                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });
        SplashScreen.splashSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer splashSound) {

                splashSound.stop();
                splashSound.release();


            } });

    }

    public void shareapp(){
        String message = "https://erginus.net";
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, message);
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(Intent.createChooser(share, "Share Via"));
    }
    public void dialog()
    {
        final Dialog dialog = new Dialog(HomeActivity.this, R.style.cust_dialog);
        dialog.setTitle("Select Content Language");

        dialog.setContentView(R.layout.dialog_ist_time_popup);
      //  dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        Button  button=(Button)dialog.findViewById(R.id.textView_camera);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogLanguage();
                ll_slide.setVisibility(View.GONE);

                dialog.dismiss();
            }
        });

        dialog.show();
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void dialogLanguage()
    {
        final Dialog dialog = new Dialog(HomeActivity.this, R.style.DialogSlideAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_language_select);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        ImageView img_arrow=(ImageView)dialog.findViewById(R.id.image_arrow);
        final Button btn_arabic=(Button)dialog.findViewById(R.id.textView_arabic);
        final Button btn_english=(Button)dialog.findViewById(R.id.textView_english);
if(prefshelper.getUserLanguageFromPreference().equals("1"))
{
    //btn_english.setBackground(getResources().getDrawable(R.drawable.round_corner));
    btn_english.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_corner));

}
     else if(prefshelper.getUserLanguageFromPreference().equals("2"))
        {
            //btn_arabic.setBackground(getResources().getDrawable(R.drawable.round_corner));
            btn_arabic.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_corner));

        }


    btn_arabic.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            prefshelper.storeUserLanguageToPreference("2");
            dialog.dismiss();
            ll_slide.setVisibility(View.VISIBLE);

            //btn_arabic.setBackground(getResources().getDrawable(R.drawable.round_corner));
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
            //btn_english.setBackground(getResources().getDrawable(R.drawable.round_corner));
            btn_english.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_corner));
            //  getTrendingSongs();
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

        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // app icon in action bar clicked; goto parent activity.

                Intent intent=new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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


    public void getVideos() {
        try {
            final ProgressDialog pDialog = new ProgressDialog(HomeActivity.this);
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
                                    String nameArtist = "", fav_status = null, playlist_status = null;
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
                                    Log.e("listtttttttttt", list.toString());
                                    Intent intent = new Intent(HomeActivity.this, VideoDetailActivity.class);
                                    intent.putExtra("list", (Serializable) list);
                                    intent.putExtra("id", "music");
                                    intent.putExtra("status",fav_status );
                                    intent.putExtra("play_status",playlist_status);

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
                        Toast.makeText(HomeActivity.this, "Timeout Error",
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
                    params.put("user_id", prefshelper.getUserIdFromPreference());
                    params.put("user_security_hash", prefshelper.getUserSecHashFromPreference());

                    return params;
                }
            };
            sr.setShouldCache(true);

            sr.setRetryPolicy(new DefaultRetryPolicy(50000 * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(HomeActivity.this.getApplicationContext()).addToRequestQueue(sr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void initInstances() {
        btnLoginToLike = (LinearLayout) linearLayout.findViewById(R.id.btnLoginToLike);
        likeView = (LikeView)linearLayout.findViewById(R.id.likeView);
        likeView.setLikeViewStyle(LikeView.Style.BUTTON);
        likeView.setAuxiliaryViewPosition(LikeView.AuxiliaryViewPosition.INLINE);
        final ImageView img_liked=(ImageView)linearLayout.findViewById(R.id.liked);
        likeView.setObjectIdAndType("https://www.facebook.com/sarcasmLOL/", LikeView.ObjectType.OPEN_GRAPH);
        btnLoginToLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(HomeActivity.this, Arrays.asList("public_profile"));
                Toast.makeText(HomeActivity.this, "You are now loggedIn to facebook. Please click again to Like us on facebook.", Toast.LENGTH_LONG).show();
         /*       Intent facebookAppIntent;
                try {
                    facebookAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/sarcasmLOL/"));
                    startActivity(facebookAppIntent);

                } catch (ActivityNotFoundException e) {
                    facebookAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/sarcasmLOL/"));
                    startActivity(facebookAppIntent);

                }*/

            }
        });
    }

    private void initCallbackManager() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                refreshButtonsState();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {

            }
        });
    }

    private void refreshButtonsState() {
        if (!isLoggedIn()) {
            btnLoginToLike.setVisibility(View.VISIBLE);
            likeView.setVisibility(View.GONE);
        } else {
            btnLoginToLike.setVisibility(View.GONE);
            likeView.setVisibility(View.VISIBLE);
        }
    }
    public boolean isLoggedIn() {
        return AccessToken.getCurrentAccessToken() != null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Handle Facebook Login Result
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
        HomeActivity.list = list;
    }

}
