package controllers.client;

import controllers.admin.AdminLoansController;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import models.Wypozyczenia;
import service.DataBase;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ClientLoansController implements Initializable{
    @FXML
    private TableView<Wypozyczenia> table_wypozyczenia;
    public static int loan_id;
    public static int car_id;
    private Component frame;

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
    private TextField numerField;
    @FXML
    private javafx.scene.control.Label name;

    @FXML
    private Label lastName;

    @FXML
    void ClientKatalogPojazdowButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/client/Client.fxml"));
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
    void ClientRentButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/client/ClientLoans.fxml"));
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
    void ClientCofnijButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/client/Client.fxml"));
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
    void ClientSettings(ActionEvent event) {
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


    /**
     * Anuluje rezerwacje, jeśli status wypożyczenia to 'Rezerwacja' ,
     * w innym przypadku wyskoczy okienko, że nie da się anulować.
     *
     * @param event do obsługi zdarzeń
     */
    @FXML
    void cancelReservationButton(ActionEvent event){

        String status;

        car_id = table_wypozyczenia.getSelectionModel().getSelectedItem().getId_car();
        loan_id = table_wypozyczenia.getSelectionModel().getSelectedItem().getId();
        status = table_wypozyczenia.getSelectionModel().getSelectedItem().getStatus();

        System.out.println(status);
        System.out.println(car_id);

        if(status.equals("Rezerwacja")) {
            try {
                Connection connection = DataBase.connect();
                String query = "delete from loans where id_loan = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, loan_id);
                preparedStatement.execute();

                String query2 = "update cars set status = ? where id_car = ?";
                PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
                String status3 = "Dostępny";
                preparedStatement2.setString(1, status3);
                preparedStatement2.setInt(2,car_id);
                preparedStatement2.execute();


                JOptionPane.showMessageDialog(frame, "Anulowano rezerwacje");


            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }else {
            JOptionPane.showMessageDialog(frame, "Anulować można tylko rezerwacje");
        }
    }


    /**
     * Wyświetla szczegóły wypożyczenia, które jest wybrane w tabelview
     * @param event
     */
    @FXML
    void detailsButton(ActionEvent event){
        int index = table_wypozyczenia.getSelectionModel().getFocusedIndex();
        Wypozyczenia wypozyczenia = table_wypozyczenia.getItems().get(index);
        loan_id = wypozyczenia.getId();

        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/client/ClientLoansDetails.fxml"));
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

        name.setText(ClientController.String_name);
        lastName.setText(ClientController.String_lastName);



        try {
            Statement stmt = DataBase.connect().createStatement();
            String sql = "SELECT l.id_car, l.id_loan, id_client, u.user_name, u.surname, l.date_of_loan, l.date_of_delivery," +
                    " a.city, c.model, l.status, l.cost FROM agency a, loans l, cars c," +
                    " users u WHERE l.id_client = u.id_users AND l.id_car = c.id_car AND a.id_agency = l.agency_of_loan " +
                    "AND id_client =" + ClientController.userId + ";";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                list.add(new Wypozyczenia(
                        rs.getInt("id_car"),
                        rs.getInt("id_loan"),
                        rs.getInt("id_loan"),
                        rs.getString("user_name"),
                        rs.getString("surname"),
                        rs.getString("date_of_loan"),
                        rs.getString("date_of_delivery"),
                        rs.getString("city"),
                        rs.getString("model"),
                        rs.getString("status"),
                        rs.getDouble("cost")));
            }


        } catch (SQLException ex) {

        }

        //Wypełnianie tabeli

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



        FilteredList<Wypozyczenia> filterlist = new FilteredList<>(list, p -> true);

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