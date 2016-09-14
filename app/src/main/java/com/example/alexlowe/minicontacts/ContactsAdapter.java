package com.example.alexlowe.minicontacts;

import android.content.Context;
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
    private Context context;

    public ContactsAdapter(Context context, ArrayList<Contact> contacts){
        this.contactList = contacts;
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
        Contact contact = contactList.get(position);

        TextView tvName = holder.tvName;
        tvName.setText(contact.getName());

        HashMap<String, String> contactNumbers = contact.getNumbers();

        StringBuilder phoneListString = new StringBuilder();
        String newline = "";
        for (Map.Entry<String, String> entry : contactNumbers.entrySet())
        {
            phoneListString.append(newline)
                    .append(entry.getValue())
                    .append(": ")
                    .append(entry.getKey());
            newline = "\n";
        }

        TextView tvNumbers = holder.tvNumbers;
        tvNumbers.setText(phoneListString);
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvName;
        public TextView tvNumbers;

        public ViewHolder(View itemView){
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvNumbers = (TextView) itemView.findViewById(R.id.tvNumbers);
        }
    }
}
