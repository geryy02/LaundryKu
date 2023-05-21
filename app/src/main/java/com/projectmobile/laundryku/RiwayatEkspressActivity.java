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
import com.projectmobile.laundryku.adapter.PesanEkspressAdapter;
import com.projectmobile.laundryku.model.PesanEkspress;

import java.util.ArrayList;
import java.util.List;

public class RiwayatEkspressActivity extends AppCompatActivity {
;
    private RecyclerView riwayatEkspress;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<PesanEkspress> list = new ArrayList<>();
    private PesanEkspressAdapter pesanEkspressAdapter;
    private LoadingDialog myLoadingDialog;
    private SwipeRefreshLayout refreshLayoutEkspress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_ekspress);
        myLoadingDialog = new LoadingDialog(this);
        Toolbar toolbar = findViewById(R.id.toolbar_riwayatEkspress);
        setSupportActionBar(toolbar);

        // Set tombol kembali (back) pada toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        riwayatEkspress = findViewById(R.id.recyclerviewRiwayatEkspress);

        refreshLayoutEkspress = findViewById(R.id.swipeRefresh_Ekspress);

        pesanEkspressAdapter = new PesanEkspressAdapter(RiwayatEkspressActivity.this, list);
        pesanEkspressAdapter.setDialog(new PesanEkspressAdapter.Dialog() {
            @Override
            public void onClick(int pos) {
                final CharSequence[] dialogItem = {"Edit", "Hapus"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(RiwayatEkspressActivity.this);
                dialog.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                Intent intent = new Intent(RiwayatEkspressActivity.this, EditPesanEkspressActivity.class);
                                intent.putExtra("id", list.get(pos).getId());
                                intent.putExtra("kodePelangganEkspress", list.get(pos).getKodePelangganEkspress());
                                intent.putExtra("namaPelangganEkspress", list.get(pos).getNamaPelangganEkspress());
                                intent.putExtra("nomorHpEkspress", list.get(pos).getNomorHpEkspress());
                                intent.putExtra("tanggalMasukEkspress", list.get(pos).getTanggalMasukEkspress());
                                intent.putExtra("tanggalKeluarEkspress", list.get(pos).getTanggalKeluarEkspress());
                                intent.putExtra("paketEkspress", list.get(pos).getPaketEkspress());
                                intent.putExtra("kilogramEkspress", list.get(pos).getKilogramEkspress());
                                intent.putExtra("hargaEkspress", list.get(pos).getHargaEkspress());
                                startActivity(intent);
                                break;
                            case 1:
                                HapusPesanEkspress(list.get(pos).getId() );
                                break;
                        }
                    }
                });
                dialog.show();
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RiwayatEkspressActivity.this, LinearLayoutManager.VERTICAL, false);
        riwayatEkspress.setLayoutManager(layoutManager);
        riwayatEkspress.setAdapter(pesanEkspressAdapter);

        getDataEkspress();

        refreshLayoutEkspress.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataEkspress();
                refreshLayoutEkspress.setRefreshing(false);
            }
        });
    }

    //tampil data
    private void getDataEkspress(){
        myLoadingDialog.show();
        db.collection("pesan_ekspress").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                list.clear();
                myLoadingDialog.cancel();
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        PesanEkspress pesanEkspress = new PesanEkspress(document.getString("kodePelangganEkspress"), document.getString("namaPelangganEkspress"), document.getString("nomorHpEkspress"), document.getString("tanggalMasukEkspress"), document.getString("tanggalKeluarEkspress"), document.getString("paketEkspress"), document.getString("kilogramEkspress"), document.getString("hargaEkspress"));
                        pesanEkspress.setId(document.getId());
                        list.add(pesanEkspress);
                    }
                    pesanEkspressAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(RiwayatEkspressActivity.this, "Data gagal diambil!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //hapus data pesanan ekspress
    private void HapusPesanEkspress(String id){
        showInfoAlertDialog(id);

    }

    private void showInfoAlertDialog(String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RiwayatEkspressActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(RiwayatEkspressActivity.this).inflate(R.layout.layout_info_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText("Hapus Pesan Ekspress");
        ((TextView) view.findViewById(R.id.textMessage)).setText("Apakah ingin menghapus pemesanan Ekapress?");
        ((Button) view.findViewById(R.id.btnYes)).setText("Iya");
        ((Button) view.findViewById(R.id.btnNo)).setText("Tidak");
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.info);

        final AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.btnYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                myLoadingDialog.show();
                db.collection("pesan_ekspress").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        myLoadingDialog.cancel();
                        if (!task.isSuccessful()){
                            Toast.makeText(RiwayatEkspressActivity.this, "Data Ekspress gagal di hapus!", Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(RiwayatEkspressActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(RiwayatEkspressActivity.this).inflate(R.layout.layout_success_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText("Sukses");
        ((TextView) view.findViewById(R.id.textMessage)).setText("Ekspress telah berhasil dihapus!");
        ((Button) view.findViewById(R.id.btnAction)).setText("Ok");
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.success);

        final AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.btnAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                getDataEkspress();
            }
        });
        if (alertDialog.getWindow()  !=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
}