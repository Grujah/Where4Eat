package com.example.nn.where4eatclient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nn.where4eatclient.Utils.Constants;
import com.example.nn.where4eatclient.Utils.SessionManager;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.example.nn.where4eatclient.Utils.Convertor.convertInputStreamToString;



public class LoginActivity extends Activity {


    TextView usernameContainer, passwordContainer;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        SessionManager sm = new SessionManager(this);
        setContentView(R.layout.activity_login);
        usernameContainer = (TextView) findViewById(R.id.username_prompt);
        passwordContainer = (TextView) findViewById(R.id.password_prompt);
        context = this;
        if (sm.isLoggedIn())
        {
            Intent intent = new Intent(context, MainMenuActivity.class );
            context.startActivity(intent);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    public void beginLogin (View view){

        AsyncLogin toRun = new AsyncLogin();
        toRun.execute();


    }

    private class AsyncLogin extends AsyncTask<String, String, JSONObject> {



        ProgressDialog pDialog = new ProgressDialog(LoginActivity.this);
        List<NameValuePair> requestData;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // progress dialog make
            pDialog.setMessage("Conecting to Database");
            pDialog.show();


        }

        @Override
        protected void onPostExecute(JSONObject jasonAnswer) {
            super.onPostExecute(jasonAnswer);

            pDialog.hide();

            try {
                if (jasonAnswer != null && jasonAnswer.getInt("success") == 1){
                    Toast t =  Toast.makeText(getApplicationContext(),"Login Successful", Toast.LENGTH_SHORT);
                    t.show();


                    //save login info
                    SessionManager sm = new SessionManager(context);
                    sm.logIn(Integer.valueOf(jasonAnswer.getString("id")),jasonAnswer.getString("name"));


                    String UserTest = sm.getUsername();


                    //make intent
                    Intent intent = new Intent(context, MainMenuActivity.class );
                    context.startActivity(intent);


                }
                else {
                    Toast t = Toast.makeText(getApplicationContext(),jasonAnswer.getString("message"),Toast.LENGTH_LONG);
                    t.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject jasonAnswer = null;

            try {

                // build requestData

                requestData = new ArrayList<NameValuePair>();
                requestData.add(new BasicNameValuePair("username", usernameContainer.getText().toString()));
                requestData.add(new BasicNameValuePair("password", passwordContainer.getText().toString()));

              //  JSONObject requestData = new JSONObject();
              //  requestData.accumulate("username", usernameContainer.getText().toString());
              //  requestData.accumulate("password", passwordContainer.getText().toString());




                UrlEncodedFormEntity jEntity = new UrlEncodedFormEntity(requestData);
              //  StringEntity jEntity = new StringEntity(requestData.toString());

                //bild Http Client + stuff

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(Constants.SERVER_URL + Constants.LOGIN_URL);

                httpPost.setEntity(jEntity);


                // put data in http client



                // Send stuff
                HttpResponse httpResponse = httpClient.execute(httpPost);

                // receive answer

                InputStream inputStream = httpResponse.getEntity().getContent();
                String answerString = convertInputStreamToString(inputStream);
                jasonAnswer = new JSONObject(answerString);





            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
                Toast t =  Toast.makeText(getApplicationContext(),e.getLocalizedMessage(), Toast.LENGTH_SHORT);
                t.show();
            }
            return jasonAnswer;
        }
    }
}
