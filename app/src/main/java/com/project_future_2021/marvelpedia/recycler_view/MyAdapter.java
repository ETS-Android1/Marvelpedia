package com.project_future_2021.marvelpedia.recycler_view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project_future_2021.marvelpedia.R;
import com.project_future_2021.marvelpedia.data.Hero;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private static final String TAG = "MyAdapter";

    public interface onItemClickListener {
        void onClick(View v, Hero data);
    }

    private final List<Hero> adapterHeroesList;
    private final onItemClickListener adapterListener;

    public MyAdapter(List<Hero> adapterHeroesList, onItemClickListener adapterListener) {
        this.adapterHeroesList = adapterHeroesList;
        this.adapterListener = adapterListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_list_item, parent, false);
        return new MyViewHolder(view, adapterListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(adapterHeroesList.get(position));
    }

    public void updateList(List<Hero> newList) {
        adapterHeroesList.clear();
        adapterHeroesList.addAll(newList);
    }

    @Override
    public int getItemCount() {
        return adapterHeroesList.size();
    }
}