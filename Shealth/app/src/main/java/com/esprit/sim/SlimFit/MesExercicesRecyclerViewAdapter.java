package com.esprit.sim.SlimFit;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.sim.SlimFit.Classe.Connexion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MesExercicesRecyclerViewAdapter extends RecyclerView.Adapter<ItemExercice> {
    RequestQueue queue;
    Context context;
    private List<JSONObject> carItemList;
    List<JSONObject> ListProgrammeObj = new ArrayList<>();
    private List<String> Liste;
    FragmentManager fragmentManager;
    Activity activity;
    Spinner spinner;
    String Source;
    public MesExercicesRecyclerViewAdapter(List<JSONObject> carItemList, Context context, FragmentManager fragmentManager, Activity activity) {
        this.carItemList = carItemList;
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.activity= activity;
    }

    @Override
    public ItemExercice onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get LayoutInflater object.
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // Inflate the RecyclerView item layout xml.
        View carItemView = layoutInflater.inflate(R.layout.activity_item_exercice, parent, false);

        // Get car title text view object.
        final TextView carTitleView = (TextView)carItemView.findViewById(R.id.card_view_image_title);
        // Get car image view object.
        final ImageView carImageView = (ImageView)carItemView.findViewById(R.id.card_view_image);
        // When click the image.
        carImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get car title text.
                String carTitle = carTitleView.getText().toString();
                // Create a snackbar and show it.
                Snackbar snackbar = Snackbar.make(carImageView, "You click " + carTitle +" image", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });

        // Create and return our custom Car Recycler View Item Holder object.
        ItemExercice ret = new ItemExercice(carItemView);
        return ret;
    }

    @Override
    public void onBindViewHolder(final ItemExercice holder, int position) {
        queue = Volley.newRequestQueue(context);

        if(carItemList!=null) {
            // Get car item dto in list.
            final JSONObject carItem = carItemList.get(position);

            if(carItem != null) {
                // Set car item title.
                try {
                    holder.getCarTitleText().setText(carItem.getString("nom"));
                    if(carItem.getString("source").equalsIgnoreCase("base")){
                        ImageRequest imageLoad = new ImageRequest(carItem.getString("image"), new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap response) {
                                // callback
                                holder.getCarImageView().setImageBitmap(response);
                            }
                        }, 100, 100, null, null);queue.add(imageLoad);
                    }
                    else {
                        String url5 = "https://wger.de/api/v2/exerciseimage/?exercisename=" + carItem.getString("nom").replace(" ", "%20");
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
                                            holder.getCarImageView().setImageBitmap(response);
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
                holder.getCarImageView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DetailMesExercies DetailMesExerciesFragment = new DetailMesExercies();
                        try {
                            DetailMesExerciesFragment.setIdExercice(carItem.getInt("id"));
                            DetailMesExerciesFragment.setNomExercice(carItem.getString("nom"));
                            DetailMesExerciesFragment.setDescriptionExercice(carItem.getString("description"));
                            DetailMesExerciesFragment.setNomCatExercice(carItem.getJSONObject("categorie").getString("abdo"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        fragmentManager.beginTransaction().replace(R.id.fragment,DetailMesExerciesFragment).addToBackStack(null).commit();
                    }
                });
                holder.getCarImageView().setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setCancelable(true);
                        builder.setTitle("Programme");
                        builder.setMessage("Vous Voulez ajouter cet exercice a un programme?");
                        View mView = View.inflate(context, R.layout.spinner_alert, null);
                        spinner = (Spinner) mView.findViewById(R.id.spinner);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, R.layout.spinner_alert_text, SpinnerListe());
                        adapter.notifyDataSetChanged();
                        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
                        spinner.setAdapter(adapter);
                        builder.setPositiveButton("Confirm",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String text = spinner.getSelectedItem().toString();
                                        if(text != "Veuillez choisir un programme"){
                                            int pos = spinner.getSelectedItemPosition()-1;
                                            JSONObject object = ListProgrammeObj.get(pos);
                                            Log.e("aaaaaaaaaa exercice pos","aaaaaaaaa"+ carItem);
                                            int IdCat ;
                                            int IdProg;
                                            int IdEx ;
                                            try {
                                                IdCat = carItem.getInt("category");
                                                IdProg = object.getInt("id");
                                                IdEx = carItem.getInt("id");
                                                if(IdCat > 14){
                                                    Source = "local";
                                                }
                                                else {
                                                    Source = carItem.getString("name");
                                                }
                                                String url3 = Connexion.adresse + "/programme/AjouterExercice/"+IdProg+"/"+IdEx+"/"+Source;
                                                StringRequest getData2 = new StringRequest(Request.Method.GET, url3, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        try {
                                                            JSONObject obj = new JSONObject(response);
                                                            if (obj.getInt("success") == 1)
                                                                Log.e("aaaa","c bon ajouter");
                                                        } catch (JSONException e) {
                                                            Log.e("JSONExeption", e.getMessage());
                                                        }
                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Log.e("ErrorResponse", "aaaaaaaaa aaa");
                                                    }
                                                });
                                                queue.add(getData2);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                });

                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.setView(mView);
                        dialog.show();
                        return true;
                    }
                });
            }}}

    @Override
    public int getItemCount() {
        int ret = 0;
        if(carItemList!=null)
        {
            ret = carItemList.size();
        }
        return ret;
    }

    public List<String> SpinnerListe(){
        queue = Volley.newRequestQueue(context);
        Liste = new ArrayList<String>();
        Liste.add("Veuillez choisir un programme");
        String url2 = Connexion.adresse + "/programme";
        StringRequest getData1 = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    Log.e("response : ", response);
                    JSONArray res = obj.getJSONArray("success");
                    for (int i = 0; i <= res.length() - 1; i++) {
                        Log.e("el i ", "hhh" + i);
                        JSONObject exec = res.getJSONObject(i);
                        Log.e("noooooom :", "fiiiiih :" + exec.getString("nom"));
                        Liste.add(exec.getString("nom"));
                        ListProgrammeObj.add(exec);
                        Log.e("aaaaaaaa","liste obj prog  "+ListProgrammeObj);
                    }
                    Log.e("liste prog", "feha :" + Liste.toString());

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
        return  Liste;

    }
}
