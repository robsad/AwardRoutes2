package pl.robertsadlowski.awardroutes.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import pl.robertsadlowski.awardroutes.R;
import pl.robertsadlowski.awardroutes.app.gateaway.FormAirportData;
import pl.robertsadlowski.awardroutes.app.gateaway.FormPossibles;
import pl.robertsadlowski.awardroutes.view.activities.MainActivity;
import pl.robertsadlowski.awardroutes.view.activities.SelectAirports;
import pl.robertsadlowski.awardroutes.view.activities.TimeTableActivity;


public class CustomMainListAdapter extends ArrayAdapter<FormAirportData> {

    private final int REQEST_CODE = 1;
    private final String ANY_AIRPOPT = "Any airport";
    private final String ANY_COUNTRY = "Any country";
    Context context;
    FormPossibles formPossibles;

    public CustomMainListAdapter(Context context, ArrayList<FormAirportData> formAirportData,FormPossibles formPossibles) {
        super(context, 0, formAirportData);
        this.context = context;
        this.formPossibles = formPossibles;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FormAirportData formAirportData = getItem(position);
        Holder holder;
        if (convertView  == null) {
            holder = new Holder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            //  (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.list_item_main, parent, false);
            holder.airportNr = (TextView) convertView.findViewById(R.id.airportNr);
            holder.airportName = (TextView) convertView.findViewById(R.id.airportCity);
            holder.airportCountry = (TextView) convertView.findViewById(R.id.airportCountry);
            holder.airlineLeg = (TextView) convertView.findViewById(R.id.airline);
            holder.arrow_right = (ImageView) convertView.findViewById(R.id.arrow_right);
            holder.relativeclic =(RelativeLayout)convertView.findViewById(R.id.airline_button);
            convertView.setTag(holder);
        }
        else {
            holder = (Holder) convertView.getTag();
        }
        int pos = position + 1;
        holder.airportNr.setText("AIRPORT " + pos + ":");
        holder.airportName.setText(formAirportData.name);
        holder.airportCountry.setText(formAirportData.country);
        holder.airportName.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    View parentRow = (View) v.getParent();
                    ListView listView = (ListView) parentRow.getParent();
                    final int position = listView.getPositionForView(parentRow)-1;
                    Intent intent = new Intent(context, SelectAirports.class);
                    ArrayList<String> airportList = new ArrayList<String>(formPossibles.getAirports(position));
                    airportList.add(0,ANY_AIRPOPT);
                    intent.putExtra("Type","Airport City");
                    intent.putStringArrayListExtra("List", airportList);
                    intent.putExtra("Position",position);
                    ((Activity) context).startActivityForResult(intent,REQEST_CODE);
                }
            });
        holder.airportCountry.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    View parentRow = (View) v.getParent();
                    ListView listView = (ListView) parentRow.getParent();
                    final int position = listView.getPositionForView(parentRow)-1;
                    Intent intent = new Intent(context, SelectAirports.class);
                    ArrayList<String> countryList = new ArrayList<String>(formPossibles.getCountries(position));
                    countryList.add(0,ANY_COUNTRY);
                    intent.putExtra("Type","Country");
                    intent.putStringArrayListExtra("List", countryList);
                    intent.putExtra("Position",position);
                    ((Activity) context).startActivityForResult(intent,REQEST_CODE);
                }
            });
        String airline = formPossibles.getAirline(position);
        if (airline.equals("")) {
            holder.airlineLeg.setText("Flight " + pos);
        } else {
            holder.airlineLeg.setText("Flight " + pos + ": " + airline);
            holder.arrow_right.setVisibility(View.VISIBLE);
            holder.relativeclic.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    View parentRow = (View) v.getParent();
                    ListView listView = (ListView) parentRow.getParent();
                    final int position = listView.getPositionForView(parentRow) - 1;
                    Intent intent = new Intent(context, TimeTableActivity.class);
                    MainActivity mainActivity = (MainActivity) context;
                    intent.putExtra("mode", mainActivity.getMode());
                    intent.putExtra("Origin", formPossibles.getChoosenPortsCodes(position));
                    intent.putExtra("Destination", formPossibles.getChoosenPortsCodes(position + 1));
                    ((Activity) context).startActivity(intent);
                }
            });
        }
        if (pos==formPossibles.getSize()) {
            holder.airlineLeg.setText("");
        }
        return convertView;
    }

    static class Holder {
        TextView airportNr;
        TextView airportName;
        TextView airportCountry;
        TextView airlineLeg;
        ImageView arrow_right;
        RelativeLayout relativeclic;
    }
}
