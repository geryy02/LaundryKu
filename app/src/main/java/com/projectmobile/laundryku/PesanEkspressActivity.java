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

public class PesanEkspressActivity extends AppCompatActivity {
    private EditText etKodePelangganEkspress, etNamaPelangganEkspress, etNomorHpEkspress, etTanggalMasukEkspress, etTanggalKeluarEkspress, etKilogramEkspress, etHargaEkspress;
    private Spinner spPaketEkspress;
    private int hargaPaketEkspress1 = 20000;
    private int hargaPaketEkspress2 = 15000;
    private int hargaPaketEkspress3 = 12000;
    private Button btnKonfirmEkspress;
    private FirebaseFirestore db;
    private LoadingDialog myLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan_ekspress);

        db = FirebaseFirestore.getInstance();

        etKodePelangganEkspress = findViewById(R.id.et_kodepelanggan_ekspress);
        etNamaPelangganEkspress = findViewById(R.id.et_namapelanggan_ekspress);
        etNomorHpEkspress = findViewById(R.id.et_nomorhp_ekspress);

        etTanggalMasukEkspress = findViewById(R.id.et_tanggalmasuk_ekspress);
        etTanggalKeluarEkspress = findViewById(R.id.et_tanggalkeluar_ekspress);
        final Calendar kalender = Calendar.getInstance();
        int tahun = kalender.get(Calendar.YEAR);
        int bulan = kalender.get(Calendar.MONTH);
        int hari = kalender.get(Calendar.DAY_OF_MONTH);

        spPaketEkspress = findViewById(R.id.sp_paket_ekspress);
        ArrayAdapter<CharSequence> adapterPesanEkspress = ArrayAdapter.createFromResource(this, R.array.paketEkspress_array, R.layout.spinner_dropdown);
        adapterPesanEkspress.setDropDownViewResource(R.layout.spinner_dropdown);
        spPaketEkspress.setAdapter(adapterPesanEkspress);

        etKilogramEkspress = findViewById(R.id.et_kilogram_ekspress);
        etHargaEkspress = findViewById(R.id.et_harga_ekspress);

        btnKonfirmEkspress = findViewById(R.id.btn_konfirm_ekspress);

        myLoadingDialog = new LoadingDialog(this);

        Toolbar toolbar = findViewById(R.id.toolbar_pesanEkspress);
        setSupportActionBar(toolbar);

        // Set tombol kembali (back) pada toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        spPaketEkspress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                // Ambil paket Ekspress yang dipilih dari Spinner
                String selectedPaket = adapterView.getItemAtPosition(position).toString();

                // Set nilai default kilogram dan harga sesuai dengan paket ekspress yang dipilih
                if (selectedPaket.equals("Pilih Paket")){
                    etKilogramEkspress.setText("");
                    etHargaEkspress.setText("");
                }else if (selectedPaket.equals("Paket 1 (3 Jam)")) {
                    etKilogramEkspress.setText("1");
                    etHargaEkspress.setText(String.valueOf(hargaPaketEkspress1));
                } else if (selectedPaket.equals("Paket 2 (6 Jam)")) {
                    etKilogramEkspress.setText("1");
                    etHargaEkspress.setText(String.valueOf(hargaPaketEkspress2));
                } else if (selectedPaket.equals("Paket 3 (9 Jam)")) {
                    etKilogramEkspress.setText("1");
                    etHargaEkspress.setText(String.valueOf(hargaPaketEkspress3));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //tidak perlu
            }
        });

        etTanggalMasukEkspress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(PesanEkspressActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int tahun, int bulan, int hari) {
                        // Set tanggal yang dipilih ke EditText
                        etTanggalMasukEkspress.setText(hari + "/" + (bulan + 1) + "/" + tahun);
                    }
                }, tahun, bulan, hari);
                dialog.show();
            }
        });

        etTanggalKeluarEkspress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(PesanEkspressActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int tahun, int bulan, int hari) {
                        // Set tanggal yang dipilih ke EditText
                        etTanggalKeluarEkspress.setText(hari + "/" + (bulan + 1) + "/" + tahun);
                    }
                }, tahun, bulan, hari);
                dialog.show();
            }
        });

        // Set listener untuk EditText kilogram
        etKilogramEkspress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                //tidak perlu
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Ambil nilai kilogram kiloan dari EditText
                String kilogramEkspressString = charSequence.toString();

                // Ubah nilai kilogram kiloan menjadi integer
                int kilogramEkspress = 0;
                if (!kilogramEkspressString.isEmpty()){
                    kilogramEkspress = Integer.parseInt(kilogramEkspressString);
                }

                // Ambil paket kiloan yang dipilih dari Spinner
                String pilihPaket = spPaketEkspress.getSelectedItem().toString();

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
                int hargaBaruEkspress = kilogramEkspress * hargaEkspress;

                // Set nilai harga baru pada EditText harga
                etHargaEkspress.setText(String.valueOf(hargaBaruEkspress));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //tidak perlu
            }
        });



        btnKonfirmEkspress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isiFormValid()){
                    showInfoAlertDialog();
                }
            }
        });
    }

    private boolean isiFormValid() {
        String kodePelangganEkspress = etKodePelangganEkspress.getText().toString();
        String namaPelangganEkspress = etNamaPelangganEkspress.getText().toString();
        String nomorHpEkspress = etNomorHpEkspress.getText().toString();
        String tanggalMasukEkspress = etTanggalMasukEkspress.getText().toString();
        String tanggalKeluarEkspress = etTanggalKeluarEkspress.getText().toString();
        String paketEkspress = spPaketEkspress.getSelectedItem().toString();
        String kilogramEkspress = etKilogramEkspress.getText().toString();
        String hargaEkspress = etHargaEkspress.getText().toString();

        if (kodePelangganEkspress.isEmpty() || namaPelangganEkspress.isEmpty() || nomorHpEkspress.isEmpty() || tanggalMasukEkspress.isEmpty() || tanggalKeluarEkspress.isEmpty() || paketEkspress.isEmpty() || kilogramEkspress.isEmpty() || hargaEkspress.isEmpty()){
            showInfoFormValid();
            return false;
        }
        return true;
    }

    private void showInfoFormValid() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PesanEkspressActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(PesanEkspressActivity.this).inflate(R.layout.layout_info_formvalid_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer));
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
        AlertDialog.Builder builder = new AlertDialog.Builder(PesanEkspressActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(PesanEkspressActivity.this).inflate(R.layout.layout_info_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText("Tambah Pesan Ekspress");
        ((TextView) view.findViewById(R.id.textMessage)).setText("Apakah ingin dipesan Ekspress?");
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
        String kodePelangganEkspress = etKodePelangganEkspress.getText().toString();
        String namaPelangganEkspress = etNamaPelangganEkspress.getText().toString();
        String nomorHpEkspress = etNomorHpEkspress.getText().toString();
        String tanggalMasukEkspress = etTanggalMasukEkspress.getText().toString();
        String tanggalKeluarEkspress = etTanggalKeluarEkspress.getText().toString();
        String paketEkspress = spPaketEkspress.getSelectedItem().toString();
        String kilogramEkspress = etKilogramEkspress.getText().toString();
        String hargaEkspress = etHargaEkspress.getText().toString();

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

        // Tambahkan data pada koleksi "pesan_ekspress"
        db.collection("pesan_ekspress").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
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
        AlertDialog.Builder builder = new AlertDialog.Builder(PesanEkspressActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(PesanEkspressActivity.this).inflate(R.layout.layout_success_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText("Sukses");
        ((TextView) view.findViewById(R.id.textMessage)).setText("Ekspress telah berhasil dipesan!");
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
        AlertDialog.Builder builder = new AlertDialog.Builder(PesanEkspressActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(PesanEkspressActivity.this).inflate(R.layout.layout_error_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer));
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