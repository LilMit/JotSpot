package edu.northeastern.jotspot.settings;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.preference.DialogPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceDialogFragmentCompat;
import androidx.preference.PreferenceFragmentCompat;

import edu.northeastern.jotspot.NotificationService;
import edu.northeastern.jotspot.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Most dialog preference code from https://medium.com/@JakobUlbrich/building-a-settings-screen-for-android-part-3-ae9793fd31ec
     */
    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.app_preferences);
        }

        @Override
        public void onDisplayPreferenceDialog(Preference preference) {
            // Try if the preference is one of our custom Preferences
            DialogFragment dialogFragment = null;
            if (preference instanceof TimePreference) {
                // Create a new instance of TimePreferenceDialogFragment with the key of the related
                // Preference
                Log.e("Settings", "TimePreference detected");
                dialogFragment = TimePreferenceDialogFragmentCompat
                        .newInstance(preference.getKey());
            }

            // If it was one of our custom Preferences, show its dialog
            if (dialogFragment != null) {
                Log.e("Settings", "trying to show dialog");
                dialogFragment.setTargetFragment(this, 0);
                dialogFragment.show(this.getFragmentManager(),
                        "android.support.v7.preference" +
                                ".PreferenceFragment.DIALOG");
            }
            // Could not be handled here. Try with the super method.
            else {
                super.onDisplayPreferenceDialog(preference);
            }
        }
    }


    public static class TimePreferenceDialogFragmentCompat extends PreferenceDialogFragmentCompat {
        TimePicker mTimePicker;
        public static TimePreferenceDialogFragmentCompat newInstance(
                String key) {
            final TimePreferenceDialogFragmentCompat
                    fragment = new TimePreferenceDialogFragmentCompat();
            final Bundle b = new Bundle(1);
            b.putString(ARG_KEY, key);
            fragment.setArguments(b);

            return fragment;
        }

        @Override
        protected void onBindDialogView(View view) {
            super.onBindDialogView(view);

            mTimePicker = (TimePicker) view.findViewById(R.id.edit);

            // Exception when there is no TimePicker
            if (mTimePicker == null) {
                throw new IllegalStateException("Dialog view must contain" +
                        " a TimePicker with id 'edit'");
            }

            // Get the time from the related Preference
            Integer minutesAfterMidnight = null;
            DialogPreference preference = getPreference();
            if (preference instanceof TimePreference) {
                minutesAfterMidnight =
                        ((TimePreference) preference).getTime();
            }

            // Set the time to the TimePicker
            if (minutesAfterMidnight != null) {
                int hours = minutesAfterMidnight / 60;
                int minutes = minutesAfterMidnight % 60;
                boolean is24hour = DateFormat.is24HourFormat(getContext());

                mTimePicker.setIs24HourView(is24hour);
                mTimePicker.setCurrentHour(hours);
                mTimePicker.setCurrentMinute(minutes);
            }
        }
        @Override
        public void onDialogClosed(boolean positiveResult) {
            if (positiveResult) {
                // generate value to save
                int hours = mTimePicker.getCurrentHour();
                int minutes = mTimePicker.getCurrentMinute();
                int minutesAfterMidnight = (hours * 60) + minutes;

                // Get the related Preference and save the value
                DialogPreference preference = getPreference();
                if (preference instanceof TimePreference) {
                    TimePreference timePreference =
                            ((TimePreference) preference);
                    // This allows the client to ignore the user value.
                    if (timePreference.callChangeListener(
                            minutesAfterMidnight)) {
                        // Save the value
                        timePreference.setTime(minutesAfterMidnight);
                        NotificationService.scheduleNotification(getContext(),hours,minutes);
                    }
                }
            }
        }
    }

}