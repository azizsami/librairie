package com.example.lahcene.librairie;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class DBManager extends Activity  {
    private static final String SAMPLE_DB_NAME = "books.db";
    private static final String SAMPLE_TABLE_NAME = "Info";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbmanager);
        Button b2 = (Button) findViewById(R.id.btnE);
    }

    //this click can used according the kind of button
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnE:
                exportDB();
                break;
            case R.id.btnI:
                importDB();
                break;
            case R.id.btnD:
                deleteDB();
                break;
            case R.id.btnC:
                finish();
        }
    }

    //for delete data base
    private void deleteDB(){
        boolean result = this.deleteDatabase(SAMPLE_DB_NAME);
        if (result==true) {
            Toast.makeText(this, "DB Deleted!", Toast.LENGTH_LONG).show();
        }
    }

    //import data base
    private void importDB(){
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source=null;
        FileChannel destination=null;
        String currentDBPath = SAMPLE_DB_NAME;
        String backupDBPath =  "/data/"+ "com.example.lahcene.librairie" +"/databases/"+SAMPLE_DB_NAME;
        File currentDB = new File(sd, currentDBPath);
        File backupDB = new File(data, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            Toast.makeText(this, "DB Imported!", Toast.LENGTH_LONG).show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    //export data base
    private void exportDB(){
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source=null;
        FileChannel destination=null;
        String currentDBPath = "/data/"+ "com.example.lahcene.librairie" +"/databases/"+SAMPLE_DB_NAME;
        String backupDBPath = SAMPLE_DB_NAME;
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}