package com.erginus.klips;


import android.app.ProgressDialog;

import android.content.Intent;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;

import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

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
import com.erginus.klips.Commons.ConnectionDetector;
import com.erginus.klips.Commons.MapAppConstant;
import com.erginus.klips.Commons.Prefshelper;
import com.erginus.klips.Commons.Utilities;
import com.erginus.klips.Commons.VolleySingleton;

import com.erginus.klips.Fragments.FavSongsFragment;
import com.erginus.klips.Model.VideoModel;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class VideoDetailActivity extends AppCompatActivity {

    int current;

    LinearLayout ll_back, ll_share, ll_player,ll_time;
    RelativeLayout rl_videoPlayer;
    VideoView videoView;
    SeekBar seekBar, seekBar_land;
    TextView txt_song, txt_artist, txt_time;
    int  length;
    RelativeLayout relativeLayout;
    ImageView img_play,img_pause, img_share, img_fav, img_expand, img_forward,img_forward1,img_thumbnail, img_previous,img_previous1,
            img_add_to_playlist,img_cross, image_ply,image_ply1, image_pre, image_for, img_home;
    View view;
    Thread updateSeekbar;
    TextView txt_start_time, txt_end_time, txt_start, txt_end,txt_desc,title;
    Prefshelper prefshelper;
    ProgressDialog pDialog;
    List<VideoModel> list;
    String video="", category_id, status, pstatus;
    private int currentindex = 0;
    private Utilities utils;
    //private Handler mHandler=new Handler();
    int video_id,status_id=1,fav_status_id=1;
    String getstatus,getpstatus;
LinearLayout lttt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
        status=getIntent().getStringExtra("status");
        category_id=getIntent().getStringExtra("id");
        pstatus=getIntent().getStringExtra("play_status");
        title= (TextView) findViewById(R.id.toolbar_title);
        if(category_id.equalsIgnoreCase("music"))
        {
            title.setText("Music");
        }
        else {
            title.setText("Video");
        }
        txt_song = (TextView) findViewById(R.id.textView_title);
        txt_artist = (TextView) findViewById(R.id.textView_artist);
        txt_time = (TextView) findViewById(R.id.textView_duration);
        img_thumbnail = (ImageView) findViewById(R.id.imageView_albm);
        img_add_to_playlist = (ImageView) findViewById(R.id.imageView_add);


        list=new ArrayList<>();
        videoView=(VideoView)findViewById(R.id.videoView);
        ll_back=(LinearLayout)findViewById(R.id.ll_navi);
        seekBar=(SeekBar)findViewById(R.id.seekBar);
        seekBar_land=(SeekBar)findViewById(R.id.seekBar_land);
        img_pause=(ImageView)findViewById(R.id.imageView_pause);
        img_play=(ImageView)findViewById(R.id.image_play);

        img_share=(ImageView)findViewById(R.id.imageView_share);
        img_fav=(ImageView)findViewById(R.id.imageView_fav);
        img_expand=(ImageView)findViewById(R.id.imageView_expand);
        img_cross=(ImageView)findViewById(R.id.imageView_cross);
        img_previous=(ImageView)findViewById(R.id.imageView_previous);
        img_forward=(ImageView)findViewById(R.id.imageView_forward);
        lttt=(LinearLayout)findViewById(R.id.llt);


        ll_player=(LinearLayout)findViewById(R.id.linearLayout);
        ll_share=(LinearLayout)findViewById(R.id.ll_share);
        ll_time=(LinearLayout)findViewById(R.id.ll_time);
       relativeLayout=(RelativeLayout)findViewById(R.id.rl_bar);
        rl_videoPlayer=(RelativeLayout)findViewById(R.id.rl_video);
        image_ply=(ImageView)findViewById(R.id.img_playh);
        image_pre=(ImageView)findViewById(R.id.img_previoush);
        image_for=(ImageView)findViewById(R.id.img_forwardh);
        img_home=(ImageView)findViewById(R.id.imageView_home);
        txt_start_time=(TextView)findViewById(R.id.start_time);
        txt_end_time=(TextView)findViewById(R.id.end_time);
        txt_start=(TextView)findViewById(R.id.textView_start);
        txt_end=(TextView)findViewById(R.id.textView_end);
        txt_desc=(TextView)findViewById(R.id.textView_desc);

        img_expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                videoView.setLayoutParams(params);

                updateseek();
            }
        });
        img_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prefshelper.getLoginWithFromPreference().equals("0"))
                {

                    Intent intent=new Intent(VideoDetailActivity.this, GuestHomeActivity.class);
                    startActivity(intent);
                }
                else
                {
                   // mHandler.removeCallbacks(mUpdateTimeTask);
                    Intent intent=new Intent(VideoDetailActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
//                mHandler.removeCallbacks(mUpdateTimeTask);
//                Intent intent=new Intent(VideoDetailActivity.this, HomeActivity.class);
//                startActivity(intent);
//                finish();
            }
        });
        prefshelper=new Prefshelper(this);

        if(status.equals("0"))
        {
            img_fav.setImageResource(R.drawable.my_favort);
        }
        else {
            img_fav.setImageResource(R.drawable.fav_red);
        }
        if(pstatus.equals("0"))
        {
            img_add_to_playlist.setImageResource(R.drawable.playlist);
        }
        else{
            img_add_to_playlist.setImageResource(R.drawable.playlist_red);
        }



        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  finish();

            }
        });
        img_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                params.addRule(RelativeLayout.ABOVE, R.id.llt);
                params.addRule(RelativeLayout.BELOW, R.id.ll_navi);
                videoView.setLayoutParams(params);


            }
        });
        utils = new Utilities();
       list= (List<VideoModel>) getIntent().getSerializableExtra("list");
          //Setting MediaController and URI, then starting the videoView

        playVideo(getIntent().getExtras().getInt("currentIndex"));

        if(prefshelper.getLoginWithFromPreference().equals("0"))
        {
            // img_fav.setVisibility(View.GONE);
            img_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(VideoDetailActivity.this, "First Login Your Account", Toast.LENGTH_SHORT).show();
                }
            });
            img_fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(VideoDetailActivity.this, "First Login Your Account", Toast.LENGTH_SHORT).show();
                }
            });
            img_add_to_playlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(VideoDetailActivity.this, "First Login Your Account", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {

            img_add_to_playlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (category_id.equalsIgnoreCase("music")) {
                        if (pstatus.equalsIgnoreCase("0")) {
                            pstatus = "1";
                            img_add_to_playlist.setImageResource(R.drawable.playlist_red);
                            addToMusicPlaylist();

                   } else {
                            pstatus = "0";
                            img_add_to_playlist.setImageResource(R.drawable.playlist);
                            addToMusicPlaylist();
                      }
                    } else if (category_id.equalsIgnoreCase("video")) {
                        if (pstatus.equalsIgnoreCase("0")) {
                                pstatus = "1";
                                img_add_to_playlist.setImageResource(R.drawable.playlist_red);
                                addToPlaylist();

                        } else {
                            pstatus = "0";
                                img_add_to_playlist.setImageResource(R.drawable.playlist);
                                addToPlaylist();

                        }

                    }
                }


            });
        }

//=====================================seekbar change listener========
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

               // mHandler.removeCallbacks(mUpdateTimeTask);
//                int totalDuration = videoView.getDuration();
//                int currentindex = utils.progressToTimer(seekBar.getProgress(), totalDuration);
                seekBar.setMax(videoView.getDuration());
                // forward or backward to certain seconds
                videoView.seekTo(seekBar.getProgress());
                // update timer progress again
                //updateProgressBar();

                updateseek();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
             //   mHandler.removeCallbacks(mUpdateTimeTask);
                updateseek();
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if (fromUser) {
                    videoView.seekTo(progress);
                }

            }
        });

       seekBar_land.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
               // mHandler.removeCallbacks(mUpdateTimeTask);
                seekBar_land.setMax(videoView.getDuration());
                // forward or backward to certain seconds
                videoView.seekTo(seekBar_land.getProgress());
                // update timer progress again
                //updateProgressBar();

                updateseek();
          }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
              //  mHandler.removeCallbacks(mUpdateTimeTask);
                updateseek();
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if (fromUser) {
                    videoView.seekTo(progress);
                }
            }
        });

        img_forward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check if next song is there or not
                Log.e("forward", "forward");

                img_pause.setVisibility(View.GONE);
                if (currentindex < (list.size() - 1)) {
                    playVideo(currentindex + 1);
                    currentindex = currentindex + 1;
                } else {
                    // play first song
                    playVideo(0);
                    currentindex = 0;
                }
            }
        });
        img_previous.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Log.e("backward","backward");
                img_pause.setVisibility(View.GONE);
                if (currentindex > 0) {
                    playVideo(currentindex - 1);
                    currentindex = currentindex - 1;
                } else {
                    // play last song
                    playVideo(list.size() - 1);
                    currentindex = list.size() - 1;
                }
            }
        });
        image_for.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check if next song is there or not
                Log.e("for","forh");
                image_ply.setImageResource(R.drawable.pause);
                img_pause.setVisibility(View.GONE);
                if (currentindex < (list.size() - 1)) {
                    playVideo(currentindex + 1);
                    currentindex = currentindex + 1;
                } else {
                    // play first song
                    playVideo(0);
                    currentindex = 0;
                }
            }
        });
        image_pre.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Log.e("pre","preh");
                image_ply.setImageResource(R.drawable.pause);
                img_pause.setVisibility(View.GONE);
                if (currentindex > 0) {
                    playVideo(currentindex - 1);
                    currentindex = currentindex - 1;
                } else {
                    // play last song
                    playVideo(list.size() - 1);
                    currentindex = list.size() - 1;
                }
            }
        });
        img_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    if (videoView != null) {
                        videoView.pause();
                        // Changing button image to play button
                        img_pause.setVisibility(View.VISIBLE);
                        img_play.setImageResource(R.drawable.play_btn);
                        image_ply.setImageResource(R.drawable.play_btn);
                    }
                } else {
                    // Resume song
                    if (videoView != null) {
                        videoView.start();
                        updateseek();
                        // Changing button image to pause button
                        img_pause.setVisibility(View.GONE);
                        img_play.setImageResource(R.drawable.pause);
                        image_ply.setImageResource(R.drawable.pause);
                    }
                }
            }
        });
        img_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* videoView.start();
                img_play.setVisibility(View.GONE);
                seekBar.setMax(videoView.getDuration());
                seekBar.postDelayed(onEverySecond, 1000);
             */   if (videoView.isPlaying()) {
                    if (videoView != null) {
                        videoView.pause();
                        // Changing button image to play button
                        img_play.setImageResource(R.drawable.play_btn);
                        img_pause.setVisibility(View.VISIBLE);
                    }
                } else {
                    // Resume song
                    if (videoView != null) {
                        videoView.start();
                        updateseek();
                        // Changing button image to pause button
                        img_play.setImageResource(R.drawable.pause);
                        img_pause.setVisibility(View.GONE);
                    }
                }

            }
        });
        image_ply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (videoView.isPlaying()) {
                    if (videoView != null) {
                        videoView.pause();
                        // Changing button image to play button
                        image_ply.setImageResource(R.drawable.play_btn);
                        img_pause.setVisibility(View.VISIBLE);
                    }
                } else {
                    // Resume song
                    if (videoView != null) {
                        videoView.start();
                        // Changing button image to pause button
                        updateseek();
                        image_ply.setImageResource(R.drawable.pause);
                        img_pause.setVisibility(View.GONE);
                    }
                }

            }
        });
        if(prefshelper.getLoginWithFromPreference().equals("0"))
        {
            // img_fav.setVisibility(View.GONE);
            img_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(VideoDetailActivity.this, "First Login Your Account", Toast.LENGTH_SHORT).show();
                }
            });
            img_fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(VideoDetailActivity.this, "First Login Your Account", Toast.LENGTH_SHORT).show();
                }
            });
            img_add_to_playlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(VideoDetailActivity.this, "First Login Your Account", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            img_fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (category_id.equalsIgnoreCase("music")) {
                        if (status.equals("0")) {
                           // if (fav_status_id == 1) {
                                //prefshelper.storeFavStatusIdToPreference(fav_status_id + "");
                                //fav_status_id = 0;
                           status = "1";
                            img_fav.setImageResource(R.drawable.fav_red);
                                addTofavouritesMusic();

                        } else {
//
                            status = "0";
                            img_fav.setImageResource(R.drawable.my_favort);
                            addTofavouritesMusic();
                        }
                    }
                    else if
                            (category_id.equalsIgnoreCase("video")) {
                        if (status.equals("0")) {
                            status = "1";
                                img_fav.setImageResource(R.drawable.fav_red);
                                addTofavourites();
//
                        }
                    else {
//
                       status = "0";
                                img_fav.setImageResource(R.drawable.my_favort);
                                addTofavourites();
                        }

                    }

                }


            });
        }
    }
    //=================================================config
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT) {
            ll_time.setVisibility(View.VISIBLE);
            seekBar.setVisibility(View.VISIBLE);
            ll_share.setVisibility(View.VISIBLE);
            ll_player.setVisibility(View.VISIBLE);
            ll_back.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);
            img_cross.setVisibility(View.GONE);
            txt_desc.setVisibility(View.VISIBLE);
            videoView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (videoView.isPlaying()) {
                        relativeLayout.setVisibility(View.GONE);
                        img_cross.setVisibility(View.GONE);
                    }
                    return false;
                }
            });
            if (videoView.isPlaying()) {
                if (videoView != null) {
                    img_play.setImageResource(R.drawable.pause);
                    img_pause.setVisibility(View.GONE);
                }
            } else {
                // Resume song
                if (videoView != null) {
                    img_play.setImageResource(R.drawable.play_btn);
                    img_pause.setVisibility(View.VISIBLE);
                }
            }


        } else {

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

            ll_time.setVisibility(View.GONE);
            seekBar.setVisibility(View.GONE);
            ll_share.setVisibility(View.GONE);
            ll_player.setVisibility(View.GONE);
            ll_back.setVisibility(View.GONE);
            txt_desc.setVisibility(View.GONE);
            videoView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if((relativeLayout.getVisibility()==View.VISIBLE)||(img_cross.getVisibility()==View.VISIBLE))
                    {
                        relativeLayout.setVisibility(View.GONE);
                        txt_desc.setVisibility(View.GONE);
                        img_cross.setVisibility(View.GONE);
                    }
                    else {
                        relativeLayout.setVisibility(View.VISIBLE);
                        txt_desc.setVisibility(View.GONE);
                        img_cross.setVisibility(View.VISIBLE);
                    }
                    return false;
                }
            });
            if (videoView.isPlaying()) {
                if (videoView != null) {
                    image_ply.setImageResource(R.drawable.pause);
                    img_pause.setVisibility(View.GONE);
                }
            } else {
                // Resume song
                if (videoView != null) {
                    image_ply.setImageResource(R.drawable.play_btn);
                    img_pause.setVisibility(View.VISIBLE);
                }
            }


        }

    }
 //==========================================================video play method
    public  void playVideo(int index)
    {


        // Create a progressbar
        pDialog = new ProgressDialog(VideoDetailActivity.this);
        // Set progressbar title
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        // Show progressbar
        pDialog.show();

        try {
        MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);
            video_id= Integer.parseInt(list.get(index).getId());

            if(category_id.equalsIgnoreCase("video")) {
                video_status();
            }
            else
            {
                Log.e("music Status","status");
                music_status();

            }
             //addToTrending();
            video=list.get(index).getVideo();

            videoView.setVideoURI(Uri.parse(video));
         videoView.requestFocus();
        videoView.start();
        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return true;
            }
        });
            Picasso.with(VideoDetailActivity.this).load(list.get(index).getImage()).into(img_thumbnail);

         //   duration= Integer.parseInt(list.get(index).getDuration());

         //   dur_time=String.format("%02d:%02d", (duration % 3600) / 60, (duration % 60));
         //   txt_time.setText(dur_time);

            txt_song.setText(list.get(index).getName());
            txt_artist.setText(list.get(index).getArtistName());
            txt_desc.setText(list.get(index).getdescription());

            img_play.setImageResource(R.drawable.pause);

            // set Progress bar values
                  } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }


        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.pause();
                img_pause.setVisibility(View.VISIBLE);
                img_play.setImageResource(R.drawable.play_btn);
                image_ply.setImageResource(R.drawable.play_btn);
               /* if (currentindex < (list.size() - 1)) {
                    playVideo(currentindex + 1);
                    currentindex = currentindex + 1;
                } else {
                    // play first song
                    playVideo(0);
                    currentindex = 0;
                }*/

            }
        });


        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                int topContainerId = getResources().getIdentifier("mediacontroller_progress", "id", "android");
                pDialog.dismiss();
                seekBar.setProgress(0);
                seekBar.setMax(videoView.getDuration());
                // seekBar.setMax(100);
                seekBar_land.setProgress(0);
                seekBar_land.setMax(videoView.getDuration());

                mp.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                    @Override
                    public void onBufferingUpdate(MediaPlayer mp, int percent) {

                        if (percent <= 100) {
                            seekBar.setSecondaryProgress(percent);
                            seekBar.setSecondaryProgress(percent / 100);
                            Log.e("percent", "" + percent);
                        }
                        if (percent <= 100) {
                            seekBar_land.setSecondaryProgress(percent);
                            seekBar_land.setSecondaryProgress(percent / 100);
                        }
                    }
                });
                updateseek();
//                updateProgressBar();
            }
        });

        if(prefshelper.getLoginWithFromPreference().equals("0"))
        {
            // img_fav.setVisibility(View.GONE);
            img_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(VideoDetailActivity.this, "First Login Your Account", Toast.LENGTH_SHORT).show();
                }
            });
            img_fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(VideoDetailActivity.this, "First Login Your Account", Toast.LENGTH_SHORT).show();
                }
            });
            img_add_to_playlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(VideoDetailActivity.this, "First Login Your Account", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            img_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String message = "If you want to see more Videos. Install this app.\n";
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.putExtra(Intent.EXTRA_TEXT,message+""+video);
                    share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    Log.d("imageeeeeeeeeee", "" + Uri.parse(video));
                    startActivity(Intent.createChooser(share, "Share Via"));

                }
            });
        }
        if(category_id.equals("music")) {
            addToTrending();
        }

    }

    @Override
    public  void onResume()
    {
        super.onResume();
        videoView.start();
        videoView.seekTo(length);
       // img_play.setVisibility(View.GONE);
    }
    @Override
    public  void onPause() {
        super.onPause();
        videoView.pause();
        length=videoView.getCurrentPosition();
    }

    @Override
    public  void onStop()
    {
        super.onStop();
    }

    @Override
    public void onDestroy()
    {
        videoView.suspend();
        super.onDestroy();
    }

    /**
     * Background Runnable thread
     */
    public void updateseek()
    {
        pDialog.dismiss();
        Log.e("seek bar","updated");
         updateSeekbar = new Thread() {
            @Override
            public void run() {
                int duration = videoView.getDuration();
                current = 0;

                while (current <= duration) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            long totalDuration = videoView.getDuration();

                            long currentDuration = videoView.getCurrentPosition();

                            // Displaying Total Duration time
                            txt_end_time.setText(""+utils.milliSecondsToTimer(totalDuration));
                            txt_end.setText(""+utils.milliSecondsToTimer(totalDuration));

                            txt_start_time.setText(""+utils.milliSecondsToTimer(currentDuration));
                            txt_start.setText(""+utils.milliSecondsToTimer(currentDuration));
                            txt_time.setText("" + utils.milliSecondsToTimer(totalDuration));
                        }
                    });

                    try {
                        sleep(100);

                        current = videoView.getCurrentPosition();
                        seekBar.setProgress(current);
                        seekBar_land.setProgress(current);
                        //Toast.makeText(getApplicationContext(), "Song Position " + current, Toast.LENGTH_LONG).show();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        updateSeekbar.start();
    }


    public void onBackPressed()
    {

        finish();
        super.onBackPressed();
    }
    public void video_status()
    {

        try {
          /*  final ProgressDialog pDialog = new ProgressDialog(VideoDetailActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();
            pDialog.setCancelable(false);*/
            Log.e("", "SIGNUP " + MapAppConstant.API + "video_playlist_operations");
           StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "get_video_by_id", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                   // pDialog.dismiss();
                    Log.d("", ".......response====" + response.toString());

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        Log.e("response on checking",""+object.toString());
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                       // Toast.makeText(VideoDetailActivity.this, "" + serverMessage, Toast.LENGTH_SHORT).show();
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {
                                    JSONObject ob = object.getJSONObject("data");
                                    status = ob.getString("favourite_status");
                                    pstatus=ob.getString("playlist_status");
                                    Log.e("status favourit ", "" + status);
                                    Log.e("status ps status", "" + pstatus);
                                    if (status.equalsIgnoreCase("1")) {
                                        // if(prefshelper.getFavStatusIdFromPreference().equals("1"))
                                        //{
                                        // fav_status_id = 0;
                                        img_fav.setImageResource(R.drawable.fav_red);
                                        // prefshelper.storeFavStatusIdToPreference("" + 0);
                                        //}
                                    } else {
                                        //    fav_status_id=1;
                                        img_fav.setImageResource(R.drawable.my_favort);
                                        // prefshelper.storeFavStatusIdToPreference("" + 1);
                                    }
                                    if(pstatus.equals("0"))
                                    {
                                        img_add_to_playlist.setImageResource(R.drawable.playlist);
                                    }
                                    else{
                                        img_add_to_playlist.setImageResource(R.drawable.playlist_red);
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
                   // pDialog.dismiss();
                    //  VolleyLog.d("", "Error: " + error.getMessage());
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(VideoDetailActivity.this, "Timeout Error",
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
                    params.put("video_id", "" + video_id);

                    return params;
                }
            };
            sr.setShouldCache(true);

            sr.setRetryPolicy(new DefaultRetryPolicy(50000 * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(sr);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void music_status()
    {

        try {
          /*  final ProgressDialog pDialog = new ProgressDialog(VideoDetailActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();
            pDialog.setCancelable(false);*/
            Log.e("", "SIGNUP " + MapAppConstant.API + "video_playlist_operations");
            //StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "add_video_playlist", new Response.Listener<String>() {
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "get_music_by_id", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                  //  pDialog.dismiss();
                    Log.d("", ".......response====" + response.toString());

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        Log.e("music status response",""+object.toString());
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                        //Toast.makeText(VideoDetailActivity.this, "" + serverMessage, Toast.LENGTH_SHORT).show();
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {
                                    JSONObject ob = object.getJSONObject("data");
                                    status = ob.getString("favourite_status");
                                    pstatus=ob.getString("playlist_status");
                                    Log.e("status on Click", "" + status);

                                    if (status.equalsIgnoreCase("1")) {
                                        // if(prefshelper.getFavStatusIdFromPreference().equals("1"))
                                        //{
                                        // fav_status_id = 0;
                                        img_fav.setImageResource(R.drawable.fav_red);
                                        // prefshelper.storeFavStatusIdToPreference("" + 0);
                                        //}
                                    } else {
                                        //    fav_status_id=1;
                                        img_fav.setImageResource(R.drawable.my_favort);
                                        // prefshelper.storeFavStatusIdToPreference("" + 1);
                                    }
                                    if(pstatus.equals("0"))
                                    {
                                        img_add_to_playlist.setImageResource(R.drawable.playlist);
                                    }
                                    else{
                                        img_add_to_playlist.setImageResource(R.drawable.playlist_red);
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
                    //pDialog.dismiss();
                    //  VolleyLog.d("", "Error: " + error.getMessage());
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(VideoDetailActivity.this, "Timeout Error",
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
                    params.put("music_id", "" + video_id);
                    return params;
                }
            };
            sr.setShouldCache(true);

            sr.setRetryPolicy(new DefaultRetryPolicy(50000 * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(sr);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void addToPlaylist() {
        try {
           /* final ProgressDialog pDialog = new ProgressDialog(VideoDetailActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();
            pDialog.setCancelable(false);*/
            Log.e("", "SIGNUP " + MapAppConstant.API + "video_playlist_operations");
            //StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "add_video_playlist", new Response.Listener<String>() {
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "video_playlist_operations", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                  //  pDialog.dismiss();
                    Log.d("", ".......response====" + response.toString());

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                        Toast.makeText(VideoDetailActivity.this, "" + serverMessage, Toast.LENGTH_SHORT).show();
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {

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
                  //  pDialog.dismiss();
                    //  VolleyLog.d("", "Error: " + error.getMessage());
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(VideoDetailActivity.this, "Timeout Error",
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
                    params.put("video_id", "" + video_id);
                   params.put("video_status_id", pstatus);
                     return params;
                }
            };
            sr.setShouldCache(true);

            sr.setRetryPolicy(new DefaultRetryPolicy(50000 * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(sr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addToMusicPlaylist() {
        try {
         /*   final ProgressDialog pDialog = new ProgressDialog(VideoDetailActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();
            pDialog.setCancelable(false);*/
            Log.e("", "SIGNUP " + MapAppConstant.API + "music_playlist_operations");
            //StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "add_video_playlist", new Response.Listener<String>() {
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "music_playlist_operations", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                   // pDialog.dismiss();
                    Log.d("", ".......response====" + response.toString());

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                        Toast.makeText(VideoDetailActivity.this, "" + serverMessage, Toast.LENGTH_SHORT).show();
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {

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
                  //  pDialog.dismiss();
                    //  VolleyLog.d("", "Error: " + error.getMessage());
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(VideoDetailActivity.this, "Timeout Error",
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
                    params.put("music_id", "" + video_id);
                    params.put("music_status_id", pstatus);
                    return params;
                }
            };
            sr.setShouldCache(true);

            sr.setRetryPolicy(new DefaultRetryPolicy(50000 * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(sr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addTofavourites() {
        try {
          /*  final ProgressDialog pDialog = new ProgressDialog(VideoDetailActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();
            pDialog.setCancelable(false);*/
            Log.e("", "SIGNUP " + MapAppConstant.API + "favourite_video_operations");
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "favourite_video_operations", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                   // pDialog.dismiss();
                    Log.d("", ".......response====" + response.toString());

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                        Toast.makeText(VideoDetailActivity.this, "" + serverMessage, Toast.LENGTH_SHORT).show();
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {

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
                  //  pDialog.dismiss();
                    //  VolleyLog.d("", "Error: " + error.getMessage());
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(VideoDetailActivity.this, "Timeout Error",
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
                    params.put("video_id",""+video_id);

                    params.put("video_favourite_status", status);

                    return params;
                }
            };
            sr.setShouldCache(true);

            sr.setRetryPolicy(new DefaultRetryPolicy(50000 * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(sr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addTofavouritesMusic() {
        try {
           /* final ProgressDialog pDialog = new ProgressDialog(VideoDetailActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();
            pDialog.setCancelable(false);*/
            Log.e("", "SIGNUP " + MapAppConstant.API + "favourite_music_operations" );
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "favourite_music_operations", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                  //  pDialog.dismiss();
                    Log.d("", ".......response====" + response.toString());

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                        Toast.makeText(VideoDetailActivity.this, "" + serverMessage, Toast.LENGTH_SHORT).show();
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {
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
                  //  pDialog.dismiss();
                    //  VolleyLog.d("", "Error: " + error.getMessage());
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(VideoDetailActivity.this, "Timeout Error",
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
                    params.put("music_id",""+video_id);
                   params.put("music_favourite_status", status);

                    return params;
                }
            };
            sr.setShouldCache(true);

            sr.setRetryPolicy(new DefaultRetryPolicy(50000 * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(sr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addToTrending() {
        try {
          /*  final ProgressDialog pDialog = new ProgressDialog(VideoDetailActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();
            pDialog.setCancelable(false);*/
            Log.e("", "SIGNUP " + MapAppConstant.API + "music_played_count");
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "music_played_count", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                   // pDialog.dismiss();
                    Log.d("", ".......response====" + response.toString());

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                   //     Toast.makeText(VideoDetailActivity.this, "" + serverMessage, Toast.LENGTH_SHORT).show();
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {

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
                    //pDialog.dismiss();
                    //  VolleyLog.d("", "Error: " + error.getMessage());
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(VideoDetailActivity.this, "Timeout Error",
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
                    params.put("music_id",""+video_id);

                    return params;
                }
            };
            sr.setShouldCache(true);

            sr.setRetryPolicy(new DefaultRetryPolicy(50000 * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(sr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
