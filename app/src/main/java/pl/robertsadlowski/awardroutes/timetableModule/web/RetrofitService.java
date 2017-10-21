package pl.robertsadlowski.awardroutes.timetableModule.web;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RetrofitService {

    @Headers({
            "Host: starmap.fltmaps.com",
            "User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64; rv:56.0) Gecko/20100101 Firefox/56.0",
            "Accept: */*",
            "Accept-Language: pl,en-US;q=0.7,en;q=0.3",
            //"Accept-Encoding: gzip, deflate",
            "Content-Type: application/x-www-form-urlencoded; charset=UTF-8",
            "X-Requested-With: XMLHttpRequest",
            "Referer: http://starmap.fltmaps.com/en/other",
            "Cookie: ROUTEID=.4",
            "DNT: 1",
            "Connection: keep-alive"
    })
    @POST("flights")
    Call<ResponseBody> getHtml(@Body String requestBody);
}
