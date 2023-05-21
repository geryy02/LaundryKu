package com.projectmobile.laundryku.model;

public class PesanKiloan {
    private String id, kodePelangganKiloan, namaPelangganKiloan, nomorHpKiloan, tanggalMasukKiloan, tanggalKeluarKiloan, paketkiloan, kilogramKiloan, hargaKiloan;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKodePelangganKiloan() {
        return kodePelangganKiloan;
    }

    public void setKodePelangganKiloan(String kodePelangganKiloan) {
        this.kodePelangganKiloan = kodePelangganKiloan;
    }

    public String getNamaPelangganKiloan() {
        return namaPelangganKiloan;
    }

    public void setNamaPelangganKiloan(String namaPelangganKiloan) {
        this.namaPelangganKiloan = namaPelangganKiloan;
    }

    public String getNomorHpKiloan() {
        return nomorHpKiloan;
    }

    public void setNomorHpKiloan(String nomorHpKiloan) {
        this.nomorHpKiloan = nomorHpKiloan;
    }

    public String getTanggalMasukKiloan() {
        return tanggalMasukKiloan;
    }

    public void setTanggalMasukKiloan(String tanggalMasukKiloan) {
        this.tanggalMasukKiloan = tanggalMasukKiloan;
    }

    public String getTanggalKeluarKiloan() {
        return tanggalKeluarKiloan;
    }

    public void setTanggalKeluarKiloan(String tanggalKeluarKiloan) {
        this.tanggalKeluarKiloan = tanggalKeluarKiloan;
    }

    public String getPaketkiloan() {
        return paketkiloan;
    }

    public void setPaketkiloan(String paketkiloan) {
        this.paketkiloan = paketkiloan;
    }

    public String getKilogramKiloan() {
        return kilogramKiloan;
    }

    public void setKilogramKiloan(String kilogramKiloan) {
        this.kilogramKiloan = kilogramKiloan;
    }

    public String getHargaKiloan() {
        return hargaKiloan;
    }

    public void setHargaKiloan(String hargaKiloan) {
        this.hargaKiloan = hargaKiloan;
    }

    public PesanKiloan(String kodePelangganKiloan, String namaPelangganKiloan, String nomorHpKiloan, String tanggalMasukKiloan, String tanggalKeluarKiloan, String paketkiloan, String kilogramKiloan, String hargaKiloan) {
        this.kodePelangganKiloan = kodePelangganKiloan;
        this.namaPelangganKiloan = namaPelangganKiloan;
        this.nomorHpKiloan = nomorHpKiloan;
        this.tanggalMasukKiloan = tanggalMasukKiloan;
        this.tanggalKeluarKiloan = tanggalKeluarKiloan;
        this.paketkiloan = paketkiloan;
        this.kilogramKiloan = kilogramKiloan;
        this.hargaKiloan = hargaKiloan;

    }

    public PesanKiloan(){

    }
}
