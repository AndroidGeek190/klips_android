package com.erginus.klips.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;

import com.erginus.klips.Model.QuoteModel;

import com.erginus.klips.QuoteDetailActivity;
import com.erginus.klips.R;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

public class QuotesAdapter extends PagerAdapter {
// Declare Variables

private List<QuoteModel> list;
private final Context context;

public QuotesAdapter(Context context, List<QuoteModel> list) {
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
        }

@Override
public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = null;
        ImageView imageView;

        inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView =inflater.inflate(R.layout.list_gallery_item,container,false);
//        txt_title=(TextView)itemView.findViewById(R.id.textView_title);
//        txt_text=(TextView)itemView.findViewById(R.id.textView_desc);
//        txt_user=(TextView)itemView.findViewById(R.id.textView_user);
//        txt_time=(TextView)itemView.findViewById(R.id.textView_time);
//        txt_title.setText(list.get(position).getTitle());
//        txt_text.setText(list.get(position).getText());
//    txt_user.setText("Shared from"+" "+list.get(position).getFName()+" "+list.get(position).getLName());

        imageView=(ImageView)itemView.findViewById(R.id.image_product);
        imageView.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        //Intent intent = new Intent(v.getContext(), QuoteDetailActivity.class);
    Intent intent = new Intent(v.getContext(), QuoteDetailActivity.class);
    intent.putExtra("list1", (Serializable) list);
    intent.putExtra("title", list.get(position).getName());
    intent.putExtra("id", list.get(position).getId());
    intent.putExtra("cat_id","quotes");
    intent.putExtra("status", list.get(position).getFavStatus());
    intent.putExtra("image", list.get(position).getImage());

    v.getContext().startActivity(intent);
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
        container.removeView((LinearLayout) object);
        }
        }
