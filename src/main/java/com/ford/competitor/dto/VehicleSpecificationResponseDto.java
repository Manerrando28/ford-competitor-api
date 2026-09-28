package com.ford.competitor.dto;

public class VehicleSpecificationResponseDto {

    private Long id;
    private String brand;
    private String model;
    private String version;
    private String attribute;
    private String value;

    public VehicleSpecificationResponseDto() {
    }

    public VehicleSpecificationResponseDto(Long id, String brand, String model, String version,
                                           String attribute, String value) {
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
