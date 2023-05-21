package com.projectmobile.laundryku;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageView btn_editprofil;
    private TextView tvNamaLengkap;
    private TextView tvNomorHp;
    private TextView tvEmail;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private SwipeRefreshLayout refreshLayoutProfil;
    private LoadingDialog myLoadingDialog;

    public ProfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfilFragment newInstance(String param1, String param2) {
        ProfilFragment fragment = new ProfilFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        // Inisialisasi Firebase Authentication dan Realtime Database
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profil, container, false);
        tvNamaLengkap = view.findViewById(R.id.profil_namaLengkap);
        tvNomorHp = view.findViewById(R.id.profil_nomorHp);
        tvEmail = view.findViewById(R.id.profil_email);
        btn_editprofil = view.findViewById(R.id.ic_edit_profil);

        myLoadingDialog = new LoadingDialog(getActivity());

        refreshLayoutProfil = view.findViewById(R.id.swipeRefresh_Profil);
        refreshLayoutProfil.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataProfil();
                refreshLayoutProfil.setRefreshing(false);
            }
        });

        btn_editprofil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent untuk berpindah ke EditProfilActivity
                Intent intent = new Intent(getActivity(), EditProfilActivity.class);
                startActivity(intent);
            }
        });

        getDataProfil();
        return view;
    }

    private void getDataProfil(){
        myLoadingDialog.show();
        // Mengambil data pengguna dari Firebase Authentication dan FireStore
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DocumentReference userRef = mFirestore.collection("users").document(userId);
            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    myLoadingDialog.cancel();
                    if (documentSnapshot.exists()) {
                        String namaLengkap = documentSnapshot.getString("namaLengkap");
                        String nomorHp = documentSnapshot.getString("nomorHp");
                        String email = documentSnapshot.getString("email");

                        // Set data pengguna ke TextView
                        tvNamaLengkap.setText(namaLengkap);
                        tvNomorHp.setText(nomorHp);
                        tvEmail.setText(email);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //handle error
                }
            });
        }
    }
}