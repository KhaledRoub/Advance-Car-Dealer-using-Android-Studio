package com.example.project_temp2.ui.reservations;

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
import com.example.project_temp2.database.ReservedCarDetails;
import com.example.project_temp2.databinding.FragmentReservationsBinding;
import com.example.project_temp2.shared.Dealer;
import com.example.project_temp2.shared.SignedUser;
import com.example.project_temp2.ui.reserves.ReservesAdapter;

import java.util.ArrayList;

public class ReservationsFragment extends Fragment {

    private FragmentReservationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentReservationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        Context context = getContext();

        DataBaseHelper dataBaseHelper = new DataBaseHelper(context, "project", null, 1);

        ArrayList<ReservedCarDetails> reservedCars;

        if (Dealer.getInstance().dealer == null || Dealer.getInstance().dealer.equals("All")) {
            reservedCars = (ArrayList<ReservedCarDetails>) dataBaseHelper.getReservedCarDetailsByUserEmail(SignedUser.getInstance().user.getEmail());
        } else {
            reservedCars = (ArrayList<ReservedCarDetails>) dataBaseHelper.getReservedCarDetailsByUserAndDealer(SignedUser.getInstance().user.getEmail(), Dealer.getInstance().dealer);
        }


        RecyclerView carRV = root.findViewById(R.id.reservedRV);

        ReservesAdapter reservationsAdapter = new ReservesAdapter(requireContext(), reservedCars, "reservations");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);

        carRV.setLayoutManager(linearLayoutManager);
        carRV.setAdapter(reservationsAdapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}