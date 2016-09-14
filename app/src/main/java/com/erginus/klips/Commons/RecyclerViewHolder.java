package com.erginus.klips.Commons;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.erginus.klips.R;

/**
 * Created by kundan on 10/26/2015.
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    public TextView title,artist;
    public ImageView imageView;
    public ImageView cardViewclick;
    public RecyclerViewHolder(View itemView) {
        super(itemView);

        cardViewclick=(ImageView)itemView.findViewById(R.id.image_product);
        title= (TextView) itemView.findViewById(R.id.textView_title);
        artist= (TextView) itemView.findViewById(R.id.textView_artist);


    }

}
