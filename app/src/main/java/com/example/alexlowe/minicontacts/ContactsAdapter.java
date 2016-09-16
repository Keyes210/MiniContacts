package com.example.alexlowe.minicontacts;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by alexlowe on 9/14/16.
 */
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder>{
    private ArrayList<Contact> contactList;
    private ArrayList<Contact> contactListCopy;
    private Context context;

    public ContactsAdapter(Context context, ArrayList<Contact> contacts){
        this.contactList = contacts;
        this.contactListCopy = new ArrayList<>();
        for(Contact contact : contacts){
            contactListCopy.add(contact);
        }
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_contact, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Contact contact = contactList.get(position);

        TextView tvName = holder.tvName;
        tvName.setText(contact.getName());

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

        TextView tvNumbers = holder.tvNumbers;
        tvNumbers.setText(phoneListString);

        CardView cardView = holder.cardView;
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contact.getNumbers().size() > 1){
                    launchDialog(contact);
                }else{
                    startDialer(contact.getOnlyNumber());
                }

            }
        });
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

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public void filter(String text) {
        if(text.isEmpty()){
            contactList.clear();
            contactList.addAll(contactListCopy);
        } else{
            ArrayList<Contact> result = new ArrayList<>();
            text = text.toLowerCase();
            for(Contact item: contactListCopy){
                if(item.getName().toLowerCase().contains(text)){
                    result.add(item);
                }
            }
            contactList.clear();
            contactList.addAll(result);
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvName;
        public TextView tvNumbers;
        public CardView cardView;

        public ViewHolder(View itemView){
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvNumbers = (TextView) itemView.findViewById(R.id.tvNumbers);
            cardView = (CardView) itemView.findViewById(R.id.card);
        }
    }

    private void startDialer(String current_number){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + current_number));
        context.startActivity(intent);
    }

    private void launchDialog(Contact contact) {
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


}
