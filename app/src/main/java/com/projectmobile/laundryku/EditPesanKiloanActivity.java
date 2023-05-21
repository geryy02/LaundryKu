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

public class EditPesanKiloanActivity extends AppCompatActivity {
    private EditText etEditKodePelangganKiloan, etEditNamaPelangganKiloan, etEditNomorHpKiloan, etEditTanggalMasukKiloan, etEditTanggalKeluarKiloan, etEditKilogramKiloan, etEditHargaKiloan;
    private Spinner spEditPaketKiloan;
    private int hargaPaketKiloan1 = 4000;
    private int hargaPaketKiloan2 = 6000;
    private int hargaPaketKiloan3 = 8000;
    private Button btnUbahKiloan;
    private String id = "";
    private FirebaseFirestore db;
    private LoadingDialog myLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pesan_kiloan);
        db = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar_edit_pesanKiloan);
        setSupportActionBar(toolbar);

        // Set tombol kembali (back) pada toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        etEditKodePelangganKiloan = findViewById(R.id.et_edit_kodepelanggan_kiloan);
        etEditNamaPelangganKiloan = findViewById(R.id.et_edit_namapelanggan_kiloan);
        etEditNomorHpKiloan = findViewById(R.id.et_edit_nomorhp_kiloan);

        etEditTanggalMasukKiloan = findViewById(R.id.et_edit_tanggalmasuk_kiloan);
        etEditTanggalKeluarKiloan = findViewById(R.id.et_edit_tanggalkeluar_kiloan);
        final Calendar kalender = Calendar.getInstance();
        int tahun = kalender.get(Calendar.YEAR);
        int bulan = kalender.get(Calendar.MONTH);
        int hari = kalender.get(Calendar.DAY_OF_MONTH);

        spEditPaketKiloan = findViewById(R.id.sp_edit_paket_kiloan);
        ArrayAdapter<CharSequence> adapterPesanKiloan = ArrayAdapter.createFromResource(this, R.array.paketkiloan_array, R.layout.spinner_dropdown);
        adapterPesanKiloan.setDropDownViewResource(R.layout.spinner_dropdown);
        spEditPaketKiloan.setAdapter(adapterPesanKiloan);

        etEditKilogramKiloan = findViewById(R.id.et_edit_kilogram_kiloan);
        etEditHargaKiloan = findViewById(R.id.et_edit_harga_kiloan);

        btnUbahKiloan = findViewById(R.id.btn_ubah_kiloan);

        myLoadingDialog = new LoadingDialog(this);

        Intent intent = getIntent();
        if (intent !=null){
            id = intent.getStringExtra("id");
            String kodePelangganKiloan = intent.getStringExtra("kodePelangganKiloan");
            String namaPelangganKiloan = intent.getStringExtra("namaPelangganKiloan");
            String nomorHpKiloan = intent.getStringExtra("nomorHpKiloan");
            String tanggalMasukKiloan = intent.getStringExtra("tanggalMasukKiloan");
            String tanggalKeluarKiloan = intent.getStringExtra("tanggalKeluarKiloan");
            String paketKiloan = intent.getStringExtra("paketKiloan");
            String kilogramKiloan = intent.getStringExtra("kilogramKiloan");
            String hargaKiloan = intent.getStringExtra("hargaKiloan");

            // Set nilai-nilai tersebut pada form input
            etEditKodePelangganKiloan.setText(kodePelangganKiloan);
            etEditNamaPelangganKiloan.setText(namaPelangganKiloan);
            etEditNomorHpKiloan.setText(nomorHpKiloan);
            etEditTanggalMasukKiloan.setText(tanggalMasukKiloan);
            etEditTanggalKeluarKiloan.setText(tanggalKeluarKiloan);
            spEditPaketKiloan.setSelection(getIndex(spEditPaketKiloan, paketKiloan));
            etEditKilogramKiloan.setText(kilogramKiloan);
            etEditHargaKiloan.setText(hargaKiloan);
        }

        spEditPaketKiloan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                // Ambil paket kiloan yang dipilih dari Spinner
                String selectedPaket = adapterView.getItemAtPosition(position).toString();

                // Set nilai default kilogram dan harga sesuai dengan paket kiloan yang dipilih
                if (selectedPaket.equals("Pilih Paket")){
                    etEditKilogramKiloan.setText("");
                    etEditHargaKiloan.setText("");
                }else if (selectedPaket.equals("Paket 1")) {
                    etEditKilogramKiloan.setText("1");
                    etEditHargaKiloan.setText(String.valueOf(hargaPaketKiloan1));
                } else if (selectedPaket.equals("Paket 2")) {
                    etEditKilogramKiloan.setText("1");
                    etEditHargaKiloan.setText(String.valueOf(hargaPaketKiloan2));
                } else if (selectedPaket.equals("Paket 3")) {
                    etEditKilogramKiloan.setText("1");
                    etEditHargaKiloan.setText(String.valueOf(hargaPaketKiloan3));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //tidak perlu
            }
        });

        etEditTanggalMasukKiloan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(EditPesanKiloanActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int tahun, int bulan, int hari) {
                        // Set tanggal yang dipilih ke EditText
                        etEditTanggalMasukKiloan.setText(hari + "/" + (bulan + 1) + "/" + tahun);
                    }
                }, tahun, bulan, hari);
                dialog.show();
            }
        });

        etEditTanggalKeluarKiloan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(EditPesanKiloanActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int tahun, int bulan, int hari) {
                        // Set tanggal yang dipilih ke EditText
                        etEditTanggalKeluarKiloan.setText(hari + "/" + (bulan + 1) + "/" + tahun);
                    }
                }, tahun, bulan, hari);
                dialog.show();
            }
        });

        // Set listener untuk EditText kilogram
        etEditKilogramKiloan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                //tidak perlu
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Ambil nilai kilogram kiloan dari EditText
                String kilogramKiloanString = charSequence.toString();

                // Ubah nilai kilogram kiloan menjadi integer
                int kilogramKiloan = 0;
                if (!kilogramKiloanString.isEmpty()){
                    kilogramKiloan = Integer.parseInt(kilogramKiloanString);
                }

                // Ambil paket kiloan yang dipilih dari Spinner
                String pilihPaket = spEditPaketKiloan.getSelectedItem().toString();

                // Set harga sesuai dengan paket kiloan yang dipilih
                int hargaKiloan = 0;
                if (pilihPaket.equals("Paket 1")) {
                    hargaKiloan = hargaPaketKiloan1;
                } else if (pilihPaket.equals("Paket 2")) {
                    hargaKiloan = hargaPaketKiloan2;
                } else if (pilihPaket.equals("Paket 3")) {
                    hargaKiloan = hargaPaketKiloan3;
                }

                // Hitung harga baru berdasarkan kilogram yang diubah
                int hargaBaruKiloan = kilogramKiloan * hargaKiloan;

                // Set nilai harga baru pada EditText harga
                etEditHargaKiloan.setText(String.valueOf(hargaBaruKiloan));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //tidak perlu
            }
        });

        btnUbahKiloan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfoEditAlertDialog();
            }
        });
    }
    private void showInfoEditAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditPesanKiloanActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(EditPesanKiloanActivity.this).inflate(R.layout.layout_info_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText("Edit Pesan Kiloan");
        ((TextView) view.findViewById(R.id.textMessage)).setText("Apakah ingin mengubah pesan Kiloan?");
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
        String kodePelangganKiloan = etEditKodePelangganKiloan.getText().toString();
        String namaPelangganKiloan = etEditNamaPelangganKiloan.getText().toString();
        String nomorHpKiloan = etEditNomorHpKiloan.getText().toString();
        String tanggalMasukKiloan = etEditTanggalMasukKiloan.getText().toString();
        String tanggalKeluarKiloan = etEditTanggalKeluarKiloan.getText().toString();
        String paketKiloan = spEditPaketKiloan.getSelectedItem().toString();
        String kilogramKiloan = etEditKilogramKiloan.getText().toString();
        String hargaKiloan = etEditHargaKiloan.getText().toString();

        // Buat objek data baru
        Map<String, Object> data = new HashMap<>();
        data.put("kodePelangganKiloan", kodePelangganKiloan);
        data.put("namaPelangganKiloan", namaPelangganKiloan);
        data.put("nomorHpKiloan", nomorHpKiloan);
        data.put("tanggalMasukKiloan", tanggalMasukKiloan);
        data.put("tanggalKeluarKiloan", tanggalKeluarKiloan);
        data.put("paketKiloan", paketKiloan);
        data.put("kilogramKiloan", kilogramKiloan);
        data.put("hargaKiloan", hargaKiloan);

        // Update data pada dokumen yang sudah ada
        db.collection("pesan_kiloan").document(id).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
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
    private int getIndex(Spinner spinner, String namaPaketKiloan) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(namaPaketKiloan)) {
                return i;
            }
        }
        return 0;
    }
    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditPesanKiloanActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(EditPesanKiloanActivity.this).inflate(R.layout.layout_success_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText("Sukses");
        ((TextView) view.findViewById(R.id.textMessage)).setText("Kiloan telah berhasil mengubah pesan!");
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
        AlertDialog.Builder builder = new AlertDialog.Builder(EditPesanKiloanActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(EditPesanKiloanActivity.this).inflate(R.layout.layout_error_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer));
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