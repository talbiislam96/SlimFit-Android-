package com.esprit.sim.SlimFit;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeHolder> {
    private JSONArray mDataset;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ChallengeAdapter(JSONArray myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ChallengeHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // create a new view
        LayoutInflater layoutInflater = LayoutInflater.from((parent.getContext()));
        View view = layoutInflater.inflate(R.layout.challenge_item, parent, false);

        /*TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view, parent, false);

        MyViewHolder vh = new MyViewHolder(v);*/

        return new ChallengeHolder(view);
    }

    @Override
    public void onBindViewHolder(ChallengeHolder holder, int position) {
        try {
            holder.display((JSONObject) mDataset.get(position),position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length();
    }
}
