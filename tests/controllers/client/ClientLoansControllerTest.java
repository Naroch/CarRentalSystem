package controllers.client;

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

class ClientLoansControllerTest {

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
    void getClientLoans() throws SQLException {
        System.out.println("Test pobierający liste wypożyczen danego klienta");
        int client = 10;
        ArrayList<String> loans = new ArrayList<>();
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT l.id_loan, id_client, u.user_name, u.surname, l.date_of_loan, l.date_of_delivery," +
                " a.city, c.model, l.status, l.cost FROM agency a, loans l, cars c," +
                " users u WHERE l.id_client = u.id_users AND l.id_car = c.id_car AND a.id_agency = l.agency_of_loan " +
                "AND id_client = "+client+";");
        while (resultSet.next()) {
            loans.add(resultSet.getString("id_loan"));
            loans.add(resultSet.getString("user_name"));
            loans.add(resultSet.getString("surname"));
            loans.add(resultSet.getString("date_of_loan"));
            loans.add(resultSet.getString("date_of_delivery"));
            loans.add(resultSet.getString("city"));
            loans.add(resultSet.getString("model"));
            loans.add(resultSet.getString("status"));
            loans.add(resultSet.getString("cost"));
        }
        if (!loans.isEmpty()) {
            Assertions.assertNotNull(loans);
        }

    }

    @Test
    void getNoExistingClientLoans() throws SQLException {
        System.out.println("Test pobierający liste wypożyczen nie istneijacego klienta");
        int client = 10000;
        ArrayList<String> loans = new ArrayList<>();
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT l.id_loan, id_client, u.user_name, u.surname, l.date_of_loan, l.date_of_delivery," +
                " a.city, c.model, l.status, l.cost FROM agency a, loans l, cars c," +
                " users u WHERE l.id_client = u.id_users AND l.id_car = c.id_car AND a.id_agency = l.agency_of_loan " +
                "AND id_client = "+client+";");
        if (resultSet.next()) {
            loans.add(resultSet.getString("id_loan"));
            loans.add(resultSet.getString("user_name"));
            loans.add(resultSet.getString("surname"));
            loans.add(resultSet.getString("date_of_loan"));
            loans.add(resultSet.getString("date_of_delivery"));
            loans.add(resultSet.getString("city"));
            loans.add(resultSet.getString("model"));
            loans.add(resultSet.getString("status"));
            loans.add(resultSet.getString("cost"));
        }else {
            Assertions.assertEquals(true, loans.isEmpty());
        }

    }

    @Test
    void initialize() {
    }

    @Test
    void cancelReservationButton() {
        System.out.println("Test sprawdzający anulowanie rezerwacji");


        int id_loan = 1;

        try {
            Connection connection = DataBase.connect();
            String query = "delete from loans where id_loan = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id_loan);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}