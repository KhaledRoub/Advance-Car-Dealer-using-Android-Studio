package com.example.project_temp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.project_temp2.database.DataBaseHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        DataBaseHelper databaseHelper = new DataBaseHelper(MainActivity.this, "project", null, 1);

        Intent intent = new Intent(MainActivity.this, ConnectActivity.class);
        MainActivity.this.startActivity(intent);
    }
}