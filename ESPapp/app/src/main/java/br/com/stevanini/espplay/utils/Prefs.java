package br.com.stevanini.espplay.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Romulo-note on 12/01/2016.
 */
public class Prefs {

    //Identificador de banco de dados desta preferencia
    public static final String PREF_ID = "ESPplay";

    public static void setBoolean(Context context, String chave, boolean on){
        SharedPreferences pref = context.getSharedPreferences(PREF_ID,0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(chave,on);
        editor.commit();
    }

    public static boolean getBoolean(Context context, String chave){
        SharedPreferences pref = context.getSharedPreferences(PREF_ID,0);
        boolean b = pref.getBoolean(chave, true);
        return b;
    }

    public static void setInteger(Context context, String chave,int valor){
        SharedPreferences pref = context.getSharedPreferences(PREF_ID,0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(chave, valor);
        editor.commit();
    }

    public static int getInteger(Context context, String chave){
        SharedPreferences pref = context.getSharedPreferences(PREF_ID,0);
        int i = pref.getInt(chave, 0);
        return i;
    }

    public static void setLong(Context context, String chave,Long valor){
        SharedPreferences pref = context.getSharedPreferences(PREF_ID,0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(chave, valor);
        editor.commit();
    }

    public static long getLong(Context context, String chave){
        SharedPreferences pref = context.getSharedPreferences(PREF_ID,0);
        long i = pref.getLong(chave, 0);
        return i;
    }

    public static void setString(Context context, String chave,String valor){
        SharedPreferences pref = context.getSharedPreferences(PREF_ID,0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(chave, valor);
        editor.commit();
    }

    public static String getString(Context context, String chave){
        SharedPreferences pref = context.getSharedPreferences(PREF_ID,0);
        String s = pref.getString(chave, "");
        return s;
    }

    public static void removePreferences(Context context, String chave){
        SharedPreferences pref = context.getSharedPreferences(PREF_ID,0);
        SharedPreferences.Editor editor = pref.edit().remove(chave);
        editor.commit();
    }


}
