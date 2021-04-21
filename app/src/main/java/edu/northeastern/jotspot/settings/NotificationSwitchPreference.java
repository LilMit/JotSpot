package edu.northeastern.jotspot.settings;

import android.content.Context;
import android.util.AttributeSet;

import androidx.preference.SwitchPreferenceCompat;

import edu.northeastern.jotspot.NotificationService;

public class NotificationSwitchPreference extends SwitchPreferenceCompat {

    public NotificationSwitchPreference(Context context, AttributeSet attrs,
            int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public NotificationSwitchPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NotificationSwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NotificationSwitchPreference(Context context) {
        super(context);
    }

    @Override
    protected void onClick(){
        if(isChecked()){
            NotificationService.cancelReminder(getContext(), NotificationService.AlarmReceiver.class);
        }
        super.onClick();
    }
}
