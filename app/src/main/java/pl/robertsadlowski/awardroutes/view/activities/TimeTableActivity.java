package pl.robertsadlowski.awardroutes.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import pl.robertsadlowski.awardroutes.R;
import pl.robertsadlowski.awardroutes.timetableModule.ServiceTrimHTML;
import pl.robertsadlowski.awardroutes.timetableModule.entities.TimetableConnection;
import pl.robertsadlowski.awardroutes.timetableModule.web.RequestBodyCreator;
import pl.robertsadlowski.awardroutes.timetableModule.web.RetrofitService;
import pl.robertsadlowski.awardroutes.timetableModule.web.ServiceGenerator;
import pl.robertsadlowski.awardroutes.view.adapters.CustomTimetableAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimeTableActivity extends AppCompatActivity {

    private ListView listView;
    private List<TimetableConnection> timetableList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        listView = (ListView) findViewById(R.id.listViewTimetable);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        Intent intent = getIntent();
        String origin = intent.getStringExtra("Origin");
        String destination = intent.getStringExtra("Destination");

        RequestBodyCreator requestBodyCreator = new RequestBodyCreator();
        String body = requestBodyCreator.getRequestBody("POZ", "MUC");

        RetrofitService taskService = ServiceGenerator.createService(RetrofitService.class);
        Call<ResponseBody> call = taskService.getHtml(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String html = response.body().string();
                        listViewDataFeed(html);
                    } catch (IOException e) {
                        Log.d("POST html", e.getMessage());
                    }
                } else {
                    Log.d("POST Error", "response.isWrong");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });

    }

    private void listViewDataFeed(String rawHTML) {
        ServiceTrimHTML serviceTrimHTML = new ServiceTrimHTML();
        timetableList = serviceTrimHTML.parse(rawHTML);
        timetableList.add(new TimetableConnection("POZ","Poznan Lawica","MUC","Monich","Lufthansa","LH3456","1:20","non-stop"));
        CustomTimetableAdapter adapter = new CustomTimetableAdapter(this, timetableList);
        listView.setAdapter(adapter);
    }

}
