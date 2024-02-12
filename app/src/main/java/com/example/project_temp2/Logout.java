package com.example.project_temp2;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.project_temp2.shared.SignedUser;

public class Logout extends DialogFragment {

    public Logout() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.logout, null);

        final Button logout = (Button) view.findViewById(R.id.log);
        final Button cancel = (Button) view.findViewById(R.id.cancel);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationManager notificationManager = (NotificationManager) requireActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                if (notificationManager != null) {
                    notificationManager.cancelAll();
                }
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;

                if (SignedUser.getInstance().user.getRole() == 0) {
                    intent = new Intent(getActivity(), NormalCustomer.class);
                } else intent = new Intent(getActivity(), Admin.class);

                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }
}