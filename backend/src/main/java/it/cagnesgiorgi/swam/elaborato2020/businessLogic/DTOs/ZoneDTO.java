package it.cagnesgiorgi.swam.elaborato2020.businessLogic.DTOs;

public class ZoneDTO extends BaseDTO {
    public String name;
    public String countryCode;

    public ZoneDTO(String name, String countryCode) {
        this.name = name;
        this.countryCode = countryCode;
    }
}
