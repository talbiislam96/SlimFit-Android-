package com.esprit.sim.SlimFit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.sim.SlimFit.Classe.Connexion;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    public EditText login, mdp, confmdp, nom, prenom, age, taille, poids;
    public Button register;
    String url;
    RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        queue = Volley.newRequestQueue(this);
        login = findViewById(R.id.loginreg);
        mdp = findViewById(R.id.mdp);
        confmdp = findViewById(R.id.confmdp);
        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        age = findViewById(R.id.age);
        taille = findViewById(R.id.taille);
        poids = findViewById(R.id.poids);
        register = findViewById(R.id.register);
        url = Connexion.adresse + "/register/";

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (login.getText().toString().equalsIgnoreCase("") || nom.getText().toString().equalsIgnoreCase("")
                        || prenom.getText().toString().equalsIgnoreCase("") || mdp.getText().toString().equalsIgnoreCase("")
                        || confmdp.getText().toString().equalsIgnoreCase("") || age.getText().toString().equalsIgnoreCase("")
                        || poids.getText().toString().equalsIgnoreCase("") || taille.getText().toString().equalsIgnoreCase("")) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Veillez remplir tous les champs",
                            Toast.LENGTH_SHORT);

                    toast.show();
                } else {
                    if (!mdp.getText().toString().equals(confmdp.getText().toString())) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "le mot de passe n'est pas le meme",
                                Toast.LENGTH_SHORT);

                        toast.show();
                    } else {

                        //url = "http:// 172.18.7.222:3000/register/";
                        url += login.getText() + "/" + mdp.getText() +"/"+ nom.getText()+"/"+prenom.getText() +"/"+ poids.getText() +"/"+ taille.getText() +"/"+ age.getText();
                        StringRequest getData = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    if (obj.getInt("success") == 1) {
                                        Intent Login = new Intent(RegisterActivity.this, com.esprit.sim.SlimFit.Login.class);
                                        startActivity(Login);
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

                }
            }


        });
    }
}
