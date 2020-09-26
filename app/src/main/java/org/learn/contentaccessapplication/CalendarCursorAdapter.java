package org.learn.contentaccessapplication;

import android.content.Context;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class CalendarCursorAdapter extends CursorAdapter {
    public CalendarCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context)
                .inflate(R.layout.layout_calendar_events_list, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView calendarEventTextView = view.findViewById(R.id.text_calendar_event);

        String eventName = cursor.getString(cursor.getColumnIndex(
                CalendarContract.Events.TITLE));

        calendarEventTextView.setText(eventName);
    }
}
