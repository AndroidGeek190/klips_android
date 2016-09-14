package com.erginus.klips.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.erginus.klips.ImageDetailActivity;
import com.erginus.klips.Model.QuoteModel;
import com.erginus.klips.QuoteDetailActivity;
import com.erginus.klips.R;
import com.erginus.klips.Commons.RecyclerViewHolder;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nazer on 3/2/2016.
 */
public class Recycle_Adapter_Quotes extends  RecyclerView.Adapter<RecyclerViewHolder> {


    Context context;
    //  LayoutInflater inflater;
    List<QuoteModel> list;
    public Recycle_Adapter_Quotes(Context context, List<QuoteModel> list) {
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

             Intent intent = new Intent(v.getContext(), QuoteDetailActivity.class);
            intent.putExtra("title", list.get(position).getName());
            intent.putExtra("list1", (Serializable) list);
            intent.putExtra("id", list.get(position).getId());
            intent.putExtra("cat_id","quotes");
            intent.putExtra("status", "1");
            intent.putExtra("image", list.get(position).getImage());
            v.getContext().startActivity(intent);


        }
    };



    @Override
    public int getItemCount() {
        return list.size();
    }
}
