package com.hurricanedevelopmentstudios.android.jobsender;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by gaby on 9/11/15.
 * A convenient interface for reading and writing to and from shared preferences.
 */
public class Preferences {

    private static final String PREF_NAME = "preferences";
    private static final String PREF_RECIPIENTS = "recipients";
    private static final String PREF_SUBJECT = "default_subject_line";

    public static String getStoredUserName (Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_NAME,  null);
    }

    public static void setStoredUserName (Context context, String name) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_NAME, name)
                .apply();
    }

    public static String getStoredRecipients (Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_RECIPIENTS,  null);
    }

    public static void setStoredRecipients (Context context, String emails) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_RECIPIENTS, emails)
                .apply();
    }

    public static String getStoredSubjectLine (Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_SUBJECT,  null);
    }

    public static void setStoredSubjectLine (Context context, String subject) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_SUBJECT, subject)
                .apply();
    }



}
