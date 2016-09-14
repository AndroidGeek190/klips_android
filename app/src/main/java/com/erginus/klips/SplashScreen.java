package com.erginus.klips;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
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
import com.erginus.klips.Commons.ConnectionDetector;
import com.erginus.klips.Commons.MapAppConstant;
import com.erginus.klips.Commons.Prefshelper;
import com.erginus.klips.Commons.VolleySingleton;
import com.facebook.FacebookSdk;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 6000;
    public static MediaPlayer splashSound;
    Prefshelper prefshelper;
    ConnectionDetector cd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        setContentView(R.layout.activity_splash_screen);
        cd = new ConnectionDetector(getApplicationContext());
        if (!cd.isConnectingToInternet()) {
            AlertDialog alertDialog=new AlertDialog.Builder(SplashScreen.this, R.style.cust_dialog).create();
            alertDialog.setTitle("Alert!");
            alertDialog.setMessage("Internet Connection Error, Please connect to working Internet connection");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog,int which)
                {
                    finish();
                    Intent intent=new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            // Showing Alert Message
            alertDialog.show();

        }


        prefshelper=new Prefshelper(this);

        splashSound = MediaPlayer.create(SplashScreen.this, R.raw.start_music);
        /*splashSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                                        @Override
                                                        public void onCompletion(MediaPlayer splashSound) {

                                                            splashSound.stop();
                                                            splashSound.release();
                                                            if(cd.isConnectingToInternet()) {
                                    if((prefshelper.getUserIdFromPreference().equals("") || prefshelper.getUserSecHashFromPreference().equals("")))
                                                            {
                                                                Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                                                                startActivity(intent);
                                                                overridePendingTransition(0, 0);
                                                                finish();
                                                            }
                                                            else
                                                            {
                                                               sessionLogin();

                                                            }
                                                        }}
                                                    });*/
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                if(cd.isConnectingToInternet()) {
                if((prefshelper.getUserIdFromPreference().equals("") && prefshelper.getUserSecHashFromPreference().equals("")))
                {

                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                } else {
                    sessionLogin();
                }
                }
            }

        }, SPLASH_TIME_OUT);

        splashSound.start();
    }
    @Override
    protected void onPause() {

        super.onPause();
       // splashSound.release();

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
       // splashSound.release();
        finish();
    }
    public void sessionLogin() {
        try {
            final ProgressDialog pDialog = new ProgressDialog(SplashScreen.this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "Login " + MapAppConstant.API + "session_login");
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "session_login", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.d("", ".......response====" + response.toString());

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                       // Toast.makeText(SplashScreen.this, "" + serverMessage, Toast.LENGTH_SHORT).show();
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {
                                    JSONObject jsonObject=object.getJSONObject("data");
                                    String user_first_name=jsonObject.getString("user_first_name");
                                    String user_last_name=jsonObject.getString("user_last_name");
                                    String email=jsonObject.getString("user_email");
                                   String contact=jsonObject.getString("user_primary_contact");
                                    String image_profile=jsonObject.getString("user_profile_image_url");
                                    prefshelper.storeUserFirstNameToPreference(user_first_name);
                                    prefshelper.storeUserlastNameToPreference(user_last_name);
                                    prefshelper.storeEmailToPreference(email);
                                    prefshelper.storePrimaryContactToPreference(contact);
                                    prefshelper.storeImageToPreference(image_profile);
                                    prefshelper.storeIstTimeHomeToPreference(false);

                                }



                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if(prefshelper.getLoginWithFromPreference().equals("1")||prefshelper.getLoginWithFromPreference().equals("2")) {
                                Intent intent = new Intent(SplashScreen.this, HomeActivity.class);
                                startActivity(intent);
                                SplashScreen.this.overridePendingTransition(0, 0);
                                finish();
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
                        Toast.makeText(SplashScreen.this, "Timeout Error",
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

                    return params;
                }
            };
            sr.setShouldCache(true);

            sr.setRetryPolicy(new DefaultRetryPolicy(50000 * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(SplashScreen.this.getApplicationContext()).addToRequestQueue(sr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override

    public void onBackPressed()
    {
        super.onBackPressed();
        splashSound.release();
        finish();

    }
}