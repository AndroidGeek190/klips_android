package com.erginus.klips;

import android.app.AlertDialog;
import android.app.ProgressDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.erginus.klips.Adapter.CountryAdapter;
import com.erginus.klips.Commons.MapAppConstant;
import com.erginus.klips.Commons.VolleySingleton;
import com.erginus.klips.Model.CountryModel;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegistrationActivity extends AppCompatActivity {
    TextView btn_submit;
    Spinner spr_country;
    EditText edt_fname, edt_lname, edt_email;
    String fname, lname, email, contact;
    LinearLayout ll_back;

    int  country_id;
    List<CountryModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        btn_submit=(TextView)findViewById(R.id.button_submit);
        spr_country=(Spinner)findViewById(R.id.spinner_country);
      edt_fname=(EditText)findViewById(R.id.editText_fname);
        edt_lname=(EditText)findViewById(R.id.editText_lname);
        edt_email=(EditText)findViewById(R.id.editText_email);
       // edt_contact=(EditText)findViewById(R.id.editText_contact);
        ll_back=(LinearLayout)findViewById(R.id.ll_navi);
        list = new ArrayList<CountryModel>();


            ll_back.setOnClickListener(new View.OnClickListener()

                                       {
                                           @Override
                                           public void onClick(View v) {
                                               finish();

                                           }
                                       }

            );

            btn_submit.setOnClickListener(new View.OnClickListener()

                                          {
                                              @Override
                                              public void onClick(View view) {
                                                  View focusView = null;
                                                  boolean cancelLogin = false;

                                                  fname= edt_fname.getText().toString();
                                                  lname=edt_lname.getText().toString();
                                                  email=edt_email.getText().toString();
                                             //     contact=edt_contact.getText().toString();

                                                  if (TextUtils.isEmpty(fname)) {
                                                      edt_fname.setError("User's First Name is required");
                                                      focusView = edt_fname;
                                                      cancelLogin = true;
                                                  }
                                                  if (TextUtils.isEmpty(lname)) {
                                                      edt_lname.setError("User's Last Name is required");
                                                      focusView = edt_lname;
                                                      cancelLogin = true;
                                                  }
                                                  if (TextUtils.isEmpty(email)) {
                                                      edt_email.setError("Email is required");
                                                      focusView = edt_email;
                                                      cancelLogin = true;
                                                  } else if (!isValidEmail(email)) {
                                                      edt_email.setError("Invalid Email");
                                                      focusView = edt_email;
                                                      cancelLogin = true;
                                                  }
                                                 /* if (!isValidPhone((contact))) {
                                                      edt_contact.setError("Phone number must be of digits 10");
                                                      focusView = edt_contact;
                                                      cancelLogin = true;
                                                  }*/
                                                  if (cancelLogin) {
                                                      // error in login
                                                      focusView.requestFocus();
                                                  } else {
                           Log.d("entered vaues",fname+" "+lname+" "+email+" "+contact);
                                                     signUp();
                                                  }

                                              }
                                          }

            );
        spr_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                country_id = Integer.parseInt(list.get(position).getId());
                Log.d("countryid", ""+country_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getCountry();
        }
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidPhone(String pass) {
        return pass != null && pass.length() == 10;
    }
    @Override
    public void onStart() {
        super.onStart();

    }
    @Override
    public void onResume() {
        super.onResume();


    }
    @Override
    public void onPause() {
        super.onPause();


    }
    public void signUp() {
        try {
            final ProgressDialog pDialog = new ProgressDialog(RegistrationActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "SIGNUP " + MapAppConstant.API + "signup");
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "signup", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.d("", ".......response====" + response.toString());

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");


                        new AlertDialog.Builder(RegistrationActivity.this, R.style.cust_dialog)
                                .setTitle("Alert !")
                                .setMessage(serverMessage)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).create().show();



                       // Toast.makeText(RegistrationActivity.this, "" + serverMessage, Toast.LENGTH_SHORT).show();
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            finish();


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
                        Toast.makeText(RegistrationActivity.this, "Timeout Error",
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

                    params.put("user_first_name", fname);
                    params.put("user_last_name", lname);
                    params.put("user_email", email);
                  // params.put("user_primary_contact", contact);
                    params.put("countries_id", ""+country_id);
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
    public void getCountry() {
        try {
            final ProgressDialog pDialog = new ProgressDialog(RegistrationActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "countries " + MapAppConstant.API + "countries");
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "countries", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.d("", ".......response====" + response.toString());

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                    //    Toast.makeText(RegistrationActivity.this, "" + serverMessage, Toast.LENGTH_SHORT).show();
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {
                                    JSONArray jsonArray = object.getJSONArray("data");
                                    String id = "", country_name = "";
                                    if(jsonArray.length()>0)
                                    {
                                    for(int i=0; i<jsonArray.length(); i++) {
                                        JSONObject object1 = jsonArray.getJSONObject(i);
                                        // id = object1.getString(MapAppConstants.COACH_ID);
                                        id = object1.getString("country_id");
                                        country_name = object1.getString("country_name");
                                        list.add(countryModel(id, country_name));
                                    }}
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            CountryAdapter countryAdapter=new CountryAdapter(RegistrationActivity.this,list);
                            spr_country.setAdapter(countryAdapter);

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
                        Toast.makeText(RegistrationActivity.this, "Timeout Error",
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

                    params.put("current_timestamp", "1");

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
    private CountryModel countryModel(String id,String name)
    {
        CountryModel coach = new CountryModel();
        coach.setId(id);
        coach.setCountry_name(name);

        return coach;
    }

   @Override

   public void onBackPressed()
   {
       super.onBackPressed();


       finish();

   }
    }
