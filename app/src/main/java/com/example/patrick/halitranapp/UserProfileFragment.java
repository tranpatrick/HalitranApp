package com.example.patrick.halitranapp;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Patrick on 06/11/2016.
 */

public class UserProfileFragment extends Fragment {

    private UserProfileFragment.OnFragmentInteractionListener mListener;
    private ListView listView;
    private TextView profilNameTextView;
    private TextView bioTextView;
    private String profilName;
    private ArrayAdapter<Message> adapter;

    private HalitranApplication mApp;

    public UserProfileFragment(){}


    public static UserProfileFragment newInstance(String param1, String param2) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    Bundle args = getArguments();
        this.profilName = args.getString("profilName");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i("Fragment", "onCreateView");

        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        profilNameTextView = (TextView) view.findViewById(R.id.profil_name);
        bioTextView = (TextView) view.findViewById(R.id.bio);
        mApp = (HalitranApplication) getActivity().getApplication();
        mApp.getMessages().clear();
        adapter = new MessageAdapter(getActivity(), mApp.getMessages(), mApp);
        ((MessageAdapter)adapter).setDisplayAnswer(false);
        listView.setAdapter(adapter);
        profilNameTextView.setText(profilName);
        getBio();
        search();
        return view;
    }

    public void getBio() {
        String url = Util.ServerAdress+Util.DISPLAY_PROFIL_INFO;
        url = Util.addFirstParameter(url, "profil_name", this.profilName);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (!response.has("erreur")) {
                                bioTextView.setText(response.getString("info"));
                            }
                            else {
                                Toast.makeText(mApp, response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.i("getMessageActivity", "onResponse - "+e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("getMessageActivity", "onErrorResponse - "+error.getMessage().toString());
                        Toast.makeText(mApp, "Probleme reseau", Toast.LENGTH_SHORT).show();
                    }
                });
        mApp.getRequestQueue().add(request);
    }

    public void search() {
        String url = Util.ServerAdress+Util.DISPLAY_PROFIL_TWEETS;
        url = Util.addFirstParameter(url, "profil_name", this.profilName);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (!response.has("erreur")) {
                                mApp.getMessages().clear();
                                JSONArray resultats = response.getJSONArray("resultats");
                                for (int i = 0; i<resultats.length(); i++) {
                                    JSONObject message = resultats.getJSONObject(i);
                                    JSONObject auteur = message.getJSONObject("auteur");
                                    Auteur a = new Auteur(auteur.getInt("id"), auteur.getString("login"));
                                    Message m = new Message(message.getString("_id"),
                                            message.getLong("date"),
                                            message.getString("texte"), a);
                                    mApp.getMessages().add(m);
                                }
                                adapter.notifyDataSetChanged();
                            }
                            else {
                                Toast.makeText(mApp, response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.i("getMessageActivity", "onResponse - "+e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("getMessageActivity", "onErrorResponse - "+error.getMessage().toString());
                        Toast.makeText(mApp, "Probleme reseau", Toast.LENGTH_SHORT).show();
                    }
                });
        mApp.getRequestQueue().add(request);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UserProfileFragment.OnFragmentInteractionListener) {
            mListener = (UserProfileFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
    }

}
