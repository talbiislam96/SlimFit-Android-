package com.esprit.sim.SlimFit;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemExercice extends RecyclerView.ViewHolder {

    private TextView carTitleText = null;

    private ImageView carImageView = null;

    public ItemExercice(View itemView) {
        super(itemView);

        if(itemView != null)
        {
            carTitleText = (TextView)itemView.findViewById(R.id.card_view_image_title);

            carImageView = (ImageView)itemView.findViewById(R.id.card_view_image);
        }
    }

    public TextView getCarTitleText() {
        return carTitleText;
    }

    public ImageView getCarImageView() {
        return carImageView;
    }

}
