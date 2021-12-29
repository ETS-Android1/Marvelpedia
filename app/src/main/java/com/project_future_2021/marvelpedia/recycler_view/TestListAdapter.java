package com.project_future_2021.marvelpedia.recycler_view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project_future_2021.marvelpedia.R;
import com.project_future_2021.marvelpedia.data.Hero;

import java.util.List;

public class TestListAdapter
        extends ListAdapter<Hero, TestListAdapter.MyTestViewHolder> {

    private static final String TAG = "TestListAdapter";

    public interface myTestClickListener {
        void onClick(View v, Hero data);
    }

    private final List<Hero> myTestAdapterHeroesList;
    private final myTestClickListener myTestClickListener;

    public TestListAdapter(List<Hero> myTestAdapterHeroesList, myTestClickListener myTestClickListener) {
        super(new HeroDC());
        this.myTestAdapterHeroesList = myTestAdapterHeroesList;
        this.myTestClickListener = myTestClickListener;
    }

    @NonNull
    @Override
    public MyTestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_list_item, parent, false);
        return new MyTestViewHolder(view, myTestClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyTestViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    void swapData(List<Hero> data) {
        submitList(data);
    }

    public class MyTestViewHolder extends RecyclerView.ViewHolder {

        private final myTestClickListener myTestClickListener;

        public MyTestViewHolder(@NonNull View itemView, myTestClickListener myTestClickListener) {
            super(itemView);
            this.myTestClickListener = myTestClickListener;
        }


        public void bind(Hero data) {
            TextView hero_name = itemView.findViewById(R.id.holder_list_hero_name);
            TextView hero_description = itemView.findViewById(R.id.holder_list_hero_description);
            ImageView hero_thumbnail = itemView.findViewById(R.id.holder_list_hero_thumbnail);
            ImageView hero_favorite = itemView.findViewById(R.id.holder_list_hero_favorite);
            if (data.getFavorite()) {
                hero_favorite.setImageResource(R.drawable.ic_yes_favorite);
            } else {
                hero_favorite.setImageResource(R.drawable.ic_no_favorite);
            }

            hero_name.setText(data.getName());
            hero_description.setText(data.getDescription());

            Glide.with(itemView)
                    .load(data.getThumbnail().makeImageWithVariant("portrait_xlarge"))
                    .placeholder(R.drawable.ic_baseline_image_search_24)
                    .into(hero_thumbnail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myTestClickListener.onClick(v, data);
                }
            });

            // handle the user clicking on the 'favorite' icon.
            hero_favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (data.getFavorite()) {
                        data.setFavorite(false);
                        hero_favorite.setImageResource(R.drawable.ic_no_favorite);
                    } else {
                        data.setFavorite(true);
                        hero_favorite.setImageResource(R.drawable.ic_yes_favorite);
                    }
                    notifyItemChanged(getPosition());
                }
            });

        }
    }


    private static class HeroDC extends DiffUtil.ItemCallback<Hero> {
        @Override
        public boolean areItemsTheSame(@NonNull Hero oldItem,
                                       @NonNull Hero newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Hero oldItem,
                                          @NonNull Hero newItem) {
            return oldItem.equals(newItem);
        }
    }
}