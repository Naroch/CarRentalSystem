package controllers.admin;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.DataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class AdminEditCarControllerTest {

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
    void initialize() {
    }

    @Test
    void adminZapiszEdycjePojazduButton() {
        System.out.println("Test sprawdzający edycję pojazdu");

        String model = "VW";
        int production_date = 192;
        String engine = "Silnik";
        int power = 150;
        String fuel = "Benzyna";
        String transmission = "Automatyczna";
        String color = "Biały";
        int cost = 100;
        String registration_number = "RZ123";

        try {
            Connection connection = DataBase.connect();
            String query = "update cars set model = ?, production_date = ?, engine = ?, power = ?, fuel = ?," +
                    " transmission =?,  color=?, cost=?, registration_number=? where id_car = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, model);
            preparedStatement.setInt(2, production_date);
            preparedStatement.setString(3, engine);
            preparedStatement.setInt(4, power);
            preparedStatement.setString(5, fuel);
            preparedStatement.setString(6, transmission);
            preparedStatement.setString(7, color);
            preparedStatement.setInt(8, cost);
            preparedStatement.setString(9, registration_number);
            preparedStatement.setInt(10, 1);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}