package com.technophile.sarah;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class ArticleAdapter extends ArrayAdapter<Articles> {

    public ArticleAdapter(Context mcontext, ArrayList<Articles> newsArrayList) {
        super(mcontext, 0, newsArrayList);

    }



    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        View convertView=view;
        ViewHolder holder;

        if (convertView == null) {
            convertView =View.inflate(getContext(),R.layout.news_items,null);

            holder = new ViewHolder();
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.tv_section = (TextView) convertView.findViewById(R.id.tv_section);
            holder.tv_contributor = (TextView) convertView.findViewById(R.id.tv_contributor);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Articles articles = getItem(position);
        if (articles != null) {
            holder.tv_title.setText(articles.getTitle());
            holder.tv_date.setText(articles.getDate());
            holder.tv_section.setText(articles.getSection());
            if (!TextUtils.isEmpty(articles.getcontributorsName())) {
                holder.tv_contributor.setVisibility(View.VISIBLE);
                holder.tv_contributor.setText(articles.getcontributorsName());
            }else {
                holder.tv_contributor.setVisibility(View.GONE);
            }
        }


        return convertView;
    }

    static class ViewHolder {
        TextView tv_title;
        TextView tv_date;
        TextView tv_section;
        TextView tv_contributor;
    }
}
