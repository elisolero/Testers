package com.testers.elinaim.testers;

import android.app.ListActivity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends ListActivity {

    String sData;
    TextView tv;
    String Data = "";
    String result;
    InputStream isr;
    Context con;
    JSONObject testersObj;
    ArrayAdapter<String> simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new getData().execute("");

//        ListView listView = (ListView) findViewById(android.R.id.list);
//        SimpleAdapter simpleAdapter = new SimpleAdapter(this, testersList,
//                android.R.layout.simple_list_item_1, new String[] {"testers"}, new int[] { android.R.id.text1});

    }




    private class getData extends AsyncTask<String, Void, String> {
        String name;

        @Override
        protected String doInBackground(String... params) {
            result = "";
            isr = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://solero-web.co.il/MatchingAssignment/api.php?country=US"); //YOUR PHP SCRIPT ADDRESS
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                isr = entity.getContent();
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());

            }




            //convert response to string
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(isr, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                isr.close();

                result = sb.toString();
//                Log.e("log_DATA", result);
            } catch (Exception e) {
                Log.e("log_tag", "Error  converting result " + e.toString());
            }


            try {


                try {
                    JSONArray allTesters= new JSONArray(result);

                    for(int i=0;i<allTesters.length()-1; i++){
                        JSONObject tester = allTesters.getJSONObject(i);

                        Iterator<String> iter = tester.keys();


                        String FinalOutput = "User " + tester.keys().next() + " has ";
                        Integer counter = 0;

                        //Loop for all User Devices
                        while (iter.hasNext()) {
                            String key = iter.next();

                            try {
                                JSONArray value = (JSONArray) tester.get(key);

                                for(int x=0;x<value.length()-1; x++){
                                    JSONObject testerDevices = value.getJSONObject(x);

                                    counter += Integer.valueOf(testerDevices.getString("COUNT(c.bugId)"));

                                    FinalOutput +=   testerDevices.getString("COUNT(c.bugId)")
                                                    +" bugs for "
                                                    + testerDevices.getString("description")
                                                    +",";

                                }
                                FinalOutput +="\n- "+ counter +" bugs filed for devices in search\n";

//                                Log.d("First Names", FinalOutput);
                            } catch (JSONException e) {
                                // Something went wrong!
                            }
                        }

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }




//                 Data = jArray.toString();

//                for (int i = 0; i < jArray.length(); i++) {
//                    JSONObject json = jArray.getJSONObject(i);
//
//                    Data=Data+"\n"+  json.getString("Head");
//
//                }

            } catch (Exception e) {
                // TODO: handle exception
                Log.e("log_tag", "Error Parsing Data " + e.toString());
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
//            tv.setText(""+Data);
//            simpleAdapter.notifyDataSetChanged();
            Log.e("log_DATA", Data);
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
