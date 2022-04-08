package controllers.employee;

import controllers.admin.AdminUserEditController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.User;
import service.DataBase;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Kontroler sceny używany z EmployeeUserEdit.fxml
 * Wyświetla opcje do edycji użytkownika
 */
public class EmployeeUserEditController implements Initializable {


    @FXML
    private Label labelImie;

    @FXML
    private Label labelNazwisko;

    @FXML
    private Label labelTelefon;

    @FXML
    private Label labelEmail;

    @FXML
    private CheckBox activeCheckBox;

    @FXML
    private Button saveButton;

    @FXML
    private ImageView imageView;

    private File file;

    private Image image;

    private FileInputStream fis;

    private User user;

    boolean active;

    public void EmplyeeUserEdit(User user) {
        this.user = user;
    }

    /**
     * wypełnia wszystkie pola danymi użytkownika z bazy
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Platform.runLater(() -> {

                    try {
                        Connection con = DataBase.connect();
                        ResultSet rs = con.createStatement().executeQuery("SELECT u.*, a.agency_name FROM users u LEFT JOIN agency a ON u.id_agency = a.id_agency where id_users = " + user.getId_users());
                        while (rs.next()) {
                            user = (new User(rs.getInt("id_users"), rs.getString("user_name"), rs.getString("surname"),
                                    rs.getString("login"), rs.getString("password"), rs.getInt("id_agency"), rs.getString("agency_name"), rs.getInt("role"), "rola", rs.getString("phone_number"), rs.getString("email"),
                                    "document", rs.getBoolean("active"), "activeString"));
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    active = user.isAktywny();

                    labelImie.setText(user.getImie());
                    labelNazwisko.setText(user.getNazwisko());
                    labelTelefon.setText(user.getTelefon());
                    labelEmail.setText(user.getEmail());
                    showDocumentFromDatabase();
                    fillCheckBox();
                }
        );
    }

    /**
     * metoda pobiera zdjęcie dokumentu z bazy i wyświetla je w scenie
     */
    void showDocumentFromDatabase() {

        //select img from id
        System.out.println(user.getImie());
        Connection con = DataBase.connect();

        try {
            ResultSet rs = con.createStatement().executeQuery("SELECT document FROM users WHERE id_users = '" + user.getId_users() + "' ");
            while (rs.next()) {
                InputStream is = rs.getBinaryStream("document");
                OutputStream os = new FileOutputStream(new File("images/photoPass.jpg"));
                byte[] content = new byte[1024];
                int size = 0;
                while ((size = is.read(content)) != -1) {
                    os.write(content, 0, size);
                }
                os.close();
                is.close();

                image = new Image("file:images/photoPass.jpg");
                imageView.setImage(image);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda aktywuje checkBoxa jeśli user jest aktywny
     */
    void fillCheckBox() {
        activeCheckBox.setSelected(active);
    }

    /**
     * metoda wywoływna przez przycisk, zapisuje dane z pól i zdjęcie do bazy danych
     *
     * @param event
     * @throws FileNotFoundException
     * @throws SQLException
     */
    @FXML
    void saveButton(ActionEvent event) throws FileNotFoundException, SQLException {
        if (file != null) {
            Connection con = DataBase.connect();
            String query = "UPDATE users SET document=?, active=? WHERE id_users = " + user.getId_users();
            fis = new FileInputStream(file);
            PreparedStatement pst = con.prepareStatement(query);
            pst.setBinaryStream(1, fis, (int) file.length());
            pst.setBoolean(2, activeCheckBox.isSelected());
            pst.executeUpdate();
        } else {
            Connection con = DataBase.connect();
            String query = "UPDATE users SET active=? WHERE id_users = " + user.getId_users();
            PreparedStatement pst = con.prepareStatement(query);
            pst.setBoolean(1, activeCheckBox.isSelected());
            pst.executeUpdate();
        }
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }


    /**
     * metoda wywoływna przez przycisk, pozwala zapisać zdjęcie do programu
     *
     * @param event
     */
    @FXML
    void uploadButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        file = fileChooser.showOpenDialog(new Stage());
        image = new Image(file.toURI().toString());

        imageView.setImage(image);
    }
}
