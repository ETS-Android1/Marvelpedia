package com.project_future_2021.marvelpedia.recycler_view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project_future_2021.marvelpedia.R;
import com.project_future_2021.marvelpedia.data.Hero;

public class MyViewHolder extends RecyclerView.ViewHolder {

    private final MyAdapter.onItemClickListener listener;


    public MyViewHolder(@NonNull View itemView, MyAdapter.onItemClickListener listener) {
        super(itemView);
        this.listener = listener;
    }

    public void bind(Hero data) {
        TextView hero_name = itemView.findViewById(R.id.holder_list_hero_name);
        TextView hero_description = itemView.findViewById(R.id.holder_list_hero_description);
        ImageView hero_thumbnail = itemView.findViewById(R.id.holder_list_hero_thumbnail);
        ImageView hero_favorite = itemView.findViewById(R.id.holder_list_hero_favorite);

        hero_name.setText(data.getName());
        hero_description.setText(data.getDescription());

        Glide.with(itemView)
                //.load(data.getThumbnail())
                .load(data.getThumbnail().makeImageWithVariant("portrait_xlarge"))
                .placeholder(R.drawable.ic_baseline_image_search_24)
                .into(hero_thumbnail);
        //Glide.with(v).load(heroesViewModel.temp_image_link).placeholder(R.drawable.ic_baseline_image_search_24).into(imageView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v, data);
            }
        });

        // handle the user clicking on the 'favorite' icon.
        hero_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.getFavorite()){
                    hero_favorite.setImageResource(R.drawable.ic_no_favorite);
                    data.setFavorite(false);
                }
                else{
                    hero_favorite.setImageResource(R.drawable.ic_yes_favorite);
                    data.setFavorite(true);
                }
            }
        });

    }
}
