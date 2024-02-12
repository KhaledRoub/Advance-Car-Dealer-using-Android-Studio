package com.example.project_temp2.ui.delete;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project_temp2.R;
import com.example.project_temp2.database.DataBaseHelper;
import com.example.project_temp2.database.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DeleteAdapter extends RecyclerView.Adapter<DeleteAdapter.MyViewHolder> {
    private Context context;

    private List<User> userModels;

    public DeleteAdapter(List<User> userModels, Context context) {
        this.userModels = userModels;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_delete_user, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        User user = userModels.get(position);
        holder.name.setText(user.getFirstName() + " " + user.getLastName());
        holder.email.setText(user.getEmail());
        Glide.with(holder.itemView.getContext())
                .load(user.getProfileURI())
                .into(holder.circleImageView);
    }

    @Override
    public int getItemCount() {
        return userModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, lastName, email;
        CircleImageView circleImageView;


        public MyViewHolder(View itemView) {

            super(itemView);
            name = itemView.findViewById(R.id.nametext);
            lastName = itemView.findViewById(R.id.lastName);
            email = itemView.findViewById(R.id.emailtext);
            circleImageView = itemView.findViewById(R.id.img1);
            View delete = itemView.findViewById(R.id.deleteicon); // Adjust with the actual ID
            DataBaseHelper dataBaseHelper = new DataBaseHelper(context, "project", null, 1);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());

                    builder.setTitle("Delete Customer");
                    builder.setMessage("Delete...?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Implement your delete logic here
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                try {
                                    // Replace "khaled@gmail.com" with the actual user's email
//                                    dataBaseHelper.deleteUserByEmail(email.toString());
                                    String clickedEmail = userModels.get(position).getEmail();
                                    dataBaseHelper.deleteUserByEmail(clickedEmail);
                                    userModels.remove(position);
                                    notifyDataSetChanged();
                                    Toast.makeText(itemView.getContext(), "User deleted successfully", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(itemView.getContext(), "Error deleting user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                // Notify your activity/fragment about the delete action
                                // (You can use an interface callback here)
                            }
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Handle the "No" button click if needed
                        }
                    });

                    builder.show();
                }
            });
        }
    }
}

