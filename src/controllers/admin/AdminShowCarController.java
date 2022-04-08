package controllers.admin;

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

import javax.imageio.ImageIO;
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



public class AdminShowCarController implements Initializable {

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
    public ImageView imageView;
    private Component frame;
    public int id_photo;
    public static int id_car_damage;
    String s;
    public static String name;
    int count = 0;
    int licz = 0;

    public void slideshow() {
        ArrayList<Image> images = new ArrayList<Image>();
        AdminController controller = new AdminController();
        Connection connection = DataBase.connect();
        int count_image = 1;

        try {
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT DISTINCT photo from photos " +
                    "INNER JOIN photo_car pc on photos.id_photo = pc.id_photo " +
                    "where pc.id_car = " + AdminController.id);

            while (resultSet.next()) {
                InputStream is = resultSet.getBinaryStream("photo");
                InputStreamReader inputReader = new InputStreamReader(is);
                if(inputReader.ready()) {
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
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
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
     * Wyświetalnie wszystkich informacji o aucie.
     */
    public void initialize(URL location, ResourceBundle resources) {
        slideshow();
        AdminController controler = new AdminController();
        try{
            Connection connection = DataBase.connect();


            ResultSet rs = connection.createStatement().executeQuery("SELECT c.*, a.agency_name FROM cars c " +
                    "LEFT JOIN agency a ON c.id_agency = a.id_agency where c.id_car = " + AdminController.id);
            while(rs.next()) {
                id_car_damage = rs.getInt("id_car");
                Cars car = new Cars(rs.getInt("id_car"), rs.getString("model"),rs.getInt("production_date"),
                        rs.getString("engine"),
                        rs.getInt("power"), rs.getString("fuel"), rs.getString("transmission"),
                        rs.getString("color"), rs.getInt("cost"), rs.getString("registration_number"),
                        rs.getString("status"), rs.getInt("id_agency"));
                name = rs.getString("agency_name");

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
                filia.setText(name);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @param event Przycisk zmieniający status pojazdu pod warunkiem, że auto nie jest zarezerwowane/wypożyczone.
     */
    @FXML
    void AdminZmienStatusButton(ActionEvent event) {
        AdminController controller = new AdminController();


        try {
            Connection connection = DataBase.connect();
            String query = "update cars set status = ? where id_car = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            String status2 = String.valueOf(status1.getSelectionModel().getSelectedItem());

            preparedStatement.setString(1, String.valueOf(status2));
            preparedStatement.setInt(2, AdminController.id);
            preparedStatement.execute();


            JOptionPane.showMessageDialog(frame, "Zmieniono status");


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/admin/AdminShowCar.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene menu = new Scene(zmianaSceny);
        Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
        okno.setScene(menu);
        okno.centerOnScreen();
        okno.show();
    }

    /**
     * @param e Przycisk dodający zdjęcie auta.
     */
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

    /**
     * @param event Przycisk zapisujący wybrane zdjęcie z metody dodajzdjecie do bazy danych dla wybranego auta.
     */
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
            preparedStatement1.setInt(2, AdminController.id);
            preparedStatement1.execute();


            JOptionPane.showMessageDialog(frame, "Dodano zdjecie");
        } catch (SQLException | FileNotFoundException throwables) {
            throwables.printStackTrace();
        }
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/admin/AdminShowCar.fxml"));
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
    void AdminDamagesButton(ActionEvent event){
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/admin/AdminDamages.fxml"));
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
    void AdminKatalogPojazdowButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/admin/Admin.fxml"));
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
    void AdminCofnijButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/admin/Admin.fxml"));
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
    void AdminRaportyButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/admin/Raports.fxml"));
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
    void AdminUstawieniaButton(ActionEvent event) {

    }

    @FXML
    void AdminUzytkownicyButton(ActionEvent event) {

    }
    @FXML
    void AdminWypozyczeniaKlientowButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/admin/AdminLoans.fxml"));
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
    void AdminLogoutButton(ActionEvent event) {
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
    void AdminEdytujPojazdButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/admin/EditCar.fxml"));
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
