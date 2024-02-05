package it.cagnesgiorgi.swam.elaborato2020.businessLogic.DTOs;

import com.google.gson.JsonObject;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public abstract class BaseDTO {
    @XmlElement
    protected Long id;

    public void setId(long id){
        this.id = id;
    }

    public Long getId(){
        return this.id;
    }



}
