package com.example.project_temp2.ui.contact;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.project_temp2.R;
import com.example.project_temp2.database.DataBaseHelper;
import com.example.project_temp2.database.User;
import com.example.project_temp2.databinding.FragmentContactBinding;
import com.example.project_temp2.shared.Dealer;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactFragment extends Fragment {

    private FragmentContactBinding binding;

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentContactBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        DataBaseHelper dataBaseHelper = new DataBaseHelper(root.getContext(), "project", null, 1);

        User getInfo;

        if (Dealer.getInstance().dealer != null) {
            getInfo = dataBaseHelper.getUserPhoneAndLocation(Dealer.getInstance().dealer);
        } else {
            getInfo = new User();
            getInfo.setPhoneNumber("+970 599000000");
            getInfo.setEmail("CarDealer@cars.com");
            getInfo.setCountry("Palestine");
            getInfo.setCity("Ramallah");
        }

        CircleImageView phoneImg = (CircleImageView) root.findViewById(R.id.phoneContact);
        CircleImageView emailImg = (CircleImageView) root.findViewById(R.id.emailContact);
        CircleImageView locationImg = (CircleImageView) root.findViewById(R.id.locationContact);

        TextView phone = (TextView) root.findViewById(R.id.phoneNumber);
        TextView email = (TextView) root.findViewById(R.id.emailAddress);
        TextView location = (TextView) root.findViewById(R.id.location);

        phone.setText(getInfo.getPhoneNumber());
        email.setText(getInfo.getEmail());
        location.setText(getInfo.getCountry() + "\n" + getInfo.getCity());

        phoneImg.setImageResource(R.color.cyan);
        emailImg.setImageResource(R.color.cyan);
        locationImg.setImageResource(R.color.cyan);

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dialIntent = new Intent();
                dialIntent.setAction(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:" + getInfo.getPhoneNumber()));
                startActivity(dialIntent);
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gmailIntent = new Intent();
                gmailIntent.setAction(Intent.ACTION_SENDTO);
                gmailIntent.setType("message/rfc822");
                gmailIntent.setData(Uri.parse("mailto:"));
                gmailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{getInfo.getEmail()});
                gmailIntent.putExtra(Intent.EXTRA_SUBJECT, "Refund Car");
                gmailIntent.putExtra(Intent.EXTRA_TEXT, "Content of the message");
                startActivity(gmailIntent);
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapsIntent = new Intent();
                mapsIntent.setAction(Intent.ACTION_VIEW);
                mapsIntent.setData(Uri.parse("geo:19.076,72.8777"));
                startActivity(mapsIntent);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}