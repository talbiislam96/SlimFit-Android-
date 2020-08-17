package com.esprit.sim.SlimFit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.sim.SlimFit.Classe.Connexion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class RecycleViewAdapterMesProgrammes extends RecyclerView.Adapter<RecycleViewAdapterMesProgrammes.MyViewHolder> {

    private Context context;
    private List<JSONObject> list;
    RequestQueue queue;
    public FragmentManager f_manager;


    public RecycleViewAdapterMesProgrammes(Context context, List<JSONObject> list, FragmentManager f_manager) {
        this.context = context;
        this.list = list;
        this.f_manager = f_manager;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mesprogrammeitem, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecycleViewAdapterMesProgrammes.MyViewHolder holder, int position) {
        queue = Volley.newRequestQueue(context);
        final JSONObject programme = list.get(position);
        try {
            holder.nomProgramme.setText(programme.getString("nom"));
            ImageRequest imageLoad = new ImageRequest(Connexion.adresse+"/Ressources/Programme/" + programme.getString("image"), new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    // callback
                    holder.imageProgramme.setImageBitmap(response);
                }
            }, 100, 100, null, null);
            queue.add(imageLoad);
            holder.imageProgramme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MonProgrammeDetail MonProgrammedetail = new MonProgrammeDetail();
                    try {
                        MonProgrammedetail.setIdProgramme(programme.getInt("id"));
                        MonProgrammedetail.setNomProg(programme.getString("nom"));
                        MonProgrammedetail.setDescriptionProg(programme.getString("description"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    f_manager.beginTransaction().replace(R.id.fragment,MonProgrammedetail).addToBackStack(null).commit();
                }
            });
            holder.imageProgramme.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setCancelable(true);
                    builder.setTitle("Programme");
                    builder.setMessage("Vous Voulez supprimer ce programme?");
                    builder.setPositiveButton("Confirm",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    remove(programme);
                                }
                            });

                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return true;
                }
            });
        } catch (JSONException e) {
            Log.e("JSONExeption", e.getMessage());
        }


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void remove(final JSONObject item){
        try {
            String url = Connexion.adresse + "/programme/Delete/"+item.getInt("id");
            StringRequest getData = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getInt("success") == 1) {
                            list.remove(item);
                        }
                    } catch (JSONException e) {
                        Log.e("JSONExeption", e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("ErrorResponse", error.getMessage());
                }
            });
            queue.add(getData);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nomProgramme;
        public ImageView imageProgramme;


        public MyViewHolder(View itemView) {
            super(itemView);
            nomProgramme = itemView.findViewById(R.id.nommonprogramme);
            imageProgramme = itemView.findViewById(R.id.imagemesprogrammes);
        }
    }
}
