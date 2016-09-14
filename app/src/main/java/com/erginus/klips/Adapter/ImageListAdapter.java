package com.erginus.klips.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.erginus.klips.Fragments.FavImagesFragment;
import com.erginus.klips.Fragments.FavSongsFragment;
import com.erginus.klips.Model.ImageModel;
import com.erginus.klips.Model.VideoModel;
import com.erginus.klips.R;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by paramjeet on 8/9/15.
 */
public class ImageListAdapter extends BaseAdapter {

    // Declare Variables
    private List<ImageModel> list;
    private final Context context;
    Prefshelper prefshelper;
    String status, image_id;
    int item_position;

    public ImageListAdapter(Context context, List<ImageModel> list) {
        this.context = context;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.indexOf(list.get(position));
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater;
        prefshelper=new Prefshelper(context);
        ImageButton img_fav;
        final ImageView imageView;
        TextView txt_title,txt_artist;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.list_item, parent,
                false);
        txt_title=(TextView)itemView.findViewById(R.id.textView_title);
        txt_title.setText(list.get(position).getName());
        txt_artist=(TextView)itemView.findViewById(R.id.textView_artist);
        txt_artist.setText(list.get(position).getdescription());
        img_fav=(ImageButton)itemView.findViewById(R.id.image_fav);
        img_fav.setFocusable(false);
        img_fav.setFocusableInTouchMode(false);
        imageView=(ImageView)itemView.findViewById(R.id.image_product);
        Picasso.with(context).load(list.get(position).getImage()).into(imageView);
        img_fav.setTag(position);
        img_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_position = (Integer) v.getTag();
                image_id = list.get(item_position).getId();
                status = list.get(item_position).getFavStatus();
                       if (status.equals("0")) {

                            status = "1";
                            addTofavourites();
                        } else {

                            status = "0";
                            addTofavourites();

                        }
                    }

        });
        return itemView;
    }

    public void addTofavourites() {
        try {
            final ProgressDialog pDialog = new ProgressDialog(context);
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
                        Toast.makeText(context, "" + serverMessage, Toast.LENGTH_SHORT).show();
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {
                                    FavImagesFragment.list.remove(item_position);
                                    FavImagesFragment.horizontalListAdapter.notifyDataSetChanged();                           }


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
                    params.put("image_id",""+image_id);
                    params.put("image_favourite_status", status);
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
