package com.example.project_temp2.ui.reserves;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_temp2.R;
import com.example.project_temp2.Signup;
import com.example.project_temp2.database.DataBaseHelper;
import com.example.project_temp2.database.ReservedCarDetails;
import com.example.project_temp2.shared.AllCars;
import com.example.project_temp2.shared.SignedUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class ReservesAdapter extends RecyclerView.Adapter<ReservesAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<ReservedCarDetails> carArrayList;

    private final String fragment;

    public ReservesAdapter(Context context, ArrayList<ReservedCarDetails> carArrayList, String fragment) {
        this.context = context;
        this.carArrayList = carArrayList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ReservesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reserves_admin_card, parent, false);
        return new ReservesAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReservedCarDetails model = carArrayList.get(position);

        DataBaseHelper dataBaseHelper = new DataBaseHelper(context, "project", null, 1);

        double ratingValue = dataBaseHelper.getRatingForCarByDate(model.getCarId(), model.getReservationDT());

        if (fragment.equals("reservations admin")) {
            holder.ratingBar.setVisibility(View.INVISIBLE);
            holder.ratingBar.setClickable(false);
            holder.rateButton.setVisibility(View.INVISIBLE);
            holder.rateButton.setClickable(false);
        } else {
            holder.rateButton.setVisibility(View.VISIBLE);
            holder.rateButton.setClickable(true);
            holder.ratingBar.setVisibility(View.VISIBLE);
            holder.ratingBar.setClickable(true);
        }

        if (fragment.equals("reservations admin") || (fragment.equals("reservations") && model.getReturnDT() != null)) {
            holder.returnCar.setVisibility(View.INVISIBLE);
            holder.returnCar.setClickable(false);
        } else {
            holder.returnCar.setVisibility(View.VISIBLE);
            holder.returnCar.setClickable(true);
        }

        holder.carNameAdmin.setText(model.getManufacturer() + " " + model.getType());
        holder.carModelAdmin.setText(model.getModel());
        holder.carMilesAdmin.setText(model.getMiles() + " miles");
        holder.carImage.setImageResource(model.getImage());

        Context context = holder.itemView.getContext();
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.filter_dialog);

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.reserver_details_dialog);
        dialog.getWindow().setBackgroundDrawable(drawable);

        Dialog dialogReturn = new Dialog(context);
        dialogReturn.setContentView(R.layout.return_dialog);
        dialogReturn.getWindow().setBackgroundDrawable(drawable);

        Dialog dialogRating = new Dialog(context);
        dialogRating.setContentView(R.layout.rating_dialog);
        dialogRating.getWindow().setBackgroundDrawable(drawable);

        TextView reservedBy2 = dialog.findViewById(R.id.reservedBy2);
        TextView reservedByEmail2 = dialog.findViewById(R.id.reservedByEmail2);
        TextView manufactureConfirm2 = dialog.findViewById(R.id.manufactureConfirm2);
        TextView modelConfirm2 = dialog.findViewById(R.id.modelConfirm2);
        TextView yearConfirm2 = dialog.findViewById(R.id.yearConfirm2);
        TextView priceConfirm2 = dialog.findViewById(R.id.priceConfirm2);
        TextView offersConfirm2 = dialog.findViewById(R.id.offersConfirm2);
        TextView reserveDate2 = dialog.findViewById(R.id.reserveDate2);
        TextView reserveTime2 = dialog.findViewById(R.id.reserveTime2);

        TextView rateStatement = dialogRating.findViewById(R.id.submitStatement);
        Button submitRate = (Button) dialogRating.findViewById(R.id.acceptRating);
        Button cancelRating = (Button) dialogRating.findViewById(R.id.cancelRating);

        Button confirmReturn = (Button) dialogReturn.findViewById(R.id.acceptReturn);
        Button cancelReturn = (Button) dialogReturn.findViewById(R.id.cancelReturn);

        reservedBy2.setText(model.getReserverName());
        reservedByEmail2.setText(model.getReserverEmail());
        manufactureConfirm2.setText(model.getManufacturer());
        modelConfirm2.setText(model.getType());
        yearConfirm2.setText(model.getModel());

        if (ratingValue > 0) {
            holder.ratingBar.setRating((float) ratingValue);
        }

        int priceWithOffer = (int) ((int) model.getPrice() - (model.getPrice() * (model.getPromoPercentage() / 100)));
        priceConfirm2.setText(Double.toString(priceWithOffer) + "$");
        offersConfirm2.setText(Double.toString(model.getPrice()) + "$");

        reserveDate2.setText(model.getReservationDT());
        reserveTime2.setText(model.getReturnDT());

        holder.detailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        holder.returnCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogReturn.show();
            }
        });

        confirmReturn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String returnDT = dateFormat.format(new Date());

                dataBaseHelper.updateReturnDate(SignedUser.getInstance().user.getEmail(), model.getCarId(), returnDT);
                dataBaseHelper.updateReservedStatus(model.getCarId(), 0);
                AllCars.getInstance().cars.get(model.getCarId() - 1).setReserved(1);

                model.setReturnDT(returnDT);

                dialogReturn.cancel();
                notifyDataSetChanged();
            }
        });

        cancelReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogReturn.cancel();
            }
        });

        holder.rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rateStatement.setText("Submit Rating: " + holder.ratingBar.getRating());
                dialogRating.show();
            }
        });

        cancelRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogRating.dismiss();
            }
        });

        submitRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataBaseHelper.updateReservationRating(SignedUser.getInstance().user.getEmail(), model.getCarId(), model.getReservationDT(), holder.ratingBar.getRating());
                Toast.makeText(context, "Your rating submitted successfully", Toast.LENGTH_SHORT).show();
                dialogRating.dismiss();
            }
        });

    }

    @Override
    public int getItemCount() {
        return carArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView carImage;
        private final ImageView returnCar;
        private final TextView carNameAdmin;
        private final TextView carMilesAdmin;
        private final TextView carModelAdmin;
        private final Button detailsBtn;
        private final RatingBar ratingBar;
        private final TextView rateButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            carImage = itemView.findViewById(R.id.carImage);
            returnCar = itemView.findViewById(R.id.returnCar);
            carNameAdmin = itemView.findViewById(R.id.carNameAdmin);
            carMilesAdmin = itemView.findViewById(R.id.carMilesAdmin);
            carModelAdmin = itemView.findViewById(R.id.carModelAdmin);
            detailsBtn = itemView.findViewById(R.id.detailsBtn);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            rateButton = itemView.findViewById(R.id.rateButton);
        }
    }
}
