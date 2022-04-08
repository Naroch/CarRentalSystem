package controllers.admin;

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
import javafx.stage.Stage;
import models.Wypozyczenia;
import service.DataBase;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class AdminLoansController implements Initializable{
    @FXML
    private TableView<Wypozyczenia> table_wypozyczenia;
    public static int loan_id;
    public static int car_id;

    @FXML
    private TableColumn<Wypozyczenia, String> numerColumn;
    @FXML
    private TableColumn<Wypozyczenia, String> imieColumn;
    @FXML
    private TableColumn<Wypozyczenia, String> nazwiskoColumn;
    @FXML
    private TableColumn<Wypozyczenia, String> data2Column;
    @FXML
    private TableColumn<Wypozyczenia, String> dataColumn;
    @FXML
    private TableColumn<Wypozyczenia, String> filiaColumn;
    @FXML
    private TableColumn<Wypozyczenia, String> statusColumn;
    @FXML
    private TableColumn<Wypozyczenia, String> pojazdColumn;
    @FXML
    private TableColumn<Wypozyczenia, String> cenaColumn;
    @FXML
    private ChoiceBox filiaBox;
    @FXML
    private ChoiceBox statusBox;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField numerField;

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
    void AdminProtokolyButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/admin/AdminLoansDetails.fxml"));
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



    ObservableList<Wypozyczenia> list = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            Statement stmt = DataBase.connect().createStatement();
            String sql = "SELECT l.id_loan, l.id_car, u.user_name, u.surname, l.date_of_loan, l.date_of_delivery," +
                    " a.agency_name, c.model, l.status, l.cost FROM agency a, loans l, cars c," +
                    " users u WHERE l.id_client = u.id_users AND l.id_car = c.id_car AND a.id_agency = l.agency_of_loan;";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                list.add(new Wypozyczenia(rs.getInt("id_car"),rs.getInt("id_loan"), rs.getInt("id_loan"), rs.getString("user_name"), rs.getString("surname"), rs.getString("date_of_loan"), rs.getString("date_of_delivery"), rs.getString("agency_name"), rs.getString("model"), rs.getString("status"), rs.getDouble("cost")));
            }

        } catch (SQLException ex) {

        }


        /**
         * Wypełnienianie tabeli danymi z bazy po włączeniu
         */
        numerColumn.setCellValueFactory(new PropertyValueFactory<>("numer"));
        imieColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nazwiskoColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        dataColumn.setCellValueFactory(new PropertyValueFactory<>("date_of_loan"));
        data2Column.setCellValueFactory(new PropertyValueFactory<>("date_of_delivery"));
        filiaColumn.setCellValueFactory(new PropertyValueFactory<>("agency"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        pojazdColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        cenaColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        table_wypozyczenia.setItems(list);

        /**
         * Przejscie do szczegółów wypożyczenia po naciśnieciu w tabeli
         */
        table_wypozyczenia.setOnMouseClicked((event) -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && (! list.isEmpty())) {
                int index = table_wypozyczenia.getSelectionModel().getFocusedIndex();
                Wypozyczenia wypozyczenia = table_wypozyczenia.getItems().get(index);
                loan_id = wypozyczenia.getId();
                car_id = wypozyczenia.getId_car();

                Parent zmianaSceny = null;
                try {
                    zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/admin/AdminLoansDetails.fxml"));
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

        /**
         * Wyszukiwarka po Nazwisku klienta
         */
        FilteredList<Wypozyczenia> filterlist = new FilteredList<>(list, p -> true);
        surnameField.textProperty().addListener((observable, oldValue, newValue) ->{
            filterlist.setPredicate(Wypozyczenia -> {
                if(newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return String.valueOf(Wypozyczenia.getSurname()).toLowerCase().contains(lowerCaseFilter);
            });
        });

        /**
         * Wyszukiwarka po numerze wypożyczenia
         */
        numerField.textProperty().addListener((observable1, oldValue, newValue) ->{
            filterlist.setPredicate(Wypozyczenia -> {
                if(newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return String.valueOf(Wypozyczenia.getNumer()).toLowerCase().contains(lowerCaseFilter);
            });
        });

        /**
         * Sortowanie po fili za pomocą choicebox
         */
        filiaBox.getSelectionModel().selectedItemProperty().addListener((observable2, oldValue, newValue) -> {
            filterlist.setPredicate(Wypozyczenia -> {
                if(newValue == null) {
                    return true;
                }
                String help = newValue.toString();
                if (String.valueOf(Wypozyczenia.getAgency()).contains(help)) {
                    return true;
                }else return help.equals("Wszystkie");
            });
        });

        /**
         * Sortowanie po statusie za pomocą choicebox
         */
        statusBox.getSelectionModel().selectedItemProperty().addListener((observable3, oldValue, newValue) -> {
            filterlist.setPredicate(Wypozyczenia -> {
                if(newValue == null) {
                    return true;
                }
                String help = newValue.toString();
                if (String.valueOf(Wypozyczenia.getStatus()).contains(help)) {
                    return true;
                }else return help.equals("Wszystko");
            });
        });

        SortedList<Wypozyczenia> sorted = new SortedList<>(filterlist);
        sorted.comparatorProperty().bind(table_wypozyczenia.comparatorProperty());
        table_wypozyczenia.setItems(sorted);
    }
}