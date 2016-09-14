package com.erginus.klips.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.erginus.klips.Model.VideoModel;
import com.erginus.klips.R;
import com.erginus.klips.Commons.RecyclerViewHolder;
import com.erginus.klips.VideoDetailActivity;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kundan on 10/26/2015.
 */
public class RecyclerAdapter extends  RecyclerView.Adapter<RecyclerViewHolder> {


    Context context;
  //  LayoutInflater inflater;
List<VideoModel> list;
    public RecyclerAdapter(Context context, List<VideoModel> list) {
        this.list=list;
        this.context=context;
      //  inflater=LayoutInflater.from(context);
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);

        RecyclerViewHolder viewHolder=new RecyclerViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Picasso.with(context).load(list.get(position).getImage()).into(holder.cardViewclick);
        holder.title.setText(list.get(position).getName());
        holder.artist.setText(list.get(position).getdescription());
        holder.cardViewclick.setOnClickListener(clickListener);
        holder.cardViewclick.setTag(holder);
        
    }

    View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            RecyclerViewHolder vholder = (RecyclerViewHolder) v.getTag();
            int position = vholder.getPosition();

           
            Intent intent=new Intent(v.getContext(), VideoDetailActivity.class);
           intent.putExtra("list", (Serializable) list);
            intent.putExtra("currentIndex", position);
            intent.putExtra("title", list.get(position).getName());
            intent.putExtra("id", "video");
            intent.putExtra("image", list.get(position).getImage());
            intent.putExtra("status", list.get(position).getFavStatus());
            intent.putExtra("play_status", list.get(position).getPlayStatus());

            v.getContext().startActivity(intent);


        }
    };



    @Override
    public int getItemCount() {
        return list.size();
    }
}
