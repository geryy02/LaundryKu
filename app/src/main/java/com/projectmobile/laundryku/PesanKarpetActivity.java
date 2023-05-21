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
import android.widget.Toast;

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

public class PesanKarpetActivity extends AppCompatActivity {
    private EditText etKodePelangganKarpet, etNamaPelangganKarpet, etNomorHpKarpet, etTanggalMasukKarpet, etTanggalKeluarKarpet, etItemKarpet, etHargaKarpet;
    private Spinner spKarpet;
    private int hargaItemKarpet1 = 15000;
    private int hargaItemKarpet2 = 25000;
    private int hargaItemKarpet3 = 30000;
    private Button btnKonfirmKarpet;
    private FirebaseFirestore db;
    private LoadingDialog myLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan_karpet);
        db = FirebaseFirestore.getInstance();

        etKodePelangganKarpet = findViewById(R.id.et_kodepelanggan_karpet);
        etNamaPelangganKarpet = findViewById(R.id.et_namapelanggan_karpet);
        etNomorHpKarpet = findViewById(R.id.et_nomorhp_karpet);

        etTanggalMasukKarpet = findViewById(R.id.et_tanggalmasuk_karpet);
        etTanggalKeluarKarpet = findViewById(R.id.et_tanggalkeluar_karpet);
        final Calendar kalender = Calendar.getInstance();
        int tahun = kalender.get(Calendar.YEAR);
        int bulan = kalender.get(Calendar.MONTH);
        int hari = kalender.get(Calendar.DAY_OF_MONTH);

        spKarpet = findViewById(R.id.sp_karpet);
        ArrayAdapter<CharSequence> adapterPesanKarpet = ArrayAdapter.createFromResource(this, R.array.karpet_array, R.layout.spinner_dropdown);
        adapterPesanKarpet.setDropDownViewResource(R.layout.spinner_dropdown);
        spKarpet.setAdapter(adapterPesanKarpet);

        etItemKarpet = findViewById(R.id.et_item_karpet);
        etHargaKarpet = findViewById(R.id.et_harga_karpet);

        btnKonfirmKarpet = findViewById(R.id.btn_konfirm_karpet);

        myLoadingDialog = new LoadingDialog(this);

        Toolbar toolbar = findViewById(R.id.toolbar_pesanKarpet);
        setSupportActionBar(toolbar);

        // Set tombol kembali (back) pada toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        spKarpet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedItem = adapterView.getItemAtPosition(position).toString();

                // Set nilai default kilogram dan harga sesuai dengan paket kiloan yang dipilih
                if (selectedItem.equals("Pilih Item")){
                    etItemKarpet.setText("");
                    etHargaKarpet.setText("");
                }else if (selectedItem.equals("Karpet Tipis")) {
                    etItemKarpet.setText("1");
                    etHargaKarpet.setText(String.valueOf(hargaItemKarpet1));
                } else if (selectedItem.equals("Karpet Sedang")) {
                    etItemKarpet.setText("1");
                    etHargaKarpet.setText(String.valueOf(hargaItemKarpet2));
                } else if (selectedItem.equals("Karpet Super")) {
                    etItemKarpet.setText("1");
                    etHargaKarpet.setText(String.valueOf(hargaItemKarpet3));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        etTanggalMasukKarpet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(PesanKarpetActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int tahun, int bulan, int hari) {
                        // Set tanggal yang dipilih ke EditText
                        etTanggalMasukKarpet.setText(hari + "/" + (bulan + 1) + "/" + tahun);
                    }
                }, tahun, bulan, hari);
                dialog.show();
            }
        });

        etTanggalKeluarKarpet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(PesanKarpetActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int tahun, int bulan, int hari) {
                        // Set tanggal yang dipilih ke EditText
                        etTanggalKeluarKarpet.setText(hari + "/" + (bulan + 1) + "/" + tahun);
                    }
                }, tahun, bulan, hari);
                dialog.show();
            }
        });

        // Set listener untuk EditText item
        etItemKarpet.addTextChangedListener(new TextWatcher() {
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
                String pilihItem = spKarpet.getSelectedItem().toString();

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
                etHargaKarpet.setText(String.valueOf(hargaBaruKarpet));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //tidak perlu
            }
        });

        btnKonfirmKarpet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isiFormValid()){
                    showInfoAlertDialog();
                }
            }
        });
    }
    private boolean isiFormValid() {
        String kodePelangganKarpet = etKodePelangganKarpet.getText().toString();
        String namaPelangganKarpet = etNamaPelangganKarpet.getText().toString();
        String nomorHpKarpet = etNomorHpKarpet.getText().toString();
        String tanggalMasukKarpet = etTanggalMasukKarpet.getText().toString();
        String tanggalKeluarKarpet = etTanggalKeluarKarpet.getText().toString();
        String pilihKarpet = spKarpet.getSelectedItem().toString();
        String itemKarpet = etItemKarpet.getText().toString();
        String hargaKarpet = etHargaKarpet.getText().toString();

        if (kodePelangganKarpet.isEmpty() || namaPelangganKarpet.isEmpty() || nomorHpKarpet.isEmpty() || tanggalMasukKarpet.isEmpty() || tanggalKeluarKarpet.isEmpty() || pilihKarpet.isEmpty() || itemKarpet.isEmpty() || hargaKarpet.isEmpty()){
            showInfoFormValid();
            return false;
        }
        return true;
    }

    private void showInfoFormValid() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PesanKarpetActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(PesanKarpetActivity.this).inflate(R.layout.layout_info_formvalid_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer));
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
        AlertDialog.Builder builder = new AlertDialog.Builder(PesanKarpetActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(PesanKarpetActivity.this).inflate(R.layout.layout_info_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText("Tambah Pesan Karpet");
        ((TextView) view.findViewById(R.id.textMessage)).setText("Apakah ingin dipesan Karpet?");
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
        String kodePelangganKarpet = etKodePelangganKarpet.getText().toString();
        String namaPelangganKarpet = etNamaPelangganKarpet.getText().toString();
        String nomorHpKarpet = etNomorHpKarpet.getText().toString();
        String tanggalMasukKarpet = etTanggalMasukKarpet.getText().toString();
        String tanggalKeluarKarpet = etTanggalKeluarKarpet.getText().toString();
        String pilihKarpet = spKarpet.getSelectedItem().toString();
        String itemKarpet = etItemKarpet.getText().toString();
        String hargaKarpet = etHargaKarpet.getText().toString();

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

        // Tambahkan data pada koleksi "pesan_karpet"
        db.collection("pesan_karpet").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                myLoadingDialog.cancel();
                showSuccessDialog();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myLoadingDialog.cancel();
                Toast.makeText(getApplicationContext(), "Karpet telah gagal dipesan! : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                showErrorDialog();
            }
        });
    }

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PesanKarpetActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(PesanKarpetActivity.this).inflate(R.layout.layout_success_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText("Sukses");
        ((TextView) view.findViewById(R.id.textMessage)).setText("Karpet telah berhasil dipesan!");
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
        AlertDialog.Builder builder = new AlertDialog.Builder(PesanKarpetActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(PesanKarpetActivity.this).inflate(R.layout.layout_error_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer));
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