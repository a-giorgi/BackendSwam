package it.cagnesgiorgi.swam.elaborato2020.businessLogic.DTOs;

public class LinkResource {
    private String href;
    private String verbs;
    private String description;

    public LinkResource(String href, String verbs, String description) {
        this.href = href;
        this.verbs = verbs;
        this.description = description;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getVerbs() {
        return verbs;
    }

    public void setVerbs(String verbs) {
        this.verbs = verbs;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
