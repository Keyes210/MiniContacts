package com.example.alexlowe.minicontacts;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexlowe on 9/14/16.
 */
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> implements ContactsInterface {
    private List<Contact> contactList;
    private List<Contact> contactListCopy;
    private Context context;

    private ContactsPresenter presenter;

    public ContactsAdapter(Context context){
        this.context = context;
        presenter = new ContactsPresenter(this);
        this.contactList = ContactsUtility.getContacts(context);
        this.contactListCopy = new ArrayList<>();
        for(Contact contact : contactList){
            contactListCopy.add(contact);
        }
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
        bindViews(holder, contact);
    }

    private void bindViews(ViewHolder holder, final Contact contact){
        TextView tvName = holder.tvName;
        tvName.setText(contact.getName());

        TextView tvNumbers = holder.tvNumbers;
        String phoneListString = presenter.getPhoneListString(contact);
        tvNumbers.setText(phoneListString);

        CardView cardView = holder.cardView;

        View.OnClickListener cardClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onCardClicked(contact);
            }
        };

        cardView.setOnClickListener(cardClickListener);
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

    @Override
    public void launchDialerDialog(final CharSequence[] numbers) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose a number:");
        builder.setItems(numbers, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.numberChosen(numbers[which].toString());
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    @Override
    public void startDialer(String currentNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + currentNumber));
        context.startActivity(intent);
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
