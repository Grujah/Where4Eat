package com.example.nn.where4eatclient.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nn.where4eatclient.R;
import com.example.nn.where4eatclient.ShowFoodActivity;
import com.example.nn.where4eatclient.Utils.Constants;
import com.example.nn.where4eatclient.Utils.DownloadImageTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

/**
 * Created by nn on 9/22/2015.
 */
public class FoodListAdapterJSON extends BaseAdapter{

    private JSONArray jarray;
    private Context context;
    private LayoutInflater inflater;

    public FoodListAdapterJSON(Context context, JSONArray jarray){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.jarray = jarray;
    }
    @Override
    public int getCount() {
        return jarray.length();
    }

    @Override
    public Object getItem(int position) {
        JSONObject returnable = null;
        try {
            returnable = jarray.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnable;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.foodlist_item, parent, false);


        }
        //Test deo
        TextView ime = (TextView) convertView.findViewById(R.id.foodlist_name);
        TextView venue = (TextView) convertView.findViewById(R.id.foodlist_venue);
        ImageView image = (ImageView) convertView.findViewById(R.id.foodlist_image);


        try {
            ime.setText(jarray.getJSONObject(position).getString("name"));
            venue.setText(jarray.getJSONObject(position).getString("venue"));
            final int id = jarray.getJSONObject(position).getInt("itemsID");

            image.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v){
                    Intent intent = new Intent(context, ShowFoodActivity.class);
                    intent.putExtra(Constants.TAG_FOOD_ID, id);
                    context.startActivity(intent);

                }
            }

            );

            DownloadImageTask d = new DownloadImageTask(image);

       //     image.setImageResource(R.drawable.nofood);

            d.execute("http://192.168.1.3:8081/where2eat/images/"+jarray.getJSONObject(position).getString("image")+"_t.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }


        return convertView;
    }


}
