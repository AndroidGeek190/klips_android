package com.erginus.klips.Fragments;

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
import com.erginus.klips.GuestHomeActivity;
import com.erginus.klips.HomeActivity;
import com.erginus.klips.R;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ContactUsFragment extends Fragment {
    TextView btn_submit;
    Prefshelper prefshelper;
    String fname,user_email, user_phn,comments;
    EditText edt_name, edt_phone, edt_email, edt_comments;
    ProgressDialog pDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.contact_us, container, false);
        prefshelper=new Prefshelper(getActivity());
        if (prefshelper.getLoginWithFromPreference().equals("0")) {
            GuestHomeActivity.txt_title.setText("Contact Us");

        }
        else{
            HomeActivity.txt_title.setText("Contact Us");

        }
        edt_name=(EditText)rootview.findViewById(R.id.edt_userFname);
        edt_phone=(EditText)rootview.findViewById(R.id.edt_contact);
        edt_email=(EditText)rootview.findViewById(R.id.edt_email);
        edt_comments=(EditText)rootview.findViewById(R.id.edt_comments);
        btn_submit=(TextView)rootview.findViewById(R.id.button_sbmt);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View focusView = null;
                boolean cancelLogin = false;
                fname = edt_name.getText().toString();
                comments = edt_comments.getText().toString();
                user_email = edt_email.getText().toString();
                user_phn = edt_phone.getText().toString();
                if (TextUtils.isEmpty(fname)) {
                    edt_name.setError(getString(R.string.userName_required));
                    focusView = edt_name;
                    cancelLogin = true;
                }
                if (TextUtils.isEmpty(comments)) {
                    edt_comments.setError("Field must not be empty");
                    focusView = edt_comments;
                    cancelLogin = true;
                }


                if (TextUtils.isEmpty(user_email)) {
                    edt_email.setError(getString(R.string.email_required));
                    focusView = edt_email;
                    cancelLogin = true;
                } else if (!isValidEmail(user_email)) {
                    edt_email.setError(getString(R.string.invalid_email));
                    focusView = edt_email;
                    cancelLogin = true;
                }

                if (cancelLogin) {
                    // error in login
                    focusView.requestFocus();
                } else {
                    contactUs();
                }
            }
        });
        return  rootview;
    }
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    @Override
    public void onResume() {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                    if (getFragmentManager().getBackStackEntryCount() > 0) {
                        if (prefshelper.getLoginWithFromPreference().equals("0")) {
                            GuestHomeActivity.txt_title.setText("Home");
                            getFragmentManager().popBackStack();
                        } else {
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

    public void contactUs()
    {
        try {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "Edit Profile" + MapAppConstant.API + "contact");
            StringRequest sr = new StringRequest(Request.Method.POST,MapAppConstant.API+"contact", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if ((pDialog != null) && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
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

                                }
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }

                                final Dialog dialog = new Dialog(getActivity(), R.style.cust_dialog);
                                dialog.setTitle(serverMessage);
                                dialog.setContentView(R.layout.dialog_thanku);
                                Button btn_ok=(Button)dialog.findViewById(R.id.btn_ok);
                                btn_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        if(prefshelper.getLoginWithFromPreference().equalsIgnoreCase("0"))
                                        {
                                            Intent intent=new Intent(getActivity(), GuestHomeActivity.class);
                                            startActivity(intent);

                                            getActivity().finish();
                                        }
                                        else{
                                        Intent intent=new Intent(getActivity(), HomeActivity.class);
                                        startActivity(intent);

                                        getActivity().finish();
                                    }}
                                });


                                dialog.show();


                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if ((pDialog != null) && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
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

                    params.put("name", fname);
                    params.put("email", user_email);
                    params.put("phone",user_phn);
                    params.put("comments",comments);

                    return params;
                }
            };
            sr.setRetryPolicy(new DefaultRetryPolicy(100000*2,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            sr.setShouldCache(true);
            VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(sr);


        } catch (Exception e) {
            e.printStackTrace();
        }



    }
    }

