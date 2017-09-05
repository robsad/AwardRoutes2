package pl.robertsadlowski.awardroutes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class SelectAirports extends AppCompatActivity {

    private ListView listView ;
    private ArrayAdapter<String> adapter ;
    private ArrayList<String> airportList;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_airports);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        Intent intent = getIntent();
        airportList = intent.getStringArrayListExtra("AirportList");
        position = intent.getIntExtra("Pos",0);

        listView = (ListView) findViewById(R.id.listViewAirports);
        adapter = new ArrayAdapter<String>(this, R.layout.row, airportList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                                    long arg3) {
                if(pos > -1)
                {
                    Intent intent = new Intent();
                    intent.putExtra("Choosen Airport", airportList.get(pos));
                    intent.putExtra("Pos", position);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

    }

}
