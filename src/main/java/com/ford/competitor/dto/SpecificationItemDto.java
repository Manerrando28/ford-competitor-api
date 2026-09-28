package com.ford.competitor.dto;

public class SpecificationItemDto {

    private String attribute;
    private String value;
    private boolean available;

    public SpecificationItemDto() {
    }

    public SpecificationItemDto(String attribute, String value, boolean available) {
        this.attribute = attribute;
        this.value = value;
        this.available = available;
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

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}