package models;

import javafx.scene.control.Button;

public class Cars {
    private int id;
    private String model;
    private int production_date;
    private String engine;
    private int power;
    private String fuel;
    private String transmission;
    private String color;
    private int cost;
    private String registration_number;
    private String status;
    private int id_agency;

    public Cars(int id, String model, int production_date, String engine, int power, String fuel, String transmission, String color, int cost,String registration_number, String status, int id_agency) {
        this.id = id;
        this.model = model;
        this.production_date = production_date;
        this.engine = engine;
        this.power = power;
        this.fuel = fuel;
        this.transmission = transmission;
        this.color = color;
        this.cost = cost;
        this.registration_number = registration_number;
        this.status = status;
        this.id_agency = id_agency;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getProduction_date() {
        return production_date;
    }

    public void setProduction_date(int production_date) {
        this.production_date = production_date;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId_agency() {
        return id_agency;
    }

    public void setId_agency(int id_agency) {
        this.id_agency = id_agency;
    }

    public String getRegistration_number(){
        return registration_number;
    }
    public void setRegistration_number(String registration_number){
        this.registration_number = registration_number;
    }

}
