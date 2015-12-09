package com.example.lahcene.librairie;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class MainList extends AppCompatActivity {
    private ImageView imgSpecimenPhoto;
    public String isbn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);
        final DBConnections db10 = new DBConnections(this);
        Button b20 = (Button) findViewById(R.id.btn20);
        b20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final String[] chaine = {""};
        final ListView lv2 = (ListView) findViewById(R.id.listView3);
        lv2.setClickable(true);
        lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Object o = lv2.getItemAtPosition(position);
                chaine[0] = o.toString();
                FragmentDetail frag = (FragmentDetail) getFragmentManager().findFragmentById(R.id.detail_fragment);
                if (frag != null && frag.isInLayout()) {
                    String t,a;
                    imgSpecimenPhoto = (ImageView) findViewById(R.id.imgSpecimenPhoto);
                    TextView etvTitre = (TextView) findViewById(R.id.tvTitre);
                    TextView etvAuthor = (TextView) findViewById(R.id.tvAuthor);
                    TextView etvAuthor2 = (TextView) findViewById(R.id.tvAuthor2);
                    TextView etvSerie = (TextView) findViewById(R.id.tvSerie);
                    TextView etvGenre = (TextView) findViewById(R.id.tvGenre);
                    TextView etvEditeur = (TextView) findViewById(R.id.tvEditeur);
                    TextView etvAnnee = (TextView) findViewById(R.id.tvAnnee);
                    TextView etvAnnotation = (TextView) findViewById(R.id.tvAnnotation);
                    TextView etvDescription= (TextView) findViewById(R.id.tvDescription);
                    t=db10.getTitleTitle(chaine[0].toString());
                    a=db10.getAuthorAuthor(chaine[0].toString());
                    isbn="";
                    isbn =db10.getIsbn2(t,a);
                    String chaineImage= Environment.getExternalStorageDirectory().toString();
                    File imgFile = new File( chaineImage+"/"+isbn+".bmp" );
                    imgSpecimenPhoto.setImageBitmap(null);
                    if(imgFile.exists()){
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        imgSpecimenPhoto.setImageBitmap(myBitmap);
                    }
                    //we desplaye information books table
                    etvTitre.setText("Title: " + db10.getTitleTitle(chaine[0].toString()));
                    etvAuthor.setText("Author1: " + db10.getAuthorAuthor(chaine[0].toString()));
                    etvAuthor2.setText("Author2: "+db10.getAuthor2Author2(chaine[0].toString()));
                    etvSerie.setText("Series: "+db10.getSerieSerie(chaine[0].toString()));
                    etvGenre.setText("Kind: "+db10.getGenreGenre(chaine[0].toString()));
                    etvEditeur.setText("Editor: "+db10.getEditeurEditeur(chaine[0].toString()));
                    etvAnnee.setText("Year: "+db10.getAnneeAnnee(chaine[0].toString()));
                    etvAnnotation.setText("Annotation: "+db10.getAnnotationAnnotation(chaine[0].toString()));
                    etvDescription.setText("Description: "+db10.getDescriptionDescription(chaine[0].toString()));
                }
            }
        });

        final DBConnections db = new DBConnections(this);
        ListView lv = (ListView) findViewById(R.id.listView3);
        ArrayList<String> arrlist = db.getAllrecordId();
        lv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrlist));

        //crier  une boite de dialogue est l'afficher pour faire des opératons sur un éliment séléctionné
        ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
        SimpleAdapter mSchedule = new SimpleAdapter(this.getBaseContext(), listItem, R.layout.affichageitem,
                new String[]{"img", "titre", "auteur", "isbn"}, new int[]{R.id.img, R.id.titre, R.id.auteur, R.id.isbn});
        lv2.setAdapter(mSchedule);
        lv2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> av, View v, int pos, final long id) {
                Object o = lv2.getItemAtPosition(pos);
                final String titre = o.toString();
                final String isbn = db.getIsbn(o.toString());
                //on créé une boite de dialogue
                AlertDialog.Builder adb = new AlertDialog.Builder(MainList.this);
                //on insère un titre à notre boite de dialogue
                adb.setTitle("Sélection Item");
                //on insère un message à notre boite de dialogue, et ici on affiche le titre de l'item cliqué
                adb.setMessage("Vous souhaitez faire une action sur cet element :"+titre+" ?");
                adb.setNegativeButton("Annuler", null);
                //cliquer pour supprimer
                adb.setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        db.delateByIsbn(isbn);
                        lv2.deferNotifyDataSetChanged();
                        finish();
                    }
                });

                //cliquer pour modifier
                adb.setNeutralButton("Modifier", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(MainList.this, UpDate.class);
                        i.putExtra("isbn", isbn);
                        startActivity(i);
                        finish();
                    }
                });
                //  afficher la boite de dialogue
                adb.show();
                return true;
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        //listing the rows
        DBConnections db = new DBConnections(this);
        ListView lv = (ListView) findViewById(R.id.listView3);
        ArrayList<String> arrlist = db.getAllrecordId();
        lv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrlist));
    }
}