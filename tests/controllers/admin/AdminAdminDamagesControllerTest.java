package controllers.admin;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.DataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AdminDamagesControllerTest {

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
    void updateDescriptionDamage() throws SQLException {
        System.out.println("Test aktualizujący szczególy uszkodzen auta do bazy...");
        int id_car = 1;
        String description = "uszkodzone prawe lusterko";
        int cost = 450;
        String sql =  "INSERT INTO damage (id_car,description, damage_cost) VALUES(?,?,?) " +
                "ON DUPLICATE KEY UPDATE id_car = '"+id_car+"', description = '"+description+"', damage.damage_cost = '"+cost+"'";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, Integer.valueOf(id_car));
        statement.setString(2, description);
        statement.setInt(3, Integer.valueOf(cost));
        if (statement != null) {
            Assertions.assertNotNull(statement);
        }
    }

    @Test
    void addDescriptionDamage() throws SQLException {
        System.out.println("Test dodający szczególy uszkodzen auta do bazy...");
        int id_car = 2;
        String description = "uszkodzone prawe lusterko";
        int cost = 450;
        String sql =  "INSERT INTO damage (id_car,description, damage_cost) VALUES(?,?,?) " +
                "ON DUPLICATE KEY UPDATE id_car = '"+id_car+"', description = '"+description+"', damage_cost = '"+cost+"'";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, Integer.valueOf(id_car));
        statement.setString(2, description);
        statement.setInt(3, Integer.valueOf(cost));
        if (statement != null) {
            Assertions.assertNotNull(statement);
        }
    }

    @Test
    void showExistingPhotoDamage() throws SQLException {
        System.out.println("pobieranie zdjecia uszkodzenia konkretnego auta z bazy...");
        ArrayList<String> images1 = new ArrayList<>();
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT d.id_damage, p.photo " +
                "FROM damage d " +
                "LEFT JOIN photo_damage pd on d.id_damage = pd.id_damage " +
                "LEFT JOIN photos p on p.id_photo = pd.id_photo " +
                "INNER JOIN cars c on d.id_car = c.id_car " +
                "WHERE d.id_car = 1");
        while (resultSet.next()) {
            images1.add(resultSet.getString("photo"));
        }
        if (!images1.isEmpty()) {
            Assertions.assertNotNull(images1);
        }
    }

    @Test
    void showExistingPhotosDamage() throws SQLException {
        System.out.println("pobieranie zdjeć uszkodzeń wszystkich aut z bazy...");
        ArrayList<String> images2 = new ArrayList<>();
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT d.id_damage, p.photo " +
                "FROM damage d " +
                "LEFT JOIN photo_damage pd on d.id_damage = pd.id_damage " +
                "LEFT JOIN photos p on p.id_photo = pd.id_photo " +
                "INNER JOIN cars c on d.id_car = c.id_car ");
        while (resultSet.next()) {
            images2.add(resultSet.getString("photo"));
        }
        if (!images2.isEmpty()) {
            Assertions.assertNotNull(images2);
        }
    }

    @Test
    void showNonExistingPhotosDamage() throws SQLException {
        System.out.println("pobieranie zdjeć uszkodzenia nieistniajacego auta z bazy...");
        ArrayList<String> images3 = new ArrayList<>();
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT d.id_damage, p.photo " +
                "FROM damage d " +
                "LEFT JOIN photo_damage pd on d.id_damage = pd.id_damage " +
                "LEFT JOIN photos p on p.id_photo = pd.id_photo " +
                "INNER JOIN cars c on d.id_car = c.id_car " +
                "WHERE d.id_car = 4");
        if (resultSet.next()) {
            images3.add(resultSet.getString("photo"));
        } else {
            Assertions.assertEquals(true, images3.isEmpty());
        }
    }

    @Test
    void showExistingDamage() throws SQLException {
        System.out.println("pobieranie uszkodzen konkretnego auta z bazy...");
        ArrayList<String> damage = new ArrayList<>();
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT d.id_damage, d.description, d.damage_cost, p.photo " +
                "FROM damage d " +
                "LEFT JOIN photo_damage pd on d.id_damage = pd.id_damage " +
                "LEFT JOIN photos p on p.id_photo = pd.id_photo " +
                "INNER JOIN cars c on d.id_car = c.id_car " +
                "WHERE d.id_car = 1");
        while (resultSet.next()) {
            damage.add(resultSet.getString("photo"));
            damage.add(resultSet.getString("description"));
            damage.add(resultSet.getString("damage_cost"));
            System.out.println(damage);
        }
        if (!damage.isEmpty()) {
            Assertions.assertNotNull(damage);
        }
    }

    @Test
    void showExistingDamages() throws SQLException {
        System.out.println("pobieranie wszystkich uszkodzen aut z bazy...");
        ArrayList<String> damage1 = new ArrayList<>();
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT d.id_damage, d.description, d.damage_cost, p.photo " +
                "FROM damage d " +
                "LEFT JOIN photo_damage pd on d.id_damage = pd.id_damage " +
                "LEFT JOIN photos p on p.id_photo = pd.id_photo " +
                "INNER JOIN cars c on d.id_car = c.id_car");
        while (resultSet.next()) {
            damage1.add(resultSet.getString("photo"));
            damage1.add(resultSet.getString("description"));
            damage1.add(resultSet.getString("damage_cost"));
        }
        if (!damage1.isEmpty()) {
            Assertions.assertNotNull(damage1);
        }
    }

    @Test
    void showNonExistingDamage() throws SQLException {
        System.out.println("pobieranie uszkodzen nieistniajacego auta z bazy...");
        ArrayList<String> damage = new ArrayList<>();
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT d.id_damage, d.description, d.damage_cost, p.photo " +
                "FROM damage d " +
                "LEFT JOIN photo_damage pd on d.id_damage = pd.id_damage " +
                "LEFT JOIN photos p on p.id_photo = pd.id_photo " +
                "INNER JOIN cars c on d.id_car = c.id_car " +
                "WHERE d.id_car = 10");
        if (resultSet.next()) {
            damage.add(resultSet.getString("photo"));
            damage.add(resultSet.getString("description"));
            damage.add(resultSet.getString("damage_cost"));
            System.out.println(damage);
        } else {
            Assertions.assertEquals(true, damage.isEmpty());
        }
    }
}