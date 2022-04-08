package controllers.admin;

import connection.conn;
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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.Cars;
import models.User;
import service.DataBase;


import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Kontroler sceny używany z AdminUser.fxml
 * Wyświetla TableView z wszystkimi użytkownikami z przyciskiem do ich edycji
 */
public class AdminUsersController implements Initializable {

    @FXML
    private TableView tableView;

    @FXML
    private TableColumn imieColumn;

    @FXML
    private TableColumn nazwiskoColumn;

    @FXML
    private TableColumn loginColumn;

    @FXML
    private TableColumn hasloColumn;

    @FXML
    private TableColumn filiaColumn;

    @FXML
    private TableColumn rolaColumn;

    @FXML
    private TableColumn telefonColumn;

    @FXML
    private TableColumn emailColumn;

    @FXML
    private TableColumn dokumentColumn;

    @FXML
    private TableColumn active;

    @FXML
    private Button refreshButton;

    @FXML
    private TextField modelTextField;

    Stage stage = new Stage();

    ObservableList<User> data = FXCollections.observableArrayList();

    /**
     * wypełnia TableView użytkownikami z bazy danych
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        try {
            Connection con = DataBase.connect();

            ResultSet rs = con.createStatement().executeQuery("SELECT u.*, a.agency_name FROM users u LEFT JOIN agency a ON u.id_agency = a.id_agency");
            String document;
            String rola;
            String activeString;

            while (rs.next()) {
                if (rs.getString("document").isEmpty()) document = "Brak";
                else
                    document = "zapisany";

                if (rs.getInt("role") == 0) rola = "Klient";
                else if (rs.getInt("role") == 1) rola = "Pracownik";
                else rola = "Administrator";

                if (rs.getBoolean("active") == true) activeString = "Tak";
                else activeString = "Nie";

                data.add(new User(rs.getInt("id_users"), rs.getString("user_name"), rs.getString("surname"),
                        rs.getString("login"), rs.getString("password"), rs.getInt("id_agency"), rs.getString("agency_name"), rs.getInt("role"), rola, rs.getString("phone_number"), rs.getString("email"),
                        document, rs.getBoolean("active"), activeString));
            }
        } catch (SQLException ex) {

        }

        imieColumn.setCellValueFactory(new PropertyValueFactory<User, String>("imie"));
        nazwiskoColumn.setCellValueFactory(new PropertyValueFactory<User, String>("nazwisko"));
        loginColumn.setCellValueFactory(new PropertyValueFactory<User, String>("login"));
        hasloColumn.setCellValueFactory(new PropertyValueFactory<User, String>("haslo"));
        rolaColumn.setCellValueFactory(new PropertyValueFactory<User, String>("rolaString"));
        filiaColumn.setCellValueFactory(new PropertyValueFactory<User, String>("filia"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        telefonColumn.setCellValueFactory(new PropertyValueFactory<User, String>("telefon"));
        dokumentColumn.setCellValueFactory(new PropertyValueFactory<User, String>("dokument"));
        active.setCellValueFactory(new PropertyValueFactory<User, String>("aktywnyString"));
        tableView.setItems(data);
        tableViewFiltering();
    }

    /**
     * funkcja filtrująca po znakach wprowadzonych w polu tekstowym nad TableView
     */
    void tableViewFiltering() {

        FilteredList<User> filterlist = new FilteredList<>(data, p -> true);

        modelTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterlist.setPredicate(Users -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(Users.getImie()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (String.valueOf(Users.getNazwisko()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (String.valueOf(Users.getEmail()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (String.valueOf(Users.getFilia()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (String.valueOf(Users.getTelefon()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (String.valueOf(Users.getLogin()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else return String.valueOf(Users.getAktywnyString()).toLowerCase().indexOf(lowerCaseFilter) != -1;
            });
        });

        SortedList<User> sorted = new SortedList<>(filterlist);
        sorted.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sorted);
    }

    /**
     * metoda wywoływna przez przycisk, otwiera nowe okno do edycji użytkownika
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void UpdateTableView(ActionEvent event) throws IOException {

        TableView.TableViewSelectionModel<User> selectionModel = tableView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        ObservableList<User> selectedItems = selectionModel.getSelectedItems();
        if (!selectedItems.isEmpty()) {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    getClass().getClassLoader().getResource("scenes/admin/AdminUserEdit.fxml")
            );
            Parent root = fxmlLoader.load();
            AdminUserEditController controller = fxmlLoader.getController();


            controller.AdminUserEdit(selectedItems.get(0));


            stage.setScene(new Scene(root));
            stage.showAndWait();
            refresh();

            //DELETE ELEMENT FROM FOLDER
            Path imagesPath = Paths.get(
                    "images/photoPass.jpg");

            try {
                Files.delete(imagesPath);
                System.out.println("File "
                        + imagesPath.toAbsolutePath().toString()
                        + " successfully removed");
            } catch (IOException e) {
                System.err.println("Unable to delete "
                        + imagesPath.toAbsolutePath().toString()
                        + " due to...");
                e.printStackTrace();
            }

        }

    }

    /**
     * metoda zmienia scenę na zakładkę Katalog
     *
     * @param event
     */
    @FXML
    void AdminKatalogPojazdowButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/admin/Admin.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.close();
        Scene menu = new Scene(zmianaSceny);
        Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
        okno.setScene(menu);
        okno.centerOnScreen();
        okno.show();
    }

    /**
     * metoda zmienia scenę na ekran logowania
     *
     * @param event
     */
    @FXML
    void AdminLogoutButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/login/Login.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.close();
        Scene menu = new Scene(zmianaSceny);
        Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
        okno.setScene(menu);
        okno.centerOnScreen();
        okno.show();
    }

    /**
     * metoda zmienia scenę na zakładkę Raporty
     *
     * @param event
     */
    @FXML
    void AdminRaportyButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/admin/Raports.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.close();
        Scene menu = new Scene(zmianaSceny);
        Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
        okno.setScene(menu);
        okno.centerOnScreen();
        okno.show();
    }

    /**
     * metoda zmienia scenę na zakładkę Ustawień
     *
     * @param event
     */
    @FXML
    void AdminUstawieniaButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/admin/AdminSettings.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.close();
        Scene menu = new Scene(zmianaSceny);
        Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
        okno.setScene(menu);
        okno.centerOnScreen();
        okno.show();
    }

    /**
     * metoda zmienia scenę na zakładkę Uzytkownicy
     *
     * @param event
     */
    @FXML
    void AdminUzytkownicyButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/admin/AdminUsers.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.close();
        Scene menu = new Scene(zmianaSceny);
        Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
        okno.setScene(menu);
        okno.centerOnScreen();
        okno.show();
    }

    /**
     * metoda zmienia scenę na zakładkę Wypożyczenia
     *
     * @param event
     */
    @FXML
    void AdminWypozyczeniaKlientowButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/admin/AdminLoans.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.close();
        Scene menu = new Scene(zmianaSceny);
        Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
        okno.setScene(menu);
        okno.centerOnScreen();
        okno.show();
    }

    /**
     * metoda zmienia scenę na zakładkę Filie
     *
     * @param event
     */
    @FXML
    void AdminFilieButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/admin/AdminFilie.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.close();
        Scene menu = new Scene(zmianaSceny);
        Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
        okno.setScene(menu);
        okno.centerOnScreen();
        okno.show();
    }

    /**
     * metoda wywoływna przez przycisk, odświerza tableView
     *
     * @param event
     */
    @FXML
    void refreshButton(ActionEvent event) {
        refresh();
    }

    /**
     * metoda odświerza tableView
     */
    void refresh() {

        data.remove(0, data.size());
        tableView.setItems(data);
        try {
            Connection con = DataBase.connect();

            ResultSet rs = con.createStatement().executeQuery("SELECT u.*, a.agency_name FROM users u LEFT JOIN agency a ON u.id_agency = a.id_agency");
            String document;
            String rola;
            String activeString;
            while (rs.next()) {
                if (rs.getString("document").isEmpty()) document = "Brak";
                else
                    document = "zapisany";

                if (rs.getInt("role") == 0) rola = "Klient";
                else if (rs.getInt("role") == 1) rola = "Pracownik";
                else rola = "Administrator";

                if (rs.getBoolean("active") == true) activeString = "Tak";
                else activeString = "Nie";

                data.add(new User(rs.getInt("id_users"), rs.getString("user_name"), rs.getString("surname"),
                        rs.getString("login"), rs.getString("password"), rs.getInt("id_agency"), rs.getString("agency_name"), rs.getInt("role"), rola, rs.getString("phone_number"), rs.getString("email"),
                        document, rs.getBoolean("active"), activeString));
            }
        } catch (SQLException ex) {

        }
        tableView.setItems(data);
        tableViewFiltering();
    }

}
