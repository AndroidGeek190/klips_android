package com.erginus.klips.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.erginus.klips.Model.CountryModel;
import com.erginus.klips.Model.SongsModel;
import com.erginus.klips.R;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by paramjeet on 29/9/15.
 */
public class CountryAdapter extends BaseAdapter{
    private final Context context;
    private List<CountryModel> countryList;

        public CountryAdapter(Context context, List<CountryModel> list) {

            this.context = context;
            this.countryList=list;
        }

        @Override
        public View getDropDownView(int position, View convertView,ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

    @Override
    public int getCount() {
        return countryList.size();
    }

    @Override
    public Object getItem(int position) {
        return countryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return countryList.indexOf(countryList.get(position));
    }

    @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater= (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=inflater.inflate(R.layout.layout_spinner_item, parent, false);
            TextView label=(TextView)row.findViewById(R.id.textView_item);
            label.setText(countryList.get(position).getCountry_name());

            return row;
        }
    }


