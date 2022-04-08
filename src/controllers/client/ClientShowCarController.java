package controllers.client;

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
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Cars;
import service.DataBase;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Date;

import static controllers.employee.EmployeeController.u_lastName;
import static controllers.employee.EmployeeController.u_name;

public class ClientShowCarController implements Initializable {

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
    public DatePicker data_od;
    public DatePicker data_do;
    public Label cena;
    public int daysBetween = 0;
    public static int cost;
    private Component frame;
    public Label name;
    public Label lastName;
    public int id_photo;
    String s;
    int count = 0;


    /**
     * @param actionEvent Metoda pozwala zarezerwować auto przez klienta, jeśli:
     *                    1.Jego dokument tożsamości jest potwierdzony przez pracownika
     *                    2.Auto jest dostępne
     *                    3.Wybrał daty od-do
     */
    @FXML
    public void ZarezerwujAuto(ActionEvent actionEvent) {
        ClientController controler = new ClientController();
        System.out.println(ClientController.status);
        String s1 = "Dostępny";
        String s3 = "1";
        if (ClientController.string_active.equals(s3)) {
            if (ClientController.status.equals(s1)) {
                if (data_od.getValue() != null && data_do.getValue() != null) {

                    String date_of_loan = data_od.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    String date_of_expiry = data_do.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                    try {
                        Date date_od = dateFormat.parse(date_of_loan);
                        Date date_do = dateFormat.parse(date_of_expiry);
                        long difference = date_do.getTime() - date_od.getTime();
                        daysBetween = (int) (difference / (1000 * 60 * 60 * 24));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (daysBetween >= 0) {
                        daysBetween++;
                        cost = ClientController.cost * daysBetween;

                        int id_car = ClientController.id;
                        int id_user = ClientController.userId;
                        int id_agency = ClientController.id_agency;
                        String status2 = "Rezerwacja";

                        try {
                            Connection connection = DataBase.connect();
                            String query = "insert into loans values (null, ?, null, null, ?, ?, ?, ?, null, ?, null, ?)";
                            PreparedStatement preparedStatement = connection.prepareStatement(query);
                            preparedStatement.setInt(1, id_user);
                            preparedStatement.setInt(2, id_car);
                            preparedStatement.setString(3, status2);
                            preparedStatement.setDate(4, java.sql.Date.valueOf(date_of_loan));
                            preparedStatement.setDate(5, java.sql.Date.valueOf(date_of_expiry));
                            preparedStatement.setInt(6, id_agency);
                            preparedStatement.setInt(7, cost);
                            preparedStatement.execute();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }

                        try {
                            Connection connection = DataBase.connect();
                            String query = "update cars set status = ? where id_car = ?";
                            String status3 = "Zarezerwowany";
                            PreparedStatement preparedStatement = connection.prepareStatement(query);
                            preparedStatement.setString(1, status3);
                            preparedStatement.setInt(2, ClientController.id);
                            preparedStatement.execute();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        JOptionPane.showMessageDialog(frame, "Zarezerwowano pojazd");

                        Parent zmianaSceny = null;
                        try {
                            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/client/ClientLoans.fxml"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Scene menu = new Scene(zmianaSceny);
                        Stage okno = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                        okno.setScene(menu);
                        okno.centerOnScreen();
                        okno.show();
                    }
                    else{
                        JOptionPane.showMessageDialog(frame, "Twoja data oddania auta jest wczesniejsza niz data wypozyczenia!");
                    }
                }

                    else {
                        JOptionPane.showMessageDialog(frame, "Nie wybrano dat od i do!");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Auto jest aktualnie niedostępne");
                }
            }
        }



    /**
     * Funkcja wyświeta zdjęcia wybranego auta.
     */
    public void slideshow() {
        ArrayList<javafx.scene.image.Image> images = new ArrayList<javafx.scene.image.Image>();
        ClientController controler = new ClientController();
        String s3 = "1";
        if (!ClientController.string_active.equals(s3)) {
            JOptionPane.showMessageDialog(frame, "Twoje konto jest niekatywne, nie możesz rezerwować aut");
            data_od.setDisable(true);
            data_do.setDisable(true);
        }

        LocalDate minDate =  LocalDate.now().plusDays(1);
        LocalDate maxDate = LocalDate.now().plusDays(3);
        data_od.setDayCellFactory(d ->
                new DateCell() {
                    @Override public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setDisable(item.isAfter(maxDate) || item.isBefore(minDate));
                    }});
        data_do.setDayCellFactory(d ->
                new DateCell() {
                    @Override public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setDisable(item.isBefore(minDate.plusDays(1)));
                    }});


        Connection connection = DataBase.connect();
        int count_image = 1;
        try {
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT DISTINCT photo from photos " +
                    "INNER JOIN photo_car pc on photos.id_photo = pc.id_photo " +
                    "where pc.id_car = " + ClientController.id);

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
     * @param resources Wyświetla wszystkie informacje o aucie.
     */
    public void initialize(URL location, ResourceBundle resources) {



        System.out.println(ClientController.userId);
        slideshow();
        ClientController controler = new ClientController();
        Platform.runLater(() -> {
            name.setText(ClientController.String_name);
            lastName.setText(ClientController.String_lastName);
        });
        if (data_od.getValue() != null && data_do.getValue() != null) {
            //showcost();
        }


        try {
            Connection connection = DataBase.connect();


            ResultSet rs = connection.createStatement().executeQuery("SELECT c.*, a.agency_name FROM cars c " +
                    "LEFT JOIN agency a ON c.id_agency = a.id_agency where c.id_car = " + ClientController.id);
            while (rs.next()) {

                Cars car = new Cars(rs.getInt("id_car"), rs.getString("model"), rs.getInt("production_date"),
                        rs.getString("engine"),
                        rs.getInt("power"), rs.getString("fuel"), rs.getString("transmission"),
                        rs.getString("color"), rs.getInt("cost"), rs.getString("registration_number"),
                        rs.getString("status"), rs.getInt("id_agency"));
                String name = rs.getString("agency_name");

                System.out.println(car.getId());
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

    @FXML
    void ClientCofnijButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/client/Client.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene menu = new Scene(zmianaSceny);
        Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
        okno.setScene(menu);
        okno.centerOnScreen();
        okno.show();
    }

    public void ClientKatalogPojazdowButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/client/Client.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene menu = new Scene(zmianaSceny);
        Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
        okno.setScene(menu);
        okno.centerOnScreen();
        okno.show();
    }

    public void ClientWypozyczeniaKlientowButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/client/ClientLoans.fxml"));
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
    void ClientLogoutButton(ActionEvent event) {
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
    void ClientDamagesButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/client/ClientDamages.fxml"));
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
    void ClientUstawieniaButton(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/client/ClientSettings.fxml"));
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
