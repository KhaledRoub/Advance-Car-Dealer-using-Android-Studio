package com.example.project_temp2.ui.car;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_temp2.database.DataBaseHelper;
import com.example.project_temp2.database.Favorites;
import com.example.project_temp2.shared.AllCars;
import com.example.project_temp2.car.CarModel;
import com.example.project_temp2.car.CarAdapter;
import com.example.project_temp2.R;
import com.example.project_temp2.databinding.FragmentCarBinding;
import com.example.project_temp2.shared.Dealer;
import com.example.project_temp2.shared.SignedUser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.RangeSlider;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CarFragment extends Fragment {

    private FragmentCarBinding binding;

    private FloatingActionButton filter;

    private Dialog dialog;

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        filter = (FloatingActionButton) root.findViewById(R.id.filterBtn);


        Context context = getContext();

        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.filter_dialog);

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.filter_dialog);
        dialog.getWindow().setBackgroundDrawable(drawable);
        dialog.setCancelable(false);

        Button search = dialog.findViewById(R.id.searchFilter);
        Button cancel = dialog.findViewById(R.id.cancelFilter);
        AutoCompleteTextView manu = dialog.findViewById(R.id.manufactureFilterSpinner);
        AutoCompleteTextView model = dialog.findViewById(R.id.modelFilterSpinner);
        AutoCompleteTextView year = dialog.findViewById(R.id.yearFilterSpinner);
        CheckBox discount = dialog.findViewById(R.id.discount);

        RangeSlider priceRangeSlider = dialog.findViewById(R.id.priceRange);

        priceRangeSlider.setLabelFormatter(new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setMaximumFractionDigits(0);
                format.setCurrency(Currency.getInstance("USD"));
                return format.format(value);
            }
        });

        DataBaseHelper dataBaseHelper = new DataBaseHelper(context, "project", null, 1);

        SignedUser signedUser = SignedUser.getInstance();
        List<Favorites> favorites = dataBaseHelper.getFavoritesByUserEmail(signedUser.user.getEmail());

        ArrayList<CarModel> carArrayList;
        ArrayList<CarModel> toLoad;

        if (Dealer.getInstance().dealer == null || Dealer.getInstance().dealer.equals("All")) {
            carArrayList = (ArrayList<CarModel>) dataBaseHelper.getAllCars();
            toLoad = (ArrayList<CarModel>) dataBaseHelper.getAllCars();
        } else {
            carArrayList = (ArrayList<CarModel>) dataBaseHelper.getCarsByDealer(Dealer.getInstance().dealer);
            toLoad = (ArrayList<CarModel>) dataBaseHelper.getCarsByDealer(Dealer.getInstance().dealer);
        }


        RecyclerView carRV = root.findViewById(R.id.recyclerView);

        updateStatus(favorites, carArrayList);

        CarAdapter carAdapter = new CarAdapter(requireContext(), carArrayList, "car");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);

        carRV.setLayoutManager(linearLayoutManager);
        carRV.setAdapter(carAdapter);


        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();

                manu.requestFocus();

                List<String> manufacturers = getAllManufacturers(toLoad);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(dialog.getContext(), android.R.layout.simple_spinner_dropdown_item, manufacturers);
                manu.setDropDownHeight(calculateDropDownHeight(manufacturers.size()));
                manu.setAdapter(dataAdapter);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manu.setText("");
                model.setText("");
                year.setText("");

                dialog.cancel();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                carArrayList.clear();
                carArrayList.addAll(toLoad);
                updateStatus(favorites, carArrayList);

                List<Float> priceRange = priceRangeSlider.getValues();

                String manuValue = manu.getText().toString();
                String modelValue = model.getText().toString();
                String yearValue = year.getText().toString();
                boolean discountValue = discount.isChecked();

                float from = priceRange.get(0);
                float to = priceRange.get(1);

                List<CarModel> filteredList = carArrayList.stream()
                        .filter(car -> (manuValue.isEmpty() || manuValue.equals("All") || car.getManufacturer().contains(manuValue))
                                && (modelValue.isEmpty() || modelValue.equals("All") || car.getType().contains(modelValue))
                                && (yearValue.isEmpty() || yearValue.equals("All") || car.getModel().contains(yearValue))
                                && (from <= car.getPrice() && car.getPrice() <= to)
                                && (!discountValue || car.getPromoPercentage() != 0))
                        .collect(Collectors.toList());

                carArrayList.clear();
                carArrayList.addAll(filteredList);

                carAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        manu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                model.setText("All");
                year.setText("All");

                List<String> models = getModelsByManufacturer(manu.getText().toString(), toLoad);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(dialog.getContext(), android.R.layout.simple_spinner_dropdown_item, models);
                model.setDropDownHeight(calculateDropDownHeight(models.size()));
                model.setAdapter(dataAdapter);

                List<String> years = getYearsByModels(model.getText().toString(), manu.getText().toString(), toLoad);
                ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<>(dialog.getContext(), android.R.layout.simple_spinner_dropdown_item, years);
                year.setDropDownHeight(calculateDropDownHeight(years.size()));
                year.setAdapter(dataAdapter1);
            }
        });

        model.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                year.setText("All");

                List<String> years = getYearsByModels(model.getText().toString(), manu.getText().toString(), toLoad);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(dialog.getContext(), android.R.layout.simple_spinner_dropdown_item, years);
                year.setDropDownHeight(calculateDropDownHeight(years.size()));
                year.setAdapter(dataAdapter);
            }
        });

        return root;
    }

    private void updateStatus(List<Favorites> favorites, List<CarModel> carsList) {

        for (CarModel car : carsList) {
            for (Favorites favorite : favorites) {
                if (favorite.getCarID() == car.getId()) {
                    car.setStatus(1);
                    break;
                }
            }
        }
    }


    private List<String> getAllManufacturers(List<CarModel> cars) {
        Set<String> manufacturerSet = new HashSet<>();

        for (CarModel car : cars) {
            String manufacturer = car.getManufacturer();
            manufacturerSet.add(manufacturer);
        }

        manufacturerSet.add("All");
        List<String> manufacturerList = new ArrayList<>(manufacturerSet);
        Collections.sort(manufacturerList);

        return manufacturerList;
    }

    private List<String> getModelsByManufacturer(String manufacturer, List<CarModel> cars) {
        Set<String> modelSet = new HashSet<>();

        for (CarModel car : cars) {
            if (car.getManufacturer().equalsIgnoreCase(manufacturer)) {
                String model = car.getType();
                modelSet.add(model);
            }
        }

        modelSet.add("All");
        List<String> modelList = new ArrayList<>(modelSet);
        Collections.sort(modelList);

        return modelList;
    }

    private List<String> getYearsByModels(String model, String manufacturer, List<CarModel> cars) {
        Set<String> yearSet = new HashSet<>();

        for (CarModel car : cars) {
            if (car.getType().equalsIgnoreCase(model)) {
                String year = car.getModel();
                yearSet.add(year);
            } else if ((model.equalsIgnoreCase("All") || model.equalsIgnoreCase("")) && car.getManufacturer().equalsIgnoreCase(manufacturer)) {
                String year = car.getModel();
                yearSet.add(year);
            }
        }

        if (yearSet.isEmpty()) {
            for (CarModel car : cars) {
                String year = car.getModel();
                yearSet.add(year);
            }
        }

        yearSet.add("All");
        List<String> yearList = new ArrayList<>(yearSet);
        Collections.sort(yearList);

        return yearList;
    }

    private int calculateDropDownHeight(int itemCount) {
        int itemHeight = getResources().getDimensionPixelSize(android.R.dimen.app_icon_size);
        int maxVisibleItems = 3;

        return itemCount > maxVisibleItems ? maxVisibleItems * itemHeight : ViewGroup.LayoutParams.WRAP_CONTENT;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}