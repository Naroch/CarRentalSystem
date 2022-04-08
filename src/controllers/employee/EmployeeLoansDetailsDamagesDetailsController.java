package controllers.employee;

import controllers.admin.AdminLoansController;
import controllers.admin.AdminLoansDetailsController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import service.DataBase;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
/**
 * Stworzono i aktualizowano przez Dmytro Polianychko
 *
 * Dany kontroler służy do obsługi uszkodzeń samochodu w szczególach wypożyczeń
 * klientów przez administratora
 */
public class EmployeeLoansDetailsDamagesDetailsController implements Initializable {

        /**
         * Inicjalizacja zmiennych globalnych do obsługi slideshow zdjęć uszkodzeń auta
         */
        int count;
        int ilosc;
        int car_damage_id;

        /**
         * Inicjalizacja komponentów z pliku AdminLoansDetailsDamagesDetails.fxml
         */
        @FXML
        private ImageView zdjeciaUszkodzenAuta;


        /**
         * Dana metoda służy do powrotu użytkownika do poprzedniej sceny (AdminLoansDetails)
         * @param event wykorzysuje się dla obsługi zdarzeń
         */
        @FXML
        void buttonPowrot(ActionEvent event) {
            Parent zmianaSceny = null;
            try {
                zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/employee/EmployeeLoansDetails.fxml"));
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
         * Dana metoda (Initialize) zostaje wykonywana przy przejściu na daną scenę
         * Zajmuje się pobieraniem szczegółów oraz zdjęć uszkodzeń konkretnego samochodu
         * W przypadku braku danych z bazy jest obsługa blędów oraz zostaje pokazany alertbox
         * @param url służy do przechowywania i modyfikacji modyfikatora zasobów
         * @param resourceBundle
         */
        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
            EmployeeLoansDetailsController controller = new EmployeeLoansDetailsController();
            car_damage_id = EmployeeLoansController.car_id;
            ArrayList<Image> images = new ArrayList<Image>();
            Connection connection = DataBase.connect();
            int count_image = 1;
            try {
                ResultSet resultSet = connection.createStatement().executeQuery("SELECT DISTINCT l.id_loan, d.id_damage, p.photo " +
                        "FROM loans l " +
                        "INNER JOIN damage d on l.id_loan = d.id_loan " +
                        "INNER JOIN photo_damage pd on d.id_damage = pd.id_damage " +
                        "INNER JOIN photos p on pd.id_photo = p.id_photo " +
                        "WHERE l.id_car = " + car_damage_id);
                if (resultSet.next()) {
                    InputStream is = resultSet.getBinaryStream("photo");
                    InputStreamReader inputReader = new InputStreamReader(is);
                    if(inputReader.ready()) {
                        File tempFile = new File("images/damage"+count_image+".jpg");
                        FileOutputStream fos = new FileOutputStream(tempFile);
                        byte[] buffer = new byte[1024];
                        while (is.read(buffer) > 0) {
                            fos.write(buffer);
                        }
                        images.add(new Image(tempFile.toURI().toURL().toString()));
                    }
                    count_image++;
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Informacja");
                    alert.setHeaderText(null);
                    alert.setContentText("Dane auto nie ma zdjęć uszkodzeń");
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add(
                            getClass().getResource("/CSS/login.css").toExternalForm());
                    dialogPane.getStyleClass().add("alert");
                    alert.showAndWait();
                }
                ilosc = count_image - 1;
                zdjeciaUszkodzenAuta.setImage(images.get(0));
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
                    zdjeciaUszkodzenAuta.setImage(images.get(count));
                    count++;
                    if (count == ilosc) {
                        count = 0;
                    }
                }));
                timeline.setCycleCount(Timeline.INDEFINITE);
                timeline.play();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
