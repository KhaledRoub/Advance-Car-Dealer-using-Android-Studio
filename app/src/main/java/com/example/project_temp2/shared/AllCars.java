package com.example.project_temp2.shared;

import com.example.project_temp2.car.CarModel;

import java.util.List;

public class AllCars {
    private static volatile AllCars INSTANCE = null;

    public List<CarModel> cars;

    private AllCars() {
    }

    public static AllCars getInstance() {
        if (INSTANCE == null) {
            synchronized (AllCars.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AllCars();
                }
            }
        }
        return INSTANCE;
    }
}
