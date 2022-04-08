package controllers.admin;

import java.net.URL;
import java.sql.*;

import controllers.employee.EmployeeController;
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
import java.sql.Connection;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static java.sql.DriverManager.getConnection;

public class AdminController implements Initializable {

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
    public static String status;
    ObservableList<Cars> oblist = FXCollections.observableArrayList();

    public static int userId = 1, id_agency;

    /**
     * Wyświetlanie katalogu aut
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println(userId);

        Platform.runLater(() -> {

            try {
                String authorize = "select user_name, surname, id_agency from users where id_users=?";
                PreparedStatement stat_statement = DataBase.connect().prepareStatement(authorize);
                stat_statement.setInt(1, userId);
                ResultSet rs1 = stat_statement.executeQuery();

                if (rs1.next()) {
                    id_agency = rs1.getInt("id_agency");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        try {
            Connection connection = DataBase.connect();

            ResultSet rs = connection.createStatement().executeQuery("select * from cars");

            while (rs.next()) {
                oblist.add(new Cars(rs.getInt("id_car"), rs.getString("model"),
                        rs.getInt("production_date"), rs.getString("engine"),
                        rs.getInt("power"), rs.getString("fuel"),
                        rs.getString("transmission"), rs.getString("color"),
                        rs.getInt("cost"),
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

        cars_table.setOnMouseClicked((event) -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && (! oblist.isEmpty())) {
                int index = cars_table.getSelectionModel().getFocusedIndex();
                Cars cars = cars_table.getItems().get(index);
                id = cars.getId();
                status = cars.getStatus();

                Parent zmianaSceny = null;
                try {
                    zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/admin/AdminShowCar.fxml"));
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

    }


    @FXML
    void szczegoly(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/admin/AdminShowCar.fxml"));
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
    void AdminEdytujPojazdButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/admin/EditCar.fxml"));
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
    void AdminZapiszEdycjePojazduButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/admin/AdminShowCar.fxml"));
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
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/admin/AdminShowCar.fxml"));
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
    void AdminDodajSamochodButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/admin/AddCar.fxml"));
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
    public void AdminWybierzZdjecieButton(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.IMAGE", "jpg", "gif", "png");
        fileChooser.addChoosableFileFilter(filter);
        int result = fileChooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String path = selectedFile.getAbsolutePath();
        } else if (result == JFileChooser.CANCEL_OPTION) {
            System.out.println("No Data");
        }
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
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/admin/Raports.fxml"));
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

