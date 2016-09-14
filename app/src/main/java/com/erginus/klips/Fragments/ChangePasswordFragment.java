package com.erginus.klips.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.erginus.klips.HomeActivity;

import com.erginus.klips.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ChangePasswordFragment extends Fragment {
    String id = "", usr_login = "", usr_email = "", usr_contc = "", hash = "", gender = "", desc = "", rate = "", grup_slug = "";
    TextView btn_chng_pwd;
    Prefshelper prefsHelper;
    String new_pswrd, cnfrm_pwd;
    EditText edt_new_pwd, edt_confirm_pwd;
    View view;

    public ChangePasswordFragment() {
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
        View rootview = inflater.inflate(R.layout.fragment_change_password, container, false);
       HomeActivity.txt_title.setText("Change Password");
        edt_new_pwd = (EditText) rootview.findViewById(R.id.edt_new_password);
        edt_confirm_pwd = (EditText) rootview.findViewById(R.id.edt_confirm_password);
        btn_chng_pwd = (TextView) rootview.findViewById(R.id.button_sbmt);
        prefsHelper = new Prefshelper(getActivity());

        btn_chng_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_pswrd = edt_new_pwd.getText().toString();
                cnfrm_pwd = edt_confirm_pwd.getText().toString();
                boolean cancelLogin = false;
                View focusView = null;

                if (!TextUtils.isEmpty(new_pswrd) && !isValidPassword(new_pswrd)) {
                    edt_new_pwd.setError(getString(R.string.invalid_password));
                    focusView = edt_new_pwd;
                    cancelLogin = true;
                } else if (TextUtils.isEmpty(new_pswrd)) {
                    edt_new_pwd.setError(getString(R.string.pswrd_required));
                    focusView = edt_new_pwd;
                    cancelLogin = true;
                } else if (TextUtils.isEmpty(cnfrm_pwd)) {
                    edt_confirm_pwd.setError(getString(R.string.invalid_password));
                    focusView = edt_confirm_pwd;
                    cancelLogin = true;
                } else if (!cnfrm_pwd.equals(new_pswrd)) {
                    edt_confirm_pwd.setError("Password do not match");
                    focusView = edt_confirm_pwd;
                    cancelLogin = true;
                }
                if (cancelLogin) {
                    // error in login
                    focusView.requestFocus();
                } else {
                    changePassword();
                }
            }
        });
        return rootview;
    }

    private boolean isValidPassword(String pass) {
        return pass != null && pass.length() >= 6;
    }

    public void changePassword() {
        try {
            final ProgressDialog pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "Change password" + MapAppConstant.API + "change_password");
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "change_password", new Response.Listener<String>() {
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
                                    JSONObject object1 = object.getJSONObject("data");
                                    String image = "", fname, lname, trmsAccpt;
                                   hash = object1.getString("user_security_hash");

                                    prefsHelper.storeSecHashToPreference(hash);

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                            startActivity(intent);
                            getActivity().overridePendingTransition(0, 0);
                            getActivity().finish();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
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
                    params.put("user_id", prefsHelper.getUserIdFromPreference());
                    params.put("user_security_hash", prefsHelper.getUserSecHashFromPreference());
                    params.put("user_login_password", edt_new_pwd.getText().toString());
                    params.put("confirm_login_password", edt_confirm_pwd.getText().toString());

                    return params;
                }
            };
            sr.setRetryPolicy(new DefaultRetryPolicy(100000 * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            sr.setShouldCache(true);
            VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(sr);


        } catch (Exception e) {
            e.printStackTrace();
        }


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

                        getFragmentManager().popBackStack();
                        HomeActivity.txt_title.setText("Home");
                    }

                    return true;

                }

                return false;
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();


    }
}
