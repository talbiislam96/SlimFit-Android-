package com.esprit.sim.SlimFit;

import android.support.annotation.NonNull;

public class Equipe {
    private String nomE1 ;
    private String nomE2 ;
    private String nomEg ;
    private String image1 ;
    private String image2 ;

    public Equipe(String nomE1, String nomE2, String nomEg,String image1,String image2) {
        this.nomE1 = nomE1;
        this.nomE2 = nomE2;
        this.nomEg = nomEg;
        this.image1 = image1;
        this.image2 = image2;
    }

    public String getNomE1() {
        return nomE1;
    }

    public void setNomE1(String nomE1) {
        this.nomE1 = nomE1;
    }

    public String getNomE2() {
        return nomE2;
    }

    public void setNomE2(String nomE2) {
        this.nomE2 = nomE2;
    }

    public String getNomEg() {
        return nomEg;
    }

    public void setNomEg(String nomEg) {
        this.nomEg = nomEg;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    @NonNull
    @Override
    public String toString() {
        return "Equipe{" +
                "nomE1='" + nomE1 + '\'' +
                ", nomE2='" + nomE2 + '\'' +
                ", nomEg='" + nomEg + '\'' +
                ", image='" + image1 + '\'' +
                '}';
    }
}
