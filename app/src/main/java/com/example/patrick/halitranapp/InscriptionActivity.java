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

public class InscriptionActivity extends AppCompatActivity {

    private EditText pseudo_edit_text;
    private EditText prenom_edit_text;
    private EditText nom_edit_text;
    private EditText email_edit_text;
    private EditText mdp_edit_text;
    private EditText mdp_conf_edit_text;

    private Button valider_button;
    private Button annuler_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inscription_layout);

        pseudo_edit_text = (EditText) findViewById(R.id.inscription_pseudo);
        prenom_edit_text = (EditText) findViewById(R.id.inscription_prenom);
        nom_edit_text = (EditText) findViewById(R.id.inscription_nom);
        email_edit_text = (EditText) findViewById(R.id.inscription_email);
        mdp_edit_text = (EditText) findViewById(R.id.inscription_mdp);
        mdp_conf_edit_text = (EditText) findViewById(R.id.inscription_mdp_confirmation);

        valider_button = (Button) findViewById(R.id.inscription_valider);
        annuler_button = (Button) findViewById(R.id.inscription_annuler);

    }

    protected void valider(View view) {
        String pseudo, prenom, nom, email, mdp, mdp_conf;

        pseudo = pseudo_edit_text.getText().toString();
        prenom = prenom_edit_text.getText().toString();
        nom = nom_edit_text.getText().toString();
        email = email_edit_text.getText().toString();
        mdp = mdp_edit_text.getText().toString();
        mdp_conf = mdp_conf_edit_text.getText().toString();

        /* if(pseudo.equals("") || prenom.equals("") || nom.equals("") || email.equals("") || mdp.equals("") || mdp_conf.equals("")){
            Toast.makeText(this, "Formulaire pas complet", 3).show();
            return;
        } */

        /* Verification du mdp */
        if (!mdp.equals(mdp_conf)) {
            Toast.makeText(this, "Passwords should match", Toast.LENGTH_SHORT).show();
            return;
        }

        /* Requete vers le serveur */
        String url = Util.ServerAdress + Util.INSCRIPTION + "?" +
                "login=" + pseudo +
                "&password=" + mdp +
                "&name=" + prenom +
                "&lastName=" + nom +
                "&mail=" + email;

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

}


