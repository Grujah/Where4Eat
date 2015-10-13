package com.example.nn.where4eatclient;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.nn.where4eatclient.Utils.Constants;
import com.example.nn.where4eatclient.Utils.DownloadImageTask;


public class FullImageActivity extends Activity {


    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        image = (ImageView)findViewById(R.id.fullscreen_image);

        if (getIntent().hasExtra(Constants.TAG_IMAGEURL)) {

            String imageurl = getIntent().getStringExtra(Constants.TAG_IMAGEURL);
            DownloadImageTask downloader = new DownloadImageTask(image);
            downloader.execute(imageurl);

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_full_image, menu);
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
