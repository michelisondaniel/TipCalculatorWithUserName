package com.murach.tipcalculator;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {

    // have to changed the method handlers from protected to public
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // load the preferences from the xml resources
        addPreferencesFromResource(R.xml.preferences); // had to change this from android given code

    }
}
