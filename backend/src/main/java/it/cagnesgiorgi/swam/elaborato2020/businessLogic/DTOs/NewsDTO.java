package it.cagnesgiorgi.swam.elaborato2020.businessLogic.DTOs;

import com.google.gson.Gson;

public class NewsDTO extends BaseDTO{
    public String title;
    public String description;
    public String href;
    public String tags;
    public String source;
    public String enclosure;
    public boolean topNews;

    public NewsDTO(String title, String description, String href, String tags, String source, boolean topNews, String enclosure) {
        this.title = title;
        this.description = description;
        this.href = href;
        this.tags = tags;
        this.topNews = topNews;
        this.enclosure = enclosure;
        this.source = source;

    }
}
