package models;

import java.awt.*;
import javafx.scene.control.Button;
import java.sql.Blob;

public class CarsTable {
    String model;
    String engine;
    int cost;
    String status;
    Button button;


    public CarsTable(String model, String engine, Integer cost, String status){
        this.model = model;
        this.engine = engine;
        this.cost = cost;
        this.status = status;
        this.button = new Button("Szczegoly");

    }


    public void setModel(String model) {
        this.model = model;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getModel() {
        return model;
    }

    public String getEngine() {
        return engine;
    }

    public int getCost() {
        return cost;
    }

    public String getStatus() {
        return status;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }
}
