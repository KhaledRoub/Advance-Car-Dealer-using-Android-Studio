package com.example.project_temp2.server;

import com.example.project_temp2.car.CarModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CarJSONParser {
    public static List<CarModel> getObjectFromJson(String json) {
        List<CarModel> cars;
        try {
            JSONArray jsonArray = new JSONArray(json);
            cars = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject = (JSONObject) jsonArray.get(i);
                CarModel car = new CarModel();
                car.setId(jsonObject.getInt("id"));
                car.setType(jsonObject.getString("type"));
                cars.add(car);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return cars;
    }
}