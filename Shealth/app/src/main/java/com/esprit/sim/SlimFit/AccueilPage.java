package com.esprit.sim.SlimFit;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccueilPage extends Fragment {

    private BottomNavigationView accueilNav ;
    private FrameLayout accueilFrame ;

    public AccueilPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_accueil_page, container, false);
        accueilNav = (BottomNavigationView)view.findViewById(R.id.accueilNav) ;
        accueilFrame = (FrameLayout)view.findViewById(R.id.accueilFrame);
        getFragmentManager().beginTransaction().replace(R.id.accueilFrame,new Exercice()).commit();
        accueilNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_exercice :
                        accueilNav.setItemBackgroundResource(R.color.colorWhiteTransparent);
                        getFragmentManager().beginTransaction().replace(R.id.accueilFrame,new Exercice()).commit();
                        return true;
                    case R.id.nav_programme :
                        accueilNav.setItemBackgroundResource(R.color.colorWhiteTransparent);
                        getFragmentManager().beginTransaction().replace(R.id.accueilFrame,new Programmes()).commit();
                        return true;
                    }
                return false ;
            }
        });
        return view ;
    }

}
