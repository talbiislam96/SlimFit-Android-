package com.esprit.sim.SlimFit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Spinner;

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

public class Exercice extends Fragment {
    View view ;
    String url,url2,url3,url1;
    RequestQueue queue ;
    ArrayList<JSONObject> ListExercicesObj = new ArrayList<>();
    ArrayList<String> ListExercices = new ArrayList<>();
    List<String> Liste = new ArrayList<>();
    RecyclerView listExercice;
    Spinner spinner;
    String Source;
    ImageButton ajouter;
    private ArrayList<JSONObject> category = new ArrayList<>();

    public Exercice() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_exercice, container, false);
        RecyclerViewAdapterCategory.view = view;
        RecyclerViewAdapterCategory.activity = getActivity();

        listExercice = (RecyclerView) view.findViewById(R.id.exerciceList);
        //ajouter = view.findViewById(R.id.imageButton);
        queue = Volley.newRequestQueue(getContext());
        /*category = new ArrayList<JSONObject>();
        url2 = Connexion.adresse + "/categorie";
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
                        Log.e("noooooom :", "fiiiiih :" + exec.getString("name"));
                        category.add(exec);
                    }

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
        url3 = "https://wger.de/api/v2/exercisecategory/";
        StringRequest getData2 = new StringRequest(Request.Method.GET, url3, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    Log.e("response : ", response);
                    JSONArray res = obj.getJSONArray("results");
                    for (int i = 0; i <= res.length() - 1; i++) {
                        Log.e("el i ", "hhh" + i);
                        JSONObject exec = res.getJSONObject(i);
                        Log.e("noooooom :", "fiiiiih :" + exec.getString("name"));
                        category.add(exec);
                    }

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
        queue.add(getData2);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        final RecyclerView recyclerView = view.findViewById(R.id.recyclerViewCat);
        recyclerView.setLayoutManager(layoutManager);
        final RecyclerViewAdapterCategory adapterR = new RecyclerViewAdapterCategory(getContext(), category);
        recyclerView.setAdapter(adapterR);*/
        queue = Volley.newRequestQueue(getContext());
        url = "https://wger.de/api/v2/exercise/?language=2&page=2";
        StringRequest getData = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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
                        exec.put("source","api");
                        ListExercicesObj.add(exec);
                        ListExercices.add(exec.getString("name_original"));
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
                    // Set layout manager.
                    listExercice.setLayoutManager(gridLayoutManager);
                    CarRecyclerViewDataAdapter adapterE = new CarRecyclerViewDataAdapter(ListExercicesObj,getContext(),getFragmentManager(),getActivity());
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
        queue.add(getData);
        url1 = Connexion.adresse + "/exercice";
        StringRequest getData3 = new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
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
                        exec.put("source","base");
                        exec.put("image", Connexion.adresse + "/Ressources/Exercice/" + exec.getString("image"));
                        ListExercicesObj.add(exec);
                        ListExercices.add(exec.getString("nom"));
                    }
                    LinearLayoutManager layoutManager1Exercice =  new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    listExercice.setLayoutManager(layoutManager1Exercice);
                    CarRecyclerViewDataAdapter adapterE = new CarRecyclerViewDataAdapter(ListExercicesObj,getContext(),getFragmentManager(),getActivity());
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




        /*
        listExercice.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                builder.setTitle("Programme");
                builder.setMessage("Vous Voulez ajouter cet exercice a un programme?");
                View mView = View.inflate(getContext(), R.layout.spinner_alert, null);
                spinner = (Spinner) mView.findViewById(R.id.spinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_alert_text, SpinnerListe());
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
                                    JSONObject objExe = ListExercicesObj.get(position);
                                    Log.e("aaaaaaaaaa exercice pos","aaaaaaaaa"+ objExe);
                                    int IdCat ;
                                    int IdProg;
                                    int IdEx ;
                                    try {
                                        IdCat = objExe.getInt("category");
                                        IdProg = object.getInt("id");
                                        IdEx = objExe.getInt("id");
                                        if(IdCat > 14){
                                            Source = "local";
                                        }
                                        else {
                                            Source = objExe.getString("name");
                                        }
                                        url3 = Connexion.adresse + "/programme/AjouterExercice/"+IdProg+"/"+IdEx+"/"+Source;
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
*/
        return  view ;
    }


}