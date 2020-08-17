package com.esprit.sim.SlimFit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.sim.SlimFit.Classe.Connexion;
import com.esprit.sim.SlimFit.Classe.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CommentAdapter extends BaseAdapter {
    private Context context ;
    private JSONArray CommentListe ;
    LayoutInflater inflater;
    RequestQueue queue ;
    ListView listeCommentaire ;
    TextView comment ;

    public CommentAdapter(Context context, JSONArray CommentListe, ListView listeCommentaire,TextView comment){
        this.context = context;
        this.CommentListe = CommentListe;
        inflater = (LayoutInflater.from(context));
        queue = Volley.newRequestQueue(context);
        this.listeCommentaire = listeCommentaire ;
        this.comment = comment ;
    }

    @Override
    public int getCount() {
        return CommentListe.length();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = inflater.inflate(R.layout.activity_commentaire_item,null);
        TextView commentaireTxtItem ;
        Button commentBtn ;
        Button CommentUpdate ;
        Button CommentDelate ;
        TextView nameUserComment ;
        TextView dateUserCommentaire ;

        try {
            final JSONObject commentaire = (JSONObject) CommentListe.get(i);
            if (commentaire.getInt("replay") != 0){
                Log.e("normalment te5dem","aaaaaaaaaaaaaaaaaaa");
                view = inflater.inflate(R.layout.commentaire_replay_item,null);
            }
            commentaireTxtItem = (TextView) view.findViewById(R.id.commentaireTxtItem) ;
            commentBtn = (Button) view.findViewById(R.id.commentBtn) ;
            CommentUpdate = (Button) view.findViewById(R.id.CommentUpdate) ;
            CommentDelate = (Button) view.findViewById(R.id.deleteComment) ;
            nameUserComment = (TextView) view.findViewById(R.id.userNameComment) ;
            dateUserCommentaire = (TextView) view.findViewById(R.id.dateCommentaire) ;

            Log.d("commentaire :" , String.valueOf(commentaire));
            commentaireTxtItem.setText(commentaire.getString("body"));

            nameUserComment.setText(commentaire.getString("nom") + " " + commentaire.getString("prenom"));
            dateUserCommentaire.setText(commentaire.getString("date"));
            if(User.id != commentaire.getInt("idUser")){
                CommentUpdate.setVisibility(View.INVISIBLE);
                CommentDelate.setVisibility(View.INVISIBLE);
            }
            /*if(commentaire.getInt("replay") != 0){

                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) commentaireTxtItem.getLayoutParams();
                lp.setMargins(25,25,25,25);
                commentaireTxtItem.setLayoutParams(lp);

                //commentaireTxtItem.setPadding(15,0,0,0);
                //commentaireTxtItem.setLayoutParams(lp);
            }*/
            CommentDelate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url1 = null;
                    try {
                        url1 = Connexion.adresse + "/deleteComment/" + String.valueOf(commentaire.getInt("id"));
                        StringRequest getData1 = new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    int res = obj.getInt("success");
                                    if(res == 1 ) {
                                        String[] parts = comment.getText().toString().split(" ");
                                        comment.setText(String.valueOf(Integer.parseInt(parts[0]) - 1));

                                        //comment.setText(String.valueOf(Integer.parseInt(comment.getText().toString()) - 1));
                                        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                                        alertDialog.setTitle("Success");
                                        alertDialog.setMessage("Success de suppression de votre commentaire");
                                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        try {
                                                            fetchCommentaire(commentaire.getString("idaccueil"),context);
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        dialog.dismiss();
                                                    }
                                                });
                                        alertDialog.show();
                                    }
                                    else{
                                        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                                        alertDialog.setTitle("Error");
                                        alertDialog.setMessage("Erreur de mettre à jour votre commentaire");
                                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                        alertDialog.show();
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
                        queue.add(getData1);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("Commenter ce commentaire");
                    alert.setMessage("veuillez ajouter votre commentaire");
                    // Create TextView
                    final EditText inputComment = new EditText (context);
                    inputComment.setHint("Veuillez ecrire votre commentaire");
                    alert.setView(inputComment);

                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (!inputComment.getText().toString().equals(""))
                            {
                                String url = Connexion.adresse + "/commentaire/Replay/Ajouter";
                                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                                        new Response.Listener<String>()
                                        {
                                            @Override
                                            public void onResponse(String response) {
                                                // response
                                                try {
                                                    String[] parts = comment.getText().toString().split(" ");
                                                    comment.setText(String.valueOf(Integer.parseInt(parts[0]) + 1));
                                                    fetchCommentaire(commentaire.getString("idaccueil"),context);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                Log.d("Response", response);
                                            }
                                        },
                                        new Response.ErrorListener()
                                        {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                // error
                                                Log.d("Error.Response", ""+error);
                                            }
                                        }
                                ) {
                                    @Override
                                    protected Map<String, String> getParams()
                                    {
                                        //Log.e("el filename","fiih   "+filename);
                                        Map<String, String>  params = new HashMap<String, String>();
                                        params.put("body", inputComment.getText().toString());
                                        params.put("idUser", String.valueOf(User.id));
                                        try {
                                            if(commentaire.getInt("replay") == 0)
                                                params.put("replay", String.valueOf(commentaire.getInt("id")));
                                            else
                                                params.put("replay", String.valueOf(commentaire.getInt("replay")));
                                            params.put("idAccueil", String.valueOf(commentaire.getInt("idaccueil")));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        return params;
                                    }
                                };
                                queue.add(postRequest);
                            }
                            // Do something with value!
                        }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Canceled.
                            dialog.cancel();
                        }
                    });
                    alert.show();
                }
            });

            CommentUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("Modifier votre commentaire");
                    alert.setMessage("veuillez mettre a jour votre commentaire");
                    // Create TextView
                    final EditText inputComment = new EditText (context);
                    try {
                        inputComment.setText(commentaire.getString("body"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    alert.setView(inputComment);

                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (!inputComment.getText().toString().equals(""))
                            {
                                String url = Connexion.adresse + "/Commentaire/updateComment";
                                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                                        new Response.Listener<String>()
                                        {
                                            @Override
                                            public void onResponse(String response) {
                                                // response
                                                Log.d("Response", response);
                                                try {
                                                    fetchCommentaire(commentaire.getString("idaccueil"),context);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        },
                                        new Response.ErrorListener()
                                        {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                // error
                                                Log.d("Error.Response", ""+error);
                                            }
                                        }
                                ) {
                                    @Override
                                    protected Map<String, String> getParams()
                                    {
                                        //Log.e("el filename","fiih   "+filename);
                                        Map<String, String>  params = new HashMap<String, String>();
                                        params.put("body", inputComment.getText().toString());
                                        try {
                                            params.put("idComment", String.valueOf(commentaire.getInt("id")));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        return params;
                                    }
                                };
                                queue.add(postRequest);

                            }
                            // Do something with value!
                        }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Canceled.
                            dialog.cancel();
                        }
                    });
                    alert.show();

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view ;
    }

    private void fetchCommentaire(final String idAccueil, final Context popUpContext) {
        StringRequest getData = null;
        String urlComment = Connexion.adresse + "/getAllComment/" + idAccueil ;
        getData = new StringRequest(Request.Method.GET, urlComment , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //tabda
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);
                    final JSONArray arrayObjC = obj.getJSONArray("success");
                    StringRequest getData = null;
                    String urlComment = Connexion.adresse + "/getAllReplay/" + idAccueil ;
                    getData = new StringRequest(Request.Method.GET, urlComment , new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                JSONArray arrayObjReplay = obj.getJSONArray("success");
                                JSONArray allComment = new JSONArray() ;
                                int j = 0 ;
                                for(int i =0 ; i< arrayObjC.length() ;i++){
                                    allComment.put(arrayObjC.get(i));
                                    while(j< arrayObjReplay.length() && ((JSONObject)arrayObjC.get(i)).getInt("id") == ((JSONObject)arrayObjReplay.get(j)).getInt("replay")){
                                        allComment.put(arrayObjReplay.get(j)) ;

                                        j += 1;
                                    }
                                    if(i == arrayObjC.length()-1 ){
                                        CommentListe = allComment ;
                                        //CommentAdapter adapter = new CommentAdapter(popUpContext,allComment,listeCommentaire);
                                        listeCommentaire.setAdapter(CommentAdapter.this);
                                    }
                                }
                            } catch (JSONException e) {
                                Log.e("JSONExeption", e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("ErrorResponse", ""+ error.getMessage());
                        }
                    });
                    queue.add(getData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //toufa


/*
                            JSONObject obj = new JSONObject(response);
                            arrayObj = obj.getJSONArray("success");
                            for(int i =0 ; i< arrayObj.length() ;i++){
                                JSONObject accueil = (JSONObject) arrayObj.get(i);
                                Log.e("aaaaaaaaaaaaaaaaaaaaaaa",accueil.getString("body"));
                            }
                            CommentAdapter adapter = new CommentAdapter(popupInputDialogView.getContext(),arrayObj);
                            listeCommentaire.setAdapter(adapter);
*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ErrorResponse", ""+ error.getMessage());
            }
        });
        queue.add(getData);

    }

}
