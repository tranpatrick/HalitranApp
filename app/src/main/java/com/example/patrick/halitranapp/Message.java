package com.example.patrick.halitranapp;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by ladislas on 13/10/2016.
 */

public class Message {

    private String _id;
    private Long date;
    private String texte;
    private Auteur auteur;

    public Message(String _id, Long date, String texte, Auteur auteur) {
        this._id = _id;
        this.date = date;
        this.texte = texte;
        this.auteur = auteur;
    }

    public String get_id() {
        return _id;
    }

    public String getDate() {
        return getPersonalizedDate(date);
    }

    public String getTexte() {
        return texte;
    }

    public Auteur getAuteur() {
        return auteur;
    }

    private String getPersonalizedDate(Long date) {
        Calendar c = new GregorianCalendar();
        c.setTimeInMillis(date);
        String jour = c.get(Calendar.DAY_OF_MONTH)+"";
        String mois = c.get(Calendar.MONTH)+"";
        if (mois.length() == 1)
            mois = "0"+mois;
        String annee = c.get(Calendar.YEAR)+"";
        String heure = c.get(Calendar.HOUR_OF_DAY)+"";
        String minutes = c.get(Calendar.MINUTE)+"";
        if (minutes.length() == 1)
            minutes = "0"+minutes;
        String secondes = c.get(Calendar.SECOND)+"";
        if (secondes.length() == 1)
            secondes = "0"+secondes;
        return jour+"/"+mois+"/"+annee+" "+heure+":"+minutes+":"+secondes;
    }
}
