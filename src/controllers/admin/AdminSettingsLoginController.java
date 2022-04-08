package controllers.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.DataBase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Kontroler sceny używany z AdminSettingsLogin.fxml
 * Nowe okno do edycji loginu
 */
public class AdminSettingsLoginController {
    @FXML
    private TextField loginTextField;

    @FXML
    private PasswordField passwordField;

    private int idUser;

    /**
     * tworzony jest w poprzedniej scenie
     *
     * @param idUser użytkownik do zmiany
     */
    public void AdminSettingsLogin(int idUser) {
        this.idUser = idUser;
    }

    /**
     * metoda wywoływana przez przycisk, zapisuje nowy login do bazy
     *
     * @param event
     */
    @FXML
    void updateButton(ActionEvent event) {
        if (loginTextField.getText().trim().isEmpty()) {
            errorLogin();
        } else {
            try {
                Connection con = DataBase.connect();
                ResultSet rs = con.createStatement().executeQuery("SELECT password FROM users WHERE id_users = " + idUser);
                while (rs.next()) {
                    if (hashingPassword(passwordField).equals(rs.getString(1))) {
                        String query = "UPDATE users SET login=? WHERE id_users = " + idUser;
                        PreparedStatement pst = con.prepareStatement(query);
                        pst.setString(1, loginTextField.getText());
                        pst.executeUpdate();

                        updateSucces();
                        Stage stage = (Stage) loginTextField.getScene().getWindow();
                        stage.close();
                    } else
                        updateFail();
                }
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /**
     * Metoda hashuje hasło i zwraca jego zahashowaną wersję
     *
     * @param Haslo1TextField tekst do hasowania
     * @return zahashowany String
     */
    private String hashingPassword(PasswordField Haslo1TextField) {
        String password = Haslo1TextField.getText().trim();
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    /**
     * wyświetla wiadomość błedu
     */
    private void errorLogin() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ustawienia");
        alert.setHeaderText(null);
        alert.setContentText("Nie podałeś loginu!");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/CSS/login.css").toExternalForm());
        dialogPane.getStyleClass().add("alert");
        alert.showAndWait();
    }

    /**
     * wyświetla wiadomość błedu
     */
    private void updateFail() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ustawienia");
        alert.setHeaderText(null);
        alert.setContentText("Nieprawidłowe hasło");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/CSS/login.css").toExternalForm());
        dialogPane.getStyleClass().add("alert");
        alert.showAndWait();
    }

    /**
     * wyświetla wiadomość błedu
     */
    private void updateSucces() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ustawienia");
        alert.setHeaderText(null);
        alert.setContentText("Zmiana ustawień powiodła się");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/CSS/login.css").toExternalForm());
        dialogPane.getStyleClass().add("alert");
        alert.showAndWait();
    }
}
