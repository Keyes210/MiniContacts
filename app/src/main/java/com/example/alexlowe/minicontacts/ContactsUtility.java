package com.example.alexlowe.minicontacts;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.ArrayList;

/**
 * Created by alexlowe on 9/14/16.
 */
public class ContactsUtility {
    private Context context;
    private ArrayList<Contact> masterList;

    public ContactsUtility(Context context){
        this.context = context;
        masterList = ContactsSingleton.getInstance().masterList;
    }

    public ArrayList<Contact> getContactList(){
        Uri contactsUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String[] PROJECTION = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.Contacts.PHOTO_URI,
                ContactsContract.CommonDataKinds.Phone.TYPE
        };

        String SELECTION = ContactsContract.Contacts.HAS_PHONE_NUMBER + " = '1'";
        String SORT_ORDER = ContactsContract.Contacts.DISPLAY_NAME + " ASC";

        Cursor contactsCursor = context.getContentResolver().query(
                contactsUri,
                PROJECTION,
                SELECTION,
                null,
                SORT_ORDER
        );

        while (contactsCursor.moveToNext()){
            String name = contactsCursor.getString(contactsCursor.
                    getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String number = contactsCursor.getString(contactsCursor.
                    getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String numberType = contactsCursor.getString(contactsCursor.
                    getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
            String photoUri = contactsCursor.getString(contactsCursor.
                    getColumnIndex(ContactsContract.Contacts.PHOTO_URI));

            Contact contact = new Contact(name, number, numberType, photoUri);

            if(masterList.isEmpty()){
                masterList.add(contact);
            }else{
                addAdditionalNumberToContact(contact, number, numberType);
            }
        }

        return masterList;
    }

    private void addAdditionalNumberToContact(Contact contact, String number, String numberType) {
        Contact lastContact = masterList.get(masterList.size()-1);

        if(lastContact.getName().equals(contact.getName())){
            lastContact.addNumber(number, numberType);
        }else{
            masterList.add(contact);
        }
    }
}
