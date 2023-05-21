package com.projectmobile.laundryku.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.projectmobile.laundryku.R;
import com.projectmobile.laundryku.model.ModelItemVip;

import java.util.ArrayList;

public class VipAdapter extends RecyclerView.Adapter<VipAdapter.VipViewHolder> {
    Context context;
    ArrayList<ModelItemVip> list;

    public VipAdapter(Context context, ArrayList<ModelItemVip> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public VipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_vip, parent, false);
        return new VipViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VipViewHolder holder, int position) {
        ModelItemVip vip = list.get(position);
        holder.namaItemVip.setText(vip.getNamaItemVip());
        holder.hargaItemVip.setText(vip.getHargaItemVip());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VipViewHolder extends RecyclerView.ViewHolder{
        TextView namaItemVip;
        TextView hargaItemVip;
        public VipViewHolder(@NonNull View itemView){
            super(itemView);
            namaItemVip = itemView.findViewById(R.id.namaItemVip);
            hargaItemVip = itemView.findViewById(R.id.keteranganhargaVip);
        }
    }
}
