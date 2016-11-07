package com.example.patrick.halitranapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Patrick on 06/11/2016.
 */

public class ForgotMdpActivity extends AppCompatActivity {
    private static final String TAG = "RecupMdpActivity";
    private static final int REQUEST_SIGNUP = 0;

    @InjectView(R.id.mailEditText) EditText _mailText;
    @InjectView(R.id.recupButton) Button _recupButton;
    HalitranApplication mApp;
    boolean isMailValid;
    boolean isPasswordValid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_mdp);
        ButterKnife.inject(this);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mApp = (HalitranApplication) getApplication();
        _recupButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
        _recupButton.setAlpha(.5f);


        _mailText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isMailValid = _mailText.getText().length() > 0;
                refreshButtonsStates();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

    }

    protected void refreshButtonsStates() {
        if(isMailValid) {
            _recupButton.setAlpha(1);
            _recupButton.setEnabled(true);
        }else {
            _recupButton.setEnabled(false);
            _recupButton.setAlpha(.5f);
        }
    }

    public void login() {
        Log.d(TAG, "Login");

        /*if (!validate()) {
            return;
        }*/

        _recupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(ForgotMdpActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        // TODO: Implement your own authentication logic here.

        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        // onLoginSuccess();
                        // onLoginFailed();
                        recupererMotDePasse();
                        progressDialog.dismiss();
                    }
                }, 2000);
    }

    public void recupererMotDePasse() {
        final String mail;

        mail = _mailText.getText().toString();
        String loginRequestUrl = Util.ServerAdress + Util.FORGOT_PWD;
        loginRequestUrl = Util.addFirstParameter(loginRequestUrl, "email", mail);
        Log.i("URL", loginRequestUrl);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, loginRequestUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("REPONSE", response.toString());
                        try {
                            if (!response.has("erreur")) {
                                Toast.makeText(mApp, "Un mail vous a été envoyé", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(mApp, response.getString("message"), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(mApp, "Erreur interne au serveur", Toast.LENGTH_SHORT).show();
                    }
                });
        mApp.getRequestQueue().add(jsonObjectRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }


    /*public boolean validate() {
        boolean valid = true;
        String login = _loginText.getText().toString();
        String password = _passwordText.getText().toString();

        if (login.isEmpty()) {
            _loginText.setError("invalid username");
            valid = false;
        } else {
            _loginText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6) {
            _passwordText.setError("must contain more than 6 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }
        return valid;
   }*/

}
