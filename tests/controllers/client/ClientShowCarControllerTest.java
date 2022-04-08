package controllers.client;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.DataBase;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ClientShowCarControllerTest {

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
    void zarezerwujAuto() {
        System.out.println("Test rezerwacji auta przez klienta");
        DataBase.InsertInToLoansTable(1, 1, 1, 1, "status", "1999-2-12",
                "1999-2-12", null, 1, 1, 150);
    }
}