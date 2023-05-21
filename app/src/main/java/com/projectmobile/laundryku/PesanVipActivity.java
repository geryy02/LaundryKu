package com.projectmobile.laundryku;

import android.annotation.SuppressLint;
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

public class PesanVipActivity extends AppCompatActivity {
    private EditText etKodePelangganVip, etNamaPelangganVip, etNomorHpVip, etTanggalMasukVip, etTanggalKeluarVip, etItemVip, etHargaVip;
    private Spinner spVip;
    private int hargaItemVipGaun = 5000;
    private int hargaItemVipJas = 27000;
    private int hargaItemVipSepatu = 30000;
    private int hargaItemVipTas = 25000;
    private Button btnKonfirmVip;
    private FirebaseFirestore db;
    private LoadingDialog myLoadingDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan_vip);
        db = FirebaseFirestore.getInstance();

        etKodePelangganVip = findViewById(R.id.et_kodepelanggan_vip);
        etNamaPelangganVip = findViewById(R.id.et_namapelanggan_vip);
        etNomorHpVip = findViewById(R.id.et_nomorhp_vip);

        etTanggalMasukVip = findViewById(R.id.et_tanggalmasuk_vip);
        etTanggalKeluarVip = findViewById(R.id.et_tanggalkeluar_vip);
        final Calendar kalender = Calendar.getInstance();
        int tahun = kalender.get(Calendar.YEAR);
        int bulan = kalender.get(Calendar.MONTH);
        int hari = kalender.get(Calendar.DAY_OF_MONTH);

        spVip = findViewById(R.id.sp_vip);
        ArrayAdapter<CharSequence> adapterPesanVip = ArrayAdapter.createFromResource(this, R.array.itemVip_array, R.layout.spinner_dropdown);
        adapterPesanVip.setDropDownViewResource(R.layout.spinner_dropdown);
        spVip.setAdapter(adapterPesanVip);

        etItemVip = findViewById(R.id.et_item_vip);
        etHargaVip = findViewById(R.id.et_harga_vip);

        btnKonfirmVip = findViewById(R.id.btn_konfirm_vip);

        myLoadingDialog = new LoadingDialog(this);

        Toolbar toolbar = findViewById(R.id.toolbar_pesanVip);
        setSupportActionBar(toolbar);

        // Set tombol kembali (back) pada toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        spVip.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedItemVip = adapterView.getItemAtPosition(position).toString();

                // Set nilai default kilogram dan harga sesuai dengan paket kiloan yang dipilih
                if (selectedItemVip.equals("Pilih Item")){
                    etItemVip.setText("");
                    etHargaVip.setText("");
                }else if (selectedItemVip.equals("Gaun")) {
                    etItemVip.setText("1");
                    etHargaVip.setText(String.valueOf(hargaItemVipGaun));
                } else if (selectedItemVip.equals("Jas")) {
                    etItemVip.setText("1");
                    etHargaVip.setText(String.valueOf(hargaItemVipJas));
                } else if (selectedItemVip.equals("Sepatu")) {
                    etItemVip.setText("1");
                    etHargaVip.setText(String.valueOf(hargaItemVipSepatu));
                } else if (selectedItemVip.equals("Tas")) {
                    etItemVip.setText("1");
                    etHargaVip.setText(String.valueOf(hargaItemVipTas));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        etTanggalMasukVip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(PesanVipActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int tahun, int bulan, int hari) {
                        // Set tanggal yang dipilih ke EditText
                        etTanggalMasukVip.setText(hari + "/" + (bulan + 1) + "/" + tahun);
                    }
                }, tahun, bulan, hari);
                dialog.show();
            }
        });

        etTanggalKeluarVip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(PesanVipActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int tahun, int bulan, int hari) {
                        // Set tanggal yang dipilih ke EditText
                        etTanggalKeluarVip.setText(hari + "/" + (bulan + 1) + "/" + tahun);
                    }
                }, tahun, bulan, hari);
                dialog.show();
            }
        });

        // Set listener untuk EditText item
        etItemVip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                //tidak perlu
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Ambil nilai kilogram kiloan dari EditText
                String itemVipString = charSequence.toString();

                // Ubah nilai kilogram kiloan menjadi integer
                int itemVip = 0;
                if (!itemVipString.isEmpty()){
                    itemVip = Integer.parseInt(itemVipString);
                }

                // Ambil paket kiloan yang dipilih dari Spinner
                String pilihItem = spVip.getSelectedItem().toString();

                // Set harga sesuai dengan paket kiloan yang dipilih
                int hargaVip = 0;
                if (pilihItem.equals("Gaun")) {
                    hargaVip = hargaItemVipGaun;
                } else if (pilihItem.equals("Jas")) {
                    hargaVip = hargaItemVipJas;
                } else if (pilihItem.equals("Sepatu")) {
                    hargaVip = hargaItemVipSepatu;
                } else if (pilihItem.equals("Tas")) {
                    hargaVip = hargaItemVipTas;
                }

                // Hitung harga baru berdasarkan kilogram yang diubah
                int hargaBaruVip = itemVip * hargaVip;

                // Set nilai harga baru pada EditText harga
                etHargaVip.setText(String.valueOf(hargaBaruVip));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //tidak perlu
            }
        });

        btnKonfirmVip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isiFormValid()){
                    showInfoAlertDialog();
                }
            }
        });
    }
    private boolean isiFormValid() {
        String kodePelangganVip = etKodePelangganVip.getText().toString();
        String namaPelangganVip = etNamaPelangganVip.getText().toString();
        String nomorHpVip = etNomorHpVip.getText().toString();
        String tanggalMasukVip = etTanggalMasukVip.getText().toString();
        String tanggalKeluarVip = etTanggalKeluarVip.getText().toString();
        String pilihVip = spVip.getSelectedItem().toString();
        String itemVip = etItemVip.getText().toString();
        String hargaVip = etHargaVip.getText().toString();
        if (kodePelangganVip.isEmpty() || namaPelangganVip.isEmpty() || nomorHpVip.isEmpty() || tanggalMasukVip.isEmpty() || tanggalKeluarVip.isEmpty() || pilihVip.isEmpty() || itemVip.isEmpty() || hargaVip.isEmpty()) {
            showInfoFormValid();
            return false;
        }
        return true;
    }

    private void showInfoFormValid() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PesanVipActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(PesanVipActivity.this).inflate(R.layout.layout_info_formvalid_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer));
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
        AlertDialog.Builder builder = new AlertDialog.Builder(PesanVipActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(PesanVipActivity.this).inflate(R.layout.layout_info_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText("Tambah Pesan Vip");
        ((TextView) view.findViewById(R.id.textMessage)).setText("Apakah ingin dipesan VIP?");
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
        String kodePelangganVip = etKodePelangganVip.getText().toString();
        String namaPelangganVip = etNamaPelangganVip.getText().toString();
        String nomorHpVip = etNomorHpVip.getText().toString();
        String tanggalMasukVip = etTanggalMasukVip.getText().toString();
        String tanggalKeluarVip = etTanggalKeluarVip.getText().toString();
        String pilihVip = spVip.getSelectedItem().toString();
        String itemVip = etItemVip.getText().toString();
        String hargaVip = etHargaVip.getText().toString();

        // Buat objek data baru
        Map<String, Object> data = new HashMap<>();
        data.put("kodePelangganVip", kodePelangganVip);
        data.put("namaPelangganVip", namaPelangganVip);
        data.put("nomorHpVip", nomorHpVip);
        data.put("tanggalMasukVip", tanggalMasukVip);
        data.put("tanggalKeluarVip", tanggalKeluarVip);
        data.put("pilihVip", pilihVip);
        data.put("itemVip", itemVip);
        data.put("hargaVip", hargaVip);

        // Jika documentId null, berarti sedang dalam mode tambah data baru
        // Tambahkan data pada koleksi "kiloan"
        db.collection("pesan_vip").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
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
        AlertDialog.Builder builder = new AlertDialog.Builder(PesanVipActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(PesanVipActivity.this).inflate(R.layout.layout_success_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText("Sukses");
        ((TextView) view.findViewById(R.id.textMessage)).setText("VIP telah berhasil dipesan!");
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
        AlertDialog.Builder builder = new AlertDialog.Builder(PesanVipActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(PesanVipActivity.this).inflate(R.layout.layout_error_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer));
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