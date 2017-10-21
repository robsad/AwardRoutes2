package pl.robertsadlowski.awardroutes.timetableModule;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.robertsadlowski.awardroutes.timetableModule.entities.TimetableConnection;

public class ServiceTrimHTML {


    public List<TimetableConnection> parse(String html) {
        List<TimetableConnection> timetableConnectionList = new ArrayList<>();
        html = trimHeaderFooter(html);
        String[] array = html.split("<tr class=\"th-l-activebg\" id=\"result-summary-2\">");
        List<String> timetableRawList = Arrays.asList(array);
        for(String htmlTimetableConnection : timetableRawList) {
            parseData(htmlTimetableConnection);
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
            System.out.println(array[i]);
        }
        return htmlTimetableConnection;
    }

}
