package controllers.admin;

import controllers.admin.AdminShowCarController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;
import service.DataBase;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Stworzono i aktualizowano przez Dmytro Polianychko
 *
 * Dany kontroler służy do obsługi dodania/aktualizacji oraz usuwania uszkodzeń konkretnego samochodu
 */
public class AdminDamagesController implements Initializable {

    /**
     * Inicjalizacja zmiennych globalnych które służą do pokazania slideshow uszkodzeń samochodu
     */
    int id_photo = 0;
    public int id_damage;
    int count;
    int ilosc;
    private File file;
    private Image image;
    private FileInputStream fis;
    public static int car_damage_id;


    /**
     * Inicjalizacja komponentów z pliku AdminDamages.fxml
     */
    @FXML
    private ImageView PhotoSlideDamage;

    @FXML
    private TextArea DescriptionDamageArea;

    @FXML
    private TextArea DamagesCostArea;

    @FXML
    private ImageView AddedImageDamage;

    /**
     * Dana metoda wywołuje poprzednie metody które są stworzone dla konwersji FileChooser do ImageChooser
     * Także metoda dodaje nowe zdjęcie uszkodzenia samochodu do bazy
     * W przeciwnym przypadku jest obsługiwane blędy oraz wyświetlany alertbox
     * @param event wykorzysuje się dla obsługi zdarzeń
     * @throws IOException służy do obsługi blędów związanych z zapytaniem do bazy
     * @throws SQLException służy do obsługi blędów związanych z bazą danych MariaDB
     */
    @FXML
    void AddImageButton(ActionEvent event) throws IOException, SQLException {
        FileChooser fileChooser = new FileChooser();
        file = fileChooser.showOpenDialog(new Stage());
        image = new Image(file.toURI().toString());
        if (image != null) {
            AddedImageDamage.setImage(image);
            try {
                if (id_damage != 0) {
                    PreparedStatement insert_statement = DataBase.connect().prepareStatement("INSERT INTO photos (photo) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
                    fis = new FileInputStream(file);
                    insert_statement.setBinaryStream(1, fis, (int) (file.length()));
                    insert_statement.executeUpdate();
                    ResultSet resultSet = insert_statement.getGeneratedKeys();
                    if (resultSet.next()) {
                        id_photo = resultSet.getInt(1);
                        PreparedStatement statement = DataBase.connect().prepareStatement("INSERT INTO photo_damage (id_photo, id_damage) VALUES (?,?)");
                        statement.setInt(1, Integer.valueOf(id_photo));
                        statement.setInt(2, Integer.valueOf(id_damage));
                        statement.executeUpdate();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Info");
                    alert.setHeaderText(null);
                    alert.setContentText("Masz dodać szczegóły uszkodzenia!");
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add(
                            getClass().getResource("/CSS/login.css").toExternalForm());
                    dialogPane.getStyleClass().add("alert");
                    alert.showAndWait();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                showPhotoDamage();
            }
        } else {
            ErrorImage();
        }
    }

    /**
     * Dana metoda służy do wyświetlania alertbox w przypadku kiedy użytkownik nie wybrał zdjęcia albo wystąpił
     * bląd przy dodaniu zdjęcia
     */
    private void ErrorImage() {
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

    /**
     * Dana metoda służy do usuwania zdjęć uszkozeń samochodu jeśli takie są
     * Przy poprawnym usunięciu zdjęcia jest pokazywany podtwierdzający alertbox
     * W przeciwnym przypadku jest obsługa blędów oraz wyświetlanie alertboxu
     * @param event wykorzysuje się dla obsługi zdarzeń
     * @throws IOException służy do obsługi blędów związanych z zapytaniem do bazy
     * @throws SQLException służy do obsługi blędów związanych z bazą danych MariaDB
     */
    @FXML
    void DeleteImageButton(ActionEvent event) throws IOException, SQLException {
        try {
            if (id_damage != 0) {
                PreparedStatement delete_statement = DataBase.connect().prepareStatement("DELETE p.*, pd.* FROM photo_damage pd LEFT JOIN photos p on pd.id_photo = p.id_photo WHERE pd.id_damage = '"+id_damage+"'");
                delete_statement.executeUpdate();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Info");
                alert.setHeaderText(null);
                alert.setContentText("Usunięto wszystkie zdjęcia uszkodzeń danego samochodu!");
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(
                        getClass().getResource("/CSS/login.css").toExternalForm());
                dialogPane.getStyleClass().add("alert");
                alert.showAndWait();
                } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Info");
                alert.setHeaderText(null);
                alert.setContentText("Dany samochód nie posiada zdjęć uszkodzeń!");
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(
                        getClass().getResource("/CSS/login.css").toExternalForm());
                dialogPane.getStyleClass().add("alert");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            showPhotoDamage();
        }
    }


    /**
     * Dana metoda służy do dodania albo aktualizacji szczegółów uszkodzenia do bazy
     * W przeciwnym przypadku jest obsługa blędów
     * @param event wykorzysuje się dla obsługi zdarzeń
     */
    @FXML
    void DescriptionButton(ActionEvent event) {
        AdminShowCarController controller = new AdminShowCarController();
        car_damage_id = AdminShowCarController.id_car_damage;
        try {
            String description = DescriptionDamageArea.getText().trim();
            String cost = DamagesCostArea.getText().trim();

            String sql = "INSERT INTO damage (id_car,description, damage_cost) VALUES(?,?,?) " +
                    "ON DUPLICATE KEY UPDATE id_car = '"+car_damage_id+"', description = '"+description+"', damage_cost = '"+cost+"'";
            PreparedStatement statement = DataBase.connect().prepareStatement(sql);
            statement.setInt(1,Integer.valueOf(car_damage_id));
            statement.setString(2, DescriptionDamageArea.getText().trim());
            statement.setInt(3, Integer.valueOf(DamagesCostArea.getText().trim()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            refresh();
        }
    }

    /**
     * Dana metoda jest stosowania dla powrotu do poprzedniej sceny (AdminShowCar)
     * @param event wykorzysuje się dla obsługi zdarzeń
     */
    @FXML
    void CofnijButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/admin/AdminShowCar.fxml"));
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
     * Dana metoda służy do wyświetlania slideshow zdjęć uszkodzeń samochodu
     * Także metoda pobiera z bazy dane stosowno zdjęć uszkodzeń samochodu
     * @throws SQLException służy do obsługi blędów związanych z zapytaniem do bazy
     * @throws IOException służy do obsługi blędów związanych z bazą danych MariaDB
     */
    public void showPhotoDamage() throws SQLException, IOException {
        AdminShowCarController controller = new AdminShowCarController();
        ArrayList<Image> images = new ArrayList<Image>();
        int count_image = 1;
        PreparedStatement statement = DataBase.connect().prepareStatement("SELECT d.id_damage, p.photo FROM damage d " +
                "LEFT JOIN photo_damage pd on d.id_damage = pd.id_damage " +
                "LEFT JOIN photos p on p.id_photo = pd.id_photo " +
                "INNER JOIN cars c on d.id_car = c.id_car " +
                "WHERE d.id_car = " + AdminShowCarController.id_car_damage);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            InputStream is = resultSet.getBinaryStream("photo");
            if (is == null) {
                break;
            }
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
        ilosc = count_image - 1;
        System.out.println("Ilosc: " +ilosc);
        if (ilosc >= 1) {
            PhotoSlideDamage.setImage(images.get(0));
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(4), event -> {
                PhotoSlideDamage.setImage(images.get(count));
                count++;
                if (count == ilosc) {
                    count = 0;
                }
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        } else {}
    }

    /**
     * Dana metoda służy do odświerzania strony o uszkodzeniach auta
     */
    protected void refresh() {
        AdminShowCarController controller = new AdminShowCarController();
        car_damage_id = AdminShowCarController.id_car_damage;
        ArrayList<Image> images = new ArrayList<Image>();
        Connection connection = DataBase.connect();
        int count_image = 1;
        try {
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT d.id_damage, d.description, d.damage_cost, p.photo " +
                    "FROM damage d " +
                    "LEFT JOIN photo_damage pd on d.id_damage = pd.id_damage " +
                    "LEFT JOIN photos p on p.id_photo = pd.id_photo " +
                    "INNER JOIN cars c on d.id_car = c.id_car " +
                    "WHERE d.id_car = " + car_damage_id);
            String description, cost;
            if (resultSet.next()) {
                description = resultSet.getString("description");
                cost = resultSet.getString("damage_cost");
                id_damage = resultSet.getInt("id_damage");
                DescriptionDamageArea.setStyle("-fx-text-fill: grey");
                DescriptionDamageArea.setText(description);
                DamagesCostArea.setText(cost);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        try {
            showPhotoDamage();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AdminShowCarController controller = new AdminShowCarController();
        car_damage_id = AdminShowCarController.id_car_damage;
        ArrayList<Image> images = new ArrayList<Image>();
        Connection connection = DataBase.connect();
        int count_image = 1;
        try {
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT d.id_damage, d.description, d.damage_cost, p.photo " +
                    "FROM damage d " +
                    "LEFT JOIN photo_damage pd on d.id_damage = pd.id_damage " +
                    "LEFT JOIN photos p on p.id_photo = pd.id_photo " +
                    "INNER JOIN cars c on d.id_car = c.id_car " +
                    "WHERE d.id_car = " + car_damage_id);
            String description, cost;
            InputStream photo;
            if (resultSet.next()) {
                description = resultSet.getString("description");
                cost = resultSet.getString("damage_cost");
                id_damage = resultSet.getInt("id_damage");
                DescriptionDamageArea.setStyle("-fx-text-fill: grey");
                DescriptionDamageArea.setText(description);
                DamagesCostArea.setText(cost);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
