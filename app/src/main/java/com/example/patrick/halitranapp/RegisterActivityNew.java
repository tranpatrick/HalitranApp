package com.example.patrick.halitranapp;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Patrick on 06/11/2016.
 */

public class RegisterActivityNew extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @InjectView(R.id.inscription_login)
    EditText _usernameText;
    @InjectView(R.id.inscription_name)
    EditText _firstNameText;
    @InjectView(R.id.inscription_lastName)
    EditText _lastNameText;
    @InjectView(R.id.inscription_mail)
    EditText _emailText;
    @InjectView(R.id.inscription_pwd)
    EditText _passwordText;
    @InjectView(R.id.inscription_pwd_confirmation)
    EditText _passwordConfirmationText;
    @InjectView(R.id.btn_signup)
    Button _signupButton;
    @InjectView(R.id.link_login)
    TextView _loginLink;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new);
        ButterKnife.inject(this);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup(v);
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void signup(View v) {
        Log.d(TAG, "Signup");

        final View view = v;

        /*if (!validate()) {
            return;
        }*/

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivityNew.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = _firstNameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: Implement your own signup logic here.

        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        //onSignupSuccess();
                        // onSignupFailed();
                        register(view);
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    protected void register(View view) {
        String login, name, lastname, mail, pwd, pwd_conf;

        login = _usernameText.getText().toString();
        name = _firstNameText.getText().toString();
        lastname = _lastNameText.getText().toString();
        mail = _emailText.getText().toString();
        pwd = _passwordText.getText().toString();
        pwd_conf = _passwordConfirmationText.getText().toString();

        if (login.equals("") || name.equals("") || lastname.equals("") || mail.equals("") || pwd.equals("") || pwd_conf.equals("")) {
            Toast.makeText(this, "Missing informations", Toast.LENGTH_LONG).show();
            return;
        }

        /* Verification du mail */
        if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
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
                            if (response.has("erreur")) {
                                Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                                _signupButton.setEnabled(true);
                            } else if (response.has("JSON")) {
                                Toast.makeText(getApplicationContext(), "Account successfully created", Toast.LENGTH_LONG).show();
                                /* retourner à l'activité de connexion */
                                onSignupSuccess();
                            } else {
                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                                _signupButton.setEnabled(true);
                                return;
                            }

                        } catch (JSONException je) {

                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                _signupButton.setEnabled(true);
            }
        });

        rq.add(jsonRequest);
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("RegisterActivityNew Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
