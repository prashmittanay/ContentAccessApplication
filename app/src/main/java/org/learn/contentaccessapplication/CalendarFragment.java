package org.learn.contentaccessapplication;

import android.Manifest;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import java.util.Calendar;

public class CalendarFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private ListView mListView;
    private TextView mTextView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mTextView = (TextView) view.findViewById(R.id.emptyElementCalendar);
        mListView = (ListView) view.findViewById(R.id.list_calendar_events);
        mListView.setEmptyView(mTextView);

        super.onViewCreated(view, savedInstanceState);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            getCalendarDetails();
        } else {
            mTextView.setText("Calendar Permissions Denied!");
        }
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

        CursorLoader cursorLoader = new CursorLoader(getContext(), CalendarContract.Events.CONTENT_URI, null,
                selection, selectionArgs, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        cursor.moveToFirst();
        CalendarCursorAdapter calendarCursorAdapter = new CalendarCursorAdapter(getContext(),
                cursor, 0);
        mListView.setAdapter(calendarCursorAdapter);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        loader = null;
    }

    public void getCalendarDetails() {
        LoaderManager.getInstance(this).initLoader(1, null, this);
    }
}
