package com.example.nn.where4eatclient.Utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nn on 10/13/2015.
 */
public class ContentMaker {
    private final JSONObject jason;

    ContentMaker (JSONObject jason){
        this.jason = jason;

    }

    String createContent (){
        String s = null;
        try {
            s += "<table class=\"tg\"><tr> <th rowspan=\"2\"> <img src=\"";
            s += jason.getString("image");
            s += "\"></img></th><th><p>";
            s+= jason.getString("name");
            s += "</br>";
            s += jason.getString("venue");
            s += "</p></th></tr><tr><td>";
            s += jason.getString("description");
            s += "</td></tr></table>";

        } catch (JSONException e){
            Log.e("Content", e.getLocalizedMessage());
        }


        return s;
    }
}
