package controllers.client;

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
import javafx.scene.control.Label;
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
 * Dany kontroler służy do przeglądania uszkodzeń konkretnego samochodu
 */
public class ClientDamagesController implements Initializable {

    /**
     * Inicjalizacja zmiennych globalnych które służą do pokazania slideshow uszkodzeń samochodu
     */
    int count;
    int ilosc;
    File image;
    FileInputStream fis;

    /**
     * Inicjalizacja komponentów z pliku ClientDamages.fxml
     */
    @FXML
    private ImageView ClientPhotoCarDamage;

    @FXML
    private Label ClientCarDetailDamageLabel;

    /**
     * Dana metoda jest stosowania dla powrotu do poprzedniej sceny (ClientShowCar)
     * @param event wykorzysuje się dla obsługi zdarzeń
     */
    @FXML
    void cofnijButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/client/ClientShowCar.fxml"));
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
     * wykorzystuje poprzednią metodę ShowPhotoDamage
     * Zajmuję się pobieraniem danych z bazy stosowno szczegółów oraz zdjęć uszkodzeń konkretnego samochodu
     * @param url służy do przechowywania i modyfikacji modyfikatora zasobów
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ClientController controller = new ClientController();
            ArrayList<Image> images = new ArrayList<Image>();
            Connection connection = DataBase.connect();
            int count_image = 1;
            try {
                ResultSet resultSet = connection.createStatement().executeQuery("SELECT d.id_damage, d.description, p.photo " +
                        "FROM damage d " +
                        "LEFT JOIN photo_damage pd on d.id_damage = pd.id_damage " +
                        "LEFT JOIN photos p on p.id_photo = pd.id_photo " +
                        "INNER JOIN cars c on d.id_car = c.id_car " +
                        "WHERE d.id_car = " + ClientController.id);
            InputStream photo;
            String description;
            if (resultSet.next()) {
                description = resultSet.getString("description");
                ClientCarDetailDamageLabel.setText(description);
                photo = resultSet.getBinaryStream("photo");
                if (photo != null) {
                    InputStream is = resultSet.getBinaryStream("photo");
                    InputStreamReader inputReader = new InputStreamReader(is);
                    if (inputReader.ready()) {
                        File tempFile = new File("images/damage_car" + count_image + ".jpg");
                        FileOutputStream fos = new FileOutputStream(tempFile);
                        byte[] buffer = new byte[1024];
                        while (is.read(buffer) > 0) {
                            fos.write(buffer);
                        }
                        images.add(new Image(tempFile.toURI().toURL().toString()));
                    }
                    count_image++;
                }
            } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Info");
                    alert.setHeaderText(null);
                    alert.setContentText("Dane auto nie posiada uszkodzeń!");
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add(
                        getClass().getResource("/CSS/login.css").toExternalForm());
                    dialogPane.getStyleClass().add("alert");
                    alert.showAndWait();
            }
            if (count_image > 1) {
                ilosc = count_image - 1;
                ClientPhotoCarDamage.setImage(images.get(0));
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
                    ClientPhotoCarDamage.setImage(images.get(count));
                    count++;
                    if (count == ilosc) {
                        count = 0;
                    }
                }));
                timeline.setCycleCount(Timeline.INDEFINITE);
                timeline.play();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
