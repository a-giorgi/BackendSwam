package it.cagnesgiorgi.swam.elaborato2020.domainModel;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import it.cagnesgiorgi.swam.elaborato2020.domainModel.BaseEntity;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="role")
@Transactional
@Table(name = "user")
public class User extends BaseEntity {

    @Column(unique = true)
    protected String username;

    @Column(unique = true)
    protected String email;
    protected String password;

    //this is used to hash the password
    protected String salt;

    @ManyToOne
    protected Zone zone;

    public User() {
        super();
    }
    public User(String uuid) {
        super(uuid);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public boolean hasRole(String role){
        //basic user has no role: return false everytime
        return false;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    //password encryption is managed by the business logic
    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }
}
