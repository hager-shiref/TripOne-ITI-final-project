package com.Team.Tripawy.ui.upcoming;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.Team.Tripawy.R;
import com.Team.Tripawy.RVAdaptor;
import com.Team.Tripawy.models.Trip;

import java.util.ArrayList;
import java.util.List;

public class UpcomingFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_upcoming, container, false);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Trip> trips = new ArrayList<>();
        Trip trip =new Trip(1,"mm","23","24","mans","cairo");
        trips.add(trip);
        trips.add(trip);
        trips.add(trip);
        trips.add(trip);
        RecyclerView recyclerView=view.findViewById(R.id.rv_trip);
        RVAdaptor homeAdaptor =new RVAdaptor(trips,getActivity());
        recyclerView.setAdapter(homeAdaptor);


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}