package com.example.project_temp2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.example.project_temp2.database.DataBaseHelper;
import com.example.project_temp2.server.ConnectionAsyncTask;

public class ConnectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        Button connect = (Button) findViewById(R.id.connect);
        ImageView car = (ImageView) findViewById(R.id.car);
        ImageView smoke = (ImageView) findViewById(R.id.smoke);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                car.setVisibility(View.VISIBLE);
                smoke.setVisibility(View.VISIBLE);
                car.startAnimation(AnimationUtils.loadAnimation(ConnectActivity.this, R.anim.car_moves));
                smoke.startAnimation(AnimationUtils.loadAnimation(ConnectActivity.this, R.anim.fade_out));


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        smoke.setVisibility(View.INVISIBLE);
                    }
                }, 1000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        car.setVisibility(View.INVISIBLE);
                        ConnectionAsyncTask connectionAsyncTask = new ConnectionAsyncTask(ConnectActivity.this);
                        connectionAsyncTask.execute("https://658582eb022766bcb8c8c86e.mockapi.io/api/mock/rest-apis/encs5150/car-types");

                    }
                }, 1500);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DataBaseHelper dataBaseHelper = new DataBaseHelper(ConnectActivity.this, "project", null, 1);
                        dataBaseHelper.insertCar();
                    }
                }, 10000);

            }
        });

    }
}