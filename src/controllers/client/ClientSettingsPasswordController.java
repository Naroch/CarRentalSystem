package controllers.client;

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
 * Kontroler sceny używany z ClientSettingsPassword.fxml
 * Nowe okno do edycji hasła
 */
public class ClientSettingsPasswordController {
    @FXML
    private PasswordField oldPassword;

    @FXML
    private PasswordField newPassword;

    @FXML
    private PasswordField newPassword2;

    private int idUser;

    /**
     * @param idUser użytkownik do zmiany
     *               tworzony jest w poprzedniej scenie
     */
    public void AdminSettingsPassword(int idUser) {
        this.idUser = idUser;
    }

    /**
     * metoda wywoływana przez przycisk, zapisuje nowy hasło do bazy
     *
     * @param event
     */
    @FXML
    void updateButton(ActionEvent event) {
        if (passwordCheck(newPassword) == 0) {
            errorLengthPassword();
        } else if (!newPassword.getText().trim().equals(newPassword2.getText().trim())) {
            errorPassword();
        } else {
            try {
                Connection con = DataBase.connect();
                ResultSet rs = con.createStatement().executeQuery("SELECT password FROM users WHERE id_users = " + idUser);
                while (rs.next()) {
                    if (hashingPassword(oldPassword).equals(rs.getString(1))) {
                        String query = "UPDATE users SET password=? WHERE id_users = " + idUser;
                        PreparedStatement pst = con.prepareStatement(query);
                        pst.setString(1, hashingPassword(newPassword));
                        pst.executeUpdate();
                        updateSucces();
                        Stage stage = (Stage) newPassword2.getScene().getWindow();
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
     * Metoda sprawdzania podanego przez użytkownika hasła
     * Jeżeli hasło zawiera conajmniej 8 znaków to zwracamy 1
     * W przeciwnym przypadku zwracamy 0
     * Dana metoda jest wykorzystana w ZarejestrujButton
     *
     * @return zwraca 0 albo 1
     */
    public int passwordCheck(TextField Haslo1TextField) {
        int a;
        String haslo = Haslo1TextField.getText().trim();
        boolean valid = haslo.length() >= 8;
        if (!valid) {
            a = 0;
        } else {
            a = 1;
        }
        return a;
    }

    /**
     * Dana metoda wykorzysuje się w metodzie updateButton w przypadku blędnie podanego hasła
     * Jest wyświetlany alertbox
     */
    private void errorLengthPassword() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Hasło musi mieć co najmniej 8 znaków.");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStyleClass().add("alert");
        alert.showAndWait();
    }

    /**
     * Dana metoda wykorzysuje się w metodzie updateButton w przypadku niepowtarzających haseł
     * Jest wyświetlany alertbox
     */
    private void errorPassword() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ustawienia");
        alert.setHeaderText(null);
        alert.setContentText("Wprowadzone hasła nie są jednakowe!");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/CSS/login.css").toExternalForm());
        dialogPane.getStyleClass().add("alert");
        alert.showAndWait();
    }

    /**
     * Dana metoda wykorzysuje się w metodzie updateButton w przypadku udanej zmiany hasła
     * Jest wyświetlany alertbox
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

    /**
     * Dana metoda wykorzysuje się w metodzie updateButton w przypadku nieudanej zmiany hasła
     * Jest wyświetlany alertbox
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

}
