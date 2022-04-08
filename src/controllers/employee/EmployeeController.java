package controllers.employee;

import controllers.client.ClientController;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.Cars;
import service.DataBase;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EmployeeController implements Initializable {

    public TableView<Cars> cars_table;
    public TableColumn<Cars, String> model_table;
    public TableColumn<Cars, String> silnik_table;
    public TableColumn<Cars, String> status_table;
    public TableColumn<Cars, Integer> cena_table;
    public TableColumn<Cars, String> paliwo_table;
    public TableColumn<Cars, String> skrzynia_table;
    public TableColumn<Cars, Integer> moc_table;

    public static int id;
    public TextField modelTextField;
    public ChoiceBox statusChoice;
    public ChoiceBox skrzyniaChoice;
    public ChoiceBox paliwoChoice;

    ObservableList<Cars> oblist = FXCollections.observableArrayList();

    //Damian
    public static int userId, id_agency;

    public void setUser(int userId) {
        EmployeeController.userId = userId;
    }

    @FXML
    private Label name;
    @FXML
    private Label lastName;

    public static String u_name;
    public static String u_lastName;


    /**
     *  Funkcja wypełnia tabelę aut danymi z bazy danych.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Platform.runLater(() -> {

            try {
                String authorize = "select user_name, surname, id_agency from users where id_users=?";
                PreparedStatement stat_statement = DataBase.connect().prepareStatement(authorize);
                stat_statement.setInt(1, userId);
                ResultSet rs1 = stat_statement.executeQuery();

                if (rs1.next()) {
                    id_agency = rs1.getInt("id_agency");
                    name.setText(rs1.getString("user_name"));
                    lastName.setText(rs1.getString("surname"));
                    u_name=rs1.getString("user_name");
                    u_lastName=rs1.getString("surname");

                }


            } catch (SQLException e) {
                e.printStackTrace();
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
        });

    }

    @FXML
    private Label Name;
    @FXML
    private Label LastName;

    @FXML
    private Pane raporty_pane;

    @FXML
    void EmloyeeKatalogPojazdowButton(ActionEvent event) {

    }

    @FXML
    void EmloyeeRaportyButton(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("scenes/employee/EmployeeRaports.fxml"));
        Parent root=fxmlLoader.load();
        EmployeeReportsController controller = fxmlLoader.getController();
        controller.setU_name(u_name);
        controller.setUlastName(u_lastName);
        Scene menu = new Scene(root);
        Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
        okno.setScene(menu);
        okno.centerOnScreen();
        okno.show();
    }

    @FXML
    void EmloyeeUstawieniaButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/employee/EmployeeSettings.fxml"));
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
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/employee/EmployeeLoans.fxml"));
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
    void PracownikCofnijButton(ActionEvent event) {
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
    void EmployeeLogoutButton(ActionEvent event) throws SQLException {
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
    void PokazButton(ActionEvent event) {
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
    void EmployeeEdytujPojazdButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/employee/EmployeeEditCar.fxml"));
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
    void EmployeeZapiszEdycjePojazduButton(ActionEvent event) {
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


    public void EmployeeDodajSamochodButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/employee/EmployeeAddCar.fxml"));
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
}
