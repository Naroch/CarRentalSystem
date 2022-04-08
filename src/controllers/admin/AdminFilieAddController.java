package controllers.admin;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.DataBase;


import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Kontroler sceny używany z AdminFilieAdd.fxml
 * Wyświetla pola tekstowe wymagane do dodania nowej fili
 */
public class AdminFilieAddController {
    @FXML
    private TextField agencyTextField;

    @FXML
    private TextField roadTextField;

    @FXML
    private TextField bNumberTextField;

    @FXML
    private TextField cityTextField;

    @FXML
    private TextField postTextield;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private Button agencyAddButton;


    /**
     * metoda połaczona z przyciskiem dodania fili
     *
     * @param event
     * @throws SQLException
     */
    @FXML
    void agencyAdd(ActionEvent event) throws SQLException {

        if (agencyTextField.getText().trim().isEmpty()) {
            errorAgency();
        } else if (roadTextField.getText().trim().isEmpty()) {
            errorRoad();
        } else if (bNumberTextField.getText().trim().isEmpty()) {
            errorBNumber();
        } else if (roadTextField.getText().trim().isEmpty()) {
            errorRoad();
        } else if (cityTextField.getText().trim().isEmpty()) {
            errorCity();
        } else if (postTextield.getText().trim().isEmpty()) {
            errorPost();
        } else if (postTextield.getText().trim().isEmpty()) {
            errorEmail();
        } else if (phoneTextField.getText().trim().isEmpty()) {
            errorPhone();
        } else {
            Connection con = DataBase.connect();
            String query = "INSERT INTO agency (agency_name, road, building_number, city, post_code, email, phone_number) VALUES" +
                    "(?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, agencyTextField.getText());
            pst.setString(2, roadTextField.getText());
            pst.setString(3, bNumberTextField.getText());
            pst.setString(4, cityTextField.getText());
            pst.setString(5, postTextield.getText());
            pst.setString(6, emailTextField.getText());
            pst.setString(7, phoneTextField.getText());
            pst.execute();
            con.close();
            Stage stage = (Stage) agencyAddButton.getScene().getWindow();
            stage.close();
        }

    }

    /**
     * wyświetla wiadomość błędu
     */
    private void errorAgency() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText("Podaj nazwę fili!");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/CSS/login.css").toExternalForm());
        dialogPane.getStyleClass().add("alert");
        alert.showAndWait();
    }

    /**
     * wyświetla wiadomość błędu
     */
    private void errorRoad() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText("Podaj nazwę ulicy!");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/CSS/login.css").toExternalForm());
        dialogPane.getStyleClass().add("alert");
        alert.showAndWait();
    }

    /**
     * wyświetla wiadomość błędu
     */
    private void errorBNumber() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText("Podaj numer budynku!");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/CSS/login.css").toExternalForm());
        dialogPane.getStyleClass().add("alert");
        alert.showAndWait();
    }

    /**
     * wyświetla wiadomość błędu
     */
    private void errorCity() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText("Podaj nazwę miasta!");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/CSS/login.css").toExternalForm());
        dialogPane.getStyleClass().add("alert");
        alert.showAndWait();
    }

    /**
     * wyświetla wiadomość błędu
     */
    private void errorPost() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText("Podaj kod pocztowy!");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/CSS/login.css").toExternalForm());
        dialogPane.getStyleClass().add("alert");
        alert.showAndWait();
    }

    /**
     * wyświetla wiadomość błędu
     */
    private void errorEmail() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText("Podaj email!");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/CSS/login.css").toExternalForm());
        dialogPane.getStyleClass().add("alert");
        alert.showAndWait();
    }

    /**
     * wyświetla wiadomość błędu
     */
    private void errorPhone() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText("Podaj telefon!");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/CSS/login.css").toExternalForm());
        dialogPane.getStyleClass().add("alert");
        alert.showAndWait();
    }

}
