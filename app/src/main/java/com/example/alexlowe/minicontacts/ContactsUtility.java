package com.example.alexlowe.minicontacts;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexlowe on 9/14/16.
 */
public class ContactsUtility {
    public static final String[] PROJECTION = new String[]{
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.Contacts.PHOTO_URI,
            ContactsContract.CommonDataKinds.Phone.TYPE
    };

    public static final String SELECTION = ContactsContract.Contacts.HAS_PHONE_NUMBER + "= ?";
    public static final String[] SELECTION_ARGS = {"1"};
    public static final String SORT_ORDER = ContactsContract.Contacts.DISPLAY_NAME + " ASC";

    public static List<Contact> getContactList(Context context) {
        List<Contact> masterList = new ArrayList<>();

        //should be backgrounded
        Uri contactsUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        Cursor contactsCursor = context.getContentResolver().query(
                contactsUri,
                PROJECTION,
                SELECTION,
                SELECTION_ARGS,
                SORT_ORDER
        );

        if (contactsCursor != null) {
            while (contactsCursor.moveToNext()) {
                String name = contactsCursor.getString(contactsCursor.
                        getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String number = contactsCursor.getString(contactsCursor.
                        getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String numberType = contactsCursor.getString(contactsCursor.
                        getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));

                Contact contact = new Contact(name, rawNumber(number), numberType);

                if (masterList.isEmpty()) {
                    masterList.add(contact);
                } else {
                    addContact(masterList, contact, rawNumber(number), numberType);
                }
            }
            contactsCursor.close();
        }

        return masterList;
    }

    private static void addContact(List<Contact> contactList, Contact contact,
                                   String number, String numberType) {
        Contact lastContact = contactList.get(contactList.size() - 1);

        if (lastContact.getName().equals(contact.getName())) {
            lastContact.addNumber(number, numberType);
        } else {
            contactList.add(contact);
        }
    }

    private static String rawNumber(String input) {
        if (input.startsWith("1")) {
            input = input.substring(1);
        }
        return input.replaceAll("[^0-9]+", "");
    }

}
