package controllers.client;

import controllers.employee.EmployeeLoansController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
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
 * Dany kontroler służy do obsługi szczegółów wypożyczeń klienta
 */
public class ClientLoansDetailsController implements Initializable {

    /**
     * Inicjalizacja globalnych zmiennych dla slideshow zdjęć samochodu
     */
    int count;
    int ilosc;
    private Image image;
    private FileInputStream fis;

    /**
     * Inicjalizacja komponentów z pliku AdminLoansDetails.fxml
     */
    @FXML
    private Label protokolyUszkodzenia;

    @FXML
    private Label protokolyMarkaModel;

    @FXML
    private Label protokolyRokWyprodukowania;

    @FXML
    private Label protokolySilnik;

    @FXML
    private Label protokolyTypPaliwa;

    @FXML
    private Label protokolyNumerRejestracyjny;

    @FXML
    private Label protokolyKolor;

    @FXML
    private Label protokolyCenaZaDobe;

    @FXML
    private Label protokolyImieKlienta;

    @FXML
    private Label protokolyNazwiskoKlienta;

    @FXML
    private Label protokolyTelefonKlienta;

    @FXML
    private Label protokolyEmailKlienta;

    @FXML
    private ImageView protokolyZdjecieCar;

    /**
     * Dana metoda służu do pobrania zdjęć samochodu z bazy danych oraz
     * wyświetlania ich w postać slideshow przy pomocy takiego elementu animacji jak Timeline.
     * W przypadku braku zdjęć z bazy danych jest obsługa blędów
     */
    public void slideshow() {
        ArrayList<Image> images = new ArrayList<Image>();
        ClientLoansController controller = new ClientLoansController();
        Connection connection = DataBase.connect();
        int count_image = 1;
        try {
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT DISTINCT c.id_car, l.id_loan, p.photo FROM loans l " +
                    "INNER JOIN cars c on c.id_car = l.id_car " +
                    "INNER JOIN photo_car pc on c.id_car = pc.id_car " +
                    "INNER JOIN photos p on pc.id_photo = p.id_photo " +
                    "WHERE l.id_loan = " + ClientLoansController.loan_id);
            while (resultSet.next()) {
                InputStream is = resultSet.getBinaryStream("photo");
                InputStreamReader inputReader = new InputStreamReader(is);
                if(inputReader.ready()) {
                    File tempFile = new File("images/image"+count_image+".jpg");
                    FileOutputStream fos = new FileOutputStream(tempFile);
                    byte[] buffer = new byte[1024];
                    while (is.read(buffer) > 0) {
                        fos.write(buffer);
                    }
                    images.add(new Image(tempFile.toURI().toURL().toString()));
                }
                count_image++;
            }
            ilosc = count_image - 1;
            protokolyZdjecieCar.setImage(images.get(0));
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
                protokolyZdjecieCar.setImage(images.get(count));
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

    /**
     * Dana metoda służy do przejścia do listy wypożyczeń klienta
     * @param event wykorzysuje się dla obsługi zdarzeń
     */
    @FXML
    void wracamyDoWypozyczen(ActionEvent event) {
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
     * Dana metoda (Initialize) zostaje wykonywana przy przejściu na daną scenę
     * Zajmuje się pobieraniem szczegółow konkretnego samochodu, danych klieta, który wypożycza
     * ten samochód, date wypożyczenia i informacje o uszkodzeniach samochodu jeśli takie są
     * @param url służy do przechowywania i modyfikacji modyfikatora zasobów
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        slideshow();
        ClientLoansController controller = new ClientLoansController();
        Connection con = DataBase.connect();
        try {
            ResultSet rs = con.createStatement().executeQuery("SELECT c.model, c.production_date, c.engine, c.fuel, c.registration_number, c.color, c.cost, " +
                    "u.user_name, u.surname, u.phone_number, u.email \n" +
                    "FROM cars c \n" +
                    "INNER JOIN loans l on c.id_car = l.id_car \n" +
                    "INNER JOIN users u on l.id_client = u.id_users where l.id_loan = " + ClientLoansController.loan_id);
            String model, engine, fuel, registration_number, color, user_name, surname, phone, email;
            int production_date, cost;
            if (rs.next()) {
                model = rs.getString("model");
                production_date = rs.getInt("production_date");
                engine = rs.getString("engine");
                fuel = rs.getString("fuel");
                registration_number = rs.getString("registration_number");
                color = rs.getString("color");
                cost = rs.getInt("cost");
                user_name = rs.getString("user_name");
                surname = rs.getString("surname");
                phone = rs.getString("phone_number");
                email = rs.getString("email");

                protokolyMarkaModel.setText(model);
                protokolyRokWyprodukowania.setText(String.valueOf(production_date));
                protokolySilnik.setText(engine);
                protokolyTypPaliwa.setText(fuel);
                protokolyNumerRejestracyjny.setText(registration_number);
                protokolyKolor.setText(color);
                protokolyCenaZaDobe.setText(String.valueOf(cost));
                protokolyImieKlienta.setText(user_name);
                protokolyNazwiskoKlienta.setText(surname);
                protokolyTelefonKlienta.setText(phone);
                protokolyEmailKlienta.setText(email);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
