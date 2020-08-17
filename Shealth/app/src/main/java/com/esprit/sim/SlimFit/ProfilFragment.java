package com.esprit.sim.SlimFit;


import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.sim.SlimFit.Classe.Connexion;
import com.esprit.sim.SlimFit.Classe.User;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;


public class ProfilFragment extends Fragment {
    Login login = new Login();
    TextView nom,prenom ;
    TextView age,poid,taille;
    Button modifier,confirmer;
    String url,url2;
    MaterialEditText Poidamodifer,Ageamodifier,Tailleamodifier;
    RequestQueue queue ;
    public User user;

    public ProfilFragment() {
        // Required empty public constructor
    }

    public static ProfilFragment newInstance(String param1, String param2) {
        ProfilFragment fragment = new ProfilFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil, container, false);
        queue = Volley.newRequestQueue(getContext());
        nom = view.findViewById(R.id.nom);
        age = view.findViewById(R.id.age);
        poid = view.findViewById(R.id.poids2);
        taille = view.findViewById(R.id.taille);
        modifier = view.findViewById(R.id.modifier);
        final int id = User.id;
        String nomprenom = User.nom +" "+ User.prenom;
        String PoidsAvecUnite = String.valueOf(User.poids)+"   Kg";
        String tailleAvecUnite = String.valueOf(User.taille)+"  Cm";
        String ageavecUnite =  String.valueOf(User.age)+" Ans";
        age.setText(ageavecUnite);
        nom.setText(nomprenom);
        poid.setText(PoidsAvecUnite);
        taille.setText(tailleAvecUnite);

        modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Modifier Profil");
                alertDialog.setMessage("Veuillez remplir tous les champs");
                View layout_modifier = inflater.inflate(R.layout.layout_update_profil,null);
                Poidamodifer = layout_modifier.findViewById(R.id.poidsamodifer);
                Ageamodifier = layout_modifier.findViewById(R.id.ageamodifier);
                Tailleamodifier = layout_modifier.findViewById(R.id.tailleamodifier);
                confirmer = layout_modifier.findViewById(R.id.modifierconfirmer);
                Poidamodifer.setText((User.poids).toString());
                Tailleamodifier.setText((User.taille).toString());
                Ageamodifier.setText((User.age).toString());
                alertDialog.setView(layout_modifier);
                alertDialog.show();
                confirmer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        url = Connexion.adresse + "/profil/";
                        //url = "http://192.168.1.10:3000/profil/";
                        if (Poidamodifer.getText().length() != 0 && Tailleamodifier.getText().length() != 0 && Ageamodifier.getText().length() != 0){
                            url +=id +"/"+ Poidamodifer.getText() + "/" + Tailleamodifier.getText();
                            StringRequest getData = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject obj = new JSONObject(response);
                                        if (obj.getInt("success") == 1) {
                                            User.poids = Integer.parseInt(Poidamodifer.getText().toString()) ;
                                            User.age = Integer.parseInt(Ageamodifier.getText().toString());
                                            User.taille = Integer.parseInt(Tailleamodifier.getText().toString());
                                            Toast.makeText(getContext(), "profil modifier", Toast.LENGTH_SHORT).show();

                                        } else
                                            Toast.makeText(getContext(), "profil non modifier", Toast.LENGTH_LONG).show();
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
                        alertDialog.setCancelable(true);
                    }
                });
            }
        });
        return view;
    }

}
