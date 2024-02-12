package com.example.project_temp2.server;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.project_temp2.Login;
import com.example.project_temp2.shared.AllCars;
import com.example.project_temp2.car.CarModel;

public class ConnectionAsyncTask extends AsyncTask<String, String, String> {
    Activity activity;

    public ConnectionAsyncTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected String doInBackground(String... params) {
        return HttpManager.getData(params[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (s != null) {
            AllCars listCars = AllCars.getInstance();
            listCars.cars = CarJSONParser.getObjectFromJson(s);

            // Seed Cars
            assert listCars.cars != null;
            listCars.cars.remove(21);
            CarModel.seedCars();

            Toast.makeText(activity, "Connected", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(activity, Login.class);
            activity.startActivity(intent);
            activity.finish();
        } else
            Toast.makeText(activity, "Unable to Establish a Connection to the Server", Toast.LENGTH_SHORT).show();

    }

}