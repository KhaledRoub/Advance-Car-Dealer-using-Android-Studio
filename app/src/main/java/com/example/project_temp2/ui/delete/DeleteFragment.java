package com.example.project_temp2.ui.delete;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_temp2.R;
import com.example.project_temp2.database.DataBaseHelper;
import com.example.project_temp2.database.User;
import com.example.project_temp2.databinding.FragmentDeleteBinding;
import com.example.project_temp2.databinding.FragmentLogoutBinding;

import java.util.List;

public class DeleteFragment extends Fragment {

    private RecyclerView recyclerView;
    private DeleteAdapter adapter;
    private DataBaseHelper dbHelper;

    public DeleteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete, container, false);

        recyclerView = view.findViewById(R.id.deleteRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext(), "project", null, 1);

        List<User> userList = dataBaseHelper.getAllCustomer();
        adapter = new DeleteAdapter(userList, getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }
}