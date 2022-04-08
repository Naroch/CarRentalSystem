package service;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;

public class DataBase {
    private static final String URL =
            "jdbc:mysql://localhost:3306/samochody?autoReconnect=true&useSSL=false&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public DataBase() {
    }

    /**
     * Funkcja łączy się z bazą danych
     * @return zwraca connection, jesli true
     *         zwraca null, jesli false
     */
    public static Connection connect() {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/samochody?autoReconnect=true&useSSL=false&serverTimezone=UTC",
                    "root", "");
            if (connection == null) {
                throw new SQLException();
            } else {

                return connection;
            }
        } catch (SQLException var1) {
            System.out.println("CONNECTION ERROR WITH DATA BASE");
            return null;
        }
    }

    /**
     * Funkcja tworzy bazę danych, jeśli nie istnieje
     * @return zwraca true lub false
     */
    public static boolean createDatabase() {
        String dbUrl = "jdbc:mysql://localhost:3306";
        String query = "CREATE DATABASE IF NOT EXISTS samochody";
        Statement st = null;
        Connection con = null;
        try {
            con = DriverManager.getConnection(dbUrl, "root", "");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            st = con.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            if (st.executeUpdate(query) == 1) { // Then database created
                JOptionPane.showMessageDialog(null, "Stworzono bazę");
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    /**
     * Funkcja tworzy tabelę Cars oraz Constrainty
     */
    public static void checkIfCarsExist() {
        Connection connection = connect();
        try {
            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT id_car FROM cars LIMIT 1");
        } catch (SQLException throwables) {
            createCarsTable();
            addConstraintCars();
        }
    }

    public static void createCarsTable() {
        try {
            Connection connection = connect();
            Throwable var1 = null;

            try {
                Statement statement = connection.createStatement();
                Throwable var3 = null;

                try {
                    if (statement != null) {
                        statement.execute("CREATE TABLE IF NOT EXISTS cars (id_car INTEGER PRIMARY KEY auto_increment, model TEXT," +
                                " production_date INTEGER, engine TEXT, power INTEGER, fuel TEXT, transmission TEXT," +
                                " color TEXT, cost INTEGER, registration_number TEXT, status TEXT, id_agency INTEGER)");
                    }
                } catch (Throwable var28) {
                    var3 = var28;
                    throw var28;
                } finally {
                    if (statement != null) {
                        if (var3 != null) {
                            try {
                                statement.close();
                            } catch (Throwable var27) {
                                var3.addSuppressed(var27);
                            }
                        } else {
                            statement.close();
                        }
                    }

                }
            } catch (Throwable var30) {
                var1 = var30;
                throw var30;
            } finally {
                if (connection != null) {
                    if (var1 != null) {
                        try {
                            connection.close();
                        } catch (Throwable var26) {
                            var1.addSuppressed(var26);
                        }
                    } else {
                        connection.close();
                    }
                }
            }
        } catch (SQLException var32) {
            var32.printStackTrace();
        }
    }

    public static void addConstraintCars() {
        Connection connection = connect();
        try {
            Statement statement = connection.createStatement();
            statement.execute("ALTER TABLE `cars`" +
                    "  ADD CONSTRAINT `cars_ibfk_1` FOREIGN KEY (`id_agency`) REFERENCES `agency` (`id_agency`) ON DELETE SET NULL ON UPDATE CASCADE;");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void createAgencyTable() {
        try {
            Connection connection = connect();
            Throwable var1 = null;

            try {
                Statement statement = connection.createStatement();
                Throwable var3 = null;

                try {
                    if (statement != null) {
                        statement.execute("CREATE TABLE IF NOT EXISTS agency (id_agency INTEGER PRIMARY KEY auto_increment," +
                                " agency_name TEXT, city TEXT, road TEXT, building_number TEXT, post_code TEXT," +
                                " email TEXT, phone_number TEXT)");
                    }
                } catch (Throwable var28) {
                    var3 = var28;
                    throw var28;
                } finally {
                    if (statement != null) {
                        if (var3 != null) {
                            try {
                                statement.close();
                            } catch (Throwable var27) {
                                var3.addSuppressed(var27);
                            }
                        } else {
                            statement.close();
                        }
                    }

                }
            } catch (Throwable var30) {
                var1 = var30;
                throw var30;
            } finally {
                if (connection != null) {
                    if (var1 != null) {
                        try {
                            connection.close();
                        } catch (Throwable var26) {
                            var1.addSuppressed(var26);
                        }
                    } else {
                        connection.close();
                    }
                }

            }
        } catch (SQLException var32) {
            var32.printStackTrace();
        }

    }


    /**
     * Funkcja tworzy tabelę Users oraz Constrainty
     */
    public static void checkIfUsersExist() {
        Connection connection = connect();
        try {
            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT id_users FROM users LIMIT 1");
        } catch (SQLException throwables) {
            createUsersTable();
            addConstraintUsers();
        }
    }

    public static void createUsersTable() {
        try {
            Connection connection = connect();
            Throwable var1 = null;

            try {
                Statement statement = connection.createStatement();
                Throwable var3 = null;

                try {
                    if (statement != null) {
                        statement.execute("CREATE TABLE IF NOT EXISTS users (id_users INTEGER PRIMARY KEY auto_increment," +
                                " user_name TEXT, surname TEXT, login TEXT, password TEXT, role INTEGER, email TEXT," +
                                " phone_number TEXT, document MEDIUMBLOB, id_agency INTEGER, active BOOLEAN)");
                    }
                } catch (Throwable var28) {
                    var3 = var28;
                    throw var28;
                } finally {
                    if (statement != null) {
                        if (var3 != null) {
                            try {
                                statement.close();
                            } catch (Throwable var27) {
                                var3.addSuppressed(var27);
                            }
                        } else {
                            statement.close();
                        }
                    }

                }
            } catch (Throwable var30) {
                var1 = var30;
                throw var30;
            } finally {
                if (connection != null) {
                    if (var1 != null) {
                        try {
                            connection.close();
                        } catch (Throwable var26) {
                            var1.addSuppressed(var26);
                        }
                    } else {
                        connection.close();
                    }
                }

            }
        } catch (SQLException var32) {
            var32.printStackTrace();
        }
    }

    public static void addConstraintUsers() {
        Connection connection = connect();
        try {
            Statement statement = connection.createStatement();
            statement.execute("ALTER TABLE `users`" +
                    "  ADD CONSTRAINT `users_ibfk_1` FOREIGN KEY (`id_agency`) REFERENCES `agency` (`id_agency`) ON DELETE SET NULL ON UPDATE CASCADE;");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Funkcja tworzy tabelę Loans oraz Constrainty
     */
    public static void checkIfLoansExist() {
        Connection connection = connect();
        try {
            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT id_loan FROM loans LIMIT 1");
        } catch (SQLException throwables) {
            createLoansTable();
            addConstraintLoans();
        }
    }

    public static void createLoansTable() {
        try {
            Connection connection = connect();
            Throwable var1 = null;

            try {
                Statement statement = connection.createStatement();
                Throwable var3 = null;

                try {
                    if (statement != null) {
                        statement.execute("CREATE TABLE IF NOT EXISTS loans (id_loan INTEGER PRIMARY KEY auto_increment," +
                                " id_client INTEGER," +
                                " id_emphandover INTEGER," +
                                " id_empreturn INTEGER," +
                                " id_car INTEGER," +
                                " status TEXT, " +
                                " date_of_loan DATE," +
                                " date_of_expiry DATE," +
                                " date_of_delivery DATE," +
                                " agency_of_loan INTEGER," +
                                " agency_of_delivery INTEGER," +
                                " cost INTEGER)");

                    }
                } catch (Throwable var28) {
                    var3 = var28;
                    throw var28;
                } finally {
                    if (statement != null) {
                        if (var3 != null) {
                            try {
                                statement.close();
                            } catch (Throwable var27) {
                                var3.addSuppressed(var27);
                            }
                        } else {
                            statement.close();
                        }
                    }

                }
            } catch (Throwable var30) {
                var1 = var30;
                throw var30;
            } finally {
                if (connection != null) {
                    if (var1 != null) {
                        try {
                            connection.close();
                        } catch (Throwable var26) {
                            var1.addSuppressed(var26);
                        }
                    } else {
                        connection.close();
                    }
                }

            }
        } catch (SQLException var32) {
            var32.printStackTrace();
        }
    }

    public static void addConstraintLoans() {
        Connection connection = connect();
        try {
            Statement statement = connection.createStatement();
            statement.execute("ALTER TABLE `loans`" +
                    "  ADD CONSTRAINT `loans_ibfk_3` FOREIGN KEY (`id_car`) REFERENCES `cars` (`id_car`) ON DELETE CASCADE ON UPDATE CASCADE,\n" +
                    "  ADD CONSTRAINT `loans_ibfk_4` FOREIGN KEY (`id_client`) REFERENCES `users` (`id_users`) ON DELETE CASCADE ON UPDATE CASCADE,\n" +
                    "  ADD CONSTRAINT `loans_ibfk_5` FOREIGN KEY (`agency_of_loan`) REFERENCES `agency` (`id_agency`) ON DELETE SET NULL ON UPDATE CASCADE,\n" +
                    "  ADD CONSTRAINT `loans_ibfk_6` FOREIGN KEY (`agency_of_delivery`) REFERENCES `agency` (`id_agency`) ON DELETE SET NULL ON UPDATE CASCADE,\n" +
                    "  ADD CONSTRAINT `loans_ibfk_7` FOREIGN KEY (`id_emphandover`) REFERENCES `users` (`id_users`) ON DELETE SET NULL ON UPDATE CASCADE,\n" +
                    "  ADD CONSTRAINT `loans_ibfk_8` FOREIGN KEY (`id_empreturn`) REFERENCES `users` (`id_users`) ON DELETE SET NULL ON UPDATE CASCADE;");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Funkcja tworzy tabelę Damage oraz Constrainty
     */
    //create table Damage
    public static void checkIfDamageExist() {
        Connection connection = connect();
        try {
            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT id_damage FROM damage LIMIT 1");
        } catch (SQLException throwables) {
            createDamageTable();
            addConstraintDamage();
        }
    }

    public static void createDamageTable() {
        try {
            Connection connection = connect();
            Throwable var1 = null;

            try {
                Statement statement = connection.createStatement();
                Throwable var3 = null;

                try {
                    if (statement != null) {
                        statement.execute("CREATE TABLE IF NOT EXISTS damage (id_damage INTEGER PRIMARY KEY auto_increment," +
                                " id_loan INTEGER," +
     /*+Unique+*/               " id_car INTEGER UNIQUE," +
                                " description TEXT, damage_cost INTEGER)");

                    }
                } catch (Throwable var28) {
                    var3 = var28;
                    throw var28;
                } finally {
                    if (statement != null) {
                        if (var3 != null) {
                            try {
                                statement.close();
                            } catch (Throwable var27) {
                                var3.addSuppressed(var27);
                            }
                        } else {
                            statement.close();
                        }
                    }

                }
            } catch (Throwable var30) {
                var1 = var30;
                throw var30;
            } finally {
                if (connection != null) {
                    if (var1 != null) {
                        try {
                            connection.close();
                        } catch (Throwable var26) {
                            var1.addSuppressed(var26);
                        }
                    } else {
                        connection.close();
                    }
                }

            }
        } catch (SQLException var32) {
            var32.printStackTrace();
        }
    }

    public static void addConstraintDamage() {
        Connection connection = connect();
        try {
            Statement statement = connection.createStatement();
            statement.execute("ALTER TABLE `damage`" +
                    "  ADD CONSTRAINT `damage_ibfk_1` FOREIGN KEY (`id_loan`) REFERENCES `loans` (`id_loan`) ON DELETE CASCADE ON UPDATE CASCADE," +
                    "  ADD CONSTRAINT `damage_ibfk_2` FOREIGN KEY (`id_car`) REFERENCES `cars` (`id_car`) ON DELETE CASCADE ON UPDATE CASCADE;");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Funkcja tworzy tabelę Photo_Damage oraz Constrainty
     */
    //create table Photo_Damage
    public static void checkIfPhoto_DamageExist() {
        Connection connection = connect();
        try {
            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT id_photo FROM photo_damage LIMIT 1");
        } catch (SQLException throwables) {
            createPhoto_DamageTable();
            addConstraintPhoto_Damage();
        }
    }

    public static void createPhoto_DamageTable() {
        try {
            Connection connection = connect();
            Throwable var1 = null;

            try {
                Statement statement = connection.createStatement();
                Throwable var3 = null;

                try {
                    if (statement != null) {
                        statement.execute("CREATE TABLE IF NOT EXISTS photo_damage (id_photo INTEGER, id_damage INTEGER)");


                    }
                } catch (Throwable var28) {
                    var3 = var28;
                    throw var28;
                } finally {
                    if (statement != null) {
                        if (var3 != null) {
                            try {
                                statement.close();
                            } catch (Throwable var27) {
                                var3.addSuppressed(var27);
                            }
                        } else {
                            statement.close();
                        }
                    }

                }
            } catch (Throwable var30) {
                var1 = var30;
                throw var30;
            } finally {
                if (connection != null) {
                    if (var1 != null) {
                        try {
                            connection.close();
                        } catch (Throwable var26) {
                            var1.addSuppressed(var26);
                        }
                    } else {
                        connection.close();
                    }
                }

            }
        } catch (SQLException var32) {
            var32.printStackTrace();
        }
    }

    public static void addConstraintPhoto_Damage() {
        Connection connection = connect();
        try {
            Statement statement = connection.createStatement();
            statement.execute("ALTER TABLE `photo_damage`" +
                    "  ADD CONSTRAINT `photo_damage_ibfk_2` FOREIGN KEY (`id_photo`) REFERENCES `photos` (`id_photo`) ON DELETE CASCADE ON UPDATE CASCADE,\n" +
                    "  ADD CONSTRAINT `photo_damage_ibfk_3` FOREIGN KEY (`id_damage`) REFERENCES `damage` (`id_damage`) ON DELETE CASCADE ON UPDATE CASCADE;");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void createPhotosTable() {
        try {
            Connection connection = connect();
            Throwable var1 = null;

            try {
                Statement statement = connection.createStatement();
                Throwable var3 = null;

                try {
                    if (statement != null) {
                        statement.execute("CREATE TABLE IF NOT EXISTS photos (id_photo INTEGER PRIMARY KEY auto_increment," +
                                " photo mediumblob)");

                    }
                } catch (Throwable var28) {
                    var3 = var28;
                    throw var28;
                } finally {
                    if (statement != null) {
                        if (var3 != null) {
                            try {
                                statement.close();
                            } catch (Throwable var27) {
                                var3.addSuppressed(var27);
                            }
                        } else {
                            statement.close();
                        }
                    }

                }
            } catch (Throwable var30) {
                var1 = var30;
                throw var30;
            } finally {
                if (connection != null) {
                    if (var1 != null) {
                        try {
                            connection.close();
                        } catch (Throwable var26) {
                            var1.addSuppressed(var26);
                        }
                    } else {
                        connection.close();
                    }
                }

            }
        } catch (SQLException var32) {
            var32.printStackTrace();
        }
    }

    /**
     * Funkcja tworzy tabelę PhotoCar oraz Constrainty
     */
    //create table PhotoCar
    public static void checkIfPhotoCarExist() {
        Connection connection = connect();
        try {
            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT id_photo FROM photo_car LIMIT 1");
        } catch (SQLException throwables) {
            createPhoto_CarTable();
            addConstraintPhotoCar();
        }
    }

    public static void createPhoto_CarTable() {
        try {
            Connection connection = connect();
            Throwable var1 = null;

            try {
                Statement statement = connection.createStatement();
                Throwable var3 = null;

                try {
                    if (statement != null) {
                        statement.execute("CREATE TABLE IF NOT EXISTS photo_car (id_photo INTEGER, id_car INTEGER)");

                    }
                } catch (Throwable var28) {
                    var3 = var28;
                    throw var28;
                } finally {
                    if (statement != null) {
                        if (var3 != null) {
                            try {
                                statement.close();
                            } catch (Throwable var27) {
                                var3.addSuppressed(var27);
                            }
                        } else {
                            statement.close();
                        }
                    }

                }
            } catch (Throwable var30) {
                var1 = var30;
                throw var30;
            } finally {
                if (connection != null) {
                    if (var1 != null) {
                        try {
                            connection.close();
                        } catch (Throwable var26) {
                            var1.addSuppressed(var26);
                        }
                    } else {
                        connection.close();
                    }
                }

            }
        } catch (SQLException var32) {
            var32.printStackTrace();
        }
    }

    public static void addConstraintPhotoCar() {
        Connection connection = connect();
        try {
            Statement statement = connection.createStatement();
            statement.execute("ALTER TABLE `photo_car`" +
                    "  ADD CONSTRAINT `photo_car_ibfk_1` FOREIGN KEY (`id_car`) REFERENCES `cars` (`id_car`) ON DELETE CASCADE ON UPDATE CASCADE," +
                    "  ADD CONSTRAINT `photo_car_ibfk_2` FOREIGN KEY (`id_photo`) REFERENCES `photos` (`id_photo`) ON DELETE CASCADE ON UPDATE CASCADE;");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Funkcja wprowadza dane do tabeli Cars
     * @param model
     * @param production_date
     * @param engine
     * @param power
     * @param fuel
     * @param transmission
     * @param color
     * @param cost
     * @param registration_number
     * @param status
     * @param id_agency
     */
    public static void InsertInToCarsTable(String model, int production_date, String engine,
                                           int power, String fuel, String transmission, String color,
                                           int cost, String registration_number, String status, int id_agency) {
        try {
            Connection connection = connect();
            Throwable var6 = null;

            try {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO cars VALUES (null,?,?,?,?,?,?,?,?,?,?,?)");
                Throwable var8 = null;

                try {
                    if (ps != null) {
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
                    }
                } catch (Throwable var33) {
                    var8 = var33;
                    throw var33;
                } finally {
                    if (ps != null) {
                        if (var8 != null) {
                            try {
                                ps.close();
                            } catch (Throwable var32) {
                                var8.addSuppressed(var32);
                            }
                        } else {
                            ps.close();
                        }
                    }

                }
            } catch (Throwable var35) {
                var6 = var35;
                throw var35;
            } finally {
                if (connection != null) {
                    if (var6 != null) {
                        try {
                            connection.close();
                        } catch (Throwable var31) {
                            var6.addSuppressed(var31);
                        }
                    } else {
                        connection.close();
                    }
                }
            }
        } catch (SQLException var37) {
            var37.printStackTrace();
        }
    }

    /**
     * Funkcja wprowadza dane do tabeli Agency
     * @param agency_name
     * @param city
     * @param road
     * @param building_number
     * @param post_code
     * @param email
     * @param phone_number
     */
    public static void InsertInToAgencyTable(String agency_name, String city, String road,
                                             String building_number, String post_code, String email, String phone_number) {
        try {
            Connection connection = connect();
            Throwable var6 = null;

            try {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO agency VALUES (null,?,?,?,?,?,?,?)");
                Throwable var8 = null;

                try {
                    if (ps != null) {
                        ps.setString(1, agency_name);
                        ps.setString(2, city);
                        ps.setString(3, road);
                        ps.setString(4, building_number);
                        ps.setString(5, post_code);
                        ps.setString(6, email);
                        ps.setString(7, phone_number);
                        ps.execute();
                    }
                } catch (Throwable var33) {
                    var8 = var33;
                    throw var33;
                } finally {
                    if (ps != null) {
                        if (var8 != null) {
                            try {
                                ps.close();
                            } catch (Throwable var32) {
                                var8.addSuppressed(var32);
                            }
                        } else {
                            ps.close();
                        }
                    }

                }
            } catch (Throwable var35) {
                var6 = var35;
                throw var35;
            } finally {
                if (connection != null) {
                    if (var6 != null) {
                        try {
                            connection.close();
                        } catch (Throwable var31) {
                            var6.addSuppressed(var31);
                        }
                    } else {
                        connection.close();
                    }
                }
            }
        } catch (SQLException var37) {
            var37.printStackTrace();
        }
    }

    /**
     * Funkcja wprowadza dane do tabeli Users
     * @param user_name
     * @param surname
     * @param login
     * @param password
     * @param role
     * @param email
     * @param phone_number
     * @param document
     * @param id_agency
     * @param active
     */
    public static void InsertInToUsersTable(String user_name, String surname, String login,
                                            String password, int role, String email, String phone_number,
                                            String document, int id_agency, boolean active) {
        try {
            Connection connection = connect();
            Throwable var6 = null;

            try {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO users VALUES (null,?,?,?,?,?,?,?,?,?,?)");
                Throwable var8 = null;

                try {
                    if (ps != null) {
                        ps.setString(1, user_name);
                        ps.setString(2, surname);
                        ps.setString(3, login);
                        ps.setString(4, password);
                        ps.setInt(5, role);
                        ps.setString(6, email);
                        ps.setString(7, phone_number);
                        ps.setString(8, document);
                        ps.setInt(9, id_agency);
                        ps.setBoolean(10,active);
                        ps.execute();
                    }
                } catch (Throwable var33) {
                    var8 = var33;
                    throw var33;
                } finally {
                    if (ps != null) {
                        if (var8 != null) {
                            try {
                                ps.close();
                            } catch (Throwable var32) {
                                var8.addSuppressed(var32);
                            }
                        } else {
                            ps.close();
                        }
                    }

                }
            } catch (Throwable var35) {
                var6 = var35;
                throw var35;
            } finally {
                if (connection != null) {
                    if (var6 != null) {
                        try {
                            connection.close();
                        } catch (Throwable var31) {
                            var6.addSuppressed(var31);
                        }
                    } else {
                        connection.close();
                    }
                }
            }
        } catch (SQLException var37) {
            var37.printStackTrace();
        }
    }

    /**
     * Funkcja wprowadza dane do tabeli Loans
     * @param id_client
     * @param id_emphandover
     * @param id_empreturn
     * @param id_car
     * @param status
     * @param date_of_loan
     * @param date_of_expiry
     * @param date_of_delivery
     * @param agency_of_loan
     * @param agency_of_delivery
     * @param cost
     */
    public static void InsertInToLoansTable(int id_client, Integer id_emphandover, Integer id_empreturn, int id_car,
                                            String status, String date_of_loan, String date_of_expiry, String date_of_delivery,
                                            int agency_of_loan, Integer agency_of_delivery, int cost) {
        try {
            Connection connection = connect();
            Throwable var6 = null;

            try {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO loans VALUES (null,?,?,?,?,?,STR_TO_DATE( ?, '%Y-%m-%d'),STR_TO_DATE( ?, '%Y-%m-%d'),STR_TO_DATE( ?, '%Y-%m-%d'),?,?,?)");
                Throwable var8 = null;

                try {
                    if (ps != null) {
                        ps.setInt(1, id_client);
                        ps.setInt(2, id_emphandover);
                        ps.setInt(3, id_empreturn);
                        ps.setInt(4, id_car);
                        ps.setString(5, status);
                        ps.setString(6, date_of_loan);
                        ps.setString(7, date_of_expiry);
                        ps.setString(8, date_of_delivery);
                        ps.setInt(9, agency_of_loan);
                        ps.setInt(10, agency_of_delivery);
                        ps.setInt(11, cost);
                        ps.execute();
                    }
                } catch (Throwable var33) {
                    var8 = var33;
                    throw var33;
                } finally {
                    if (ps != null) {
                        if (var8 != null) {
                            try {
                                ps.close();
                            } catch (Throwable var32) {
                                var8.addSuppressed(var32);
                            }
                        } else {
                            ps.close();
                        }
                    }

                }
            } catch (Throwable var35) {
                var6 = var35;
                throw var35;
            } finally {
                if (connection != null) {
                    if (var6 != null) {
                        try {
                            connection.close();
                        } catch (Throwable var31) {
                            var6.addSuppressed(var31);
                        }
                    } else {
                        connection.close();
                    }
                }
            }
        } catch (SQLException var37) {
            var37.printStackTrace();
        }
    }

    /**
     * Funkcja wprowadza dane do tabeli Damage
     * @param id_loan
     * @param id_car
     * @param description
     * @param damage_cost
     */
    public static void InsertInToDamageTable(int id_loan, int id_car, String description, int damage_cost) {
        try {
            Connection connection = connect();
            Throwable var6 = null;

            try {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO damage VALUES (null,?,?,?,?)");
                Throwable var8 = null;

                try {
                    if (ps != null) {
                        ps.setInt(1, id_loan);
                        ps.setInt(2, id_car);
                        ps.setString(3, description);
                        ps.setInt(4, damage_cost);
                        ps.execute();
                    }
                } catch (Throwable var33) {
                    var8 = var33;
                    throw var33;
                } finally {
                    if (ps != null) {
                        if (var8 != null) {
                            try {
                                ps.close();
                            } catch (Throwable var32) {
                                var8.addSuppressed(var32);
                            }
                        } else {
                            ps.close();
                        }
                    }

                }
            } catch (Throwable var35) {
                var6 = var35;
                throw var35;
            } finally {
                if (connection != null) {
                    if (var6 != null) {
                        try {
                            connection.close();
                        } catch (Throwable var31) {
                            var6.addSuppressed(var31);
                        }
                    } else {
                        connection.close();
                    }
                }
            }
        } catch (SQLException var37) {
            var37.printStackTrace();
        }
    }

    /**
     * Funkcja wprowadza dane do tabeli Photos
     * @param photo
     */
    public static void InsertInToPhotosTable(FileInputStream photo) {
        try {
            Connection connection = connect();
            Throwable var6 = null;

            try {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO photos VALUES (null,?)");
                Throwable var8 = null;

                try {
                    if (ps != null) {
                        ps.setBlob(1, photo);
                        ps.execute();
                    }
                } catch (Throwable var33) {
                    var8 = var33;
                    throw var33;
                } finally {
                    if (ps != null) {
                        if (var8 != null) {
                            try {
                                ps.close();
                            } catch (Throwable var32) {
                                var8.addSuppressed(var32);
                            }
                        } else {
                            ps.close();
                        }
                    }

                }
            } catch (Throwable var35) {
                var6 = var35;
                throw var35;
            } finally {
                if (connection != null) {
                    if (var6 != null) {
                        try {
                            connection.close();
                        } catch (Throwable var31) {
                            var6.addSuppressed(var31);
                        }
                    } else {
                        connection.close();
                    }
                }
            }
        } catch (SQLException var37) {
            var37.printStackTrace();
        }
    }

    /**
     * Funkcja wprowadza dane do tabeli Photo_car
     * @param id_photo
     * @param id_car
     */
    public static void InsertInToPhoto_carTable(int id_photo, int id_car) {
        try {
            Connection connection = connect();
            Throwable var6 = null;

            try {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO photo_car VALUES (?,?)");
                Throwable var8 = null;

                try {
                    if (ps != null) {
                        ps.setInt(1, id_photo);
                        ps.setInt(2, id_car);
                        ps.execute();
                    }
                } catch (Throwable var33) {
                    var8 = var33;
                    throw var33;
                } finally {
                    if (ps != null) {
                        if (var8 != null) {
                            try {
                                ps.close();
                            } catch (Throwable var32) {
                                var8.addSuppressed(var32);
                            }
                        } else {
                            ps.close();
                        }
                    }

                }
            } catch (Throwable var35) {
                var6 = var35;
                throw var35;
            } finally {
                if (connection != null) {
                    if (var6 != null) {
                        try {
                            connection.close();
                        } catch (Throwable var31) {
                            var6.addSuppressed(var31);
                        }
                    } else {
                        connection.close();
                    }
                }
            }
        } catch (SQLException var37) {
            var37.printStackTrace();
        }
    }

    /**
     * Funkcja wprowadza dane do tabeli PhotoDamage
     * @param id_photo
     * @param id_damage
     */
    public static void InsertInToPhotoDamageTable(int id_photo, int id_damage) {
        try {
            Connection connection = connect();
            Throwable var6 = null;

            try {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO photo_damage VALUES (?,?)");
                Throwable var8 = null;

                try {
                    if (ps != null) {
                        ps.setInt(1, id_photo);
                        ps.setInt(2, id_damage);
                        ps.execute();
                    }
                } catch (Throwable var33) {
                    var8 = var33;
                    throw var33;
                } finally {
                    if (ps != null) {
                        if (var8 != null) {
                            try {
                                ps.close();
                            } catch (Throwable var32) {
                                var8.addSuppressed(var32);
                            }
                        } else {
                            ps.close();
                        }
                    }

                }
            } catch (Throwable var35) {
                var6 = var35;
                throw var35;
            } finally {
                if (connection != null) {
                    if (var6 != null) {
                        try {
                            connection.close();
                        } catch (Throwable var31) {
                            var6.addSuppressed(var31);
                        }
                    } else {
                        connection.close();
                    }
                }
            }
        } catch (SQLException var37) {
            var37.printStackTrace();
        }
    }


    /**
     * Funkcja sprawdza czy tabela jest pusta, jesli tak to wprowadza dane przy pomocy funkcji InsertInTo...
     */
    public static void nullCheckAgency() {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        String qry = "SELECT * From agency ";

        try {
            Connection connection = connect();
            stmt = connection.prepareStatement(qry);
            rs = stmt.executeQuery();
            int count = 0;
            while (rs.next()) {
                count++;
            }
            if (count == 0) {
                InsertInToAgencyTable("Brak", "", "",
                        "", "", "", "");
                InsertInToAgencyTable("Cars Rzeszów S.A.", "Rzeszów", "Paderewskiego",
                        "20", "35-302", "rzeszowpad@gmail.com", "939456321");
                InsertInToAgencyTable("Cars Kraków S.A.", "Kraków", "Gutewicza",
                        "12", "85-498", "krakowgut@gmail.com", "805643342");
                InsertInToAgencyTable("Cars Warszawa S.A.", "Warszawa", "Powstańców",
                        "45a", "45-764", "wawagut@gmail.com", "805643999");
                InsertInToAgencyTable("Cars Gdynia S.A.", "Gdynia", "św. Wojciecha",
                        "293", "80-058", "gdanwojciech@gmail.com", "584255325");
                InsertInToAgencyTable("Cars Katowice S.A.", "Katowice", "Monte Cassino",
                        "53", "40-219", "katowcassino@gmail.com", "680433232");
                InsertInToAgencyTable("Cars Wrocław S.A.", "Wrocław", "Krakowska",
                        "15", "50-424", "wroclkrakowska@gmail.com", "342452654");
                InsertInToAgencyTable("Cars Lublin S.A.", "Lublin", "Solidarności",
                        "34", "20-811", "lublinsolidar@gmail.com", "435824535");

            }
        } catch (SQLException var32) {
            var32.printStackTrace();
        }
    }

    /**
     * Funkcja sprawdza czy tabela jest pusta, jesli tak to wprowadza dane przy pomocy funkcji InsertInTo...
     */
    public static void nullCheckCars() {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        String qry = "SELECT * From cars ";

        try {
            Connection connection = connect();
            stmt = connection.prepareStatement(qry);
            rs = stmt.executeQuery();
            int count = 0;
            while (rs.next()) {
                count++;
            }
            if (count == 0) {
                InsertInToCarsTable("Volkswagen Passat B6", 2011, "1.6 tdi", 110,
                        "Diesel", "Manualna", "Srebrny", 50, "RZ241242",
                        "Dostępny", 2);
                InsertInToCarsTable("Audi A6 C7", 2018, "2.0 tdi", 190,
                        "Diesel", "Automatyczna", "Czarny", 50, "RZ241243",
                        "Dostępny", 2);
                InsertInToCarsTable("Audi r8", 2019, "4.0", 620,
                        "Benzyna", "Automatyczna", "Żółty", 350, "RZ241245",
                        "Dostępny", 3);
                InsertInToCarsTable("Tesla Model S", 2013, "Elektryczny", 320,
                        "Elektryczny", "Automatyczna", "Biały", 200, "RZ241246",
                        "Dostępny", 3);
                InsertInToCarsTable("Volkswagen CC ", 2009, "2.0 tdi", 230,
                        "Diesel", "Automatyczna", "Srebny", 80, "RZ241247",
                        "Dostępny", 4);
                InsertInToCarsTable("Skoda Rapid NH3", 2012, "1.2 tsi", 103,
                        "Benzyna", "Manualna", "Brązowy", 58, "RZ241248",
                        "Dostępny", 4);
                InsertInToCarsTable("Volkswagen Golf GTI", 2018, "2.0 tdi", 260,
                        "Diesel", "Automatyczna", "Niebieski", 120, "RZ241249",
                        "Dostępny", 5);
                InsertInToCarsTable("Audi A3 8Y", 2020, "2.0 tdi evo", 300,
                        "Diesel", "Automatyczna", "Czarny", 150, "RZ241250",
                        "Dostępny", 5);
                InsertInToCarsTable("Range Rover Sport", 2019, "4.4 SDV8", 339,
                        "Benzyna", "Automatyczna", "Biały", 280, "RZ241251",
                        "Dostępny", 6);
                InsertInToCarsTable("Audi Q5 S-Line", 2019, "3.0 tdi", 355,
                        "Diesel", "Automatyczna", "Czarny", 260, "RZ241252",
                        "Dostępny", 6);
                InsertInToCarsTable("Mercedes-Benz C 200", 2020, "3.0 M256", 380,
                        "Benzyna", "Automatyczna", "Błękitny", 300, "RZ241253",
                        "Dostępny", 7);
                InsertInToCarsTable("Mercedes-Benz G500", 2019, "4.0 cdi", 250,
                        "Diesel", "Automatyczna", "Czarny", 400, "RZ241254",
                        "Dostępny", 7);
                InsertInToCarsTable("BMW 330i M pakiet", 2018, "2.0", 230,
                        "Benzyna", "Automatyczna", "Niebieski", 290, "RZ241255",
                        "Dostępny", 7);
                InsertInToCarsTable("BMW 530d", 2018, "3.0 twin-turbocharged", 360,
                        "Benzyna", "Automatyczna", "Biały", 390, "RZ241256",
                        "Dostępny", 7);
            }
        } catch (SQLException var32) {
            var32.printStackTrace();
        }
    }

    /**
     * Funkcja sprawdza czy tabela jest pusta, jesli tak to wprowadza dane przy pomocy funkcji InsertInTo...
     */
    public static void nullCheckUsers() {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        String qry = "SELECT * From users ";

        try {
            Connection connection = connect();
            stmt = connection.prepareStatement(qry);
            rs = stmt.executeQuery();
            int count = 0;
            while (rs.next()) {
                count++;
            }
            if (count == 0) {
                InsertInToUsersTable("Emilian", "Rutycki", "admin", "0192023a7bbd73250516f069df18b500", 2,
                        "emilianrut@gmail.com", "934256123", "", 2, true);
                InsertInToUsersTable("Damian", "Liszcz", "Pracownik", "491910ff69cf9f888d5bed54630ffbaa", 1,
                        "pracownik@gmail.com", "934256123", "", 2, true);
                InsertInToUsersTable("Dorota", "Kopernicka", "klient123", "2b5b815871b348fbcd605a4fd46dfbe4", 0,
                        "klient@gmail.com", "934256123", "", 2, true);
                InsertInToUsersTable("Dmytro", "Polianychko", "DmytroP", "62a0b585db4f41dd571118c0336add3e", 1,
                        "dmypol@gmail.com", "148949567", "", 2, true);
                InsertInToUsersTable("Ewa", "Kogut", "EwaKogut", "648191c2249bc98dd53c1a03e8074346", 1,
                        "ewakogut@gmail.com", "714567234", "", 2, true);
                InsertInToUsersTable("Emma", "Rutycka", "EmmaR", "b964e183e33538b187f4d2639adef60b", 1,
                        "emma234@gmail.com", "714567234", "", 2, true);
                InsertInToUsersTable("Robert", "Bomba", "RobertB", "a82762f30c1a27faa4f256b24fcaff24", 0,
                        "robertb@gmail.com", "945678123", "", 3, true);
                InsertInToUsersTable("Katarzyna", "Matskewycz", "KatarzynaM", "5f6c8ccfcbbbba19206026b97f1b8e0c", 0,
                        "katia@gmail.com", "789456432", "", 4, true);
            }
        } catch (SQLException var32) {
            var32.printStackTrace();
        }
    }

    /**
     * Funkcja sprawdza czy tabela jest pusta, jesli tak to wprowadza dane przy pomocy funkcji InsertInTo...
     */
    public static void nullCheckLoans() {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        String qry = "SELECT * From loans ";

        try {
            Connection connection = connect();
            stmt = connection.prepareStatement(qry);
            rs = stmt.executeQuery();
            int count = 0;
            while (rs.next()) {
                count++;
            }
            if (count == 0) {
                InsertInToLoansTable(6, 2, 3, 3, "Zwrócony",
                        "2021-03-17", "2021-04-01", "2021-03-24", 2,
                        3, 500);
                InsertInToLoansTable(7,2,3,1,"Zwrócony",
                        "2021-05-09", "2021-06-01", "2021-05-12", 2,
                        3, 200);
            }
        } catch (SQLException var32) {
            var32.printStackTrace();
        }
    }

    /**
     * Funkcja sprawdza czy tabela jest pusta, jesli tak to wprowadza dane przy pomocy funkcji InsertInTo...
     */
    public static void nullCheckDamage() {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        String qry = "SELECT * From damage ";

        try {
            Connection connection = connect();
            stmt = connection.prepareStatement(qry);
            rs = stmt.executeQuery();
            int count = 0;
            while (rs.next()) {
                count++;
            }
            if (count == 0) {
                InsertInToDamageTable(1, 3, "Uszkodzony wachacz", 0);
                InsertInToDamageTable(2, 1, "Uszkodzona tylna atrapa", 120);
            }
        } catch (SQLException var32) {
            var32.printStackTrace();
        }
    }

    /**
     * Funkcja sprawdza czy tabela jest pusta, jesli tak to wprowadza dane przy pomocy funkcji InsertInTo...
     */
    public static void nullCheckPhotos() {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        String qry = "SELECT * From photos ";
        try {
            Connection connection = connect();
            stmt = connection.prepareStatement(qry);
            rs = stmt.executeQuery();
            int count = 0;
            while (rs.next()) {
                count++;
            }
            String pathToDir = System.getProperty("user.dir");
            if (count == 0) {
                /*passat*/     /*1*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/passat1.jpg"));
                /*2*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/passat2.jpg"));
                /*3*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/passat3.jpg"));
                /*4*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/passat4.jpg"));
                /*audi a6*/    /*5*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/a61.jpg"));
                /*6*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/a62.jpg"));
                /*7*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/a63.jpg"));
                /*8*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/a64.jpg"));
                /*9*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/a65.jpg"));
                /*audi R8*/   /*10*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/audi-r8.jpg"));
                /*11*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/audi-r8-1.jpg"));
                /*12*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/audi-r8-2.jpg"));
                /*13*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/audi-r8-3.jpg"));
                /*14*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/audi-r8-4.jpg"));
                /*tesla*/     /*15*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/tesla1.jpg"));
                /*16*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/tesla2.jpg"));
                /*17*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/tesla3.jpg"));
                /*18*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/tesla4.jpg"));
                /*vw cc*/  /*19*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/cc1.jpg"));
                /*20*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/cc2.jpg"));
                /*21*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/cc3.jpg"));
                /*22*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/cc4.jpg"));
                /*23*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/cc5.jpg"));
                /*24*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/rapid1.jpg"));
                /*25*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/rapid2.jpg"));
                /*26*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/rapid3.jpg"));
                /*27*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/rapid4.jpg"));
                /*28*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/rapid5.jpg"));
                /*29*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/rapid6.jpg"));
                /*30*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/golf1.jpg"));
                /*31*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/golf2.jpg"));
                /*32*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/golf3.jpg"));
                /*33*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/golf4.jpg"));
                /*34*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/golf5.jpg"));
                /*35*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/golf6.jpg"));
                /*36*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/a31.jpg"));
                /*37*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/a32.jpg"));
                /*38*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/a33.jpg"));
                /*39*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/a34.jpg"));
                /*40*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/a35.jpg"));
                /*41*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/rr-sport1.jpg"));
                /*42*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/rr-sport2.jpg"));
                /*43*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/rr-sport3.jpg"));
                /*44*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/rr-sport4.jpg"));
                /*45*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/rr-sport5.jpg"));
                /*46*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/q51.jpg"));
                /*47*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/q52.jpg"));
                /*48*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/q53.jpg"));
                /*49*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/q54.jpg"));
                /*50*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/c2001.jpg"));
                /*51*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/c2002.jpg"));
                /*52*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/c2003.jpg"));
                /*53*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/c2004.jpg"));
                /*54*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/c2005.jpg"));
                /*55*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/g-class1.jpg"));
                /*56*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/g-class2.jpg"));
                /*57*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/g-class3.jpg"));
                /*58*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/g-class4.jpg"));
                /*59*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/g-class5.jpg"));
                /*60*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/g-class6.jpg"));
                /*61*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/bmw330i1.jpg"));
                /*62*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/bmw330i2.jpg"));
                /*63*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/bmw330i3.jpg"));
                /*64*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/bmw330i4.jpg"));
                /*65*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/bmw330i5.jpg"));
                /*66*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/bmw530d1.jpg"));
                /*67*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/bmw530d2.jpg"));
                /*68*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/bmw530d3.jpg"));
                /*69*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/bmw530d4.jpg"));
                /*70*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/bmw530d5.jpg"));
                /*71*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/bmw530d6.jpg"));
                /*72*/
                InsertInToPhotosTable(new FileInputStream(pathToDir + "/images/auta/total.jpg"));
            }
        } catch (SQLException | FileNotFoundException var32) {
            var32.printStackTrace();
        }
    }

    /**
     * Funkcja sprawdza czy tabela jest pusta, jesli tak to wprowadza dane przy pomocy funkcji InsertInTo...
     */
    public static void nullCheckPhoto_car() {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        String qry = "SELECT * From photo_car ";

        try {
            Connection connection = connect();
            stmt = connection.prepareStatement(qry);
            rs = stmt.executeQuery();
            int count = 0;
            while (rs.next()) {
                count++;
            }
            if (count == 0) {
                InsertInToPhoto_carTable(1, 1);
                InsertInToPhoto_carTable(2, 1);
                InsertInToPhoto_carTable(3, 1);
                InsertInToPhoto_carTable(4, 1);
                InsertInToPhoto_carTable(5, 2);
                InsertInToPhoto_carTable(6, 2);
                InsertInToPhoto_carTable(7, 2);
                InsertInToPhoto_carTable(8, 2);
                InsertInToPhoto_carTable(9, 2);
                InsertInToPhoto_carTable(10, 3);
                InsertInToPhoto_carTable(11, 3);
                InsertInToPhoto_carTable(12, 3);
                InsertInToPhoto_carTable(13, 3);
                InsertInToPhoto_carTable(14, 3);
                InsertInToPhoto_carTable(15, 4);
                InsertInToPhoto_carTable(16, 4);
                InsertInToPhoto_carTable(17, 4);
                InsertInToPhoto_carTable(18, 4);
                InsertInToPhoto_carTable(19, 5);
                InsertInToPhoto_carTable(20, 5);
                InsertInToPhoto_carTable(21, 5);
                InsertInToPhoto_carTable(22, 5);
                InsertInToPhoto_carTable(23, 5);
                InsertInToPhoto_carTable(24, 6);
                InsertInToPhoto_carTable(25, 6);
                InsertInToPhoto_carTable(26, 6);
                InsertInToPhoto_carTable(27, 6);
                InsertInToPhoto_carTable(28, 6);
                InsertInToPhoto_carTable(29, 6);
                InsertInToPhoto_carTable(30, 7);
                InsertInToPhoto_carTable(31, 7);
                InsertInToPhoto_carTable(32, 7);
                InsertInToPhoto_carTable(33, 7);
                InsertInToPhoto_carTable(34, 7);
                InsertInToPhoto_carTable(35, 7);
                InsertInToPhoto_carTable(36, 8);
                InsertInToPhoto_carTable(37, 8);
                InsertInToPhoto_carTable(38, 8);
                InsertInToPhoto_carTable(39, 8);
                InsertInToPhoto_carTable(40, 8);
                InsertInToPhoto_carTable(41, 9);
                InsertInToPhoto_carTable(42, 9);
                InsertInToPhoto_carTable(43, 9);
                InsertInToPhoto_carTable(44, 9);
                InsertInToPhoto_carTable(45, 9);
                InsertInToPhoto_carTable(46, 10);
                InsertInToPhoto_carTable(47, 10);
                InsertInToPhoto_carTable(48, 10);
                InsertInToPhoto_carTable(49, 10);
                InsertInToPhoto_carTable(50, 11);
                InsertInToPhoto_carTable(51, 11);
                InsertInToPhoto_carTable(52, 11);
                InsertInToPhoto_carTable(53, 11);
                InsertInToPhoto_carTable(54, 11);
                InsertInToPhoto_carTable(55, 12);
                InsertInToPhoto_carTable(56, 12);
                InsertInToPhoto_carTable(57, 12);
                InsertInToPhoto_carTable(58, 12);
                InsertInToPhoto_carTable(59, 12);
                InsertInToPhoto_carTable(60, 12);
                InsertInToPhoto_carTable(61, 13);
                InsertInToPhoto_carTable(62, 13);
                InsertInToPhoto_carTable(63, 13);
                InsertInToPhoto_carTable(64, 13);
                InsertInToPhoto_carTable(65, 13);
                InsertInToPhoto_carTable(66, 14);
                InsertInToPhoto_carTable(67, 14);
                InsertInToPhoto_carTable(68, 14);
                InsertInToPhoto_carTable(69, 14);
                InsertInToPhoto_carTable(70, 14);
                InsertInToPhoto_carTable(71, 14);
            }
        } catch (SQLException var32) {
            var32.printStackTrace();
        }
    }

    /**
     * Funkcja sprawdza czy tabela jest pusta, jesli tak to wprowadza dane przy pomocy funkcji InsertInTo...
     */
    public static void nullCheckPhoto_damage() {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        String qry = "SELECT * From photo_damage ";

        try {
            Connection connection = connect();
            stmt = connection.prepareStatement(qry);
            rs = stmt.executeQuery();
            int count = 0;
            while (rs.next()) {
                count++;
            }
            if (count == 0) {
                InsertInToPhotoDamageTable(72, 2);
            }
        } catch (SQLException var32) {
            var32.printStackTrace();
        }
    }
}