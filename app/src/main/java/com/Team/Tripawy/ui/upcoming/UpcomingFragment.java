package com.Team.Tripawy.ui.upcoming;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.Team.Tripawy.R;
import com.Team.Tripawy.RVAdaptor;
import com.Team.Tripawy.Room.RDB;
import com.Team.Tripawy.models.Trip;

import java.util.List;

public class UpcomingFragment extends Fragment {
    RecyclerView recyclerView;
    LiveData<List<Trip>> listLiveData ;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        listLiveData = RDB.getTrips(getContext()).getAllUpcoming();

        View view=inflater.inflate(R.layout.fragment_upcoming, container, false);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(getContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getContext().getPackageName()));
                startActivityForResult(intent, 1234);
            }
        }
        // to get all upcoming trips
        listLiveData.observe((LifecycleOwner) getContext(), new Observer<List<Trip>>() {
            @Override
            public void onChanged(List<Trip> trips) {

                recyclerView=view.findViewById(R.id.rv_trip);
                RVAdaptor homeAdaptor =new RVAdaptor(trips,getActivity());
                recyclerView.setAdapter(homeAdaptor);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234) {

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}