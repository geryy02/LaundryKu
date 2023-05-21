package com.projectmobile.laundryku.model;

public class User {
    private String namaLengkap;
    private String nomorHp;
    private String email;

    public User() {
        // Diperlukan oleh Firebase Realtime Database
    }

    public User(String nama, String nomorHp, String email) {
        this.namaLengkap = nama;
        this.nomorHp = nomorHp;
        this.email = email;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String nama) {
        this.namaLengkap = nama;
    }

    public String getNomorHp() {
        return nomorHp;
    }

    public void setNomorHp(String nomorHp) {
        this.nomorHp = nomorHp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
