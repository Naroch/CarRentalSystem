package controllers.admin;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.DataBase;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class AdminShowCarControllerTest {

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
    void adminZmienStatusButton() {
        System.out.println("Test sprawdzający zmianę statusu auta");
        try {
            Connection connection = DataBase.connect();
            String query = "update cars set status = ? where id_car = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            String status = "dostępny";

            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, 1);
            preparedStatement.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}