package pl.robertsadlowski.awardroutes.view.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import pl.robertsadlowski.awardroutes.R;
import pl.robertsadlowski.awardroutes.app.Application;
import pl.robertsadlowski.awardroutes.app.gateaway.FormAirportData;
import pl.robertsadlowski.awardroutes.app.gateaway.FormChoosen;
import pl.robertsadlowski.awardroutes.app.gateaway.FormPossibles;
import pl.robertsadlowski.awardroutes.app.logic.Container;
import pl.robertsadlowski.awardroutes.app.logic.ContainerManager;
import pl.robertsadlowski.awardroutes.view.adapters.CustomMainListAdapter;
import pl.robertsadlowski.awardroutes.view.map.WorldMap;

public class MainActivity extends AppCompatActivity {

    private final int REQEST_CODE = 1;
    private Toolbar toolbar;
    private ListView listView;
    private Button buttonZoneStart;
    private Button buttonZoneEnd;
    private TextView textZoneStartEnd;
    private TextView textMileage;
    private Application application;
    private int countainerSize;
    private ContainerManager containerManager;
    private Container container;
    private FormChoosen formChoosen;
    private FormPossibles formPossibles;
    private WorldMap mapa;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Award Routes Finder");
        }
        toolbar.setSubtitle("for Miles&More Programme");
        listView = (ListView) findViewById(R.id.listViewMain);
        LayoutInflater layoutinflater = getLayoutInflater();
        ViewGroup footerView = (ViewGroup)layoutinflater.inflate(R.layout.list_view_footer,listView,false);
        listView.addFooterView(footerView);
        ViewGroup headerView = (ViewGroup)layoutinflater.inflate(R.layout.list_view_header,listView,false);
        listView.addHeaderView(headerView);
        buttonZoneStart = (Button) findViewById(R.id.zoneStart);
        buttonZoneEnd  = (Button) findViewById(R.id.zoneEnd);
        textZoneStartEnd = (TextView) findViewById(R.id.textZoneStartEnd);
        textMileage = (TextView) findViewById(R.id.textMileage);
        buttonZoneStart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                openButtonDialog("start");
            }});
        buttonZoneEnd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                openButtonDialog("end");
            }});
        Resources resources = getResources();
        logicStartConfiguration(resources);
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

    private void openButtonDialog(String phase){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(MainActivity.this);
        myDialog.setTitle("Choose " + phase + " zone");
        final String[] targetArray =  getButtonTargetArray(phase);
        final String phaseZone = phase;
        myDialog.setItems(targetArray, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String item = targetArray[which];
                String itemView = targetArray[which];
                if (item.equals(Container.ANY_ZONE)) {
                    itemView = "Select " + phaseZone + " zone";
                }
                if (phaseZone.equals("start")) {
                    buttonZoneStart.setText(itemView);
                    formChoosen.setStartZone(item);
                } else {
                    buttonZoneEnd.setText(itemView);
                    formChoosen.setEndZone(item);
                }
                formPossibles = container.calculateRoutes(formChoosen);
                formsUpdate();
            }});
        myDialog.setNegativeButton("Cancel", null);
        myDialog.show();
    }

    private String[] getButtonTargetArray(String phase) {
        int position;
        if (phase.equals("start"))  {
            position = 0;
        }
        else {
            position = countainerSize-1;
        }
        ArrayList<String> targetArray = new ArrayList<String>(formPossibles.getZones(position));
        targetArray.add(0,Container.ANY_ZONE);
        return targetArray.toArray(new String[targetArray.size()]);
    }

    private void logicStartConfiguration(Resources resources) {
        application = new Application(resources);
        getCountainerManager("MilesMore");
        initCountainer(4);
    }

    private void initMap(Resources resources) {
        mapa = new WorldMap();
        //LinearLayout mapaLayout = (LinearLayout) findViewById(R.id.world_map);
        ImageView img=(ImageView)findViewById(R.id.imageWorldMap);
        mapa.initMap(img, resources, countainerSize, containerManager.getAirportsModule());
    }

    private void getCountainerManager(String programmeName) {
        toolbar.setSubtitle("for " +  programmeName + " Programme");
        containerManager = application.getContainerManager(programmeName);
    }

    private void initCountainer(int size) {
        countainerSize = size;
        container = containerManager.addCountainer(countainerSize);
        formChoosen = container.getFormChoosen();
        if (countainerSize == 5) {
            formChoosen.setZoneRule(false);
        }
        formPossibles = container.calculateRoutes(formChoosen);
        buttonZoneStart.setText("Select start zone");
        buttonZoneEnd.setText("Select end zone");
        initMap(getResources());  //mapa
        formsUpdate();
    }

    private void formsUpdate() {
        ArrayList<FormAirportData> arrayOfUsers = new ArrayList<FormAirportData>(formChoosen.getRouteDataList());
        CustomMainListAdapter adapter = new CustomMainListAdapter(this, arrayOfUsers, formPossibles);
        listView.setAdapter(adapter);
        updateMileage();
        mapa.setAirportOnMap(formChoosen,formPossibles); //mapa
    }

    private void updateMileage() {
        String zonesRoute = formPossibles.getZoneStart() + " -> " + formPossibles.getZoneEnd();
        textZoneStartEnd.setText(zonesRoute);
        if (formPossibles.getMessage()!=null) {
            showToast(formPossibles.getMessage());
        }
        int milesY = formPossibles.getMileageNeeded().getY()*500;
        int milesC = formPossibles.getMileageNeeded().getC()*500;
        String message = "Select route to know how many miles you need";
        if (milesY>0) {
            message = "Required one way miles: " + milesY + "(Y)";
            if (milesC>0) message = message + ", " + milesC + "(C)";
        }
        textMileage.setText(message);
    }

    private void showToast(String toast) {
        Toast.makeText(this, toast, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_new3segments) {
            initCountainer(3);
            return true;
        }
        if (id == R.id.action_new4segments) {
            initCountainer(4);
            return true;
        }
        if (id == R.id.action_new5segments) {
            initCountainer(5);
            return true;
        }
        if (id == R.id.action_MMmode) {
            getCountainerManager("MilesMore");
            initCountainer(4);
            return true;
        }
        if (id == R.id.action_AAmode) {
            getCountainerManager("AAdvantage");
            initCountainer(4);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
