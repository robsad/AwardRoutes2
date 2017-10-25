package pl.robertsadlowski.awardroutes.timetableModule.web;


import java.util.ArrayList;
import java.util.List;

import pl.robertsadlowski.awardroutes.timetableModule.entities.TimetableConnection;

public class ServiceTrimHTML {


    public List<TimetableConnection> parse(String html) {
        List<TimetableConnection> timetableConnectionList = new ArrayList<>();
        html = trimHeaderFooter(html);
        html = html.replace("\n", "").replace("\r", "");
        String[] arrayItems = html.split("<tr class=\"th-l-activebg\" id=\"result-summary");
        for (int i=1; i<arrayItems.length ;i++) {
            TimetableConnection timetableConnection = parseData(arrayItems[i]);
            timetableConnectionList.add(timetableConnection);
        }
        return timetableConnectionList;
    }

    private String trimHeaderFooter(String html) {
        int startPosition = html.indexOf("Share Results <span data-icon=\"X\" aria-hidden=\"true\"></span></a></td>");
        int endPosition = html.indexOf("<textarea data-map-route-data=");
        html.substring(startPosition, endPosition);
        return html;
    }

    private TimetableConnection parseData(String htmlTimetableConnection) {
        int startPosition = htmlTimetableConnection.indexOf("dates-of-operation results-details-schedule");
        htmlTimetableConnection.substring(startPosition,htmlTimetableConnection.length());
        String[] array = htmlTimetableConnection.split("detail-value\">");
        for (int i=1; i<array.length ;i++) {
            int cutPostition = array[i].indexOf("</span>");
            if (cutPostition>0) {
                array[i] = array[i].substring(0,cutPostition);
            }
        }
        String departureTime = array[1];
        String arrivalTime = array[2];
        String originCode = "";
        String originName = cutHref(array[4]);
        String destinationCode = "";
        String destinationName = cutHref(array[5]);
        String airline = cutAny(cutP(array[6]));
        String flightNr = "FlightNr: "+array[3];
        String aircraft = "Aircraft: "+array[9];
        String duration = "Duration: "+array[16];
        String distance = "Distance: "+array[15];
        String stops = "Stops: "+cutP(array[12]);
        boolean[] daysOfOperation = getDaysOfOperation(array[0]);
        TimetableConnection timetableConnection = new TimetableConnection(departureTime,arrivalTime,originCode,originName,destinationCode,destinationName,airline,flightNr,aircraft,duration,distance,stops,daysOfOperation);
        return timetableConnection;
    }

    private boolean[] getDaysOfOperation(String html) {
        boolean[] daysOfOperation = {false, false, false, false, false, false, false};
        String[] array = html.split("</span></td>");
        for (int i=0; i<7 ;i++) {
            if (array[i].contains("Available")) {
                daysOfOperation[i] = true;
            }
        }
        return daysOfOperation;
    }

    private String cutP(String html) {
        int startPosition = html.indexOf(">");
        int endPosition = html.indexOf("</");
        html = html.substring(startPosition+1, endPosition);
        return html;
    }

    private String cutHref(String html) {;
        int endPosition = html.indexOf("<a");
        html = html.substring(0, endPosition);
        html = html.replace("<small>&lrm;","");
        html = html.replace("&lrm;</small>","");
        return html;
    }

    private String cutAny(String html) {;
        int endPosition = html.indexOf("<");
        if (endPosition>0) {
            html = html.substring(0, endPosition);
        }
        return html;
    }
}
