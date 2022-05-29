package com.example.travelcompanyapplication.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.travelcompanyapplication.GuideViewBuilder;
import com.example.travelcompanyapplication.databinding.FragmentGuideBinding;
import com.example.travelcompanyapplication.db_controller.TravelCompanyDBHelper;


public class GuideFragment extends Fragment {
    private View view;
    private FragmentGuideBinding binding;
    private ActionBar actionBar;
    private GuideViewBuilder guideViewBuilder;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}