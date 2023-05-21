package com.projectmobile.laundryku.model;

public class ModelItemVip {
    String namaItemVip;
    String hargaItemVip;

    public ModelItemVip(String namaItemVip, String hargaItemVip) {
        this.namaItemVip = namaItemVip;
        this.hargaItemVip = hargaItemVip;
    }

    public ModelItemVip(){

    }

    public String getNamaItemVip() {
        return namaItemVip;
    }

    public void setNamaItemVip(String namaItemVip) {
        this.namaItemVip = namaItemVip;
    }

    public String getHargaItemVip() {
        return hargaItemVip;
    }

    public void setHargaItemVip(String hargaItemVip) {
        this.hargaItemVip = hargaItemVip;
    }
}
