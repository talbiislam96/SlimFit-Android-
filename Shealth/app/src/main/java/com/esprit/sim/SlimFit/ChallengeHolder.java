package com.esprit.sim.SlimFit;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.sim.SlimFit.Classe.Connexion;

import org.json.JSONException;
import org.json.JSONObject;

public class ChallengeHolder extends RecyclerView.ViewHolder {

    private TextView nomUserChallenge ;
    private TextView rangUserChallenge ;
    private ImageView imageChallengeUser ;
    RequestQueue queue ;

    public ChallengeHolder(View itemView) {
        super(itemView);
        queue = Volley.newRequestQueue(itemView.getContext());
        nomUserChallenge =(TextView) itemView.findViewById(R.id.nomUserChallenge) ;
        rangUserChallenge =(TextView) itemView.findViewById(R.id.rangUserChallenge) ;
        imageChallengeUser =(ImageView) itemView.findViewById(R.id.imageChallengeUser) ;
    }

    void display(JSONObject obj,int position){
        try {
            JSONObject user = obj.getJSONObject("user") ;
            nomUserChallenge.setText(user.getString("nom") + " " + user.getString("prenom"));
            JSONObject niveau = obj.getJSONObject("niveau");
            int res = loadImage(Connexion.adresse + "/Ressources/Challenge/" + niveau.getString("image"));
            rangUserChallenge.setText(String.valueOf(position) + 1);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int loadImage(String url ) throws InterruptedException {
        ImageRequest imageLoad = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                // callback
                imageChallengeUser.setImageBitmap(response);

            }
        }, 100, 100, null, null);
        // 100 is your custom Size before Downloading the Image.
        Object lock = new Object();

        queue.add(imageLoad);
        return 1 ;
    }
}