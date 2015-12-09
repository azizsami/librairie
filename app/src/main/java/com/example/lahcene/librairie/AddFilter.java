package com.example.lahcene.librairie;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class AddFilter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_filter);
    }

    //to save information in the filter table
    public void buSave (View view) {
        EditText e1 = (EditText)findViewById(R.id.eFTitre);
        EditText e2 = (EditText)findViewById(R.id.eFAuteur);
        EditText e3 = (EditText)findViewById(R.id.eFAnnee);
        EditText e4 = (EditText)findViewById(R.id.eFName);
        DBConnections db =  new DBConnections(this);
        db.insertF(e1.getText().toString(), e2.getText().toString(), e3.getText().toString(), e4.getText().toString());
        finish();
    }
}