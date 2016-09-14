package com.example.alexlowe.minicontacts;

import java.util.HashMap;

/**
 * Created by alexlowe on 9/13/16.
 */
public class Contact {
    private String name;
    private HashMap<String,String> numbers;
    private String photoURI;

    public Contact(String name, String number, String type, String photoURI){
        this.name = name;
        numbers = new HashMap<>();
        addNumbers(number, type);
        this.photoURI = photoURI;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, String> getNumbers() {
        return numbers;
    }

    public void addNumbers(String number, String type) {
        this.numbers.put(number, type);
    }

    public String getPhotoURI() {
        return photoURI;
    }

    public void setPhotoURI(String photoURI) {
        this.photoURI = photoURI;
    }
}
