package models;

import javafx.beans.property.*;
import javafx.scene.control.Button;

public class Wypozyczenia {

    private final int id;
    private int id_car;
    private Integer numer;
    private String name;
    private String surname;
    private String date_of_loan;
    private String date_of_delivery;
    private String agency;
    private String model;
    private String status;
    private Double price;



    public Wypozyczenia(Integer id_car, Integer id, Integer numer, String name, String surname, String date_of_loan, String date_of_delivery, String agency, String model, String status, Double price) {
        this.id_car = id_car;
        this.id = id;
        this.numer = numer;
        this.name = name;
        this.surname = surname;
        this.date_of_loan = date_of_loan;
        this.date_of_delivery = date_of_delivery;
        this.agency = agency;
        this.model = model;
        this.status = status;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public Integer getNumer() {
        return numer;
    }

    public void setNumer(Integer numer) {
        this.numer = numer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDate_of_loan() {
        return date_of_loan;
    }

    public void setDate_of_loan(String date_of_loan) {
        this.date_of_loan = date_of_loan;
    }

    public String getDate_of_delivery() {
        return date_of_delivery;
    }

    public void setDate_of_delivery(String date_of_delivery) {
        this.date_of_delivery = date_of_delivery;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getId_car() {
        return id_car;
    }

    public void setId_car(int id_car) {
        this.id_car = id_car;
    }
}