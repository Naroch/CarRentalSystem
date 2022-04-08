package controllers.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import service.DataBase;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Kontroler sceny używany z AdminSettings.fxml
 * Zakładka Ustawień
 */
public class ClientSettingsController implements Initializable {

    @FXML
    private TextField imieTextField;

    @FXML
    private TextField nazwiskoTextField;

    @FXML
    private TextField telefonTextField;

    @FXML
    private TextField emialTextField;

    @FXML
    private PasswordField hasloField;

    @FXML
    private TextField loginTextField;

    @FXML
    private Label name;

    @FXML
    private Label lastName;

    private final int idUser = ClientController.userId;

    /**
     * wypenia pola danymi uzytkownika z bazy
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name.setText(ClientController.String_name);
        lastName.setText(ClientController.String_lastName);

        try {
            Connection con = DataBase.connect();
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM users WHERE id_users = " + idUser);

            while (rs.next()) {
                imieTextField.setText(rs.getString(2));
                nazwiskoTextField.setText(rs.getString(3));
                loginTextField.setText(rs.getString(4));
                hasloField.setText("admin123");
                emialTextField.setText(rs.getString(7));
                telefonTextField.setText(rs.getString(8));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * metoda wywoływna przez przycisk, zapisuje dane z pól tekstowych do bazy danych
     *
     * @param event
     * @throws SQLException
     */
    @FXML
    void saveDataButton(ActionEvent event) throws SQLException {
        if (imieTextField.getText().trim().isEmpty()) {
            errorName();
        } else if (nazwiskoTextField.getText().trim().isEmpty()) {
            errorSurname();
        } else if (emailCheck() == 0) {
            errorEmail();
        } else if (telefonCheck() == 0) {
            errorPhone();
        } else {
            Connection con = DataBase.connect();
            String query = "UPDATE users SET user_name=?, surname=?, email=?, phone_number=? WHERE id_users = " + idUser;
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, imieTextField.getText());
            pst.setString(2, nazwiskoTextField.getText());
            pst.setString(3, emialTextField.getText());
            pst.setString(4, telefonTextField.getText());
            pst.executeUpdate();
            con.close();
            refresh();
            updateSucces();
        }
    }

    /**
     * metoda wywoływna przez przycisk, otwiera nowe okno do zmiany hasła
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void updateHasloButton(ActionEvent event) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getClassLoader().getResource("scenes/client/ClientSettingsPassword.fxml")
        );
        Parent root = fxmlLoader.load();
        ClientSettingsPasswordController controller = fxmlLoader.getController();


        controller.AdminSettingsPassword(idUser);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
        refresh();
    }

    /**
     * metoda wywoływna przez przycisk, otwiera nowe okno do zmiany loginu
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void updateLoginButton(ActionEvent event) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getClassLoader().getResource("scenes/client/ClientSettingsLogin.fxml")
        );
        Parent root = fxmlLoader.load();
        ClientSettingsLoginController controller = fxmlLoader.getController();


        controller.AdminSettingsLogin(idUser);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
        refresh();
    }


    /**
     * metoda która akutalizuje dane pokazane w polach tekstowych
     */
    void refresh() {
        try {
            Connection con = DataBase.connect();
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM users WHERE id_users = " + idUser);

            while (rs.next()) {
                imieTextField.setText(rs.getString(2));
                nazwiskoTextField.setText(rs.getString(3));
                loginTextField.setText(rs.getString(4));
                hasloField.setText("admin123");
                emialTextField.setText(rs.getString(7));
                telefonTextField.setText(rs.getString(8));
            }
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * @return 0 jeśli pole tekstowe telefonu jest równe 9 znaków, 1 w innym wypadku
     */
    public int telefonCheck() {
        int a;
        String telefon = telefonTextField.getText().trim();
        boolean valid = telefon.length() == 9;
        if (!valid|| (!telefon.matches("[0-9]+"))) {
            a = 0;
        } else {
            a = 1;
        }
        return a;
    }

    /**
     * @return 0 jeśli email ma niepoprawną formę, w innym przypadku zwraca 1
     */
    public int emailCheck() {
        int a;
        String email = emialTextField.getText();
        if (validate(email) == false) {
            a = 0;
        } else {
            a = 1;
        }
        return a;
    }

    public static final Pattern VALIDEMAIL =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    /**
     * @param emailStr
     * @return true jeśli email jest poprawny składniowo, w innym przypadku false
     */
    public static boolean validate(String emailStr) {
        Matcher matcher = VALIDEMAIL.matcher(emailStr);
        return matcher.find();
    }

    /**
     * wyświetla wiadomość błedu
     */
    private void errorEmail() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ustawienia");
        alert.setHeaderText(null);
        alert.setContentText("Błędnie wprowadzony email!");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/CSS/login.css").toExternalForm());
        dialogPane.getStyleClass().add("alert");
        alert.showAndWait();
    }

    /**
     * wyświetla wiadomość błedu
     */
    private void errorName() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ustawienia");
        alert.setHeaderText(null);
        alert.setContentText("Nie podałeś imienia!");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/CSS/login.css").toExternalForm());
        dialogPane.getStyleClass().add("alert");
        alert.showAndWait();
    }

    /**
     * wyświetla wiadomość błedu
     */
    private void errorSurname() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ustawienia");
        alert.setHeaderText(null);
        alert.setContentText("Nie podałeś nazwiska!");
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
        alert.setTitle("Ustawienia");
        alert.setHeaderText(null);
        alert.setContentText("Nie podałeś telefonu, telefon musi zawierać 9 znaków!");
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

    /**
     * metoda zmienia scenę na ekran logowania
     *
     * @param event
     */
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

    /**
     * metoda zmienia scenę na zakładkę Wypożyczenia
     *
     * @param event
     */
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

    /**
     * metoda zmienia scenę na zakładkę Katalog
     *
     * @param event
     */
    @FXML
    void clientCatalog(ActionEvent event) {
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
}
