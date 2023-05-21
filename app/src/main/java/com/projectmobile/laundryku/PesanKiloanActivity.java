package com.projectmobile.laundryku;

import android.app.DatePickerDialog;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PesanKiloanActivity extends AppCompatActivity {
    private EditText etKodePelangganKiloan, etNamaPelangganKiloan, etNomorHpKiloan, etTanggalMasukKiloan, etTanggalKeluarKiloan, etKilogramKiloan, etHargaKiloan;
    private Spinner spPaketKiloan;
    private int hargaPaketKiloan1 = 4000;
    private int hargaPaketKiloan2 = 6000;
    private int hargaPaketKiloan3 = 8000;
    private Button btnKonfirmKiloan;
    private FirebaseFirestore db;
    private LoadingDialog myLoadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan_kiloan);

        db = FirebaseFirestore.getInstance();

        etKodePelangganKiloan = findViewById(R.id.et_kodepelanggan_kiloan);
        etNamaPelangganKiloan = findViewById(R.id.et_namapelanggan_kiloan);
        etNomorHpKiloan = findViewById(R.id.et_nomorhp_kiloan);

        etTanggalMasukKiloan = findViewById(R.id.et_tanggalmasuk_kiloan);
        etTanggalKeluarKiloan = findViewById(R.id.et_tanggalkeluar_kiloan);
        final Calendar kalender = Calendar.getInstance();
        int tahun = kalender.get(Calendar.YEAR);
        int bulan = kalender.get(Calendar.MONTH);
        int hari = kalender.get(Calendar.DAY_OF_MONTH);

        spPaketKiloan = findViewById(R.id.sp_paket_kiloan);
        ArrayAdapter<CharSequence> adapterPesanKiloan = ArrayAdapter.createFromResource(this, R.array.paketkiloan_array, R.layout.spinner_dropdown);
        adapterPesanKiloan.setDropDownViewResource(R.layout.spinner_dropdown);
        spPaketKiloan.setAdapter(adapterPesanKiloan);

        etKilogramKiloan = findViewById(R.id.et_kilogram_kiloan);
        etHargaKiloan = findViewById(R.id.et_harga_kiloan);

        btnKonfirmKiloan = findViewById(R.id.btn_konfirm_kiloan);

        myLoadingDialog = new LoadingDialog(this);
        Toolbar toolbar = findViewById(R.id.toolbar_pesanKiloan);
        setSupportActionBar(toolbar);

        // Set tombol kembali (back) pada toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        spPaketKiloan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                // Ambil paket kiloan yang dipilih dari Spinner
                String selectedPaket = adapterView.getItemAtPosition(position).toString();

                // Set nilai default kilogram dan harga sesuai dengan paket kiloan yang dipilih
                if (selectedPaket.equals("Pilih Paket")){
                    etKilogramKiloan.setText("");
                    etHargaKiloan.setText("");
                }else if (selectedPaket.equals("Paket 1")) {
                    etKilogramKiloan.setText("1");
                    etHargaKiloan.setText(String.valueOf(hargaPaketKiloan1));
                } else if (selectedPaket.equals("Paket 2")) {
                    etKilogramKiloan.setText("1");
                    etHargaKiloan.setText(String.valueOf(hargaPaketKiloan2));
                } else if (selectedPaket.equals("Paket 3")) {
                    etKilogramKiloan.setText("1");
                    etHargaKiloan.setText(String.valueOf(hargaPaketKiloan3));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //tidak perlu
            }
        });

        etTanggalMasukKiloan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(PesanKiloanActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int tahun, int bulan, int hari) {
                        // Set tanggal yang dipilih ke EditText
                        etTanggalMasukKiloan.setText(hari + "/" + (bulan + 1) + "/" + tahun);
                    }
                }, tahun, bulan, hari);
                dialog.show();
            }
        });

        etTanggalKeluarKiloan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(PesanKiloanActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int tahun, int bulan, int hari) {
                        // Set tanggal yang dipilih ke EditText
                        etTanggalKeluarKiloan.setText(hari + "/" + (bulan + 1) + "/" + tahun);
                    }
                }, tahun, bulan, hari);
                dialog.show();
            }
        });

        // Set listener untuk EditText kilogram
        etKilogramKiloan.addTextChangedListener(new TextWatcher() {
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
                String pilihPaket = spPaketKiloan.getSelectedItem().toString();

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
                etHargaKiloan.setText(String.valueOf(hargaBaruKiloan));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //tidak perlu
            }
        });

        btnKonfirmKiloan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isiFormValid()){
                    showInfoAlertDialog();
                }
            }
        });
    }
    private boolean isiFormValid() {
        String kodePelangganKiloan = etKodePelangganKiloan.getText().toString();
        String namaPelangganKiloan = etNamaPelangganKiloan.getText().toString();
        String nomorHpKiloan = etNomorHpKiloan.getText().toString();
        String tanggalMasukKiloan = etTanggalMasukKiloan.getText().toString();
        String tanggalKeluarKiloan = etTanggalKeluarKiloan.getText().toString();
        String kilogramKiloan = etKilogramKiloan.getText().toString();
        String hargaKiloan = etHargaKiloan.getText().toString();
        if (kodePelangganKiloan.isEmpty() || namaPelangganKiloan.isEmpty() || nomorHpKiloan.isEmpty() || tanggalMasukKiloan.isEmpty() || tanggalKeluarKiloan.isEmpty() || kilogramKiloan.isEmpty() || hargaKiloan.isEmpty()) {
            showInfoFormValid();
            return false;
        }
        return true;
    }

    private void showInfoFormValid() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PesanKiloanActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(PesanKiloanActivity.this).inflate(R.layout.layout_info_formvalid_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText("Form Valid");
        ((TextView) view.findViewById(R.id.textMessage)).setText("Harap isi semua field yang diperlukan!");
        ((Button) view.findViewById(R.id.btnAction)).setText("Ok");
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.info);

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

    private void showInfoAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PesanKiloanActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(PesanKiloanActivity.this).inflate(R.layout.layout_info_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText("Tambah Pesan Kiloan");
        ((TextView) view.findViewById(R.id.textMessage)).setText("Apakah ingin dipesan Kiloan?");
        ((Button) view.findViewById(R.id.btnYes)).setText("Iya");
        ((Button) view.findViewById(R.id.btnNo)).setText("Tidak");
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.info);

        final AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.btnYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                myLoadingDialog.show();
                simpanData();
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
    private void simpanData() {
        String kodePelangganKiloan = etKodePelangganKiloan.getText().toString();
        String namaPelangganKiloan = etNamaPelangganKiloan.getText().toString();
        String nomorHpKiloan = etNomorHpKiloan.getText().toString();
        String tanggalMasukKiloan = etTanggalMasukKiloan.getText().toString();
        String tanggalKeluarKiloan = etTanggalKeluarKiloan.getText().toString();
        String paketKiloan = spPaketKiloan.getSelectedItem().toString();
        String kilogramKiloan = etKilogramKiloan.getText().toString();
        String hargaKiloan = etHargaKiloan.getText().toString();

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

        // Tambahkan data pada koleksi "pesan_kiloan"
        db.collection("pesan_kiloan").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
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

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PesanKiloanActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(PesanKiloanActivity.this).inflate(R.layout.layout_success_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText("Sukses");
        ((TextView) view.findViewById(R.id.textMessage)).setText("Kiloan Telah berhasil dipesan!");
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
        AlertDialog.Builder builder = new AlertDialog.Builder(PesanKiloanActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(PesanKiloanActivity.this).inflate(R.layout.layout_error_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer));
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