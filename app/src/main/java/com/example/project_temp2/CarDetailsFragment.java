package com.example.project_temp2;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CarDetailsFragment extends DialogFragment {

    public CarDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_car_details, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve data from arguments
        Bundle args = getArguments();
        if (args != null) {
            String manufacturer = args.getString("manufacturer", "");
            String type = args.getString("type", "");
            String model = args.getString("model", "");
            double price = args.getDouble("price", 0);
            double miles = args.getDouble("miles", 0);
            double offers = args.getDouble("offers", 0);

            TextView manufacturerView = view.findViewById(R.id.manufacturerView);
            TextView modelView = view.findViewById(R.id.modelView);
            TextView yearView = view.findViewById(R.id.yearView);
            TextView priceView = view.findViewById(R.id.priceView);
            TextView distanceView = view.findViewById(R.id.milesView);
            TextView offersView = view.findViewById(R.id.offersView);

            manufacturerView.setText(manufacturer);
            modelView.setText(type);
            yearView.setText(model);
            priceView.setText(Double.toString(price));
            distanceView.setText(Double.toString(miles));

            int percentage = (int) offers;
            offersView.setText(Integer.toString(percentage) + "%");
        }
    }
}