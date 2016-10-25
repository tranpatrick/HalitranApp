package com.example.patrick.halitranapp;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Patrick on 18/10/2016.
 */

public class AccountFragment extends Fragment {

    EditText username;
    EditText mailAddress;
    EditText currentPassword;
    EditText newPassword;
    EditText confirmPassword;
    Button mailSaveButton;
    Button pwdSaveButton;

    HalitranApplication application;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        application = (HalitranApplication) getActivity().getApplication();

        View view = inflater.inflate(R.layout.fragment_account, container, false);
        username = (EditText) view.findViewById(R.id.account_username);
        mailAddress = (EditText) view.findViewById(R.id.account_mailaddress);
        currentPassword = (EditText) view.findViewById(R.id.account_currentpassword);
        newPassword = (EditText) view.findViewById(R.id.account_newpassword);
        confirmPassword = (EditText) view.findViewById(R.id.account_confirmpassword);

        mailSaveButton = (Button) view.findViewById(R.id.mailsavechanges);
        pwdSaveButton = (Button) view.findViewById(R.id.pwdsavechanges);

        username.setEnabled(false);
        username.setText(application.getLogin());

        /* listeners */
        mailSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChangeMail();
            }
        });

        pwdSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChangePassword();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    public void saveChangeMail(){
        if(isEmailValide(mailAddress.getText().toString())){
            /* Requete vers le serveur */
            String url = Util.ServerAdress + Util.CHANGE_MAIL + "?" +
                    "key=" + application.getKey() +
                    "&newMail=" + mailAddress.getText().toString();

            RequestQueue rq = Volley.newRequestQueue(application.getApplicationContext());

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                //System.out.println(response.toString());
                                if(response.has("erreur")){
                                    Toast.makeText(application.getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                                }else if(response.has("Mail address successfully changed")){
                                    Toast.makeText(application.getApplicationContext(), "Mail address successfully changed", Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(application.getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                            } catch (JSONException je) {
                                je.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    error.printStackTrace();
                }
            });

            rq.add(jsonRequest);
        }else{
            Toast.makeText(application.getApplicationContext(), "Invalid mail address format", Toast.LENGTH_LONG).show();
        }
    }

    public boolean isEmailValide(String email){
        Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9]+.[a-zA-Z]{2,3}$");
        Matcher m = pattern.matcher(email);
        return m.matches();
    }

    public void saveChangePassword() {
        String newpwd = newPassword.getText().toString();

        if (newpwd.length() < 6) {
            Toast.makeText(application.getApplicationContext(), "Password must contain at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newpwd.equals(confirmPassword.getText().toString())){
            Toast.makeText(application.getApplicationContext(), "Passwords must match", Toast.LENGTH_SHORT).show();
            return;
        }

        /* Requete vers le serveur */
        String url = Util.ServerAdress + Util.CHANGE_PASSWORD + "?" +
                "key=" + application.getKey() +
                "&newPwd=" + newpwd +
                "&oldPwd=" + currentPassword.getText().toString();

        RequestQueue rq = Volley.newRequestQueue(application.getApplicationContext());

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //System.out.println(response.toString());
                            if(response.has("erreur")){
                                String msg = response.getString("message");
                                if(msg.equals("Your session has time out")){
                                    //reconnexion
                                    reconnexion();
                                    /* Ici refaire l'action de changerPassword avec un runnable ? */
                                }
                                Toast.makeText(application.getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                            }else if(response.has("Password successfully changed")){
                                Toast.makeText(application.getApplicationContext(), "Password successfully changed", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(application.getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        } catch (JSONException je) {
                            je.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        rq.add(jsonRequest);
    }

    public void reconnexion(){
        String login = application.getSharedPreferences("user", Context.MODE_PRIVATE).getString("Login", "");
        String password = application.getSharedPreferences("user", Context.MODE_PRIVATE).getString("Password", "");

        /* Requete de connexion */
        String loginRequestUrl = Util.ServerAdress + Util.LOGIN;
        loginRequestUrl = Util.addFirstParameter(loginRequestUrl, "login", login);
        loginRequestUrl = Util.addParameter(loginRequestUrl, "password", password);
        Log.i("URL", loginRequestUrl);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, loginRequestUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (!response.has("erreur")) {
                                application.setId(response.getInt("id"));
                                application.setKey(response.getString("key"));
                                application.setLogin(response.getString("login"));
                            } else {
                                Toast.makeText(application, response.getString("message"), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(application, "Probleme reseau", Toast.LENGTH_SHORT).show();
                    }
                });
        application.getRequestQueue().add(jsonObjectRequest);
    }

}
