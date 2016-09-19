package com.example.alexlowe.minicontacts;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ContactsPresenter.Callbacks{
    ContactsAdapter contactsAdapter;
    ContactsPresenter contactsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactsPresenter = new ContactsPresenter(callbacks);

        ArrayList<Contact> contactList = (ArrayList<Contact>) contactsPresenter.getContactsList();

        RecyclerView rvContacts = (RecyclerView) findViewById(R.id.contacts_recyclerview);
        contactsAdapter = new ContactsAdapter(this, contactList);
        rvContacts.setAdapter(contactsAdapter);
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                contactsAdapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                contactsAdapter.filter(newText);
                return true;
            }
        });
        return true;
    }

    @Override
    public void buildDialog(Context context) {
        final CharSequence[] numbersCharSeq = getCharSequences(contact);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose a number:");
        builder.setItems(numbersCharSeq, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startDialer(numbersCharSeq[which].toString());
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
