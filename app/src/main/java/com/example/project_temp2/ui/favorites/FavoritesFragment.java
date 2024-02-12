package com.example.project_temp2.ui.favorites;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_temp2.R;
import com.example.project_temp2.car.CarAdapter;
import com.example.project_temp2.car.CarModel;
import com.example.project_temp2.database.DataBaseHelper;
import com.example.project_temp2.databinding.FragmentFavoritesBinding;
import com.example.project_temp2.shared.Dealer;
import com.example.project_temp2.shared.SignedUser;

import java.util.ArrayList;

public class FavoritesFragment extends Fragment {

    private FragmentFavoritesBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Context context = getContext();

        DataBaseHelper dataBaseHelper = new DataBaseHelper(context, "project", null, 1);
        SignedUser signedUser = SignedUser.getInstance();

        ArrayList<CarModel> favCars;

        if (Dealer.getInstance().dealer == null || Dealer.getInstance().dealer.equals("All")) {
            favCars = (ArrayList<CarModel>) dataBaseHelper.getFavoriteCars(signedUser.user.getEmail());
        } else {
            favCars = (ArrayList<CarModel>) dataBaseHelper.getFavoriteCarsByDealer(signedUser.user.getEmail(), Dealer.getInstance().dealer);
        }

        for (int i = 0; i < favCars.size(); i++) {
            favCars.get(i).setStatus(1);
        }

        RecyclerView carRV = root.findViewById(R.id.favoriteRV);

        CarAdapter carAdapter = new CarAdapter(requireContext(), favCars, "favorite");


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);

        carRV.setLayoutManager(linearLayoutManager);
        carRV.setAdapter(carAdapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}