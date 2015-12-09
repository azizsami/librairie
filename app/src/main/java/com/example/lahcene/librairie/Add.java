package com.example.lahcene.librairie;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import google.zxing.integration.android.IntentIntegrator;
import google.zxing.integration.android.IntentResult;

;


public class Add extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String result="https://www.googleapis.com/books/v1/volumes?q=isbn:";
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView imgSpecimenPhoto;
    public File tempFile;
    public String isbn;
    Spinner spinner;  // Spinner  element
    String genre;
    DBConnections db1 = new DBConnections(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        imgSpecimenPhoto = (ImageView) findViewById(R.id.imgSpecimenPhoto);

        // Spinner element for desplay the kind of books
        spinner = (Spinner) findViewById(R.id.spinner);

        // Loading spinner data from database
        loadSpinnerData();
        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        //ChekBox to make EditText enable if we want to add a new kind of books
        CheckBox locationCheckbox = (CheckBox)findViewById(R.id.locationCheckbox);
        EditText vGenre2 = (EditText) findViewById(R.id.valueGenre2);
        vGenre2.setEnabled(false);

        locationCheckbox.setOnClickListener(new CheckBox.OnClickListener() {
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    EditText vGenre2 = (EditText) findViewById(R.id.valueGenre2);
                    vGenre2.setEnabled(true);
                } else {
                    EditText vGenre2 = (EditText) findViewById(R.id.valueGenre2);
                    vGenre2.setEnabled(false);
                }
            }
        });
    }


    // the function which allow us to save a new record in the table of books
    public void buSave(View view) {
        EditText vIsbn = (EditText) findViewById(R.id.valueIsbn);
        EditText vTitre= (EditText) findViewById(R.id.valueTitre);
        EditText vAuthor = (EditText) findViewById(R.id.valueAuthor);
        EditText vAuthor2 = (EditText) findViewById(R.id.valueAuthor2);
        EditText vSerie = (EditText) findViewById(R.id.valueSerie);
        EditText vEditeur = (EditText) findViewById(R.id.valueEditeur);
        EditText vAnnee = (EditText) findViewById(R.id.valueAnnee);
        EditText vAnnotation = (EditText) findViewById(R.id.valueAnnotation);
        TextView vImage = (TextView) findViewById(R.id.textImage);
        EditText vDescription = (EditText) findViewById(R.id.valueDescription);
        CheckBox locationCheckbox = (CheckBox)findViewById(R.id.locationCheckbox);
        //only if we want to add a new record if we don't satisfy of the all kinds of the books in the list (Spinner)
        if (locationCheckbox.isChecked()) {
            EditText vGenre2 = (EditText) findViewById(R.id.valueGenre2);
            genre = vGenre2.getText().toString();
            db1.insertGenre(genre);
        }
        //add in the books table all the content of the form
        DBConnections db = new DBConnections(this);
        db.insertRawAdmin(vIsbn.getText().toString(), vTitre.getText().toString(), vAuthor.getText().toString(),
                vAuthor2.getText().toString(), vSerie.getText().toString(), genre,
                vEditeur.getText().toString(), vAnnee.getText().toString(),
                vAnnotation.getText().toString(), vImage.getText().toString(),
                vDescription.getText().toString());
        finish();
    }

    //to caul the scan application
    public void onClick(View v) {
        if (v.getId() == R.id.scanBtn) {
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }
    }

    /**
     * Function to load the spinner data from SQLite database
     * */
    private void loadSpinnerData() {
        // database handler
        DBConnections db = new DBConnections(getApplicationContext());

        // Spinner Drop down elements
        List<String> lables = db.getAllrecordGenre();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    //give the value of item selected from the spinner to genre
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        genre= parent.getItemAtPosition(position).toString();
    }

    //we don't need this method for the moment
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    //we recovered the isbn retrieve by scan
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve result of scanning - instantiate ZXing object
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        //check we have a valid result
        if (scanningResult != null) {
            //get content from Intent Result
            String scanContent = scanningResult.getContents();
            EditText vIsbn = (EditText) findViewById(R.id.valueIsbn);
            vIsbn.setText(scanContent);
            //get format name of data scanned
            String scanFormat = scanningResult.getFormatName();
            Log.v("SCAN", "content: " + scanContent + " - format: " + scanFormat);
            if (scanContent != null && scanFormat != null && scanFormat.equalsIgnoreCase("EAN_13")) {
                result +=scanContent;
                new MyTask().execute();
            } else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Not a valid scan!", Toast.LENGTH_SHORT);
                toast.show();

            }
        } else {
            //invalid scan data or scan canceled
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No book scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    //we recovered the json file with the isbn number and send the json file to onPostExecute method
    private class MyTask extends AsyncTask<Void, Void, Void> {
        String textResult;
        @Override
        protected Void doInBackground(Void... params) {
            URL textUrl;
            try {
                textUrl = new URL(result);
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(textUrl.openStream()));
                String stringBuffer;
                String stringText = "";
                while ((stringBuffer = bufferedReader.readLine()) != null) {
                    stringText += stringBuffer;
                }
                bufferedReader.close();
                textResult = stringText;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                textResult = e.toString();
            } catch (IOException e) {
                e.printStackTrace();
                textResult = e.toString();
            }
            return null;
        }

        //now we check information from the json file
        @Override
        protected void onPostExecute(Void result) {
            EditText vTitre = (EditText) findViewById(R.id.valueTitre);
            EditText vAuthor = (EditText) findViewById(R.id.valueAuthor);
            EditText vAnnee = (EditText) findViewById(R.id.valueAnnee);
            EditText vDescription = (EditText) findViewById(R.id.valueDescription);

            try{
                //parse results
                JSONObject resultObject = new JSONObject(textResult);
                JSONArray bookArray = resultObject.getJSONArray("items");
                JSONObject bookObject = bookArray.getJSONObject(0);
                JSONObject volumeObject = bookObject.getJSONObject("volumeInfo");
                //try for title
                try{ vTitre.setText(volumeObject.getString("title")); }
                catch(JSONException jse){
                    vTitre.setText("");
                    jse.printStackTrace();
                }
                //author can be multiple
                StringBuilder authorBuild = new StringBuilder("");
                try{
                    JSONArray authorArray = volumeObject.getJSONArray("authors");
                    for(int a=0; a<authorArray.length(); a++){
                        if(a>0) authorBuild.append(", ");
                        authorBuild.append(authorArray.getString(a));
                    }
                    vAuthor.setText(authorBuild.toString());
                }
                catch(JSONException jse){
                    vAuthor.setText("");
                    jse.printStackTrace();
                }
                //publication date
                try{ vAnnee.setText(volumeObject.getString("publishedDate")); }
                catch(JSONException jse){
                    vAnnee.setText("");
                    jse.printStackTrace();
                }
                //book description
                try{ vDescription.setText(volumeObject.getString("description")); }
                catch(JSONException jse){
                    vDescription.setText("");
                    jse.printStackTrace();
                }

            }
            catch (Exception e) {
                //no result
                e.printStackTrace();
                vTitre.setText("NOT FOUND");
                vAuthor.setText("NOT FOUND");
                vAnnee.setText("NOT FOUND");
                vDescription.setText("NOT FOUND");
            }
        }
    }


    // this method will be called when the take photo button was clicked
    public void btnTakePhotoClicked(View view) {
        EditText vIsbn = (EditText) findViewById(R.id.valueIsbn);
        isbn=vIsbn.getText().toString();
        File path = new File( Environment.getExternalStorageDirectory(), getPackageName() );
        if(!path.exists()){
            path.mkdir();
        }
        tempFile = new File( Environment.getExternalStorageDirectory(), isbn+".bmp" );
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        TextView vImage = (TextView) findViewById(R.id.textImage);
        vImage.setText(isbn+".bmp");
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
    }
}


