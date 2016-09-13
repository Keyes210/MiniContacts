package com.example.alexlowe.minicontacts;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private SimpleCursorAdapter mCursorAdapter;

    public static final int CONTACT_LOADER_ID = 78; // From docs: A unique identifier for this loader. Can be whatever you want.

    // Defines the asynchronous callback for the contacts data loader
    private LoaderManager.LoaderCallbacks<Cursor> mContactsLoader =


            new LoaderManager.LoaderCallbacks<Cursor>() {

                // Create and return the actual cursor loader for the contacts data
                @Override
                public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                    // Define the columns to retrieve
                    String[] projectionFields = new String[]{ContactsContract.Contacts._ID,
                            ContactsContract.Contacts.DISPLAY_NAME,
                            ContactsContract.Contacts.PHOTO_URI};

                    //create the loader
                    CursorLoader cursorLoader = new CursorLoader(
                            MainActivity.this, //context
                            ContactsContract.Contacts.CONTENT_URI, //uri
                            projectionFields, //projection fields
                            null, //selection criteria
                            null, //selection args
                            null //sort order
                            );

                return cursorLoader;
                }

                // When the system finishes retrieving the Cursor through the CursorLoader,
                // a call to the onLoadFinished() method takes place.
                @Override
                public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                    // The swapCursor() method assigns the new Cursor to the adapter
                    mCursorAdapter.swapCursor(data);
                }


                // This method is triggered when the loader is being reset
                // and the loader data is no longer available. Called if the data
                // in the provider changes and the Cursor becomes stale.
                @Override
                public void onLoaderReset(Loader<Cursor> loader) {
                    // Clear the Cursor we were using with another call to the swapCursor()
                    mCursorAdapter.swapCursor(null);
                }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupCursorAdapter();
        // Initialize the loader with a special ID and the defined callbacks from above
        getSupportLoaderManager().initLoader(CONTACT_LOADER_ID,
                new Bundle(), mContactsLoader);
        ListView lvContacts = (ListView) findViewById(R.id.lvContacts);
        lvContacts.setAdapter(mCursorAdapter);
    }

    private void setupCursorAdapter() {
        String[] uiBindFrom = {ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_URI};
        int[] uiBindTo = {R.id.tvName, R.id.ivImage};

        mCursorAdapter = new SimpleCursorAdapter(this, R.layout.item_contact, null, uiBindFrom,
                uiBindTo, 0);
    }




}
