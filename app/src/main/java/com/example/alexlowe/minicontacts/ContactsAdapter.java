package com.example.alexlowe.minicontacts;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by alexlowe on 9/14/16.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder>{
    private ArrayList<Contact> contactList;
    private ArrayList<Contact> contactListCopy;
    private Context context;

    //adapter is logic heavy
    public ContactsAdapter(Context context, ArrayList<Contact> contacts){
        //pointing to singleton
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
            //bind method
        ContactsPresenter contactsPresenter = new ContactsPresenter();
        contactsPresenter.bindAdapterViews(holder, contact);
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

}
