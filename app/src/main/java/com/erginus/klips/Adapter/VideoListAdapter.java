package com.erginus.klips.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.erginus.klips.Model.VideoModel;
import com.erginus.klips.R;
import com.erginus.klips.VideoDetailActivity;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;


public class VideoListAdapter extends PagerAdapter
{
    TextView txt_title, txt_artist;
    //Declare Variables
    private List<VideoModel> songsList;
    private final Context context;

    public VideoListAdapter(Context context, List<VideoModel> list) {
        this.context = context;
        this.songsList=list;
    }

    @Override
    public int getCount() {
        return songsList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
   LayoutInflater inflater = null;
        ImageView imageView;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView =inflater.inflate(R.layout.list_video_item,container,false);

        txt_title=(TextView)itemView.findViewById(R.id.textView_title);
        txt_artist=(TextView)itemView.findViewById(R.id.textView_artist);
        txt_title.setText(songsList.get(position).getName());
        txt_artist.setText(songsList.get(position).getdescription());

        imageView=(ImageView)itemView.findViewById(R.id.image_product);
        imageView.setAdjustViewBounds(true);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent=new Intent(v.getContext(), VideoDetailActivity.class);
                intent.putExtra("currentIndex", position);
                intent.putExtra("id", "video"); //category id
                intent.putExtra("list", (Serializable) songsList);
                intent.putExtra("title",""+txt_title);
                intent.putExtra("artist",""+txt_artist);
               // intent.putExtra("description",""+songsList.get(position).getdescription());
                intent.putExtra("image", songsList.get(position).getImage());
                 intent.putExtra("duration", songsList.get(position).getDuration());
                intent.putExtra("status", songsList.get(position).getFavStatus());
                intent.putExtra("play_status", songsList.get(position).getPlayStatus());
                Log.e("currentIndex",""+ position);
                Log.e("id", "video"); //category id
                Log.e("list",""+ songsList);
                Log.e("title", "" + txt_title);
                Log.e("artist", "" + txt_artist);
                Log.e("image", ""+songsList.get(position).getImage());
                Log.e("duration", ""+songsList.get(position).getDuration());
                v.getContext().startActivity(intent);
                
            }
        });



        Picasso.with(context).load(songsList.get(position).getImage()).into(imageView);


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
        container.removeView((LinearLayout) object);
    }

}
