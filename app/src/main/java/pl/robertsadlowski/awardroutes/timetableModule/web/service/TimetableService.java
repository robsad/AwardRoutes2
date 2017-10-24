package pl.robertsadlowski.awardroutes.timetableModule.web.service;

import okhttp3.ResponseBody;
import pl.robertsadlowski.awardroutes.view.activities.MainActivity;
import retrofit2.Call;

public class TimetableService {

    private final String mode;
    private final String URL_SA = "http://starmap.fltmaps.com/en/other/";
    private final String URL_OW = "http://onw.fltmaps.com/en/";

    public TimetableService(String mode) {
        this.mode=mode;
    }

    public Call<ResponseBody> getCall(String body) {
        if (mode.equals(MainActivity.MILESMORE)) {
            RetrofitService taskService = ServiceGenerator.createService(RetrofitService.class);
            return taskService.getStarAllianceHtml(body);
        } else {
            RetrofitService taskService = ServiceGenerator.createService(RetrofitService.class);
            return taskService.getOneWorldHtml(body);
        }
    }

}
