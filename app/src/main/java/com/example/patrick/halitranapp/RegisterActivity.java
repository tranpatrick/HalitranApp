package com.example.patrick.halitranapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

/**
 * Created by Patrick on 11/10/2016.
 */

public class RegisterActivity extends AppCompatActivity {

    private EditText login_edit_text;
    private EditText name_edit_text;
    private EditText lastname_edit_text;
    private EditText mail_edit_text;
    private EditText pwd_edit_text;
    private EditText pwdconf_edit_text;

    private Button valider_button;
    private Button annuler_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        login_edit_text = (EditText) findViewById(R.id.inscription_login);
        name_edit_text = (EditText) findViewById(R.id.inscription_lastName);
        lastname_edit_text = (EditText) findViewById(R.id.inscription_lastName);
        mail_edit_text = (EditText) findViewById(R.id.inscription_mail);
        pwd_edit_text = (EditText) findViewById(R.id.inscription_pwd);
        pwdconf_edit_text = (EditText) findViewById(R.id.inscription_pwd_confirmation);

        valider_button = (Button) findViewById(R.id.inscription_valider);
        annuler_button = (Button) findViewById(R.id.inscription_cancel);

    }

    protected void register(View view) {
        String login, name, lastname, mail, pwd, pwd_conf;

        login = login_edit_text.getText().toString();
        name = name_edit_text.getText().toString();
        lastname = lastname_edit_text.getText().toString();
        mail = mail_edit_text.getText().toString();
        pwd = pwd_edit_text.getText().toString();
        pwd_conf = pwdconf_edit_text.getText().toString();

        if(login.equals("") || name.equals("") || lastname.equals("") || mail.equals("") || pwd.equals("") || pwd_conf.equals("")){
            Toast.makeText(this, "Formulaire pas complet", 3).show();
            return;
        }

        /* Verification du pwd */
        if (!pwd.equals(pwd_conf)) {
            Toast.makeText(this, "Passwords must match", Toast.LENGTH_SHORT).show();
            return;
        }

        /* Requete vers le serveur */
        String url = Util.ServerAdress + Util.INSCRIPTION + "?" +
                "login=" + login +
                "&password=" + pwd +
                "&name=" + name +
                "&lastName=" + lastname +
                "&mail=" + mail;

        RequestQueue rq = Volley.newRequestQueue(this);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //System.out.println(response.toString());
                            if(response.has("erreur")){
                                Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                            }else if(response.has("JSON")){
                                Toast.makeText(getApplicationContext(), "Account successfully created", Toast.LENGTH_LONG).show();
                                /* retourner à l'activité de connexion */
                            }else{
                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                                return;
                            }


                        } catch (JSONException je) {

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

    public void cancel(View view){
        finish();
    }

}


