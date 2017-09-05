package pl.robertsadlowski.awardroutes;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import pl.robertsadlowski.awardroutes.app.Application;
import pl.robertsadlowski.awardroutes.app.gateaway.FormChoosen;
import pl.robertsadlowski.awardroutes.app.gateaway.FormPossibles;
import pl.robertsadlowski.awardroutes.app.logic.Container;
import pl.robertsadlowski.awardroutes.app.logic.ContainerManager;

public class ScrollingActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    ArrayList<String> airportList;
    private Container container;
    private FormChoosen formChoosen;
    private FormPossibles formPossibles;
    private final int REQEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Resources resources = getResources();

        Application application = new Application(resources);
        ContainerManager containerManager = application.getContainerManager();
        int countainerSize = 4;
        container = containerManager.addCountainer(countainerSize);

        formChoosen = container.getFormChoosen();
        formPossibles = container.setRouteLines(formChoosen);
        airportList = new ArrayList<String>(formChoosen.getAirportList());
        listView = (ListView) findViewById(R.id.listViewMain);
        adapter = new ArrayAdapter<String>(this, R.layout.row, airportList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                                    long arg3) {
                if(pos > -1)
                {
                    Intent intent = new Intent(ScrollingActivity.this, SelectAirports.class);
                    ArrayList<String> airportList = new ArrayList<String>(formPossibles.getAirports(pos));
                    airportList.add(0,"All");
                    intent.putStringArrayListExtra("AirportList", airportList);
                    intent.putExtra("Position",pos);
                    startActivityForResult(intent,REQEST_CODE);
                }
            }
        });


        //mList = getListData();
        //MyAdapter listAdapter = new MyAdapter(this, R.layout.row, mList);


/*
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getApplicationContext(),
                        "Click ListItem Number " + position, Toast.LENGTH_LONG)
                        .show();
            }
        });
*/
    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String getAirport = intent.getStringExtra("Choosen Airport");
                int pos = intent.getIntExtra("Pos",0);
                formChoosen.setAirport(pos,getAirport);
                formPossibles = container.setRouteLines(formChoosen);
                airportList = new ArrayList<String>(formChoosen.getAirportList());
                adapter = new ArrayAdapter<String>(this, R.layout.row, airportList);
                listView.setAdapter(adapter);
            }}};


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    */
}
