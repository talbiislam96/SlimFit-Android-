package com.esprit.sim.SlimFit;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.sim.SlimFit.Classe.Connexion;
import com.esprit.sim.SlimFit.Classe.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MesProgrammes extends Fragment {

    FloatingActionButton ajouter;
    String url;
    RequestQueue queue ;
    List<JSONObject> ListProgrammeObj = new ArrayList<>();
    List<String> ListProg = new ArrayList<>();
    RecyclerView ListProgramme;

    public MesProgrammes() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mes_programmes, container, false);
        ListProgramme = view.findViewById(R.id.RecycleMesProgrammes);
        ajouter = view.findViewById(R.id.floatingActionButtonP);
        ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AjouterProgramme AjouterProgrammeFragement = new AjouterProgramme();
                getFragmentManager().beginTransaction().replace(R.id.fragment,AjouterProgrammeFragement).commit();
            }
        });

        queue = Volley.newRequestQueue(getContext());
        url = Connexion.adresse + "/programme/user/"+User.id;
        final StringRequest getData = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //Log.e("kaaaaaaaaaaaaa","aaaaaaaaaaaaaaaaa");
                    JSONObject obj = new JSONObject(response);
                    //Log.e("response : ",response);
                    JSONArray res = obj.getJSONArray("success");
                    for(int i=0; i<= res.length() -1; i++)
                    {
                        JSONObject exec = res.getJSONObject(i);
                        // Log.e("aaaaaaaaaaaaaaaaaaaaa",exec.getString("nom"));
                        ListProgrammeObj.add(exec);
                        //ListProg.add(exec.getString("nom"));
                    }
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    // Set layout manager.
                    ListProgramme.setLayoutManager(layoutManager);
                    RecycleViewAdapterMesProgrammes adapterE = new RecycleViewAdapterMesProgrammes(getContext(),ListProgrammeObj,getFragmentManager());
                    ListProgramme.setAdapter(adapterE);
                    Log.e("lista","el lista feha" + ListProg.toString());
                } catch (JSONException e) {
                    Log.e("JSONExeption", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ErrorResponse", "aaaaaaaaaaaa");
            }
        });
        queue.add(getData);



        return  view ;

    }




}
