package com.example.patrick.halitranapp;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ladislas on 13/10/2016.
 */

public class Auteur {
    private int id;
    private String login;

    public Auteur(int id, String login) {
        this.id = id;
        this.login = login;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

}
