package com.esprit.sim.SlimFit;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.sim.SlimFit.Classe.Connexion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class AdapterListeProgramme extends BaseAdapter {
    RequestQueue queue;

    Context context ;
    List<String> nomProgramme ;
    List<JSONObject> progObj;
    LayoutInflater inflater;

    public AdapterListeProgramme(Context context, List<String> nomProgramme, List<JSONObject> progObj) {
        this.context = context;
        this.nomProgramme = nomProgramme;
        this.progObj = progObj;
        //Log.d("CREATION"," lll = "+equipe.length);
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return nomProgramme.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.activity_programme_item,null);
        TextView exerciceName = (TextView) convertView.findViewById(R.id.nomprogramme);
        final ImageView imageProg = (ImageView) convertView.findViewById(R.id.programmeimage);
        exerciceName.setText(nomProgramme.get(position));
        queue = Volley.newRequestQueue(context);
        try {
            ImageRequest imageLoad = new ImageRequest(Connexion.adresse + "/Ressources/Programme/"+progObj.get(position).getString("image"), new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    // callback
                    imageProg.setImageBitmap(response);
                }
            }, 100, 100, null, null);
            // 100 is your custom Size before Downloading the Image.
            queue.add(imageLoad);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 100 is your custom Size before Downloading the Image.
        return convertView;
    }
}
