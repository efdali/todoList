package com.efdalincesu.todolist.Ui.Fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.efdalincesu.todolist.R;

public class PreferencesFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.setting_preferences);



    }
}
