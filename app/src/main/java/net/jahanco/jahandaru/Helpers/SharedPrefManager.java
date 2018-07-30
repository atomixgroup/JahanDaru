package net.jahanco.jahandaru.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

import net.jahanco.jahandaru.App;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by GunAy-PC17 on 8/30/2016.
 */
public class SharedPrefManager {

    private SharedPreferences sharedPreferences;
    public SharedPrefManager(String sharedPrefName) {
        if(sharedPrefName==null){
            sharedPrefName= "jd";
        }
        sharedPreferences= App.context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
    }
    public void savePreferences(HashMap<String,String> params){
        if(params!=null){
            SharedPreferences.Editor editor=sharedPreferences.edit();
            for ( Map.Entry<String, String> entry : params.entrySet()) {
                editor.putString(entry.getKey(),entry.getValue());
            }
            editor.apply();
        }
    }
    public void savePreference(String key, String value){
        if(key!=null && value!=null){
            SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString(key,value);
            editor.apply();
        }
    }
    public HashMap<String,String> getPreferences(ArrayList<String> params){
            HashMap<String, String> temp = new HashMap<>();
            for (String entry : params) {
                temp.put(entry, sharedPreferences.getString(entry, null));
            }
            return temp;
    }
    public String getPreference(String param){
        return sharedPreferences.getString(param,"-1");
    }
    public boolean isSetPereference(){
        if((sharedPreferences.getString("initialized","false")).equals("false")){
            return false;
        }
        else {
            return true;
        }
    }
}
