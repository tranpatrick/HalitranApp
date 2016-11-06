package com.example.patrick.halitranapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Patrick on 06/11/2016.
 */

public class SplashActivity extends AppCompatActivity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 2000;

    private String login;
    private String password;
    HalitranApplication mApp;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);

        mApp = (HalitranApplication) getApplication();

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                if (isUserLogged()) {
                    /* connexion et lance activity_main */
                    connexionDuplicata();
                }else{
                    /* lance activity login */
                    mApp.clearUsersId();
                    Intent intent = new Intent(mApp, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    /* méthode pour vérifier si un uilisateur est loggé */
    private boolean isUserLogged() {
        Log.i("isUserLogged", "je passe dans isUserLogged");
        if (getSharedPreferences("user", MODE_PRIVATE).contains("Login")
                && getSharedPreferences("user", MODE_PRIVATE).contains("Password")) {
            login = mApp.getSharedPreferences("user", MODE_PRIVATE).getString("Login", "");
            password = mApp.getSharedPreferences("user", MODE_PRIVATE).getString("Password", "");
            return true;
        } else {
            return false;
        }
    }

    public void connexionDuplicata() {
        String loginRequestUrl = Util.ServerAdress + Util.LOGIN;
        loginRequestUrl = Util.addFirstParameter(loginRequestUrl, "login", login);
        loginRequestUrl = Util.addParameter(loginRequestUrl, "password", password);
        Log.i("URL", loginRequestUrl);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, loginRequestUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("REPONSE", response.toString());
                        try {
                            if (!response.has("erreur")) {
                                mApp.setId(response.getInt("id"));
                                mApp.setKey(response.getString("key"));
                                mApp.setLogin(response.getString("login"));
                                Intent intent = new Intent(mApp, MainActivity.class);
                                /* Enregistrer les id utilisateurs dans les preferences */
                                mApp.saveUsersId(login, password);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(mApp, response.getString("message"), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(mApp, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } catch (JSONException e) {
                            Log.i("LoginActivity", "onResponse - " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("LoginActivity", "onErrorResponse - " + error.getMessage().toString());
                        Toast.makeText(mApp, "Probleme reseau", Toast.LENGTH_SHORT).show();
                    }
                });
        mApp.getRequestQueue().add(jsonObjectRequest);
    }

}
