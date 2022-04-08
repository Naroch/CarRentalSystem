package controllers.login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import service.DataBase;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Stworzono i aktualizowano przez Dmytro Polianychko
 *
 * Dana klasa służy do rejestracji nowych użytkowników systemu oraz haszowania hasła
 */
public class RegisterController implements Initializable {

    /**
     * Inicjalizacja zmiennych globalnych dla
     */
    String haslo;
    String telefon;
    private File file;
    private Image image;
    private FileInputStream fis;
    String generatedPassword;

    /**
     * Inicjalizacja komponentow z pliku Register.fxml
     */
    @FXML
    private TextField EmailTextField;

    @FXML
    private PasswordField Haslo1TextField;

    @FXML
    private PasswordField Haslo2TextField;

    @FXML
    private TextField NazwiskoTextField;

    @FXML
    private TextField ImieTextField;

    @FXML
    private TextField NrTelefonuTextField;

    @FXML
    private TextField loginTextField;

    @FXML
    private ImageView prawoJazdyImage;

    /**
     * Dana metoda pozwala dodawać zdjęcia prawa jazdy albo innego dokumentu podtwierdzającego wiek użytkownika
     * @param event wykorzysuje się dla obsługi zdarzeń
     * @throws FileNotFoundException w przypadku złego wybrania pliku
     */
    @FXML
    public void prawoJazdyButton(ActionEvent event) throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        file = fileChooser.showOpenDialog(new Stage());
        image = new Image(file.toURI().toString());
        if (image != null) {
            prawoJazdyImage.setImage(image);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Zdjęcie");
            alert.setHeaderText(null);
            alert.setContentText("Nie wybrałeś zdjęcia!");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("/CSS/login.css").toExternalForm());
            dialogPane.getStyleClass().add("alert");
            alert.showAndWait();
        }
    }

    /**
     * Dana metoda służy do przejścia do strony logowania
     * @param event wykorzysuje się dla obsługi zdarzeń
     */
    @FXML
    public void cofnijButton(ActionEvent event) {
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
     * Dana metoda sluży do sprawdzania poprawności oraz spełniania warunków wszystkich danych wprowadzonych
     * przez użytkownika
     * @param event wykorzysuje się dla obsługi zdarzeń
     * @throws SQLException służy do obsługi blędów związanych z bazą danych MariaDB
     */
    @FXML
    void ZarejestrujButton(ActionEvent event) throws SQLException, FileNotFoundException {
        if (loginTextField.getText().trim().isEmpty()) {
            errorLogin();
        } else if (ImieTextField.getText().trim().isEmpty()) {
            errorName();
        } else if (NazwiskoTextField.getText().trim().isEmpty()) {
            errorSurname();
        } else if (emailCheck() == 0) {
            errorEmail();
        } else if (passwordCheck() == 0) {
            errorLengthPassword();
        } else if (!Haslo1TextField.getText().trim().equals(Haslo2TextField.getText().trim())) {
            errorPassword();
        } else if (telefonCheck() == 0) {
            errorPhone();
        } else {
            RegisterUser();
            Parent zmianaSceny = null;
            try {
                zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/login/Login.fxml"));
                Scene menu = new Scene(zmianaSceny);
                Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
                okno.setScene(menu);
                okno.show();
                pomyslnaRejestracja();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Metoda sprawdzania podanego przez użytkownika hasła
     * Jeżeli hasło zawiera conajmniej 8 znaków to zwracamy 1
     * W przeciwnym przypadku zwracamy 0
     * Dana metoda jest wykorzystana w ZarejestrujButton
     * @return zwraca 0 albo 1
     */
    public int passwordCheck() {
        int a;
        haslo = Haslo1TextField.getText().trim();
        boolean valid = haslo.length() >= 8;
        if (!valid) {
            a = 0;
        } else {
            a = 1;
        }
        return a;
    }

    /**
     * Metoda sprawdzania podanego przez użytkownika numeru telefonu
     * Jeżeli numer telefonu zawiera 9 znaków to zwracamy 1
     * W przeciwnym przypadku zwracamy 0
     * @return zwraca 0 albo 1
     */
    public int telefonCheck() {
        int a;
        telefon = NrTelefonuTextField.getText().trim();
        boolean valid = telefon.length() == 9;
        if (!valid || (!telefon.matches("[0-9]+"))) {
            a = 0;
        } else {
            a = 1;
        }
        return a;
    }

    /**
     * Metoda sprawdzania podanego przez użytkownika emailu
     * Jeżeli email przechodzi walidacje to zwracamy 1
     * W przeciwnym przypadku zwracamy 0
     * @return zwraca 0 albo 1
     */
    public int emailCheck() {
        int a;
        String email = EmailTextField.getText();
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
     * Metoda walidacji emailu
     * @param emailStr podany email
     * @return
     */
    public static boolean validate(String emailStr) {
        Matcher matcher = VALIDEMAIL.matcher(emailStr);
        return matcher.find();
    }

    /**
     * Dana metoda wykorzysuje się w metodzie ZarejestrujButton w przypadku blędnie podanego hasła
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
     * Dana metoda wykorzysuje się w metodzie ZarejestrujButton w przypadku niepowtarzających haseł
     * Jest wyświetlany alertbox
     */
    private void errorPassword() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Rejestracja");
        alert.setHeaderText(null);
        alert.setContentText("Wprowadzone hasła nie są jednakowe!");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/CSS/login.css").toExternalForm());
        dialogPane.getStyleClass().add("alert");
        alert.showAndWait();
    }

    /**
     * Dana metoda wykorzysuje się w metodzie ZarejestrujButton w przypadku blędnie podanego emailu
     * Jest wyświetlany alertbox
     */
    private void errorEmail() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Rejestracja");
        alert.setHeaderText(null);
        alert.setContentText("Błędnie wprowadzony email!");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/CSS/login.css").toExternalForm());
        dialogPane.getStyleClass().add("alert");
        alert.showAndWait();
    }

    /**
     * Dana metoda wykorzysuje się w metodzie ZarejestrujButton w przypadku blędnie podanego loginu
     * Jest wyświetlany alertbox
     */
    private void errorLogin() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Rejestracja");
        alert.setHeaderText(null);
        alert.setContentText("Nie podałeś loginu!");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/CSS/login.css").toExternalForm());
        dialogPane.getStyleClass().add("alert");
        alert.showAndWait();
    }

    /**
     * Dana metoda wykorzysuje się w metodzie ZarejestrujButton w przypadku blędnie podanego imienia użytkownika
     * Jest wyświetlany alertbox
     */
    private void errorName() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Rejestracja");
        alert.setHeaderText(null);
        alert.setContentText("Nie podałeś imienia!");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/CSS/login.css").toExternalForm());
        dialogPane.getStyleClass().add("alert");
        alert.showAndWait();
    }

    /**
     * Dana metoda wykorzysuje się w metodzie ZarejestrujButton w przypadku blędnie podanego nazwiska użytkownika
     * Jest wyświetlany alertbox
     */
    private void errorSurname() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Rejestracja");
        alert.setHeaderText(null);
        alert.setContentText("Nie podałeś nazwiska!");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/CSS/login.css").toExternalForm());
        dialogPane.getStyleClass().add("alert");
        alert.showAndWait();
    }

    /**
     * Dana metoda wykorzysuje się w metodzie ZarejestrujButton w przypadku blędnie podanego numeru telefonu użytkownika
     * Jest wyświetlany alertbox
     */
    private void errorPhone() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Rejestracja");
        alert.setHeaderText(null);
        alert.setContentText("Nie podałeś telefonu, telefon musi zawierać 9 znaków!");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/CSS/login.css").toExternalForm());
        dialogPane.getStyleClass().add("alert");
        alert.showAndWait();
    }

    /**
     * Dana metoda wykorzysuje się w metodzie ZarejestrujButton w przypadku poprawnej rejestracji
     * Jest wyświetlany alertbox
     */
    private void pomyslnaRejestracja() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Rejestracja");
        alert.setHeaderText(null);
        alert.setContentText("Rejestracja skączyła się pomyślnie!");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/CSS/login.css").toExternalForm());
        dialogPane.getStyleClass().add("alert");
        alert.showAndWait();
    }

    /**
     * Dana metoda jest wykorzystywana dla haszowania hasła użytkownika metodą MD5
     * @return zwraca zahaszowane hasło
     */
    private String hashingPassword() {
        String password = Haslo1TextField.getText().trim();
        generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    /**
     * Dana metoda dodaje do bazy danych wszystkie informacje o użytkowniku przy pomyślnej rejestracji
     * @throws SQLException służy do obsługi blędów związanych z bazą danych MariaDB
     */
    public void RegisterUser() throws SQLException, FileNotFoundException {
        hashingPassword();
        String sql="INSERT INTO users (user_name, surname, login, password, role, email, phone_number, document, active) VALUES (?, ?, ?, ?, 0, ?, ?, ?, 0);";
        PreparedStatement insert_statement= DataBase.connect().prepareStatement(sql);
        insert_statement.setString(1, ImieTextField.getText());
        insert_statement.setString(2, NazwiskoTextField.getText());
        insert_statement.setString(3, loginTextField.getText());
        insert_statement.setString(4, generatedPassword);
        insert_statement.setString(5, EmailTextField.getText());
        insert_statement.setString(6, NrTelefonuTextField.getText());
        fis = new FileInputStream(file);
        insert_statement.setBinaryStream(7, fis, (int)(file.length()));
        insert_statement.executeUpdate();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}