package com.projectmobile.laundryku;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RiwayatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RiwayatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CardView btnRiwayatKiloan, btnRiwayatVip, btnRiwayatKarpet, btnRiwayatEkspress;

    public RiwayatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RiwayatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RiwayatFragment newInstance(String param1, String param2) {
        RiwayatFragment fragment = new RiwayatFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_riwayat, container, false);

        btnRiwayatKiloan = view.findViewById(R.id.riwayat_kiloan);
        btnRiwayatVip = view.findViewById(R.id.riwayat_vip);
        btnRiwayatKarpet = view.findViewById(R.id.riwayat_karpet);
        btnRiwayatEkspress = view.findViewById(R.id.riwayat_ekspress);

        btnRiwayatKiloan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent untuk berpindah ke RiwayatKiloanActivity
                Intent intent = new Intent(getActivity(), RiwayatKiloanActivity.class);
                startActivity(intent);
            }
        });

        btnRiwayatVip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent untuk berpindah ke RiwayatVipActivity
                Intent intent = new Intent(getActivity(), RiwayatVipActivity.class);
                startActivity(intent);
            }
        });

        btnRiwayatKarpet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent untuk berpindah ke RiwayatKarpetActivity
                Intent intent = new Intent(getActivity(), RiwayatKarpetActivity.class);
                startActivity(intent);
            }
        });

        btnRiwayatEkspress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent untuk berpindah ke RiwayatEkspressActivity
                Intent intent = new Intent(getActivity(), RiwayatEkspressActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}