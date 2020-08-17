package com.esprit.sim.SlimFit;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdapterListe extends BaseAdapter {

    Context context;
    List<JSONObject> nomExercices;
    LayoutInflater inflater;
    RequestQueue queue;

    public AdapterListe(Context context, ArrayList<JSONObject> nomExercices) {
        this.context = context;
        this.nomExercices = nomExercices;
        //Log.d("CREATION"," lll = "+equipe.length);
        inflater = (LayoutInflater.from(context));
    }


    @Override
    public int getCount() {
        return nomExercices.size();
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
        queue = Volley.newRequestQueue(context);
        convertView = inflater.inflate(R.layout.activity_item_exercice, null);
        TextView exerciceName = (TextView) convertView.findViewById(R.id.card_view_image_title);
        final ImageView image = (ImageView) convertView.findViewById(R.id.card_view_image);
        JSONObject exercice = nomExercices.get(position);
        try {
            exerciceName.setText(exercice.getString("nom"));
            if(exercice.getString("source").equalsIgnoreCase("base")){
                ImageRequest imageLoad = new ImageRequest(exercice.getString("image"), new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        // callback
                        image.setImageBitmap(response);
                    }
                }, 100, 100, null, null);queue.add(imageLoad);
            }
            else {
                String url5 = "https://wger.de/api/v2/exerciseimage/?exercisename=" + exercice.getString("nom").replace(" ", "%20");
                StringRequest getData5 = new StringRequest(Request.Method.GET, url5, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray res = obj.getJSONArray("results");
                            JSONObject exec = res.getJSONObject(0);
                            ImageRequest imageLoad = new ImageRequest(exec.getString("image"), new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap response) {
                                    // callback
                                    image.setImageBitmap(response);
                                }
                            }, 100, 100, null, null);
                            queue.add(imageLoad);

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
                queue.add(getData5);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return convertView;
    }
}
