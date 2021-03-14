package com.jaekapps.expensetracker.view.fragments;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.jaekapps.expensetracker.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    public SettingsFragment() {}

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        setPreferencesFromResource(R.xml.settings_preferences, rootKey);
    }
}
