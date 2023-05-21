package com.projectmobile.laundryku.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.projectmobile.laundryku.R;
import com.projectmobile.laundryku.model.PesanKiloan;

import java.util.List;

public class PesanKiloanAdapter extends RecyclerView.Adapter<PesanKiloanAdapter.PesanKiloanViewHolder> {
    private Context context;
    private List<PesanKiloan> listPesanKiloan;
    private Dialog dialog;

    public interface Dialog{
        void onClick (int pos);
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public PesanKiloanAdapter(Context context, List<PesanKiloan> list) {
        this.context = context;
        this.listPesanKiloan = list;
    }

    @NonNull
    @Override
    public PesanKiloanViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_riwayat_pesankiloan, viewGroup, false);
        return new PesanKiloanViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull PesanKiloanViewHolder holder, int position) {
        PesanKiloan pesanKiloan = listPesanKiloan.get(position);
        holder.kodePelangganKiloan.setText(pesanKiloan.getKodePelangganKiloan());
        holder.namaPelangganKiloan.setText(pesanKiloan.getNamaPelangganKiloan());
        holder.nomorHpKiloan.setText(pesanKiloan.getNomorHpKiloan());
        holder.tanggalMasukKiloan.setText(pesanKiloan.getTanggalMasukKiloan());
        holder.tanggalKeluarKiloan.setText(pesanKiloan.getTanggalKeluarKiloan());
        holder.paketKiloan.setText(pesanKiloan.getPaketkiloan());
        holder.kilogramKiloan.setText(pesanKiloan.getKilogramKiloan());
        holder.hargaKiloan.setText(pesanKiloan.getHargaKiloan());
    }

    @Override
    public int getItemCount() {
        return listPesanKiloan.size();
    }

    public class PesanKiloanViewHolder extends RecyclerView.ViewHolder {
        TextView kodePelangganKiloan, namaPelangganKiloan, nomorHpKiloan, tanggalMasukKiloan, tanggalKeluarKiloan, paketKiloan, kilogramKiloan, hargaKiloan;

        public PesanKiloanViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;
            kodePelangganKiloan = itemView.findViewById(R.id.tv_riwayat_kode_pelanggan_kiloan);
            namaPelangganKiloan = itemView.findViewById(R.id.tv_riwayat_nama_pelanggan_kiloan);
            nomorHpKiloan = itemView.findViewById(R.id.tv_riwayat_nomor_hp_kiloan);
            tanggalMasukKiloan = itemView.findViewById(R.id.tv_riwayat_tanggal_masuk_kiloan);
            tanggalKeluarKiloan = itemView.findViewById(R.id.tv_riwayat_tanggal_keluar_kiloan);
            paketKiloan = itemView.findViewById(R.id.tv_riwayat_paket_kiloan);
            kilogramKiloan = itemView.findViewById(R.id.tv_riwayat_kilogram_kiloan);
            hargaKiloan = itemView.findViewById(R.id.tv_riwayat_harga_kiloan);
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
