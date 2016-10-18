package com.example.patrick.halitranapp;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    private ListView listView;
    private ArrayAdapter<Message> adapter;
    private HalitranApplication mApp;
    private Button refreshButton;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        refreshButton = (Button) view.findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMessages();
            }
        });
        mApp = (HalitranApplication) getActivity().getApplication();
        adapter = new MessageAdapter(getActivity(), mApp.getMessages(), mApp);
        listView.setAdapter(adapter);
        if (mApp.getMessages().size() == 0) {
            loadMessages();
        }
        return view;
    }

    public void loadMessages() {
        Log.i("GASGSG", "loadMessages");
        String url = Util.ServerAdress+Util.DISPLAY_TWEET;
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
    }
}
