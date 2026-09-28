package com.ford.competitor.dto;

import java.util.List;

public class CompetitorResponseDto {

    private String brand;
    private String model;
    private String version;
    private List<SpecificationItemDto> specifications;

    public CompetitorResponseDto() {
    }

    public CompetitorResponseDto(String brand, String model, String version, List<SpecificationItemDto> specifications) {
        this.brand = brand;
        this.model = model;
        this.version = version;
        this.specifications = specifications;
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

    public List<SpecificationItemDto> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(List<SpecificationItemDto> specifications) {
        this.specifications = specifications;
    }
}