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

class AdminLoansControllerTest {

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
    void getAllLoans() throws SQLException {
        System.out.println("Test pobierający liste wszystkich wypożyczen z bazy danych");
        ArrayList<String> loans = new ArrayList<>();
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT l.id_loan, u.user_name, u.surname, l.date_of_loan, l.date_of_delivery," +
                " a.city, c.model, l.status, l.cost FROM agency a, loans l, cars c," +
                " users u WHERE l.id_client = u.id_users AND l.id_car = c.id_car AND a.id_agency = l.agency_of_loan;");
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
    void changeStatusButton() {
        System.out.println("Test sprawdzający zmiane statusu wypożyczenia");

        String status = "Zwrócony";
        int id_loan = 1;

        try {
            Connection connection = DataBase.connect();
            String query = "update loans set status = ? where id_loan = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, id_loan);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}