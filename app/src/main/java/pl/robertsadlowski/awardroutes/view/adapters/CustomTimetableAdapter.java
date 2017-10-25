package pl.robertsadlowski.awardroutes.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import pl.robertsadlowski.awardroutes.R;
import pl.robertsadlowski.awardroutes.timetableModule.entities.TimetableConnection;

public class CustomTimetableAdapter extends ArrayAdapter<TimetableConnection> {

    private Context context;
    private List<TimetableConnection> timetableList;

    public CustomTimetableAdapter(Context context, List<TimetableConnection> timetableList) {
        super(context, 0, timetableList);
        this.context = context;
        this.timetableList = timetableList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TimetableConnection timetableConnection = timetableList.get(position);
        Holder holder;
        if (convertView  == null) {
            holder = new Holder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            //  (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.list_item_timetable, parent, false);

            holder.originName = (TextView) convertView.findViewById(R.id.timetableOriginName);
            holder.destinationName = (TextView) convertView.findViewById(R.id.timetableDestinationName);
            holder.time = (TextView) convertView.findViewById(R.id.timetableTime);
            holder.duration = (TextView) convertView.findViewById(R.id.timetableDuration);
            holder.airline = (TextView) convertView.findViewById(R.id.timetableAirline);
            holder.flightNr = (TextView) convertView.findViewById(R.id.timetableFlightNr);
            holder.aircraft = (TextView) convertView.findViewById(R.id.timetableAircraft);
            holder.distance = (TextView) convertView.findViewById(R.id.timetableDistance);
            holder.stops = (TextView) convertView.findViewById(R.id.timetableStops);
            holder.dayOfOperations1 = (TextView) convertView.findViewById(R.id.timetableDaysOfOperations1);
            holder.dayOfOperations2 = (TextView) convertView.findViewById(R.id.timetableDaysOfOperations2);
            holder.dayOfOperations3 = (TextView) convertView.findViewById(R.id.timetableDaysOfOperations3);
            holder.dayOfOperations4 = (TextView) convertView.findViewById(R.id.timetableDaysOfOperations4);
            holder.dayOfOperations5 = (TextView) convertView.findViewById(R.id.timetableDaysOfOperations5);
            holder.dayOfOperations6 = (TextView) convertView.findViewById(R.id.timetableDaysOfOperations6);
            holder.dayOfOperations7 = (TextView) convertView.findViewById(R.id.timetableDaysOfOperations7);

            convertView.setTag(holder);
        }
        else {
            holder = (Holder) convertView.getTag();
        }
        holder.originName.setText(timetableConnection.getOriginCode()+" "+timetableConnection.getOriginName()+" -");
        holder.destinationName.setText(timetableConnection.getDestinationCode()+" "+timetableConnection.getDestinationName());
        holder.time.setText(timetableConnection.getDepartureTime()+"-"+timetableConnection.getArrivalTime());
        holder.duration.setText(timetableConnection.getDuration());
        holder.airline.setText(timetableConnection.getAirline());
        holder.flightNr.setText(timetableConnection.getFlightNr());
        holder.aircraft.setText(timetableConnection.getAircraft());
        holder.distance.setText(timetableConnection.getDistance());
        holder.stops.setText(timetableConnection.getStops());
        boolean[] daysOfOperations = timetableConnection.getDaysOfOperations();
        if (daysOfOperations[0]) holder.dayOfOperations1.setText("OK");
        if (daysOfOperations[1]) holder.dayOfOperations2.setText("OK");
        if (daysOfOperations[2]) holder.dayOfOperations3.setText("OK");
        if (daysOfOperations[3]) holder.dayOfOperations4.setText("OK");
        if (daysOfOperations[4]) holder.dayOfOperations5.setText("OK");
        if (daysOfOperations[5]) holder.dayOfOperations6.setText("OK");
        if (daysOfOperations[6]) holder.dayOfOperations7.setText("OK");

        return convertView;
    }

    static class Holder {
        TextView originName;
        TextView destinationName;
        TextView time;
        TextView duration;
        TextView airline;
        TextView flightNr;
        TextView aircraft;
        TextView distance;
        TextView stops;
        TextView dayOfOperations1;
        TextView dayOfOperations2;
        TextView dayOfOperations3;
        TextView dayOfOperations4;
        TextView dayOfOperations5;
        TextView dayOfOperations6;
        TextView dayOfOperations7;
    }

}
