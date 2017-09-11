package pl.robertsadlowski.awardroutes.view.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import pl.robertsadlowski.awardroutes.R;
import pl.robertsadlowski.awardroutes.app.Application;
import pl.robertsadlowski.awardroutes.app.gateaway.FormAirportData;
import pl.robertsadlowski.awardroutes.app.gateaway.FormChoosen;
import pl.robertsadlowski.awardroutes.app.gateaway.FormPossibles;
import pl.robertsadlowski.awardroutes.app.logic.Container;
import pl.robertsadlowski.awardroutes.app.logic.ContainerManager;
import pl.robertsadlowski.awardroutes.view.adapters.CustomMainListAdapter;

public class ScrollingActivity extends AppCompatActivity {

    private ListView listView;
    private Button buttonZoneStart;
    private Button buttonZoneEnd;
    private TextView textZoneStart;
    private TextView textZoneEnd;
    private TextView textMileage;

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
        formPossibles = container.calculateRoutes(formChoosen);

        listView = (ListView) findViewById(R.id.listViewMain);
        //adapter = new ArrayAdapter<String>(this, R.layout.row, airportList);  //simple adapter
        ArrayList<FormAirportData> arrayOfUsers = new ArrayList<FormAirportData>(formChoosen.getRouteDataList());
        CustomMainListAdapter adapter = new CustomMainListAdapter(this, arrayOfUsers, formPossibles);
        listView.setAdapter(adapter);

/*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                                    long arg3) {
                if(pos < 4)
                {
                    Intent intent = new Intent(ScrollingActivity.this, SelectAirports.class);
                    ArrayList<String> airportList = new ArrayList<String>(formPossibles.getAirports(pos));
                    airportList.add(0,"All");
                    intent.putExtra("Type","Airport City");
                    intent.putStringArrayListExtra("List", airportList);
                    intent.putExtra("Position",pos);
                    startActivityForResult(intent,REQEST_CODE);
                }
            }
        });
*/

        buttonZoneStart = (Button) findViewById(R.id.zoneStart);
        buttonZoneEnd  = (Button) findViewById(R.id.zoneEnd);
        textZoneStart = (TextView) findViewById(R.id.textZoneStart);
        textZoneEnd = (TextView) findViewById(R.id.textZoneEnd);
        textMileage = (TextView) findViewById(R.id.textMileage);
        buttonZoneStart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                openButtonDialog();
            }});

    }


    private void openButtonDialog(){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(ScrollingActivity.this);
        myDialog.setTitle("Choose Zone");
        final String[] targetArray = formPossibles.getZones(0).toArray(new String[formPossibles.getZones(0).size()]);
        myDialog.setItems(targetArray, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String item = targetArray[which];
                buttonZoneStart.setText(item);
            }});
        myDialog.setNegativeButton("Cancel", null);
        myDialog.show();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        System.out.println("requestCode: " + requestCode + " resultCode " + resultCode);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String getAirport = intent.getStringExtra("Choosen");
                int pos = intent.getIntExtra("Position",0);
                String type = intent.getStringExtra("Type");
                System.out.println("Return type: " + type + " getAirport " + getAirport);
                if (type.equals("Airport City")) {
                    formChoosen.setAirport(pos,getAirport);
                } else {
                    System.out.println("getCountry i: " + pos);
                    formChoosen.setCountry(pos,getAirport);
                }
                formPossibles = container.calculateRoutes(formChoosen);
                formsUpdate();
            }
        }
    };

    private void formsUpdate() {
        ArrayList<FormAirportData> arrayOfUsers = new ArrayList<FormAirportData>(formChoosen.getRouteDataList());
        CustomMainListAdapter adapter = new CustomMainListAdapter(this, arrayOfUsers, formPossibles);
        listView.setAdapter(adapter);
        updateMileage();
    }

    private void updateMileage() {
        textZoneStart.setText(formPossibles.getZoneStart());
        textZoneEnd.setText(formPossibles.getZoneEnd());
        textMileage.setText(String.valueOf(formPossibles.getMileageNeeded())+"miles");
    }


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
