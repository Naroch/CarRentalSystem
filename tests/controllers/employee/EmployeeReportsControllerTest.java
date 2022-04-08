package controllers.employee;

import models.Report;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.DataBase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeReportsControllerTest {
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
        System.out.println("Polaczenie zamkniete ");
    }


    @Test
    void DateFromDatabase() throws SQLException {
        System.out.println("Pobieranie wszystkich dannych dla raportu ...");
        ArrayList<String> data = new ArrayList<>();
        String date_from, date_to;
        date_from="2021-03-01";
        date_to="2021-05-31";
        ResultSet rs1 = connection.createStatement().executeQuery("select u.user_name, u.surname, c.model, l.date_of_loan, l.date_of_expiry, l.cost from users u, loans l, cars c where c.id_car=l.id_car AND id_client=id_users AND date_of_loan BETWEEN '" + date_from + "' AND '" + date_to + "'");
        while (rs1.next()) {
            data.add(rs1.getString("user_name"));
            data.add(rs1.getString("surname"));
            data.add(rs1.getString("model"));
            data.add(rs1.getString("date_of_loan"));
            data.add(rs1.getString("date_of_expiry"));
            data.add(rs1.getString("cost"));
            System.out.println(data);

        }



        if (!data.isEmpty()) {
            Assertions.assertNotNull(data);
        }

    }

    @Test
    void DateFromDatabaseEmpty() throws SQLException {
        System.out.println("Pobieranie wszystkich dannych dla raportu ...");
        ArrayList<String> data = new ArrayList<>();
        String date_from, date_to;
        date_from="2002-03-01";
        date_to="2002-05-31";
        ResultSet rs1 = connection.createStatement().executeQuery("select u.user_name, u.surname, c.model, l.date_of_loan, l.date_of_expiry, l.cost from users u, loans l, cars c where c.id_car=l.id_car AND id_client=id_users AND date_of_loan BETWEEN '" + date_from + "' AND '" + date_to + "'");
        if (rs1.next()) {
            data.add(rs1.getString("user_name"));
            data.add(rs1.getString("surname"));
            data.add(rs1.getString("model"));
            data.add(rs1.getString("date_of_loan"));
            data.add(rs1.getString("date_of_expiry"));
            data.add(rs1.getString("cost"));
            System.out.println(data);

        }

        else {
            Assertions.assertEquals(true, data.isEmpty());
        }

    }

    @Test
    void initialize() {

    }
}