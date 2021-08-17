package com.nick.mobile.dev.videostreaming;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.nick.mobile.dev.videostreaming.databinding.RecyclerViewFragmentLayoutBinding;

public class RecyclerViewFragment extends Fragment {

    MainActivity mainActivity;

    public RecyclerViewFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        RecyclerViewFragmentLayoutBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.recycler_view_fragment_layout,
                container,
                false);
        binding.recyclerview.setAdapter(new RecyclerViewAdapter(mainActivity));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(binding.getRoot().getContext(), RecyclerView.VERTICAL, false);
        binding.recyclerview.setLayoutManager(layoutManager);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(binding.recyclerview);
        return binding.getRoot();
    }
}
