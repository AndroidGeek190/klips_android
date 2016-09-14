package com.erginus.klips.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.erginus.klips.Model.ImageModel;
import com.erginus.klips.Model.SongsModel;
import com.erginus.klips.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by paramjeet on 8/9/15.
 */
public class MyImagesAdapter extends BaseAdapter {

    // Declare Variables
    private List<ImageModel> list;
    private final Context context;

    public MyImagesAdapter(Context context, List<ImageModel> list) {
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

        ImageView imageView;
        TextView txt_title,description;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.list_my_images_item, parent,
                false);
        description=(TextView)itemView.findViewById(R.id.textView_desc);

        txt_title=(TextView)itemView.findViewById(R.id.textView_title);
        txt_title.setText(list.get(position).getName());
        description.setText(list.get(position).getdescription());

        imageView=(ImageView)itemView.findViewById(R.id.image_product);
        Picasso.with(context).load(list.get(position).getImage()).into(imageView);
        return itemView;
    }


}
