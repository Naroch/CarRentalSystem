package controllers.client;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.DataBase;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

class ClientDamagesControllerTest {

    Connection connection;

    @BeforeEach
    void setUp() {
        connection = DataBase.connect();
        System.out.println("Podłączono do bazy danych");

    }

    @AfterAll
    static void tearDown() throws SQLException {
        Connection connection = DataBase.connect();
        connection.close();
        System.out.println("Zamknięto polącenia z bazą danych");
    }

    @Test
    void readAllPhotosDamageCars() throws SQLException {
        System.out.println("Test pobierający listę uszkodzeń wszystkich aut...");
        ArrayList<String> images = new ArrayList<>();
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT d.id_damage, d.description, p.photo " +
                "FROM damage d " +
                "LEFT JOIN photo_damage pd on d.id_damage = pd.id_damage " +
                "LEFT JOIN photos p on p.id_photo = pd.id_photo " +
                "INNER JOIN cars c on d.id_car = c.id_car");
        while (resultSet.next()) {
            images.add(resultSet.getString("description"));
            images.add(resultSet.getString("photo"));
        }
        if (!images.isEmpty()) {
            Assertions.assertNotNull(images);
        }
    }

    @Test
    void readPhotoDamageNoNExistingCar() throws SQLException {
        System.out.println("Test pobierający listę uszkodzeń nieistniającego auta...");
        ArrayList<String> images = new ArrayList<>();
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT d.id_damage, d.description, p.photo " +
                "FROM damage d " +
                "LEFT JOIN photo_damage pd on d.id_damage = pd.id_damage " +
                "LEFT JOIN photos p on p.id_photo = pd.id_photo " +
                "INNER JOIN cars c on d.id_car = c.id_car " +
                "WHERE d.id_car = 0");
        if (resultSet.next()) {
            images.add(resultSet.getString("description"));
            images.add(resultSet.getString("photo"));
        } else {
            Assertions.assertEquals(true, images.isEmpty());
        }
    }


    @Test
    void initialize() {
    }
}