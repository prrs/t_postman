package com.example.ravis.firstapplication;

import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.database.Cursor;
import android.telephony.TelephonyManager;
import android.content.Context;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;


public class MainActivity extends ActionBarActivity {

    String ph = "1234567890";
    @Override
    public void onStart() {
        super.onStart();

        Branch branch = Branch.getInstance(getApplicationContext());
        branch.initSession(new Branch.BranchReferralInitListener() {
            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                if (error == null) {
                    System.out.println("error on init");
                    // params are the deep linked params associated with the link that the user clicked before showing up
                    Log.i("BranchConfigTest", "deep link data: " + referringParams.toString());
                }
            }
        }, this.getIntent().getData(), this);
    }

    @Override
    public void onStop() {
        super.onStop();
        Branch.getInstance(getApplicationContext()).closeSession();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
//            TelephonyManager tMgr =(TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
//            if (tMgr.getLine1Number() != null)
//            {
//                ph = tMgr.getLine1Number();
//            }
        }
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public void clearText(View button){
        int id= button.getId();
        TextView tv = (TextView)findViewById(R.id.textView);
        tv.setText("");

    }
    public void postSms(View button){
        int id= button.getId();
        TextView tv = (TextView)findViewById(R.id.textView);
        //tv.setText("New Hello, its working :)");

        Uri uri = Uri.parse("content://sms/sent");

        Cursor cursor = getContentResolver().query(uri,
                new String[] { "_id", "thread_id", "address", "person", "date", "body" },
                null,null,null);

        String address="",body="";
        String wholeText = "";

        if (cursor != null)
        {

            try
            {
                int count = cursor.getCount();
                cursor.moveToFirst();
                while (count > 0)
                {
                    count--;
                    long messageId = cursor.getLong(0);
                    long threadId = cursor.getLong(1);
                    address = cursor.getString(2);
                    long contactId = cursor.getLong(3);
                    String contactId_string = String.valueOf(contactId);
                    body = cursor.getString(5);
                    String text =  "\"" + "h"+messageId + "\":\"" + body + "\"";
                    wholeText = wholeText + text + ",";
                    cursor.moveToNext();
                }
                wholeText = wholeText.substring(0,wholeText.length() - 1);
                System.out.print("*********" + wholeText);
                postData(wholeText);

            }
            catch(Exception e){
                e.printStackTrace();
            }
            finally { cursor.close(); }

            tv.setText(wholeText);
        }
    }

    public void changeText1(View button){
        int id= button.getId();
        TextView tv = (TextView)findViewById(R.id.textView);
        tv.setText("Hello World !!");


    }
    public void postData(String postData1) {

        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://10.60.23.140:5000/rest_api");
        postData1 = "{\"" + ph + "\"" +  ":{" + postData1 + "}}";
        System.out.print("###############" + postData1);

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("doc", postData1));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
        }catch (Exception e){
            //System.out.println(((String) response));
            System.out.println("Error !!");
            e.printStackTrace();
        }
    }
}
