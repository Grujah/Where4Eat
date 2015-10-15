package com.example.nn.where4eatclient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import com.example.nn.where4eatclient.utils.Constants;
import com.example.nn.where4eatclient.utils.SessionManager;
import com.example.nn.where4eatclient.utils.SimpleLocationListener;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import com.facebook.FacebookSdk;
import com.google.android.gms.maps.model.LatLng;

import static com.example.nn.where4eatclient.utils.Convertor.convertInputStreamToString;


public class SubmitItemActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{


    private File imageFile;
    private Uri imageUri;
    private static final int TAKE_PICTURE_REQUEST = 1010;
    private static final int PLACE_PICKER_REQUEST = 1020;

    private Bitmap bitmap;
    private ImageView imageContainer;
    private RatingBar ratingContainer;
    private TextView nameContainer;
    private TextView venueContainer;
    private TextView descriptionContainer;
    private TextView titleContainer;
    private String imagePath;
    private InputStream in;

    private SimpleLocationListener locListener;
    private GoogleApiClient mGoogleApiClient;
    private boolean connectedToGoogle = false;
    private String placeId;
    private ShareButton shareButton;
    private ShareContent content;
    private String ba1;
    private SessionManager sm;
    private boolean gotPlace = false;
    Context context;
    private LatLng placeLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_submit_item);
        FacebookSdk.sdkInitialize(getApplicationContext());

        sm = new SessionManager(this);
        sm.checkIfLoggedIn();
        nameContainer = (TextView) findViewById(R.id.food_prompt);
        venueContainer = (TextView) findViewById(R.id.venue_prompt);
        descriptionContainer = (TextView) findViewById(R.id.description_prompt);
        ratingContainer = (RatingBar) findViewById(R.id.food_rating);
        imageContainer = (ImageView) findViewById(R.id.preview_image);
        context = this;
   //     shareButton = (ShareButton) findViewById(R.id.fb_share_button);
     //   shareButton.setShareContent(content);

        locListener = new SimpleLocationListener(this);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        connectedToGoogle = true;
    }

    @Override
    public void onConnectionSuspended(int i) {
        connectedToGoogle = false;

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_submit_item, menu);
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


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void takeAPicture(View view) {

       /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "myfood.png");
        imageUri = Uri.fromFile(imageFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PICTURE_REQUEST);*/

            final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
            AlertDialog.Builder builder = new AlertDialog.Builder(SubmitItemActivity.this);
            builder.setTitle("Add Photo!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (items[item].equals("Take Photo")) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "myfood.png");
                        imageUri = Uri.fromFile(imageFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, Constants.REQUEST_CAMERA_CODE);
                    } else if (items[item].equals("Choose from Library")) {
                        Intent intent = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(
                                Intent.createChooser(intent, "Select File"),
                                Constants.SELECT_FILE_CODE);
                    } else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        }


    public void submitItem(View view){

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bao);
        byte[] ba = bao.toByteArray();
        ba1 = Base64.encodeToString(ba,Base64.DEFAULT);

        uploadToServer uploader = new uploadToServer();
        uploader.execute();

     //   AsyncSubmit s = new AsyncSubmit();
      //  s.execute();
    }

    public void pickAPlace(View view) throws GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException {
        if (connectedToGoogle = true) {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        }
    }

     class uploadToServer extends AsyncTask<Void, Void, Integer> {

        ProgressDialog pDialog = new ProgressDialog(SubmitItemActivity.this);
        JSONObject jasonAnswer;
        private ProgressDialog pd = new ProgressDialog(SubmitItemActivity.this);

        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Wait image uploading!");
            pd.show();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            int returnId = -1;
            ArrayList<NameValuePair> requestData = new ArrayList<NameValuePair>();
            String imagename = "default";

            if (bitmap != null) {
                imagename = sm.getUsername() + System.currentTimeMillis();
                requestData.add(new BasicNameValuePair("base64", ba1));
                requestData.add(new BasicNameValuePair("ImageName", imagename));
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(Constants.SERVER_URL + Constants.IMAGE_URL);
                    httppost.setEntity(new UrlEncodedFormEntity(requestData));
                    HttpResponse response = httpclient.execute(httppost);
                    String st = EntityUtils.toString(response.getEntity());
                    Log.v("log_tag", "In the try Loop" + st);

                } catch (Exception e) {
                    Log.v("log_tag", "Error in http connection " + e.toString());
                }

            }

            requestData = new ArrayList<NameValuePair>();
            requestData.add(new BasicNameValuePair("name", nameContainer.getText().toString()));
            requestData.add(new BasicNameValuePair("venue", venueContainer.getText().toString()));
            requestData.add(new BasicNameValuePair("desc", descriptionContainer.getText().toString()));
            requestData.add(new BasicNameValuePair("rating", String.valueOf(ratingContainer.getRating())));

            if (gotPlace) {
                requestData.add(new BasicNameValuePair("lat", String.valueOf(placeLL.latitude)));
                requestData.add(new BasicNameValuePair("long", String.valueOf(placeLL.longitude)));
            } else {
                requestData.add(new BasicNameValuePair("lat", String.valueOf(locListener.getLocation().getLatitude())));
                requestData.add(new BasicNameValuePair("long", String.valueOf(locListener.getLocation().getLongitude())));
            }

            requestData.add(new BasicNameValuePair("userID", String.valueOf(sm.getId())));
            requestData.add(new BasicNameValuePair("image",imagename));



            try{
            UrlEncodedFormEntity jEntity = new UrlEncodedFormEntity(requestData);
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(Constants.SERVER_URL + Constants.SUBMIT_URL);
            httpPost.setEntity(jEntity);


            // Send stuff
            HttpResponse httpResponse = httpClient.execute(httpPost);
            //    HttpResponse httpResponse2 = httpClient.execute(imagePost);

            // receive infobox

            InputStream inputStream = httpResponse.getEntity().getContent();
            String answerString = convertInputStreamToString(inputStream);
            returnId = Integer.valueOf(answerString);
        } catch (Exception e) {
            Log.v("log_tag", "Error in http connection " + e.toString());
               return -1;
        }


            return returnId;

        }

        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            pd.hide();
            pd.dismiss();
            if (result > 0) {

                Intent intent = new Intent(context, ShowFoodActivity.class);
                intent.putExtra(Constants.TAG_FOOD_ID,result);
                context.startActivity(intent);
            }

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.REQUEST_CAMERA_CODE) {


                getContentResolver().notifyChange(imageUri, null);
                ContentResolver cr = getContentResolver();

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(cr, imageUri);
                    SharePhoto sp = new SharePhoto.Builder()
                            .setBitmap(bitmap)
                            .build();
                    content = new SharePhotoContent.Builder()
                            .addPhoto(sp)
                            .build();
                    imagePath = imageUri.getPath();
                    imageContainer.setImageBitmap(bitmap);

                } catch (Exception e) {
                    Log.e("setImage:", "failure");
                }
            }
           /* if (requestCode == Constants.REQUEST_CAMERA_CODE) {
                Bitmap thumbnail = (Bitmap) intent.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                imageContainer.setImageBitmap(thumbnail);
            }*/
            if (requestCode == Constants.SELECT_FILE_CODE) {
                Uri selectedImageUri = intent.getData();
                String[] projection = { MediaStore.MediaColumns.DATA };
                Cursor cursor = managedQuery(selectedImageUri, projection, null, null,
                        null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String selectedImagePath = cursor.getString(column_index);
           //     BitmapFactory.Options options = new BitmapFactory.Options();
            //    options.inJustDecodeBounds = true;
                bitmap= BitmapFactory.decodeFile(selectedImagePath);
            /*    final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;*/
             //   bm = BitmapFactory.decodeFile(selectedImagePath, options);
                imageContainer.setImageBitmap(bitmap);
            }

            if (requestCode == PLACE_PICKER_REQUEST) {
                Place place = PlacePicker.getPlace(intent, this);
                if (place != null) {
                    venueContainer.setText(place.getName());
                    placeId = place.getId();
                    gotPlace = true;
                    placeLL = place.getLatLng();
                }
            }
        }

    }

}
