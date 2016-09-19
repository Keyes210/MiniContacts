package com.example.alexlowe.minicontacts;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactsPresenter {

    public interface Callbacks {
        void buildDialog(Context context);
    }


    public ContactsPresenter(Callbacks callbacks) {

    }

    public List<Contact> getContactsList(){
        return null;
    }

    public void bindAdapterViews(ContactsAdapter.ViewHolder holder, Contact contact) {
        TextView tvName = holder.tvName;
        tvName.setText(contact.getName());

        TextView tvNumbers = holder.tvNumbers;
        String phoneListString = getPhoneListString(contact);
        tvNumbers.setText(phoneListString);

        CardView cardView = holder.cardView;
        CardClickListner cardClickListner = new CardClickListner(contact);
        cardView.setOnClickListener(cardClickListner);
    }

    private String getPhoneListString(Contact contact) {
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

    public void launchDialog(Contact contact) {
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

    @NonNull
    private CharSequence[] getCharSequences(Contact contact) {
        ArrayList<String> numbersArrayList = new ArrayList<>();
        for (Map.Entry<String, String> entry : contact.getNumbers().entrySet()){
            numbersArrayList.add(entry.getKey());
        }

        return numbersArrayList
                .toArray(new CharSequence[numbersArrayList.size()]);
    }

    public void startDialer(String current_number){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + current_number));
        //context.startActivity(intent);
    }


}
