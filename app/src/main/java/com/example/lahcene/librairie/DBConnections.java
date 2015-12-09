package com.example.lahcene.librairie;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

/**
 * Created by lahcene on 30/10/15.
 */
public class DBConnections  extends SQLiteOpenHelper {
    public static final String DbName = "books.db";
    public static final int Version = 1;

    public DBConnections(Context context) {
        super(context, DbName, null, Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//the path of data base is : /data/data/com.example.lahcene.librairie/databases/books.db
        db.execSQL("create table IF NOT EXISTS filters (idF INTEGER primary key, titreF TEXT, authorF TEXT, nomF TEXT, anneeF TEXT)");
        db.execSQL("create table IF NOT EXISTS books (isbn TEXT primary key, titre TEXT, author TEXT,author2 TEXT,serie TEXT,genre TEXT,editeur TEXT, annee TEXT,annotation TEXT, image TEXT, description TEXT)");
        db.execSQL("create table IF NOT EXISTS genre (id INTEGER primary key,genreName TEXT)");


    }

    //we change the database version
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop  table IF  EXISTS books ");
        db.execSQL("Drop  table IF  EXISTS filters ");
        db.execSQL("Drop  table IF  EXISTS genre ");
        onCreate(db);
    }

    //to insert in the filter table
    public void insertF(String titre, String author,String annee, String nom){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("titreF", titre);
        contentValues.put("authorF", author);
        contentValues.put("anneeF", annee);
        contentValues.put("nomF", nom);
        db.insert("filters", null, contentValues);
    }


    // insert in the genre(kind of books) table
    public void insertGenre(String genre){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("genreName", genre);
        db.insert("genre", null, contentValues);
    }

    //insert in the books table
    public void insertRawAdmin(String isbn, String titre, String author, String author2,  String serie,  String genre,  String editeur,   String annee,  String annotation, String image, String description){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isbn", isbn);
        contentValues.put("titre", titre);
        contentValues.put("author", author);
        contentValues.put("author2", author2);
        contentValues.put("serie", serie);
        contentValues.put("genre", genre);
        contentValues.put("editeur", editeur);
        contentValues.put("annee", annee);
        contentValues.put("annotation", annotation);
        contentValues.put("image", image);
        contentValues.put("description", description);
        db.insert("books", null, contentValues);
    }

    //update the books table
    public void updateR(String isbn, String titre, String author, String author2,  String serie,  String genre,  String editeur,   String annee,  String annotation,  String description){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update  books set titre='" + titre + "', author='" + author + "', author2='" + author2
                + "', serie='" + serie + "', genre='" + genre + "', editeur='" + editeur + "', annee='" + annee + "', annotation='" + annotation
                + "',  description='" + description + "'  where isbn ='" + isbn + "'");

    }

    //delete from books table
    public void delateByIsbn(String isbn) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from books where isbn ='" + isbn + "'");
    }

    //the title of the books is to retrieve a list
    public ArrayList getAllrecordId(){
        ArrayList  array_list = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from books",null);
        res.moveToFirst();
        while(res.isAfterLast()==false){
            array_list.add(res.getString(res.getColumnIndex("titre")));
            res.moveToNext();
        }
        return array_list;
    }

    //the title of the genre(kind of books) is to retrieve a list
    public ArrayList getAllrecordGenre(){
        ArrayList  array_list = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from genre",null);
        res.moveToFirst();
        while(res.isAfterLast()==false){
            array_list.add(res.getString(res.getColumnIndex("genreName")));
            res.moveToNext();
        }
        return array_list;
    }

    // we recover the isbn code  books table
    public String getIsbn(String t) {
        String s="";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from books where titre like'%" + t + "%' ", null);

        if (res.moveToNext()) {
            s = res.getString(res.getColumnIndex("isbn"));

        }
        return s;
    }

    // we recover the isbn code  books table too
    public String getIsbn2(String t, String a) {
        String s="";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from books where (  titre like'%" + t + "%') and (author like'%" + a + "%')  ", null);
        if (res.moveToNext()) {
            s = res.getString(res.getColumnIndex("isbn"));
        }
        return s;
    }

    // we recover the title  books table with isbn code
    public String getTitle(String i) {
        String s="";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from books where isbn like'%"+i+"%' ", null);
        if (res.moveToNext()) {
            s = res.getString(res.getColumnIndex("titre"));

        }
        return s;
    }

    // we recover the title  books table with title
    public String getTitleTitle(String t) {
        String s="";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from books where titre like'%"+t+"%' ", null);
        if (res.moveToNext()) {
            s = res.getString(res.getColumnIndex("titre"));
        }
        return s;
    }

    // we recover the author  books table with isbn code
    public String getAuthor(String i) {
        String s="";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from books where isbn like'%"+i+"%' ", null);
        if (res.moveToNext()) {
            s = res.getString(res.getColumnIndex("author"));
        }
        return s;
    }

    // we recover the author  books table with title
    public String getAuthorAuthor(String t) {
        String s="";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from books where titre like'%"+t+"%' ",null);
        if (res.moveToNext()) {
            s = res.getString(res.getColumnIndex("author"));
        }
        return s;
    }

    // we recover the second author  books table with isbn code
    public String getAuthor2(String i) {
        String s="";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from books where isbn like'%"+i+"%' ", null);
        if (res.moveToNext()) {
            s = res.getString(res.getColumnIndex("author2"));

        }
        return s;
    }

    // we recover the second author  books table with title
    public String getAuthor2Author2(String t) {
        String s="";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from books where titre like'%"+t+"%' ",null);
        if (res.moveToNext()) {
            s = res.getString(res.getColumnIndex("author2"));

        }
        return s;
    }

    // we recover the serie  books table with isbn code
    public String getSerie(String i) {
        String s="";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from books where isbn like'%"+i+"%' ", null);
        if (res.moveToNext()) {
            s = res.getString(res.getColumnIndex("serie"));

        }
        return s;
    }

    // we recover the serie books table with title
    public String getSerieSerie(String t) {
        String s="";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from books where titre like'%"+t+"%' ",null);
        if (res.moveToNext()) {
            s = res.getString(res.getColumnIndex("serie"));

        }
        return s;
    }

    // we recover the genre(kind books) from books table with isbn code
    public String getGenre(String i) {
        String s="";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from books where isbn like'%"+i+"%' ", null);
        if (res.moveToNext()) {
            s = res.getString(res.getColumnIndex("genre"));

        }
        return s;
    }

    // we recover the genre(kind books) from books   table with title
    public String getGenreGenre(String t) {
        String s="";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from books where titre like'%"+t+"%' ",null);
        if (res.moveToNext()) {
            s = res.getString(res.getColumnIndex("genre"));

        }
        return s;
    }

    // we recover the editor from books  table with isbn code
    public String getEditeur(String i) {
        String s="";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from books where isbn like'%"+i+"%' ", null);
        if (res.moveToNext()) {
            s = res.getString(res.getColumnIndex("editeur"));

        }
        return s;
    }

    // we recover the editor from books  table with title
    public String getEditeurEditeur(String t) {
        String s="";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from books where titre like'%"+t+"%' ",null);
        if (res.moveToNext()) {
            s = res.getString(res.getColumnIndex("editeur"));
        }
        return s;
    }

    // we recover the year from books  table with isbn code
    public String getAnnee(String i) {
        String s="";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from books where isbn like'%"+i+"%' ", null);
        if (res.moveToNext()) {
            s = res.getString(res.getColumnIndex("annee"));

        }
        return s;
    }

    // we recover the year from books  table with title
    public String getAnneeAnnee(String t) {
        String s="";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from books where titre like'%"+t+"%' ", null);
        if (res.moveToNext()) {
            s = res.getString(res.getColumnIndex("annee"));
        }
        return s;
    }


    // we recover the description from books  table with isbn code
    public String getDescription(String i) {
        String s="";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from books where isbn like'%"+i+"%' ", null);
        if (res.moveToNext()) {
            s = res.getString(res.getColumnIndex("description"));
        }
        return s;
    }

    // we recover the annotation from books  table isbn code
    public String getAnnotation(String i) {
        String s="";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from books where isbn like'%"+i+"%' ", null);
        if (res.moveToNext()) {
            s = res.getString(res.getColumnIndex("annotation"));
        }
        return s;
    }

    // we recover the annotation from books  table with title
    public String getAnnotationAnnotation(String t) {
        String s="";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from books where titre like'%"+t+"%' ", null);
        if (res.moveToNext()) {
            s = res.getString(res.getColumnIndex("annotation"));
        }
        return s;
    }

    // we recover the description from books  table with title
    public String getDescriptionDescription(String t) {
        String s="";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from books where titre like'%"+t+"%' ",null);
        if (res.moveToNext()) {
            s = res.getString(res.getColumnIndex("description"));
        }
        return s;
    }

    // get all name of filter table
    public ArrayList getAllrecordF(){
        ArrayList  array_list = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from filters",null);
        res.moveToFirst();
        while(res.isAfterLast()==false){
            array_list.add(res.getString(res.getColumnIndex("nomF")));
            res.moveToNext();
        }
        return array_list;
    }

    //get all record filter table
    public ArrayList getAllrecordFiltre(String t,String a,String an){
        ArrayList  array_list = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from books where (  titre like'%" + t + "%') and (author like'%" + a + "%') and (annee like'%" + an + "%')  ", null);
        res.moveToFirst();
        while(res.isAfterLast()==false){
            array_list.add("Titre  "+res.getString(res.getColumnIndex("titre"))+"  Author  "+res.getString(res.getColumnIndex("author"))+"  Annee  "+res.getString(res.getColumnIndex("annee")));
            res.moveToNext();
        }
        return array_list;
    }

    // get title of filter table
    public String getTitleFilter(String t) {
        String s="";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from filters where nomF like'%" + t + "%' ", null);

        if (res.moveToNext()) {
            s = res.getString(res.getColumnIndex("titreF"));
        }
        return s;
    }

    // get authors of filter table
    public String getAuthorFilter(String t) {
        String s="";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from filters where nomF like'%" + t + "%' ", null);

        if (res.moveToNext()) {
            s = res.getString(res.getColumnIndex("authorF"));
        }
        return s;
    }

    // get years of filter table
    public String getAnneeFilter(String t) {
        String s="";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from filters where nomF like'%" + t + "%' ", null);

        if (res.moveToNext()) {
            s = res.getString(res.getColumnIndex("anneeF"));
        }
        return s;
    }

    // delete from filter table
    public void delateFilterByTitle(String nom) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from filters where nomF ='" + nom + "'");
    }
}