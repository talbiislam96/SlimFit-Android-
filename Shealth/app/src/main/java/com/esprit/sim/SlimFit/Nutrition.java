package com.esprit.sim.SlimFit;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esprit.sim.SlimFit.Classe.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class Nutrition extends Fragment {


    public Nutrition() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_nutrition, container, false);

        Integer ideal= User.taille-100;


        TextView textEq= view.findViewById(R.id.equation);


        int correct=0;
        String s="Vous avez ";
        if(User.poids>ideal){
            correct=1;
            s+=" un surpoids, vous devez suivre le regime slimFit";
            textEq.setText("Fruits et légumes.\n" +
                    "Pomme de terre et maïs.\n" +
                    "Céréales complètes.\n" +
                    "Légumineuses.\n" +
                    "Poissons blancs et fruits de mer.\n" +
                    "Jambon de dinde.\n" +
                    "Lait écrémé et de soja.\n" +
                    "Bouillons de légumes.");
        }else if(User.poids<ideal){
            correct=2;
            s+="la maigreur , vous devez suivre le regime slimFit";
            textEq.setText("Volailles.\n" +
                    "Oeufs.\n" +
                    "Poissons.\n" +
                    "Viandes maigres.\n" +
                    "Produits laitiers 0%\n" +
                    "Légumineuses.\n" +
                    "Substituts de viande à base de soja et tofu.\n" +
                    "Beurres d'oléagineux (à limiter à 1 cuillère à café par jour)\n");
        }else{
            s+=" un poids idéal";
        }
        TextView res= view.findViewById(R.id.statu);
        res.setText(s);




        return view;
    }

}
