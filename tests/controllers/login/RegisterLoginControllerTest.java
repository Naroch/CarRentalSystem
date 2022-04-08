package controllers.login;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.DataBase;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;

import static controllers.login.RegisterController.validate;

class RegisterLoginControllerTest {
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
    void shouldBeEmailCheck() {
        System.out.println("Test sprawdzający czy string jest emailem");
        String email = "email@gmail.com";
        String email1 = "email!gmail.com";
        Assertions.assertTrue(validate(email));
        Assertions.assertFalse(validate(email1));
    }

    @Test
    void writeRegisterUsers() throws FileNotFoundException, SQLException {
        System.out.println("Test zapisujący nowego użytkownika");
        String user_name = "Wojciech";
        String surname = "Wrona";
        String login = "WojciechW";
        String password = "0192023a7bbd73250516f069df18b500";
        String email = "wojciechw@gmail.com";
        String phone_number = "904647976";
        String pathToDir = System.getProperty("user.dir");
        System.out.println(System.getProperty("user.dir"));
        InputStream document = new FileInputStream(pathToDir+"/src/images/prawo-jazdy-wzor.jpg");
        String sql = "INSERT INTO users (user_name, surname, login, password, role, email, phone_number, document) VALUES (?, ?, ?, ?, 0, ?, ?, ?);";
        PreparedStatement insert_statement= DataBase.connect().prepareStatement(sql);
        insert_statement.setString(1,user_name);
        insert_statement.setString(2, surname);
        insert_statement.setString(3, login);
        insert_statement.setString(4, password);
        insert_statement.setString(5, email);
        insert_statement.setString(6, phone_number);
        insert_statement.setBlob(7, document);
        insert_statement.executeUpdate();
        Assertions.assertFalse(insert_statement.execute());
    }

    @Test
    void readUsersTest() throws SQLException {
        System.out.println("Test pobierający użytkowników systemu");
        ArrayList<String> users = new ArrayList<String>();
        ResultSet rs = connection.createStatement().executeQuery("select * from users");
        while (rs.next()) {
            users.add(rs.getString( "user_name"));
            users.add(rs.getString( "surname"));
            users.add(rs.getString( "login"));
            users.add(rs.getString( "email"));
            users.add(rs.getString( "phone_number"));
            System.out.println(users);
        }
        if (!users.isEmpty()){
            Assertions.assertNotNull(users);
        }
    }

    @Test
    void readNonExistingUserTest() throws SQLException {
        System.out.println("Test pobierający nieistniającego użytkownika systemu");
        ArrayList<String> users = new ArrayList<String>();
        String email = "mailnieist@gmail.com";
        ResultSet rs = connection.createStatement().executeQuery("select * from users u where u.email = '"+email+"'");
        if (rs.next()) {
            users.add(rs.getString("user_name"));
            System.out.println(users);
        } else {
            Assertions.assertEquals(true, users.isEmpty());
        }
    }

    @Test
    void readEmployeeUserTest() throws SQLException {
        System.out.println("Test pobierający pracownika: ");
        ArrayList<String> employee = new ArrayList<>();
        String login = "DmytroP";
        ResultSet rs = connection.createStatement().executeQuery("select * from users u where u.login = '"+login+"'");
        if (rs.next()) {
            System.out.print("Pracownik: ");
            employee.add(rs.getString("user_name"));
            employee.add(rs.getString("surname"));
            employee.add(rs.getString("login"));
            employee.add(rs.getString("email"));
            employee.add(rs.getString("role"));
            employee.add(rs.getString("phone_number"));
            System.out.println(employee);
        } else {
            Assertions.assertEquals(true, !employee.isEmpty());
        }
    }

    @Test
    void readAdminUserTest() throws SQLException {
        System.out.println("Test pobierający pracownika: ");
        ArrayList<String> admin = new ArrayList<>();
        int role = 2;
        ResultSet rs = connection.createStatement().executeQuery("select * from users u where u.role = '"+role+"'");
        if (rs.next()) {
            System.out.print("Administrator: ");
            admin.add(rs.getString("user_name"));
            admin.add(rs.getString("surname"));
            admin.add(rs.getString("login"));
            admin.add(rs.getString("email"));
            admin.add(rs.getString("role"));
            admin.add(rs.getString("phone_number"));
            System.out.println(admin);
        } else {
            Assertions.assertEquals(true, !admin.isEmpty());
        }
    }
}