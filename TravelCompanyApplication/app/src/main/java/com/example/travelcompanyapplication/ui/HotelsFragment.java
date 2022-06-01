package com.example.travelcompanyapplication.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.travelcompanyapplication.HotelsViewBuilder;
import com.example.travelcompanyapplication.MainActivity;
import com.example.travelcompanyapplication.R;
import com.example.travelcompanyapplication.databinding.FragmentGuideBinding;
import com.example.travelcompanyapplication.databinding.FragmentHotelsBinding;
import com.example.travelcompanyapplication.db_controller.TravelCompanyDBHelper;

public class HotelsFragment extends Fragment {
    private FragmentHotelsBinding binding;
    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHotelsBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        TravelCompanyDBHelper travelCompanyDBHelper = new TravelCompanyDBHelper(getContext());
        HotelsViewBuilder hotelsViewBuilder = new HotelsViewBuilder(travelCompanyDBHelper, inflater, container);

        view = inflater.inflate(R.layout.fragment_hotels, container, false);
        hotelsViewBuilder.setupMenu(view);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.hotels_container);
        ViewGroup parent = (ViewGroup) layout.getParent();
        int index = parent.indexOfChild(layout);
        parent.removeView(layout);
        layout = hotelsViewBuilder.getLayout();
        parent.addView(layout, index);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}