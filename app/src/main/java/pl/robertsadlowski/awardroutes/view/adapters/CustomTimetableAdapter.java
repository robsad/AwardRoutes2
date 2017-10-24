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
            holder.daysOfOperations = (TextView) convertView.findViewById(R.id.timetableDaysOfOperations);

            convertView.setTag(holder);
        }
        else {
            holder = (Holder) convertView.getTag();
        }
        holder.originName.setText(timetableConnection.getOriginCode()+" "+timetableConnection.getOriginName());
        holder.destinationName.setText(timetableConnection.getDestinationCode()+" "+timetableConnection.getDestinationName());
        holder.time.setText(timetableConnection.getDepartureTime()+"-"+timetableConnection.getArrivalTime());
        holder.duration.setText(timetableConnection.getDuration());
        holder.airline.setText(timetableConnection.getAirline());
        holder.flightNr.setText(timetableConnection.getFlightNr());
        holder.aircraft.setText(timetableConnection.getAircraft());
        holder.distance.setText(timetableConnection.getDistance());
        holder.stops.setText(timetableConnection.getStops());
        holder.daysOfOperations.setText("Mon,Tue,Wed,Thu,Fri,Sat,Sun");

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
        TextView daysOfOperations;
    }

}
