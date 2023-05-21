package com.projectmobile.laundryku;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.projectmobile.laundryku.adapter.PesanKarpetAdapter;
import com.projectmobile.laundryku.model.PesanKarpet;

import java.util.ArrayList;
import java.util.List;

public class RiwayatKarpetActivity extends AppCompatActivity {

    private RecyclerView riwayatKarpet;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<PesanKarpet> list = new ArrayList<>();
    private PesanKarpetAdapter pesanKarpetAdapter;
    private LoadingDialog myLoadingDialog;
    private SwipeRefreshLayout refreshLayoutKarpet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_karpet);
        myLoadingDialog = new LoadingDialog(this);
        Toolbar toolbar = findViewById(R.id.toolbar_riwayatKarpet);
        setSupportActionBar(toolbar);

        // Set tombol kembali (back) pada toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        riwayatKarpet = findViewById(R.id.recyclerviewRiwayatKarpet);

        refreshLayoutKarpet = findViewById(R.id.swipeRefresh_Karpet);

        pesanKarpetAdapter = new PesanKarpetAdapter(RiwayatKarpetActivity.this, list);
        pesanKarpetAdapter.setDialog(new PesanKarpetAdapter.Dialog() {
            @Override
            public void onClick(int pos) {
                final CharSequence[] dialogItem = {"Edit", "Hapus"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(RiwayatKarpetActivity.this);
                dialog.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                Intent intent = new Intent(RiwayatKarpetActivity.this, EditPesanKarpetActivity.class);
                                intent.putExtra("id", list.get(pos).getId());
                                intent.putExtra("kodePelangganKarpet", list.get(pos).getKodePelangganKarpet());
                                intent.putExtra("namaPelangganKarpet", list.get(pos).getNamaPelangganKarpet());
                                intent.putExtra("nomorHpKarpet", list.get(pos).getNomorHpKarpet());
                                intent.putExtra("tanggalMasukKarpet", list.get(pos).getTanggalMasukKarpet());
                                intent.putExtra("tanggalKeluarKarpet", list.get(pos).getTanggalKeluarKarpet());
                                intent.putExtra("pilihKarpet", list.get(pos).getPilihKarpet());
                                intent.putExtra("itemKarpet", list.get(pos).getItemKarpet());
                                intent.putExtra("hargaKarpet", list.get(pos).getHargaKarpet());
                                startActivity(intent);
                                break;
                            case 1:
                                HapusPesanKarpet(list.get(pos).getId());
                                break;
                        }
                    }
                });
                dialog.show();
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RiwayatKarpetActivity.this, LinearLayoutManager.VERTICAL, false);
        riwayatKarpet.setLayoutManager(layoutManager);
        riwayatKarpet.setAdapter(pesanKarpetAdapter);

        getDataKarpet();

        refreshLayoutKarpet.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataKarpet();
                refreshLayoutKarpet.setRefreshing(false);
            }
        });
    }

    private void getDataKarpet(){
        myLoadingDialog.show();
        db.collection("pesan_karpet").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                list.clear();
                myLoadingDialog.cancel();
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        PesanKarpet pesanKarpet = new PesanKarpet(document.getString("kodePelangganKarpet"), document.getString("namaPelangganKarpet"), document.getString("nomorHpKarpet"), document.getString("tanggalMasukKarpet"), document.getString("tanggalKeluarKarpet"), document.getString("pilihKarpet"), document.getString("itemKarpet"), document.getString("hargaKarpet"));
                        pesanKarpet.setId(document.getId());
                        list.add(pesanKarpet);
                    }
                    pesanKarpetAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(RiwayatKarpetActivity.this, "Data gagal diambil!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //hapus data pesanan karpet
    private void HapusPesanKarpet(String id){
        showInfoAlertDialog(id);
    }

    private void showInfoAlertDialog(String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RiwayatKarpetActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(RiwayatKarpetActivity.this).inflate(R.layout.layout_info_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText("Hapus Pesan Karpet");
        ((TextView) view.findViewById(R.id.textMessage)).setText("Apakah ingin menghapus pemesanan Karpet?");
        ((Button) view.findViewById(R.id.btnYes)).setText("Iya");
        ((Button) view.findViewById(R.id.btnNo)).setText("Tidak");
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.info);

        final AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.btnYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                myLoadingDialog.show();
                db.collection("pesan_karpet").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        myLoadingDialog.cancel();
                        if (!task.isSuccessful()){
                            Toast.makeText(RiwayatKarpetActivity.this, "Data Karpet gagal di hapus!", Toast.LENGTH_SHORT).show();
                        }
                        showSuccessDialog();
                    }
                });
            }
        });
        view.findViewById(R.id.btnNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        if (alertDialog.getWindow()  !=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RiwayatKarpetActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(RiwayatKarpetActivity.this).inflate(R.layout.layout_success_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText("Sukses");
        ((TextView) view.findViewById(R.id.textMessage)).setText("Karpet telah berhasil dihapus!");
        ((Button) view.findViewById(R.id.btnAction)).setText("Ok");
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.success);

        final AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.btnAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                getDataKarpet();
            }
        });
        if (alertDialog.getWindow()  !=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
}