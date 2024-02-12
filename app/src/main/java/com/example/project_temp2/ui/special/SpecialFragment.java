package com.example.project_temp2.ui.special;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_temp2.R;
import com.example.project_temp2.car.CarAdapter;
import com.example.project_temp2.car.CarModel;
import com.example.project_temp2.database.DataBaseHelper;
import com.example.project_temp2.database.Favorites;
import com.example.project_temp2.database.ReservedCarDetails;
import com.example.project_temp2.databinding.FragmentSpecialBinding;
import com.example.project_temp2.shared.Dealer;
import com.example.project_temp2.shared.SignedUser;

import java.util.ArrayList;
import java.util.List;

public class SpecialFragment extends Fragment {

    private FragmentSpecialBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSpecialBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Context context = getContext();

        DataBaseHelper dataBaseHelper = new DataBaseHelper(context, "project", null, 1);

        ArrayList<CarModel> specialOffers;

        if (Dealer.getInstance().dealer == null || Dealer.getInstance().dealer.equals("All")) {
            specialOffers = (ArrayList<CarModel>) dataBaseHelper.getCarsWithPromoPercentage();
        } else {
            specialOffers = (ArrayList<CarModel>) dataBaseHelper.getCarsWithPromoPercentageByDealer(Dealer.getInstance().dealer);
        }


        ArrayList<CarModel> favCars = (ArrayList<CarModel>) dataBaseHelper.getFavoriteCars(SignedUser.getInstance().user.getEmail());
        updateStatus(specialOffers, favCars);

        RecyclerView carRV = root.findViewById(R.id.specialRV);

        CarAdapter carAdapter = new CarAdapter(requireContext(), specialOffers, "special");


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);

        carRV.setLayoutManager(linearLayoutManager);
        carRV.setAdapter(carAdapter);


        return root;
    }

    private void updateStatus(List<CarModel> specialOffers, List<CarModel> favCars) {

        for (CarModel car : specialOffers) {
            for (CarModel favorite : favCars) {
                if (favorite.getId() == car.getId()) {
                    car.setStatus(1);
                    break;
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}