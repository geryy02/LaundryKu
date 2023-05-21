package com.projectmobile.laundryku.model;

public class PesanVip {
    private String id, kodePelangganVip, namaPelangganVip, nomorHpVip, tanggalMasukVip, tanggalKeluarVip, pilihVip, itemVip, hargaVip;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKodePelangganVip() {
        return kodePelangganVip;
    }

    public void setKodePelangganVip(String kodePelangganVip) {
        this.kodePelangganVip = kodePelangganVip;
    }

    public String getNamaPelangganVip() {
        return namaPelangganVip;
    }

    public void setNamaPelangganVip(String namaPelangganVip) {
        this.namaPelangganVip = namaPelangganVip;
    }

    public String getNomorHpVip() {
        return nomorHpVip;
    }

    public void setNomorHpVip(String nomorHpVip) {
        this.nomorHpVip = nomorHpVip;
    }

    public String getTanggalMasukVip() {
        return tanggalMasukVip;
    }

    public void setTanggalMasukVip(String tanggalMasukVip) {
        this.tanggalMasukVip = tanggalMasukVip;
    }

    public String getTanggalKeluarVip() {
        return tanggalKeluarVip;
    }

    public void setTanggalKeluarVip(String tanggalKeluarVip) {
        this.tanggalKeluarVip = tanggalKeluarVip;
    }

    public String getPilihVip() {
        return pilihVip;
    }

    public void setPilihVip(String pilihVip) {
        this.pilihVip = pilihVip;
    }

    public String getItemVip() {
        return itemVip;
    }

    public void setItemVip(String itemVip) {
        this.itemVip = itemVip;
    }

    public String getHargaVip() {
        return hargaVip;
    }

    public void setHargaVip(String hargaVip) {
        this.hargaVip = hargaVip;
    }

    public PesanVip(String kodePelangganVip, String namaPelangganVip, String nomorHpVip, String tanggalMasukVip, String tanggalKeluarVip, String pilihVip, String itemVip, String hargaVip) {
        this.kodePelangganVip = kodePelangganVip;
        this.namaPelangganVip = namaPelangganVip;
        this.nomorHpVip = nomorHpVip;
        this.tanggalMasukVip = tanggalMasukVip;
        this.tanggalKeluarVip = tanggalKeluarVip;
        this.pilihVip = pilihVip;
        this.itemVip = itemVip;
        this.hargaVip = hargaVip;
    }

    public PesanVip() {

    }
}