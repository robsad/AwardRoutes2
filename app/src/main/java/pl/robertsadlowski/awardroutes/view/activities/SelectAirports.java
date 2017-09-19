package pl.robertsadlowski.awardroutes.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import pl.robertsadlowski.awardroutes.R;

public class SelectAirports extends AppCompatActivity {

    private ListView listView ;
    private ArrayAdapter<String> adapter ;
    private ArrayList<String> airportList;
    private int position;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_airports);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        Intent intent = getIntent();
        type = intent.getStringExtra("Type");
        airportList = intent.getStringArrayListExtra("List");
        position = intent.getIntExtra("Position",0);

        listView = (ListView) findViewById(R.id.listViewAirports);
        adapter = new ArrayAdapter<String>(this, R.layout.list_item_airport, airportList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                                    long arg3) {
                if(pos > -1)
                {
                    Intent intent = new Intent();
                    intent.putExtra("Choosen", airportList.get(pos));
                    intent.putExtra("Position", position);
                    intent.putExtra("Type", type);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

    }

}
