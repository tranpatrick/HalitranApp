package com.example.patrick.halitranapp;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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

public class SearchFragment extends Fragment {

    private SearchFragment.OnFragmentInteractionListener mListener;
    private EditText inputEditText;
    private Button searchButton;
    private ListView listView;
    private ArrayAdapter<Message> adapter;

    private HalitranApplication mApp;

    public SearchFragment(){}

    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i("Fragment", "onCreateView");
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        inputEditText = (EditText) view.findViewById(R.id.inputEditText);
        searchButton = (Button) view.findViewById(R.id.searchButton);
        listView = (ListView) view.findViewById(R.id.listView);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {search();}
        });

        inputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                refreshButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mApp = (HalitranApplication) getActivity().getApplication();
        mApp.getMessages().clear();
        adapter = new MessageAdapter(getActivity(), mApp.getMessages(), mApp);
        listView.setAdapter(adapter);
        return view;
    }

    public void search() {
        String url = Util.ServerAdress+Util.SEARCH;
        String query = inputEditText.getText().toString();
        query = query.replaceAll(" ","%20");
        url = Util.addFirstParameter(url, "key", mApp.getKey());
        url = Util.addParameter(url, "query", query);
        url = Util.addParameter(url, "friends", "0");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (!response.has("erreur")) {
                                mApp.getMessages().clear();
                                inputEditText.setText("");
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


    private void refreshButtonState() {
        String content = inputEditText.getText().toString();
        boolean ok = content.length() > 0
                && content.length() < 140;
        searchButton.setEnabled(ok);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchFragment.OnFragmentInteractionListener) {
            mListener = (SearchFragment.OnFragmentInteractionListener) context;
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
