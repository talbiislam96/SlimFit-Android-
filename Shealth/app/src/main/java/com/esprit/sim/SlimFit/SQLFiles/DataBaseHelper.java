package com.esprit.sim.SlimFit.SQLFiles;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.esprit.sim.SlimFit.Classe.User;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_EXERCICE = "exercice";
    public static final int COLUMN_IDExercice = 0;
    public static final String COLUMN_NOMExercice = "nom";
    public static final String COLUMN_DETAILExercice = "detail";
    public static final String CULUMN_ImageExercice = "image";
    public  static  final int CULUMN_IdUser = User.id;


    public static final String TABLE_PROGRAMME = "programme";
    public static final int COLUMN_IDProgramme = 0 ;
    public static final String COLUMN_NOMProgramme = "nomprogramme";
    public static final String COLUMN_DETAILProgramme = "detailprogramme";
    public static final String CULUMN_ImageProgramme = "imageprogramme";
    public  static  final int CULUMN_IdUserProg = User.id;

    public static final String TABLE_PROGRAMMEEXERCICE = "programme";
    public static final int COLUMN_IDProgrammeExe = 0 ;
    public static final int COLUMN_IDExercicePROG = 0;




    private static final String DATABASE_NAME = "Favoris.db";
    private static final int DATABASE_VERSION = 1;

    // Commande sql pour la création de la base de données
    private static final String DATABASE_CREATE = "create table "
            + TABLE_EXERCICE + "(" + COLUMN_IDExercice
            + " integer primary key autoincrement, " + COLUMN_NOMExercice
            + " text not null" + COLUMN_DETAILExercice + " text not null " + CULUMN_ImageExercice + ");";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DataBaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCICE);
        onCreate(db);
    }
}
