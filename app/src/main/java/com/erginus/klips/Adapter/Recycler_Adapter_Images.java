package com.erginus.klips.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.erginus.klips.ImageDetailActivity;
import com.erginus.klips.Model.ImageModel;
import com.erginus.klips.R;
import com.erginus.klips.Commons.RecyclerViewHolder;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nazer on 3/2/2016.
 */
public class Recycler_Adapter_Images extends  RecyclerView.Adapter<RecyclerViewHolder> {


    Context context;
    //  LayoutInflater inflater;
    List<ImageModel> list;
    public Recycler_Adapter_Images(Context context, List<ImageModel> list) {
        this.list=list;
        this.context=context;
        //  inflater=LayoutInflater.from(context);
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.recycler_images, parent, false);

        RecyclerViewHolder viewHolder=new RecyclerViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Picasso.with(context).load(list.get(position).getImage()).into(holder.cardViewclick);
        // holder.title.setText(list.get(position).getArtistName());
       // holder.artist.setText(list.get(position).getdescription());
        holder.cardViewclick.setOnClickListener(clickListener);
        holder.cardViewclick.setTag(holder);

    }

    View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            RecyclerViewHolder vholder = (RecyclerViewHolder) v.getTag();
            int position = vholder.getPosition();

            Intent intent = new Intent(v.getContext(), ImageDetailActivity.class);
            intent.putExtra("list1", (Serializable) list);
            intent.putExtra("title", list.get(position).getName());
            intent.putExtra("id", list.get(position).getId());
            intent.putExtra("status", list.get(position).getFavStatus());

            intent.putExtra("image", list.get(position).getImage());
            intent.putExtra("cat_id", "images");
            v.getContext().startActivity(intent);


        }
    };



    @Override
    public int getItemCount() {
        return list.size();
    }
}
