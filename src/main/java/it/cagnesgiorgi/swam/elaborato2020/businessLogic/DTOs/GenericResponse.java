package it.cagnesgiorgi.swam.elaborato2020.businessLogic.DTOs;


public class GenericResponse {
    public String message;
    public String code;

    public GenericResponse(String message, String code) {
        this.message = message;
        this.code = code;
    }
}
