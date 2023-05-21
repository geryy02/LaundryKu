package com.projectmobile.laundryku;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrasiActivity extends AppCompatActivity {

    private Button tombolRegistrasi;
    private LoadingDialog myLoadingDialog;
    private TextView login;
    private EditText etNamaLengkap, etNomorHp, etEmail, etPassword, etConfirmPassword;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);

        etNamaLengkap = findViewById(R.id.et_namaLengkap);
        etNomorHp = findViewById(R.id.et_nomorHP);
        etEmail = findViewById(R.id.et_email2);
        etPassword = findViewById(R.id.et_password2);
        etConfirmPassword = findViewById(R.id.et_confPass);
        tombolRegistrasi = findViewById(R.id.registrasi_btn);
        login = findViewById(R.id.btn_login);

        myLoadingDialog = new LoadingDialog(this);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        tombolRegistrasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namaLengkap = etNamaLengkap.getText().toString().trim();
                String nomorHp = etNomorHp.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();

                if (!namaLengkap.isEmpty() && !nomorHp.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()) {
                    if (password.equals(confirmPassword)) {
                        myLoadingDialog.show();
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseUser currentUser = mAuth.getCurrentUser();
                                            if (currentUser != null) {
                                                String userId = currentUser.getUid();
                                                Map<String, Object> user = new HashMap<>();
                                                user.put("namaLengkap", namaLengkap);
                                                user.put("nomorHp", nomorHp);
                                                user.put("email", email);
                                                user.put("password", password);
                                                db.collection("users").document(userId).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(RegistrasiActivity.this, "Registrasi berhasil.", Toast.LENGTH_SHORT).show();
                                                        myLoadingDialog.dismiss();
                                                        Intent intent = new Intent(RegistrasiActivity.this, MainActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(RegistrasiActivity.this, "Registrasi gagal. Silakan coba lagi.", Toast.LENGTH_SHORT).show();
                                                        myLoadingDialog.dismiss();
                                                    }
                                                });
                                            }
                                        } else {
                                            Toast.makeText(RegistrasiActivity.this, "Registrasi gagal. Silakan coba lagi.", Toast.LENGTH_SHORT).show();
                                            myLoadingDialog.dismiss();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(RegistrasiActivity.this, "Password dan konfirmasi password tidak cocok.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegistrasiActivity.this, "Harap isi semua data.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tvlogin = new Intent(RegistrasiActivity.this, LoginActivity.class);
                startActivity(tvlogin);
            }
        });
    }
}