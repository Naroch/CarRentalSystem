package controllers.admin;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.AdminReport;
import models.Report;
import service.DataBase;

import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AdminReportController extends Component implements Initializable {

    /**
     * zmienna globalna do zysków końcowych
     */
    int sum=0;

    /**
     * Zmienne na dane do raportu imie i Nazwisko użytkownika oraz przedział czasowy za który został wygenerowany raport
     */
    String from;
    String to;
    public void setfrom(String from){this.from=from;}
    public void setto(String to){this.to=to;}

    /**
     * Metoda zmienia scenę na logowanie po kliknięciu przycisku
     * @param event służy do obsługi zdarzeń
     * @throws IOException obsługa błędów bazy danych
     */
    @FXML
    void AdminLogoutButton(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader =new FXMLLoader(getClass().getClassLoader().getResource("scenes/login/Login.fxml"));
        Parent root = fxmlLoader.load();
        Scene menu = new Scene(root);
        Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
        okno.setScene(menu);
        okno.centerOnScreen();
        okno.show();
    }

    /**
     * Metoda zmienia scenę na katalog samochodów po kliknięciu przycisku
     * @param event służy do obsługi zdarzeń
     * @throws IOException obsługa błędów bazy danych
     */
    @FXML
    void AdminCarCatalogueButton(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader1 =new FXMLLoader(getClass().getClassLoader().getResource("scenes/admin/Admin.fxml"));
        Parent root1 = fxmlLoader1.load();
        Scene menu = new Scene(root1);
        Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
        okno.setScene(menu);
        okno.centerOnScreen();
        okno.show();
    }

    /**
     * Metoda zmienia scenę na scenę wypożyczenia klientów po kliknięciu przycisku
     * @param event służy do obsługi zdarzeń
     * @throws IOException obsługa błędów bazy danych
     */
    @FXML
    void AdminLoansButton(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader =new FXMLLoader(getClass().getClassLoader().getResource("scenes/admin/AdminLoans.fxml"));
        Parent root = fxmlLoader.load();
        Scene menu = new Scene(root);
        Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
        okno.setScene(menu);
        okno.centerOnScreen();
        okno.show();
    }

    /**
     * Metoda zmienia scenę na scenę filii po kliknięciu przycisku
     * @param event służy do obsługi zdarzeń
     * @throws IOException obsługa błędów bazy danych
     */
    @FXML
    void AdminFiliasButton(ActionEvent event)throws IOException{
        FXMLLoader fxmlLoader =new FXMLLoader(getClass().getClassLoader().getResource("scenes/admin/AdminFilie.fxml"));
        Parent root = fxmlLoader.load();
        Scene menu = new Scene(root);
        Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
        okno.setScene(menu);
        okno.centerOnScreen();
        okno.show();
    }

    /**
     * Metoda zmienia scenę na scenę ustawień po kliknięciu przycisku
     * @param event służy do obsługi zdarzeń
     * @throws IOException obsługa błędów bazy danych
     */
    @FXML
    void AdminSettingsButton(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader =new FXMLLoader(getClass().getClassLoader().getResource("scenes/admin/AdminSettings.fxml"));
        Parent root = fxmlLoader.load();
        Scene menu = new Scene(root);
        Stage okno = (Stage) ((Node) event.getSource()).getScene().getWindow();
        okno.setScene(menu);
        okno.centerOnScreen();
        okno.show();
    }

    /**
     * Deklaracja FXML tabeli i poszczególnych kolumn
     */
    @FXML
    private DatePicker date_from;

    @FXML
    private DatePicker date_to;

    public TableView<Report> table_view;
    public TableColumn<Report, String> user_name;
    public TableColumn<Report, String> surname;
    public TableColumn<Report, String> model;
    public TableColumn<Report, Date> date_of_loan;
    public TableColumn<Report, Date> date_of_expiry;
    public TableColumn<Report, Integer> price;


    ObservableList<Report> oblist_raport = FXCollections.observableArrayList();

    /**
     * Metoda wyśwetla informację pobrane z bazy danych i wypełnia tabele
     * @param event służy do obsługi zdarzeń
     * @throws SQLException służy do obsługi błędów SQL
     */
    @FXML
    void report_button(ActionEvent event) throws SQLException {
        deleterows();
        sum=0;
        setfrom(date_from.getValue().toString());
        setto(date_to.getValue().toString());

        try {
            Connection connection = DataBase.connect();
            ResultSet rs1 = connection.createStatement().executeQuery("select u.user_name, u.surname, c.model, l.date_of_loan, l.date_of_expiry, l.cost from users u, loans l, cars c where c.id_car=l.id_car AND id_client=id_users AND date_of_loan BETWEEN '" + date_from.getValue() + "' AND '" + date_to.getValue() + "'");

            int psum;
            while (rs1.next()) {
                oblist_raport.add(new Report(rs1.getString("user_name"), rs1.getString("surname"), rs1.getString("model"), rs1.getDate("date_of_loan"), rs1.getDate("date_of_expiry"), rs1.getInt("cost")));
                psum=rs1.getInt("cost");
                sum+=psum;
                System.out.println("Koncowa cena"+sum);
            }
        } catch (SQLException e) {
        }

        user_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        surname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        model.setCellValueFactory(new PropertyValueFactory<>("model"));
        date_of_loan.setCellValueFactory(new PropertyValueFactory<>("date_of_loan"));
        date_of_expiry.setCellValueFactory(new PropertyValueFactory<>("date_of_delivery"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));

        table_view.setItems(oblist_raport);

    }


    /**
     * Metoda usuwa wszystkie wiersze z tabeli
     */
    public void deleterows(){
        table_view.getItems().removeAll(table_view.getItems());
    }

    /**
     * Dodawanie pustej linii w dokumencie
     * @param paragraph służy do tworzenia paragrafu za pomocą biblioteki itextpdf
     * @param number przekazywana zmienna, jest ilością pustych linii
     */
    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }


    /**
     * Generowanie raprtów po kliknięciu i wybraniu miejsca docelowego
     * @param event służy do obsługi zdarzeń
     * @throws SQLException służy do obsługi błędów SQL
     */
    @FXML
    void Generate_report(ActionEvent event) throws SQLException{
        String path="";
        JFileChooser j=new JFileChooser();
        j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int x=j.showSaveDialog(this);

        if(x==JFileChooser.APPROVE_OPTION) {
            path=j.getSelectedFile().getPath();
            path=path+"";
        }
        String path1=System.getProperty("user.dir");
        System.out.println(path1);
        try {
            Document document = new Document();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
            LocalDateTime now = LocalDateTime.now();

            //czcionka
            BaseFont helvetica = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
            com.itextpdf.text.Font helvetica16=new Font(helvetica,16);
            com.itextpdf.text.Font helvetica11=new Font(helvetica,11);

            PdfWriter.getInstance(document, new FileOutputStream(path+"/report"+dtf.format(now).toString()+".pdf"));
            document.open();
            Paragraph preface = new Paragraph();
            Paragraph finalPrice = new Paragraph();
            Paragraph Signature = new Paragraph();
            //pusta linia
            addEmptyLine(preface, 1);


            preface.add(new Paragraph("                                      Raport wypożyczeń od: " + from +" do: "+ to, helvetica11));
            addEmptyLine(preface, 1);
            // dane raportu
            preface.add(new Paragraph("Raport Wygenerował : ", helvetica11));
            preface.add(new Paragraph("Administrator ", helvetica16));
            preface.add(new Paragraph("Emilian Rutycki ", helvetica11));
            preface.add(new Paragraph("Rzeszów, ul Hetmańska 20,  35-326", helvetica11));
            addEmptyLine(preface, 7);
            preface.add(new Paragraph("                                               Wypożyczenia", helvetica16));
            addEmptyLine(preface, 1);
            document.add(preface);

            PdfPTable table =new PdfPTable(6);
            table.setWidthPercentage(80);
            table.addCell("Imie Klienta");
            table.addCell("Nazwisko Klienta");
            table.addCell("Model");
            table.addCell("Data odbioru");
            table.addCell("Data oddania");
            table.addCell("Cena");
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            for (int counter = 0; counter < oblist_raport.size(); counter++) {
                if (counter % 2 == 1) {
                    table.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);
                } else {
                    table.getDefaultCell().setBackgroundColor(GrayColor.LIGHT_GRAY);
                }
                table.addCell(new Phrase(oblist_raport.get(counter).getName()));
                table.addCell(new Phrase(oblist_raport.get(counter).getSurname()));
                table.addCell(new Phrase(oblist_raport.get(counter).getModel()));
                table.addCell(new Phrase(String.valueOf(oblist_raport.get(counter).getDate_of_loan())));
                table.addCell(new Phrase(String.valueOf(oblist_raport.get(counter).getDate_of_delivery())));
                table.addCell(new Phrase(String.valueOf(oblist_raport.get(counter).getPrice())));

            }
            document.add(table);
            //final summary cost
            addEmptyLine(finalPrice, 2);
            finalPrice.add(new Paragraph("Suma zysków wynosi: " + sum +" PLN", helvetica11));
            document.add(finalPrice);
            //signature
            addEmptyLine(Signature, 3);
            Signature.add("                                                                     Administrator ...............................");
            document.add(Signature);

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Metoda dodaje wszystkie filie.
     * @param url służy do przechowywania i modyfikacjii lokalizatora zasobów
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}