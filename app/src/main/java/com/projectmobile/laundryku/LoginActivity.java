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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button tombolLogin;
    private LoadingDialog myLoadingDialog;
    private TextView registrasi, lupaPassword;
    private FirebaseAuth mAuth;
    static final String PREF_FIRST_RUN = "PREF_FIRST_RUN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        tombolLogin = findViewById(R.id.login_btn);
        registrasi = findViewById(R.id.btn_registrasi);
        lupaPassword = findViewById(R.id.btn_lupapass);

        myLoadingDialog = new LoadingDialog(this);

        mAuth = FirebaseAuth.getInstance();

        // Cek apakah pengguna sudah login sebelumnya
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        tombolLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (!email.isEmpty() && !password.isEmpty()){
                    myLoadingDialog.show();
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                myLoadingDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Login berhasil!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                myLoadingDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Login gagal. Silakan cek email dan password Anda.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Harap isi semua field.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        registrasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tvregistrasi = new Intent(LoginActivity.this, RegistrasiActivity.class);
                startActivity(tvregistrasi);
            }
        });

        lupaPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tvlupapass = new Intent(LoginActivity.this, LupaPasswordActivity.class);
                startActivity(tvlupapass);
            }
        });
    }
}