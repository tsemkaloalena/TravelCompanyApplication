package com.example.travelcompanyapplication.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.travelcompanyapplication.AccountViewBuilder;
import com.example.travelcompanyapplication.MainActivity;
import com.example.travelcompanyapplication.databinding.FragmentAccountBinding;
import com.example.travelcompanyapplication.db_controller.TravelCompanyDBHelper;

public class AccountFragment extends Fragment {
    private View view;
    private FragmentAccountBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TravelCompanyDBHelper travelCompanyDBHelper = new TravelCompanyDBHelper(getContext());
        AccountViewBuilder accountViewBuilder = new AccountViewBuilder(travelCompanyDBHelper, inflater, container);
        view = accountViewBuilder.createView(null, null);
        MainActivity.removeView(view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}