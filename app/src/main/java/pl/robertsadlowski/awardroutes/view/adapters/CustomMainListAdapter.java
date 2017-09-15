package pl.robertsadlowski.awardroutes.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import pl.robertsadlowski.awardroutes.R;
import pl.robertsadlowski.awardroutes.app.gateaway.FormAirportData;
import pl.robertsadlowski.awardroutes.app.gateaway.FormPossibles;
import pl.robertsadlowski.awardroutes.view.activities.SelectAirports;


public class CustomMainListAdapter extends ArrayAdapter<FormAirportData> {

    private final int REQEST_CODE = 1;
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
            convertView = vi.inflate(R.layout.list_item_airport, parent, false);
            holder.airportNr = (TextView) convertView.findViewById(R.id.airportNr);
            holder.airportName = (TextView) convertView.findViewById(R.id.airportCity);
            holder.airportCountry = (TextView) convertView.findViewById(R.id.airportCountry);
            holder.airlineLeg = (TextView) convertView.findViewById(R.id.airline);
            convertView.setTag(holder);
        }
        else {
            holder = (Holder) convertView.getTag();
        }
        int pos = position + 1;
        holder.airportNr.setText("AIRPORT " + pos + ":");
        holder.airportName.setText(formAirportData.name);
        holder.airportCountry.setText(formAirportData.country);
        holder.airlineLeg.setText(formPossibles.getAirline(position));
        holder.airportName.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    View parentRow = (View) v.getParent();
                    ListView listView = (ListView) parentRow.getParent();
                    final int position = listView.getPositionForView(parentRow);
                    Intent intent = new Intent(context, SelectAirports.class);
                    ArrayList<String> airportList = new ArrayList<String>(formPossibles.getAirports(position));
                    airportList.add(0,"All");
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
                    final int position = listView.getPositionForView(parentRow);
                    Intent intent = new Intent(context, SelectAirports.class);
                    ArrayList<String> countryList = new ArrayList<String>(formPossibles.getCountries(position));
                    countryList.add(0,"All");
                    intent.putExtra("Type","Country");
                    intent.putStringArrayListExtra("List", countryList);
                    intent.putExtra("Position",position);
                    ((Activity) context).startActivityForResult(intent,REQEST_CODE);
                }
            });

        return convertView;


    }

    static class Holder {
        TextView airportNr;
        TextView airportName;
        TextView airportCountry;
        TextView airlineLeg;
    }
}
