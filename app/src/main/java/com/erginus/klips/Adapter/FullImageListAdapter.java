package com.erginus.klips.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.erginus.klips.Model.ImageModel;
import com.erginus.klips.R;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by paramjeet on 8/9/15.
 */
public class FullImageListAdapter extends BaseAdapter {

    // Declare Variables
    private List<ImageModel> list;
    private final Context context;

    public FullImageListAdapter(Context context, List<ImageModel> list) {
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
         final ImageView imageView, img_fav;
        TextView txt_title,txt_artist;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.image_list_item, parent,
                false);
        txt_title=(TextView)itemView.findViewById(R.id.textView_title);
        txt_title.setText(list.get(position).getName());
        txt_artist=(TextView)itemView.findViewById(R.id.textView_artist);
        txt_artist.setText(list.get(position).getdescription());
        imageView=(ImageView)itemView.findViewById(R.id.image_product);
        Picasso.with(context).load(list.get(position).getImage()).into(imageView);

        return itemView;
    }


}
