package com.esprit.sim.SlimFit;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

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

public class RecyclerViewAdapterCategory extends RecyclerView.Adapter<RecyclerViewAdapterCategory.ViewHolder> {

    private String url,url1;
    public  String IdCat;
    ListView liste;
   public static View view ;
    private ArrayList<JSONObject> FiltredListeObject ;
    private ArrayList<String> FiltredListe;
    //vars
    private ArrayList<JSONObject> category ;
    private Context mContext;
    RequestQueue queue;
    public static Activity activity;


    public RecyclerViewAdapterCategory(Context context, ArrayList<JSONObject> exercices) {
        category = exercices;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardcatitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

       // holder.image.setImageDrawable(getResouces().getDrawable(imageResource));
        final JSONObject ex = category.get(position);
        try {
            holder.name.setText(ex.getString("name"));
            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Log.e("el category id ","iss"+ ex.getString("id"));
                        IdCat = ex.getString("id");
                        FetchData(Integer.parseInt(IdCat));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            Log.e("JSONExeption", e.getMessage());
        }



    }



    @Override
    public int getItemCount() {
        return category.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.NomCat);
        }
    }

    public void FetchData(int id){
        FiltredListe = new ArrayList<>();
        FiltredListeObject = new ArrayList<>();
        if (id <= 14 ){
            queue = Volley.newRequestQueue(mContext);
            url = "https://wger.de/api/v2/exercise/?language=2&page=2&category="+id;
            StringRequest getData1 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.e("5raaaaaaaaaaaaa","aaaaaaaaaaaaaaaaa");
                        JSONObject obj = new JSONObject(response);
                        Log.e("response : ",response);
                        JSONArray res = obj.getJSONArray("results");
                        for(int i=0; i<res.length()-1; i++)
                        {
                            JSONObject exec = res.getJSONObject(i);
                            Log.e("aaaaaaaaaaaaaaaaaaaaa",exec.getString("name_original"));
                            exec.put("nom",exec.getString("name_original")) ;
                            FiltredListeObject.add(exec);
                            FiltredListe.add(exec.getString("name_original"));
                        }
                        liste = view.findViewById(R.id.exerciceList);
                        AdapterListe adapter = new AdapterListe(activity,FiltredListeObject);
                        adapter.notifyDataSetChanged();
                        liste.setAdapter(adapter);
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
        }
        else{
            queue = Volley.newRequestQueue(mContext);
            url1 = Connexion.adresse + "/exercice/Categorie/"+id;
            StringRequest getData = new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.e("5raaaaaaaaaaaaa","aaaaaaaaaaaaaaaaa");
                        JSONObject obj = new JSONObject(response);
                        Log.e("response : ",response);
                        JSONArray res = obj.getJSONArray("success");
                        for(int i=0; i<res.length()-1; i++)
                        {
                            JSONObject exec = res.getJSONObject(i);
                            Log.e("aaaaaaaaaaaaaaaaaaaaa",exec.getString("nom"));
                            FiltredListeObject.add(exec);
                            FiltredListe.add(exec.getString("nom"));
                        }
                        liste = view.findViewById(R.id.exerciceList);
                        AdapterListe adapter = new AdapterListe(activity,FiltredListeObject);
                        adapter.notifyDataSetChanged();
                        liste.setAdapter(adapter);

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
            queue.add(getData);
        }
    }

}