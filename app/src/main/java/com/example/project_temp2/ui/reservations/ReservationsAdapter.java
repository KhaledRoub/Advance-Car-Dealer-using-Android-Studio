package com.example.project_temp2.ui.reservations;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_temp2.CarDetailsFragment;
import com.example.project_temp2.R;
import com.example.project_temp2.car.CarModel;

import java.util.ArrayList;


public class ReservationsAdapter extends RecyclerView.Adapter<ReservationsAdapter.ViewHolder> {

    private final Context context;

    private final ArrayList<CarModel> carArrayList;

    public ReservationsAdapter(Context context, ArrayList<CarModel> carArrayList) {
        this.context = context;
        this.carArrayList = carArrayList;
    }

    @NonNull
    @Override
    public ReservationsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reserve_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CarModel model = carArrayList.get(position);
        holder.carName.setText(model.getManufacturer() + " " + model.getType());
        holder.carModel.setText(model.getModel());
        holder.carMiles.setText(String.valueOf(model.getMiles()) + " miles");
        holder.carImage.setImageResource(model.getImage());

        holder.carName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCarDetailsFragment(model, ((FragmentActivity) context).getSupportFragmentManager());
            }
        });
    }

    private void showCarDetailsFragment(CarModel car, FragmentManager fragmentManager) {
        CarDetailsFragment carDetailsFragment = new CarDetailsFragment();

        Bundle bundle = new Bundle();
        bundle.putString("manufacturer", car.getManufacturer());
        bundle.putString("type", car.getType());
        bundle.putString("model", car.getModel());
        bundle.putDouble("price", car.getPrice());
        bundle.putDouble("miles", car.getMiles());
        bundle.putDouble("offers", car.getPromoPercentage());

        carDetailsFragment.setArguments(bundle);

        carDetailsFragment.show(fragmentManager, "CarDetailsFragmentTag");
    }

    @Override
    public int getItemCount() {
        return carArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView carImage;
        private final TextView carName;
        private final TextView carMiles;
        private final TextView carModel;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            carImage = itemView.findViewById(R.id.carImage);
            carName = itemView.findViewById(R.id.carName);
            carMiles = itemView.findViewById(R.id.carMiles);
            carModel = itemView.findViewById(R.id.carModel);
        }
    }
}
