
package controllers.client;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
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

public class ClientController implements Initializable {

    public TableView<Cars> cars_table;
    public TableColumn<Cars, String> model_table;
    public TableColumn<Cars, String> silnik_table;
    public TableColumn<Cars, String> status_table;
    public TableColumn<Cars, Integer> cena_table;
    public TableColumn<Cars, String> paliwo_table;
    public TableColumn<Cars, String> skrzynia_table;
    public TableColumn<Cars, Integer> moc_table;


    public TextField modelTextField;
    public ChoiceBox statusChoice;
    public ChoiceBox skrzyniaChoice;
    public ChoiceBox paliwoChoice;

    public static int id;
    public static int cost;
    public static int id_agency;
    public static String status;
    ObservableList<Cars> oblist = FXCollections.observableArrayList();


    public static String String_name;
    public static String String_lastName;
    public static String string_active;

    //za kolejnym razem
    String u_name;
    String u_lastName;

    public void setU_name(String u_name) {
        this.u_name = u_name;
    }

    public void setUlastName(String u_lastName) {
        this.u_lastName = u_lastName;
    }


    //za pierwszym razem
    public static int userId;

    public void setUser(int userId) {
        ClientController.userId = userId;
    }

    /**
     * Funkcja wypełnia tabelę aut z bazy danych.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Platform.runLater(() -> {

            try {
                String authorize = "select user_name, surname, active from users where id_users=?";
                PreparedStatement stat_statement = DataBase.connect().prepareStatement(authorize);
                stat_statement.setInt(1, userId);
                ResultSet rs1 = stat_statement.executeQuery();

                if (rs1.next()) {
                    String_name = rs1.getString("user_name");
                    String_lastName = rs1.getString("surname");
                    string_active = rs1.getString("active");
                    setU_name(String_name);
                    setUlastName(String_lastName);
                    name.setText(u_name);
                    lastName.setText(u_lastName);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (!u_name.isEmpty()) {
                name.setText(u_name);
                lastName.setText(u_lastName);
            }

        });

        try {
            Connection connection = DataBase.connect();

            ResultSet rs = connection.createStatement().executeQuery("select * from cars");

            while (rs.next()) {
                oblist.add(new Cars(rs.getInt("id_car"), rs.getString("model"), rs.getInt("production_date"), rs.getString("engine"),
                        rs.getInt("power"), rs.getString("fuel"), rs.getString("transmission"), rs.getString("color"), rs.getInt("cost"),
                        rs.getString("registration_number"),
                        rs.getString("status"), rs.getInt("id_agency")));

            }
        } catch (SQLException e) {

        }

        model_table.setCellValueFactory(new PropertyValueFactory<>("model"));
        silnik_table.setCellValueFactory(new PropertyValueFactory<>("engine"));
        cena_table.setCellValueFactory(new PropertyValueFactory<>("cost"));
        status_table.setCellValueFactory(new PropertyValueFactory<>("status"));
        paliwo_table.setCellValueFactory(new PropertyValueFactory<>("fuel"));
        skrzynia_table.setCellValueFactory(new PropertyValueFactory<>("transmission"));
        moc_table.setCellValueFactory(new PropertyValueFactory<>("power"));



        cars_table.setItems(oblist);

        FilteredList<Cars> filterlist = new FilteredList<>(oblist, p -> true);

        modelTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterlist.setPredicate(Cars -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                return String.valueOf(Cars.getModel()).toLowerCase().contains(lowerCaseFilter);
            });
        });

        statusChoice.getSelectionModel().selectedItemProperty().addListener((observable2, oldValue, newValue) -> {
            filterlist.setPredicate(Cars -> {
                if (newValue == null) {
                    return true;
                }

                String help = newValue.toString();
                if (String.valueOf(Cars.getStatus()).contains(help)) {
                    return true;
                } else return help.equals("Wszystkie");
            });
        });

        skrzyniaChoice.getSelectionModel().selectedItemProperty().addListener((observable2, oldValue, newValue) -> {
            filterlist.setPredicate(Cars -> {
                if (newValue == null) {
                    return true;
                }

                String help = newValue.toString();
                if (String.valueOf(Cars.getTransmission()).contains(help)) {
                    return true;
                } else return help.equals("Wszystkie");
            });
        });

        paliwoChoice.getSelectionModel().selectedItemProperty().addListener((observable2, oldValue, newValue) -> {
            filterlist.setPredicate(Cars -> {
                if (newValue == null) {
                    return true;
                }

                String help = newValue.toString();
                if (String.valueOf(Cars.getFuel()).contains(help)) {
                    return true;
                } else return help.equals("Wszystkie");
            });
        });

        SortedList<Cars> sorted = new SortedList<>(filterlist);
        sorted.comparatorProperty().bind(cars_table.comparatorProperty());
        cars_table.setItems(sorted);


        cars_table.setOnMouseClicked((event) -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && (! oblist.isEmpty())) {
                int index = cars_table.getSelectionModel().getFocusedIndex();
                Cars cars = cars_table.getItems().get(index);
                id = cars.getId();
                cost = cars.getCost();
                id_agency = cars.getId_agency();
                status = cars.getStatus();

                Parent zmianaSceny = null;
                try {
                    zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/client/ClientShowCar.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Scene menu = new Scene(zmianaSceny);
                Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
                okno.setScene(menu);
                okno.centerOnScreen();
                okno.show();


            }
        });

    }


    @FXML
    public Label name;
    @FXML
    public Label lastName;


    @FXML
    void Car_Details_Button(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader1 = new FXMLLoader(
                getClass().getClassLoader().getResource("scenes/client/ClientCarDetails.fxml")
        );
        Parent root = fxmlLoader1.load();
        ClientCarDetailController controller1 = fxmlLoader1.getController();
        controller1.setU_name(String_name);
        controller1.setUlastName(String_lastName);
        Scene menu = new Scene(root);
        Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
        okno.setScene(menu);
        okno.centerOnScreen();
        okno.show();
    }


    @FXML
    void ClientUstawieniaButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/client/ClientSettings.fxml"));
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
    void ClientLogoutButton(ActionEvent event) {
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


    @FXML
    void ClientRentButton(ActionEvent event) throws IOException {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/client/ClientLoans.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(String_name);


        //
        name.setText(String_name);
        lastName.setText(String_lastName);


        Scene menu = new Scene(zmianaSceny);
        Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
        okno.setScene(menu);
        okno.centerOnScreen();
        okno.show();
    }

    @FXML
    void ClientCatalogButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/client/Client.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(String_name);
        name.setText(String_name);
        lastName.setText(String_lastName);

        Scene menu = new Scene(zmianaSceny);
        Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
        okno.setScene(menu);
        okno.centerOnScreen();
        okno.show();
    }


}

