package com.example.project_temp2;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.project_temp2.database.DataBaseHelper;
import com.example.project_temp2.shared.SignedUser;
import com.google.android.material.navigation.NavigationView;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project_temp2.databinding.ActivityNormalCustomerBinding;

import de.hdodenhof.circleimageview.CircleImageView;

public class NormalCustomer extends AppCompatActivity {

    private DataBaseHelper dbHelper;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNormalCustomerBinding binding;
    private static final String CHANNEL_ID = "offers";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNormalCustomerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarNormalCustomer.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_car, R.id.nav_reservations, R.id.nav_contacts,
                R.id.nav_logout, R.id.nav_profile, R.id.nav_special_offers, R.id.nav_favorites)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_normal_customer);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerView = navigationView.getHeaderView(0);

        TextView name = (TextView) headerView.findViewById(R.id.userName);
        TextView email = (TextView) headerView.findViewById(R.id.userEmail);
        CircleImageView userImage = (CircleImageView) headerView.findViewById(R.id.userImage);

        Glide.with(NormalCustomer.this)
                .load(SignedUser.getInstance().user.getProfileURI())
                .into(userImage);

        name.setText(SignedUser.getInstance().user.getFirstName() + " " + SignedUser.getInstance().user.getLastName());
        email.setText(SignedUser.getInstance().user.getEmail());

        dbHelper = new DataBaseHelper(NormalCustomer.this, "project", null, 1);

        if (dbHelper.hasCarsWithPromo()) {
            showNotification();
            // Check if the activity was opened from a notification
            if (getIntent().getBooleanExtra("fromNotification", false)) {
                String fragmentToOpen = getIntent().getStringExtra("fragmentToOpen");
                if (fragmentToOpen != null && fragmentToOpen.equals("SpecialFragment")) {
                    navController.navigate(R.id.nav_special_offers);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_normal_customer);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            //  importance
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "special offers and promotions", importance);
            channel.setDescription("my channel");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showNotification() {
        createNotificationChannel(); // Ensure the channel is created
        // Create an explicit intent for the activity in your app.
        Intent intent = new Intent(this, NormalCustomer.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Set the extra to indicate that it's coming from the notification and specify the fragment to open
        intent.putExtra("fromNotification", true);
        intent.putExtra("fragmentToOpen", "SpecialFragment");

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        // Set your custom icon here (replace R.drawable.custom_icon with your icon resource)
        int customIconResId = R.drawable.baseline_notifications_active_24;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(customIconResId)
                .setContentTitle("Special offers!")
                .setContentText("Super deal on car .....")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        int notificationId = 1;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(notificationId, builder.build());
    }

}