package com.projectmobile.laundryku.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.projectmobile.laundryku.R;
import com.projectmobile.laundryku.model.PesanEkspress;

import java.util.List;

public class PesanEkspressAdapter extends RecyclerView.Adapter<PesanEkspressAdapter.PesanEkspressViewHolder> {
    private Context context;
    private List<PesanEkspress> listPesanEkspress;
    private Dialog dialog;

    public interface Dialog{
        void onClick (int pos);
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public PesanEkspressAdapter(Context context, List<PesanEkspress> listPesanEkspress) {
        this.context = context;
        this.listPesanEkspress = listPesanEkspress;
    }

    @NonNull
    @Override
    public PesanEkspressViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_riwayat_pesanekspress, viewGroup, false);
        return new PesanEkspressViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull PesanEkspressViewHolder holder, int position) {
        PesanEkspress pesanEkspress = listPesanEkspress.get(position);
        holder.kodePelangganEkspress.setText(pesanEkspress.getKodePelangganEkspress());
        holder.namaPelangganEkspress.setText(pesanEkspress.getNamaPelangganEkspress());
        holder.nomorHpEkspress.setText(pesanEkspress.getNomorHpEkspress());
        holder.tanggalMasukEkspress.setText(pesanEkspress.getTanggalMasukEkspress());
        holder.tanggalKeluarEkspress.setText(pesanEkspress.getTanggalKeluarEkspress());
        holder.paketEkspress.setText(pesanEkspress.getPaketEkspress());
        holder.kilogramEkspress.setText(pesanEkspress.getKilogramEkspress());
        holder.hargaEkspress.setText(pesanEkspress.getHargaEkspress());
    }

    @Override
    public int getItemCount() {
        return listPesanEkspress.size();
    }

    public class PesanEkspressViewHolder extends RecyclerView.ViewHolder{
        TextView kodePelangganEkspress, namaPelangganEkspress, nomorHpEkspress, tanggalMasukEkspress, tanggalKeluarEkspress, paketEkspress, kilogramEkspress, hargaEkspress;
        public PesanEkspressViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;
            kodePelangganEkspress = itemView.findViewById(R.id.tv_riwayat_kode_pelanggan_ekspress);
            namaPelangganEkspress = itemView.findViewById(R.id.tv_riwayat_nama_pelanggan_ekspress);
            nomorHpEkspress = itemView.findViewById(R.id.tv_riwayat_nomor_hp_ekspress);
            tanggalMasukEkspress = itemView.findViewById(R.id.tv_riwayat_tanggal_masuk_ekspress);
            tanggalKeluarEkspress = itemView.findViewById(R.id.tv_riwayat_tanggal_keluar_ekspress);
            paketEkspress = itemView.findViewById(R.id.tv_riwayat_paket_ekspress);
            kilogramEkspress = itemView.findViewById(R.id.tv_riwayat_kilogram_ekspress);
            hargaEkspress = itemView.findViewById(R.id.tv_riwayat_harga_ekspress);
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
