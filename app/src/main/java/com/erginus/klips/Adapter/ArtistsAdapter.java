package com.erginus.klips.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.erginus.klips.Model.ArtistModel;
import com.erginus.klips.Model.SongsModel;
import com.erginus.klips.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by paramjeet on 8/9/15.
 */
public class ArtistsAdapter extends BaseAdapter {

    // Declare Variables
    private List<ArtistModel> list;
    private final Context context;

    public ArtistsAdapter(Context context, List<ArtistModel> list) {
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
        TextView textView_artist, text_albm;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.artists_item, parent,
                false);

        imageView=(ImageView)itemView.findViewById(R.id.image_product);
        textView_artist=(TextView)itemView.findViewById(R.id.textView_artist);
        text_albm=(TextView)itemView.findViewById(R.id.textView);
        textView_artist.setText(list.get(position).getName());
        text_albm.setText(list.get(position).getDescription());
        Picasso.with(context).load(list.get(position).getImage()).into(imageView);
        return itemView;
    }


}
