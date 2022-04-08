package service;

public class CarsService {
    public CarsService() {
    }

    public boolean createNewCar(String model, int production_date, String engine, int power, String fuel, String transmission, String color, int cost, String registration_number, String status, int id_agency) {
        DataBase.InsertInToCarsTable(model, production_date, engine, power, fuel, transmission, color, cost, registration_number, status, id_agency);
        return true;
    }
}

