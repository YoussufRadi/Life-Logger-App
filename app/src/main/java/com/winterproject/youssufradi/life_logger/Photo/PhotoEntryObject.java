package com.winterproject.youssufradi.life_logger.Photo;

import com.winterproject.youssufradi.life_logger.helpers.Contact;

import java.util.ArrayList;


/**
 * Created by GUC on 30.07.17.
 */

public class PhotoEntryObject {

    private final int type;
    private long id;
    private final String name;
    private final String description;
    private final ArrayList<String> photos;
    private final ArrayList<Contact> contacts;

    public int getType() {
        return type;
    }

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

    public PhotoEntryObject(long id, String name, String description, int type,
                            ArrayList<String> photos, ArrayList<Contact> contacts) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.photos = photos;
        this.contacts = contacts;
    }

    public PhotoEntryObject(long id, String name, String description, int type, String filePath) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.photos = new ArrayList<>();
        photos.add(filePath);
        this.contacts = new ArrayList<>();
    }

    public void addNewPhoto(String photo){
        this.photos.add(photo);
    }
}
