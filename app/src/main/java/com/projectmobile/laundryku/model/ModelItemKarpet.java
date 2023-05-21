package com.projectmobile.laundryku.model;

public class ModelItemKarpet {
    String namaItemKarpet;
    String hargaItemKarpet;

    public String getNamaItemKarpet() {
        return namaItemKarpet;
    }

    public void setNamaItemKarpet(String namaItemKarpet) {
        this.namaItemKarpet = namaItemKarpet;
    }

    public String getHargaItemKarpet() {
        return hargaItemKarpet;
    }

    public void setHargaItemKarpet(String hargaItemKarpet) {
        this.hargaItemKarpet = hargaItemKarpet;
    }

    public ModelItemKarpet(String namaItemKarpet, String hargaItemKarpet) {
        this.namaItemKarpet = namaItemKarpet;
        this.hargaItemKarpet = hargaItemKarpet;
    }

    public ModelItemKarpet(){

    }
}
