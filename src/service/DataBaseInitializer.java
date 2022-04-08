package service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import service.DataBase;
public class DataBaseInitializer {
    public DataBaseInitializer() {
    }

    /**
     * Funkcja przy uruchomianiu programu sprwadza czy istnieje baza, czy są stworzone tabele i wypełnia je jeśli są puste.
     */
    public static void initializeDataBase() {
        DataBase.createDatabase();
        createAllTables();
        DataBase.nullCheckAgency();
        DataBase.nullCheckUsers();
        DataBase.nullCheckCars();
        DataBase.nullCheckLoans();
        DataBase.nullCheckDamage();
        DataBase.nullCheckPhotos();
        DataBase.nullCheckPhoto_car();
        DataBase.nullCheckPhoto_damage();
    }

    /**
     * Funkcja tworzy po kolei każdą tabelę w bazie.
     */
    private static void createAllTables() {
        DataBase.createAgencyTable();
        DataBase.checkIfUsersExist();
        DataBase.checkIfCarsExist();
        DataBase.checkIfLoansExist();
        DataBase.checkIfDamageExist();
        DataBase.createPhotosTable();
        DataBase.checkIfPhotoCarExist();
        DataBase.checkIfPhoto_DamageExist();

    }
}

