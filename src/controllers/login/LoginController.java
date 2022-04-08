package controllers.login;

import controllers.client.ClientController;
import controllers.employee.EmployeeController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import service.DataBase;

import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

/**
 * Stworzono i aktualizowano przez Dmytro Polianychko i Władysław Kurczenko
 *
 * Dany kontroler służy do logowania wszystkich użytkownikow systemu
 */
public class LoginController implements Initializable {

    /**
     * Inicjalizacja zmiennych globalnych dla przechowywania haszowanego hasla użytkownika oraz loginu
     */
    String generatedPassword;
    String login_db;

    /**
     * Inicjalizacja komponentow z pliku Login.fxml
     */
    @FXML
    private TextField login_username;

    @FXML
    private PasswordField login_password;

    @FXML
    private Button cancelButton;

    /**
     * Dana metoda pozwala zamknąć system
     * @param event wykorzysuje się dla obsługi zdarzeń
     */
    @FXML
    public void CancelButton(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Dana metoda jest wykorzystywana dla haszowania hasła użytkownika metodą MD5
     * @return zwraca zahaszowane hasło
     */
    private String hashingPassword() {
        String password = login_password.getText().trim();
        generatedPassword = null;
        try {
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
     * Dana metoda sprawdza czy istnieje login podany przez użytkownika w bazie danych
     * Także dana metoda porównuje hasz hasła w bazie z zahaszowanym hasłem podanym przez użytkownika
     * Na tej podstawie metoda przy pomocy roli zapisanej w bazie dla każdego zarejestrowanego użytkownika
     * Decyduje do jakiej kategorii on należy (Administrator, pracownik, klient)
     * W przeciwnym przypadku jest obsluga blędow oraz jest wyswietlany alertbox
     * @param event
     * @throws IOException służy do obsługi blędów związanych z zapytaniem i nawiązaniem polączenia z bazą
     * @throws SQLException służy do obsługi blędów związanych z bazą danych MariaDB
     */
    @FXML
    public void LoginButton (ActionEvent event) throws IOException, SQLException {
        hashingPassword();
        Statement stmt = DataBase.connect().createStatement();
        String sql = "select id_users, role, login, password from users where login='" + login_username.getText() + "'";
        ResultSet rs = stmt.executeQuery(sql);
        String password_db;
        int role;
        int id;
        if (rs.next()) {
            id = rs.getInt("id_users");
            role = rs.getInt("role");
            login_db = rs.getString("login");
            password_db = rs.getString("password");
            if ((generatedPassword.equals(password_db)) && (role == 0)) {
                FXMLLoader fxmlLoader1 = new FXMLLoader(
                        getClass().getClassLoader().getResource("scenes/client/Client.fxml")
                );
                Parent root = fxmlLoader1.load();
                ClientController controller = fxmlLoader1.getController();
                controller.setUser(id);
                Scene scene = new Scene(root);
                Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
                okno.setScene(scene);
                okno.centerOnScreen();
                okno.show();
            } else if ((generatedPassword.equals(password_db)) && (role == 1)) {
                FXMLLoader fxmlLoader2 = new FXMLLoader(
                        getClass().getClassLoader().getResource("scenes/employee/Employee.fxml")
                );
                Parent root = fxmlLoader2.load();
                EmployeeController controller = fxmlLoader2.getController();
                controller.setUser(id);
                Scene scene = new Scene(root);
                Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
                okno.setScene(scene);
                okno.centerOnScreen();
                okno.show();
            } else if ((generatedPassword.equals(password_db)) && (role == 2)) {
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
            } else {
                errorPassword();
            }
        } else {
            errorPassword();
        }
    }

    /**
     * Dana metoda wykorzysuje się w metodzie ZarejestrujButton w przypadku niepowtarzających haseł
     * Jest wyświetlany alertbox
     */
    private void errorPassword() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Rejestracja");
        alert.setHeaderText(null);
        alert.setContentText("Błędnie wprowadzony login lub hasło! Hasło powinno zawierać conajmniej 8 znaków");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/CSS/login.css").toExternalForm());
        dialogPane.getStyleClass().add("alert");
        alert.showAndWait();
    }

    /**
     *  Dana metoda przenosi użytkownika do sceny rejestracji
     * @param event wykorzysuje się dla obsługi zdarzeń
     */
    @FXML
    public void RejestracjaButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/login/Register.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene menu = new Scene(zmianaSceny);
        Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
        okno.setScene(menu);
        okno.centerOnScreen();
        okno.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}