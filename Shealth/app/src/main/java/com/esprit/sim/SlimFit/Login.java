package com.esprit.sim.SlimFit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Calendar;

public class Login extends AppCompatActivity {

    static LoginButton loginButton ;
    String url;
    RequestQueue queue ;
    public User user;
    Double poid , taille ;
    int verifDonnées = 1 ;
    EditText poidEditText,tailleEditText ;
    Button annulerBtn,validerBtn ;
    View popupInputDialogView;
    String SharedLogin,SharedPassword,SharedidFacebook ;
    SharedPreferences prefs;

    CallbackManager callbackManager = CallbackManager.Factory.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        queue = Volley.newRequestQueue(this);
        final int verif = 0 ;
        prefs = getApplicationContext().getSharedPreferences("myLogin",0);
        SharedidFacebook =  prefs.getString("idfacebook","");
        SharedLogin =  prefs.getString("login","");
        SharedPassword =  prefs.getString("password","");

        if(!SharedidFacebook.equals("") && !SharedidFacebook.equals(null)){
            Log.e("facebook id wa7dou ",SharedidFacebook);
            url = Connexion.adresse + "/verificationFaceBook/" + SharedidFacebook ;
            //url = "http://192.168.1.10:3000/register/";
            StringRequest getData = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject obj = new JSONObject(response);
                        Log.e("user 2 njarreb :" , response);
                        if (obj.getInt("success") == 1) {
                            Intent mainApplication = new Intent(Login.this, MainActivity.class);
                            startActivity(mainApplication);
                            JSONArray row = new JSONArray(obj.getString("result"));
                            JSONObject use = row.getJSONObject(0);
                            User.id = use.getInt("id");
                            User.login = use.getString("login");
                            User.nom = use.getString("nom");
                            User.prenom =  use.getString("prenom");
                            User.mdp = use.getString("mdp");
                            User.poids = use.getInt("poid");
                            User.taille = use.getInt("taille");
                            User.age = use.getInt("age");
                        }
                    } catch (JSONException e) {
                        Log.e("JSONExeption", ""+e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("ErrorResponse", "" +error.getMessage());
                }
            });
            queue.add(getData);
        }

        else if(!SharedLogin.equals("") && !SharedPassword.equals("")){
            url = Connexion.adresse + "/login/" + SharedLogin + "/" + SharedPassword;
            StringRequest getData = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject obj = new JSONObject(response);
                        Log.e("user : wa7dou :" ,response);
                        if (obj.getInt("success") == 1) {
                            JSONArray row = new JSONArray(obj.getString("result"));
                            Intent accueil = new Intent(Login.this, MainActivity.class);
                            JSONObject use = row.getJSONObject(0);
                            User.id = use.getInt("id");
                            Log.e("el id mta3 el khra","aa"+use.getInt("id"));
                            User.login = use.getString("login");
                            User.nom = use.getString("nom");
                            User.prenom =  use.getString("prenom");
                            User.mdp = use.getString("mdp");
                            User.poids = use.getInt("poid");
                            User.taille = use.getInt("taille");
                            User.age = use.getInt("age");
                            startActivity(accueil);
                        } else
                            Toast.makeText(getApplicationContext(), "Invalid password or login", Toast.LENGTH_LONG).show();
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


        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.aziz.ShareFit",
                    PackageManager.GET_SIGNATURES);
            Log.e("infoooooo " , String.valueOf(info.hashCode()));
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("error 2 : ", e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            Log.e("error lowl :" , e.getMessage());
        }

        final TextView login = findViewById(R.id.loginText);
        final TextView password = findViewById(R.id.passwordText);
        Button connexionBtn = findViewById(R.id.connexion);
        TextView reg = findViewById(R.id.register);

        //action Button Connexion
        connexionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                url = Connexion.adresse + "/login/";
                //url = "http://192.168.1.10:3000/login/";
                if (login.getText().length() != 0 && password.getText().length() != 0){
                    url += login.getText() + "/" + password.getText();
                    StringRequest getData = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                Log.e("user 3 njarreb :",response);
                                if (obj.getInt("success") == 1) {
                                    SharedPreferences.Editor editor = prefs.edit();
                                    JSONArray row = new JSONArray(obj.getString("result"));
                                    JSONObject use = row.getJSONObject(0);
                                    User.id = use.getInt("id");
                                    Log.e("el id mta3 el khra","aa"+use.getInt("id"));
                                    User.login = use.getString("login");
                                    User.nom = use.getString("nom");
                                    User.prenom =  use.getString("prenom");
                                    User.mdp = use.getString("mdp");
                                    User.poids = use.getInt("poid");
                                    User.taille = use.getInt("taille");
                                    User.age = use.getInt("age");

                                    editor.putString("login",use.getString("login"));
                                    editor.putString("password",use.getString("mdp"));
                                    editor.putString("idfacebook",use.getString("idfacebook"));
                                    editor.apply();

                                    Intent accueil = new Intent(Login.this, MainActivity.class);
                                    startActivity(accueil);
                                } else
                                    Toast.makeText(getApplicationContext(), "Invalid password or login", Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                Log.e("JSONExeption", e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("ErrorResponse", error.getMessage()+"hello");
                        }
                    });
                    queue.add(getData);
                }
            }
        });

        //action button register
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reg = new Intent(Login.this, RegisterActivity.class);
                startActivity(reg);
            }
        });

        //login facebook button
        loginButton = (LoginButton) findViewById(R.id.login_button);
        //loginButton.setReadPermissions("email");
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends", "user_birthday"));

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                String accessToken = loginResult.getAccessToken().getToken();

/*
                // save accessToken to SharedPreference
                prefUtil.saveAccessToken(accessToken);
*/

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject jsonObject,
                                                    GraphResponse response) {

                                // Getting FB User Data
                                if (jsonObject.has("first_name")) {
                                    try {
                                        User.prenom = jsonObject.getString("first_name");
                                        Log.e("name = ", jsonObject.getString("first_name"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (jsonObject.has("last_name"))
                                    try {
                                        User.nom = jsonObject.getString("last_name") ;
                                        Log.e("last_name = ", jsonObject.getString("last_name"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                if (jsonObject.has("email"))
                                    try {
                                        Log.e("email = ", jsonObject.getString("email"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                if (jsonObject.has("birthday"))
                                    try {
                                        String year = jsonObject.getString("birthday").substring(6) ;
                                        int age = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(year) ;
                                        User.age = age ;
                                        Log.e("age = ", String.valueOf(age) );
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name,email,birthday");
                request.setParameters(parameters);
                request.executeAsync();
                User.idFacebook = loginResult.getAccessToken().getUserId() ;

                url = Connexion.adresse + "/verificationFaceBook/" + User.idFacebook ;
                //url = "http://192.168.1.10:3000/register/";
                StringRequest getData = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getInt("success") == 1) {
                                SharedPreferences.Editor editor = prefs.edit();
                                Intent mainApplication = new Intent(Login.this, MainActivity.class);
                                startActivity(mainApplication);
                                JSONArray row = new JSONArray(obj.getString("result"));
                                JSONObject use = row.getJSONObject(0);
                                User.id = use.getInt("id");
                                User.login = use.getString("login");
                                User.nom = use.getString("nom");
                                User.prenom =  use.getString("prenom");
                                User.mdp = use.getString("mdp");
                                User.poids = use.getInt("poid");
                                User.taille = use.getInt("taille");
                                User.age = use.getInt("age");
                                editor.putString("login",use.getString("login"));
                                editor.putString("password",use.getString("mdp"));
                                editor.putString("idfacebook",use.getString("idfacebook"));
                                editor.apply();
                            }
                            else{
                                showDialogRegister();
                            }
                        } catch (JSONException e) {
                            Log.e("JSONExeption", ""+e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ErrorResponse", error.getMessage());
                    }
                });
                queue.add(getData);

                Log.e("facebook", "succes" + loginResult.getAccessToken().getToken() + "id" + loginResult.getAccessToken().getExpires() + " data" + loginResult.getAccessToken().getUserId());
            }

            @Override
            public void onCancel() {
                Log.e("intra","facebook");
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e("facebook", "error" + exception.toString());
                // App code
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    /* Initialize popup dialog view and ui controls in the popup dialog. */
    private void initPopupViewControls()
    {
        // Get layout inflater object.
        LayoutInflater layoutInflater = LayoutInflater.from(Login.this);

        // Inflate the popup dialog from a layout xml file.
        View popupInputDialogView = layoutInflater.inflate(R.layout.popup_register, null);

        // Get user input edittext and button ui controls in the popup dialog.
        poidEditText = (EditText) popupInputDialogView.findViewById(R.id.poidEditText);
        tailleEditText = (EditText) popupInputDialogView.findViewById(R.id.tailleEditText);
        validerBtn = popupInputDialogView.findViewById(R.id.validerBtnPopup);
        annulerBtn = popupInputDialogView.findViewById(R.id.annulerBtnPopup);
    }


    public void showDialogRegister(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Login.this);
        // Set title, icon, can not cancel properties.
        alertDialogBuilder.setTitle("veuillez compléter votre inscription");
        alertDialogBuilder.setIcon(R.drawable.ic_launcher_background);
        alertDialogBuilder.setCancelable(false);

        // Init popup dialog view and it's ui controls.
        LayoutInflater layoutInflater = LayoutInflater.from(Login.this);

        // Inflate the popup dialog from a layout xml file.
        View popupInputDialogView = layoutInflater.inflate(R.layout.popup_register, null);

        // Get user input edittext and button ui controls in the popup dialog.
        poidEditText = (EditText) popupInputDialogView.findViewById(R.id.poidEditText);
        tailleEditText = (EditText) popupInputDialogView.findViewById(R.id.tailleEditText);
        validerBtn = popupInputDialogView.findViewById(R.id.validerBtnPopup);
        annulerBtn = popupInputDialogView.findViewById(R.id.annulerBtnPopup);

        // Set the inflated layout view object to the AlertDialog builder.
        alertDialogBuilder.setView(popupInputDialogView);

        // Create AlertDialog and show.
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        validerBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                int validerTest = 1 ;
                User.poids = Integer.parseInt(poidEditText.getText().toString());
                User.taille = Integer.parseInt(tailleEditText.getText().toString());
                if(poidEditText.getText().toString() == "" )
                {
                    poidEditText.setTextColor(R.color.redColor);
                    validerTest = 0 ;
                }
                else{
                    poidEditText.setTextColor(R.color.colorPrimaryDark);
                }
                if(tailleEditText.getText().toString() == "" )
                {
                    tailleEditText.setTextColor(R.color.redColor);
                    validerTest = 0 ;
                }
                else{
                    tailleEditText.setTextColor(R.color.colorPrimaryDark);
                }
                if(validerTest == 1){
                    url = Connexion.adresse + "/registerFacebook/";
                    url +=  User.nom+"/"+User.prenom +"/"+ User.poids +"/"+ User.taille +"/"+ User.age +"/" + User.idFacebook ;
                    //url = "http://192.168.1.10:3000/register/";
                    StringRequest getData = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                if (obj.getInt("success") == 1) {
                                    Intent mainApplication = new Intent(Login.this, MainActivity.class);
                                    startActivity(mainApplication);
                                    url = Connexion.adresse + "/verificationFaceBook/" + User.idFacebook ;
                                    //url = "http://192.168.1.10:3000/register/";
                                    StringRequest getData = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject obj = new JSONObject(response);
                                                if (obj.getInt("success") == 1) {
                                                    SharedPreferences.Editor editor = prefs.edit();
                                                    Intent mainApplication = new Intent(Login.this, MainActivity.class);
                                                    startActivity(mainApplication);
                                                    JSONArray row = new JSONArray(obj.getString("result"));
                                                    JSONObject use = row.getJSONObject(0);
                                                    User.id = use.getInt("id");
                                                    User.login = use.getString("login");
                                                    User.nom = use.getString("nom");
                                                    User.prenom =  use.getString("prenom");
                                                    User.mdp = use.getString("mdp");
                                                    User.poids = use.getInt("poid");
                                                    User.taille = use.getInt("taille");
                                                    User.age = use.getInt("age");
                                                    editor.putString("login",use.getString("login"));
                                                    editor.putString("password",use.getString("mdp"));
                                                    editor.putString("idfacebook",use.getString("idfacebook"));
                                                    editor.apply();
                                                }
                                            } catch (JSONException e) {
                                                Log.e("JSONExeption", ""+e.getMessage());
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
                            } catch (JSONException e) {
                                Log.e("JSONExeption", ""+e.getMessage());
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
        });
        annulerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAccessToken();
                alertDialog.cancel();
                LoginManager.getInstance().logOut();
            }
        });
    }

    private void deleteAccessToken() {
        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {

                if (currentAccessToken == null){
                    //User logged out
                    LoginManager.getInstance().logOut();
                }
            }
        };
    }

    public User getUser() {
        return user;
    }
}
