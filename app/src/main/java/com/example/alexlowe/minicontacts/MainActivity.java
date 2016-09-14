package com.example.alexlowe.minicontacts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ContactsUtility contactsUtility = new ContactsUtility(this);
        ArrayList<Contact> contactList = contactsUtility.getContactList();

        RecyclerView rvContacts = (RecyclerView) findViewById(R.id.contacts_recyclerview);
        ContactsAdapter contactsAdapter = new ContactsAdapter(this, contactList);

        rvContacts.setAdapter(contactsAdapter);
        rvContacts.setLayoutManager(new LinearLayoutManager(this));

    }


}
