package com.projectmobile.laundryku;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.projectmobile.laundryku.adapter.KiloanAdapter;
import com.projectmobile.laundryku.model.ModelItemKiloan;

import java.util.ArrayList;

public class KiloanActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseFirestore database;
    CollectionReference kiloanRef;
    KiloanAdapter myAdapter;
    ArrayList<ModelItemKiloan>list;
    Button btnPesanKiloan;
    LoadingDialog myLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kiloan);

        recyclerView = findViewById(R.id.recyclerviewKiloan);
        database = FirebaseFirestore.getInstance();
        kiloanRef = database.collection("kiloan");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new KiloanAdapter(this, list);
        recyclerView.setAdapter(myAdapter);
        myLoadingDialog = new LoadingDialog(this);

        dataKiloan();

        btnPesanKiloan = findViewById(R.id.btn_pesankiloan);
        Toolbar toolbar = findViewById(R.id.toolbar_kiloan);
        setSupportActionBar(toolbar);

        // Set tombol kembali (back) pada toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnPesanKiloan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myLoadingDialog.show();
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        myLoadingDialog.cancel();
                        Intent intent = new Intent(KiloanActivity.this, PesanKiloanActivity.class);
                        startActivity(intent);
                    }
                };
                handler.postDelayed(runnable, 2000);
            }
        });
    }

    private void dataKiloan() {
        myLoadingDialog.show();
        kiloanRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                myLoadingDialog.cancel();
                if (task.isSuccessful()){
                    list.clear();
                    for (DocumentSnapshot document : task.getResult()){
                        ModelItemKiloan kiloan = document.toObject(ModelItemKiloan.class);
                        list.add(kiloan);
                    }
                    myAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}