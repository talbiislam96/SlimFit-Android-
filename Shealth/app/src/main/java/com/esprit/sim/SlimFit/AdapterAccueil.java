package com.esprit.sim.SlimFit;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.sim.SlimFit.Classe.Connexion;
import com.esprit.sim.SlimFit.Classe.User;
import com.facebook.CallbackManager;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;

import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdapterAccueil extends BaseAdapter {
    private Context context ;
    private JSONArray accueils ;
    LayoutInflater inflater;
    RequestQueue queue ;
    private RequestQueue queue1 ;
    private JSONArray arrayObj ;
    private JSONObject Obj ;
    ImageView image;
    private TextView nomAccueil,descriptionAccueil ;
    private ShareButton shareButton ;
    private LinearLayout shareFB ;
    private ShareDialog shareDialog ;
    private CallbackManager callbackManager;
    ListView listeCommentaire ;

    Handler handler = new Handler();

    public AdapterAccueil(Context context, JSONArray nomExercices,ShareDialog shareDialog) {
        this.shareDialog = shareDialog ;
        this.context = context;
        this.accueils = nomExercices;
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return accueils.length();
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
        view = inflater.inflate(R.layout.activity_cell_prototype_accueil,null);
        queue = Volley.newRequestQueue(view.getContext());
        queue1 = Volley.newRequestQueue(view.getContext());
        shareButton = (ShareButton)view.findViewById(R.id.fb_share_button);
        shareFB = (LinearLayout) view.findViewById(R.id.btnPartege);
        callbackManager = CallbackManager.Factory.create();
        final TextView commentaire =  view.findViewById(R.id.commentaireTxtAccueil);
        JSONObject accueil = null;
        try {
            accueil = (JSONObject) accueils.get(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // comment alertView
        final JSONObject finalAccueil = accueil;
        commentaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                // Set title, icon, can not cancel properties.
                alertDialogBuilder.setTitle("Liste des commentaires");
                alertDialogBuilder.setIcon(R.drawable.comment);
                alertDialogBuilder.setCancelable(true);

                alertDialogBuilder.setNeutralButton("Exit", new DialogInterface.OnClickListener() { // define the 'Cancel' button
                    public void onClick(DialogInterface dialog, int which) {
                        //Either of the following two lines should work.
                        dialog.cancel();
                        //dialog.dismiss();
                    }
                });

                // Init popup dialog view and it's ui controls.
                LayoutInflater layoutInflater = LayoutInflater.from(context);

                // Inflate the popup dialog from a layout xml file.
                final View popupInputDialogView = layoutInflater.inflate(R.layout.popup_commentaire, null);
                final TextView commentaireAddTxt = (TextView) popupInputDialogView.findViewById(R.id.commentaireTxt) ;
                Button addComment = (Button) popupInputDialogView.findViewById(R.id.addCommentaire) ;
                listeCommentaire = (ListView) popupInputDialogView.findViewById(R.id.listeCommentaire) ;
                String idAc = null;
                try {
                    idAc = String.valueOf(finalAccueil.getInt("id"));
                    fetchCommentaire(idAc,popupInputDialogView.getContext(),commentaire);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String finalIdAc = idAc;
                addComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if( commentaireAddTxt.getText().toString() != ""){

                            String url = Connexion.adresse + "/commentaire/Ajouter";
                            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                                    new Response.Listener<String>()
                                    {
                                        @Override
                                        public void onResponse(String response) {
                                            // response
                                            commentaireAddTxt.setText("");
                                            String[] parts = commentaire.getText().toString().split(" ");
                                            commentaire.setText(String.valueOf(Integer.parseInt(parts[0]) + 1));
                                            fetchCommentaire(finalIdAc,popupInputDialogView.getContext(),commentaire);
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
                                    params.put("body", commentaireAddTxt.getText().toString());
                                    params.put("idUser", String.valueOf(User.id));
                                    try {
                                        params.put("idAccueil", String.valueOf(finalAccueil.getInt("id")));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    return params;
                                }
                            };
                            queue.add(postRequest);
                        }
                    }
                });

                alertDialogBuilder.setView(popupInputDialogView);

                // Create AlertDialog and show.
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        //shareDialog = new ShareDialog();
        try {
            accueil = (JSONObject) accueils.get(i);
            TextView nomText = view.findViewById(R.id.NomUserAccueil) ;
            TextView dateText = view.findViewById(R.id.DatePubAccueil) ;
            final TextView likeText = view.findViewById(R.id.likeTxt) ;
            final TextView likeBtn = view.findViewById(R.id.likeBtn) ;
            nomAccueil = view.findViewById(R.id.nomPubAccueil) ;
            descriptionAccueil = view.findViewById(R.id.descriptionUser) ;
            image = view.findViewById(R.id.imageAccueil);
            final ImageView likeImageBtn = view.findViewById(R.id.likeImageBtn);

            final JSONObject finalAccueil1 = accueil;

            String readLike = Connexion.adresse + "/aime/" + String.valueOf(finalAccueil1.getInt("id") + "/" + String.valueOf(User.id));
            //String url = "http://192.168.1.10:3000/challenge/" + String.valueOf(idetranger);
            StringRequest getDataLikeAdd = new StringRequest(Request.Method.GET, readLike, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject obj = new JSONObject(response);
                        int countAimeUser = obj.getInt("success");
                        if(countAimeUser == 0 ){
                            likeImageBtn.setImageResource(R.drawable.untitled);
                        }
                        else{
                            likeImageBtn.setImageResource(R.drawable.untitled1);
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
            queue.add(getDataLikeAdd);


            likeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String urlLikeAdd = null;
                    try {
                        urlLikeAdd = Connexion.adresse + "/aime/" + String.valueOf(finalAccueil1.getInt("id") + "/" + String.valueOf(User.id));
                        //String url = "http://192.168.1.10:3000/challenge/" + String.valueOf(idetranger);
                        StringRequest getDataLikeAdd = new StringRequest(Request.Method.GET, urlLikeAdd, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    int countAimeUser = obj.getInt("success");
                                    if(countAimeUser == 0 ){
                                        String urlLike = Connexion.adresse + "/aime/Ajouter/" + String.valueOf(finalAccueil.getInt("id") + "/" + String.valueOf(User.id));
                                        //String url = "http://192.168.1.10:3000/challenge/" + String.valueOf(idetranger);
                                        StringRequest getDataLike = new StringRequest(Request.Method.GET, urlLike, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                likeText.setText(String.valueOf(Integer.parseInt(likeText.getText().toString()) + 1));
                                                likeImageBtn.setImageResource(R.drawable.untitled1);
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Log.e("ErrorResponse", error.getMessage());
                                            }
                                        });
                                        queue.add(getDataLike);
                                    }
                                    else{
                                        String urlLike = Connexion.adresse + "/aime/Delete/" + String.valueOf(finalAccueil.getInt("id") + "/" + String.valueOf(User.id));
                                        //String url = "http://192.168.1.10:3000/challenge/" + String.valueOf(idetranger);
                                        StringRequest getDataLike = new StringRequest(Request.Method.GET, urlLike, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                likeText.setText(String.valueOf(Integer.parseInt(likeText.getText().toString()) - 1));
                                                likeImageBtn.setImageResource(R.drawable.untitled);
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Log.e("ErrorResponse", error.getMessage());
                                            }
                                        });
                                        queue.add(getDataLike);

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
                            queue.add(getDataLikeAdd);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });
            countAime(likeText,String.valueOf(accueil.getInt("id")));

            /*String urlLike = Connexion.adresse + "/countAime/" + String.valueOf(accueil.getInt("id"));
            //String url = "http://192.168.1.10:3000/challenge/" + String.valueOf(idetranger);
            StringRequest getDataLike = new StringRequest(Request.Method.GET, urlLike, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject obj = new JSONObject(response);
                        JSONObject countObj = obj.getJSONObject("success");
                        int countAime = countObj.getInt("result");
                        Log.e("Like : " , String.valueOf(countAime));
                        //image.setImageURI(Uri.parse("http://192.168.43.35:3000/Ressources/Challenge/"+niveau.getString("image")));
                        likeText.setText(String.valueOf(countAime));

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
            queue.add(getDataLike);

*/
            String url1 = Connexion.adresse + "/countComment/" + String.valueOf(accueil.getInt("id"));
            //String url = "http://192.168.1.10:3000/challenge/" + String.valueOf(idetranger);
            StringRequest getData1 = new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject obj = new JSONObject(response);
                        JSONObject countObj = obj.getJSONObject("success");
                        int countComment = countObj.getInt("result");
                        Log.e("commentaire : " , String.valueOf(countComment));
                        //image.setImageURI(Uri.parse("http://192.168.43.35:3000/Ressources/Challenge/"+niveau.getString("image")));
                        commentaire.setText(String.valueOf(countComment) + " commentaire(s)");

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

            nomText.setText(accueil.getString("nom") + " " + accueil.getString("prenom"));
            dateText.setText(accueil.getString("dateaccueil"));
            String url = Connexion.adresse + "/exercice/"+accueil.getInt("idetranger");
            if(accueil.getString("typeaccueil").equals("challenge")){
                int nbr = challengeCell(accueil.getInt("idetranger"));
            }
            else if (accueil.getString("typeaccueil").equals("programme")){
                int nbr = Programme(accueil.getInt("idetranger"));

            }
            else {
                if(accueil.getString("source").equals("local")){
                    ExerciceLocal(accueil.getInt("idetranger"));
                }
                else if( !accueil.getString("source").equals("local") ){
                    url = "https://wger.de/api/v2/exercise/?name="+accueil.getString("source")+"&id="+String.valueOf(accueil.getInt("idetranger"));
                    StringRequest getData = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                Log.e("aaaaaaaaaaaaa",response);
                                arrayObj = obj.getJSONArray("results");
                                JSONObject accueil = (JSONObject) arrayObj.get(0);
                                Log.e("aaaaaaaaaaa ",accueil.getString("name"));
                                nomAccueil.setText("Exercice : " + accueil.getString("name"));
                                descriptionAccueil.setText(accueil.getString("description"));
                                StringRequest getData = new StringRequest(Request.Method.GET, "https://wger.de/api/v2/exerciseimage/?exercise=" + String.valueOf(accueil.getString("id")) , new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject obj = new JSONObject(response);
                                            arrayObj = obj.getJSONArray("results");
                                            Obj = (JSONObject) arrayObj.get(0);

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
                                        Log.e("ErrorResponse", error.getMessage());
                                    }
                                });
                                queue.add(getData);
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


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }

    private void fetchCommentaire(final String idAccueil, final Context popUpContext, final TextView comment) {
        StringRequest getData = null;
        String urlComment = Connexion.adresse + "/getAllComment/" + idAccueil ;
        Log.e("url : ", urlComment) ;
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
                    Log.e("url : ", urlComment) ;
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
                                        Log.e("listecomm", String.valueOf(allComment.toString())) ;
                                        CommentAdapter adapter = new CommentAdapter(popUpContext,allComment,listeCommentaire,comment);
                                        listeCommentaire.setAdapter(adapter);
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

    private int challengeCell(int idetranger) {

        String url = Connexion.adresse + "/challenge/" + String.valueOf(idetranger);
        //String url = "http://192.168.1.10:3000/challenge/" + String.valueOf(idetranger);
        StringRequest getData = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    Obj = obj.getJSONObject("success");
                    JSONObject niveau = Obj.getJSONObject("niveau");
                    //image.setImageURI(Uri.parse("http://192.168.43.35:3000/Ressources/Challenge/"+niveau.getString("image")));
                    nomAccueil.setText("félicitation vous avez complété le niveau : " + niveau.getString("nom"));
                    descriptionAccueil.setText("vous avez atteint les " + String.valueOf(niveau.getInt("seuil") + "pas"));
                    int imgRes = loadImage(Connexion.adresse + "/Ressources/Challenge/"+niveau.getString("image"),niveau.getString("nom"),niveau.getInt("seuil")) ;


                } catch (JSONException e) {
                    Log.e("JSONExeption", e.getMessage());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ErrorResponse", error.getMessage());
            }
        });
        queue.add(getData);
        return 0 ;
    }

    public int loadImage(final String url , final String name , final int niveau) throws InterruptedException {
        ImageRequest imageLoad = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                // callback
                image.setImageBitmap(response);
                ShareOpenGraphObject object = new ShareOpenGraphObject.Builder()
                        .putString("og:type", "article")
                        .putString("fb:app_id","355248931721304")
                        //.putString("og:url", mShareParams.getUrl())
                        .putString("og:title", "félicitation vous avez complété le niveau : " + name)
                        .putString("og:image", url)
                        .putString("og:image:type", "png")
                        .build();
                ShareOpenGraphAction action = new ShareOpenGraphAction.Builder()
                        .setActionType("news.publishes")
                        .putObject("article", object)
                        .build();
                final ShareOpenGraphContent content = new ShareOpenGraphContent.Builder()
                        .setPreviewPropertyName("article")
                        .setAction(action).build();

                shareButton.setShareContent(content);

                // Set a click listener on the custom facebook share button
                shareFB.setOnClickListener(new View.OnClickListener() {
                    // The code in this method will be executed when the Share  on Facebook custom button is clicked on
                    @Override
                    public void onClick(View view) {
                        shareButton.performClick();
                        shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
                    }
                });
            }
        }, 100, 100, null, null);
        // 100 is your custom Size before Downloading the Image.
        Object lock = new Object();

        queue1.add(imageLoad);
        return 1 ;
    }

    public int ExerciceLocal(int idetranger){

        String url = Connexion.adresse + "/exercice/" + String.valueOf(idetranger);
        //String url = "http://192.168.1.10:3000/exercice/" + String.valueOf(idetranger);
        StringRequest getData = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    Log.e("aaaaaaaaaaaaa",response);
                    arrayObj = obj.getJSONArray("success");
                    JSONObject accueil = (JSONObject) arrayObj.get(0);
                    Log.e("aaaaaaaaaaa ",accueil.getString("nom"));
                    nomAccueil.setText(accueil.getString("nom"));
                    descriptionAccueil.setText(accueil.getString("description"));
                    int a = readImageLocal(accueil.getString("image"),accueil.getString("nom"));
                    //image.setImageURI(Uri.parse("http://192.168.1.10:3000/Ressources/Challenge/"+accueil.getString("image")));

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
        return 1 ;
    }

    public int readImageLocal(String imageName, final String name){

        final String url = Connexion.adresse + "/Ressources/Exercice/"+imageName ;
        //String url = "http://192.168.1.10:3000/Ressources/Exercice/"+imageName ;
        ImageRequest imageLoad = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                // callback
                image.setImageBitmap(response);
                ShareOpenGraphObject object = new ShareOpenGraphObject.Builder()
                        .putString("og:type", "article")
                        .putString("fb:app_id","355248931721304")
                        //.putString("og:url", mShareParams.getUrl())
                        .putString("og:title", "Exercice sportif " + name)
                        .putString("og:image", url)
                        .putString("og:image:type", "png")
                        .build();
                ShareOpenGraphAction action = new ShareOpenGraphAction.Builder()
                        .setActionType("news.publishes")
                        .putObject("article", object)
                        .build();
                final ShareOpenGraphContent content = new ShareOpenGraphContent.Builder()
                        .setPreviewPropertyName("article")
                        .setAction(action).build();

                shareButton.setShareContent(content);

                // Set a click listener on the custom facebook share button
                shareFB.setOnClickListener(new View.OnClickListener() {
                    // The code in this method will be executed when the Share  on Facebook custom button is clicked on
                    @Override
                    public void onClick(View view) {
                        shareButton.performClick();
                        shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
                    }
                });

            }
        }, 100, 100, null, null);
        // 100 is your custom Size before Downloading the Image.
        queue1.add(imageLoad);
        return 1;
    }

    public int Programme (int idetranger){

        String url = Connexion.adresse + "/Programme/" + String.valueOf(idetranger);
        //String url = "http://192.168.1.10:3000/exercice/" + String.valueOf(idetranger);
        StringRequest getData = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    Log.e("aaaaaaaaaaaaa",response);
                    arrayObj = obj.getJSONArray("success");
                    JSONObject accueil = (JSONObject) arrayObj.get(0);
                    Log.e("aaaaaaaaaaa ",accueil.getString("nom"));
                    nomAccueil.setText(accueil.getString("nom"));
                    descriptionAccueil.setText(accueil.getString("description"));
                    int a = readImageProgramme(accueil.getString("image"),accueil.getString("nom"));
                    //image.setImageURI(Uri.parse("http://192.168.1.10:3000/Ressources/Challenge/"+accueil.getString("image")));

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
        return 1 ;
    }

    public int readImageProgramme (String imageName, final String name){

        final String url = Connexion.adresse + "/Ressources/Programme/"+imageName ;
        //String url = "http://192.168.1.10:3000/Ressources/Exercice/"+imageName ;
        ImageRequest imageLoad = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                // callback
                image.setImageBitmap(response);
                ShareOpenGraphObject object = new ShareOpenGraphObject.Builder()
                        .putString("og:type", "article")
                        .putString("fb:app_id","355248931721304")
                        //.putString("og:url", mShareParams.getUrl())
                        .putString("og:title", "Programme sportif " + name)
                        .putString("og:image", url)
                        .putString("og:image:type", "png")
                        .build();
                ShareOpenGraphAction action = new ShareOpenGraphAction.Builder()
                        .setActionType("news.publishes")
                        .putObject("article", object)
                        .build();
                final ShareOpenGraphContent content = new ShareOpenGraphContent.Builder()
                        .setPreviewPropertyName("article")
                        .setAction(action).build();

                shareButton.setShareContent(content);

                // Set a click listener on the custom facebook share button
                shareFB.setOnClickListener(new View.OnClickListener() {
                    // The code in this method will be executed when the Share  on Facebook custom button is clicked on
                    @Override
                    public void onClick(View view) {
                        shareButton.performClick();
                        shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
                    }
                });

            }
        }, 100, 100, null, null);
        // 100 is your custom Size before Downloading the Image.
        queue1.add(imageLoad);
        return 1;
    }

    public int ExerciceApi(int idetranger , String source){
        String url = "https://wger.de/api/v2/exercise/?name=" + source + "&id=" + String.valueOf(idetranger);
        StringRequest getData = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    Log.e("aaaaaaaaaaaaa",response);
                    arrayObj = obj.getJSONArray("results");
                    JSONObject accueil = (JSONObject) arrayObj.get(0);
                    Log.e("aaaaaaaaaaa ",accueil.getString("name"));
                    nomAccueil.setText("Exercice : " + accueil.getString("name"));
                    descriptionAccueil.setText(accueil.getString("description"));
                    int a = readImageServer(accueil.getInt("id"),accueil.getString("name"));
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
        return 1;
    }

    public int readImageServer(final int id, final String name){
        StringRequest getData = new StringRequest(Request.Method.GET, "https://wger.de/api/v2/exerciseimage/?exercise=" + String.valueOf(id) , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    arrayObj = obj.getJSONArray("results");
                    Obj = (JSONObject) arrayObj.get(0);

                    ImageRequest imageLoad = new ImageRequest(Obj.getString("image"), new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            // callback
                            image.setImageBitmap(response);
                            ShareOpenGraphObject object = new ShareOpenGraphObject.Builder()
                                    .putString("og:type", "article")
                                    .putString("fb:app_id","355248931721304")
                                    //.putString("og:url", mShareParams.getUrl())
                                    .putString("og:title", "Exercice sportif" + name)
                                    .putString("og:image", "https://wger.de/api/v2/exerciseimage/?exercise=" + String.valueOf(id) )
                                    .putString("og:image:type", "png")
                                    .build();
                            ShareOpenGraphAction action = new ShareOpenGraphAction.Builder()
                                    .setActionType("news.publishes")
                                    .putObject("article", object)
                                    .build();
                            final ShareOpenGraphContent content = new ShareOpenGraphContent.Builder()
                                    .setPreviewPropertyName("article")
                                    .setAction(action).build();

                            shareButton.setShareContent(content);

                            // Set a click listener on the custom facebook share button
                            shareFB.setOnClickListener(new View.OnClickListener() {
                                // The code in this method will be executed when the Share  on Facebook custom button is clicked on
                                @Override
                                public void onClick(View view) {
                                    shareButton.performClick();
                                    shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
                                }
                            });

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
                Log.e("ErrorResponse", error.getMessage());
            }
        });
        queue.add(getData);
        return 1;
    }

    public void countAime(final TextView likeText , String accueilId){
        String urlLike = Connexion.adresse + "/countAime/" + accueilId;
        //String url = "http://192.168.1.10:3000/challenge/" + String.valueOf(idetranger);
        StringRequest getDataLike = new StringRequest(Request.Method.GET, urlLike, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONObject countObj = obj.getJSONObject("success");
                    int countAime = countObj.getInt("result");
                    Log.e("Like : " , String.valueOf(countAime));
                    //image.setImageURI(Uri.parse("http://192.168.43.35:3000/Ressources/Challenge/"+niveau.getString("image")));
                    likeText.setText(String.valueOf(countAime));

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
        queue.add(getDataLike);
    }

}
