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
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.sim.SlimFit.Classe.Connexion;
import com.esprit.sim.SlimFit.Classe.User;
import com.rengwuxian.materialedittext.MaterialEditText;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class AjouterExercice extends Fragment {
    View view ;
    public static final String UPLOAD_URL = Connexion.adresse + "/UploadImage/exercice";
    private String filename;
    private Button Confirmer,annuler;
    private ImageButton buttonUpload;
    private ImageView imageView;
    private MaterialEditText nom,description,urlV;
    private ProgressBar progressBar;
    private TextView txtPercentage;
    private String url,url2,url3;
    private Spinner spinner;
    long totalSize = 0;
    private  int IdCat ;
    List<String> Liste = new ArrayList<>();
    List<JSONObject> ListeCategorie = new ArrayList<>();


    //Image request code
    private int PICK_IMAGE_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePath;
    RequestQueue queue;

    public AjouterExercice() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_ajouter_exercice, container, false);
        Confirmer = view.findViewById(R.id.ajouter);
        buttonUpload = view.findViewById(R.id.imageButton2);
        annuler = view.findViewById(R.id.annuler);
        nom = view.findViewById(R.id.name);
        description = view.findViewById(R.id.description);
        imageView = view.findViewById(R.id.imageView5);
        urlV = view.findViewById(R.id.url);
        progressBar = view.findViewById(R.id.progressBar);
        txtPercentage = view.findViewById(R.id.pourcentage);
        requestStoragePermission();
        spinner = view.findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_alert_text, SpinnerListe());
        adapter.notifyDataSetChanged();
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinner.setAdapter(adapter);
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });
        Confirmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new UploadFileToServer().execute();
            }
        });
        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment,new MesExercices()).commit();
            }
        });
        return view;
    }

    /*
     * This is the method responsible for image upload
     * We need the full image path and the name for the image in this method
     * */
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
                Log.e("filpath","fih    " + getPath(filePath));
                filename = sourceFile.getName();
                Log.e("filename melowel","fih    "+filename);

                // Adding file data to http body
                entity.addPart("file",new FileBody(sourceFile));

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
            Log.e("apres execution", "Response from server: " + result);
            AjouterExercice(result);
            // showing the server response in an alert dialog
            showAlert(result);

            super.onPostExecute(result);
        }

    }

    /**
     * Method to show alert dialog
     * */
    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(message).setTitle("Exercice Ajouter")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MesExercices MesExercicesFragment = new MesExercices();
                        getFragmentManager().beginTransaction().replace(R.id.fragment,MesExercicesFragment).commit();
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

    public void AjouterExercice(final String filename){
        queue = Volley.newRequestQueue(getContext());
        Log.e("el filename","fiih  9bal l'ajout " + filename);

        Toast toast1 = Toast.makeText(getContext(),
                "Image : " + filename,
                Toast.LENGTH_SHORT);

        toast1.show();

        String text = spinner.getSelectedItem().toString();
        if(text != "Veuillez choisir une categorie") {
            int pos = spinner.getSelectedItemPosition() - 1;
            JSONObject object = ListeCategorie.get(pos);
            try {
                IdCat = object.getInt("id");
                Log.e("el categorie id","heya "+IdCat);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
            if (nom.getText().toString().equalsIgnoreCase("") || description.getText().toString().equalsIgnoreCase("")) {
            Toast toast = Toast.makeText(getContext(),
                    "Veillez remplir tous les champs",
                    Toast.LENGTH_SHORT);

            toast.show();
        } else {
            url = Connexion.adresse + "/exercice/Ajouter";
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            // response
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
                    Log.e("el filename","fiih  9bal l'ajout "+filename);
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("nom", nom.getText().toString());
                    params.put("description", description.getText().toString());
                    params.put("user", User.id.toString());
                    params.put("image", filename.substring(1,filename.length() - 1));
                    params.put("video", urlV.getText().toString());
                    params.put("category", String.valueOf(IdCat));
                    return params;
                }
            };
            queue.add(postRequest);
            }
    }

    public List<String> SpinnerListe(){
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
        return  Liste;

    }
}
