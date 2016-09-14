package com.erginus.klips;

import android.app.ProgressDialog;

import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;

import android.graphics.drawable.BitmapDrawable;
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
import com.erginus.klips.Commons.MapAppConstant;
import com.erginus.klips.Commons.Prefshelper;
import com.erginus.klips.Commons.VolleySingleton;

import com.erginus.klips.Model.ImageModel;
import com.erginus.klips.Model.VideoModel;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageDetailActivity extends AppCompatActivity {

    LinearLayout ll_back;
    String image="";
    static ImageView img_large,imageView_home1;
    Prefshelper prefshelper;
    static ImageView img_fav;
            ImageView img_share,full_image;
  static int image_id;
    static int quotes_id;
    static String full_imagepath;

    File file;
   static List<ImageModel> listu;
    TextView txt_title;
    private int currentindex = 0;
    ViewPager viewPager;
    GalleryAdapter1 gallery1;
static String status="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        listu=new ArrayList<>();
        listu= (List<ImageModel>) getIntent().getSerializableExtra("list1");
        Log.e("Array size detail",""+listu.size());
        full_imagepath = getIntent().getStringExtra("image");
        status = getIntent().getStringExtra("status");
        image_id = Integer.parseInt(getIntent().getStringExtra("id"));
        quotes_id = Integer.parseInt(getIntent().getStringExtra("id"));

        ll_back = (LinearLayout) findViewById(R.id.ll_navi);
        img_large = (ImageView) findViewById(R.id.imageView_large);
        img_fav = (ImageView) findViewById(R.id.imageView_fav);
        img_share = (ImageView) findViewById(R.id.imageView_share);
        full_image=(ImageView)findViewById(R.id.imageView_expand);
        full_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(ImageDetailActivity.this,Full_Image.class);
                intent.putExtra("pic",""+full_imagepath);
                intent.putExtra("image_quote", "image");
                startActivity(intent);
            }
        });
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        gallery1  = new GalleryAdapter1(ImageDetailActivity.this,listu);
        viewPager.setAdapter(gallery1);


        imageView_home1 = (ImageView) findViewById(R.id.imageView_home);


        imageView_home1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefshelper.getLoginWithFromPreference().equals("0")) {
                    Intent intent = new Intent(ImageDetailActivity.this, GuestHomeActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ImageDetailActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            }
        });

        Picasso.with(ImageDetailActivity.this).load(full_imagepath).into(img_large);
        prefshelper = new Prefshelper(this);


        txt_title = (TextView) findViewById(R.id.toolbar_title);
        txt_title.setText("Images");
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
//                Intent intent=new Intent(ImageDetailActivity.this,ImageActivity.class);
//                startActivity(intent);
            }
        });
       fav();
//        addToTrending();

    }
    public void fav()
    {
        if(prefshelper.getLoginWithFromPreference().equals("0"))
        {
            // img_fav.setVisibility(View.GONE);
            img_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ImageDetailActivity.this, "First Login Your Account", Toast.LENGTH_SHORT).show();
                }
            });
            img_fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ImageDetailActivity.this, "First Login Your Account", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            if (status.equals("0")) {
                img_fav.setImageResource(R.drawable.my_favort);
            } else {
                img_fav.setImageResource(R.drawable.fav_red);
            }


            img_fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (status.equals("0")) {
                        img_fav.setImageResource(R.drawable.fav_red);
                            status="1";
                            addTofavourites();
                } else {
                      img_fav.setImageResource(R.drawable.my_favort);
                             status="0";
                            addTofavourites();

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
                    String message = "If you want to see more beautiful Images. Install this app.\n"+full_imagepath;
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


    @Override
    public void onBackPressed() {
        onResume();
        super.onBackPressed();
    }

    public void addTofavourites() {
        try {
            final ProgressDialog pDialog = new ProgressDialog(ImageDetailActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "SIGNUP " + MapAppConstant.API + "favourite_image_operations");
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "favourite_image_operations", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.d("", ".......response====" + response.toString());

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                        Toast.makeText(ImageDetailActivity.this, "" + serverMessage, Toast.LENGTH_SHORT).show();
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {
//                                    JSONObject ob=new JSONObject();
//                                    status=ob.getString("favourite_status");
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
//                            if(status.equals("0"))
//                            {
//                                if(prefshelper.getFavStatusIdFromPreference().equals("1"))
//                                {
//                                    fav_status_id=0;
//                                    img_fav.setImageResource(R.drawable.fav);
//                                    prefshelper.storeFavStatusIdToPreference("" + 0);
//                                }
//                                else
//                                {
//                                    fav_status_id=1;
//                                    img_fav.setImageResource(R.drawable.my_favort);
//                                    prefshelper.storeFavStatusIdToPreference("" + 1);
//                                }
//
//                            }
//                            else {
//                                if(prefshelper.getFavStatusIdFromPreference().equals("0"))
//                                {
//                                    fav_status_id=1;
//                                    img_fav.setImageResource(R.drawable.my_favort);
//                                    prefshelper.storeFavStatusIdToPreference("" + 1);
//                                }
//                                else
//                                {
//                                    fav_status_id=0;
//                                    img_fav.setImageResource(R.drawable.fav);
//                                    prefshelper.storeFavStatusIdToPreference("" + 0);
//                                }
//
//                            }

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
                        Toast.makeText(ImageDetailActivity.this, "Timeout Error",
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
                    params.put("image_id",""+image_id);
                   params.put("image_favourite_status", status);
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

}
class GalleryAdapter1 extends PagerAdapter
{
    Prefshelper prefshelper;


    //ImageView img_fav, img_share;
    String image="";
    File file;
    // Declare Variables
    private List<ImageModel> list;
    private final Context context;

    public GalleryAdapter1(Context context, List<ImageModel> list) {
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
//        return (view == (LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        prefshelper = new Prefshelper(context);

        LayoutInflater inflater = null;
        ImageView imageView;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView =inflater.inflate(R.layout.list_gallery_item,container,false);

//        img_fav = (ImageView)itemView. findViewById(R.id.imageView_fav);
//        img_fav.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//
//        img_share = (ImageView)itemView. findViewById(R.id.imageView_share);
//        img_share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                BitmapDrawable bm = (BitmapDrawable)ImageDetailActivity.img_large.getDrawable();
//                Bitmap mysharebmp = bm.getBitmap();
//
//
//                try {
//                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                    mysharebmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//
//                    //you can create a new file name "test.jpeg"
//                    file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "test.png");
//
//                    FileOutputStream fo = new FileOutputStream(file);
//                    fo.write(bytes.toByteArray());
//
//                    // remember close de FileOutput
//                    fo.close();
//                    Log.d("done", "done");
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                String screenshotUri = String.valueOf(Uri.fromFile(file));
//                Log.d("imageeeeeeeeeee", screenshotUri);
//                String message = "This is an image i want to share on facebook from Klips app.";
//                Intent share = new Intent(Intent.ACTION_SEND);
//                share.setType("image/*");
//                share.putExtra(Intent.EXTRA_TEXT, image);
//                share.putExtra(Intent.EXTRA_STREAM, Uri.parse(screenshotUri));
//                share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//               v.getContext().startActivity(Intent.createChooser(share, "Share Via"));
//            }
//        });



        imageView=(ImageView)itemView.findViewById(R.id.image_product);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.with(context).load(list.get(position).getImage()).into(ImageDetailActivity.img_large);
                ImageDetailActivity.full_imagepath=list.get(position).getImage();
                ImageDetailActivity.image_id= Integer.parseInt(list.get(position).getId());
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
//        container.removeView((LinearLayout) object);
        container.removeView((RelativeLayout) object);
    }
    public void addTofavourites1()
    {
        try {
            final ProgressDialog pDialog = new ProgressDialog(context);
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "SIGNUP " + MapAppConstant.API + "favourite_image_operations");
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "get_image_by_id", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.d("", ".......response====" + response.toString());

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                       // Toast.makeText(context, "" + serverMessage, Toast.LENGTH_SHORT).show();
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {

                                    JSONObject ob=object.getJSONObject("data");
                                    ImageDetailActivity.status=ob.getString("favourite_status");
                                    Log.e("status on Click",""+ImageDetailActivity.status);
                                    if(ImageDetailActivity.status.equalsIgnoreCase("1")) {
                                        // if(prefshelper.getFavStatusIdFromPreference().equals("1"))
                                        //{
                                        // fav_status_id = 0;
                                        ImageDetailActivity.img_fav.setImageResource(R.drawable.fav_red);
                                        // prefshelper.storeFavStatusIdToPreference("" + 0);
                                        //}
                                    }
                                    else
                                    {
                                        //    fav_status_id=1;
                                        ImageDetailActivity.img_fav.setImageResource(R.drawable.my_favort);
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
                    params.put("image_id",""+ImageDetailActivity.image_id);

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
