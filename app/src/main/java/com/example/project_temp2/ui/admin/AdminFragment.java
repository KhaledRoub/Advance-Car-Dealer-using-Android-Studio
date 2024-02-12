package com.example.project_temp2.ui.admin;

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
import com.example.project_temp2.Signup;
import com.example.project_temp2.databinding.FragmentAdminBinding;
import com.example.project_temp2.databinding.FragmentContactBinding;
import com.example.project_temp2.shared.SignedUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminFragment extends Fragment {

    private FragmentAdminBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdminBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView noAccess = root.findViewById(R.id.no_access);

        if (SignedUser.getInstance().user.getRole() == 1) {
            noAccess.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(requireActivity(), Signup.class);
            intent.putExtra("fromAdminFragment", true);
            startActivity(intent);
        } else {
            noAccess.setVisibility(View.VISIBLE);
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}