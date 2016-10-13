package com.example.patrick.halitranapp;

import android.app.Application;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;

/**
 * Created by Khalil on 11/10/2016.
 */

public class HalitranApplication extends Application {

    private String login;
    private int id;
    private String key;
    private RequestQueue requestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        Cache cache = new DiskBasedCache(getCacheDir(), 1024*1024);
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache,network);
        requestQueue.start();
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

}
