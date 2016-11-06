package com.example.patrick.halitranapp;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.example.patrick.halitranapp.R.drawable.comment;
import static com.example.patrick.halitranapp.R.drawable.delete;
import static com.example.patrick.halitranapp.R.drawable.reply;

/**
 * Created by ladislas on 13/10/2016.
 */

public class MessageAdapter extends ArrayAdapter<Message> {
    private static class ViewHolder {
        public TextView date;
        public TextView texte;
        public TextView auteur;
        public ImageView imageView;
    }

    private HalitranApplication mApp;

    public MessageAdapter(Context context, List<Message> objects, Application mApp) {
        super(context, R.layout.list_message, objects);
        this.mApp = (HalitranApplication) mApp;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_message, null);
            ViewHolder holder = new ViewHolder();
            holder.texte = (TextView) convertView.findViewById(R.id.texte);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.auteur = (TextView) convertView.findViewById(R.id.auteur);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);

            //TODO gestion des liens, faire comme dealabs
            /*
                holder.texte.setOnClickListerner()
                affiche un tableau avec les choix des liens
             */
        }

        final ViewHolder holder = (ViewHolder) convertView.getTag();
        final Message message = getItem(position);
        holder.texte.setText(message.getTexte());
        holder.date.setText(message.getDate());
        holder.auteur.setText(message.getAuteur().getLogin());

        if (!holder.auteur.getText().toString().equals(mApp.getLogin())) {
            holder.auteur.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createProfilLink(holder.auteur.getText().toString());
                }
            });
        }

        /* On affiche le bouton de suppression si l'utilisateur connecte est l'auteur du tweet */


        if (mApp.getId() == message.getAuteur().getId()) {
            holder.imageView.setImageResource(delete);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeMessageFromMongo(message);
                }
            });
        }
        else {
            holder.imageView.setImageResource(reply);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mApp, "ajouter @"+holder.auteur.getText().toString()+"dans l'editText", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return convertView;
    }

    public void removeMessageFromMongo(final Message m) {
        String key = mApp.getKey();
        String _id = m.get_id();
        String url = Util.ServerAdress+Util.REMOVE_TWEET;
        url = Util.addFirstParameter(url,"key",key);
        url = Util.addParameter(url, "tweetID", _id);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("RESPONSE", response.toString());
                        try {
                            if (!response.has("erreur")) {
                                remove(m);
                                Toast.makeText( mApp, "Tweet successfully removed", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mApp, response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.i("removeFromMongo", "onResponse - "+e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        mApp.getRequestQueue().add(request);
    }

    public void createProfilLink(String profilName) {
        String url = Util.ServerAdress+Util.DISPLAY_PROFIL_INFO;
        url = Util.addFirstParameter(url, "profil_name", profilName);
        Log.i("ONCLICK", url);
    }
}
