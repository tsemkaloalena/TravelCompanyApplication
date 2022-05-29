package com.example.travelcompanyapplication.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.travelcompanyapplication.FlightsViewBuilder;
import com.example.travelcompanyapplication.MainActivity;
import com.example.travelcompanyapplication.databinding.FragmentFlightsBinding;
import com.example.travelcompanyapplication.db_controller.TravelCompanyDBHelper;

public class FlightsFragment extends Fragment {
    private View view;
    private FragmentFlightsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TravelCompanyDBHelper travelCompanyDBHelper = new TravelCompanyDBHelper(getContext());
        FlightsViewBuilder flightsViewBuilder = new FlightsViewBuilder(travelCompanyDBHelper, inflater, container);
        view = flightsViewBuilder.getView();
        MainActivity.removeView(view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}