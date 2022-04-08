package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;

public class User {
    private final int id_users;
    private final SimpleStringProperty imie;
    private final SimpleStringProperty nazwisko;
    private final SimpleStringProperty login;
    private final SimpleStringProperty haslo;
    private int idFilia;
    private final SimpleStringProperty filia;
    private int rola;
    private final SimpleStringProperty rolaString;
    private final SimpleStringProperty telefon;
    private final SimpleStringProperty email;
    private final SimpleStringProperty dokument;
    private boolean aktywny;
    private final SimpleStringProperty aktywnyString;


    public User(int id_users, String imie, String nazwisko, String login, String haslo, int idFilia, String filia, int rola, String rolaString, String telefon, String email,
                String dokument, boolean aktywny, String aktywnyString) {
        this.id_users = id_users;
        this.imie = new SimpleStringProperty(imie);
        this.nazwisko = new SimpleStringProperty(nazwisko);
        this.login = new SimpleStringProperty(login);
        this.haslo = new SimpleStringProperty(haslo);
        this.idFilia = idFilia;
        this.filia = new SimpleStringProperty(filia);
        this.rola = rola;
        this.rolaString = new SimpleStringProperty(rolaString);
        this.telefon = new SimpleStringProperty(telefon);
        this.email = new SimpleStringProperty(email);
        this.dokument = new SimpleStringProperty(dokument);
        this.aktywny = aktywny;
        this.aktywnyString = new SimpleStringProperty(aktywnyString);
    }


    public int getIdFilia() {
        return idFilia;
    }

    public int getId_users() {
        return id_users;
    }

    public String getImie() {
        return imie.get();
    }

    public SimpleStringProperty imieProperty() {
        return imie;
    }

    public String getNazwisko() {
        return nazwisko.get();
    }

    public SimpleStringProperty nazwiskoProperty() {
        return nazwisko;
    }

    public String getLogin() {
        return login.get();
    }

    public SimpleStringProperty loginProperty() {
        return login;
    }

    public String getHaslo() {
        return haslo.get();
    }

    public SimpleStringProperty hasloProperty() {
        return haslo;
    }

    public String getFilia() { return filia.get(); }

    public SimpleStringProperty filiaProperty() { return filia; }

    public int getRola() {
        return rola;
    }

    public String getRolaString() {
        return rolaString.get();
    }

    public SimpleStringProperty rolaStringProperty() {
        return rolaString;
    }

    public String getAktywnyString() {
        return aktywnyString.get();
    }

    public SimpleStringProperty aktywnyStringProperty() {
        return aktywnyString;
    }

    public String getTelefon() {
        return telefon.get();
    }

    public SimpleStringProperty telefonProperty() {
        return telefon;
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public String getDokument() {
        return dokument.get();
    }

    public SimpleStringProperty dokumentProperty() {
        return dokument;
    }

    public boolean isAktywny() {
        return aktywny;
    }

    public void setIdFilia(int idFilia) {
        this.idFilia = idFilia;
    }

    public void setImie(String imie) {
        this.imie.set(imie);
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko.set(nazwisko);
    }

    public void setLogin(String login) {
        this.login.set(login);
    }

    public void setHaslo(String haslo) {
        this.haslo.set(haslo);
    }

    public void setFilia(String filia) { this.filia.set(filia); }

    public void setRola(int role) {
        this.rola = role;
    }

    public void setRolaString(String rolaString) {
        this.rolaString.set(rolaString);
    }

    public void setTelefon(String telefon) {
        this.telefon.set(telefon);
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public void setDokument(String dokument) {
        this.dokument.set(dokument);
    }

    public void setAktywny(boolean aktywny) {
        this.aktywny = aktywny;
    }

    public void setAktywnyString(String aktywnyString) {
        this.aktywnyString.set(aktywnyString);
    }
}
