package it.cagnesgiorgi.swam.elaborato2020.domainModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "zone")
public class Zone extends BaseEntity {
    private String countryName;

    @Column(unique = true)
    private String countryCode;

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Zone(String uuid){
        super(uuid);
    }

    public Zone() {

    }
}
