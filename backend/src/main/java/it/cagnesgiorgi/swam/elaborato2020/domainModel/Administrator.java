package it.cagnesgiorgi.swam.elaborato2020.domainModel;

import javax.persistence.Entity;

@Entity
public class Administrator extends User {

    public Administrator(String uuid){
        super(uuid);
    }

    public Administrator(){

    }

    @Override
    public boolean hasRole(String role){
        if(role != null) {
            return role.equals("ADMIN");
        }
        return false;
    }

}
