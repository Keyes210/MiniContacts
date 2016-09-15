package com.example.alexlowe.minicontacts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by alexlowe on 9/14/16.
 */
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> implements Filterable{
    private ArrayList<Contact> allContactList;
    private ArrayList<Contact> displayedContactList;
    private Context context;

    public ContactsAdapter(Context context, ArrayList<Contact> contacts){
        this.allContactList = contacts;
        this.displayedContactList = contacts;
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
        Contact contact = allContactList.get(position);

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
        return displayedContactList.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            /*the generic type Contact is only used for compile-time type
                checking, it is not available at runtime, so have to suppress warnings*/
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                displayedContactList = (ArrayList<Contact>) results.values; // has the filtered values
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<Contact> filteredContactList = new ArrayList<Contact>();

                if (allContactList == null) {
                    allContactList = new ArrayList<Contact>(displayedContactList); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns filteredContactList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {
                    // set the Original result to return
                    results.count = allContactList.size();
                    results.values = allContactList;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < allContactList.size(); i++) {
                        Contact currentContact = allContactList.get(i);
                        String data = currentContact.getName();
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            filteredContactList.add(new Contact(currentContact.getName(),
                                    currentContact.getNumbers(), currentContact.getPhotoURI()));
                        }
                    }
                    // set the Filtered result to return
                    results.count = filteredContactList.size();
                    results.values = filteredContactList;
                }
                return results;
            }
        };
        return filter;
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
