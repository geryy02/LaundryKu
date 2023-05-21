package com.projectmobile.laundryku;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditPesanKarpetActivity extends AppCompatActivity {

    private EditText etEditKodePelangganKarpet, etEditNamaPelangganKarpet, etEditNomorHpKarpet, etEditTanggalMasukKarpet, etEditTanggalKeluarKarpet, etEditItemKarpet, etEditHargaKarpet;
    private Spinner spEditKarpet;
    private int hargaItemKarpet1 = 15000;
    private int hargaItemKarpet2 = 25000;
    private int hargaItemKarpet3 = 30000;
    private Button btnUbahKarpet;
    private String id = "";
    private FirebaseFirestore db;
    private LoadingDialog myLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pesan_karpet);
        db = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar_edit_pesanKarpet);
        setSupportActionBar(toolbar);

        // Set tombol kembali (back) pada toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        etEditKodePelangganKarpet = findViewById(R.id.et_edit_kodepelanggan_karpet);
        etEditNamaPelangganKarpet = findViewById(R.id.et_edit_namapelanggan_karpet);
        etEditNomorHpKarpet = findViewById(R.id.et_edit_nomorhp_karpet);

        etEditTanggalMasukKarpet = findViewById(R.id.et_edit_tanggalmasuk_karpet);
        etEditTanggalKeluarKarpet = findViewById(R.id.et_edit_tanggalkeluar_karpet);
        final Calendar kalender = Calendar.getInstance();
        int tahun = kalender.get(Calendar.YEAR);
        int bulan = kalender.get(Calendar.MONTH);
        int hari = kalender.get(Calendar.DAY_OF_MONTH);

        spEditKarpet = findViewById(R.id.sp_edit_karpet);
        ArrayAdapter<CharSequence> adapterPesanKarpet = ArrayAdapter.createFromResource(this, R.array.karpet_array, R.layout.spinner_dropdown);
        adapterPesanKarpet.setDropDownViewResource(R.layout.spinner_dropdown);
        spEditKarpet.setAdapter(adapterPesanKarpet);

        etEditItemKarpet = findViewById(R.id.et_edit_item_karpet);
        etEditHargaKarpet = findViewById(R.id.et_edit_harga_karpet);

        btnUbahKarpet = findViewById(R.id.btn_ubah_karpet);

        myLoadingDialog = new LoadingDialog(this);

        Intent intent = getIntent();
        if (intent !=null){
            id = intent.getStringExtra("id");
            String kodePelangganKarpet = intent.getStringExtra("kodePelangganKarpet");
            String namaPelangganKarpet = intent.getStringExtra("namaPelangganKarpet");
            String nomorHpKarpet = intent.getStringExtra("nomorHpKarpet");
            String tanggalMasukKarpet = intent.getStringExtra("tanggalMasukKarpet");
            String tanggalKeluarKarpet = intent.getStringExtra("tanggalKeluarKarpet");
            String pilihKarpet = intent.getStringExtra("pilihKarpet");
            String itemKarpet = intent.getStringExtra("itemKarpet");
            String hargaKarpet = intent.getStringExtra("hargaKarpet");

            // Set nilai-nilai tersebut pada form input
            etEditKodePelangganKarpet.setText(kodePelangganKarpet);
            etEditNamaPelangganKarpet.setText(namaPelangganKarpet);
            etEditNomorHpKarpet.setText(nomorHpKarpet);
            etEditTanggalMasukKarpet.setText(tanggalMasukKarpet);
            etEditTanggalKeluarKarpet.setText(tanggalKeluarKarpet);
            spEditKarpet.setSelection(getIndex(spEditKarpet, pilihKarpet));
            etEditItemKarpet.setText(itemKarpet);
            etEditHargaKarpet.setText(hargaKarpet);
        }

        spEditKarpet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedItem = adapterView.getItemAtPosition(position).toString();

                // Set nilai default kilogram dan harga sesuai dengan paket kiloan yang dipilih
                if (selectedItem.equals("Pilih Item")){
                    etEditItemKarpet.setText("");
                    etEditHargaKarpet.setText("");
                }else if (selectedItem.equals("Karpet Tipis")) {
                    etEditItemKarpet.setText("1");
                    etEditHargaKarpet.setText(String.valueOf(hargaItemKarpet1));
                } else if (selectedItem.equals("Karpet Sedang")) {
                    etEditItemKarpet.setText("1");
                    etEditHargaKarpet.setText(String.valueOf(hargaItemKarpet2));
                } else if (selectedItem.equals("Karpet Super")) {
                    etEditItemKarpet.setText("1");
                    etEditHargaKarpet.setText(String.valueOf(hargaItemKarpet3));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        etEditTanggalMasukKarpet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(EditPesanKarpetActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int tahun, int bulan, int hari) {
                        // Set tanggal yang dipilih ke EditText
                        etEditTanggalMasukKarpet.setText(hari + "/" + (bulan + 1) + "/" + tahun);
                    }
                }, tahun, bulan, hari);
                dialog.show();
            }
        });

        etEditTanggalKeluarKarpet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(EditPesanKarpetActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int tahun, int bulan, int hari) {
                        // Set tanggal yang dipilih ke EditText
                        etEditTanggalKeluarKarpet.setText(hari + "/" + (bulan + 1) + "/" + tahun);
                    }
                }, tahun, bulan, hari);
                dialog.show();
            }
        });

        // Set listener untuk EditText item
        etEditItemKarpet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                //tidak perlu
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Ambil nilai kilogram kiloan dari EditText
                String itemKarpetString = charSequence.toString();

                // Ubah nilai kilogram kiloan menjadi integer
                int itemKarpet = 0;
                if (!itemKarpetString.isEmpty()){
                    itemKarpet = Integer.parseInt(itemKarpetString);
                }

                // Ambil paket kiloan yang dipilih dari Spinner
                String pilihItem = spEditKarpet.getSelectedItem().toString();

                // Set harga sesuai dengan paket kiloan yang dipilih
                int hargaKarpet = 0;
                if (pilihItem.equals("Karpet Tipis")) {
                    hargaKarpet = hargaItemKarpet1;
                } else if (pilihItem.equals("Karpet Sedang")) {
                    hargaKarpet = hargaItemKarpet2;
                } else if (pilihItem.equals("Karpet Super")) {
                    hargaKarpet = hargaItemKarpet3;
                }

                // Hitung harga baru berdasarkan kilogram yang diubah
                int hargaBaruKarpet = itemKarpet * hargaKarpet;

                // Set nilai harga baru pada EditText harga
                etEditHargaKarpet.setText(String.valueOf(hargaBaruKarpet));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //tidak perlu
            }
        });

        btnUbahKarpet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfoEditAlertDialog();
            }
        });

    }
    private void showInfoEditAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditPesanKarpetActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(EditPesanKarpetActivity.this).inflate(R.layout.layout_info_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText("Edit Pesan Karpet");
        ((TextView) view.findViewById(R.id.textMessage)).setText("Apakah ingin mengubah pesan Karpet?");
        ((Button) view.findViewById(R.id.btnYes)).setText("Iya");
        ((Button) view.findViewById(R.id.btnNo)).setText("Tidak");
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.info);

        final AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.btnYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                myLoadingDialog.show();
                updateData();
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
    private void updateData() {
        String kodePelangganKarpet = etEditKodePelangganKarpet.getText().toString();
        String namaPelangganKarpet = etEditNamaPelangganKarpet.getText().toString();
        String nomorHpKarpet = etEditNomorHpKarpet.getText().toString();
        String tanggalMasukKarpet = etEditTanggalMasukKarpet.getText().toString();
        String tanggalKeluarKarpet = etEditTanggalKeluarKarpet.getText().toString();
        String pilihKarpet = spEditKarpet.getSelectedItem().toString();
        String itemKarpet = etEditItemKarpet.getText().toString();
        String hargaKarpet = etEditHargaKarpet.getText().toString();

        // Buat objek data baru
        Map<String, Object> data = new HashMap<>();
        data.put("kodePelangganKarpet", kodePelangganKarpet);
        data.put("namaPelangganKarpet", namaPelangganKarpet);
        data.put("nomorHpKarpet", nomorHpKarpet);
        data.put("tanggalMasukKarpet", tanggalMasukKarpet);
        data.put("tanggalKeluarKarpet", tanggalKeluarKarpet);
        data.put("pilihKarpet", pilihKarpet);
        data.put("itemKarpet", itemKarpet);
        data.put("hargaKarpet", hargaKarpet);

        // Update data pada dokumen yang sudah ada
        db.collection("pesan_karpet").document(id).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                myLoadingDialog.cancel();
                showSuccessDialog();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myLoadingDialog.cancel();
                showErrorDialog();
            }
        });
    }
    // Mencari index item Spinner yang sesuai dengan nama paket
    private int getIndex (Spinner spinner, String namaPilihKarpet) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(namaPilihKarpet)) {
                return i;
            }
        }
        return 0;
    }
    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditPesanKarpetActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(EditPesanKarpetActivity.this).inflate(R.layout.layout_success_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText("Sukses");
        ((TextView) view.findViewById(R.id.textMessage)).setText("Karpet Telah berhasil mengubah pesan!");
        ((Button) view.findViewById(R.id.btnAction)).setText("Ok");
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.success);

        final AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.btnAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                finish();
            }
        });
        if (alertDialog.getWindow()  !=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
    private void showErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditPesanKarpetActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(EditPesanKarpetActivity.this).inflate(R.layout.layout_error_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText("Error");
        ((TextView) view.findViewById(R.id.textMessage)).setText("Oops ada mengalami kesalahan");
        ((Button) view.findViewById(R.id.btnAction)).setText("Coba Lagi");
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.error);

        final AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.btnAction).setOnClickListener(new View.OnClickListener() {
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
}