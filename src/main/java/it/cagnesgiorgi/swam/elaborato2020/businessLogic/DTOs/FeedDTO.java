package it.cagnesgiorgi.swam.elaborato2020.businessLogic.DTOs;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.cagnesgiorgi.swam.elaborato2020.domainModel.Feed;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;

@XmlRootElement
public class FeedDTO extends BaseDTO {

    @XmlElement
    private String name;
    @XmlElement
    private String countryCode;
    @XmlElement
    private String url;
    //tags are ids separated by comma eg: 1,4,2,5
    @XmlElement
    private String tags;
    @XmlElement
    private boolean topNews;
    @XmlElement
    private boolean active;

    public FeedDTO(){

    }

    public String getName() {
        return name;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getUrl() {
        return url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public boolean isTopNews() {
        return topNews;
    }

    public void setTopNews(boolean topNews) {
        this.topNews = topNews;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
