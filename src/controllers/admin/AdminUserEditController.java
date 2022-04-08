package controllers.admin;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
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
 * Kontroler sceny używany z AdminUserEdit.fxml
 * Wyświetla opcje do edycji użytkownika
 */
public class AdminUserEditController implements Initializable {

    @FXML
    private Label labelImie;

    @FXML
    private Label labelNazwisko;

    @FXML
    private Label labelTelefon;

    @FXML
    private Label labelEmail;

    @FXML
    private ComboBox<AgencyUser> filiaComboBox;

    @FXML
    private ComboBox<RolaUser> rolaComboBox;

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

    int idFilia;

    int idRola;

    boolean active;

    public void AdminUserEdit(User user) {
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

                    idFilia = user.getIdFilia();
                    idRola = user.getRola();
                    active = user.isAktywny();

                    labelImie.setText(user.getImie());
                    labelNazwisko.setText(user.getNazwisko());
                    labelTelefon.setText(user.getTelefon());
                    labelEmail.setText(user.getEmail());
                    showDocumentFromDatabase();
                    fillFiliaComboBox();
                    fillRolaCombobox();
                    fillCheckBox();
                }
        );
    }

    /**
     * klasa używana w comboboxie fili
     */
    public class AgencyUser {
        private final String agencyName;
        private final int idAgency;

        public AgencyUser(String agencyName, int idAgency) {
            this.agencyName = agencyName;
            this.idAgency = idAgency;
        }

        public String getAgencyName() {
            return agencyName;
        }

        public int getIdAgency() {
            return idAgency;
        }
    }

    /**
     * Klasa używana w comboboxie ról
     */
    public class RolaUser {
        private final int idRola;
        private String rola;

        public RolaUser(int idRola) {
            this.idRola = idRola;
            if (this.idRola == 0)
                this.rola = "Klient";
            if (this.idRola == 1)
                this.rola = "Pracownik";
        }

        public int getIdRola() {
            return idRola;
        }

        public String getRola() {
            return rola;
        }
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
                //image = new Image(getClass().getResourceAsStream("/images/photoPass.jpg"));
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
     * Metoda wypełnia combobox fili danymi z bazy danych
     */
    void fillFiliaComboBox() {
        ObservableList data = FXCollections.observableArrayList();
        try {
            Connection con = DataBase.connect();
            ResultSet rs = con.createStatement().executeQuery("SELECT agency_name, id_agency FROM agency");

            while (rs.next()) {
                data.add(new AgencyUser(rs.getString("agency_name"), rs.getInt("id_agency")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        filiaComboBox.setItems(data);

        filiaComboBox.setConverter(new StringConverter<AgencyUser>() {
            @Override
            public String toString(AgencyUser agencyUser) {
                return agencyUser.getAgencyName();
            }

            @Override
            public AgencyUser fromString(String string) {
                return filiaComboBox.getItems().stream().filter(ap ->
                        ap.getAgencyName().equals(string)).findFirst().orElse(null);
            }
        });
        filiaComboBox.setValue(new AgencyUser(user.getFilia(), user.getIdFilia()));

        filiaComboBox.valueProperty().addListener((obs, oldval, newval) -> {
            if (newval != null) {
                System.out.println("Selected agency " + newval.getAgencyName()
                        + ". ID: " + newval.getIdAgency());
                idFilia = newval.getIdAgency();
                filiaComboBox.setValue(newval);
            }
        });
    }


    /**
     * Metoda wypełnia combobox rolami podanymi w bazie danych
     */
    void fillRolaCombobox() {
        ObservableList data = FXCollections.observableArrayList();
        data.addAll(new RolaUser(0), new RolaUser(1));
        rolaComboBox.setItems(data);

        rolaComboBox.setConverter(new StringConverter<RolaUser>() {
            @Override
            public String toString(RolaUser rolaUser) {
                return rolaUser.getRola();
            }

            @Override
            public RolaUser fromString(String string) {
                return rolaComboBox.getItems().stream().filter(ap ->
                        ap.getRola().equals(string)).findFirst().orElse(null);
            }
        });

        rolaComboBox.setValue(new RolaUser(user.getRola()));

        rolaComboBox.valueProperty().addListener((obs, oldval, newval) -> {
            if (newval != null) {
                System.out.println("Selected Privilege " + newval.getRola()
                        + ". ID: " + newval.getIdRola());
                idRola = newval.getIdRola();
                rolaComboBox.setValue(newval);
            }
        });
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
            String query = "UPDATE users SET document=?, role=?, id_agency=?, active=? WHERE id_users = " + user.getId_users();
            fis = new FileInputStream(file);
            PreparedStatement pst = con.prepareStatement(query);
            pst.setBinaryStream(1, fis, (int) file.length());
            pst.setInt(2, idRola);
            pst.setInt(3, idFilia);
            pst.setBoolean(4, activeCheckBox.isSelected());
            pst.executeUpdate();
        } else {
            Connection con = DataBase.connect();
            String query = "UPDATE users SET role=?, id_agency=?, active=? WHERE id_users = " + user.getId_users();
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, idRola);
            pst.setInt(2, idFilia);
            pst.setBoolean(3, activeCheckBox.isSelected());
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