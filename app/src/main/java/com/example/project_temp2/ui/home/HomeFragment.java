package com.example.project_temp2.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.project_temp2.R;
import com.example.project_temp2.car.CarModel;
import com.example.project_temp2.database.DataBaseHelper;
import com.example.project_temp2.databinding.FragmentHomeBinding;
import com.example.project_temp2.shared.Dealer;
import com.example.project_temp2.shared.SignedUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private ArrayList<String> dealers;

    private HashMap<String, String> emailName;

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        root.clearFocus();

        DataBaseHelper dataBaseHelper = new DataBaseHelper(root.getContext(), "project", null, 1);

        CarModel lastReservedCar = dataBaseHelper.getLastReservedCar(SignedUser.getInstance().user.getEmail());
        CarModel lastFavoriteCar = dataBaseHelper.getLatestFavoriteCar(SignedUser.getInstance().user.getEmail());

        // Reserved
        CardView homeReserveCard = root.findViewById(R.id.homeReserveCard);
        TextView carNameHomeReserve = root.findViewById(R.id.carNameHomeReserve);
        TextView carMilesHomeReserve = root.findViewById(R.id.carMilesHomeReserve);
        TextView carModelHomeReserve = root.findViewById(R.id.carModelHomeReserve);
        ImageView carImageHomeReserve = root.findViewById(R.id.carImageHomeReserve);

        // Favorite
        CardView homeFavoriteCard = root.findViewById(R.id.homeFavoriteCard);
        TextView carNameHome = root.findViewById(R.id.carNameHome);
        TextView carPriceHome = root.findViewById(R.id.carPriceHome);
        TextView carMilesHome = root.findViewById(R.id.carMilesHome);
        TextView carModelHome = root.findViewById(R.id.carModelHome);
        TextView carRatingHome = root.findViewById(R.id.carRatingHome);
        ImageView carImageHome = root.findViewById(R.id.carImageHome);

        if (lastFavoriteCar != null) {
            homeFavoriteCard.setVisibility(View.VISIBLE);
            carNameHome.setText(lastFavoriteCar.getManufacturer() + " " + lastFavoriteCar.getType());
            carMilesHome.setText(lastFavoriteCar.getMiles() + " miles");
            carModelHome.setText(lastFavoriteCar.getModel());
            carRatingHome.setText(Double.toString(lastFavoriteCar.getRating()));
            carPriceHome.setText(Double.toString(lastFavoriteCar.getPrice()));
            carImageHome.setImageResource(lastFavoriteCar.getImage());
        } else
            homeFavoriteCard.setVisibility(View.INVISIBLE);

        if (lastReservedCar != null) {
            homeReserveCard.setVisibility(View.VISIBLE);
            carNameHomeReserve.setText(lastReservedCar.getManufacturer() + " " + lastReservedCar.getType());
            carMilesHomeReserve.setText(lastReservedCar.getMiles() + " miles");
            carModelHomeReserve.setText(lastReservedCar.getModel());
            carImageHomeReserve.setImageResource(lastReservedCar.getImage());
        } else
            homeReserveCard.setVisibility(View.INVISIBLE);

        emailName = dataBaseHelper.getDealers();
        dealers = new ArrayList<>(emailName.values());
        AutoCompleteTextView carDealers = (AutoCompleteTextView) root.findViewById(R.id.carDealer);

        carDealers.setText("All");

        dealers.add(0, "All");

        carDealers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Dealer.getInstance().dealer = getKeyForValue(emailName, carDealers.getText().toString());

                CarModel lastReservedCarDetails;
                CarModel lastFavoriteCarDetail;

                if (carDealers.getText().toString().equals("All")) {
                    lastReservedCarDetails = dataBaseHelper.getLastReservedCar(SignedUser.getInstance().user.getEmail());
                    lastFavoriteCarDetail = dataBaseHelper.getLatestFavoriteCar(SignedUser.getInstance().user.getEmail());
                } else {
                    lastReservedCarDetails = dataBaseHelper.getLastReservedCarDetails(Dealer.getInstance().dealer, SignedUser.getInstance().user.getEmail());
                    lastFavoriteCarDetail = dataBaseHelper.getLatestFavoriteCarByDealer(SignedUser.getInstance().user.getEmail(), Dealer.getInstance().dealer);
                }

                if (lastReservedCarDetails != null) {
                    homeReserveCard.setVisibility(View.VISIBLE);
                    carNameHomeReserve.setText(lastReservedCarDetails.getManufacturer() + " " + lastReservedCarDetails.getType());
                    carMilesHomeReserve.setText(lastReservedCarDetails.getMiles() + " miles");
                    carModelHomeReserve.setText(lastReservedCarDetails.getModel());
                    carImageHomeReserve.setImageResource(lastReservedCarDetails.getImage());
                } else
                    homeReserveCard.setVisibility(View.INVISIBLE);

                if (lastFavoriteCarDetail != null) {
                    homeFavoriteCard.setVisibility(View.VISIBLE);
                    carNameHome.setText(lastFavoriteCarDetail.getManufacturer() + " " + lastFavoriteCarDetail.getType());
                    carMilesHome.setText(lastFavoriteCarDetail.getMiles() + " miles");
                    carModelHome.setText(lastFavoriteCarDetail.getModel());
                    carRatingHome.setText(Double.toString(lastFavoriteCarDetail.getRating()));
                    carPriceHome.setText(Double.toString(lastFavoriteCarDetail.getPrice()));
                    carImageHome.setImageResource(lastFavoriteCarDetail.getImage());
                } else
                    homeFavoriteCard.setVisibility(View.INVISIBLE);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        ArrayAdapter<String> spinner = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, dealers);
        spinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.carDealer.setAdapter(spinner);

    }

    public String getKeyForValue(HashMap<String, String> hashMap, String targetValue) {
        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            if (targetValue.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

}