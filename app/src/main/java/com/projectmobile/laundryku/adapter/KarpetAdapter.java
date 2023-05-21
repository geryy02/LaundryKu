package com.projectmobile.laundryku.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.projectmobile.laundryku.R;
import com.projectmobile.laundryku.model.ModelItemKarpet;

import java.util.ArrayList;

public class KarpetAdapter extends RecyclerView.Adapter<KarpetAdapter.KarpetViewHolder> {
    Context context;
    ArrayList<ModelItemKarpet> list;

    public KarpetAdapter(Context context, ArrayList<ModelItemKarpet> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public KarpetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_karpet, parent, false);
        return new KarpetViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull KarpetViewHolder holder, int position) {
        ModelItemKarpet karpet = list.get(position);
        holder.namaItemKarpet.setText(karpet.getNamaItemKarpet());
        holder.hargaItemKarpet.setText(karpet.getHargaItemKarpet());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class KarpetViewHolder extends RecyclerView.ViewHolder{
        TextView namaItemKarpet;
        TextView hargaItemKarpet;
        public KarpetViewHolder(@NonNull View itemView){
            super(itemView);
            namaItemKarpet = itemView.findViewById(R.id.namaItemKarpet);
            hargaItemKarpet = itemView.findViewById(R.id.keteranganhargaKarpet);
        }
    }
}

