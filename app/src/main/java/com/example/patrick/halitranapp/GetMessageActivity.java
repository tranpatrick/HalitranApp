package com.example.patrick.halitranapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetMessageActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<Message> adapter;
    private HalitranApplication mApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_messages);
        listView = (ListView) findViewById(R.id.listView);
        mApp = (HalitranApplication) getApplication();
        adapter = new MessageAdapter(this, mApp.getMessages(), mApp);
        listView.setAdapter(adapter);
        if (mApp.getMessages().size() == 0) {
            loadMessages();
        }
    }

    public void loadMessages() {
        Log.i("GASGSG", "loadMessages");
        String url = Util.ServerAdress+Util.DISPLAY_TWEET;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (!response.has("erreur")) {
                                mApp.getMessages().clear();
                                JSONArray resultats = response.getJSONArray("resultats");
                                for (int i = 0; i<resultats.length(); i++) {
                                    JSONObject message = resultats.getJSONObject(i);
                                    JSONObject auteur = message.getJSONObject("auteur");
                                    Auteur a = new Auteur(auteur.getInt("id"), auteur.getString("login"));
                                    Message m = new Message(message.getString("_id"),
                                            message.getLong("date"),
                                            message.getString("texte"), a);
                                    mApp.getMessages().add(m);
                                }
                                adapter.notifyDataSetChanged();
                            }
                            else {
                                Toast.makeText(mApp, response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.i("getMessageActivity", "onResponse - "+e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("getMessageActivity", "onErrorResponse - "+error.getMessage().toString());
                        Toast.makeText(mApp, "Probleme reseau", Toast.LENGTH_SHORT).show();
                    }
                });
        mApp.getRequestQueue().add(request);
    }

    public void getMessages(View view) {
       loadMessages();
    }


}
