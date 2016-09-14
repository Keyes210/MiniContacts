package com.example.alexlowe.minicontacts;

import java.util.ArrayList;

/**
 * Created by alexlowe on 9/13/16.
 */
public class ContactsSingleton {
    public ArrayList<Contact> masterList;

    private ContactsSingleton() {
        masterList = new ArrayList<Contact>();
    }

    private static ContactsSingleton instance;

    public static ContactsSingleton getInstance() {
        if (instance == null) instance = new ContactsSingleton();
        return instance;
    }
    //ContactsSingleton.getInstance().masterList.get(position);
}



