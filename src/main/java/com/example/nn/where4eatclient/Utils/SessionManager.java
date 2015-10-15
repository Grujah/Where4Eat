package com.example.nn.where4eatclient.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.nn.where4eatclient.LoginActivity;

/**
 * Created by nn on 9/29/2015.
 */
public class SessionManager {


    private SharedPreferences sPref;
    private SharedPreferences.Editor editor;
    private Context context;

    private static final int PRIVATE_MODE = 0;
    public static final String PREF_NAME = "W2EPref";

    private static final String IS_LOGGED_IN = "loggedin";
    private static final String NAME = "username";
    private static final String ID = "id";


    public SessionManager (Context context){
        this.context = context;
        sPref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sPref.edit();
    }

    public boolean logIn (int id, String username){

        editor.putInt(ID, id);
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(NAME, username);
        editor.commit();

        return true;
    }

    public void logOut (){

        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        context.startActivity(i);
    }

    public void checkIfLoggedIn(){

        if (isLoggedIn()==false) {

            Intent intent = new Intent(context, LoginActivity.class );
            context.startActivity(intent);
        }
    }
    public boolean isLoggedIn(){

        return sPref.getBoolean(IS_LOGGED_IN,false);

    }

    public String getUsername(){

        return sPref.getString(NAME, null);
    }

    public int getId(){
        return sPref.getInt(ID,0);
    }

}
