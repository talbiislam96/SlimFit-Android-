package com.esprit.sim.SlimFit;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class DetailExerciceFragment extends Fragment {
    private String Video;
    private int idExercice;
    private String nomExercice;
    private String descriptionExercice;
    private String Source;
    WebView webview;
    TextView nom, description;
    ImageView image;
    RequestQueue queue1;
    FloatingActionButton btnShare;

    public DetailExerciceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_exercice, container, false);
        queue1 = Volley.newRequestQueue(view.getContext());

        webview = view.findViewById(R.id.webview);
        webview.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        String frameVideo = "<iframe width=\"560\" height=\"315\" src=\"http://www.youtube.com/embed/BU5SOOk6w50\" frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>";
        webview.loadData(frameVideo, "text/html", "utf-8");
        btnShare = view.findViewById(R.id.BtnShare);
        image = view.findViewById(R.id.imageDetailExercice);
        nom = view.findViewById(R.id.NomProg);
        description = view.findViewById(R.id.Detail);
        nom.setText(nomExercice);
        description.setText(descriptionExercice);
        if (Source.equalsIgnoreCase("base"))  {
            StringRequest getData = new StringRequest(Request.Method.GET, Connexion.adresse + "/exercice/" + idExercice, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject obj = new JSONObject(response);
                        JSONArray arrayObj = obj.getJSONArray("success");
                        JSONObject Obj = (JSONObject) arrayObj.get(0);
                        ImageRequest imageLoad = new ImageRequest(Connexion.adresse+"/Ressources/Exercice/" +Obj.getString("image"), new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap response) {
                                // callback
                                image.setImageBitmap(response);
                            }
                        }, 100, 100, null, null);
                        // 100 is your custom Size before Downloading the Image.
                        queue1.add(imageLoad);
                    } catch (JSONException e) {
                        Log.e("JSONExeption", e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("ErrorResponse", "aaaaaaa" + error.getMessage());
                }
            });
            queue1.add(getData);
        } else{
            StringRequest getData = new StringRequest(Request.Method.GET, "https://wger.de/api/v2/exerciseimage/?exercisename=" + nomExercice, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject obj = new JSONObject(response);
                        JSONArray arrayObj = obj.getJSONArray("results");
                        JSONObject Obj = (JSONObject) arrayObj.get(0);
                        ImageRequest imageLoad = new ImageRequest(Obj.getString("image"), new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap response) {
                                // callback
                                image.setImageBitmap(response);
                            }
                        }, 100, 100, null, null);
                        // 100 is your custom Size before Downloading the Image.
                        queue1.add(imageLoad);
                    } catch (JSONException e) {
                        Log.e("JSONExeption", e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("ErrorResponse", "aaaaaaa" + error.getMessage());
                }
            });
            queue1.add(getData);

        }
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = Connexion.adresse + "/accueil/Ajouter/exercice/" + String.valueOf(User.id) + "/" + String.valueOf(idExercice) + "/" + nomExercice;
                //String url = "http://192.168.1.10:3000/accueil/Ajouter/exercice/"+ String.valueOf(User.id)+"/"+ String.valueOf(idExercice)+"/"+nomExercice ;
                StringRequest getData = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
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
                queue1.add(getData);
            }
        });
        return view;
    }

    public String getDescriptionExercice() {
        return descriptionExercice;
    }

    public void setDescriptionExercice(String descriptionExercice) {
        this.descriptionExercice = descriptionExercice;
    }

    public int getIdExercice() {
        return idExercice;
    }

    public void setIdExercice(int idExercice) {
        this.idExercice = idExercice;
    }

    public String getNomExercice() {
        return nomExercice;
    }

    public void setNomExercice(String nomExercice) {
        this.nomExercice = nomExercice;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        this.Source = source;
    }
}
