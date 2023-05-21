package com.projectmobile.laundryku;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class LupaPasswordActivity extends AppCompatActivity {

    private EditText editEmail;
    private Button btnResetPass;
    private LoadingDialog myLoadingDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_password);

        editEmail = findViewById(R.id.rst_email);
        btnResetPass = findViewById(R.id.btn_reset);
        myLoadingDialog = new LoadingDialog(this);

        // Inisialisasi FirebaseAuth dan DatabaseReference
        mAuth = FirebaseAuth.getInstance();

        // Event listener untuk tombol kirim
        btnResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
    }
    private void resetPassword(){
        String email = editEmail.getText().toString().trim();
        if (email.isEmpty()){
            editEmail.setError("Email Diperlukan!");
            editEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editEmail.setError("Berikan email yang valid!");
            editEmail.requestFocus();
            return;
        }
        myLoadingDialog.show();
        // Mengirim email reset password menggunakan Firebase Auth
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    myLoadingDialog.dismiss();
                    Toast.makeText(LupaPasswordActivity.this, "Periksa email Anda untuk mengatur ulang kata sandi!", Toast.LENGTH_LONG).show();
                    finish();
                }else {
                    Toast.makeText(LupaPasswordActivity.this, "Coba lagi! sesuatu yang salah terjadi!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
