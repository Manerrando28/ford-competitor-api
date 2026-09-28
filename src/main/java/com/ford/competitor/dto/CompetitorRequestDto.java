package com.ford.competitor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class CompetitorRequestDto {

    @NotBlank(message = "A marca é obrigatória")
    private String brand;

    @NotBlank(message = "O modelo é obrigatório")
    private String model;

    @NotBlank(message = "A versão é obrigatória")
    private String version;

    @NotEmpty(message = "A lista de atributos não pode estar vazia")
    private List<@NotBlank(message = "Cada atributo deve ser informado") String> attributes;

    public CompetitorRequestDto() {
    }

    public CompetitorRequestDto(String brand, String model, String version, List<String> attributes) {
        this.brand = brand;
        this.model = model;
        this.version = version;
        this.attributes = attributes;
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

    public List<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }
}
