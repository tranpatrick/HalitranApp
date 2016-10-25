package com.example.patrick.halitranapp;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Khalil on 11/10/2016.
 */

public class HalitranApplication extends Application {

    private String login;
    private int id;
    private String key;
    private RequestQueue requestQueue;
    private List<Message> messages;

    @Override
    public void onCreate() {
        super.onCreate();
        messages = new ArrayList<Message>();
    }

    public void saveUsersId(String login, String pwd){
        // ça c'est pour garder le login et pwd de l'utilisateur dans un fichier afin de ne pas se reco à chaque fois
        // crée un fichier preferences avec ses id
        getSharedPreferences("user", MODE_PRIVATE)
                .edit()
                .putString("Login", login)
                .putString("Password", pwd)
                .apply();
    }

    public void clearUsersId() {
        // efface les données utilisateur
        getSharedPreferences("user", MODE_PRIVATE)
                .edit()
                .clear()
                .apply();
    }

    public String getLogin() {
        return login;
    }

    public int getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            Cache cache = new DiskBasedCache(getCacheDir(), 1024*1024);
            Network network = new BasicNetwork(new HurlStack());
            requestQueue = new RequestQueue(cache,network);
            requestQueue.start();
        }
        return requestQueue;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Message> getMessages() {
        return messages;
    }
}
