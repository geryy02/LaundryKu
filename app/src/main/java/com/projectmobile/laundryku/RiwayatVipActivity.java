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
import com.projectmobile.laundryku.adapter.PesanVipAdapter;
import com.projectmobile.laundryku.model.PesanVip;

import java.util.ArrayList;
import java.util.List;

public class RiwayatVipActivity extends AppCompatActivity {

    private RecyclerView riwayatVip;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<PesanVip> list = new ArrayList<>();
    private PesanVipAdapter pesanVipAdapter;
    private LoadingDialog myLoadingDialog;
    private SwipeRefreshLayout refreshLayoutVip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_vip);
        myLoadingDialog = new LoadingDialog(this);
        Toolbar toolbar = findViewById(R.id.toolbar_riwayatVip);
        setSupportActionBar(toolbar);

        // Set tombol kembali (back) pada toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        riwayatVip = findViewById(R.id.recyclerviewRiwayatVip);

        refreshLayoutVip = findViewById(R.id.swipeRefresh_Vip);

        pesanVipAdapter = new PesanVipAdapter(RiwayatVipActivity.this, list);
        pesanVipAdapter.setDialog(new PesanVipAdapter.Dialog() {
            @Override
            public void onClick(int pos) {
                final CharSequence[] dialogIten = {"Edit", "Hapus"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(RiwayatVipActivity.this);
                dialog.setItems(dialogIten, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                Intent intent = new Intent(RiwayatVipActivity.this, EditPesanVipActivity.class);
                                intent.putExtra("id", list.get(pos).getId());
                                intent.putExtra("kodePelangganVip", list.get(pos).getKodePelangganVip());
                                intent.putExtra("namaPelangganVip", list.get(pos).getNamaPelangganVip());
                                intent.putExtra("nomorHpVip", list.get(pos).getNomorHpVip());
                                intent.putExtra("tanggalMasukVip", list.get(pos).getTanggalMasukVip());
                                intent.putExtra("tanggalKeluarVip", list.get(pos).getTanggalKeluarVip());
                                intent.putExtra("pilihVip", list.get(pos).getPilihVip());
                                intent.putExtra("itemVip", list.get(pos).getItemVip());
                                intent.putExtra("hargaVip", list.get(pos).getHargaVip());
                                startActivity(intent);
                                break;
                            case 1:
                                HapusPesanVip(list.get(pos).getId());
                                break;
                        }
                    }
                });
                dialog.show();
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RiwayatVipActivity.this, LinearLayoutManager.VERTICAL, false);
        riwayatVip.setLayoutManager(layoutManager);
        riwayatVip.setAdapter(pesanVipAdapter);

        getDataVip();

        refreshLayoutVip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataVip();
                refreshLayoutVip.setRefreshing(false);
            }
        });
    }

    private void getDataVip(){
        myLoadingDialog.show();
        db.collection("pesan_vip").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                list.clear();
                myLoadingDialog.cancel();
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        PesanVip pesanVip = new PesanVip(document.getString("kodePelangganVip"), document.getString("namaPelangganVip"), document.getString("nomorHpVip"), document.getString("tanggalMasukVip"), document.getString("tanggalKeluarVip"), document.getString("pilihVip"), document.getString("itemVip"), document.getString("hargaVip"));
                        pesanVip.setId(document.getId());
                        list.add(pesanVip);
                    }
                    pesanVipAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(RiwayatVipActivity.this, "Data gagal diambil!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //hapus data pesanan vip
    private void HapusPesanVip(String id){
        showInfoAlertDialog(id);
    }

    private void showInfoAlertDialog(String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RiwayatVipActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(RiwayatVipActivity.this).inflate(R.layout.layout_info_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText("Hapus Pesan VIP");
        ((TextView) view.findViewById(R.id.textMessage)).setText("Apakah ingin menghapus pemesanan VIP?");
        ((Button) view.findViewById(R.id.btnYes)).setText("Iya");
        ((Button) view.findViewById(R.id.btnNo)).setText("Tidak");
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.info);

        final AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.btnYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                myLoadingDialog.show();
                db.collection("pesan_vip").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        myLoadingDialog.cancel();
                        if (!task.isSuccessful()){
                            Toast.makeText(RiwayatVipActivity.this, "Data VIP gagal di hapus!", Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(RiwayatVipActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(RiwayatVipActivity.this).inflate(R.layout.layout_success_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText("Sukses");
        ((TextView) view.findViewById(R.id.textMessage)).setText("VIP telah berhasil dihapus!");
        ((Button) view.findViewById(R.id.btnAction)).setText("Ok");
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.success);

        final AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.btnAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                getDataVip();
            }
        });
        if (alertDialog.getWindow()  !=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
}