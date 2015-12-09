package com.example.lahcene.librairie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;

public class Filter extends AppCompatActivity {
    public String nameF ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Filter.this, AddFilter.class);
                startActivity(i);
            }
        });

        final DBConnections db10 = new DBConnections(this);
        final DBConnections db11 = new DBConnections(this);
        final String[] chaineT = {""};
        final String[] chaineA = {""};
        final String[] chaineAn = {""};
        final ListView lv2 = (ListView)findViewById(R.id.listView1);
        lv2.setClickable(true);
        lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Object o = lv2.getItemAtPosition(position);
                nameF = o.toString();
                chaineT[0] = db10.getTitleFilter(o.toString());
                chaineA[0] = db10.getAuthorFilter(o.toString());
                chaineAn[0] = db10.getAnneeFilter(o.toString());
                // for showing the information in ArrayList according the filter name selected
                ArrayList<String> arrlist = db11.getAllrecordFiltre(chaineT[0], chaineA[0], chaineAn[0]);
                modifyView(arrlist);
            }
        });
    }

    //for delete one filter
    public void deleteF(View view) {
        final DBConnections db = new DBConnections(this);
        db.delateFilterByTitle(nameF);
        onResume();
    }

    //to modify one filter
    public void modifyView(ArrayList a){
        ListView lv5 = (ListView)findViewById(R.id.listView5);
        lv5.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, a));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void buSave(View view) {
        finish();
    }

    //make the list update
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        //listing the rows
        DBConnections db = new DBConnections(this);
        ListView lv = (ListView) findViewById(R.id.listView1);
        ArrayList<String> arrlist = db.getAllrecordF();
        lv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrlist));
    }

}