package it.cagnesgiorgi.swam.elaborato2020.domainModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="tag")
public class Tag extends BaseEntity {
    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "tags")
    private List<Feed> feeds;

    private boolean active;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Tag(){

    }

    public Tag(String uuid){
        super(uuid);
    }

    public List<Feed> getFeeds() {
        return feeds;
    }

    public void setFeeds(List<Feed> feeds) {
        this.feeds = feeds;
    }



}
