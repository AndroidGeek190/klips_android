package com.erginus.klips;

import android.app.ProgressDialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import android.os.Environment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.erginus.klips.Commons.CircularImageView;
import com.erginus.klips.Commons.MapAppConstant;
import com.erginus.klips.Commons.Prefshelper;
import com.erginus.klips.Commons.VolleySingleton;
import com.erginus.klips.Model.ImageModel;
import com.erginus.klips.Model.QuoteModel;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuoteDetailActivity extends AppCompatActivity {
    String image="";
    TextView txt_title;
    LinearLayout ll_back;
    Prefshelper prefshelper;
    static ImageView img_fav;
    File file;
    static int image_id;
    static int quotes_id;
    static String full_imagepath;
    ImageView img_share,full_image;
    static String status="";
    static ImageView img_large;
    int id;
   // String status;
    ImageView nextpic,backpic;
    private int currentindex = 0;
    ImageView shareimage,image_fav,imageView_home1;
    List<QuoteModel> list;
    static int fav_status_id=1;
    ViewPager viewPager;
    GalleryAdapter2 gallery2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        prefshelper=new Prefshelper(this);
        list=new ArrayList<>();
        list= (List<QuoteModel>) getIntent().getSerializableExtra("list1");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ll_back=(LinearLayout)findViewById(R.id.ll_navi);
        txt_title=(TextView)findViewById(R.id.toolbar_title);
        txt_title.setText("Quotes");

        image_id= Integer.parseInt(getIntent().getStringExtra("id"));
        status=getIntent().getStringExtra("status");

        img_large=(ImageView)findViewById(R.id.imageView_large);
        img_fav = (ImageView) findViewById(R.id.imageView_fav);
        full_image=(ImageView)findViewById(R.id.imageView_expand);
        full_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuoteDetailActivity.this, Full_Image.class);
                intent.putExtra("pic", "" + full_imagepath);
                intent.putExtra("image_quote", "quote");
                startActivity(intent);
            }
        });
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        gallery2  = new GalleryAdapter2(QuoteDetailActivity.this,list);
        viewPager.setAdapter(gallery2);

        imageView_home1=(ImageView)findViewById(R.id.imageView_home);
        imageView_home1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prefshelper.getLoginWithFromPreference().equals("0"))
                {
                    Intent intent=new Intent(QuoteDetailActivity.this, GuestHomeActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent=new Intent(QuoteDetailActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            }
        });
        full_imagepath = getIntent().getStringExtra("image");
        Picasso.with(this).load(full_imagepath).into(img_large);

        img_share=(ImageView)findViewById(R.id.imageView_share);

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
//                Intent intent=new Intent(QuoteDetailActivity.this,QuoteActivity.class);
//                startActivity(intent);
            }
        });
            fav();
    }

    public void fav()
    {
        if(prefshelper.getLoginWithFromPreference().equals("0"))
        {
            // img_fav.setVisibility(View.GONE);
            img_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(QuoteDetailActivity.this, "First Login Your Account", Toast.LENGTH_SHORT).show();
                }
            });
            img_fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(QuoteDetailActivity.this, "First Login Your Account", Toast.LENGTH_SHORT).show();
                }
            });

        }
        else {
            if (status.equals("0")) {
                img_fav.setImageResource(R.drawable.my_favort);
            } else {
                img_fav.setImageResource(R.drawable.fav_red);
            }


            img_fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (status.equals("0")) {
//                        if (fav_status_id == 1) {
//                            prefshelper.storeFavStatusIdToPreference(fav_status_id + "");
//                            fav_status_id = 0;
                        img_fav.setImageResource(R.drawable.fav_red);
                        status="1";
                        addTofavourites();


                        // }
//                    else {
//                            fav_status_id = 1;
//                            img_fav.setImageResource(R.drawable.my_favort);
//                            addTofavourites();
//
//                        }
                    } else {
//                        if (fav_status_id == 0) {
//                            prefshelper.storeFavStatusIdToPreference(fav_status_id + "");
//                            fav_status_id = 1;
//                            img_fav.setImageResource(R.drawable.my_favort);
//                            addTofavourites();
//
//
//                        }
                        //else {
                        // fav_status_id = 0;
                        img_fav.setImageResource(R.drawable.my_favort);
                        status="0";
                        addTofavourites();

                        //}
                    }


                }


            });
            img_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BitmapDrawable bm = (BitmapDrawable) img_large.getDrawable();
                    Bitmap mysharebmp = bm.getBitmap();


                    try {
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        mysharebmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                        //you can create a new file name "test.jpeg"
                        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "test.png");

                        FileOutputStream fo = new FileOutputStream(file);
                        fo.write(bytes.toByteArray());

                        // remember close de FileOutput
                        fo.close();
                        Log.d("done", "done");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String screenshotUri = String.valueOf(Uri.fromFile(file));
                    Log.d("imageeeeeeeeeee", screenshotUri);
                    String message = "If you want to see more beautiful Quotes. Install this app.\n"+full_imagepath;
                     Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("image/*");
                    share.putExtra(Intent.EXTRA_TEXT, message);
                    share.putExtra(Intent.EXTRA_STREAM, Uri.parse(screenshotUri));
                    share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    startActivity(Intent.createChooser(share, "Share Via"));
                }
            });
        }
    }
//    public void Showimage(int index)
//    {
//        //image= list.get(index).getName();
//        id= Integer.parseInt(list.get(index).getId());
//        status =  list.get(index).getFavStatus();
//
//
//        Picasso.with(QuoteDetailActivity.this).load(list.get(index).getImage()).into(image);
//    }
    public void addTofavourites() {
        try {
            final ProgressDialog pDialog = new ProgressDialog(QuoteDetailActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "SIGNUP " + MapAppConstant.API + "favourite_quote_operations");
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "favourite_quote_operations", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.d("", ".......response====" + response.toString());

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                        Toast.makeText(QuoteDetailActivity.this, "" + serverMessage, Toast.LENGTH_SHORT).show();
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
                    pDialog.dismiss();
                    //  VolleyLog.d("", "Error: " + error.getMessage());
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(QuoteDetailActivity.this, "Timeout Error",
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
                    params.put("quote_id",""+image_id);
                    params.put("quote_favourite_status", status);


                    Log.e("user_id", prefshelper.getUserIdFromPreference());
                    Log.e("user_security_hash", prefshelper.getUserSecHashFromPreference());
                    Log.e("quote_id", "" + image_id);
                    Log.e("quote_favourite_status", status);

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
    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file =  new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
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
    public void onBackPressed() {
        onResume();
        finish();
        super.onBackPressed();
    }
}
class GalleryAdapter2 extends PagerAdapter
{
    Prefshelper prefshelper;


    //ImageView img_fav, img_share;
    String image="";
    File file;
    // Declare Variables
    private List<QuoteModel> list;
    private final Context context;

    public GalleryAdapter2(Context context, List<QuoteModel> list) {
        this.context = context;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        prefshelper = new Prefshelper(context);

        LayoutInflater inflater = null;
        ImageView imageView;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView =inflater.inflate(R.layout.list_gallery_item,container,false);

        imageView=(ImageView)itemView.findViewById(R.id.image_product);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.with(context).load(list.get(position).getImage()).into(QuoteDetailActivity.img_large);
                QuoteDetailActivity.full_imagepath=list.get(position).getImage();
                QuoteDetailActivity.image_id= Integer.parseInt(list.get(position).getId());
                addTofavourites1();
            }
        });


        Picasso.with(context).load(list.get(position).getImage()).into(imageView);


        container.addView(itemView);
        return itemView;
    }
    @Override
    public float getPageWidth(int position) {
        return(0.3f);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //((ViewPager) container).removeView((ImageView) object);
        container.removeView((RelativeLayout) object);
    }
    public void addTofavourites1()
    {
        try {
            final ProgressDialog pDialog = new ProgressDialog(context);
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "SIGNUP " + MapAppConstant.API + "favourite_image_operations");
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "get_quote_by_id", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.d("", ".......response====" + response.toString());

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        Log.e("", ".......response====" + object.toString());
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                        //Toast.makeText(context, "" + serverMessage, Toast.LENGTH_SHORT).show();
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {

                                    JSONObject ob=object.getJSONObject("data");
                                    QuoteDetailActivity.status=ob.getString("favourite_status");
                                    Log.e("status on Click",""+QuoteDetailActivity.status);
                                    if(QuoteDetailActivity.status.equalsIgnoreCase("1")) {
                                        // if(prefshelper.getFavStatusIdFromPreference().equals("1"))
                                        //{
                                        // fav_status_id = 0;
                                        QuoteDetailActivity.img_fav.setImageResource(R.drawable.fav_red);
                                        // prefshelper.storeFavStatusIdToPreference("" + 0);
                                        //}
                                    }
                                    else
                                    {
                                        //    fav_status_id=1;
                                        QuoteDetailActivity.img_fav.setImageResource(R.drawable.my_favort);
                                        // prefshelper.storeFavStatusIdToPreference("" + 1);
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
                        Toast.makeText(context, "Timeout Error",
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
                    params.put("quote_id",""+QuoteDetailActivity.image_id);

                    return params;
                }
            };
            sr.setShouldCache(true);

            sr.setRetryPolicy(new DefaultRetryPolicy(50000 * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(context).addToRequestQueue(sr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}