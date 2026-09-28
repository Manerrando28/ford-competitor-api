package com.ford.competitor.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_vehicle_specifications")
public class VehicleSpecification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String version;

    @Column(nullable = false)
    private String attribute;

    @Column(name = "spec_value", nullable = false)
    private String value;

    public VehicleSpecification() {
    }

    public VehicleSpecification(Long id, String brand, String model, String version, String attribute, String value) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.version = version;
        this.attribute = attribute;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}