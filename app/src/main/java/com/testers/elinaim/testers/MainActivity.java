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

        initList();
//        ListView listView = (ListView) findViewById(android.R.id.list);
//        SimpleAdapter simpleAdapter = new SimpleAdapter(this, testersList,
//                android.R.layout.simple_list_item_1, new String[] {"testers"}, new int[] { android.R.id.text1});



    }



//    List<Map<String,String>> testersList = new ArrayList<Map<String,String>>();

    private void initList(){
        try
        {
            String jsonInput = "{\n" +
                    "    \"1\": [\n" +
                    "        {\n" +
                    "            \"description\": \"iPhone 3\",\n" +
                    "            \"deviceId\": \"10\",\n" +
                    "            \"COUNT(c.bugId)\": \"35\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"description\": \"iPhone 4\",\n" +
                    "            \"deviceId\": \"1\",\n" +
                    "            \"COUNT(c.bugId)\": \"23\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"description\": \"iPhone 4S\",\n" +
                    "            \"deviceId\": \"2\",\n" +
                    "            \"COUNT(c.bugId)\": \"26\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"description\": \"iPhone 5\",\n" +
                    "            \"deviceId\": \"3\",\n" +
                    "            \"COUNT(c.bugId)\": \"30\"\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}{\n" +
                    "    \"2\": [\n" +
                    "        {\n" +
                    "            \"description\": \"Droid DNA\",\n" +
                    "            \"deviceId\": \"8\",\n" +
                    "            \"COUNT(c.bugId)\": \"12\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"description\": \"Droid Razor\",\n" +
                    "            \"deviceId\": \"7\",\n" +
                    "            \"COUNT(c.bugId)\": \"16\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"description\": \"Galaxy S3\",\n" +
                    "            \"deviceId\": \"4\",\n" +
                    "            \"COUNT(c.bugId)\": \"24\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"description\": \"Galaxy S4\",\n" +
                    "            \"deviceId\": \"5\",\n" +
                    "            \"COUNT(c.bugId)\": \"19\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"description\": \"HTC One\",\n" +
                    "            \"deviceId\": \"9\",\n" +
                    "            \"COUNT(c.bugId)\": \"17\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"description\": \"Nexus 4\",\n" +
                    "            \"deviceId\": \"6\",\n" +
                    "            \"COUNT(c.bugId)\": \"11\"\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}";



            JSONObject jsonResponse = null;
            try {
                jsonResponse = new JSONObject(jsonInput);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.print("-----" + jsonResponse);




//            JSONObject problems = jsonResponse.getJSONObject("problems");
//
//            if (jsonResponse.has("hits")) {//if needs to order by hits
//                hits = jsonResponse.getJSONArray("hits");
//
//                for (int i = 0; i < 20; i++) {
//                    HashMap itemMap = new HashMap<String, String>();
//                    String key = hits.getString(i);
//                    String name = problems.getJSONObject(key).getString("name");
//                    itemMap.put("value", name);
//                    itemMap.put("key", key);
//                    System.out.println(key + "     " + name);
//                    scriptList.add(itemMap);
//                }
//
//            }

//            JSONArray jsonArray = new JSONArray(jsonInput);
//            int length = jsonArray.length();
//            List<String> listContents = new ArrayList<String>(length);
//            for (int i = 0; i < length; i++)
//            {
//                listContents.add(jsonArray.getString(i));
//            }
//
//            ListView myListView = (ListView) findViewById(android.R.id.list);
//            simpleAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listContents);
//
//            myListView.setAdapter(simpleAdapter);

//            String jsonInput = "[\"one\",\"two\",\"three\",\"four\",\"five\",\"six\",\"seven\",\"eight\",\"nine\",\"ten\"]";
//            JSONArray jsonArray = new JSONArray(jsonInput);
//            int length = jsonArray.length();
//            List<String> listContents = new ArrayList<String>(length);
//            for (int i = 0; i < length; i++)
//            {
//                listContents.add(jsonArray.getString(i));
//            }
//
//            ListView myListView = (ListView) findViewById(android.R.id.list);
//            myListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listContents));
        }
        catch (Exception e)
        {
            // this is just an example
        }
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



//                        String fname = jsonas.getString("description");
//                        Log.d("First Names",fname);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


//                JSONObject testersObj = new JSONObject(result);
//                System.out.print(testersObj);
//                Log.e("log_tag" , String.valueOf(testersObj));
//                Iterator<String> iter = testersObj.keys();
//                while (iter.hasNext()) {
//                    String key = iter.next();
//                    try {
//                        Object value = testersObj.get(key);
//                        Log.e("---", (String) value);
//                    } catch (JSONException e) {
//                        // Something went wrong!
//                    }
//                }

//        Data = jArray.toString();

//                for (int i = 0; i < jArray.length(); i++) {
//                    JSONObject json = jArray.getJSONObject(i);
//
//
//                    Data=Data+"\n"+  json.getString("Head");
//
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
