package com.winterproject.youssufradi.life_logger.Photo;

import com.winterproject.youssufradi.life_logger.helpers.Contact;

import java.util.ArrayList;


/**
 * Created by GUC on 30.07.17.
 */

public class PhotoEntry {

    private Long id;
    private final String name;
    private final String description;
    private final ArrayList<String> photos;
    private final ArrayList<Contact> contacts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getPhotos() {
        return photos;
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public PhotoEntry(Long id, String name, String description, ArrayList<String> photos, ArrayList<Contact> contacts) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.photos = photos;
        this.contacts = contacts;
    }
}
