package com.example.travelcompanyapplication.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.travelcompanyapplication.FlightsViewBuilder;
import com.example.travelcompanyapplication.MainActivity;
import com.example.travelcompanyapplication.R;
import com.example.travelcompanyapplication.databinding.FragmentFlightsBinding;
import com.example.travelcompanyapplication.databinding.FragmentHomeBinding;
import com.example.travelcompanyapplication.db_controller.TravelCompanyDBHelper;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private View view;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Button guideButton = (Button) view.findViewById(R.id.guide_page_button);
        guideButton.setOnClickListener(this);
        Button hotelButton = (Button) view.findViewById(R.id.hotels_page_button);
        hotelButton.setOnClickListener(this);
        Button flightButton = (Button) view.findViewById(R.id.flights_page_button);
        flightButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.guide_page_button:
                Navigation.findNavController(view).navigate(R.id.navigation_guide);

                break;
            case R.id.hotels_page_button:
                Navigation.findNavController(view).navigate(R.id.navigation_hotels);
                break;
            case R.id.flights_page_button:
                Navigation.findNavController(view).navigate(R.id.navigation_flights);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}