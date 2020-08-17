package com.esprit.sim.SlimFit;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {


    //vars
    private ArrayList<JSONObject> exercice = new ArrayList<>();
    private Context mContext;
    RequestQueue queue;

    public RecyclerViewAdapter(Context context, ArrayList<JSONObject> exercices) {
        exercice = exercices;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        queue = Volley.newRequestQueue(mContext);
        final JSONObject ex = exercice.get(position);
        try {
            holder.name.setText(ex.getString("nom"));
            ImageRequest imageLoad = new ImageRequest(ex.getString("image"), new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    // callback
                    holder.image.setImageBitmap(response);
                }
            }, 100, 100, null, null);
            queue.add(imageLoad);
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Log.e("el exercice id ","iss"+ ex.getString("id"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            Log.e("JSONExeption", e.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return exercice.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView image;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageexer);
            name = itemView.findViewById(R.id.nomexerprog);
        }
    }
}