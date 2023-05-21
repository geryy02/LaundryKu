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
import com.projectmobile.laundryku.adapter.PesanKiloanAdapter;
import com.projectmobile.laundryku.model.PesanKiloan;

import java.util.ArrayList;
import java.util.List;

public class RiwayatKiloanActivity extends AppCompatActivity {

    private RecyclerView riwayatKiloan;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<PesanKiloan> list = new ArrayList<>();
    private PesanKiloanAdapter pesanKiloanAdapter;
    private LoadingDialog myLoadingDialog;
    private SwipeRefreshLayout refreshLayoutKiloan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_kiloan);
        myLoadingDialog = new LoadingDialog(this);
        Toolbar toolbar = findViewById(R.id.toolbar_riwayatKiloan);
        setSupportActionBar(toolbar);

        // Set tombol kembali (back) pada toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        riwayatKiloan = findViewById(R.id.recyclerviewRiwayatKiloan);

        refreshLayoutKiloan = findViewById(R.id.swipeRefresh_Kiloan);

        pesanKiloanAdapter = new PesanKiloanAdapter(RiwayatKiloanActivity.this, list);
        pesanKiloanAdapter.setDialog(new PesanKiloanAdapter.Dialog() {
            @Override
            public void onClick(int pos) {
                final CharSequence[] dialogItem = {"Edit", "Hapus"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(RiwayatKiloanActivity.this);
                dialog.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                Intent intent = new Intent(RiwayatKiloanActivity.this, EditPesanKiloanActivity.class);
                                intent.putExtra("id", list.get(pos).getId());
                                intent.putExtra("kodePelangganKiloan", list.get(pos).getKodePelangganKiloan());
                                intent.putExtra("namaPelangganKiloan", list.get(pos).getNamaPelangganKiloan());
                                intent.putExtra("nomorHpKiloan", list.get(pos).getNomorHpKiloan());
                                intent.putExtra("tanggalMasukKiloan", list.get(pos).getTanggalMasukKiloan());
                                intent.putExtra("tanggalKeluarKiloan", list.get(pos).getTanggalKeluarKiloan());
                                intent.putExtra("paketKiloan", list.get(pos).getPaketkiloan());
                                intent.putExtra("kilogramKiloan", list.get(pos).getKilogramKiloan());
                                intent.putExtra("hargaKiloan", list.get(pos).getHargaKiloan());
                                startActivity(intent);
                                break;
                            case 1:
                                HapusPesanKiloan(list.get(pos).getId() );
                                break;
                        }
                    }
                });
                dialog.show();
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RiwayatKiloanActivity.this, LinearLayoutManager.VERTICAL, false);
        riwayatKiloan.setLayoutManager(layoutManager);
        riwayatKiloan.setAdapter(pesanKiloanAdapter);

        getDataKiloan();

        refreshLayoutKiloan.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataKiloan();
                refreshLayoutKiloan.setRefreshing(false);
            }
        });
    }

    //tampil data
    private void getDataKiloan(){
        myLoadingDialog.show();
        db.collection("pesan_kiloan").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                list.clear();
                myLoadingDialog.cancel();
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        PesanKiloan pesanKiloan = new PesanKiloan(document.getString("kodePelangganKiloan"), document.getString("namaPelangganKiloan"), document.getString("nomorHpKiloan"), document.getString("tanggalMasukKiloan"), document.getString("tanggalKeluarKiloan"), document.getString("paketKiloan"), document.getString("kilogramKiloan"), document.getString("hargaKiloan"));
                        pesanKiloan.setId(document.getId());
                        list.add(pesanKiloan);
                    }
                    pesanKiloanAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(RiwayatKiloanActivity.this, "Data gagal diambil!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //hapus data pesanan kiloan
    private void HapusPesanKiloan(String id){
        showInfoAlertDialog(id);
    }

    private void showInfoAlertDialog(String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RiwayatKiloanActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(RiwayatKiloanActivity.this).inflate(R.layout.layout_info_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText("Hapus Pesan Kiloan");
        ((TextView) view.findViewById(R.id.textMessage)).setText("Apakah ingin menghapus pemesanan Kiloan?");
        ((Button) view.findViewById(R.id.btnYes)).setText("Iya");
        ((Button) view.findViewById(R.id.btnNo)).setText("Tidak");
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.info);

        final AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.btnYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                myLoadingDialog.show();
                db.collection("pesan_kiloan").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        myLoadingDialog.cancel();
                        if (!task.isSuccessful()){
                            Toast.makeText(RiwayatKiloanActivity.this, "Data Kiloan gagal di hapus!", Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(RiwayatKiloanActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(RiwayatKiloanActivity.this).inflate(R.layout.layout_success_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText("Sukses");
        ((TextView) view.findViewById(R.id.textMessage)).setText("Kiloan telah berhasil dihapus!");
        ((Button) view.findViewById(R.id.btnAction)).setText("Ok");
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.success);

        final AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.btnAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                getDataKiloan();
            }
        });
        if (alertDialog.getWindow()  !=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
}