package com.projectmobile.laundryku.model;

public class PesanEkspress {
    String id, kodePelangganEkspress, namaPelangganEkspress, nomorHpEkspress, tanggalMasukEkspress, tanggalKeluarEkspress, paketEkspress, kilogramEkspress, hargaEkspress;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKodePelangganEkspress() {
        return kodePelangganEkspress;
    }

    public void setKodePelangganEkspress(String kodePelangganEkspress) {
        this.kodePelangganEkspress = kodePelangganEkspress;
    }

    public String getNamaPelangganEkspress() {
        return namaPelangganEkspress;
    }

    public void setNamaPelangganEkspress(String namaPelangganEkspress) {
        this.namaPelangganEkspress = namaPelangganEkspress;
    }

    public String getNomorHpEkspress() {
        return nomorHpEkspress;
    }

    public void setNomorHpEkspress(String nomorHpEkspress) {
        this.nomorHpEkspress = nomorHpEkspress;
    }

    public String getTanggalMasukEkspress() {
        return tanggalMasukEkspress;
    }

    public void setTanggalMasukEkspress(String tanggalMasukEkspress) {
        this.tanggalMasukEkspress = tanggalMasukEkspress;
    }

    public String getTanggalKeluarEkspress() {
        return tanggalKeluarEkspress;
    }

    public void setTanggalKeluarEkspress(String tanggalKeluarEkspress) {
        this.tanggalKeluarEkspress = tanggalKeluarEkspress;
    }

    public String getPaketEkspress() {
        return paketEkspress;
    }

    public void setPaketEkspress(String paketEkspress) {
        this.paketEkspress = paketEkspress;
    }

    public String getKilogramEkspress() {
        return kilogramEkspress;
    }

    public void setKilogramEkspress(String kilogramEkspress) {
        this.kilogramEkspress = kilogramEkspress;
    }

    public String getHargaEkspress() {
        return hargaEkspress;
    }

    public void setHargaEkspress(String hargaEkspress) {
        this.hargaEkspress = hargaEkspress;
    }

    public PesanEkspress(String kodePelangganEkspress, String namaPelangganEkspress, String nomorHpEkspress, String tanggalMasukEkspress, String tanggalKeluarEkspress, String paketEkspress, String kilogramEkspress, String hargaEkspress) {
        this.kodePelangganEkspress = kodePelangganEkspress;
        this.namaPelangganEkspress = namaPelangganEkspress;
        this.nomorHpEkspress = nomorHpEkspress;
        this.tanggalMasukEkspress = tanggalMasukEkspress;
        this.tanggalKeluarEkspress = tanggalKeluarEkspress;
        this.paketEkspress = paketEkspress;
        this.kilogramEkspress = kilogramEkspress;
        this.hargaEkspress = hargaEkspress;
    }

    public PesanEkspress(){

    }
}
