package controllers.admin;

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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.Agency;
import controllers.admin.AdminFilieUpdateController;
import models.Cars;
import models.User;
import service.DataBase;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;


/**
 * Kontroler sceny używany z AdminFilie.fxml
 * Wyświetla TableView z Filiami wraz z opcjami edycji
 */
public class AdminFilieController implements Initializable {


    @FXML
    private TableView tableView;

    @FXML
    private TableColumn filiaCollumn;

    @FXML
    private TableColumn ulicaCollumn;

    @FXML
    private TableColumn numerBudynkuCollumn;

    @FXML
    private TableColumn miastoCollumn;

    @FXML
    private TableColumn kodPocztowyCollumn;

    @FXML
    private TableColumn emailCollumn;

    @FXML
    private TableColumn telefonCollumn;

    @FXML
    private TextField modelTextField;

    ObservableList<Agency> data = FXCollections.observableArrayList();

    Stage stage = new Stage();

    /**
     * Wypełnia TableView Filiami
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            Connection con = DataBase.connect();

            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM agency");

            while (rs.next()) {
                data.add(new Agency(rs.getInt("id_agency"), rs.getString("agency_name"), rs.getString("road"), rs.getString("building_number"),
                        rs.getString("city"), rs.getString("post_code"), rs.getString("email"), rs.getString("phone_number")));
            }
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        filiaCollumn.setCellValueFactory(new PropertyValueFactory<Agency, String>("filia"));
        ulicaCollumn.setCellValueFactory(new PropertyValueFactory<Agency, String>("ulica"));
        numerBudynkuCollumn.setCellValueFactory(new PropertyValueFactory<Agency, String>("numerBudynku"));
        miastoCollumn.setCellValueFactory(new PropertyValueFactory<Agency, String>("miasto"));
        kodPocztowyCollumn.setCellValueFactory(new PropertyValueFactory<Agency, String>("kodPocztowy"));
        emailCollumn.setCellValueFactory(new PropertyValueFactory<Agency, String>("adresEmail"));
        telefonCollumn.setCellValueFactory(new PropertyValueFactory<Agency, String>("telefon"));

        tableView.setItems(data);
        tableViewFiltering();
    }

    /**
     * metoda tworzy popUp okno do dodawania fili
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void addButton(ActionEvent event) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getClassLoader().getResource("scenes/admin/AdminFilieAdd.fxml")
        );
        Parent root = fxmlLoader.load();


        stage.setScene(new Scene(root));
        stage.showAndWait();
        refresh();
    }

    /**
     * metoda tworzy popUp okno do usuwania fili
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void removeButton(ActionEvent event) throws IOException {
        TableView.TableViewSelectionModel<Agency> selectionModel = tableView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        ObservableList<Agency> selectedItems = selectionModel.getSelectedItems();
        if (!selectedItems.isEmpty()) {
            int countRows = 0;

            try {
                Connection con = DataBase.connect();
                ResultSet rs = con.createStatement().executeQuery("SELECT COUNT(id_agency) FROM users WHERE id_agency = " + selectedItems.get(0).getIdAgency());
                while (rs.next()) countRows += rs.getInt(1);
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            try {
                Connection con = DataBase.connect();
                ResultSet rs = con.createStatement().executeQuery("SELECT COUNT(id_loan) FROM loans WHERE agency_of_loan = " + selectedItems.get(0).getIdAgency()
                        + " OR agency_of_delivery = " + selectedItems.get(0).getIdAgency());
                while (rs.next()) countRows += rs.getInt(1);
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            try {
                Connection con = DataBase.connect();
                ResultSet rs = con.createStatement().executeQuery("SELECT COUNT(id_agency) FROM cars WHERE id_agency = " + selectedItems.get(0).getIdAgency());
                while (rs.next()) countRows += rs.getInt(1);
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            System.out.println(countRows);
            if (selectedItems.get(0).getIdAgency() == 1) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Nie można usunąć podstawowej fili");
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStyleClass().add("alert");
                alert.showAndWait();
            } else if (countRows == 0) {
                if (confirmationEmpty(countRows))
                    try {
                        Connection con = DataBase.connect();

                        String query = "DELETE FROM agency WHERE id_agency = " + selectedItems.get(0).getIdAgency();
                        PreparedStatement pst = con.prepareStatement(query);
                        pst.execute();
                        refresh();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
            } else {
                if (!selectedItems.isEmpty()) {
                    FXMLLoader fxmlLoader = new FXMLLoader(
                            getClass().getClassLoader().getResource("scenes/admin/AdminFilieRemove.fxml")
                    );
                    Parent root = fxmlLoader.load();
                    AdminFilieRemoveController controller = fxmlLoader.getController();


                    controller.AdminFilieRemove(selectedItems.get(0).getIdAgency(), countRows);


                    stage.setScene(new Scene(root));
                    stage.showAndWait();
                    refresh();
                }
            }
        }
    }

    /**
     * wyświetla alert potwierdzenia
     *
     * @param a - number of records
     * @return true jeśli przyciśniemy ButtonType.OK w innym przypadku zwraca false
     */
    private boolean confirmationEmpty(int a) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Czy jesteś pewien że chcesz usunąć tą pustą filię?");

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    /**
     * metoda tworzy popUp okno do aktualizowania fili
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void updateButon(ActionEvent event) throws IOException {

        TableView.TableViewSelectionModel<Agency> selectionModel = tableView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        ObservableList<Agency> selectedItems = selectionModel.getSelectedItems();
        if (!selectedItems.isEmpty()) {
            if (selectedItems.get(0).getIdAgency() == 1) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Nie można zmieniać podstawowej fili");
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStyleClass().add("alert");
                alert.showAndWait();
            }else {
                FXMLLoader fxmlLoader = new FXMLLoader(
                        getClass().getClassLoader().getResource("scenes/admin/AdminFilieUpdate.fxml")
                );
                Parent root = fxmlLoader.load();
                AdminFilieUpdateController controller = fxmlLoader.getController();


                controller.AdminFilieUpdate(selectedItems.get(0));


                stage.setScene(new Scene(root));
                stage.showAndWait();
                refresh();
            }
        }
    }

    /**
     * funkcja filtrująca po znakach wprowadzonych w polu tekstowym nad TableView
     */
    void tableViewFiltering() {

        FilteredList<Agency> filterlist = new FilteredList<>(data, p -> true);

        modelTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterlist.setPredicate(Agency -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(Agency.getFilia()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (String.valueOf(Agency.getUlica()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (String.valueOf(Agency.getMiasto()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (String.valueOf(Agency.getNumerBudynku()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (String.valueOf(Agency.getKodPocztowy()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (String.valueOf(Agency.getAdresEmail()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else return String.valueOf(Agency.getTelefon()).toLowerCase().indexOf(lowerCaseFilter) != -1;
            });
        });

        SortedList<Agency> sorted = new SortedList<>(filterlist);
        sorted.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sorted);
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
     * metoda używana przez przycisk do odświerzenia TableView
     *
     * @param event
     */
    @FXML
    void refreshButton(ActionEvent event) {
        refresh();
    }

    /**
     * metoda odświerzająca TableView
     */
    public void refresh() {
        data.remove(0, data.size());
        tableView.setItems(data);
        try {
            Connection con = DataBase.connect();

            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM agency");

            while (rs.next()) {
                data.add(new Agency(rs.getInt("id_agency"), rs.getString("agency_name"), rs.getString("road"), rs.getString("building_number"),
                        rs.getString("city"), rs.getString("post_code"), rs.getString("email"), rs.getString("phone_number")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        tableView.setItems(data);
        tableViewFiltering();
    }
}
