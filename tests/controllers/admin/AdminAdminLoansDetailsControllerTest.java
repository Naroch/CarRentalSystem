package controllers.admin;

import javafx.scene.image.Image;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.DataBase;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class AdminAdminLoansDetailsControllerTest {

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
    void readAllPhotosCars() throws SQLException {
        System.out.println("Test pobierający listę zdjęć wszystkich aut...");
        ArrayList<String> images = new ArrayList<>();
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT DISTINCT c.id_car, l.id_loan, p.photo FROM loans l " +
                "INNER JOIN cars c on c.id_car = l.id_car " +
                "INNER JOIN photo_car pc on c.id_car = pc.id_car " +
                "INNER JOIN photos p on pc.id_photo = p.id_photo ");
        while (resultSet.next()) {
            images.add(resultSet.getString("photo"));
        }
        if (!images.isEmpty()) {
            Assertions.assertNotNull(images);
        }
    }

    @Test
    void readSelectPhotoCar() throws SQLException {
        System.out.println("Test pobierający listę zdjęć dla konkretnego auta ...");
        ArrayList<String> images = new ArrayList<>();
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT DISTINCT c.id_car, l.id_loan, p.photo FROM loans l " +
                "INNER JOIN cars c on c.id_car = l.id_car " +
                "INNER JOIN photo_car pc on c.id_car = pc.id_car " +
                "INNER JOIN photos p on pc.id_photo = p.id_photo " +
                "WHERE l.id_car = 1");
        while (resultSet.next()) {
            images.add(resultSet.getString("photo"));
        }
        if (!images.isEmpty()) {
            Assertions.assertNotNull(images);
        }
    }

    @Test
    void readNonPhotoCar() throws SQLException {
        System.out.println("Test pobierający listę zdjęć dla nieistniającego auta ...");
        ArrayList<String> images = new ArrayList<>();
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT DISTINCT c.id_car, l.id_loan, p.photo FROM loans l " +
                "INNER JOIN cars c on c.id_car = l.id_car " +
                "INNER JOIN photo_car pc on c.id_car = pc.id_car " +
                "INNER JOIN photos p on pc.id_photo = p.id_photo " +
                "WHERE l.id_car = 0");
        if (resultSet.next()) {
            images.add(resultSet.getString("photo"));
        } else {
            Assertions.assertEquals(true, images.isEmpty());
        }
    }

    @Test
    void readAllDetailsCars() throws SQLException {
        System.out.println("Test pobierający listę szczególów wszystkich aut w wypożyczeniach...");
        ArrayList<String> damages = new ArrayList<>();
        int count = 0;
        ResultSet rs = connection.createStatement().executeQuery("SELECT c.model, c.production_date, c.engine, c.fuel, c.registration_number, c.color, c.cost, " +
                "u.user_name, u.surname, u.phone_number, u.email, l.date_of_loan, l.date_of_delivery, l.id_loan, d.description \n" +
                "FROM cars c \n" +
                "INNER JOIN loans l on c.id_car = l.id_car \n" +
                "INNER JOIN users u on l.id_client = u.id_users \n" +
                "LEFT JOIN damage d on d.id_loan = l.id_loan \n");
        while (rs.next()) {
            System.out.println("Samochod : " + count);
            damages.add(rs.getString("model"));
            damages.add(rs.getString("production_date"));
            damages.add(rs.getString("engine"));
            damages.add(rs.getString("fuel"));
            damages.add(rs.getString("registration_number"));
            damages.add(rs.getString("color"));
            damages.add(rs.getString("cost"));
            damages.add(rs.getString("user_name"));
            damages.add(rs.getString("surname"));
            damages.add(rs.getString("phone_number"));
            damages.add(rs.getString("email"));
            damages.add(rs.getString("date_of_loan"));
            damages.add(rs.getString("date_of_delivery"));
            damages.add(rs.getString("description"));
            System.out.println(damages);
            count++;
        }  if (!damages.isEmpty()){
            Assertions.assertNotNull(damages);
        }
    }

    @Test
    void readAllDetailsCar() throws SQLException {
        System.out.println("Test pobierający listę szczególów konkretnego auta w wypożyczeniach...");
        ArrayList<String> damages1 = new ArrayList<>();
        int count = 0;
        String marka = "Volkswagen Passat B6";
        ResultSet rs = connection.createStatement().executeQuery("SELECT c.model, c.production_date, c.engine, c.fuel, c.registration_number, c.color, c.cost, " +
                "u.user_name, u.surname, u.phone_number, u.email, l.date_of_loan, l.date_of_delivery, l.id_loan, d.description \n" +
                "FROM cars c \n" +
                "INNER JOIN loans l on c.id_car = l.id_car \n" +
                "INNER JOIN users u on l.id_client = u.id_users \n" +
                "LEFT JOIN damage d on d.id_loan = l.id_loan \n" +
                "WHERE c.id_car = '"+marka+"'");
        while (rs.next()) {
            System.out.println("Samochod : " + count);
            damages1.add(rs.getString("model"));
            damages1.add(rs.getString("production_date"));
            damages1.add(rs.getString("engine"));
            damages1.add(rs.getString("fuel"));
            damages1.add(rs.getString("registration_number"));
            damages1.add(rs.getString("color"));
            damages1.add(rs.getString("cost"));
            damages1.add(rs.getString("user_name"));
            damages1.add(rs.getString("surname"));
            damages1.add(rs.getString("phone_number"));
            damages1.add(rs.getString("email"));
            damages1.add(rs.getString("date_of_loan"));
            damages1.add(rs.getString("date_of_delivery"));
            damages1.add(rs.getString("description"));
            System.out.println(damages1);
            count++;
        }  if (!damages1.isEmpty()){
            Assertions.assertNotNull(damages1);
        }
    }

    @Test
    void NonReadAllDetailsCar() throws SQLException {
        System.out.println("Test pobierający listę szczególów nieistniającego auta w wypożyczeniach...");
        ArrayList<String> damages2 = new ArrayList<>();
        int count = 0;
        String marka = "Volkswagen Passat B8";
        ResultSet rs = connection.createStatement().executeQuery("SELECT c.model, c.production_date, c.engine, c.fuel, c.registration_number, c.color, c.cost, " +
                "u.user_name, u.surname, u.phone_number, u.email, l.date_of_loan, l.date_of_delivery, l.id_loan, d.description \n" +
                "FROM cars c \n" +
                "INNER JOIN loans l on c.id_car = l.id_car \n" +
                "INNER JOIN users u on l.id_client = u.id_users \n" +
                "LEFT JOIN damage d on d.id_loan = l.id_loan \n" +
                "WHERE c.id_car = '"+marka+"'");
        if (rs.next()) {
            System.out.println("Samochod : " + count);
            damages2.add(rs.getString("model"));
            damages2.add(rs.getString("production_date"));
            damages2.add(rs.getString("engine"));
            damages2.add(rs.getString("fuel"));
            damages2.add(rs.getString("registration_number"));
            damages2.add(rs.getString("color"));
            damages2.add(rs.getString("cost"));
            damages2.add(rs.getString("user_name"));
            damages2.add(rs.getString("surname"));
            damages2.add(rs.getString("phone_number"));
            damages2.add(rs.getString("email"));
            damages2.add(rs.getString("date_of_loan"));
            damages2.add(rs.getString("date_of_delivery"));
            damages2.add(rs.getString("description"));
            System.out.println(damages2);
            count++;
        } else {
            Assertions.assertEquals(true, damages2.isEmpty());
        }
    }

    @Test
    void initialize() {
    }
}