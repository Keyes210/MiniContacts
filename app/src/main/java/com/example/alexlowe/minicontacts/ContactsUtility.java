package com.example.alexlowe.minicontacts;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by alexlowe on 9/14/16.
 */
public class ContactsUtility {

    public static void getContacts(final ContactsAdapter contactsAdapter, final Context context){
        Observable.fromCallable(new Callable<Cursor>() {

            @Override
            public Cursor call() throws Exception {
                return ContactsUtility.getContactsCursor(context);
            }
        }).map(new Func1<Cursor, List<Contact>>() {
            @Override
            public List<Contact> call(Cursor cursor) {
                return ContactsUtility.traverseCursor(cursor);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Contact>>() {
                    @Override
                    public void call(List<Contact> contacts) {
                        contactsAdapter.setUpContacts(contacts);
                    }
                });
    }


    public static Cursor getContactsCursor(Context context){
        Uri contactsUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String[] PROJECTION = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.Contacts.PHOTO_URI,
                ContactsContract.CommonDataKinds.Phone.TYPE
        };

        //can be classlevel static finals
        String SELECTION = ContactsContract.Contacts.HAS_PHONE_NUMBER + "= ?";// then args
        String SORT_ORDER = ContactsContract.Contacts.DISPLAY_NAME + " ASC";

        return context.getContentResolver().query(
                contactsUri,
                PROJECTION,
                SELECTION,
                null,
                SORT_ORDER
        );
    }

    public static List<Contact> traverseCursor(Cursor cursor) {
        List<Contact> masterList = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.
                        getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String number = cursor.getString(cursor.
                        getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String numberType = cursor.getString(cursor.
                        getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));

                Contact contact = new Contact(name, rawNumber(number), numberType);

                if (masterList.isEmpty()) {
                    masterList.add(contact);
                } else {
                    addContact(masterList, contact, rawNumber(number), numberType);
                }
            }
            cursor.close();
        }

        return masterList;
    }

    private static void addContact(List<Contact> masterList, Contact contact, String number, String numberType) {
        Contact lastContact = masterList.get(masterList.size() - 1);

        if (lastContact.getName().equals(contact.getName())) {
            lastContact.addNumber(number, numberType);
        } else {
            masterList.add(contact);
        }
    }

    private static String rawNumber(String input) {
        if (input.startsWith("1")) {
            input = input.substring(1);
        }
        return input.replaceAll("[^0-9]+", "");
    }

}
