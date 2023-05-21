package com.projectmobile.laundryku.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.projectmobile.laundryku.R;
import com.projectmobile.laundryku.model.ModelItemKiloan;

import java.util.ArrayList;

public class KiloanAdapter extends RecyclerView.Adapter<KiloanAdapter.KiloanViewHolder> {
    Context context;
    ArrayList<ModelItemKiloan> list;

    public KiloanAdapter(Context context, ArrayList<ModelItemKiloan> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public KiloanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_kiloan, parent, false);
        return new KiloanViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull KiloanViewHolder holder, int position) {
        ModelItemKiloan kiloan = list.get(position);
        holder.namaPaketKiloan.setText(kiloan.getNamaPaketKiloan());
        holder.keteranganSatuPaketKiloan.setText(kiloan.getKeteranganSatuPaketKiloan());
        holder.keteranganDuaPaketKiloan.setText(kiloan.getKeteranganDuaPaketKiloan());
        holder.keteranganTigaPaketKiloan.setText(kiloan.getKeteranganTigaPaketKiloan());
        holder.keteranganEmpatPaketKiloan.setText(kiloan.getKeteranganEmpatPaketKiloan());
        holder.hargaPaketKiloan.setText(kiloan.getHargaPaketKiloan());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class KiloanViewHolder extends RecyclerView.ViewHolder{
        TextView namaPaketKiloan;
        TextView keteranganSatuPaketKiloan;
        TextView keteranganDuaPaketKiloan;
        TextView keteranganTigaPaketKiloan;
        TextView keteranganEmpatPaketKiloan;
        TextView hargaPaketKiloan;
        public KiloanViewHolder(@NonNull View itemView){
            super(itemView);
            namaPaketKiloan = itemView.findViewById(R.id.namaPaketKiloan);
            keteranganSatuPaketKiloan = itemView.findViewById(R.id.keteranganSatuPaketKiloan);
            keteranganDuaPaketKiloan = itemView.findViewById(R.id.keteranganDuaPaketKiloan);
            keteranganTigaPaketKiloan = itemView.findViewById(R.id.keteranganTigaPaketKiloan);
            keteranganEmpatPaketKiloan = itemView.findViewById(R.id.keteranganEmpatPaketKiloan);
            hargaPaketKiloan = itemView.findViewById(R.id.keteranganHargaKiloan);
        }
    }
}
