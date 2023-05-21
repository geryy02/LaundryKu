package com.projectmobile.laundryku;

import static android.content.ContentValues.TAG;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditProfilActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private EditText etProfilNamaLengkap, etProfilNomorHP, etProfilEmail, etProfilPasswordLama, etProfilPasswordBaru;
    private Button btnSimpanProfil;
    private FirebaseUser mUser;
    private FirebaseFirestore mFirestore;
    private LoadingDialog myLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);

        Toolbar toolbar = findViewById(R.id.toolbar_editProfil);
        setSupportActionBar(toolbar);
        // Set tombol kembali (back) pada toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSimpanProfil = findViewById(R.id.btn_simpanprofil);
        etProfilNamaLengkap = findViewById(R.id.et_profilnamaLengkap);
        etProfilNomorHP = findViewById(R.id.et_profilnomorhp);
        etProfilEmail = findViewById(R.id.et_profilemail);
        etProfilPasswordLama = findViewById(R.id.et_profilpasswordlama);
        etProfilPasswordBaru = findViewById(R.id.et_profilpasswordbaru);

        myLoadingDialog = new LoadingDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();

        if (mUser != null) {
            String email = mUser.getEmail();
            String uid = mUser.getUid();

            DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(uid);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        String namaLengkap = documentSnapshot.getString("namaLengkap");
                        String nomorHp = documentSnapshot.getString("nomorHp");
                        if (namaLengkap != null) {
                            // Mengisi nilai nama lengkap pada field yang sesuai
                            etProfilNamaLengkap.setText(namaLengkap);
                        }
                        if (nomorHp != null) {
                            etProfilNomorHP.setText(nomorHp);
                        }
                        etProfilEmail.setText(email);
                    } else {
                        Toast.makeText(EditProfilActivity.this, "Document not exists", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProfilActivity.this, "Error getting document", Toast.LENGTH_SHORT).show();
                }
            });
        }

        btnSimpanProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfoEditProfilDialog();
            }
        });
    }

    private void showInfoEditProfilDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfilActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(EditProfilActivity.this).inflate(R.layout.layout_info_dialog, (ConstraintLayout) findViewById(R.id.layoutDialogContainer));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText("Edit Profil");
        ((TextView) view.findViewById(R.id.textMessage)).setText("Apakah ingin mengubah data profil?");
        ((Button) view.findViewById(R.id.btnYes)).setText("Iya");
        ((Button) view.findViewById(R.id.btnNo)).setText("Tidak");
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.info);

        final AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.btnYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                myLoadingDialog.show();
                String namaLengkap = etProfilNamaLengkap.getText().toString().trim();
                String nomorHp = etProfilNomorHP.getText().toString().trim();
                String email = etProfilEmail.getText().toString().trim();
                String passwordLama = etProfilPasswordLama.getText().toString().trim();
                String passwordBaru = etProfilPasswordBaru.getText().toString().trim();
                perbaruiProfil(namaLengkap, nomorHp, email, passwordLama, passwordBaru);
                finish();
            }
        });
        view.findViewById(R.id.btnNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    private void perbaruiProfil(String namaLengkap, String nomorHp, String email, String passwordLama, String passwordBaru) {
        if (mUser != null){
            String uid = mUser.getUid();
            // update data pada firestore
            DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(uid);
            Map<String, Object> updates = new HashMap<>();
            updates.put("namaLengkap", namaLengkap);
            updates.put("nomorHp", nomorHp);
            updates.put("email", email);
            docRef.update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    myLoadingDialog.cancel();
                    Toast.makeText(EditProfilActivity.this, "Data profil berhasil diupdate", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    myLoadingDialog.cancel();
                    Toast.makeText(EditProfilActivity.this, "Data profil gagal diupdate", Toast.LENGTH_SHORT).show();
                    Log.w(TAG, "Error updating document", e);
                }
            });
            // Update email
            if (!email.equals(mUser.getEmail())) {
                mUser.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditProfilActivity.this, "Email berhasil diupdate", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EditProfilActivity.this, "Gagal update email", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Gagal update email", task.getException());
                        }
                    }
                });
            }
            // Update password
            if (!passwordLama.isEmpty() && !passwordBaru.isEmpty()) {
                mUser.reauthenticate(EmailAuthProvider.getCredential(mUser.getEmail(), passwordLama)).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mUser.updatePassword(passwordBaru).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(EditProfilActivity.this, "password berhasil diupdate", Toast.LENGTH_SHORT).show();
                                        // Update password in Firestore
                                        Map<String, Object> updates = new HashMap<>();
                                        updates.put("password", passwordBaru);
                                        // Update Firestore document
                                        DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(mUser.getUid());
                                        userRef.update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "password berhasil diupdate di Firestore");
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e(TAG, "Gagal update password di Firestore", e);
                                            }
                                        });
                                    } else {
                                        Toast.makeText(EditProfilActivity.this, "Gagal update password", Toast.LENGTH_SHORT).show();
                                        Log.e(TAG, "Gagal update password", task.getException());
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(EditProfilActivity.this, "password lama salah", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }
}