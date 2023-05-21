package com.projectmobile.laundryku.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.projectmobile.laundryku.R;
import com.projectmobile.laundryku.model.ModelItemEkspress;

import java.util.ArrayList;

;

public class EkspressAdapter extends RecyclerView.Adapter<EkspressAdapter.EkspressViewHolder>{
    Context context;
    ArrayList<ModelItemEkspress> list;

    public EkspressAdapter(Context context, ArrayList<ModelItemEkspress> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public EkspressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_ekspress, parent, false);
        return new EkspressViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EkspressViewHolder holder, int position) {
        ModelItemEkspress ekspress = list.get(position);
        holder.namaPaketEkspress.setText(ekspress.getNamaPaketEkspress());
        holder.keteranganPaketEkspress.setText(ekspress.getKeteranganPaketEkspress());
        holder.hargaPaketEkspress.setText(ekspress.getHargaPaketEkspress());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class EkspressViewHolder extends RecyclerView.ViewHolder{
        TextView namaPaketEkspress;
        TextView keteranganPaketEkspress;
        TextView hargaPaketEkspress;
        public EkspressViewHolder(@NonNull View itemView) {
            super(itemView);
            namaPaketEkspress = itemView.findViewById(R.id.namaPaketEkspress);
            keteranganPaketEkspress = itemView.findViewById(R.id.keteranganPaketEkspress);
            hargaPaketEkspress = itemView.findViewById(R.id.keteranganHargaEkspress);
        }
    }
}
