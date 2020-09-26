package org.learn.contentaccessapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.list_main_contacts);

        LoaderManager.getInstance(this).initLoader(1, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Uri contentUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        CursorLoader cursorLoader = new CursorLoader(this, contentUri, null,
                null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        cursor.moveToFirst();
        ContactsCursorAdapter contactsCursorAdapter = new ContactsCursorAdapter(this,
                cursor, 0);
        mListView.setAdapter(contactsCursorAdapter);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}