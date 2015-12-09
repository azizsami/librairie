package com.example.lahcene.librairie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //access to Activate Add
        Button b2 = (Button) findViewById(R.id.btnE);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Add.class);
                startActivity(i);
            }
        });

        //access to Activate MainList
        Button b7 = (Button) findViewById(R.id.btnaff);
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MainList.class);
                startActivity(i);
            }
        });

        //access to filter
        Button bdetails = (Button) findViewById(R.id.btnFilter);
        bdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Filter.class);
                startActivity(i);
            }
        });


        //access to fiDBManager
        Button bbtnDBManager = (Button) findViewById(R.id.btnDBManager);
        bbtnDBManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, DBManager.class);
                startActivity(i);
            }
        });
    }
}