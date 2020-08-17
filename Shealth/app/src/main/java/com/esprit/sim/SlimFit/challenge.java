package com.esprit.sim.SlimFit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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

public class challenge extends Fragment {

    private RecyclerView mRecyclerView;
    private JSONArray challenges ;
    private ChallengeAdapter challengeAdapter ;
    RequestQueue queue ;

    public challenge() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_challenge, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.challengeListe);
        ImageView imageView = (ImageView) view.findViewById(R.id.classementGeneral) ;
/*
        Double hightImage = view.getHeight() * 0.2 ;
        imageView.getLayoutParams().height = hightImage.intValue() ;
        imageView.getLayoutParams().width = view.getWidth() ;
        Double hightR = view.getHeight() * 0.8 ;
        mRecyclerView.getLayoutParams().height = hightR.intValue() ;
        mRecyclerView.getLayoutParams().width = view.getWidth() ;
*/

        queue = Volley.newRequestQueue(getContext());
        String url = Connexion.adresse + "/challenge";
        StringRequest getData1 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    Log.e("response : ", response);
                    JSONArray res = obj.getJSONArray("success");

                    challengeAdapter = new ChallengeAdapter(res);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager((getContext())));
                    mRecyclerView.setAdapter(challengeAdapter);
                    //Log.e("liste prog", "feha :" + Liste.toString());

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
        queue.add(getData1);

        return view ;
    }

}
