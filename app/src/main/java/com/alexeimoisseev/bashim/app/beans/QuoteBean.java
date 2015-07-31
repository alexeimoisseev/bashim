package com.alexeimoisseev.bashim.app.beans;

import android.util.Log;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.text.ParseException;

import java.util.Date;
import java.util.Locale;

/**
 * Created by amois on 04.11.14.
 */
public class QuoteBean {
    private String link;
    private Long id;
    private Date date;
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

    public void setDate(String date) throws ParseException {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("EEE, dd MMM yyyy kk:mm:ss Z").withLocale(Locale.ENGLISH);
        this.date = dtf.parseDateTime(date).toDate();

    }

    public void setDate(Long date) {
        this.date = new Date(date);
    }

    public Date getDate() {
        return date;
    }
}
