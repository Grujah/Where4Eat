package com.example.nn.where4eatclient.adapters;

import android.content.Context;
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

import java.io.InputStream;
import java.util.List;

/**
 * Created by nn on 9/22/2015.
 */
public class FoodListAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    List<FoodItem> foodList;

    public FoodListAdapter(Context context, List<FoodItem> foodList) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.foodList = foodList;

    }

    @Override
    public int getCount() {
        return foodList.size();
    }

    @Override
    public Object getItem(int position) {
        return foodList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.foodlist_item, parent, false);


        }
        //Test deo
        TextView ime = (TextView) convertView.findViewById(R.id.foodlist_name);
        TextView venue = (TextView) convertView.findViewById(R.id.foodlist_venue);
        ImageView image = (ImageView) convertView.findViewById(R.id.foodlist_image);

        ime.setText(foodList.get(position).getName());

        venue.setText(foodList.get(position).getVenue());
        try {

            DownloadImageTask d = new DownloadImageTask(image);
            d.execute(foodList.get(position).getThumbsurl());
        } catch (Exception e) {
            e.printStackTrace();
        }


        return convertView;
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap image = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                image = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return image;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}