package com.example.travelcompanyapplication.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.example.travelcompanyapplication.GuideViewBuilder;
import com.example.travelcompanyapplication.R;
import com.example.travelcompanyapplication.databinding.FragmentGuideBinding;
import com.example.travelcompanyapplication.db_controller.TravelCompanyDBHelper;

import java.util.List;


public class GuideFragment extends Fragment {
    private View view;
    private FragmentGuideBinding binding;
    private ActionBar actionBar;
    private GuideViewBuilder guideViewBuilder;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = FragmentGuideBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        TravelCompanyDBHelper travelCompanyDBHelper = new TravelCompanyDBHelper(getContext());
        actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        guideViewBuilder = new GuideViewBuilder(travelCompanyDBHelper, inflater, container, actionBar);
        view = guideViewBuilder.getView();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                actionBar.setDisplayHomeAsUpEnabled(false);
                guideViewBuilder.createCityListView();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}