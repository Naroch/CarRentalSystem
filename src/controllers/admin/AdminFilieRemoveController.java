package controllers.admin;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import models.Agency;
import service.DataBase;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Kontroler sceny używany z AdminFilieRemove.fxml
 * Pozwala wybrać filię do której zostaną przepisane rekordy z usuwanej fili
 */
public class AdminFilieRemoveController implements Initializable {

    @FXML
    private ComboBox<AgencyUser> filiaComboBox;

    int idFiliaRemove;

    private int idFilia;

    private int records;

    @FXML
    private Button updateButton;

    /**
     * Kontruktor do przesyłania zmiennych miedzy kontrolerami
     *
     * @param idFiliaRemove
     * @param records
     */
    public void AdminFilieRemove(int idFiliaRemove, int records) {
        this.idFiliaRemove = idFiliaRemove;
        this.records = records;
    }


    /**
     * przycisk wykonujący usuwanie fili i przesyłanie rekordów
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void updateButton(ActionEvent event) throws IOException {
        if (idFilia == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Zaznacz filię");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStyleClass().add("alert");
            alert.showAndWait();
        } else {
            if (confirmationFilled(records))
                transferData();
            try {
                Connection con = DataBase.connect();

                String query = "DELETE FROM agency WHERE id_agency = " + idFiliaRemove;
                PreparedStatement pst = con.prepareStatement(query);
                pst.execute();
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            Stage stage = (Stage) updateButton.getScene().getWindow();
            stage.close();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            fillFiliaComboBox();
        });
    }

    /**
     * klasa używana w comboboxie
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
     * metoda wypełniająca combobox filiami z bazy
     */
    void fillFiliaComboBox() {
        ObservableList data = FXCollections.observableArrayList();
        try {
            Connection con = DataBase.connect();
            ResultSet rs = con.createStatement().executeQuery("SELECT agency_name, id_agency FROM agency");

            while (rs.next()) {
                if (rs.getInt(2) != idFiliaRemove)
                    if (rs.getInt(2) != 1)
                        data.add(new AdminFilieRemoveController.AgencyUser(rs.getString("agency_name"), rs.getInt("id_agency")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        filiaComboBox.setItems(data);

        filiaComboBox.setConverter(new StringConverter<AgencyUser>() {
            @Override
            public String toString(AdminFilieRemoveController.AgencyUser agencyUser) {
                return agencyUser.getAgencyName();
            }

            @Override
            public AgencyUser fromString(String string) {
                return filiaComboBox.getItems().stream().filter(ap ->
                        ap.getAgencyName().equals(string)).findFirst().orElse(null);
            }

        });

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
     * metoda przesyłająca rekordy z jednej do drugiej fili
     */
    void transferData() {

        try {
            Connection con = DataBase.connect();
            String query = "UPDATE users SET id_agency=? Where id_agency = " + idFiliaRemove;
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, idFilia);
            statement.executeUpdate();
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            Connection con = DataBase.connect();
            String query = "UPDATE loans SET agency_of_loan=? Where agency_of_loan = " + idFiliaRemove;
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, idFilia);
            statement.executeUpdate();
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            Connection con = DataBase.connect();
            String query = "UPDATE loans SET agency_of_delivery=? Where agency_of_delivery = " + idFiliaRemove;
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, idFilia);
            statement.executeUpdate();
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            Connection con = DataBase.connect();
            String query = "UPDATE cars SET id_agency=? Where id_agency = " + idFiliaRemove;
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, idFilia);
            statement.executeUpdate();
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     * @param a - ilość rekordów
     * @return true jeśli przyciśnie się ButtonType.OK w innym wypadku false
     */
    private boolean confirmationFilled(int a) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Czy jesteś pewien że chcesz usunąć tą filie z " + a + " rekordami?");

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }
}
