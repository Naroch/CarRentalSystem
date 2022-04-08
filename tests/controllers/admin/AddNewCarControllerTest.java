package controllers.admin;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.DataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class AddNewCarControllerTest {

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
    void addCar() throws SQLException {
        System.out.println("Test dodajacy nowe auto do bazy");
        String model = "VW";
        int production_date = 192;
        String engine = "Silnik";
        int power = 150;
        String fuel = "Benzyna";
        String transmission = "Automatyczna";
        String color = "Biały";
        int cost = 100;
        String registration_number = "RZ123";
        String status = "niedostepny";
        int id_agency = 1;

        String sql = "INSERT INTO cars VALUES (null,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, model);
        ps.setInt(2, production_date);
        ps.setString(3, engine);
        ps.setInt(4, power);
        ps.setString(5, fuel);
        ps.setString(6, transmission);
        ps.setString(7, color);
        ps.setInt(8, cost);
        ps.setString(9, registration_number);
        ps.setString(10, status);
        ps.setInt(11, id_agency);
        ps.execute();
        if (ps != null) {
            Assertions.assertNotNull(ps);

        }
    }
}