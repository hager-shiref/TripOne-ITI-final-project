package com.Team.Tripawy;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.Team.Tripawy.Room.RDB;
import com.Team.Tripawy.models.Trip;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class RVAdaptor extends RecyclerView.Adapter<RVAdaptor.ViewHolder> {
    List <Trip> list=new ArrayList<Trip>();
    Context cntxt;

    LiveData<List<Trip>> listLiveData ;
    Trip trip;


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
        holder.trip_state.setText(list.get(position).getTripState());
        Calendar cal = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yy HH:mm", Locale.ENGLISH);
        try {
            cal.setTime(sdf.parse(list.get(position).getDate()+" "+list.get(position).getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(cntxt, holder.popup);
                popupMenu.inflate(R.menu.menu);
                trip=new Trip();
                int ID=list.get(position).getId();
                trip.setId(ID);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.addnote) {


                            Intent intent =new Intent(cntxt,AddNoteActivity.class);
                            int id =list.get(position).getId();
                            intent.putExtra("id",id);
                           // Toast.makeText(cntxt, id+"", Toast.LENGTH_SHORT).show();
                            List<String> data =new ArrayList<>();
                            data.add(list.get(position).getName());
                            data.add(list.get(position).getDate());
                            data.add(list.get(position).getTime());
                            data.add(list.get(position).getTripType());
                            data.add(list.get(position).getTo());
                            data.add(list.get(position).getFrom());
                            data.add(list.get(position).getTripState());
                            intent.putStringArrayListExtra("data", (ArrayList<String>) data);
                            cntxt.startActivity(intent);

                        }
                        if (item.getItemId() == R.id.delete) {
                                trip.setTripState("Deleted");
                            Executors.newSingleThreadExecutor().execute(() ->{
                                RDB.getTrips(v.getContext()).delete(trip);
                            });
                            Toast.makeText(cntxt, "deleted", Toast.LENGTH_SHORT).show();

                        }
                        if(item.getItemId()==R.id.cancel){
                            trip.setTripState("canceld");
                            trip.setName(list.get(position).getName());
                            trip.setDate(list.get(position).getDate());
                            trip.setTime(list.get(position).getTime());
                            trip.setTripType(list.get(position).getTripType());
                            trip.setTo(list.get(position).getTo());
                            trip.setFrom(list.get(position).getFrom());
                            trip.setNotes(list.get(position).getNotes());
                            Executors.newSingleThreadExecutor().execute(() ->{
                                RDB.getTrips(v.getContext()).update(trip);
                            });
                            Intent intent = new Intent(cntxt, AlarmService.class);
                            PendingIntent pendingIntent = PendingIntent.getService(
                                    cntxt.getApplicationContext(), (int) cal.getTimeInMillis(),intent , PendingIntent.FLAG_ONE_SHOT);
                            pendingIntent.cancel();
                            Toast.makeText(cntxt, "canceld", Toast.LENGTH_SHORT).show();

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
                Executors.newSingleThreadExecutor().execute(() -> {
                    RDB.getTrips(cntxt).insert(
                            new Trip(list.get(position).getName(),
                                    list.get(position).getDate(),
                                    list.get(position).getTime(),
                                    "Done",
                                    "One Way",
                                    list.get(position).getFrom(),
                                    list.get(position).getTo(),
                                    list.get(position).getNotes()));
                });
                Intent intent2 = new Intent(cntxt, AlarmService.class);
                PendingIntent pendingIntent = PendingIntent.getService(
                        cntxt.getApplicationContext(), (int) cal.getTimeInMillis(),intent2 , PendingIntent.FLAG_ONE_SHOT);
                pendingIntent.cancel();
                trip=new Trip();
                int ID=list.get(position).getId();
                trip.setId(ID);
                Executors.newSingleThreadExecutor().execute(() ->{
                    RDB.getTrips(v.getContext()).delete(trip);
                });
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + list.get(position).getTo());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                cntxt.startActivity(mapIntent);
                Intent intent =new Intent(v.getContext(), FloatingViewService.class);
                List<String> notes =list.get(position).getNotes();
                intent.putStringArrayListExtra("data", (ArrayList<String>) notes);
                cntxt.startService(intent);
            }
        });

        holder.note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List <String> notes =new ArrayList<>();
                notes=list.get(position).getNotes();
               openDialog(notes);


            }
        });
        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> data =new ArrayList<>();
                data.add(list.get(position).getName());
                data.add(list.get(position).getFrom());
                data.add(list.get(position).getTo());
                data.add(list.get(position).getDate());
                data.add(list.get(position).getTime());
                data.add(list.get(position).getId()+"");
                List<String> notes =list.get(position).getNotes();
                Intent updateIntent =new Intent(cntxt,Update_Trip.class);
                updateIntent.putStringArrayListExtra("data", (ArrayList<String>) data);
                updateIntent.putStringArrayListExtra("notes", (ArrayList<String>) notes);
                cntxt.startActivity(updateIntent);

            }
        });


    }

    private void openDialog(List <String>notes) {
        String text="";
        for(int i=0;i<notes.size();i++)
        {
            text=text+"\n"+notes.get(i);
        }
        AlertDialog dialog =new AlertDialog.Builder(cntxt)
                .setTitle("Your notes")
                .setMessage(text)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
        dialog.show();

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView start, end, date, time, name,trip_state;
        Button popup, startnowBtn;
        ImageButton note;
        LinearLayout linear;

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
            trip_state=itemView.findViewById(R.id.trip_state);
            linear=itemView.findViewById(R.id.linear);

        }
    }
}
