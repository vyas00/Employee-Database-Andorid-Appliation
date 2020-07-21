package com.example.android.fragmentlifecycle;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {

    Context context;
    private String TAG="PrefManager";

    private static final String SORTING_KEY="sorting";
    private static final String COUNT_KEY="count";
    private static final String APP_OPENTIME_KEY ="lastAppOpentime";
     private static final String DISPLAY_TIME_KEY ="lastDisplaytime";
    private static final String ADDED_TIME_KEY ="lastAddedTime";
    public static final String EMPLOYEE_PREF = "EmployeePref" ;


    private static SharedPreferences eSharedPref;
    private static SharedPreferences.Editor editor;

    private  PrefManager(){}

    public static void setContext(Context context)
    {
        if(eSharedPref== null && editor==null)
        {    eSharedPref= context.getSharedPreferences(EMPLOYEE_PREF, Activity.MODE_PRIVATE);
            editor= eSharedPref.edit();
        }

    }


    public static  void setLastSorting(String lastSorting) {
        editor.putString(SORTING_KEY, lastSorting).commit();

    }

    public static void setEmployeesAdded(int employeedCount) {
        editor.putInt(COUNT_KEY, employeedCount).commit();
    }

    public static String getLastSorting() {

        return eSharedPref.getString(SORTING_KEY, "");
    }

    public static int getLastEmployeesCount() {

        return eSharedPref.getInt(COUNT_KEY,0);
    }

    public static void setLastAppOpenTime(String time) {
        editor.putString(APP_OPENTIME_KEY, time).commit();
    }

    public static String getLastAppOpenTime() {

        return eSharedPref.getString(APP_OPENTIME_KEY, "");
    }

    public static void setLastAddedTime(String time) {
        editor.putString(ADDED_TIME_KEY, time).commit();

    }

    public static String getLastAddedTime() {

        return eSharedPref.getString(ADDED_TIME_KEY, "");
    }

    public static void setLastDisplayTime(String time) {
        editor.putString(DISPLAY_TIME_KEY, time).commit();
    }

    public static String getLastDisplayTime() {
        return eSharedPref.getString(DISPLAY_TIME_KEY, "");
    }

}
