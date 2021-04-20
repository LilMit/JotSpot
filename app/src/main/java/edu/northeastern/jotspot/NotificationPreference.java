package edu.northeastern.jotspot;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

import androidx.preference.DialogPreference;
import androidx.preference.Preference;

import java.sql.Date;
import java.text.DateFormat;
import java.util.Calendar;

public class NotificationPreference extends DialogPreference implements Preference.OnPreferenceChangeListener {
    private TimePicker picker = null;
    private static final long DEFAULT_VALUE = 0;

    public NotificationPreference(final Context context, final AttributeSet attrs) {
        super(context, attrs, 0);
        this.setOnPreferenceChangeListener(this);
    }

    protected void setTime(final long time) {
        persistLong(time);
        notifyDependencyChange(shouldDisableDependents());
        notifyChanged();
    }

    protected void updateSummary(final long time) {
        final DateFormat dateFormat = android.text.format.DateFormat.getTimeFormat(getContext());
        final Date date = new Date(time);
        setSummary(dateFormat.format(date.getTime()));
    }

    protected View onCreateDialogView() {
        picker = new TimePicker(getContext());
        picker.setIs24HourView(android.text.format.DateFormat.is24HourFormat(getContext()));
        return picker;
    }

    protected Calendar getPersistedTime(){
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(getPersistedLong(DEFAULT_VALUE));

        return c;
    }

    @Override
    protected void onDialogClosed(final boolean positiveResult){
        super.onDialogClosed(positiveResult);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ((NotificationPreference) preference).updateSummary((Long) newValue);
        return true;
    }
}
