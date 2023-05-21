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

public class EditPesanEkspressActivity extends AppCompatActivity {
    private EditText etEditKodePelangganEkspress, etEditNamaPelangganEkspress, etEditNomorHpEkspress, etEditTanggalMasukEkspress, etEditTanggalKeluarEkspress, etEditKilogramEkspress, etEditHargaEkspress;
    private Spinner spEditPaketEkspress;
    private int hargaPaketEkspress1 = 20000;
    private int hargaPaketEkspress2 = 15000;
    private int hargaPaketEkspress3 = 12000;
    private Button btnUbahEkspress;
    private FirebaseFirestore db;
    private String id = "";
    private LoadingDialog myLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pesan_ekspress);
        db = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar_edit_pesanEkspress);
        setSupportActionBar(toolbar);

        // Set tombol kembali (back) pada toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        etEditKodePelangganEkspress = findViewById(R.id.et_edit_kodepelanggan_ekspress);
        etEditNamaPelangganEkspress = findViewById(R.id.et_edit_namapelanggan_ekspress);
        etEditNomorHpEkspress = findViewById(R.id.et_edit_nomorhp_ekspress);

        etEditTanggalMasukEkspress = findViewById(R.id.et_edit_tanggalmasuk_ekspress);
        etEditTanggalKeluarEkspress = findViewById(R.id.et_edit_tanggalkeluar_ekspress);
        final Calendar kalender = Calendar.getInstance();
        int tahun = kalender.get(Calendar.YEAR);
        int bulan = kalender.get(Calendar.MONTH);
        int hari = kalender.get(Calendar.DAY_OF_MONTH);

        spEditPaketEkspress = findViewById(R.id.sp_edit_paket_ekspress);
        ArrayAdapter<CharSequence> adapterPesanEkspress = ArrayAdapter.createFromResource(this, R.array.paketEkspress_array, R.layout.spinner_dropdown);
        adapterPesanEkspress.setDropDownViewResource(R.layout.spinner_dropdown);
        spEditPaketEkspress.setAdapter(adapterPesanEkspress);

        etEditKilogramEkspress = findViewById(R.id.et_edit_kilogram_ekspress);
        etEditHargaEkspress = findViewById(R.id.et_edit_harga_ekspress);

        btnUbahEkspress = findViewById(R.id.btn_ubah_ekspress);

        myLoadingDialog = new LoadingDialog(this);

        Intent intent = getIntent();
        if (intent !=null){
            id = intent.getStringExtra("id");
            String kodePelangganEkspress = intent.getStringExtra("kodePelangganEkspress");
            String namaPelangganEkspress = intent.getStringExtra("namaPelangganEkspress");
            String nomorHpEkspress = intent.getStringExtra("nomorHpEkspress");
            String tanggalMasukEkspress = intent.getStringExtra("tanggalMasukEkspress");
            String tanggalKeluarEkspress = intent.getStringExtra("tanggalKeluarEkspress");
            String paketEkspress = intent.getStringExtra("paketEkspress");
            String kilogramEkspress = intent.getStringExtra("kilogramEkspress");
            String hargaEkspress = intent.getStringExtra("hargaEkspress");

            // Set nilai-nilai tersebut pada form input
            etEditKodePelangganEkspress.setText(kodePelangganEkspress);
            etEditNamaPelangganEkspress.setText(namaPelangganEkspress);
            etEditNomorHpEkspress.setText(nomorHpEkspress);
            etEditTanggalMasukEkspress.setText(tanggalMasukEkspress);
            etEditTanggalKeluarEkspress.setText(tanggalKeluarEkspress);
            spEditPaketEkspress.setSelection(getIndex(spEditPaketEkspress, paketEkspress));
            etEditKilogramEkspress.setText(kilogramEkspress);
            etEditHargaEkspress.setText(hargaEkspress);
        }

        spEditPaketEkspress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                // Ambil paket Ekspress yang dipilih dari Spinner
                String selectedPaket = adapterView.getItemAtPosition(position).toString();

                // Set nilai default kilogram dan harga sesuai dengan paket ekspress yang dipilih
                if (selectedPaket.equals("Pilih Paket")){
                    etEditKilogramEkspress.setText("");
                    etEditHargaEkspress.setText("");
                }else if (selectedPaket.equals("Paket 1 (3 Jam)")) {
                    etEditKilogramEkspress.setText("1");
                    etEditHargaEkspress.setText(String.valueOf(hargaPaketEkspress1));
                } else if (selectedPaket.equals("Paket 2 (6 Jam)")) {
                    etEditKilogramEkspress.setText("1");
                    etEditHargaEkspress.setText(String.valueOf(hargaPaketEkspress2));
                } else if (selectedPaket.equals("Paket 3 (9 Jam)")) {
                    etEditKilogramEkspress.setText("1");
                    etEditHargaEkspress.setText(String.valueOf(hargaPaketEkspress3));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //tidak perlu
            }
        });

        etEditTanggalMasukEkspress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(EditPesanEkspressActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int tahun, int bulan, int hari) {
                        // Set tanggal yang dipilih ke EditText
                        etEditTanggalMasukEkspress.setText(hari + "/" + (bulan + 1) + "/" + tahun);
                    }
                }, tahun, bulan, hari);
                dialog.show();
            }
        });

        etEditTanggalKeluarEkspress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(EditPesanEkspressActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int tahun, int bulan, int hari) {
                        // Set tanggal yang dipilih ke EditText
                        etEditTanggalKeluarEkspress.setText(hari + "/" + (bulan + 1) + "/" + tahun);
                    }
                }, tahun, bulan, hari);
                dialog.show();
            }
        });

        // Set listener untuk EditText item
        etEditKilogramEkspress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                //tidak perlu
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Ambil nilai kilogram kiloan dari EditText
                String kilogramEkspressString = charSequence.toString();

                // Ubah nilai kilogram kiloan menjadi integer
                int itemEkspress = 0;
                if (!kilogramEkspressString.isEmpty()){
                    itemEkspress = Integer.parseInt(kilogramEkspressString);
                }

                // Ambil paket kiloan yang dipilih dari Spinner
                String pilihPaket = spEditPaketEkspress.getSelectedItem().toString();

                // Set harga sesuai dengan paket kiloan yang dipilih
                int hargaEkspress = 0;
                if (pilihPaket.equals("Paket 1 (3 Jam)")) {
                    hargaEkspress = hargaPaketEkspress1;
                } else if (pilihPaket.equals("Paket 2 (6 Jam)")) {
                    hargaEkspress = hargaPaketEkspress2;
                } else if (pilihPaket.equals("Paket 3 (9 Jam)")) {
                    hargaEkspress = hargaPaketEkspress3;
                }

                // Hitung harga baru berdasarkan kilogram yang diubah
                int hargaBaruEkspress = itemEkspress * hargaEkspress;

                // Set nilai harga baru pada EditText harga
                etEditHargaEkspress.setText(String.valueOf(hargaBaruEkspress));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //tidak perlu
            }
        });

        btnUbahEkspress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfoEditAlertDialog();
            }
        });
    }
    private void showInfoEditAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditPesanEkspressActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(EditPesanEkspressActivity.this).inflate(R.layout.layout_info_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText("Edit Pesan Ekspress");
        ((TextView) view.findViewById(R.id.textMessage)).setText("Apakah ingin mengubah pesan Ekspress?");
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
    private void updateData(){
        String kodePelangganEkspress = etEditKodePelangganEkspress.getText().toString();
        String namaPelangganEkspress = etEditNamaPelangganEkspress.getText().toString();
        String nomorHpEkspress = etEditNomorHpEkspress.getText().toString();
        String tanggalMasukEkspress = etEditTanggalMasukEkspress.getText().toString();
        String tanggalKeluarEkspress = etEditTanggalKeluarEkspress.getText().toString();
        String paketEkspress = spEditPaketEkspress.getSelectedItem().toString();
        String kilogramEkspress = etEditKilogramEkspress.getText().toString();
        String hargaEkspress = etEditHargaEkspress.getText().toString();

        // Buat objek data baru
        Map<String, Object> data = new HashMap<>();
        data.put("kodePelangganEkspress", kodePelangganEkspress);
        data.put("namaPelangganEkspress", namaPelangganEkspress);
        data.put("nomorHpEkspress", nomorHpEkspress);
        data.put("tanggalMasukEkspress", tanggalMasukEkspress);
        data.put("tanggalKeluarEkspress", tanggalKeluarEkspress);
        data.put("paketEkspress", paketEkspress);
        data.put("kilogramEkspress", kilogramEkspress);
        data.put("hargaEkspress", hargaEkspress);

        // Update data pada dokumen yang sudah ada
        db.collection("pesan_ekspress").document(id).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
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
    private int getIndex(Spinner spinner, String namaPaketEkspress) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(namaPaketEkspress)) {
                return i;
            }
        }
        return 0;
    }
    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditPesanEkspressActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(EditPesanEkspressActivity.this).inflate(R.layout.layout_success_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText("Sukses");
        ((TextView) view.findViewById(R.id.textMessage)).setText("Ekspress Telah berhasil mengubah pesan!");
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
        AlertDialog.Builder builder = new AlertDialog.Builder(EditPesanEkspressActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(EditPesanEkspressActivity.this).inflate(R.layout.layout_error_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer));
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