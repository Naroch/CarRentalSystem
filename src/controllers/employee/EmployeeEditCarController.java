package controllers.employee;

import controllers.admin.AdminController;
import controllers.admin.AdminEditCarController;
import controllers.admin.AdminShowCarController;
import controllers.client.ClientController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import models.Cars;
import service.DataBase;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static controllers.employee.EmployeeController.*;

public class EmployeeEditCarController implements Initializable {
    public TextField marka;
    public TextField rok;
    public TextField silnik;
    public TextField moc;
    public TextField paliwo;
    public TextField skrzynia;
    public TextField kolor;
    public TextField koszt;
    public TextField nr_reje;
    int idFilia;
    @FXML
    public ComboBox<AgencyUser> filiaBox;
    private Component frame;
    @FXML
    public javafx.scene.control.Label name;
    @FXML
    public Label lastName;

    public void initialize(URL location, ResourceBundle resources) {


            Platform.runLater(() -> {
                name.setText(EmployeeController.u_name);
                lastName.setText(EmployeeController.u_lastName);
    });




        EmployeeController controler = new EmployeeController();
        fillFiliaComboBox();


        try {
            Connection connection = DataBase.connect();

            ResultSet rs = connection.createStatement().executeQuery("select * from cars where id_car =" +
                    " " + EmployeeController.id);
            while (rs.next()) {

                Cars car = new Cars(rs.getInt("id_car"), rs.getString("model"),
                        rs.getInt("production_date"),
                        rs.getString("engine"),
                        rs.getInt("power"), rs.getString("fuel"),
                        rs.getString("transmission"),
                        rs.getString("color"), rs.getInt("cost"),
                        rs.getString("registration_number"),
                        rs.getString("status"), rs.getInt("id_agency"));


                marka.setText(car.getModel());
                rok.setText(String.valueOf(car.getProduction_date()));
                silnik.setText(car.getEngine());
                moc.setText(String.valueOf(car.getPower()));
                paliwo.setText(car.getFuel());
                skrzynia.setText(car.getTransmission());
                kolor.setText(car.getColor());
                koszt.setText(String.valueOf(car.getCost()));
                nr_reje.setText(car.getRegistration_number());
                filiaBox.setValue(new AgencyUser(EmployeeShowCarController.name2, EmployeeController.id));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class AgencyUser {
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

    void fillFiliaComboBox() {
        ObservableList data = FXCollections.observableArrayList();
        try {
            Connection con = DataBase.connect();
            ResultSet rs = con.createStatement().executeQuery("SELECT agency_name, id_agency FROM agency");

            while(rs.next()) {
                data.add(new AgencyUser(rs.getString("agency_name"), rs.getInt("id_agency")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        filiaBox.setItems(data);

        filiaBox.setConverter(new StringConverter<AgencyUser>() {
            @Override
            public String toString(AgencyUser agencyUser) {
                return agencyUser.getAgencyName();
            }

            @Override
            public AgencyUser fromString(String string) {
                return filiaBox.getItems().stream().filter(ap ->
                        ap.getAgencyName().equals(string)).findFirst().orElse(null);
            }
        });


        filiaBox.valueProperty().addListener((obs, oldval, newval) -> {
            if(newval != null) {
                System.out.println("Selected agency " + newval.getAgencyName()
                        + ". ID: " + newval.getIdAgency());
                idFilia = newval.getIdAgency();
                filiaBox.setValue(newval);
            }
        });
    }

    /**
     * @param actionEvent Przycisk zapisujący edycję pojazdu.
     */
    @FXML
    void EmployeeZapiszEdycjePojazduButton(ActionEvent actionEvent) {
        EmployeeController controler = new EmployeeController();
        String marka = this.marka.getText();
        int rok = Integer.parseInt(this.rok.getText());
        String silnik = this.silnik.getText();
        int moc = Integer.parseInt(this.moc.getText());
        String paliwo = this.paliwo.getText();
        String skrzynia = this.skrzynia.getText();
        String kolor = this.kolor.getText();
        int koszt = Integer.parseInt(this.koszt.getText());
        String nr_reje = this.nr_reje.getText();


        try {
            Connection connection = DataBase.connect();
            String query = "update cars set model = ?, production_date = ?, engine = ?, power = ?, fuel = ?," +
                    " transmission =?,  color=?, cost=?, registration_number=?, id_agency=? where id_car = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, marka);
            preparedStatement.setInt(2, rok);
            preparedStatement.setString(3, silnik);
            preparedStatement.setInt(4, moc);
            preparedStatement.setString(5, paliwo);
            preparedStatement.setString(6, skrzynia);
            preparedStatement.setString(7, kolor);
            preparedStatement.setInt(8, koszt);
            preparedStatement.setString(9, nr_reje);
            preparedStatement.setInt(10, idFilia);
            preparedStatement.setInt(11, EmployeeController.id);
            preparedStatement.execute();

            JOptionPane.showMessageDialog(frame, "Edytowano pojazd");

            Parent zmianaSceny = null;
            try {
                zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/employee/EmployeeShowCar.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene menu = new Scene(zmianaSceny);
            Stage okno = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            okno.setScene(menu);
            okno.centerOnScreen();
            okno.show();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
    @FXML
    void EmployeeCofnijButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/employee/EmployeeShowCar.fxml"));
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
    void EmloyeeKatalogPojazdowButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/employee/Employee.fxml"));
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
    void EmloyeeRaportyButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/employee/Raports.fxml"));
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
    void EmloyeeUstawieniaButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/employee/Employee.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene menu = new Scene(zmianaSceny);
        Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
        okno.setScene(menu);
        okno.centerOnScreen();
        okno.show();
    }

    public void EmployeeUzytkownicyButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/employee/EmployeeUsers.fxml"));
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
    void EmloyeeWypozyczeniaKlientowButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/employee/Employee.fxml"));
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
    void EmployeeLogoutButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/login/Login.fxml"));
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
