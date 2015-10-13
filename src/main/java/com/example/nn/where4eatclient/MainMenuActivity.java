package com.example.nn.where4eatclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.nn.where4eatclient.Utils.SessionManager;


public class MainMenuActivity extends Activity {

    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_menu);
        SessionManager sm = new SessionManager(this);
        sm.checkIfLoggedIn();
        title = (TextView) findViewById(R.id.main_menu_text);
        title.setText("Welcome "+sm.getUsername()+"! What will we to today?");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void seeFood(View view){
        //make intent
        Intent intent = new Intent(this, ListFoodActivity.class);
        this.startActivity(intent);
    }

    public void postFood (View view){
        //make intent
        Intent intent = new Intent(this, SubmitItemActivity.class);
        this.startActivity(intent);
    }

    public void goToMap (View view){
        //make intent
        Intent intent = new Intent(this, FoodMapActivity.class);
        this.startActivity(intent);
    }

    public void goToSearch(View view){
        Intent intent = new Intent(this, SearchAcitivity.class);
        this.startActivity(intent);
    }
}
