package com.esprit.sim.SlimFit;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.sim.SlimFit.Classe.Connexion;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccueilSM extends Fragment {


    public AccueilSM() {
        // Required empty public constructor
    }
    String url = Connexion.adresse + "/accueil";

    RequestQueue queue ;
    JSONArray arrayObj ;
    ListView listAccueil;
    ShareDialog shareDialog ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        queue = Volley.newRequestQueue(getContext());
        View view = inflater.inflate(R.layout.fragment_accueil_sm, container, false);
        listAccueil = (ListView)view.findViewById(R.id.PublicList);
        shareDialog = new ShareDialog(this);
        //String[] nomExercices = {"Exercice 1 ","Exercice 2 ","Exercice 3"} ;
        StringRequest getData = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    arrayObj = obj.getJSONArray("success");

                    AdapterAccueil adapter = new AdapterAccueil(getActivity(),arrayObj,shareDialog);
                    listAccueil.setAdapter(adapter);
                } catch (JSONException e) {
                    Log.e("JSONExeption", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ErrorResponse", ""+ error.getMessage());
            }
        });
        queue.add(getData);
        return  view ;
    }
}
