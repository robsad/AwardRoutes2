package pl.robertsadlowski.awardroutes.timetableModule;


import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import pl.robertsadlowski.awardroutes.timetableModule.entities.TimetableConnection;

public class ServiceTrimHTML {


    public List<TimetableConnection> parse(String html) {
        List<TimetableConnection> timetableConnectionList = new ArrayList<>();
        html = trimHeaderFooter(html);
        String[] array = html.split("<tr class=\"th-l-activebg\" id=\"result-summary");
        for (int i=1; i<array.length ;i++) {
            parseData(array[i]);
        }
        return timetableConnectionList;
    }

    private String trimHeaderFooter(String html) {
        int startPosition = html.indexOf("Share Results <span data-icon=\"X\" aria-hidden=\"true\"></span></a></td>");
        int endPosition = html.indexOf("<textarea data-map-route-data=");
        html.substring(startPosition, endPosition);
        return html;
    }

    private String parseData(String htmlTimetableConnection) {
        int startPosition = htmlTimetableConnection.indexOf("<table class=\"dates-of-operation results-details-schedule");
        htmlTimetableConnection.substring(startPosition);
        String[] array = htmlTimetableConnection.split("</span>");
        for (int i=0; i<array.length ;i++) {
            int cutPostition = array[i].indexOf("<span>");
            //String item = array[i].substring(cutPostition);
            Log.d("span",array[i]);
        }
        return htmlTimetableConnection;
    }

}
