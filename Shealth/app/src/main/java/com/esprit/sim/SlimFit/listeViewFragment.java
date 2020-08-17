package com.esprit.sim.SlimFit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


public class listeViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    public listeViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_liste_view, container, false);

        ListView listView = (ListView)view.findViewById(R.id.listviewEquipe);

        /*Equipe e1 = new Equipe("Tunisie","Egypt","Tunisie","tn","egypt");
        Equipe e2 = new Equipe("Tunisie","Uraguay","Tunisie","tn","urg");
        Equipe e3 = new Equipe("Tunisie","France","France","tn","france");
        Equipe e4 = new Equipe("France","Uraguay","Uraguay","france","urg");
        Equipe e5 = new Equipe("Egypt","Uraguay","Egypt","egypt","urg");
        Equipe[] eq = new Equipe[]{e1,e2,e3,e4,e5};
        AdapterListe adapter = new AdapterListe(getActivity(),eq);
        listView.setAdapter(adapter);*/
        return view;
    }

}
