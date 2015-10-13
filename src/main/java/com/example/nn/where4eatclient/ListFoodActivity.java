package com.example.nn.where4eatclient;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.nn.where4eatclient.Utils.AsyncGetFood;
import com.example.nn.where4eatclient.adapters.FoodListAdapterJSON;

import org.json.JSONArray;


public class ListFoodActivity extends Activity {


    ListView lview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_food);
        lview = (ListView) findViewById(R.id.foodlist);
        AsyncFoodList al = new AsyncFoodList(this);
        if (getIntent().hasExtra("JSON")){
            al.execute(getIntent().getStringExtra("JSON"));

        } else {
            al.catchEverything();
        }
        }


        private class AsyncFoodList extends AsyncGetFood {

            public AsyncFoodList(Context context) {
                super(context);
            }

            @Override
            public void onPostExecute (JSONArray s){
                if (s != null) {
                    lview.setAdapter(new FoodListAdapterJSON(ListFoodActivity.this, s));
                }
            }

        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_food, menu);
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
}
