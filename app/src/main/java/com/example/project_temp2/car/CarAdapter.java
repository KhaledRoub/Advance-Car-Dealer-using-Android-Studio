package com.example.project_temp2.car;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_temp2.CarDetailsFragment;
import com.example.project_temp2.R;
import com.example.project_temp2.database.DataBaseHelper;
import com.example.project_temp2.database.Favorites;
import com.example.project_temp2.shared.AllCars;
import com.example.project_temp2.shared.SignedUser;

import java.util.ArrayList;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.ViewHolder> {

    private final Context context;
    private String fragment;
    private final ArrayList<CarModel> carArrayList;

    public CarAdapter(Context context, ArrayList<CarModel> carArrayList, String fragment) {
        this.context = context;
        this.carArrayList = carArrayList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public CarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull CarAdapter.ViewHolder holder, int position) {
        CarModel model = carArrayList.get(position);
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context, "project", null, 1);

        double carRatingValue = dataBaseHelper.getAverageRatingForCar(model.getId());
        dataBaseHelper.updateCarRating(model.getId(), carRatingValue);

        if (model.getReserved() == 1)
            holder.carBtn.setTextColor(Color.GRAY);
        else
            holder.carBtn.setTextColor(Color.WHITE);

        holder.carName.setText(model.getManufacturer() + " " + model.getType());
        holder.carPrice.setText("$" + Math.round(model.getPrice()));
        holder.carModel.setText(model.getModel());
        holder.carMiles.setText(model.getMiles() + " miles");
        holder.carRating.setText(String.format("%.1f", carRatingValue));
        holder.carImage.setImageResource(model.getImage());

        Context context = holder.itemView.getContext();

        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.filter_dialog);

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.confrim_reserve);
        dialog.getWindow().setBackgroundDrawable(drawable);
        dialog.setCancelable(false);

        TextView manu2 = (TextView) dialog.findViewById(R.id.manufactureConfirm2);
        TextView model2 = (TextView) dialog.findViewById(R.id.modelConfirm2);
        TextView year2 = (TextView) dialog.findViewById(R.id.yearConfirm2);
        TextView price2 = (TextView) dialog.findViewById(R.id.priceConfirm2);
        TextView mileage2 = (TextView) dialog.findViewById(R.id.mileageConfirm2);
        TextView offer2 = (TextView) dialog.findViewById(R.id.offersConfirm2);

        Button confirm = (Button) dialog.findViewById(R.id.confirmReserve);
        Button cancel = (Button) dialog.findViewById(R.id.cancelReserve);

        manu2.setText(model.getManufacturer());
        model2.setText(model.getType());
        year2.setText(model.getModel());
        price2.setText(Float.toString(model.getPrice()));
        mileage2.setText(Float.toString(model.getMiles()));
        offer2.setText(Float.toString(model.getPromoPercentage()));

        if (model.getPromoPercentage() > 0 && (fragment.equals("special") || fragment.equals("favorite"))) {
            holder.carPrice.setTextColor(Color.parseColor("#FF0000"));
            holder.carDay.setTextColor(Color.parseColor("#FF0000"));
            holder.carPrice.setPaintFlags(holder.carPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.carDay.setPaintFlags(holder.carDay.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            holder.priceWithOffer.setVisibility(View.VISIBLE);
            holder.dayWithOffer.setVisibility(View.VISIBLE);

            int priceWithOffer = (int) ((int) model.getPrice() - (model.getPrice() * (model.getPromoPercentage() / 100)));

            holder.priceWithOffer.setText(Integer.toString(priceWithOffer));
            holder.dayWithOffer.setVisibility(View.VISIBLE);

        }


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int priceWithOffer = (int) ((int) model.getPrice() - (model.getPrice() * (model.getPromoPercentage() / 100)));

                dataBaseHelper.insertReservation(SignedUser.getInstance().user.getEmail(), model.getId(), priceWithOffer, model.getPromoPercentage());
                dataBaseHelper.updateReservedStatus(model.getId(), 1);
                model.setReserved(1);
                AllCars.getInstance().cars.get(model.getId() - 1).setReserved(1);

                Toast.makeText(context, "Car reserved successfully", Toast.LENGTH_SHORT).show();
                holder.carBtn.setTextColor(Color.GRAY);
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        if (model.getStatus() == 0)
            holder.favClick.setImageResource(R.drawable.favorite_border);
        else
            holder.favClick.setImageResource(R.drawable.favorite_filled);


        holder.carName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCarDetailsFragment(model, ((FragmentActivity) context).getSupportFragmentManager());
            }
        });

        holder.favClick.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {

                if (model.getStatus() == 0) {

                    holder.favClick.startAnimation(AnimationUtils.loadAnimation(context, R.anim.favorite_animation));

                    model.setStatus(1);
                    AllCars.getInstance().cars.get(model.getId() - 1).setStatus(1);

                    SignedUser signedUser = SignedUser.getInstance();
                    DataBaseHelper databaseHelper = new DataBaseHelper(context, "project", null, 1);

                    databaseHelper.insertFavorite(new Favorites(model.getId(), signedUser.user.getEmail()));

                    databaseHelper.close();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            holder.favClick.setImageResource(R.drawable.favorite_filled);
                        }
                    }, 800);


                } else {
                    holder.favClick.startAnimation(AnimationUtils.loadAnimation(context, R.anim.favorite_animation));

                    model.setStatus(0);
                    AllCars.getInstance().cars.get(model.getId() - 1).setStatus(0);

                    SignedUser signedUser = SignedUser.getInstance();
                    DataBaseHelper databaseHelper = new DataBaseHelper(context, "project", null, 1);

                    int favoriteId = databaseHelper.getFavoriteId(model.getId(), signedUser.user.getEmail());
                    databaseHelper.removeFavorite(favoriteId);

                    databaseHelper.close();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            holder.favClick.setImageResource(R.drawable.favorite_border);
                        }
                    }, 800);

                }

                if (fragment.equals("favorite")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            carArrayList.remove(model);
                            notifyDataSetChanged();
                        }
                    }, 900);
                }


            }
        });

        holder.carBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model.getReserved() == 0)
                    dialog.show();
                else {
                    Toast.makeText(context, "The car is not available for reservation", Toast.LENGTH_SHORT).show();
                }
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
        private final ImageView favClick;
        private final TextView carName;
        private final TextView carPrice;
        private final TextView carDay;
        private final TextView carMiles;
        private final TextView carModel;
        private final TextView priceWithOffer;
        private final TextView dayWithOffer;
        private final TextView carRating;

        private final Button carBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            carImage = itemView.findViewById(R.id.carImage);
            carName = itemView.findViewById(R.id.carName);
            carPrice = itemView.findViewById(R.id.carPrice);
            carMiles = itemView.findViewById(R.id.carMiles);
            carBtn = itemView.findViewById(R.id.reserveBtn);
            favClick = itemView.findViewById(R.id.favClick);
            carModel = itemView.findViewById(R.id.carModel);
            carDay = itemView.findViewById(R.id.dayPrice);
            priceWithOffer = itemView.findViewById(R.id.priceWithOffer);
            dayWithOffer = itemView.findViewById(R.id.dayWithOffer);
            carRating = itemView.findViewById(R.id.carRating);
        }
    }

}
