package com.alexeimoisseev.bashim.app;

/**
 * Created by amois on 04.11.14.
 */
public class PostBean {
    private String link;
    private Long id;
    private String description;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Long getId() {
        if(id == null) {
            Long l = Long.valueOf(link.substring(link.lastIndexOf("/") + 1));
            id = l;
        }
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toString() {
        return description;
    }
}
