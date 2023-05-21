package com.projectmobile.laundryku.model;

public class ModelItemKiloan {
    String namaPaketKiloan;
    String keteranganSatuPaketKiloan;
    String keteranganDuaPaketKiloan;
    String keteranganTigaPaketKiloan;
    String keteranganEmpatPaketKiloan;
    String hargaPaketKiloan;

    public String getNamaPaketKiloan() {
        return namaPaketKiloan;
    }

    public void setNamaPaketKiloan(String namaPaketKiloan) {
        this.namaPaketKiloan = namaPaketKiloan;
    }

    public String getKeteranganSatuPaketKiloan() {
        return keteranganSatuPaketKiloan;
    }

    public void setKeteranganSatuPaketKiloan(String keteranganSatuPaketKiloan) {
        this.keteranganSatuPaketKiloan = keteranganSatuPaketKiloan;
    }

    public String getKeteranganDuaPaketKiloan() {
        return keteranganDuaPaketKiloan;
    }

    public void setKeteranganDuaPaketKiloan(String keteranganDuaPaketKiloan) {
        this.keteranganDuaPaketKiloan = keteranganDuaPaketKiloan;
    }

    public String getKeteranganTigaPaketKiloan() {
        return keteranganTigaPaketKiloan;
    }

    public void setKeteranganTigaPaketKiloan(String keteranganTigaPaketKiloan) {
        this.keteranganTigaPaketKiloan = keteranganTigaPaketKiloan;
    }

    public String getKeteranganEmpatPaketKiloan() {
        return keteranganEmpatPaketKiloan;
    }

    public void setKeteranganEmpatPaketKiloan(String keteranganEmpatPaketKiloan) {
        this.keteranganEmpatPaketKiloan = keteranganEmpatPaketKiloan;
    }

    public String getHargaPaketKiloan() {
        return hargaPaketKiloan;
    }

    public void setHargaPaketKiloan(String hargaPaketKiloan) {
        this.hargaPaketKiloan = hargaPaketKiloan;
    }

    public ModelItemKiloan(String namaPaketKiloan, String keteranganSatuPaketKiloan, String keteranganDuaPaketKiloan, String keteranganTigaPaketKiloan, String keteranganEmpatPaketKiloan, String hargaPaketKiloan) {
        this.namaPaketKiloan = namaPaketKiloan;
        this.keteranganSatuPaketKiloan = keteranganSatuPaketKiloan;
        this.keteranganDuaPaketKiloan = keteranganDuaPaketKiloan;
        this.keteranganTigaPaketKiloan = keteranganTigaPaketKiloan;
        this.keteranganEmpatPaketKiloan = keteranganEmpatPaketKiloan;
        this.hargaPaketKiloan = hargaPaketKiloan;
    }

    public ModelItemKiloan(){

    }
}
