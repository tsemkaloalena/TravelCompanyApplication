package com.example.travelcompanyapplication.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.travelcompanyapplication.FlightsViewBuilder;
import com.example.travelcompanyapplication.MainActivity;
import com.example.travelcompanyapplication.R;
import com.example.travelcompanyapplication.databinding.FragmentFlightsBinding;
import com.example.travelcompanyapplication.databinding.FragmentHotelsBinding;
import com.example.travelcompanyapplication.db_controller.TravelCompanyDBHelper;

public class FlightsFragment extends Fragment {
    private View view;
    private FragmentFlightsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFlightsBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        TravelCompanyDBHelper travelCompanyDBHelper = new TravelCompanyDBHelper(getContext());
        FlightsViewBuilder flightsViewBuilder = new FlightsViewBuilder(travelCompanyDBHelper, inflater, container);

        view = inflater.inflate(R.layout.fragment_flights, container, false);
        flightsViewBuilder.setupMenu(view);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.flights_container);
        ViewGroup parent = (ViewGroup) layout.getParent();
        int index = parent.indexOfChild(layout);
        parent.removeView(layout);
        layout = flightsViewBuilder.getLayout();
        parent.addView(layout, index);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}