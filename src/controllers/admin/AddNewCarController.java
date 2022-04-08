package controllers.admin;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import models.Agency;
import models.User;
import service.CarsService;
import service.DataBase;

import javax.swing.*;


public class AddNewCarController implements Initializable {
    @FXML
    public TextField model;
    @FXML
    public TextField production_date;
    @FXML
    public TextField engine;
    @FXML
    public TextField power;
    @FXML
    public TextField fuel;
    @FXML
    public TextField transmission;
    @FXML
    public TextField color;
    @FXML
    public TextField cost;
    @FXML
    public TextField status;
    @FXML
    public ComboBox<AgencyUser> filiaComboBox;
    private User user;
    int idFilia;

    public TextField registration_number;
    private CarsService carsService;
    private Component frame;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        idFilia = 1;
        fillFiliaComboBox();
        this.carsService = new CarsService();
    }

    public void AdminUserEdit(User user) {
        this.user = user;
    }

    /**
     * @param actionEvent Przycisk dodający nowe auto do bazy.
     */
    public void AdminDodajPojazdButton(ActionEvent actionEvent) {
        if (this.model.getText() != "" && this.production_date.getText() != ""
                && this.engine.getText() != "" && this.power.getText() != "" && this.fuel.getText() != "" &&
                this.transmission.getText() != "" && this.color.getText() != ""
                && this.registration_number.getText() != "") {
            String model = this.model.getText();
            int production_date = Integer.parseInt(this.production_date.getText());
            String engine = this.engine.getText();
            int power = Integer.parseInt(this.power.getText());
            String fuel = this.fuel.getText();
            String transmission = this.transmission.getText();
            String color = this.color.getText();
            int cost = Integer.parseInt(this.cost.getText());
            String registration_number = this.registration_number.getText();
            String status = "Niedostępny";


            if (this.carsService.createNewCar(model, production_date, engine, power, fuel,
                    transmission, color, cost, registration_number, status, idFilia)) {
                JOptionPane.showMessageDialog(frame, "Dodano pojazd do bazy");
            } else {
                System.out.println("BLAD");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Nie wypełniono wszystkich danych!");
        }
    }

    private static class AgencyUser {
        private final String agencyName;
        private final int idAgency;

        public AgencyUser(String agencyName, int idAgency) {
            this.agencyName = agencyName;
            this.idAgency = idAgency;
        }

        public String getAgencyName() {
            return agencyName;
        }

        public int getIdAgency() {
            return idAgency;
        }
    }


    /**
     * Wypełnia ComboBoxa danymi z bazy danych (id_agency oraz agency_name)
     */
    void fillFiliaComboBox() {
        ObservableList data = FXCollections.observableArrayList();
        try {
            Connection con = DataBase.connect();
            ResultSet rs = con.createStatement().executeQuery("SELECT agency_name, id_agency FROM agency");

            while (rs.next()) {
                data.add(new AgencyUser(rs.getString("agency_name"), rs.getInt("id_agency")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        filiaComboBox.setItems(data);

        filiaComboBox.setConverter(new StringConverter<AgencyUser>() {
            @Override
            public String toString(AgencyUser agencyUser) {
                return agencyUser.getAgencyName();
            }

            @Override
            public AgencyUser fromString(String string) {
                return filiaComboBox.getItems().stream().filter(ap ->
                        ap.getAgencyName().equals(string)).findFirst().orElse(null);
            }
        });


        filiaComboBox.valueProperty().addListener((obs, oldval, newval) -> {
            if (newval != null) {
                System.out.println("Selected agency " + newval.getAgencyName()
                        + ". ID: " + newval.getIdAgency());
                idFilia = newval.getIdAgency();
                filiaComboBox.setValue(newval);
            }
        });
    }


    @FXML
    void AdminCofnijButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/admin/Admin.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene menu = new Scene(zmianaSceny);
        Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
        okno.setScene(menu);
        okno.centerOnScreen();
        okno.show();
    }

    @FXML
    void AdminKatalogPojazdowButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/admin/Admin.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene menu = new Scene(zmianaSceny);
        Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
        okno.setScene(menu);
        okno.centerOnScreen();
        okno.show();
    }

    @FXML
    void AdminRaportyButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("../../scenes/admin/Raports.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene menu = new Scene(zmianaSceny);
        Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
        okno.setScene(menu);
        okno.centerOnScreen();
        okno.show();


    }

    @FXML
    void AdminUstawieniaButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/admin/AdminSettings.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene menu = new Scene(zmianaSceny);
        Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
        okno.setScene(menu);
        okno.centerOnScreen();
        okno.show();
    }

    @FXML
    void AdminUzytkownicyButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/admin/AdminUsers.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene menu = new Scene(zmianaSceny);
        Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
        okno.setScene(menu);
        okno.centerOnScreen();
        okno.show();
    }

    @FXML
    void AdminWypozyczeniaKlientowButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/admin/AdminLoans.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene menu = new Scene(zmianaSceny);
        Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
        okno.setScene(menu);
        okno.centerOnScreen();
        okno.show();
    }

    @FXML
    void AdminLogoutButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("/login/Login.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene menu = new Scene(zmianaSceny);
        Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
        okno.setScene(menu);
        okno.centerOnScreen();
        okno.show();
    }
}
