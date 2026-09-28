package com.ford.competitor.dto;

import jakarta.validation.constraints.NotBlank;

public class VehicleSpecificationRequestDto {

    @NotBlank(message = "A marca é obrigatória")
    private String brand;
    @NotBlank(message = "O modelo é obrigatório")
    private String model;
    @NotBlank(message = "A versão é obrigatória")
    private String version;
    @NotBlank(message = "O atributo é obrigatório")
    private String attribute;
    @NotBlank(message = "O valor da especificação é obrigatório")
    private String value;

    public VehicleSpecificationRequestDto() {
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
