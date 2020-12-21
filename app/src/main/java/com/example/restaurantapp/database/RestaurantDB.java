package com.example.restaurantapp.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;


public class RestaurantDB extends SQLiteOpenHelper {
    public RestaurantDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public static final  int DATABASE_VERSION=1;
    public static final String CATEGORIE_TABLE  = "categorie";
    public static final String CATEGORIE_ID = "id";
    public static final String CATEGORIE_NOM = "nom";
    public static final String DATABASE_NAME ="restaurant_db";

    private static final String SQL_CREATE_CATEGORIE=
            "CREATE TABLE IF NOT EXISTS " + CATEGORIE_TABLE+"( "+CATEGORIE_ID + " INTEGER PRIMARY KEY , "+ CATEGORIE_NOM + " TEXT )";

    public void insertInCategorie(String nom){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        db.execSQL("INSERT INTO categorie(nom) VALUES('"+nom+"');");
    }

    public ArrayList<Categorie> readCategorie(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor  = db.query(CATEGORIE_TABLE,null,null,null,null,null,null,null);
        ArrayList<Categorie> categories = new ArrayList<Categorie>();
        while (cursor.moveToNext()){
            String nom = cursor.getString(cursor.getColumnIndexOrThrow(CATEGORIE_NOM));
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(CATEGORIE_ID));
            categories.add(new Categorie(id,nom));
        }
        cursor.close();
        return categories;
    }

    public static final String RESTAURANT_TABLE = "restaurant";
    public static final String RESTAURANT_ID = "id";
    public static final String RESTAURANT_NOM = "nom";
    public static final String RESTAURANT_ETAT="etat";
    public static final String RESTAURANT_LONGITUDE="longitude";
    public static final String RESTAURANT_LATITUDE="latitude";
    public static final String RESTAURANT_TEL="tel";
    public static final String RESTAURANT_DESCRIPTION="description";
    public static final String RESTAURANT_IMAGE="image";

    private static final String SQL_CREATE_RESTAURANT=
            "CREATE TABLE IF NOT EXISTS " + RESTAURANT_TABLE +"("+RESTAURANT_ID + " INTEGER PRIMARY KEY , "
                    + RESTAURANT_NOM + " TEXT,"
                    +RESTAURANT_TEL+" TEXT , "
                    +RESTAURANT_LATITUDE+" DOUBLE , "
                    +RESTAURANT_LONGITUDE+" DOUBLE ,"
                    +RESTAURANT_DESCRIPTION+" TEXT , "
                    +RESTAURANT_IMAGE+" TEXT , "
                    + RESTAURANT_ETAT + " TEXT )";


    public void insertInRestaurant(String nom, String tel,double latitude, double longitude, String etat,String desc,String img){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RESTAURANT_NOM,nom);
        values.put(RESTAURANT_LATITUDE,latitude);
        values.put(RESTAURANT_LONGITUDE,longitude);
        values.put(RESTAURANT_ETAT,etat);
        values.put(RESTAURANT_TEL,tel);
        values.put(RESTAURANT_DESCRIPTION,desc);
        values.put(RESTAURANT_IMAGE,img);
        db.insert(RESTAURANT_TABLE,null,values);

    }

    public static final String CATEGORIEINRESTAURANT_TABLE  = "categorieinrestaurant";
    public static final String CATEGORIEID = "idcategorie";
    public static final String RESTAURANTID = "idrestaurant";
    private static final String SQL_CREATE_CATEGORIEINRESTAURANT=
            "CREATE TABLE IF NOT EXISTS " + CATEGORIEINRESTAURANT_TABLE + " ( " + CATEGORIEID + " INTEGER , " + RESTAURANTID + " INTEGER  )";

    public void insertInRestaurantCategorie(int idcategorie , int idrestauarant ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RESTAURANTID,idrestauarant);
        values.put(CATEGORIEID,idcategorie);
        db.insert(CATEGORIEINRESTAURANT_TABLE,null,values);
    }
    public ArrayList<Restaurant> readRestaurantsByCategorie(int idCategorie){
        ArrayList<Integer> ids = new ArrayList<Integer>();
        ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor_for_ids  = db.query(true,CATEGORIEINRESTAURANT_TABLE,null,"idcategorie='"+idCategorie+"'",null,null,null,null,null);

        while (cursor_for_ids.moveToNext()){
            int idrestaurant= cursor_for_ids.getInt(cursor_for_ids.getColumnIndexOrThrow(RESTAURANTID));
            ids.add(idrestaurant);
        }
        cursor_for_ids.close();
        Cursor cursor;
        for(int i=0 ; i<ids.size() ; i++) {
            cursor  = db.query(true,RESTAURANT_TABLE,null,"id='"+ids.get(i)+"'",null,null,null,null,null);
            while (cursor.moveToNext()) {
                int idRestaurant = cursor.getInt(cursor.getColumnIndexOrThrow(RESTAURANT_ID));
                String nomRestaurant = cursor.getString(cursor.getColumnIndexOrThrow(RESTAURANT_NOM));
                double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(RESTAURANT_LATITUDE));
                double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(RESTAURANT_LONGITUDE));
                String etat = cursor.getString(cursor.getColumnIndexOrThrow(RESTAURANT_ETAT));
                String telRestaurant=cursor.getString(cursor.getColumnIndexOrThrow(RESTAURANT_TEL));
                String descRestaurant=cursor.getString(cursor.getColumnIndexOrThrow(RESTAURANT_DESCRIPTION));
                String imageRestaurant=cursor.getString(cursor.getColumnIndexOrThrow(RESTAURANT_IMAGE));
                restaurants.add(new Restaurant(idRestaurant,nomRestaurant,telRestaurant, latitude, longitude, etat,descRestaurant,imageRestaurant));
            }
            if(i==ids.size()-1){
                cursor.close();
            }
        }
        db.close();
        return restaurants;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CATEGORIE);
        db.execSQL(SQL_CREATE_RESTAURANT);
        db.execSQL(SQL_CREATE_CATEGORIEINRESTAURANT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS categorieinrestaurant");
        db.execSQL("DROP TABLE IF EXISTS categorie");
        db.execSQL("DROP TABLE IF EXISTS restauarant");
        onCreate(db);

    }
    public void onDowngrade(SQLiteDatabase db , int oldVersion,int newVersion){
        onUpgrade(db,oldVersion,newVersion);
    }
    public void deletefromalltables(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM categorie");
        db.execSQL("DELETE FROM restaurant");
        db.execSQL("DELETE FROM categorieinrestaurant");
    }
    public void databaseInitialisation(){
        deletefromalltables();
        insertInCategorie("Coffee");
        insertInCategorie("Restaurant");
        insertInCategorie("Hotel");
        insertInCategorie("Sushi");
        insertInCategorie("Snack");
        insertInCategorie("HAMBURGER");
        String desc="On sait depuis longtemps que travailler avec du texte lisible et contenant du sens est source de distractions, et empêche de se concentrer sur la mise en page elle-même.";
        insertInRestaurant("La Trefle","0645947757",34.013099,-6.831316,"ouvert",desc,"restaurant_default_img");
        insertInRestaurant("Mini Chicken ","0645947757",34.003864,-6.844816,"Fermé",desc,"restaurant_default_img");
        insertInRestaurant("Les 3 Dou'soeurs ","0645947757",33.950614,-6.885714,"ouvert",desc,"restaurant_default_img");
        insertInRestaurant("StarBucks ","0645947757",33.956621,-6.867765,"ouvert",desc,"restaurant_default_img");
        insertInRestaurant("Brioche Dorée","0645947757",33.955775,-6.86736,"ouvert",desc,"restaurant_default_img");
        insertInRestaurant("Sunset","0645947757",34.016476,-6.833467,"Fermé",desc,"restaurant_default_img");
        insertInRestaurant("Mini House","0645947757",12,69,"ouvert",desc,"restaurant_default_img");
        insertInRestaurant("Big Bacha","0645947757",34.258272,-6.583769,"Fermé",desc,"restaurant_default_img");
        insertInRestaurant("Mcdonalds ","0645947757",34.017499,-6.834719,"ouvert",desc,"restaurant_default_img");
        insertInRestaurant("Dawliz ","0645947757",34.028233,-6.815225,"ouvert",desc,"restaurant_default_img");
        insertInRestaurant("Step Burger ","0645947757",34.261869,-6.584398,"ouvert",desc,"restaurant_default_img");
        insertInRestaurantCategorie(1,3);
        insertInRestaurantCategorie(1,5);
        insertInRestaurantCategorie(1,10);
        insertInRestaurantCategorie(1,8);
        insertInRestaurantCategorie(1,2);
        insertInRestaurantCategorie(2,8);
        insertInRestaurantCategorie(2,4);
        insertInRestaurantCategorie(2,6);
        insertInRestaurantCategorie(2,10);
        insertInRestaurantCategorie(2,1);
        insertInRestaurantCategorie(3,5);
        insertInRestaurantCategorie(3,9);
        insertInRestaurantCategorie(3,7);
        insertInRestaurantCategorie(3,11);
        insertInRestaurantCategorie(3,8);
        insertInRestaurantCategorie(4,2);
        insertInRestaurantCategorie(4,5);
        insertInRestaurantCategorie(4,9);
        insertInRestaurantCategorie(4,11);
        insertInRestaurantCategorie(4,10);
        insertInRestaurantCategorie(5,1);
        insertInRestaurantCategorie(5,2);
        insertInRestaurantCategorie(5,3);
        insertInRestaurantCategorie(5,4);
        insertInRestaurantCategorie(5,5);
        insertInRestaurantCategorie(6,6);
        insertInRestaurantCategorie(6,7);
        insertInRestaurantCategorie(6,8);
        insertInRestaurantCategorie(6,10);
        insertInRestaurantCategorie(6,9);
    }
}
