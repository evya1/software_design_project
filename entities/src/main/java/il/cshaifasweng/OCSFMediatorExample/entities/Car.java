package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;

@Entity
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String licensePlate;
    private double price;
    @Column(name = "manufacturing_year")
    private int year;

    public Car() {
    }

    public Car(String licensePlate, double price, int year) {
        super();
        this.licensePlate = licensePlate;
        this.price = price;
        this.year = year;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getId() {
        return id;
    }
}