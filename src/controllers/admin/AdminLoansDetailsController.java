package controllers.admin;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import service.DataBase;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;


/**
 * Stworzono i aktualizowano przez Dmytro Polianychko
 * Dany kontroler służy do obsługi szczegółów wypożyczeń klientów przez administratora
 */
public class AdminLoansDetailsController extends Component implements Initializable {

    /**
     * Inicjalizacja globalnych zmiennych dla metody Initialize (wyciąganie szczegółów samochodu)
     */
    String model, engine, fuel, registration_number, color, user_name, surname, phone, email, uszkodzenia, status;
    int production_date, cost, suma_uszkodzen;
    Date date_loan, date_delivery;


    /**
     * Inicjalizacja globalnych zmiennych dla generowania protokołów zwrotu
     */
    String delivery_agency_name, delivery_agency_city, delivery_agency_road, delivery_agency_post_code,
            agency_delivery_email, delivery_agency_phone_number, delivery_agency_building_number, loan_date_of_delivery;
    int summary_cost;

    /**
     * Inicjalizacja globalnych zmiennych dla generowania protokołu oddania
     */
    String loan_agency_name, loan_agency_city, loan_agency_road, loan_agency_post_code, loan_agency_email,
            loan_agency_phone_number, loan_agency_building_number, loan_date_of_loan;

    /**
     * Inicjalizacja globalnych zmiennych dla slideshow zdjęć samochodu
     */
    int count;
    int ilosc;
    private Image image;
    public static int id_detail_loan;
    public static int car_id;
    public static int id_samochodu;
    java.sql.Date data_wynajmu;
    public int daysBetween = 0;
    Component frame;


    /**
     * Inicjalizacja komponentów z pliku AdminLoansDetails.fxml
     */
    @FXML
    private Label protokolyUszkodzenia;

    @FXML
    private Label protokolyMarkaModel;

    @FXML
    private Label protokolyRokWyprodukowania;

    @FXML
    private Label protokolySilnik;

    @FXML
    private Label protokolyTypPaliwa;

    @FXML
    private Label protokolyNumerRejestracyjny;

    @FXML
    private Label protokolyKolor;

    @FXML
    private Label protokolyCenaZaDobe;

    @FXML
    private Label protokolyImieKlienta;

    @FXML
    private Label protokolyNazwiskoKlienta;

    @FXML
    private Label protokolyTelefonKlienta;

    @FXML
    private Label protokolyEmailKlienta;

    @FXML
    private ChoiceBox changeStatusBox;

    @FXML
    private ImageView protokolyZdjecieCar;

    @FXML
    private Label protokolyDataWypozyczenia;

    @FXML
    private Label protokolyDataZwrotu;


    @FXML
    ChoiceBox<String> TypProtokolu;
    private final String[] protokoly ={"Wydania", "Zwrotu"};

    /**
     * Dana metoda sluży do przejścia do uszkodzeń samochodów jeśli one są, w przeciwnym przypadku
     * zostaje wyświetlony alert
     * @param event wykorzysuje się dla obsługi zdarzeń
     */
    @FXML
    void protokolyUszkodzeniaAuta(ActionEvent event) {
        AdminLoansController controller = new AdminLoansController();
        Connection connection = DataBase.connect();
        int count_image = 1;
        try {
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT DISTINCT l.id_loan, d.id_damage, p.photo " +
                    "FROM loans l " +
                    "INNER JOIN damage d on l.id_loan = d.id_loan " +
                    "INNER JOIN photo_damage pd on d.id_damage = pd.id_damage " +
                    "INNER JOIN photos p on pd.id_photo = p.id_photo " +
                    "WHERE l.id_car = " + AdminLoansController.car_id);
            if (resultSet.next()) {
                Parent zmianaSceny = null;
                try {
                    zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/admin/AdminLoansDetailsDamagesDetails.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Scene menu = new Scene(zmianaSceny);
                Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
                okno.setScene(menu);
                okno.centerOnScreen();
                okno.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informacja");
                alert.setHeaderText(null);
                alert.setContentText("Dane auto nie ma zdjęć uszkodzeń");
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(
                        getClass().getResource("/CSS/login.css").toExternalForm());
                dialogPane.getStyleClass().add("alert");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Dana metoda służy do generowania pustych linii
     * która jest wykorystywana przy generowaniu protokołów oddania/zwrotu
     * @param paragraph pobieramy komponent biblioteki itextpdf który tworzy nam nową linie
     * @param number ilość linii
     */
    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    /**
     * Dana metoda służy do obsługi wyboru dokumentu który potrzebno wygenerować (oddanie albo zwrot)
     * Przy aktywnym comboboxie "Oddanie" metoda zwraca się do metody generowania protokołu oddania samochodu klientowi
     * Przy aktywnym comboboxie "Zwrot" metoda zwraca się do metody generowania protokołu zwrotu auta klientem
     * W przeciwnym razie jest pokazywany alertbox
     * @param event wykorzysuje się dla obsługi zdarzeń
     * @throws SQLException służy do obsługi blędów związanych z bazą danych MariaDB
     */
    @FXML
    void wygenerujProtokol(ActionEvent event) throws SQLException {
        if (TypProtokolu.getValue().equals("Wydania")) {
            protokol_wydania();
        } else if (TypProtokolu.getValue().equals("Zwrotu")) {
            protokol_zwrotu();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informacja");
            alert.setHeaderText(null);
            alert.setContentText("Masz wybrać typ generowanego protokołu");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("/CSS/login.css").toExternalForm());
            dialogPane.getStyleClass().add("alert");
            alert.showAndWait();
        }
    }

    /**
     * Dana metoda służy do generowania protokołów oddania samochodu przez pracownika, w szczególności
     * mieści całą strukturę generowanego protokołu
     * W przypadku kiedy niema dannych z bazy to jest obsługa blędu, i jest pokazywany alertbox
     * @throws SQLException służy do obsługi blędów związanych z bazą danych MariaDB
     */
    void protokol_wydania() throws SQLException {
        AdminLoansController controller = new AdminLoansController();
        Connection con = DataBase.connect();

        ResultSet rs = con.createStatement().executeQuery("SELECT l.id_loan, a.agency_name, a.city, a.road, a.building_number, a.post_code, a.email, a.phone_number, l.date_of_loan \n" +
                "FROM loans l \n" +
                "INNER JOIN agency a on l.agency_of_loan = a.id_agency \n" +
                "where l.id_loan = " + AdminLoansController.loan_id);

        if (rs.next()) {
            loan_agency_name = rs.getString("agency_name");
            loan_agency_city = rs.getString("city");
            loan_agency_road = rs.getString("road");
            loan_agency_building_number = rs.getString("building_number");
            loan_agency_post_code = rs.getString("post_code");
            loan_agency_email = rs.getString("email");
            loan_agency_phone_number = rs.getString("phone_number");
            loan_date_of_loan = rs.getString("date_of_loan");
        }

        String path="";
        JFileChooser j=new JFileChooser();
        j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int x=j.showSaveDialog(this);

        if(x==JFileChooser.APPROVE_OPTION) {
            path=j.getSelectedFile().getPath();
            path=path+"";
        }
        try {
            BaseFont helvetica = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
            Font helvetica16 = new Font(helvetica,16, Font.BOLD);
            Font helvetica11b = new Font(helvetica, 11, Font.BOLD);
            Font helvetica11i = new Font(helvetica, 11, Font.ITALIC);
            Font helvetica13b = new Font(helvetica, 13, Font.ITALIC);

            new Paragraph("Śćźół:",helvetica16);
            Document document = new Document();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
            DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDateTime now = LocalDateTime.now();

            // nazwa dokumentu
            PdfWriter.getInstance(document, new FileOutputStream(path+"/protokol_wydania_auta_"+ dtf.format(now) +".pdf"));
            document.open();
            Paragraph preface = new Paragraph();
            Paragraph loanDetails = new Paragraph();
            Paragraph clientData = new Paragraph();
            Paragraph Signature = new Paragraph();
            addEmptyLine(preface, 1);

            // dane pracownika który generuje protokół
            preface.add(new Paragraph("Protokół wydania samochodu " + dtf2.format(now), helvetica16));
            addEmptyLine(preface, 1);
            preface.add(new Paragraph("Protokół został wygenerowany przez: ", helvetica11i));
            preface.add(new Paragraph("Administrator Emilian Rutycki", helvetica11i));
            preface.add(new Paragraph("Filia: Cars Rzeszów S.A.", helvetica11i));
            preface.add(new Paragraph( "Adres: Rzeszów, 35-302, Paderewskiego 20", helvetica11i));
            preface.add(new Paragraph("Numer telefonu filii: 939 456 321", helvetica11i));
            addEmptyLine(preface, 3);
            document.add(preface);

            // dane wypożyczenia
            loanDetails.add(new Paragraph("                                  Szczegóły wypożyczenia", helvetica16));
            loanDetails.add("_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ");
            addEmptyLine(loanDetails,1);
            loanDetails.add(new Paragraph("Model samochodu: " +model , helvetica13b));
            loanDetails.add(new Paragraph("Rok produkcji: " +production_date, helvetica13b));
            loanDetails.add(new Paragraph("Silnik: " +engine, helvetica13b));
            loanDetails.add(new Paragraph("Typ paliwa: " +fuel, helvetica13b));
            loanDetails.add(new Paragraph("Numer rejestracyjny: " +registration_number, helvetica13b));
            loanDetails.add(new Paragraph("Kolor: " +color, helvetica13b));
            loanDetails.add(new Paragraph("Uszkodzenia/uwagi: " +uszkodzenia, helvetica13b));
            addEmptyLine(loanDetails,1);
            loanDetails.add(new Paragraph("Koszt dzienny: " + cost, helvetica13b));
            document.add(loanDetails);
            addEmptyLine(clientData,2);

            // dane klienta
            clientData.add(new Paragraph("                                               Dane klienta", helvetica16));
            clientData.add("_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ");
            addEmptyLine(clientData,1);
            clientData.add(new Paragraph("Imie: " +user_name, helvetica11i));
            clientData.add(new Paragraph("Nazwisko: " +surname, helvetica11i));
            clientData.add(new Paragraph("Numer telefonu: " +phone, helvetica11i));
            clientData.add(new Paragraph("Email: " +email, helvetica11i));

            addEmptyLine(clientData, 1);

            clientData.add(new Paragraph("Data wypożyczenia: " +loan_date_of_loan, helvetica11b));
            clientData.add(new Paragraph("Filia w której zostało wypożyczone auto: " + loan_agency_name, helvetica11b));
            clientData.add(new Paragraph("Adresa filii: " + loan_agency_city + ", " + loan_agency_post_code + ", " + loan_agency_road + " " +loan_agency_building_number, helvetica11b));
            clientData.add(new Paragraph( "Numer telefonu: " + loan_agency_phone_number, helvetica11b));
            document.add(clientData);

            //sygnatura
            addEmptyLine(Signature, 2);
            Signature.add(new Paragraph("Osoba wydająca ....................        Klient ...................... ", helvetica16));
            document.add(Signature);
            document.close();

            // informacja
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informacja");
            alert.setHeaderText(null);
            alert.setContentText("Protokól wydania został wygenerowany...");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("/CSS/login.css").toExternalForm());
            dialogPane.getStyleClass().add("alert");
            alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Dana metoda służy do generowania protokołów zwrotu samochodu przez klienta do filii, w szczególności
     * mieści całą strukturę generowanego protokołu
     * W przypadku kiedy niema dannych z bazy to jest obsługa blędu, i jest pokazywany alertbox
     * @throws SQLException służy do obsługi blędów związanych z bazą danych MariaDB
     */
    void protokol_zwrotu() throws SQLException {
        AdminLoansController controller = new AdminLoansController();
        Connection con = DataBase.connect();

        ResultSet rs = con.createStatement().executeQuery("SELECT l.id_loan, a.agency_name, a.city, a.road, a.building_number, a.post_code, a.email, a.phone_number, l.cost, l.date_of_delivery \n" +
                "FROM loans l \n" +
                "INNER JOIN agency a on l.agency_of_delivery = a.id_agency \n" +
                "where l.id_loan = " + AdminLoansController.loan_id);

        if (rs.next()) {
            delivery_agency_name = rs.getString("agency_name");
            delivery_agency_city = rs.getString("city");
            delivery_agency_road = rs.getString("road");
            delivery_agency_building_number = rs.getString("building_number");
            delivery_agency_post_code = rs.getString("post_code");
            agency_delivery_email = rs.getString("email");
            delivery_agency_phone_number = rs.getString("phone_number");
            summary_cost = rs.getInt("cost");
            loan_date_of_delivery = rs.getString("date_of_delivery");
        }

        String path="";
        JFileChooser j=new JFileChooser();
        j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int x=j.showSaveDialog(this);

        if(x==JFileChooser.APPROVE_OPTION) {
            path=j.getSelectedFile().getPath();
            path=path+"";
        }

        try {
            BaseFont helvetica = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
            Font helvetica16 = new Font(helvetica,16, Font.BOLD);
            Font helvetica11b = new Font(helvetica, 11, Font.BOLD);
            Font helvetica11i = new Font(helvetica, 11, Font.ITALIC);
            Font helvetica13b = new Font(helvetica, 13, Font.ITALIC);
            Document document = new Document();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
            DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDateTime now = LocalDateTime.now();
            System.out.println(dtf.format(now));

            // nazwa dokumentu
            PdfWriter.getInstance(document, new FileOutputStream(path+"/protokol_zwrotu_auta_"+ dtf.format(now) +".pdf"));
            document.open();
            Paragraph preface = new Paragraph();
            Paragraph loanDetails = new Paragraph();
            Paragraph clientData = new Paragraph();
            Paragraph Signature = new Paragraph();
            addEmptyLine(preface, 1);

            // dane pracownika który generuje protokół
            preface.add(new Paragraph("Protokół zwrotu samochodu " + dtf2.format(now), helvetica16));
            addEmptyLine(preface, 1);
            preface.add(new Paragraph("Protokół został wygenerowany przez: ", helvetica11i));
            preface.add(new Paragraph("Administrator Emilian Rutycki", helvetica11i));
            preface.add(new Paragraph("Filia: Cars Rzeszów S.A.", helvetica11i));
            preface.add(new Paragraph( "Adres: Rzeszów, 35-302, Paderewskiego 20", helvetica11i));
            preface.add(new Paragraph("Numer telefonu filii: 939 456 321", helvetica11i));
            addEmptyLine(preface, 3);
            document.add(preface);

            // szczegóły wypożyczeń
            loanDetails.add(new Paragraph("                                  Szczegóły wypożyczenia", helvetica16));
            loanDetails.add("_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ");
            addEmptyLine(loanDetails,1);
            loanDetails.add(new Paragraph("Model samochodu: " +model, helvetica13b));
            loanDetails.add(new Paragraph("Rok produkcji: " +production_date, helvetica13b));
            loanDetails.add(new Paragraph("Silnik: " +engine, helvetica13b));
            loanDetails.add(new Paragraph("Typ paliwa: " +fuel, helvetica13b));
            loanDetails.add(new Paragraph("Numer rejestracyjny: " +registration_number, helvetica13b));
            loanDetails.add(new Paragraph("Kolor: " +color, helvetica13b));
            loanDetails.add(new Paragraph("Uszkodzenia/uwagi: " +uszkodzenia, helvetica13b));
            addEmptyLine(loanDetails,1);
            int suma = summary_cost + suma_uszkodzen;
            loanDetails.add(new Paragraph("Końcowa cena wypożyczenia: " + suma, helvetica13b));
            document.add(loanDetails);
            addEmptyLine(clientData,2);

            // dane klienta
            clientData.add(new Paragraph("                                               Dane klienta", helvetica16));
            clientData.add("_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ");
            addEmptyLine(clientData,1);
            clientData.add(new Paragraph("Imie: " +user_name, helvetica11i));
            clientData.add(new Paragraph("Nazwisko: " +surname, helvetica11i));
            clientData.add(new Paragraph("Numer telefonu: " +phone, helvetica11i));
            clientData.add(new Paragraph("Email: " +email, helvetica11i));
            addEmptyLine(clientData, 1);
            clientData.add(new Paragraph("Data zwrotu: " +loan_date_of_delivery, helvetica11b));
            clientData.add(new Paragraph("Agencja do której zostało zwrócono samochód: " + delivery_agency_name, helvetica11b));
            clientData.add(new Paragraph("Adres: " + delivery_agency_city + ", " + delivery_agency_post_code + ", " + delivery_agency_road + " " +delivery_agency_building_number, helvetica11b));
            clientData.add(new Paragraph( "Numer telefonu: " + delivery_agency_phone_number, helvetica11b));
            document.add(clientData);

            //sygnatura
            addEmptyLine(Signature, 2);
            Signature.add(new Paragraph("Osoba przyjmująca ....................        Klient ...................... ", helvetica16));
            document.add(Signature);
            document.close();

            // informacja
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informacja");
            alert.setHeaderText(null);
            alert.setContentText("Protokól zwrotu został wygenerowany...");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("/CSS/login.css").toExternalForm());
            dialogPane.getStyleClass().add("alert");
            alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Dana metoda służu do pobrania zdjęć samochodu z bazy danych oraz
     * wyświetlania ich w postać slideshow przy pomocy takiego elementu animacji jak Timeline.
     * W przypadku braku zdjęć z bazy danych jest obsługa blędów
     */
    public void slideshow() {
        ArrayList<Image> images = new ArrayList<Image>();
        AdminLoansController controller = new AdminLoansController();
        System.out.println(AdminLoansController.loan_id);
        Connection connection = DataBase.connect();
        int count_image = 1;
        try {
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT DISTINCT c.id_car, l.id_loan, p.photo FROM loans l " +
                    "INNER JOIN cars c on c.id_car = l.id_car " +
                    "INNER JOIN photo_car pc on c.id_car = pc.id_car " +
                    "INNER JOIN photos p on pc.id_photo = p.id_photo " +
                    "WHERE l.id_loan = " + AdminLoansController.loan_id);

            while (resultSet.next()) {
                InputStream is = resultSet.getBinaryStream("photo");
                InputStreamReader inputReader = new InputStreamReader(is);
                if(inputReader.ready()) {
                    File tempFile = new File("images/image"+count_image+".jpg");
                    FileOutputStream fos = new FileOutputStream(tempFile);
                    byte[] buffer = new byte[1024];
                    while (is.read(buffer) > 0) {
                        fos.write(buffer);
                    }
                    images.add(new Image(tempFile.toURI().toURL().toString()));
                }
                count_image++;
            }
            ilosc = count_image - 1;
            protokolyZdjecieCar.setImage(images.get(0));
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
                protokolyZdjecieCar.setImage(images.get(count));
                count++;
                if (count == ilosc) {
                    count = 0;
                }
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Dana metoda służy do przejścia do listy wypożyczeń klientów
     * @param event wykorzysuje się dla obsługi zdarzeń
     */
    @FXML
    void wracamyDoWypozyczen(ActionEvent event) {
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

    /**
     * Zmienia status wypożyczenia,
     * jeśli na 'Anulowany' to zmienia też status auta na 'Dostępny',
     *
     * jeśli na 'Wypożyczony' to zmienia status auta na 'Niedostępny',
     *
     * jeśli na 'Zwrócony' to zmienia status auta na 'Dostępny',
     * uzupełnia date zwrotu auta i aktualizuje koszt wypożyczenia
     *
     * @param event do obsługi zdarzen
     */
    @FXML
    void changeStatus(ActionEvent event){
        AdminLoansController controller = new AdminLoansController();
        java.sql.Date data_zwrotu = null;
        try {
            Connection connection = DataBase.connect();
            String query = "update loans set status = ? where id_loan = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            String status2 = String.valueOf(changeStatusBox.getSelectionModel().getSelectedItem());
            preparedStatement.setString(1, String.valueOf(status2));
            preparedStatement.setInt(2, AdminLoansController.loan_id);
            preparedStatement.execute();
            if (String.valueOf(status2).equals("Zwrócony")) {
                String query2 = "update cars set status = ?, id_agency = ? where id_car = ?";
                PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
                String status3 = "Dostępny";
                preparedStatement2.setString(1, status3);
                preparedStatement2.setInt(2, AdminController.id_agency);
                preparedStatement2.setInt(3,car_id);
                preparedStatement2.execute();


                try {
                    ResultSet rs = connection.createStatement().
                            executeQuery("select date_of_loan, cast(now() as date) as curr_date from loans where id_loan =  " + AdminLoansController.loan_id);

                    if (rs.next()) {
                        data_wynajmu = rs.getDate("date_of_loan");
                        data_zwrotu = rs.getDate("curr_date");
                    }
                }catch (SQLException throwables) {
                    throwables.printStackTrace();
                }


                long difference = data_zwrotu.getTime() - data_wynajmu.getTime();
                daysBetween = (int) (difference / (1000 * 60 * 60 * 24)) + 1;

                int newCost = daysBetween * cost;
                if(newCost<0){newCost = 0;}

                String query3 = "update loans set id_empreturn = ?, agency_of_delivery = ?, date_of_delivery = ?, " +
                        "cost = ? where id_loan = ?";
                PreparedStatement preparedStatement3 = connection.prepareStatement(query3);
                preparedStatement3.setInt(1, AdminController.userId);
                preparedStatement3.setInt(2, AdminController.id_agency);
                preparedStatement3.setDate(3, data_zwrotu);
                preparedStatement3.setInt(4,newCost);
                preparedStatement3.setInt(5, AdminLoansController.loan_id);
                preparedStatement3.execute();
                JOptionPane.showMessageDialog(frame, "Zmieniono status");

                return;
            }


            if (String.valueOf(status2).equals("Anulowany")) {
                String query2 = "update cars set status = ? where id_car = ?";
                PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
                String status3 = "Dostępny";
                preparedStatement2.setString(1, status3);
                preparedStatement2.setInt(2,car_id);
                preparedStatement2.execute();

                String query6 = "update loans set cost = ? where id_loan = ?";
                PreparedStatement preparedStatement6 = connection.prepareStatement(query6);
                preparedStatement6.setInt(1,0);
                preparedStatement6.setInt(2, AdminLoansController.loan_id);
                preparedStatement6.execute();
                JOptionPane.showMessageDialog(frame, "Zmieniono status");

            }

            if (String.valueOf(status2).equals("Wypożyczony")) {
                String query2 = "update cars set status = ? where id_car = ?";
                PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
                String status3 = "Niedostępny";
                preparedStatement2.setString(1, status3);
                preparedStatement2.setInt(2,car_id);
                preparedStatement2.execute();

                System.out.println(AdminController.userId);
                System.out.println(AdminController.id_agency);
                String query7 = "update loans set id_emphandover = ?, agency_of_loan = ? where id_loan = ?";
                PreparedStatement preparedStatement7 = connection.prepareStatement(query7);
                preparedStatement7.setInt(1,AdminController.userId);
                preparedStatement7.setInt(2,AdminController.id_agency);
                preparedStatement7.setInt(3, AdminLoansController.loan_id);
                preparedStatement7.execute();
                JOptionPane.showMessageDialog(frame, "Zmieniono status");

            }

            if (String.valueOf(status2).equals("Rezerwacja")) {
                String query2 = "update cars set status = ? where id_car = ?";
                PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
                String status3 = "Zarezerwowany";
                preparedStatement2.setString(1, status3);
                preparedStatement2.setInt(2,car_id);
                preparedStatement2.execute();
                JOptionPane.showMessageDialog(frame, "Zmieniono status");
            }
            Parent zmianaSceny = null;
            try {
                zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/admin/AdminLoansDetails.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene menu = new Scene(zmianaSceny);
            Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
            okno.setScene(menu);
            okno.centerOnScreen();
            okno.show();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML
    void AddPhotoDamage(ActionEvent event) {
        Parent zmianaSceny = null;
        try {
            zmianaSceny = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/admin/AdminAddPhotoDamage.fxml"));
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
     * Dana metoda (Initialize) zostaje wykonywana przy przejściu na daną scenę
     * Zajmuje się pobieraniem szczegółow konkretnego samochodu, danych klieta, który wypożycza
     * ten samochód, date wypożyczenia i informacje o uszkodzeniach samochodu jeśli takie są
     * @param url służy do przechowywania i modyfikacji modyfikatora zasobów
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TypProtokolu.getItems().addAll(protokoly);
        slideshow();
        AdminLoansController controller = new AdminLoansController();
        Connection con = DataBase.connect();
        try {
            ResultSet rs = con.createStatement().executeQuery("SELECT c.id_car, c.model, c.production_date, c.engine, c.fuel, c.registration_number, c.color, c.cost, " +
                    "u.user_name, u.surname, u.phone_number, u.email,l.status, l.id_car, l.date_of_loan, l.date_of_delivery, l.id_loan, d.description, d.damage_cost \n" +
                    "FROM cars c \n" +
                    "INNER JOIN loans l on c.id_car = l.id_car \n" +
                    "INNER JOIN users u on l.id_client = u.id_users \n" +
                    "LEFT JOIN damage d on d.id_car = l.id_car \n" +
                    "where l.id_loan = " + AdminLoansController.loan_id);
            if (rs.next()) {
                id_samochodu = rs.getInt("id_car");
                model = rs.getString("model");
                production_date = rs.getInt("production_date");
                engine = rs.getString("engine");
                fuel = rs.getString("fuel");
                registration_number = rs.getString("registration_number");
                color = rs.getString("color");
                cost = rs.getInt("cost");
                user_name = rs.getString("user_name");
                surname = rs.getString("surname");
                phone = rs.getString("phone_number");
                email = rs.getString("email");
                date_loan = rs.getDate("date_of_loan");
                date_delivery = rs.getDate("date_of_delivery");
                uszkodzenia = rs.getString("description");
                if(uszkodzenia == null){
                    uszkodzenia = "Brak";
                }
                suma_uszkodzen = rs.getInt("damage_cost");
                System.out.println("Suma uszkodzen "+suma_uszkodzen);
                status = rs.getString("status");
                id_detail_loan = rs.getInt("id_loan");
                car_id = rs.getInt("id_car");

                protokolyMarkaModel.setText(model);
                protokolyRokWyprodukowania.setText(String.valueOf(production_date));
                protokolySilnik.setText(engine);
                protokolyTypPaliwa.setText(fuel);
                protokolyNumerRejestracyjny.setText(registration_number);
                protokolyKolor.setText(color);
                protokolyCenaZaDobe.setText(String.valueOf(cost));
                protokolyImieKlienta.setText(user_name);
                protokolyNazwiskoKlienta.setText(surname);
                protokolyTelefonKlienta.setText(phone);
                protokolyEmailKlienta.setText(email);
                protokolyDataWypozyczenia.setText(date_loan.toString());
                if (date_delivery != null) {
                    protokolyDataZwrotu.setText(date_delivery.toString());
                }
                changeStatusBox.setValue(status);
                protokolyUszkodzenia.setText(uszkodzenia);
                if(status.equals("Rezerwacja")){
                    ObservableList<String> options = FXCollections.observableArrayList("Rezerwacja","Wypożyczony","Anulowany");
                    changeStatusBox.setValue(status);
                    changeStatusBox.setItems(options); // this statement adds all values in choiceBox
                }
                if(status.equals("Wypożyczony")){
                    ObservableList<String> options = FXCollections.observableArrayList("Wypożyczony","Zwrócony");
                    changeStatusBox.setValue(status);
                    changeStatusBox.setItems(options); // this statement adds all values in choiceBox
                }
                if(status.equals("Zwrócony")){
                    ObservableList<String> options = FXCollections.observableArrayList("Zwrócony");
                    changeStatusBox.setValue(status);
                    changeStatusBox.setItems(options); // this statement adds all values in choiceBox
                }
                if(status.equals("Anulowany")){
                    ObservableList<String> options = FXCollections.observableArrayList("Anulowany");
                    changeStatusBox.setValue(status);
                    changeStatusBox.setItems(options); // this statement adds all values in choiceBox
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
