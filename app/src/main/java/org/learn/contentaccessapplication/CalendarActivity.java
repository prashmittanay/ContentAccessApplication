package org.learn.contentaccessapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.widget.ListView;

import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        mListView = (ListView) findViewById(R.id.list_calendar_events);
        LoaderManager.getInstance(this).initLoader(1, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DATE), 0, 0);
        long startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DATE), 23, 59);
        long endMillis = endTime.getTimeInMillis();


        Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(builder, startMillis);
        ContentUris.appendId(builder, endMillis);

        String selection = CalendarContract.Events.DTSTART + " > ? AND " + CalendarContract.Events.DTEND + " < ?";
        String[] selectionArgs = {String.valueOf(startMillis), String.valueOf(endMillis)};

        CursorLoader cursorLoader = new CursorLoader(this, CalendarContract.Events.CONTENT_URI, null,
                selection, selectionArgs, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        cursor.moveToFirst();
        CalendarCursorAdapter calendarCursorAdapter = new CalendarCursorAdapter(this,
                cursor, 0);
        mListView.setAdapter(calendarCursorAdapter);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}