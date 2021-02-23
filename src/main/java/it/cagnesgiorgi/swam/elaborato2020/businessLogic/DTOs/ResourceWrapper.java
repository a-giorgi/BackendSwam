package it.cagnesgiorgi.swam.elaborato2020.businessLogic.DTOs;

import com.google.gson.Gson;

import java.util.HashMap;

public class ResourceWrapper {
    private Object resource;
    private HashMap<String, LinkResource> links;
    private transient Gson gson = new Gson();

    public ResourceWrapper(Object baseDTO) {
        this.resource = baseDTO;
        links = new HashMap<String, LinkResource>();
    }

    public HashMap<String, LinkResource> getLinks() {
        return links;
    }

    public void setLinks(HashMap<String, LinkResource> links) {
        this.links = links;
    }

    public Object getResource() {
        return resource;
    }

    public ResourceWrapper addResourceInfo(String key, LinkResource value){
        links.put(key,value);
        return this;
    }

    public String toJson(){
        return gson.toJson(this);
    }





}
