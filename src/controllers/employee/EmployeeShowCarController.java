package controllers.employee;

import controllers.admin.AdminController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Cars;
import service.DataBase;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static controllers.employee.EmployeeController.u_lastName;
import static controllers.employee.EmployeeController.u_name;

public class EmployeeShowCarController implements Initializable {

    public Label marka;
    public Label data;
    public Label silnik;
    public Label moc;
    public Label paliwo;
    public Label skrzynia;
    public Label kolor;
    public Label koszt;
    public Label status;
    public Label nr_reje;
    public Label filia;
    public ChoiceBox status1;
    private Component frame;
    public ImageView imageView;
    public int id_photo;
    public Label name;
    public Label lastName;
    public static String name2;
    String s;
    int count = 0;
    public static int id_car_damage;

    /**
     * Metoda pobiera zdjęcia wybranego auta oraz je wyświetla.
     */
    public void slideshow() {
        ArrayList<Image> images = new ArrayList<Image>();
        EmployeeController controler = new EmployeeController();

        Connection connection = DataBase.connect();
        int count_image = 1;
        try {
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT DISTINCT photo from photos " +
                    "INNER JOIN photo_car pc on photos.id_photo = pc.id_photo " +
                    "where pc.id_car = " + EmployeeController.id);

            while (resultSet.next()) {
                InputStream is = resultSet.getBinaryStream("photo");
                InputStreamReader inputReader = new InputStreamReader(is);
                if (inputReader.ready()) {
                    File tempFile = new File("images/image" + count_image + ".jpg");
                    FileOutputStream fos = new FileOutputStream(tempFile);
                    byte[] buffer = new byte[1024];
                    while (is.read(buffer) > 0) {
                        fos.write(buffer);
                    }
                    images.add(new Image(tempFile.toURI().toURL().toString()));
                }
                count_image++;
            }
            int ilosc = count_image - 1;
            if(ilosc>0) {
                imageView.setImage(images.get(0));
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
                    imageView.setImage(images.get(count));
                    count++;
                    if (count == ilosc) {
                        count = 0;
                    }
                }));
                timeline.setCycleCount(Timeline.INDEFINITE);
                timeline.play();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param location
     * @param resources
     * Wyświetlanie informacji o wybranym aucie.
     */
    public void initialize(URL location, ResourceBundle resources) {
        slideshow();
        EmployeeController controler = new EmployeeController();

        Platform.runLater(() -> {
            name.setText(u_name);
            lastName.setText(u_lastName);
        });


        try {
            Connection connection = DataBase.connect();

            ResultSet rs = connection.createStatement().executeQuery("SELECT c.*, a.agency_name FROM cars c " +
                    "LEFT JOIN agency a ON c.id_agency = a.id_agency where c.id_car = " + EmployeeController.id);
            while (rs.next()) {
                id_car_damage = rs.getInt("id_car");
                Cars car = new Cars(rs.getInt("id_car"), rs.getString("model"), rs.getInt("production_date"),
                        rs.getString("engine"),
                        rs.getInt("power"), rs.getString("fuel"), rs.getString("transmission"),
                        rs.getString("color"), rs.getInt("cost"), rs.getString("registration_number"),
                        rs.getString("status"), rs.getInt("id_agency"));
                name2 = rs.getString("agency_name");

                marka.setText(car.getModel());
                data.setText(String.valueOf(car.getProduction_date()));
                silnik.setText(car.getEngine());
                moc.setText(String.valueOf(car.getPower()));
                paliwo.setText(car.getFuel());
                skrzynia.setText(car.getTransmission());
                kolor.setText(car.getColor());
                koszt.setText(String.valueOf(car.getCost()));
                status.setText(car.getStatus());
                nr_reje.setText(car.getRegistration_number());
                filia.setText(name2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void EmployeeZmienStatusButton(ActionEvent event) {
        EmployeeController controller = new EmployeeController();


        try {
            Connection connection = DataBase.connect();
            String query = "update cars set status = ? where id_car = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            String status2 = String.valueOf(status1.getSelectionModel().getSelectedItem());

            preparedStatement.setString(1, String.valueOf(status2));
            preparedStatement.setInt(2, EmployeeController.id);
            preparedStatement.execute();


            JOptionPane.showMessageDialog(frame, "Zmieniono status");


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/employee/EmployeeShowCar.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene menu = new Scene(zmianaSceny);
        Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
        okno.setScene(menu);
        okno.centerOnScreen();
        okno.show();
    }

    @FXML
    void dodajzdjecie(ActionEvent e){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.IMAGE", "jpg","gif","png");
        fileChooser.addChoosableFileFilter(filter);
        int result = fileChooser.showSaveDialog(null);
        if(result == JFileChooser.APPROVE_OPTION){
            File selectedFile = fileChooser.getSelectedFile();
            String path = selectedFile.getAbsolutePath();
            s = path;
        }
        else if(result == JFileChooser.CANCEL_OPTION){
            System.out.println("No Data");
        }
    }

    @FXML
    void zapiszzdjecie(ActionEvent event){
        AdminController controller = new AdminController();


        try {
            Connection connection = DataBase.connect();
            String query = "insert into photos values (null, ?)";
            InputStream is = new FileInputStream(new File(s));
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setBlob(1, is);
            preparedStatement.execute();

            ResultSet rs = connection.createStatement().executeQuery("SELECT MAX(id_photo) from photos");
            while (rs.next()) {
                id_photo = rs.getInt("max(id_photo)");
            }
            String queary2 = "insert into photo_car values(?,?)";
            PreparedStatement preparedStatement1 = connection.prepareStatement(queary2);

            preparedStatement1.setInt(1,id_photo);
            preparedStatement1.setInt(2, EmployeeController.id);
            preparedStatement1.execute();


            JOptionPane.showMessageDialog(frame, "Dodano zdjecie");
        } catch (SQLException | FileNotFoundException throwables) {
            throwables.printStackTrace();
        }

        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/employee/EmployeeShowCar.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene menu = new Scene(zmianaSceny);
        Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
        okno.setScene(menu);
        okno.centerOnScreen();
        okno.show();
    }



    @FXML
    void EmployeeKatalogPojazdowButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/employee/Employee.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene menu = new Scene(zmianaSceny);
        Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
        okno.setScene(menu);
        okno.centerOnScreen();
        okno.show();
    }

    @FXML
    void EmployeeCofnijButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/employee/Employee.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene menu = new Scene(zmianaSceny);
        Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
        okno.setScene(menu);
        okno.centerOnScreen();
        okno.show();
    }

    @FXML
    void EmployeeRaportyButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/employee/EmployeeRaports.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene menu = new Scene(zmianaSceny);
        Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
        okno.setScene(menu);
        okno.centerOnScreen();
        okno.show();


    }

    @FXML
    void EmployeeWypozyczeniaKlientowButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/employee/EmployeeLoans.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene menu = new Scene(zmianaSceny);
        Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
        okno.setScene(menu);
        okno.centerOnScreen();
        okno.show();
    }

    public void EmployeeUzytkownicyButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/employee/EmployeeUsers.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene menu = new Scene(zmianaSceny);
        Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
        okno.setScene(menu);
        okno.centerOnScreen();
        okno.show();
    }


    @FXML
    void EmployeeLogoutButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/login/Login.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene menu = new Scene(zmianaSceny);
        Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
        okno.setScene(menu);
        okno.centerOnScreen();
        okno.show();
    }

    @FXML
    void EmployeeEdytujPojazdButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/employee/EmployeeEditCar.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene menu = new Scene(zmianaSceny);
        Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
        okno.setScene(menu);
        okno.centerOnScreen();
        okno.show();
    }

    @FXML
    void  EmployeeUstawieniaButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/employee/EmployeeSettings.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene menu = new Scene(zmianaSceny);
        Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
        okno.setScene(menu);
        okno.centerOnScreen();
        okno.show();
    }

    @FXML
    void EmployeeDamagesButton(ActionEvent event){
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/employee/EmployeeDamages.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene menu = new Scene(zmianaSceny);
        Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
        okno.setScene(menu);
        okno.centerOnScreen();
        okno.show();
    }
}
