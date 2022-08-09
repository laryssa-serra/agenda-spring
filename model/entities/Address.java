package br.com.agenda.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.*;

@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name="customer_id")
    @JsonBackReference
    private Customer customer;
    private String state;
    private String city;
    private String street;
    private String zipCode;
    private String reference;

    public Address(){}

    public Address(String state, String city, String street, String zipCode, String reference) {
        this.state = state;
        this.city = city;
        this.street = street;
        this.zipCode = zipCode;
        this.reference = reference;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @Override
    public String toString() {
        return "Address{" +
                "state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", reference='" + reference + '\'' +
                '}';
    }
}

