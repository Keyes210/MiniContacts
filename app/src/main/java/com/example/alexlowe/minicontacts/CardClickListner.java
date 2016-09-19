package com.example.alexlowe.minicontacts;

import android.view.View;

public class CardClickListner implements View.OnClickListener {
    private Contact contact;
    ContactsPresenter contactsPresenter;

    public CardClickListner(Contact contact){
        this.contact = contact;
    }

    @Override
    public void onClick(View v) {
        if(contact.getNumbers().size() > 1){
            contactsPresenter.launchDialog(contact);
        }else{
            contactsPresenter.startDialer(contact.getOnlyNumber());
        }
    }

}
