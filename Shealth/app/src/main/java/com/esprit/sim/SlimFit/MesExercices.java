
package com.esprit.sim.SlimFit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
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


public class MesExercices extends Fragment {

    FloatingActionButton ajouter;
    View view ;
    String url;
    RecyclerView listExercice;
    RequestQueue queue ;
    ArrayList<JSONObject> ListExercicesObj = new ArrayList<>();
    ArrayList<String> ListExercices = new ArrayList<>();


    public MesExercices() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_mes_exercices, container, false);
        listExercice = (RecyclerView)view.findViewById(R.id.ListeMesE);
        ajouter = view.findViewById(R.id.floatingActionButtonE);
        ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AjouterExercice AjouterExerciceFragment = new AjouterExercice();
                getFragmentManager().beginTransaction().replace(R.id.fragment,AjouterExerciceFragment).commit();
            }
        });
        queue = Volley.newRequestQueue(getContext());
        url = Connexion.adresse + "/exercice/user/"+User.id;
        Log.e("url","aaa"+url);
        StringRequest getData3 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("5raaaaaaaaaaaaa mes exercice","aaaaaaaaaaaaaaaaa");
                    JSONObject obj = new JSONObject(response);
                    JSONArray res = obj.getJSONArray("success");
                    Log.e("response : ",res.get(0).toString());
                    for(int i=0; i<=res.length()-1; i++)
                    {
                        JSONObject exec = res.getJSONObject(i);
                        exec.put("source","base");
                        Log.e("aaaaaaaaaaaaaaaaaaaaa",exec.getString("nom"));
                        exec.put("image", Connexion.adresse + "/Ressources/Exercice/" + exec.getString("image"));
                        ListExercicesObj.add(exec);
                        ListExercices.add(exec.getString("nom"));

                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
                    // Set layout manager.
                    listExercice.setLayoutManager(gridLayoutManager);
                    MesExercicesRecyclerViewAdapter adapterE = new MesExercicesRecyclerViewAdapter(ListExercicesObj,getContext(),getFragmentManager(),getActivity());
                    listExercice.setAdapter(adapterE);
                    //Log.e("lista","el lista feha" + ListExercice.toString());
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
        queue.add(getData3);







        return view;
    }

   }
