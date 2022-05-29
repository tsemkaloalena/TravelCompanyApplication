package com.example.travelcompanyapplication.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.travelcompanyapplication.HotelsViewBuilder;
import com.example.travelcompanyapplication.MainActivity;
import com.example.travelcompanyapplication.db_controller.TravelCompanyDBHelper;

public class HotelsFragment extends Fragment {
//    private FragmentHotelsBinding binding;
    View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        binding = FragmentHotelsBinding.inflate(inflater, container, false);
//        View view = binding.getRoot();
        TravelCompanyDBHelper travelCompanyDBHelper = new TravelCompanyDBHelper(getContext());
        HotelsViewBuilder hotelsViewBuilder = new HotelsViewBuilder(travelCompanyDBHelper, inflater, container);
        view = hotelsViewBuilder.getView();
//        MainActivity.removeView(view);
        MainActivity.removeView(view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        binding = null;
    }
}