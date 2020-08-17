package com.esprit.sim.SlimFit;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.sim.SlimFit.Classe.Connexion;
import com.esprit.sim.SlimFit.Classe.User;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MonProgrammeDetail extends Fragment {

    View view;
    private int idProgramme;
    private String imageURL;
    private String nomProg;
    private String descriptionProg;
    TextView nom, description,textInvisible;
    ImageView image,imageView;
    String url1, url, url2, url3, url5;
    RequestQueue queue;
    FloatingActionButton btnShare;
    Button Modifier, confirmer;
    MaterialEditText nomamodifer, descriptionamodifier;
    private ArrayList<JSONObject> exercice = new ArrayList<>();
    private ArrayList<Integer> exerciceidLocal = new ArrayList<>();
    private ArrayList<String> exerciceidApi = new ArrayList<>();
    private ArrayList<JSONObject> ListeFE = new ArrayList<>();



    public MonProgrammeDetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_mon_programme_detail, container, false);
        listExercice(idProgramme);

        Modifier = view.findViewById(R.id.modifier);
        btnShare = view.findViewById(R.id.BtnShare);
        image = view.findViewById(R.id.imageView3);
        nom = view.findViewById(R.id.NomProg);
        description = view.findViewById(R.id.Detail);
        nom.setText(nomProg);
        description.setText(descriptionProg);
        textInvisible = view.findViewById(R.id.textInvisible);

        queue = Volley.newRequestQueue(getContext());
        url1 = Connexion.adresse + "/programme/" + String.valueOf(idProgramme);
        StringRequest getData5 = new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("el url fih :", "tadaaa :" + url1);

                    JSONObject obj = new JSONObject(response);
                    JSONArray Obj = obj.getJSONArray("success");
                    imageURL = Obj.getJSONObject(0).getString("image") ;
                    Log.e("eli image", "esmha: " + Obj.getJSONObject(0).getString("image"));
                    ImageRequest imageLoad = new ImageRequest(Connexion.adresse + "/Ressources/Programme/" + Obj.getJSONObject(0).getString("image"), new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            // callback
                            image.setImageBitmap(response);
                        }
                    }, 100, 100, null, null);
                    // 100 is your custom Size before Downloading the Image.
                    queue.add(imageLoad);

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
        queue.add(getData5);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = Connexion.adresse + "/accueil/Ajouter/programme/" + String.valueOf(User.id) + "/" + String.valueOf(idProgramme) + "/local";
                StringRequest getData = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("el url fih :", "tadaaa :" + url);

                            JSONObject obj = new JSONObject(response);
                            if (obj.getInt("success") == 1) {
                                Toast.makeText(getContext(), "Success de partage", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(), "Erreur de partage", Toast.LENGTH_LONG).show();
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
            }
        });
        Modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Modifier Programme");
                alertDialog.setMessage("Veuillez remplir tous les champs");
                View layout_modifier = inflater.inflate(R.layout.update_programme, null);
                nomamodifer = layout_modifier.findViewById(R.id.nomamodifer);
                descriptionamodifier = layout_modifier.findViewById(R.id.descriptionamodifier);
                confirmer = layout_modifier.findViewById(R.id.modifierconfirmer);
                image = layout_modifier.findViewById(R.id.imageView);
                nomamodifer.setText(nomProg);
                descriptionamodifier.setText(descriptionProg);
                alertDialog.setView(layout_modifier);
                alertDialog.show();
                confirmer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ModifierProgramme();
                        MonProgrammeDetail MonProgrammeDetailFragment = new MonProgrammeDetail();
                        MonProgrammeDetailFragment.setIdProgramme(idProgramme);
                        MonProgrammeDetailFragment.setNomProg(nomamodifer.getText().toString());
                        MonProgrammeDetailFragment.setDescriptionProg(descriptionamodifier.getText().toString());
                        getFragmentManager().beginTransaction().replace(R.id.fragment, MonProgrammeDetailFragment).commit();

                    }
                });

                alertDialog.setCancelable(true);
            }


        });
        return view;
    }

    private void ModifierProgramme() {

        url = Connexion.adresse + "/programme/Update";
        if (nomamodifer.getText().length() != 0 && descriptionamodifier.getText().length() != 0) {
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.e("Response", "aaaa"+response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.e("Error.Response", "" + error);
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("nom", nomamodifer.getText().toString());
                    params.put("description", descriptionamodifier.getText().toString());
                    params.put("id",String.valueOf(idProgramme));
                    params.put("image",imageURL);
                    return params;
                }
            };
            queue.add(postRequest);
        }
    }

    public int getIdProgramme() {
        return idProgramme;
    }

    public void setIdProgramme(int idProgramme) {
        this.idProgramme = idProgramme;
    }

    public String getNomProg() {
        return nomProg;
    }

    public void setNomProg(String nomProg) {
        this.nomProg = nomProg;
    }

    public String getDescriptionProg() {
        return descriptionProg;
    }

    public void setDescriptionProg(String descriptionProg) {
        this.descriptionProg = descriptionProg;
    }

    public void listExercice(int idProgramme) {
        queue = Volley.newRequestQueue(getContext());
        final RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        url = Connexion.adresse + "/programme/exercice/" + idProgramme;
        StringRequest getData = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //Log.e("5raaaaaaaaaaaaa","aaaaaaaaaaaaaaaaa");
                    JSONObject obj = new JSONObject(response);
                    Log.e("response : ", response);
                    if(obj.getInt("int")== 0){
                        textInvisible.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                    }
                    else{
                        Log.e("response : ",response);
                        JSONArray res = obj.getJSONArray("success");
                        for (int i = 0; i <= res.length() - 1; i++) {
                            JSONObject exec = res.getJSONObject(i);
                            // Log.e("aaaaaaaaaaaaaaaaaaaaa",exec.getString("nom"));
                            if (exec.getString("source").equalsIgnoreCase("local")) {
                                exerciceidLocal.add(exec.getInt("idexercice"));
                                Log.e("exercice local feha","hedhom "+exerciceidLocal);
                            } else {
                                exerciceidApi.add(exec.getString("source"));
                                Log.e("exercice api feha","hedhom "+exerciceidApi);

                            }
                        }}

                    for (int j = 0; j <= exerciceidLocal.size() - 1; j++) {
                        url2 = Connexion.adresse + "/exercice/" + exerciceidLocal.get(j);
                        Log.e("el url taa el local ", "fih" + url2);
                        StringRequest getData2 = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    //Log.e("5raaaaaaaaaaaaa","aaaaaaaaaaaaaaaaa");
                                    JSONObject obj = new JSONObject(response);
                                    Log.e("response : ", response);
                                    JSONArray res = obj.getJSONArray("success");
                                    JSONObject exec = res.getJSONObject(0);
                                    exec.put("image", Connexion.adresse + "/Ressources/Exercice/" + exec.getString("image"));
                                    // Log.e("aaaaaaaaaaaaaaaaaaaaa",exec.getString("nom"));
                                    exercice.add(exec);
                                    Log.e("lista", "el lista feha" + exercice.toString());
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
                    }
                    for (int g = 0; g <= exerciceidApi.size() - 1; g++) {
                        url5 = "https://wger.de/api/v2/exerciseimage/?exercisename=" + exerciceidApi.get(g).replace(" ", "%20");
                        StringRequest getData5 = new StringRequest(Request.Method.GET, url5, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    Log.e("5raaaaaaaaaaaaa", "aaaaaaaaaaaaaaaaa");
                                    JSONObject obj = new JSONObject(response);
                                    Log.e("response : ", response);
                                    JSONArray res = obj.getJSONArray("results");
                                    JSONObject exec = res.getJSONObject(0);
                                    imageURL = exec.getString("image");
                                    Log.e("el image feha url ", "heya   " + imageURL);

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
                        url3 = "https://wger.de/api/v2/exercise/?name=" + exerciceidApi.get(g).replace(" ", "%20");
                        StringRequest getData3 = new StringRequest(Request.Method.GET, url3, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    Log.e("5raaaaaaaaaaaaa", "aaaaaaaaaaaaaaaaa");
                                    JSONObject obj = new JSONObject(response);
                                    Log.e("response : ", response);
                                    JSONArray res = obj.getJSONArray("results");
                                    JSONObject exec = res.getJSONObject(0);
                                    exec.put("image", imageURL);
                                    Log.e("el image feha", "image" + imageURL);
                                    exec.put("nom", exec.getString("name_original"));
                                    Log.e("aaaaaaaaaaaaaaaaaaaaa", exec.toString());
                                    exercice.add(exec);

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
                        queue.add(getData5);
                        Log.e("lista", "el lista feha" + exercice.toString());

                    }

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

        Log.e("exercice fil programme", "houma  " + exercice);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(), exercice);
        recyclerView.setAdapter(adapter);
    }


}