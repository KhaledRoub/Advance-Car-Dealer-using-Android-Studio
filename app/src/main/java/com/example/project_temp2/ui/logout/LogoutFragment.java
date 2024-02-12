package com.example.project_temp2.ui.logout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.project_temp2.R;
import com.example.project_temp2.databinding.FragmentLogoutBinding;
import com.example.project_temp2.Logout; // Import your Logout class

public class LogoutFragment extends Fragment {

    private FragmentLogoutBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLogoutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        showLogoutDialog();

        return root;
    }

    private void showLogoutDialog() {
        Logout logoutFragment = new Logout();

        FragmentManager fragmentManager = getChildFragmentManager();
        logoutFragment.show(fragmentManager, "logout_dialog");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
