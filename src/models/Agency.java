package models;

import javafx.beans.property.SimpleStringProperty;

public class Agency {
    private final int idAgency;
    private final SimpleStringProperty filia;
    private final SimpleStringProperty ulica;
    private final SimpleStringProperty numerBudynku;
    private final SimpleStringProperty miasto;
    private final SimpleStringProperty kodPocztowy;
    private final SimpleStringProperty adresEmail;
    private final SimpleStringProperty telefon;

    public Agency(int idAgency, String filia, String ulica, String numerBudynku, String miasto, String kodPocztowy,
                  String adresEmail, String telefon) {
        this.idAgency = idAgency;
        this.filia = new SimpleStringProperty(filia);
        this.ulica = new SimpleStringProperty(ulica);
        this.numerBudynku = new SimpleStringProperty(numerBudynku);
        this.miasto = new SimpleStringProperty(miasto);
        this.kodPocztowy = new SimpleStringProperty(kodPocztowy);
        this.adresEmail = new SimpleStringProperty(adresEmail);
        this.telefon = new SimpleStringProperty(telefon);
    }

    public int getIdAgency() {
        return idAgency;
    }

    public String getFilia() {
        return filia.get();
    }

    public SimpleStringProperty filiaProperty() {
        return filia;
    }

    public String getUlica() {
        return ulica.get();
    }

    public SimpleStringProperty ulicaProperty() {
        return ulica;
    }

    public String getNumerBudynku() {
        return numerBudynku.get();
    }

    public SimpleStringProperty numerBudynkuProperty() {
        return numerBudynku;
    }

    public String getMiasto() {
        return miasto.get();
    }

    public SimpleStringProperty miastoProperty() {
        return miasto;
    }

    public String getKodPocztowy() {
        return kodPocztowy.get();
    }

    public SimpleStringProperty kodPocztowyProperty() {
        return kodPocztowy;
    }

    public String getAdresEmail() {
        return adresEmail.get();
    }

    public SimpleStringProperty adresEmailProperty() {
        return adresEmail;
    }

    public String getTelefon() {
        return telefon.get();
    }

    public SimpleStringProperty telefonProperty() {
        return telefon;
    }

    public void setFilia(String filia) {
        this.filia.set(filia);
    }

    public void setUlica(String ulica) {
        this.ulica.set(ulica);
    }

    public void setNumerBudynku(String numerBudynku) {
        this.numerBudynku.set(numerBudynku);
    }

    public void setMiasto(String miasto) {
        this.miasto.set(miasto);
    }

    public void setKodPocztowy(String kodPocztowy) {
        this.kodPocztowy.set(kodPocztowy);
    }

    public void setAdresEmail(String adresEmail) {
        this.adresEmail.set(adresEmail);
    }

    public void setTelefon(String telefon) {
        this.telefon.set(telefon);
    }
}
