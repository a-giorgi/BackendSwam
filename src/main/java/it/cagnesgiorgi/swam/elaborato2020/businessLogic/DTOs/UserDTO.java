package it.cagnesgiorgi.swam.elaborato2020.businessLogic.DTOs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.cagnesgiorgi.swam.elaborato2020.domainModel.Zone;

import javax.json.bind.annotation.JsonbNillable;
import javax.json.bind.annotation.JsonbProperty;
import javax.persistence.Access;
import javax.xml.bind.annotation.*;

@XmlRootElement
@JsonbNillable(false)
public class UserDTO extends BaseDTO {
    @XmlElement
    private String username;
    @XmlElement
    private String password;
    @XmlElement
    private String oldPassword;
    @XmlElement
    private String confirmPassword;
    @XmlElement
    private String email;
    @XmlElement
    private String countryCode;

    private final transient Gson gson =  new Gson();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public boolean assertRequired(){
        return (username != null) && (password != null) && (email != null);
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }


    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword){
        this.oldPassword = oldPassword;
    }

    public boolean checkPasswordRequirements(){
        if(password==null){
            return false;
        }
        if(confirmPassword==null){
            return false;
        }
        if(password.length()<8){
            return false;
        }
        return password.equals(confirmPassword);
    }


    public String toJson(){
        return gson.toJson(this);
    }

}
