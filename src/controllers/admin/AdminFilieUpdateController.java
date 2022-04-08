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
import models.Agency;
import service.DataBase;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Kontroler sceny używany z AdminFilieUpdate.fxml
 * Wypełnia pola tekstowe wymagane do akutalizacji, danymi podanej fili
 */
public class AdminFilieUpdateController implements Initializable {
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
    private Button agnecyUpdateButton;

    private Agency agency;

    public void AdminFilieUpdate(Agency agency) {
        this.agency = agency;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(this::run
        );
    }


    /**
     * metoda wywoływna przez przycisk, akutalizuje filię
     *
     * @param event
     * @throws SQLException
     */
    @FXML
    void agencyUpdate(ActionEvent event) throws SQLException {

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
        } else if (phoneTextField.getText().trim().isEmpty()) {
            errorPhone();
        } else {
            Connection con = DataBase.connect();
            String query = "UPDATE agency SET agency_name=?, road=?, building_number=?, city=?, post_code=?, email=?, phone_number=? WHERE id_agency = " + agency.getIdAgency();
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, agencyTextField.getText());
            pst.setString(2, roadTextField.getText());
            pst.setString(3, bNumberTextField.getText());
            pst.setString(4, cityTextField.getText());
            pst.setString(5, postTextield.getText());
            pst.setString(6, emailTextField.getText());
            pst.setString(7, phoneTextField.getText());
            pst.executeUpdate();
            con.close();
            Stage stage = (Stage) agnecyUpdateButton.getScene().getWindow();
            stage.close();
        }
    }

    /**
     * metoda wypełniająca pola tekstowe danymi podanej fili wywoływana przy starcie
     */
    private void run() {
        try {
            Connection con = DataBase.connect();

            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM agency where id_agency = " + agency.getIdAgency());

            while (rs.next()) {
                agencyTextField.setText(rs.getString("agency_name"));
                roadTextField.setText(rs.getString("road"));
                bNumberTextField.setText(rs.getString("building_number"));
                cityTextField.setText(rs.getString("city"));
                postTextield.setText(rs.getString("post_code"));
                emailTextField.setText(rs.getString("email"));
                phoneTextField.setText(rs.getString("phone_number"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * wyświetla wiadomość błedu
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
     * wyświetla wiadomość błedu
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
     * wyświetla wiadomość błedu
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
     * wyświetla wiadomość błedu
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
     * wyświetla wiadomość błedu
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
     * wyświetla wiadomość błedu
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
     * wyświetla wiadomość błedu
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
