package com.example.project_temp2.ui.reserves;

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
import com.example.project_temp2.databinding.FragmentViewReservesBinding;
import com.example.project_temp2.shared.Dealer;
import com.example.project_temp2.shared.SignedUser;

import java.util.ArrayList;

public class ReservesFragment extends Fragment {

    private FragmentViewReservesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentViewReservesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        DataBaseHelper dataBaseHelper = new DataBaseHelper(root.getContext(), "project", null, 1);
        ArrayList<ReservedCarDetails> reservedCars;

        if (SignedUser.getInstance().user.getRole() == 1) {
            reservedCars = (ArrayList<ReservedCarDetails>) dataBaseHelper.getReservedCarDetailsByDealer(SignedUser.getInstance().user.getEmail());
        } else
            reservedCars = (ArrayList<ReservedCarDetails>) dataBaseHelper.getReservedCarDetailsByDealer(SignedUser.getInstance().user.getSupervisor());

        RecyclerView carRV = root.findViewById(R.id.reservesRV);

        ReservesAdapter reservesAdapter = new ReservesAdapter(requireContext(), reservedCars, "reservations admin");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);

        carRV.setLayoutManager(linearLayoutManager);
        carRV.setAdapter(reservesAdapter);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}