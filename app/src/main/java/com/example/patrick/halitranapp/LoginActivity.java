package com.example.patrick.halitranapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText loginEditText;
    EditText passwordEditText;
    TextView createUserTextView;
    Button loginButton;
    Button resetButton;
    HalitranApplication mApp;
    boolean isLoginValid;
    boolean isPasswordValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mApp = (HalitranApplication) getApplication();
        loginEditText = (EditText) findViewById(R.id.loginEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        createUserTextView = (TextView) findViewById(R.id.createUserTextView);
        loginButton = (Button) findViewById(R.id.loginButton);
        resetButton = (Button) findViewById(R.id.resetButton);

        /* Connecter l'utilisateur déjà loggé */
        if (isUserLogged()) {
            Log.i("onCreate", "je passe dans le if isUserLogged");
            connexionDuplicata();
        }

        loginEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isLoginValid = loginEditText.getText().length() > 0;
                refreshButtonsStates();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isPasswordValid = passwordEditText.getText().length() >= 6;
                refreshButtonsStates();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    protected void refreshButtonsStates() {
        loginButton.setEnabled(isLoginValid && isPasswordValid);
        resetButton.setEnabled(isLoginValid && isPasswordValid);
    }

    public void connexionDuplicata() {
        final String login = loginEditText.getText().toString();
        final String password = passwordEditText.getText().toString();
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

    public void connexion(View view) {
        connexionDuplicata();
    }

    public void resetForm(View view) {
        loginEditText.setText("");
        passwordEditText.setText("");
    }

    public void openCreateUserActivity(View view) {
        //Toast.makeText(this, "Not Implemented Yet", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, RegisterActivity.class));
    }

    /* méthode pour vérifier si un uilisateur est loggé */
    private boolean isUserLogged() {
        Log.i("isUserLogged", "je passe dans isUserLogged");
        if (getSharedPreferences("user", MODE_PRIVATE).contains("Login")
                && getSharedPreferences("user", MODE_PRIVATE).contains("Password")) {
            loginEditText.setText(mApp.getSharedPreferences("user", MODE_PRIVATE).getString("Login", ""));
            passwordEditText.setText(mApp.getSharedPreferences("user", MODE_PRIVATE).getString("Password", ""));
            return true;
        } else {
            return false;
        }
    }



}
