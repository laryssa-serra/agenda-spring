package br.com.agenda.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.*;

@Entity
@Table(name = "telephones")
public class Telephone{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name="customer_id")
    @JsonBackReference
    private Customer customer;
    private String ddd;
    private String phoneNumber;

    public Telephone() {}

    public Telephone(String ddd, String phoneNumber) {
        this.ddd = ddd;
        this.phoneNumber = phoneNumber;
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
    public String getDdd() {
        return ddd;
    }

    public void setDdd(String ddd) {
        this.ddd = ddd;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Telephone{" +
                ", ddd='" + ddd + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}


