package com.example.lahcene.librairie;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class UpDate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_date);
        final DBConnections db10 = new DBConnections(this);
        Bundle values = getIntent().getExtras();
        if (values != null) {
            String isbn = values.getString("isbn");
            EditText vTitre= (EditText) findViewById(R.id.valueTitre);
            EditText vAuthor = (EditText) findViewById(R.id.valueAuthor);
            EditText vAuthor2 = (EditText) findViewById(R.id.valueAuthor2);
            EditText vSerie = (EditText) findViewById(R.id.valueSerie);
            EditText vGenre2 = (EditText) findViewById(R.id.valueGenre2);
            EditText vEditeur = (EditText) findViewById(R.id.valueEditeur);
            EditText vAnnee = (EditText) findViewById(R.id.valueAnnee);
            EditText vAnnotation = (EditText) findViewById(R.id.valueAnnotation);
            EditText vDescription = (EditText) findViewById(R.id.valueDescription);
            vTitre.setText(db10.getTitle(isbn));
            vAuthor.setText(db10.getAuthor(isbn));
            vAuthor2.setText(db10.getAuthor2(isbn));
            vSerie.setText(db10.getSerie(isbn));
            vGenre2.setText(db10.getGenre(isbn));
            vEditeur.setText(db10.getEditeur(isbn));
            vAnnee.setText(db10.getAnnee(isbn));
            vAnnotation.setText(db10.getAnnotation(isbn));
            vDescription.setText(db10.getDescription(isbn));
        }
    }

    //update the books table
    public void buUpdate (View view) {
        Bundle values = getIntent().getExtras();
        if (values != null) {
            String isbn = values.getString("isbn");
            EditText vTitre= (EditText) findViewById(R.id.valueTitre);
            EditText vAuthor = (EditText) findViewById(R.id.valueAuthor);
            EditText vAuthor2 = (EditText) findViewById(R.id.valueAuthor2);
            EditText vSerie = (EditText) findViewById(R.id.valueSerie);
            EditText vGenre2 = (EditText) findViewById(R.id.valueGenre2);
            EditText vEditeur = (EditText) findViewById(R.id.valueEditeur);
            EditText vAnnee = (EditText) findViewById(R.id.valueAnnee);
            EditText vAnnotation = (EditText) findViewById(R.id.valueAnnotation);
            EditText vDescription = (EditText) findViewById(R.id.valueDescription);
            DBConnections db = new DBConnections(this);
            db.updateR(isbn, vTitre.getText().toString(), vAuthor.getText().toString(),
                    vAuthor2.getText().toString(), vSerie.getText().toString(), vGenre2.getText().toString(),
                    vEditeur.getText().toString(), vAnnee.getText().toString(),
                    vAnnotation.getText().toString(), vDescription.getText().toString());
            finish();
        }
    }
}