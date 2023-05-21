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

public class EditPesanVipActivity extends AppCompatActivity {
    private EditText etEditKodePelangganVip, etEditNamaPelangganVip, etEditNomorHpVip, etEditTanggalMasukVip, etEditTanggalKeluarVip, etEditItemVip, etEditHargaVip;
    private Spinner spEditVip;
    private int hargaItemVipGaun = 5000;
    private int hargaItemVipJas = 27000;
    private int hargaItemVipSepatu = 30000;
    private int hargaItemVipTas = 25000;
    private Button btnUbahVip;
    private String id = "";
    private FirebaseFirestore db;
    private LoadingDialog myLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pesan_vip);
        db = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar_edit_pesanVip);
        setSupportActionBar(toolbar);

        // Set tombol kembali (back) pada toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        etEditKodePelangganVip = findViewById(R.id.et_edit_kodepelanggan_vip);
        etEditNamaPelangganVip = findViewById(R.id.et_edit_namapelanggan_vip);
        etEditNomorHpVip = findViewById(R.id.et_edit_nomorhp_vip);

        etEditTanggalMasukVip = findViewById(R.id.et_edit_tanggalmasuk_vip);
        etEditTanggalKeluarVip = findViewById(R.id.et_edit_tanggalkeluar_vip);
        final Calendar kalender = Calendar.getInstance();
        int tahun = kalender.get(Calendar.YEAR);
        int bulan = kalender.get(Calendar.MONTH);
        int hari = kalender.get(Calendar.DAY_OF_MONTH);

        spEditVip = findViewById(R.id.sp_edit_vip);
        ArrayAdapter<CharSequence> adapterPesanVip = ArrayAdapter.createFromResource(this, R.array.itemVip_array, R.layout.spinner_dropdown);
        adapterPesanVip.setDropDownViewResource(R.layout.spinner_dropdown);
        spEditVip.setAdapter(adapterPesanVip);

        etEditItemVip = findViewById(R.id.et_edit_item_vip);
        etEditHargaVip = findViewById(R.id.et_edit_harga_vip);

        btnUbahVip = findViewById(R.id.btn_ubah_vip);

        myLoadingDialog = new LoadingDialog(this);

        Intent intent = getIntent();
        if (intent !=null){
            id = intent.getStringExtra("id");
            String kodePelangganVip = intent.getStringExtra("kodePelangganVip");
            String namaPelangganVip = intent.getStringExtra("namaPelangganVip");
            String nomorHpVip = intent.getStringExtra("nomorHpVip");
            String tanggalMasukVip = intent.getStringExtra("tanggalMasukVip");
            String tanggalKeluarVip = intent.getStringExtra("tanggalKeluarVip");
            String pilihVip = intent.getStringExtra("pilihVip");
            String itemVip = intent.getStringExtra("itemVip");
            String hargaVip = intent.getStringExtra("hargaVip");

            // Set nilai-nilai tersebut pada form input
            etEditKodePelangganVip.setText(kodePelangganVip);
            etEditNamaPelangganVip.setText(namaPelangganVip);
            etEditNomorHpVip.setText(nomorHpVip);
            etEditTanggalMasukVip.setText(tanggalMasukVip);
            etEditTanggalKeluarVip.setText(tanggalKeluarVip);
            spEditVip.setSelection(getIndex(spEditVip, pilihVip));
            etEditItemVip.setText(itemVip);
            etEditHargaVip.setText(hargaVip);
        }

        spEditVip.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedItemVip = adapterView.getItemAtPosition(position).toString();

                // Set nilai default kilogram dan harga sesuai dengan paket kiloan yang dipilih
                if (selectedItemVip.equals("Pilih Item")){
                    etEditItemVip.setText("");
                    etEditHargaVip.setText("");
                }else if (selectedItemVip.equals("Gaun")) {
                    etEditItemVip.setText("1");
                    etEditHargaVip.setText(String.valueOf(hargaItemVipGaun));
                } else if (selectedItemVip.equals("Jas")) {
                    etEditItemVip.setText("1");
                    etEditHargaVip.setText(String.valueOf(hargaItemVipJas));
                } else if (selectedItemVip.equals("Sepatu")) {
                    etEditItemVip.setText("1");
                    etEditHargaVip.setText(String.valueOf(hargaItemVipSepatu));
                } else if (selectedItemVip.equals("Tas")) {
                    etEditItemVip.setText("1");
                    etEditHargaVip.setText(String.valueOf(hargaItemVipTas));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        etEditTanggalMasukVip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(EditPesanVipActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int tahun, int bulan, int hari) {
                        // Set tanggal yang dipilih ke EditText
                        etEditTanggalMasukVip.setText(hari + "/" + (bulan + 1) + "/" + tahun);
                    }
                }, tahun, bulan, hari);
                dialog.show();
            }
        });

        etEditTanggalKeluarVip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(EditPesanVipActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int tahun, int bulan, int hari) {
                        // Set tanggal yang dipilih ke EditText
                        etEditTanggalKeluarVip.setText(hari + "/" + (bulan + 1) + "/" + tahun);
                    }
                }, tahun, bulan, hari);
                dialog.show();
            }
        });

        // Set listener untuk EditText item
        etEditItemVip.addTextChangedListener(new TextWatcher() {
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
                String pilihItem = spEditVip.getSelectedItem().toString();

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
                etEditHargaVip.setText(String.valueOf(hargaBaruVip));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //tidak perlu
            }
        });

        btnUbahVip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfoEditAlertDialog();
            }
        });
    }
    private void showInfoEditAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditPesanVipActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(EditPesanVipActivity.this).inflate(R.layout.layout_info_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText("Edit Pesan VIP");
        ((TextView) view.findViewById(R.id.textMessage)).setText("Apakah ingin mengubah pesan VIP?");
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
        String kodePelangganVip = etEditKodePelangganVip.getText().toString();
        String namaPelangganVip = etEditNamaPelangganVip.getText().toString();
        String nomorHpVip = etEditNomorHpVip.getText().toString();
        String tanggalMasukVip = etEditTanggalMasukVip.getText().toString();
        String tanggalKeluarVip = etEditTanggalKeluarVip.getText().toString();
        String pilihVip = spEditVip.getSelectedItem().toString();
        String itemVip = etEditItemVip.getText().toString();
        String hargaVip = etEditHargaVip.getText().toString();

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

        if (id!=null){
            // Update data pada dokumen yang sudah ada
            db.collection("pesan_vip").document(id).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
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
    }
    // Mencari index item Spinner yang sesuai dengan nama paket
    private int getIndex (Spinner spinner, String namaPilihVip) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(namaPilihVip)) {
                return i;
            }
        }
        return 0;
    }
    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditPesanVipActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(EditPesanVipActivity.this).inflate(R.layout.layout_success_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText("Sukses");
        ((TextView) view.findViewById(R.id.textMessage)).setText("VIP Telah berhasil mengubah pesan!");
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
        AlertDialog.Builder builder = new AlertDialog.Builder(EditPesanVipActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(EditPesanVipActivity.this).inflate(R.layout.layout_error_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer));
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