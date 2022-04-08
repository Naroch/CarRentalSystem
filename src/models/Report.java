package models;

import javafx.scene.chart.PieChart;

import java.sql.Date;

public class Report{
    String name, surname, model;
    Date date_of_loan, date_of_delivery;
    int price;

    public Report(String name, String surname, String model, Date date_of_loan, Date date_of_delivery, int price) {
        this.name = name;
        this.surname = surname;
        this.model = model;
        this.date_of_loan = date_of_loan;
        this.date_of_delivery = date_of_delivery;
        this.price = price;
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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Date getDate_of_loan() {
        return date_of_loan;
    }

    public void setDate_of_loan(Date date_of_loan) {
        this.date_of_loan = date_of_loan;
    }

    public Date getDate_of_delivery() {
        return date_of_delivery;
    }

    public void setDate_of_delivery(Date date_of_delivery) {
        this.date_of_delivery = date_of_delivery;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
