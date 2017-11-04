package pl.robertsadlowski.awardroutes.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.ResponseBody;
import pl.robertsadlowski.awardroutes.R;
import pl.robertsadlowski.awardroutes.timetableModule.entities.TimetableConnection;
import pl.robertsadlowski.awardroutes.timetableModule.web.RequestBodyCreator;
import pl.robertsadlowski.awardroutes.timetableModule.web.ServiceTrimHTML;
import pl.robertsadlowski.awardroutes.timetableModule.web.service.TimetableService;
import pl.robertsadlowski.awardroutes.view.adapters.CustomTimetableAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimeTableActivity extends AppCompatActivity {

    private ListView listView;
    private List<TimetableConnection> timetableList = new ArrayList<>();
    private Toolbar toolbar;
    private Calendar current;
    private Calendar minDate;
    private String origin;
    private String destination;
    private String mode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        listView = (ListView) findViewById(R.id.listViewTimetable);
        LayoutInflater layoutinflater = getLayoutInflater();
        ViewGroup headerView = (ViewGroup) layoutinflater.inflate(R.layout.list_item_timetable_header, listView, false);
        listView.addHeaderView(headerView);
        toolbar = (Toolbar) findViewById(R.id.toolbar_timetable);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Selected Route Timetable");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button buttonPrevWeek = (Button) findViewById(R.id.previos_week);
        Button buttonNextWeek  = (Button) findViewById(R.id.next_week);
        Intent intent = getIntent();
        origin = intent.getStringExtra("Origin");
        destination = intent.getStringExtra("Destination");
        mode = intent.getStringExtra("mode");
        minDate = getNextMondayDate();
        current = getNextMondayDate();
        timetableRequest(origin,destination,mode,current);
        buttonPrevWeek.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                current.add( Calendar.DATE, -7 );
                timetableRequest(origin,destination,mode,current);
            }});
        buttonNextWeek.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                current.add( Calendar.DATE, 7 );
                timetableRequest(origin,destination,mode,current);
            }});
    }

    private void timetableRequest(String origin,String destination,String mode,Calendar date) {
        RequestBodyCreator requestBodyCreator = new RequestBodyCreator();
        String body = requestBodyCreator.getRequestBody(origin,destination,getFormatedDate(date));

        TimetableService timetableService = new TimetableService(mode);
        Call<ResponseBody> call = timetableService.getCall(body);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String html = response.body().string();
                        if (html.contains("an error")) {
                            errorResponse();
                        } else if (html.contains("NoResults")) {
                            noResultResponse();
                        } else {
                            listViewDataFeed(html);
                        }
                    } catch (IOException e) {
                        Log.d("xPOST html", e.getMessage());
                        errorResponse();
                    }
                } else {
                    Log.d("xPOST Error", "response.isWrong");
                    errorResponse();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Error", t.getMessage());
                errorResponse();
            }
        });
        toolbar.setSubtitle(getWeekDate(date));
    }

    private void listViewDataFeed(String rawHTML) {
        ServiceTrimHTML serviceTrimHTML = new ServiceTrimHTML();
        timetableList = serviceTrimHTML.parse(rawHTML);
        CustomTimetableAdapter adapter = new CustomTimetableAdapter(this, timetableList);
        listView.setAdapter(adapter);
    }

    private void errorResponse() {
    }

    private void noResultResponse() {
    }

    private Calendar getNextMondayDate() {
        Calendar currentTime = Calendar.getInstance();
        while( currentTime.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY )
            currentTime.add( Calendar.DATE, 1 );
        currentTime.add( Calendar.DATE, 3 );
        return currentTime;
    }

    private String getFormatedDate(Calendar currentTime) {
        int thisYear = currentTime.get(Calendar.YEAR);
        int thisMonth = currentTime.get(Calendar.MONTH)+1;
        String month = String.format("%02d",thisMonth);
        int thisDay = currentTime.get(Calendar.DAY_OF_MONTH);
        String day = String.format("%02d",thisDay);
        return day + "%2F" + month + "%2F" + thisYear;
    }

    private String getWeekDate(Calendar currentTime) {
        currentTime.add( Calendar.DATE, -3 );
        int thisMonth = currentTime.get(Calendar.MONTH)+1;
        int thisDay = currentTime.get(Calendar.DAY_OF_MONTH);
        String weekDate = "For week: " + thisDay + "." + thisMonth;
        currentTime.add( Calendar.DATE, 6 );
        thisMonth = currentTime.get(Calendar.MONTH)+1;
        thisDay = currentTime.get(Calendar.DAY_OF_MONTH);
        weekDate = weekDate+" - " +thisDay+"."+thisMonth;
        return weekDate;
    }

}
