package com.teampk.lollol.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by DDuckchul on 2015-01-12.
 */
public class LolloLSharedPref {

    private static final String Pref_name = "MyPref";

    public static LolloLSharedPref mInstance;
    public static Context mContext;

    public LolloLSharedPref(Context context){
        this.mContext = context;
    }

    public static LolloLSharedPref getInstance(Context context){
        if(mInstance == null){
            mInstance = new LolloLSharedPref(context);
        }

        return mInstance;
    }

    public String getPref(String key, String defValue){

        SharedPreferences pref = mContext.getSharedPreferences(Pref_name, Context.MODE_PRIVATE);

        if(pref.contains(key)){
            return pref.getString(key, defValue);
        } else {
            return defValue;
        }
    }

    public int getPref(String key, int defValue){

        SharedPreferences pref = mContext.getSharedPreferences(Pref_name, Context.MODE_PRIVATE);

        if(pref.contains(key)){
            return pref.getInt(key, defValue);
        } else {
            return defValue;
        }
    }

    public boolean getPref(String key, boolean defValue){

        SharedPreferences pref = mContext.getSharedPreferences(Pref_name, Context.MODE_PRIVATE);

        if(pref.contains(key)){
            return pref.getBoolean(key, defValue);
        } else {
            return defValue;
        }
    }

    public void putPref(String key, String value){

        SharedPreferences pref = mContext.getSharedPreferences(Pref_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(key, value);
        editor.commit();
    }

    public void putPref(String key, int value){

        SharedPreferences pref = mContext.getSharedPreferences(Pref_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putInt(key, value);
        editor.commit();
    }

    public void putPref(String key, boolean value){

        SharedPreferences pref = mContext.getSharedPreferences(Pref_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean(key, value);
        editor.commit();
    }

    public void settingSharedPref() {

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Pref_name, Context.MODE_PRIVATE);

        if(!sharedPreferences.contains("downToMyRank")){
            putPref("downToMyRank", 2);
            putPref("upToMyRank", 2);
        }

        if(!sharedPreferences.contains("findPosition")){
            putPref("findPosition", 6);
        }

        if(!sharedPreferences.contains("push")){
            putPref("push", true);
        }

        if(!sharedPreferences.contains("unRankNoti")){
            putPref("unRankNoti", true);
        }
    }

    public void clearSharedPref(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Pref_name, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}
