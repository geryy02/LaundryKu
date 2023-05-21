package com.projectmobile.laundryku.model;

public class PesanKarpet {
    private String id, kodePelangganKarpet, namaPelangganKarpet, nomorHpKarpet, tanggalMasukKarpet, tanggalKeluarKarpet, pilihKarpet, itemKarpet, hargaKarpet;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKodePelangganKarpet() {
        return kodePelangganKarpet;
    }

    public void setKodePelangganKarpet(String kodePelangganKarpet) {
        this.kodePelangganKarpet = kodePelangganKarpet;
    }

    public String getNamaPelangganKarpet() {
        return namaPelangganKarpet;
    }

    public void setNamaPelangganKarpet(String namaPelangganKarpet) {
        this.namaPelangganKarpet = namaPelangganKarpet;
    }

    public String getNomorHpKarpet() {
        return nomorHpKarpet;
    }

    public void setNomorHpKarpet(String nomorHpKarpet) {
        this.nomorHpKarpet = nomorHpKarpet;
    }

    public String getTanggalMasukKarpet() {
        return tanggalMasukKarpet;
    }

    public void setTanggalMasukKarpet(String tanggalMasukKarpet) {
        this.tanggalMasukKarpet = tanggalMasukKarpet;
    }

    public String getTanggalKeluarKarpet() {
        return tanggalKeluarKarpet;
    }

    public void setTanggalKeluarKarpet(String tanggalKeluarKarpet) {
        this.tanggalKeluarKarpet = tanggalKeluarKarpet;
    }

    public String getPilihKarpet() {
        return pilihKarpet;
    }

    public void setPilihKarpet(String pilihKarpet) {
        this.pilihKarpet = pilihKarpet;
    }

    public String getItemKarpet() {
        return itemKarpet;
    }

    public void setItemKarpet(String itemKarpet) {
        this.itemKarpet = itemKarpet;
    }

    public String getHargaKarpet() {
        return hargaKarpet;
    }

    public void setHargaKarpet(String hargaKarpet) {
        this.hargaKarpet = hargaKarpet;
    }

    public PesanKarpet(String kodePelangganKarpet, String namaPelangganKarpet, String nomorHpKarpet, String tanggalMasukKarpet, String tanggalKeluarKarpet, String pilihKarpet, String itemKarpet, String hargaKarpet) {
        this.kodePelangganKarpet = kodePelangganKarpet;
        this.namaPelangganKarpet = namaPelangganKarpet;
        this.nomorHpKarpet = nomorHpKarpet;
        this.tanggalMasukKarpet = tanggalMasukKarpet;
        this.tanggalKeluarKarpet = tanggalKeluarKarpet;
        this.pilihKarpet = pilihKarpet;
        this.itemKarpet = itemKarpet;
        this.hargaKarpet = hargaKarpet;
    }

    public PesanKarpet(){

    }
}
