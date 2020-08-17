package com.esprit.sim.SlimFit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.esprit.sim.SlimFit.Classe.User;
import com.facebook.login.LoginManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public String nom;
    public User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new AccueilSM()).addToBackStack(null).commit();
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.nomUti);
        TextView navUsername2 = (TextView) headerView.findViewById(R.id.nomUti2);
        navUsername.setText(User.nom);
        navUsername2.setText(User.prenom);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_deconnexion) {
            SharedPreferences settings = getApplicationContext().getSharedPreferences("myLogin", getApplicationContext().MODE_PRIVATE);
            settings.edit().clear().commit();
            Intent login = new Intent(MainActivity.this, Login.class);
            LoginManager.getInstance().logOut();
            startActivity(login);
            return true ;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_ex_prog) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new AccueilPage()).commit();
        } else if (id == R.id.nav_accueil) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new AccueilSM()).commit();
        }else if (id == R.id.nav_quotidien) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new Gallery()).commit();
        } else if (id == R.id.nav_programme) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new MesProgrammes()).commit();

        } else if (id == R.id.nav_Exercices) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new MesExercices()).commit();

        } else if (id == R.id.nav_Profil) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new ProfilFragment()).commit();
        } else if (id == R.id.nav_challenge) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new challenge()).commit();
        } else if (id == R.id.nav_food) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new Nutrition()).commit();
    }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
