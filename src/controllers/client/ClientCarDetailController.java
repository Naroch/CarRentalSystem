package controllers.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientCarDetailController implements Initializable {

    String u_name;
    String u_lastName;

    public void setU_name(String u_name) {
        this.u_name = u_name;
    }

    public void setUlastName(String u_lastName) {
        this.u_lastName = u_lastName;
    }

    @FXML
    private Label namee;
    @FXML
    private Label lastNamee;


    @FXML
    private TableView table_wypozyczenia;

    @FXML
    private TableColumn numerColumn;
    @FXML
    private TableColumn imienazwiskoColumn;
    @FXML
    private TableColumn peselColumn;
    @FXML
    private TableColumn dataColumn;
    @FXML
    private TableColumn filiaColumn;
    @FXML
    private TableColumn statusColumn;
    @FXML
    private TableColumn pojazdColumn;
    @FXML
    private TableColumn buttonColumn;

    @FXML
    void Back(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/Client.fxml"));
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
    void Rent_Car_Client_Button(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/ClientRent.fxml"));
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
    void client_back(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getClassLoader().getResource("scenes/Client.fxml")
        );
        Parent root = fxmlLoader.load();
        ClientController controller = fxmlLoader.getController();
        controller.setU_name(u_name);
        controller.setUlastName(u_lastName);


        Scene menu = new Scene(root);
        Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
        okno.setScene(menu);
        okno.centerOnScreen();
        okno.show();
    }


    @FXML
    void log_out(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/Login.fxml"));
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

    }

    @FXML
    void AdminUstawieniaButton(ActionEvent event) {

    }

    @FXML
    void AdminUzytkownicyButton(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {




/*
        numerColumn.setCellValueFactory(new PropertyValueFactory<Wypozyczenia, Integer>("numer"));
        imienazwiskoColumn.setCellValueFactory(new PropertyValueFactory<Wypozyczenia, String>("imie_nazwisko"));
        peselColumn.setCellValueFactory(new PropertyValueFactory<Wypozyczenia, String>("pesel"));
        dataColumn.setCellValueFactory(new PropertyValueFactory<Wypozyczenia, String>("data"));
        filiaColumn.setCellValueFactory(new PropertyValueFactory<Wypozyczenia, String>("filia"));
        pojazdColumn.setCellValueFactory(new PropertyValueFactory<Wypozyczenia, String>("pojazd"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<Wypozyczenia, String>("status"));
        buttonColumn.setCellValueFactory(new PropertyValueFactory<Wypozyczenia, Button>("button"));*/


//        table_wypozyczenia.setItems(data);

        Platform.runLater(() -> {
            namee.setText(u_name);
            lastNamee.setText(u_lastName);
        });

    }
}
