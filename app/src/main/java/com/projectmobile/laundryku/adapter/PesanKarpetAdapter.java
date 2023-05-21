package com.projectmobile.laundryku.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.projectmobile.laundryku.R;
import com.projectmobile.laundryku.model.PesanKarpet;

import java.util.List;


public class PesanKarpetAdapter extends RecyclerView.Adapter<PesanKarpetAdapter.PesanKarpetViewHolder>{
    private Context context;
    private List<PesanKarpet> listPesanKarpet;
    private Dialog dialog;

    public interface Dialog{
        void onClick (int pos);
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public PesanKarpetAdapter(Context context, List<PesanKarpet> listPesanKarpet) {
        this.context = context;
        this.listPesanKarpet = listPesanKarpet;
    }

    @NonNull
    @Override
    public PesanKarpetViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_riwayat_pesankarpet, viewGroup, false);
        return new PesanKarpetViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull PesanKarpetViewHolder holder, int position) {
        PesanKarpet pesanKarpet = listPesanKarpet.get(position);
        holder.kodePelangganKarpet.setText(pesanKarpet.getKodePelangganKarpet());
        holder.namaPelangganKarpet.setText(pesanKarpet.getNamaPelangganKarpet());
        holder.nomorHpKarpet.setText(pesanKarpet.getNomorHpKarpet());
        holder.tanggalMasukKarpet.setText(pesanKarpet.getTanggalMasukKarpet());
        holder.tanggalKeluarKarpet.setText(pesanKarpet.getTanggalKeluarKarpet());
        holder.pilihKarpet.setText(pesanKarpet.getPilihKarpet());
        holder.itemKarpet.setText(pesanKarpet.getItemKarpet());
        holder.hargaKarpet.setText(pesanKarpet.getHargaKarpet());
    }

    @Override
    public int getItemCount() {
        return listPesanKarpet.size();
    }

    public class PesanKarpetViewHolder extends RecyclerView.ViewHolder{
        TextView kodePelangganKarpet, namaPelangganKarpet, nomorHpKarpet, tanggalMasukKarpet, tanggalKeluarKarpet, pilihKarpet, itemKarpet, hargaKarpet;

        public PesanKarpetViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;
            kodePelangganKarpet = itemView.findViewById(R.id.tv_riwayat_kode_pelanggan_karpet);
            namaPelangganKarpet = itemView.findViewById(R.id.tv_riwayat_nama_pelanggan_karpet);
            nomorHpKarpet = itemView.findViewById(R.id.tv_riwayat_nomor_hp_karpet);
            tanggalMasukKarpet = itemView.findViewById(R.id.tv_riwayat_tanggal_masuk_karpet);
            tanggalKeluarKarpet = itemView.findViewById(R.id.tv_riwayat_tanggal_keluar_karpet);
            pilihKarpet = itemView.findViewById(R.id.tv_riwayat_pilih_karpet);
            itemKarpet = itemView.findViewById(R.id.tv_riwayat_item_karpet);
            hargaKarpet = itemView.findViewById(R.id.tv_riwayat_harga_karpet);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dialog!=null){
                        dialog.onClick(getLayoutPosition());
                    }
                }
            });
        }
    }
}
