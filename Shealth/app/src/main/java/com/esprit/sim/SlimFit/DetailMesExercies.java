package com.esprit.sim.SlimFit;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class DetailMesExercies extends Fragment {
    public static final String UPLOAD_URL = Connexion.adresse + "/UploadImage/exercice";
    private ProgressBar progressBar;
    private TextView txtPercentage;
    private String filename;
    long totalSize = 0;
    RequestQueue queue;
    private String Video;
    private int idExercice, IdCat;
    private String nomExercice;
    private String descriptionExercice;
    private String nomCatExercice;
    private Spinner spinner;
    String url1, url, url2, url3, url5;
    WebView webview;
    TextView nom, description;
    ImageView image, imageView;
    CircleImageView modifierImage;
    RequestQueue queue1;
    FloatingActionButton btnShare;
    Button Modifier, confirmer;
    MaterialEditText nomamodifer, descriptionamodifier;
    List<String> Liste = new ArrayList<>();
    List<JSONObject> ListeCategorie = new ArrayList<>();
    int tmpBranch;
    String imagenameinitial;
    String CatgorieFinal;
    public Boolean Modification = false;
    //Image request code
    private int PICK_IMAGE_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePath;

    public DetailMesExercies() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_mes_exercies, container, false);
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
        Modifier = view.findViewById(R.id.modifiermonexercie);
        nom.setText(nomExercice);
        description.setText(descriptionExercice);
        StringRequest getData = new StringRequest(Request.Method.GET, Connexion.adresse + "/exercice/" + idExercice, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray arrayObj = obj.getJSONArray("success");
                    JSONObject Obj = (JSONObject) arrayObj.get(0);
                    imagenameinitial = Obj.getString("image");
                    ImageRequest imageLoad = new ImageRequest(Connexion.adresse + "/Ressources/Exercice/" + Obj.getString("image"), new Response.Listener<Bitmap>() {
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
        Modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Modifier Programme");
                alertDialog.setMessage("Veuillez remplir tous les champs");
                View layout_modifier = inflater.inflate(R.layout.updateexercielayout, null);
                requestStoragePermission();
                nomamodifer = layout_modifier.findViewById(R.id.nomamodifer);
                descriptionamodifier = layout_modifier.findViewById(R.id.descriptionamodifier);
                confirmer = layout_modifier.findViewById(R.id.modifierconfirmer);
                nomamodifer.setText(nomExercice);
                progressBar = layout_modifier.findViewById(R.id.progressBarmodifierexercie);
                descriptionamodifier.setText(descriptionExercice);
                modifierImage = layout_modifier.findViewById(R.id.modifierImage);
                imageView = layout_modifier.findViewById(R.id.imageView);
                spinner = layout_modifier.findViewById(R.id.spinnermodifierexercice);
                txtPercentage = layout_modifier.findViewById(R.id.txtPercentage);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_alert_text, SpinnerListe());
                adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
                spinner.setAdapter(adapter);
                alertDialog.setView(layout_modifier);
                alertDialog.show();
                confirmer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ModifierExercie(imagenameinitial);
                        Log.e("Modification", "feha" + Modification.toString());
                        if (Modification == true) {
                            new UploadFileToServer().execute();
                        }
                        DetailMesExercies DetailMesExerciesFragment = new DetailMesExercies();
                        DetailMesExerciesFragment.setIdExercice(idExercice);
                        DetailMesExerciesFragment.setNomExercice(nomamodifer.getText().toString());
                        DetailMesExerciesFragment.setDescriptionExercice(descriptionamodifier.getText().toString());
                        DetailMesExerciesFragment.setNomCatExercice(CatgorieFinal);
                        getFragmentManager().beginTransaction().replace(R.id.fragment, DetailMesExerciesFragment).commit();

                    }
                });
                modifierImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showFileChooser();
                    }
                });
                alertDialog.setCancelable(true);
            }


        });
        return view;
    }

    private void ModifierExercie(final String f) {
        queue = Volley.newRequestQueue(getContext());
        url = Connexion.adresse + "/exercice/Update";
        if(spinner.getSelectedItem().toString() != "Veuillez choisir une categorie") {
            int pos = spinner.getSelectedItemPosition() - 1;
            JSONObject object = ListeCategorie.get(pos);
            try {
                IdCat = object.getInt("id");
                Log.e("el categorie id","heya "+IdCat);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (nomamodifer.getText().length() != 0 && descriptionamodifier.getText().length() != 0) {
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.d("Response", response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("Error.Response", "" + error);
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Log.e("nom :", "aaaaa" + nomamodifer.getText().toString());
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("nom", nomamodifer.getText().toString());
                    params.put("description", descriptionamodifier.getText().toString());
                    params.put("category", String.valueOf(IdCat));
                    params.put("image", imagenameinitial);
                    params.put("id", String.valueOf(idExercice));
                    return params;
                }

            };
            queue.add(postRequest);
        }
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

    public String getNomCatExercice() {
        return nomCatExercice;
    }

    public void setNomCatExercice(String nomCatExercice) {
        this.nomCatExercice = nomCatExercice;
    }

    public List<String> SpinnerListe() {
        queue = Volley.newRequestQueue(getContext());
        Liste = new ArrayList<String>();
        ListeCategorie = new ArrayList<JSONObject>();
        Liste.add("Veuillez choisir une categorie");
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
                        ListeCategorie.add(exec);
                        Liste.add(exec.getString("name"));
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
        return Liste;

    }

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            progressBar.setProgress(progress[0]);

            // updating percentage value
            txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(UPLOAD_URL);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(getPath(filePath));
                // Adding file data to http body
                entity.addPart("file", new FileBody(sourceFile));


                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);

                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            imagenameinitial = result;
            Log.e("apres execution", "Response from server: " + result);
            // showing the server response in an alert dialog
            showAlert("Modifier");

            super.onPostExecute(result);
        }

    }

    /**
     * Method to show alert dialog
     */
    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(message).setTitle("Avec success")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
                Modification = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getActivity().getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(getActivity(), "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(getActivity(), "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }


}

// TODO: Rename method, update argument and hook method into UI event


