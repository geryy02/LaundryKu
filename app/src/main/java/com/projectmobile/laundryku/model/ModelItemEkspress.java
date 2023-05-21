package com.projectmobile.laundryku.model;

public class ModelItemEkspress {
    String namaPaketEkspress;
    String keteranganPaketEkspress;
    String hargaPaketEkspress;

    public ModelItemEkspress(String namaPaketEkspress, String keteranganPaketEkspress, String hargaPaketEkspress) {
        this.namaPaketEkspress = namaPaketEkspress;
        this.keteranganPaketEkspress = keteranganPaketEkspress;
        this.hargaPaketEkspress = hargaPaketEkspress;
    }

    public ModelItemEkspress(){

    }

    public String getNamaPaketEkspress() {
        return namaPaketEkspress;
    }

    public void setNamaPaketEkspress(String namaPaketEkspress) {
        this.namaPaketEkspress = namaPaketEkspress;
    }

    public String getKeteranganPaketEkspress() {
        return keteranganPaketEkspress;
    }

    public void setKeteranganPaketEkspress(String keteranganPaketEkspress) {
        this.keteranganPaketEkspress = keteranganPaketEkspress;
    }

    public String getHargaPaketEkspress() {
        return hargaPaketEkspress;
    }

    public void setHargaPaketEkspress(String hargaPaketEkspress) {
        this.hargaPaketEkspress = hargaPaketEkspress;
    }
}
