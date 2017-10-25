package pl.robertsadlowski.awardroutes.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

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
    private TextView headerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        listView = (ListView) findViewById(R.id.listViewTimetable);
        LayoutInflater layoutinflater = getLayoutInflater();
        ViewGroup headerView = (ViewGroup)layoutinflater.inflate(R.layout.list_item_timetable_header,listView,false);
        listView.addHeaderView(headerView);
        headerText = (TextView) findViewById(R.id.textTimetableHeader);
        headerText.setText("Waiting...");
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        Intent intent = getIntent();
        String origin = intent.getStringExtra("Origin");
        String destination = intent.getStringExtra("Destination");
        String mode = intent.getStringExtra("mode");
        Log.d("Timetable Data",getNextMondayDate());

        RequestBodyCreator requestBodyCreator = new RequestBodyCreator();
        String body = requestBodyCreator.getRequestBody(origin,destination,getNextMondayDate());

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
                        Log.d("POST html", e.getMessage());
                        errorResponse();
                    }
                } else {
                    Log.d("POST Error", "response.isWrong");
                    errorResponse();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Error", t.getMessage());
                errorResponse();
            }
        });

    }

    private void listViewDataFeed(String rawHTML) {
        ServiceTrimHTML serviceTrimHTML = new ServiceTrimHTML();
        timetableList = serviceTrimHTML.parse(rawHTML);
        CustomTimetableAdapter adapter = new CustomTimetableAdapter(this, timetableList);
        listView.setAdapter(adapter);
        headerText.setText("Results for next week:");
    }

    private void errorResponse() {
        headerText.setText("Response error");
    }

    private void noResultResponse() {
        headerText.setText("no Results");
    }

    private String getNextMondayDate() {
        Calendar currentTime = Calendar.getInstance();
        while( currentTime.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY )
            currentTime.add( Calendar.DATE, 1 );
        currentTime.add( Calendar.DATE, 3 );
        int thisYear = currentTime.get(Calendar.YEAR);
        int thisMonth = currentTime.get(Calendar.MONTH)+1;
        String month = String.format("%02d",thisMonth);
        int thisDay = currentTime.get(Calendar.DAY_OF_MONTH);
        String day = String.format("%02d",thisDay);
        return day + "%2F" + month + "%2F" + thisYear;
    }

}
