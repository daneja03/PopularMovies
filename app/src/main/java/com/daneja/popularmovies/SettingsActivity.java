package com.daneja.popularmovies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class MoviesPreferencesFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            Preference sortPreference = findPreference(getString(R.string.value_field_sort_preference));
            bindPreferenceSummaryToValue(sortPreference);
        }

        /**
         * Setting the summary text to the current preference selected during onCreate
         * @param preference
         */
        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceValue = preferences.getString(preference.getKey(), getString(R.string.value_field_popularity));
            onPreferenceChange(preference, preferenceValue);
        }

        /**
         * Update the Preference summary on when preference change
         * @param preference
         * @param newValue
         * @return
         */
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();
            if (stringValue == getString(R.string.value_field_user_rating)) {
                preference.setSummary(getString(R.string.display_field_userrating));
            } else {
                preference.setSummary(getString(R.string.display_field_popularity));
            }
            return true;
        }
    }

}
