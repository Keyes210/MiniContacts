package com.example.alexlowe.minicontacts;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ContactsPresenter {

    private ContactsInterface adapterInterface;

    public ContactsPresenter(ContactsInterface contactsInterface){
        this.adapterInterface = contactsInterface;
    }

    public String getPhoneListString(Contact contact) {
        HashMap<String, String> contactNumbers = contact.getNumbers();

        StringBuilder phoneListString = new StringBuilder();
        String newline = "";
        for (Map.Entry<String, String> entry : contactNumbers.entrySet())
        {
            phoneListString.append(newline)
                    .append((entry.getValue().equals("2")) ? "M" : "H")
                    .append(": ")
                    .append(phoneDisplay(entry.getKey()));
            newline = "\n";
        }

        return phoneListString.toString();
    }

    private String phoneDisplay(String number) {
        if(number.length() == 10){
            String first = number.substring(0,3);
            String second = number.substring(3,6);
            String third = number.substring(6,10);
            return String.format("(%s) %s-%s", first, second, third);
        }
        return number;
    }

    public void onCardClicked(Contact contact) {
        if(contact.getNumbers().size() > 1){
            launchDialog(contact);
        }else{
            startDialer(contact.getOnlyNumber());
        }
    }

    private void launchDialog(Contact contact) {
        final CharSequence[] numbersCharSeq = getCharSequences(contact);
        adapterInterface.launchDialerDialog(numbersCharSeq);
    }

    @NonNull
    private CharSequence[] getCharSequences(Contact contact) {
        ArrayList<String> numbersArrayList = new ArrayList<>();
        for (Map.Entry<String, String> entry : contact.getNumbers().entrySet()){
            numbersArrayList.add(entry.getKey());
        }

        return numbersArrayList
                .toArray(new CharSequence[numbersArrayList.size()]);
    }

    private void startDialer(String currentNumber){
        adapterInterface.startDialer(currentNumber);
    }

    public void numberChosen(String number) {
        startDialer(number);
    }

}
