package com.hurricanedevelopmentstudios.android.jobsender;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by gaby on 9/11/15.
 * A convenient interface for reading and writing to and from shared preferences.
 */

public class Preferences {

    // For Settings
    private static final String PREF_NAME = "preferences";
    private static final String PREF_RECIPIENTS = "recipients";
    private static final String PREF_SUBJECT = "default_subject_line";

    // For saving current job data
    private static final String PREF_CURCLOCKINDATE = "current_clock_in_date";
    private static final String PREF_CURCLOCKOUTDATE = "current_clock_out_date";
    private static final String PREF_CURINLOC = "current_in_location";
    private static final String PREF_CUROUTLOC = "current_out_location";
    private static final String PREF_CLIENT = "current_client";
    private static final String PREF_DESC = "current_desc";

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

    public static long getStoredClockInDate (Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getLong(PREF_CURCLOCKINDATE, 0);
    }

    public static void setStoredClockInDate (Context context, long clockInDate) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putLong(PREF_CURCLOCKINDATE, clockInDate)
                .apply();
    }

    public static long getStoredClockOutDate (Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getLong(PREF_CURCLOCKOUTDATE, 0);
    }

    public static void setStoredClockOutDate (Context context, long clockOutDate) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putLong(PREF_CURCLOCKOUTDATE, clockOutDate)
                .apply();
    }

    public static String getStoredInLocation (Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_CURINLOC, null);
    }

    public static void setStoredInLocation (Context context, String inLoc) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_CURINLOC, inLoc)
                .apply();
    }

    public static String getStoredOutLocation (Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_CUROUTLOC, null);
    }

    public static void setStoredOutLocation (Context context, String outLoc) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_CUROUTLOC, outLoc)
                .apply();
    }

    public static String getStoredClient (Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_CLIENT, null);
    }

    public static void setStoredClient (Context context, String client) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_CLIENT, client)
                .apply();
    }

    public static String getStoredDescription (Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_DESC, null);
    }

    public static void setStoredDescription (Context context, String desc) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_DESC, desc)
                .apply();
    }





}
