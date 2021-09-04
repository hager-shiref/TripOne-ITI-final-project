package com.Team.Tripawy;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.Team.Tripawy.Room.RDB;
import com.Team.Tripawy.models.Trip;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Executors;

public class RVAdaptor extends RecyclerView.Adapter<RVAdaptor.ViewHolder> {
    List <Trip> list=new ArrayList<Trip>();
    Context cntxt;

    LiveData<List<Trip>> listLiveData ;


    public RVAdaptor(List<Trip> list, Context cntxt) {
        this.list = list;
        this.cntxt = cntxt;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflate = LayoutInflater.from(parent.getContext());
        View v = inflate.inflate(R.layout.recycler, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.start.setText(list.get(position).getFrom());
        holder.name.setText(list.get(position).getName());
        holder.date.setText(list.get(position).getDate()+"");
        holder.time.setText(list.get(position).getTime()+"");
        holder.end.setText(list.get(position).getTo());
        holder.popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(cntxt, holder.popup);
                popupMenu.inflate(R.menu.menu);
                Trip trip=new Trip();
                int ID=list.get(position).getId();
                trip.setId(ID);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId() == R.id.Edit){
                            trip.setTripType("Updated");
                            Intent i= new Intent(cntxt,AddTrip.class);
                           cntxt.startActivity(i);
                            Executors.newSingleThreadExecutor().execute(() ->{
                                RDB.getTrips(v.getContext()).update(trip);
                            });
                        }
                        if (item.getItemId() == R.id.addnote) {

                          /*  PopupMenu pop = new PopupMenu(cntxt, v);
                            pop.inflate(R.menu.notes_menu);
                            for (String n : list.get(position).getNotes()) {
                                pop.getMenu().add(n);
                            }
                            pop.show();*/
                            // Toast.makeText(cntxt, "no notes", Toast.LENGTH_SHORT).show();
                            Intent intent =new Intent(cntxt,AddNoteActivity.class);
                            cntxt.startActivity(intent);

                        }
                        if (item.getItemId() == R.id.delete) {
                                trip.setTripState("Deleted");
                            Executors.newSingleThreadExecutor().execute(() ->{
                                RDB.getTrips(v.getContext()).delete(trip);
                            });

                        }
                        if(item.getItemId()==R.id.cancel){
                            Executors.newSingleThreadExecutor().execute(() ->{
                                RDB.getTrips(v.getContext()).delete(trip);
                            });
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        holder.startnowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.get(position).setTripState("Done!");
                try {
                    // when google map installed
                    // intialize uri
                    Uri uri = Uri.parse("https://www.google.co.in/maps/dir/"+"Dikirnis"+"/"+"Mansoura");
                    // intialize intent with action view
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    // set package
                    intent.setPackage("com.google.android.apps.maps");
                    //set flag
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    // start activity
                    cntxt.startActivity(intent);
                } catch (ActivityNotFoundException e){
                    // when google map is not installed
                    // intialize uri
                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps&hl=en&gl=US");
                    // intialize intent with action view
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    //set flags
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    cntxt.startActivity(intent);
                }
            }
        });
        holder.note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(cntxt,ViewNotes.class);
                cntxt.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView start, end, date, time, name;
        Button popup, startnowBtn;
        ImageButton note;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            startnowBtn = itemView.findViewById(R.id.startnow);
            start = itemView.findViewById(R.id.start_loc_id);
            end = itemView.findViewById(R.id.end_loc_id);
            time = itemView.findViewById(R.id.Time_id);
            name = itemView.findViewById(R.id.trip_name_id);
            date = itemView.findViewById(R.id.Date_id);
            popup = itemView.findViewById(R.id.pop_menu_id);
            note=itemView.findViewById(R.id.note_add);

        }
    }
}
