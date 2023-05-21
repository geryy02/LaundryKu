package com.projectmobile.laundryku.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.projectmobile.laundryku.R;
import com.projectmobile.laundryku.model.PesanVip;

import java.util.List;

public class PesanVipAdapter extends  RecyclerView.Adapter<PesanVipAdapter.PesanVipViewHolder> {
    private Context context;
    private List<PesanVip> listPesanVip;
    private Dialog dialog;

    public interface Dialog{
        void onClick (int pos);
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public PesanVipAdapter(Context context, List<PesanVip> listPesanVip) {
        this.context = context;
        this.listPesanVip = listPesanVip;
    }

    @NonNull
    @Override
    public PesanVipViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_riwayat_pesanvip, viewGroup, false);
        return new PesanVipViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull PesanVipViewHolder holder, int position) {
        PesanVip pesanVip = listPesanVip.get(position);
        holder.kodePelangganVip.setText(pesanVip.getKodePelangganVip());
        holder.namaPelangganVip.setText(pesanVip.getNamaPelangganVip());
        holder.nomorHpVip.setText(pesanVip.getNomorHpVip());
        holder.tanggalMasukVip.setText(pesanVip.getTanggalMasukVip());
        holder.tanggalKeluarVip.setText(pesanVip.getTanggalKeluarVip());
        holder.pilihVip.setText(pesanVip.getPilihVip());
        holder.itemVip.setText(pesanVip.getItemVip());
        holder.hargaVip.setText(pesanVip.getHargaVip());
    }

    @Override
    public int getItemCount() {
        return listPesanVip.size();
    }

    public class PesanVipViewHolder extends RecyclerView.ViewHolder {
        TextView kodePelangganVip, namaPelangganVip, nomorHpVip, tanggalMasukVip, tanggalKeluarVip, pilihVip, itemVip, hargaVip;
        public PesanVipViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;
            kodePelangganVip = itemView.findViewById(R.id.tv_riwayat_kode_pelanggan_vip);
            namaPelangganVip = itemView.findViewById(R.id.tv_riwayat_nama_pelanggan_vip);
            nomorHpVip = itemView.findViewById(R.id.tv_riwayat_nomor_hp_vip);
            tanggalMasukVip = itemView.findViewById(R.id.tv_riwayat_tanggal_masuk_vip);
            tanggalKeluarVip = itemView.findViewById(R.id.tv_riwayat_tanggal_keluar_vip);
            pilihVip = itemView.findViewById(R.id.tv_riwayat_pilih_vip);
            itemVip = itemView.findViewById(R.id.tv_riwayat_item_vip);
            hargaVip = itemView.findViewById(R.id.tv_riwayat_harga_vip);
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
