package com.testers.elinaim.testers;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
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
    List<String> listContents = new ArrayList<String>();
    ArrayAdapter arrayAdapter;
    ListView myListView;
    Spinner mySpinner;

    String country;
    String divcesStr;

    String devices [];
    boolean[] checkedDevices;
    ArrayList<Integer> mUserDevices = new ArrayList<>();
    Button btnDvices;
    TextView mItemSelected;
    AsyncTask getDataF;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       new getData(MainActivity.this).execute("");

        btnDvices = (Button) findViewById(R.id.devicesBtn);
        mItemSelected = (TextView) findViewById(R.id.tvItemSelected);

        devices = getResources().getStringArray(R.array.devices_arrays);
        checkedDevices = new boolean[devices.length];

        btnDvices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                mBuilder.setTitle("Devices Avaliable");
                mBuilder.setMultiChoiceItems(devices, checkedDevices, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if (isChecked) {
                            mUserDevices.add(position);
                        } else {
                            mUserDevices.remove((Integer.valueOf(position)));
                        }
                    }
                });

                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String item = "";
                        for (int i = 0; i < mUserDevices.size(); i++) {
//                            item = item + devices[mUserDevices.get(i)];
                            item = item + String.valueOf(mUserDevices.get(i) + 1) ;
                            if (i != mUserDevices.size() - 1) {
                                item = item + ",";
                            }
                        }
                        divcesStr = "&deviceIds=" + item;
                        new getData(MainActivity.this).execute("");
                        mItemSelected.setText(item);
                    }
                });

                mBuilder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        new getData(MainActivity.this).execute("");
                    }
                });

                mBuilder.setNeutralButton(R.string.clear_all_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for (int i = 0; i < checkedDevices.length; i++) {
                            checkedDevices[i] = false;
                            mUserDevices.clear();
                            new getData(MainActivity.this).execute("");
                            mItemSelected.setText("");
                        }
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }


        });



        ArrayAdapter<String> devicesAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_multiple_choice,devices);

        setListAdapter(devicesAdapter);


        mySpinner  = (Spinner) findViewById(R.id.spinner1);
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);

                Log.d("item----" , item.toString());

                    if(item.toString() == "all") {
                        country = "country=" + item.toString();
                    }else{
                            country ="";
                    }
                new getData(MainActivity.this).execute("");
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });




    }


    class getData extends AsyncTask<String, String, String> {
        String name;
        MainActivity newData = null;


        public getData(MainActivity dt) {
            newData = dt;

        }

        @Override
        protected String doInBackground(String... params) {
            result = "";

            isr = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://solero-web.co.il/MatchingAssignment/api.php?" + newData.country + newData.divcesStr ); //YOUR PHP SCRIPT ADDRESS
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

                    listContents = new ArrayList<String>(allTesters.length());

                    for(int i=0;i<allTesters.length()-1; i++){
                        JSONObject tester = allTesters.getJSONObject(i);

                        Iterator<String> iter = tester.keys();

                        Data = "User " + tester.keys().next() + " has ";
                        Integer counter = 0;

                        //Loop for all User Devices
                        while (iter.hasNext()) {
                            String key = iter.next();

                            try {
                                JSONArray value = (JSONArray) tester.get(key);

                                for(int x=0;x<value.length()-1; x++){
                                    JSONObject testerDevices = value.getJSONObject(x);

                                    counter += Integer.valueOf(testerDevices.getString("COUNT(c.bugId)"));

                                    Data +=   "\n" + testerDevices.getString("COUNT(c.bugId)")
                                                    +" bugs for "
                                                    + testerDevices.getString("description")
                                                    +",";

                                }

                                Data +="\n- "+ counter +" bugs filed for devices in search\n";

//                                Log.d("First Names", FinalOutput);
                            } catch (JSONException e) {
                                // Something went wrong!
                            }
                        }
                        listContents.add(Data);
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } catch (Exception e) {
                // TODO: handle exception
                Log.e("log_tag", "Error Parsing Data " + e.toString());
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            myListView = (ListView) findViewById(android.R.id.list);
            arrayAdapter =new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, listContents);
//            arrayAdapter.notifyDataSetChanged();
            myListView.setAdapter(arrayAdapter);

        }

        @Override
        protected void onPreExecute() {}


    }
}
