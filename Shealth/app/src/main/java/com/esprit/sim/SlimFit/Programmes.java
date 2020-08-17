package com.esprit.sim.SlimFit;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.sim.SlimFit.Classe.Connexion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Programmes extends Fragment {
    String url,url2,url3;
    RequestQueue queue ;
    List<JSONObject> ListProgrammeObj = new ArrayList<>();
    List<String> ListProg = new ArrayList<>();
    ListView ListProgramme;


    public Programmes() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_programmes, container, false);
        ListProgramme = view.findViewById(R.id.programmeList);
        queue = Volley.newRequestQueue(getContext());
        url = Connexion.adresse + "/programme";
        StringRequest getData = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //Log.e("5raaaaaaaaaaaaa","aaaaaaaaaaaaaaaaa");
                    JSONObject obj = new JSONObject(response);
                    //Log.e("response : ",response);
                    JSONArray res = obj.getJSONArray("success");
                    for(int i=0; i<= res.length() -1; i++)
                    {
                        JSONObject exec = res.getJSONObject(i);
                       // Log.e("aaaaaaaaaaaaaaaaaaaaa",exec.getString("nom"));
                        ListProgrammeObj.add(exec);
                        ListProg.add(exec.getString("nom"));
                    }
                    AdapterListeProgramme adapter = new AdapterListeProgramme(getActivity(),ListProg,ListProgrammeObj);
                    adapter.notifyDataSetChanged();
                    ListProgramme.setAdapter(adapter);
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

        ListProgramme.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                JSONObject exerc = ListProgrammeObj.get(position);
                Log.e("object selectionner :","how tadaaa : "+exerc);
                BlankFragment detailProgrammedetail = new BlankFragment();
                try {
                    detailProgrammedetail.setIdProgramme(exerc.getInt("id"));
                    detailProgrammedetail.setNomProg(exerc.getString("nom"));
                    detailProgrammedetail.setDescriptionProg(exerc.getString("description"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getFragmentManager().beginTransaction().replace(R.id.fragment,detailProgrammedetail).addToBackStack(null).commit();
            }

        });

        return  view ;
    }

}
