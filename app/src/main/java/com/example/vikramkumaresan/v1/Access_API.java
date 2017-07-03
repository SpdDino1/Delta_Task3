package com.example.vikramkumaresan.v1;

import android.accessibilityservice.GestureDescription;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

public class Access_API extends AsyncTask<Void,Void,String> {

    String name;
    Context ctx;

    public Access_API(String Name,Context ctx) {
        name=Name;
        this.ctx=ctx;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            //API Accessing
            URL url = new URL("http://pokeapi.co/api/v2/pokemon/"+name);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);

            InputStream instream = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(instream,"iso-8859-1");
            BufferedReader buffread = new BufferedReader(reader);

            String JSONString="";
            String line = buffread.readLine();

            while(line!=null){
                JSONString+=line;
                line=buffread.readLine();
            }
            return JSONString;

        } catch (MalformedURLException e) {
            Log.d("TAG","Malformed URL = "+e.toString());
            return null;
        } catch (IOException e) {
            if(e instanceof UnknownHostException){
                return "No Internet";
            }
            else if(e instanceof FileNotFoundException){
                return "No Pokemon Found";
            }
            else {
                return null;
            }
        }

    }

    @Override
    protected void onPostExecute(String s) {
        if(s.equals("No Internet")){
            Toast.makeText(ctx,"Internet Access Unavailable",Toast.LENGTH_SHORT).show();
            Loading.ctx.finish();
        }
        else if(s.equals("No Pokemon Found")){
            Toast.makeText(ctx,"No Pokemon Found",Toast.LENGTH_SHORT).show();
            Loading.ctx.finish();
        }
        else if(s!=null){
            try {
                //JSON Parsing Mech
                JSONObject parent = new JSONObject(s);
                String name = parent.getString("name");
                int height = parent.getInt("height");
                int weight = parent.getInt("weight");
                String sprite = parent.getJSONObject("sprites").getString("front_default");

                JSONArray types = parent.getJSONArray("types");
                String type="";

                for (int i=0;i<types.length();i++){
                    if(i==0){
                        type=type+types.getJSONObject(i).getJSONObject("type").getString("name");
                    }
                    else {
                        type=type+"+"+types.getJSONObject(i).getJSONObject("type").getString("name");
                    }
                }

                MainActivity.Async_Complete(new ArrayList<String>(Arrays.asList(name,type,""+height,""+weight,sprite)),ctx); //Pass results to MainActivity

            } catch (JSONException e) {
                Log.d("TAG","JSON Exception = "+e.toString());
            }
        }
        else {
            Toast.makeText(ctx,"Internal Error",Toast.LENGTH_SHORT).show();
            Loading.ctx.finish();
        }

    }
}
