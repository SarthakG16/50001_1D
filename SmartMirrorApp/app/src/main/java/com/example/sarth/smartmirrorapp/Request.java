package com.example.sarth.smartmirrorapp;

import android.os.AsyncTask;
import android.util.Log;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.List;

import com.google.gson.Gson;

class Request extends AsyncTask<Void, Void, String> {

    interface Callback { public void onResponse(String response); }
    interface PostersCallback { public void onResponse(List<Poster> posters); }

    String serverURL;
    String method;
    String command;
    Map<String, String> params;
    Callback callback;
    PostersCallback postersCallback;

    public Request(String method, String command, Map<String, String> params, Callback callback) {
        this.serverURL = "http://fishy.asuscomm.com:5000/";
        this.method = method;
        this.command = command;
        this.params = params;
        this.callback = callback;
    }

    public Request(String method, String command, Map<String, String> params, PostersCallback callback) {
        this.serverURL = "http://fishy.asuscomm.com:5000/";
        this.method = method;
        this.command = command;
        this.params = params;
        this.postersCallback = callback;
    }

    private Exception exception;

    protected void onPreExecute() {

    }

    protected String doInBackground(Void... urls) {


        try {

            URL url = new URL(serverURL + command);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod(method);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");

            Gson gson = new Gson();
            String query = gson.toJson(params);
            Log.e("REQINFO", query);

            try {
                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                String json_response = "";
                InputStreamReader in = new InputStreamReader(urlConnection.getInputStream());
                BufferedReader br = new BufferedReader(in);
                String text = "";
                while ((text = br.readLine()) != null) {
                    json_response += text;
                }

                return json_response;
            }

            finally{
                urlConnection.disconnect();
            }
        }

        catch(Exception e) {
            Log.e("REQERROR", e.getMessage(), e);
            return null;
        }
    }

    protected void onPostExecute(String response) {
        if(response == null) {
            response = "NOTHING WAS RETURNED";
        }
        Log.i("REQINFO", response);
        if (callback != null) {
            callback.onResponse(response);
        }
        if (postersCallback != null) {
            Gson g = new Gson();
            Posters posters = g.fromJson(response, Posters.class);
            postersCallback.onResponse(posters.posters);
        }

    }
}