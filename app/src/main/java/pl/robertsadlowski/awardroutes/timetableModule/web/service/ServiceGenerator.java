package pl.robertsadlowski.awardroutes.timetableModule.web.service;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ServiceGenerator {

    private final static String URL_SA = "http://starmap.fltmaps.com/en/other/";
    private final static String URL_OW = "http://onw.fltmaps.com/en/";

    private static OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(URL_OW)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    //.addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.addInterceptor(setLogging())
                    .build());

    private static Retrofit retrofit = builder.build();

    private static HttpLoggingInterceptor setLogging() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logging;
    }

    public static <S> S createService(
            Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
