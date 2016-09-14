package com.erginus.klips.Fragments;


import android.app.ActionBar;

import android.app.ProgressDialog;

import android.content.Intent;

import android.os.Bundle;

import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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
import com.erginus.klips.ForgotPasswordActivity;
import com.erginus.klips.GuestHomeActivity;
import com.erginus.klips.HomeActivity;
import com.erginus.klips.R;
import com.erginus.klips.RegistrationActivity;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;

import com.facebook.GraphRequest;

import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import java.util.Map;


public class LoginFragment extends android.support.v4.app.Fragment {
    EditText edt_email, edt_passwrd;
   TextView btn_register, btn_submit;
    TextView txt_skip, txt_forgot;
    String email, password, image_profile, user_id, fname, lname;
    Prefshelper prefshelper;
    LoginButton loginButton;
    CallbackManager callbackManager;
    ProfileTracker profileTracker;
    TextView textView;
    Profile profile;
    AccessTokenTracker accessTokenTracker;
    String user_name;
    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview= inflater.inflate(R.layout.activity_login, container, false);
        //img_facebook=(ImageView)rootview.findViewById(R.id.imageView_fb);
        edt_email=(EditText)rootview.findViewById(R.id.editText_email);

        edt_passwrd=(EditText)rootview.findViewById(R.id.editText_paswrd);
        btn_register=(TextView)rootview.findViewById(R.id.button_register);
        btn_submit=(TextView)rootview.findViewById(R.id.button_submit);
        txt_forgot=(TextView)rootview.findViewById(R.id.textView_forgot);
        txt_skip=(TextView)rootview.findViewById(R.id.textView_skip);
        prefshelper=new Prefshelper(getActivity());
        ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);

        }

        loginButton = (LoginButton) rootview.findViewById(R.id.login_button);
        loginButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        loginButton.setReadPermissions("public_profile", "email", "user_about_me");
        // If using in a fragment
        loginButton.setFragment(this);
        // Other app specific specialization

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                profileTracker = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(
                            Profile oldProfile,
                            Profile currentProfile) {
                                              // App code

                   /*     if (currentProfile == null) return;

                        if (currentProfile != null) {
                            user_id = currentProfile.getId();
                            prefshelper.storeUserFirstNameToPreference(currentProfile.getFirstName());
                            prefshelper.storeUserlastNameToPreference(currentProfile.getLastName());

                            Log.e(currentProfile.getFirstName(), " " + currentProfile.getLastName());
                        }*/
                    }};

                  GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {// Application code

                                Log.v("LoginActivity", response.toString());
                                if (response.getError() != null) {
                                    // handle error
                                } else {

                                    try {
                                        email = object.optString("email");
                                        user_id = object.getString("id");
                                        user_name=object.getString("name");
                                        int firstSpace = user_name.indexOf(" ");
                                        fname = user_name.substring(0, firstSpace);
                                        lname = user_name.substring(firstSpace).trim();


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    prefshelper.storeEmailToPreference(email);
                                    prefshelper.storeUserFirstNameToPreference(fname);
                                    prefshelper.storeUserlastNameToPreference(lname);
                                    loginWithSocialMedia();
                                    Log.d("email facebook", email+" "+fname+" "+lname);

                            }

                            }

                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "email, name, id");
                request.setParameters(parameters);
                request.executeAsync();
                prefshelper.storeLoginWithToPreference("1");

                profileTracker.startTracking();
            }

            @Override
            public void onCancel() {
                // App code
                Log.d("facebook - onCancel", "cancelled");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.d("facebook - onError", exception.getMessage());
            }
        });
         accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken accessToken, AccessToken accessToken2) {
                System.out.println("acesstoken trackercalled");
            }
        };

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                      /*  profile = Profile.getCurrentProfile();
                        if (profile != null) {
                            user_id = profile.getId();
                            Log.d("user name", profile.getFirstName() + " " + profile.getLastName());
                            prefshelper.storeUserFirstNameToPreference(profile.getFirstName());
                            prefshelper.storeUserlastNameToPreference(profile.getLastName());

                        }*/

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        // Application code
                                        Log.v("LoginActivity", response.toString());
                                        if (response.getError() != null) {
                                            // handle error
                                        } else {

                                            try {
                                                email = object.optString("email");
                                                user_id=object.getString("id");
                                                user_name=object.getString("name");
                                                int firstSpace = user_name.indexOf(" ");
                                                fname = user_name.substring(0, firstSpace);
                                                lname = user_name.substring(firstSpace).trim();

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            prefshelper.storeEmailToPreference(email);
                                            prefshelper.storeUserFirstNameToPreference(fname);
                                            prefshelper.storeUserlastNameToPreference(lname);
                                            loginWithSocialMedia();
                                            Log.d("email facebook", email);
                                        }

                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "email, name, id");
                        request.setParameters(parameters);
                        request.executeAsync();
                        prefshelper.storeLoginWithToPreference("1");

                        accessTokenTracker.startTracking();
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException e) {

                    }

                });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getActivity(), RegistrationActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(0, 0);
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View focusView = null;
                boolean cancelLogin = false;

                email=edt_email.getText().toString();
                password=edt_passwrd.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    edt_email.setError("Email or Username is required");
                    focusView = edt_email;
                    cancelLogin = true;
                }
                if (TextUtils.isEmpty(password)) {
                    edt_passwrd.setError("Password is required");
                    focusView = edt_passwrd;
                    cancelLogin = true;
                }
                if (cancelLogin) {
                    // error in login
                    focusView.requestFocus();
                } else {
                    loginWithEmail();
                }


            }
        });
        txt_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefshelper.storeLoginWithToPreference("0");
                prefshelper.storeIstTimeHomeToPreference(true);
                Intent intent=new Intent(getActivity(), GuestHomeActivity.class);
                startActivity(intent);
                getActivity(). overridePendingTransition(0, 0);
            }
        });
        txt_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), ForgotPasswordActivity.class);
                startActivity(intent);
                getActivity(). overridePendingTransition(0, 0);
            }
        });
           return  rootview;
    }

    public void loginWithEmail() {
        try {
            final ProgressDialog pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "Login " + MapAppConstant.API + "login");
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "login", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.d("", ".......response====" + response.toString());

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                        Toast.makeText(getActivity(), "" + serverMessage, Toast.LENGTH_SHORT).show();
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {
                                    JSONObject jsonObject=object.getJSONObject("data");
                                    String user_first_name=jsonObject.getString("user_first_name");
                                    String user_last_name=jsonObject.getString("user_last_name");
                                    email=jsonObject.getString("user_email");
                                    String userId=jsonObject.getString("user_id");
                                    String secHash=jsonObject.getString("user_security_hash");
                                    String contact=jsonObject.getString("user_primary_contact");
                                    image_profile=jsonObject.getString("user_profile_image_url");
                                    prefshelper.storeUserFirstNameToPreference(user_first_name);
                                    prefshelper.storeUserlastNameToPreference(user_last_name);
                                    prefshelper.storeEmailToPreference(email);
                                    prefshelper.storeUserIdToPreference(userId);
                                    prefshelper.storeSecHashToPreference(secHash);
                                    prefshelper.storePrimaryContactToPreference(contact);
                                    prefshelper.storeImageToPreference(image_profile);
                                    prefshelper.storeLoginWithToPreference("2");
                                    prefshelper.storeIstTimeHomeToPreference(true);
                                }



                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Intent intent=new Intent(getActivity(), HomeActivity.class);
                            startActivity(intent);
                            getActivity(). overridePendingTransition(0,0);

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

                    params.put("user_login_email", email);
                    params.put("user_login_password", password);

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
    public void loginWithSocialMedia() {
        try {
            final ProgressDialog pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "Login " + MapAppConstant.API + "social_media_login");
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "social_media_login", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.d("", ".......response====" + response.toString());

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                       Toast.makeText(getActivity(), "" + serverMessage, Toast.LENGTH_SHORT).show();
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {
                                    JSONObject jsonObject=object.getJSONObject("data");
                                    String user_first_name=jsonObject.getString("user_first_name");
                                    String user_last_name=jsonObject.getString("user_last_name");
                                    email=jsonObject.getString("user_email");
                                    String userId=jsonObject.getString("user_id");
                                    String secHash=jsonObject.getString("user_security_hash");

                                    image_profile=jsonObject.getString("user_profile_image_url");
                                    prefshelper.storeUserFirstNameToPreference(user_first_name);
                                    prefshelper.storeUserlastNameToPreference(user_last_name);
                                    prefshelper.storeEmailToPreference(email);
                                    prefshelper.storeUserIdToPreference(userId);
                                    prefshelper.storeSecHashToPreference(secHash);

                                    prefshelper.storeImageToPreference(image_profile);
                                    prefshelper.storeLoginWithToPreference("1");
                                    prefshelper.storeIstTimeHomeToPreference(true);
                                }



                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                            startActivity(intent);
                            getActivity().overridePendingTransition(0, 0);

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
                    Log.d("email, user_id", email+" "+prefshelper.getUserFNameFromPreference());
                    params.put("social_media_plateform", "facebook");
                    params.put("social_media_id", user_id);
                    params.put("user_email", email);
                    params.put("user_first_name", prefshelper.getUserFNameFromPreference());
                    params.put("user_last_name", prefshelper.getUserLNameFromPreference());
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
    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();

    }
    @Override
    public void onStop() {
        super.onStop();

    }
    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

}
