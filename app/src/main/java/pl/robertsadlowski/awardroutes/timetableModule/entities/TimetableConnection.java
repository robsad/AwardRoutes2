package pl.robertsadlowski.awardroutes.timetableModule.entities;

public class TimetableConnection {

    private String originCode;
    private String destinationCode;
    private String originName;
    private String destinationName;
    private String originTerminal;
    private String destinationTerminal;
    private String departureTime;
    private String arrivalTime;
    private String duration;
    private String airline;
    private String flightNr;
    private String aircraft;
    private String distance;
    private String stops;
    private Boolean[] daysOfOperations;

    public TimetableConnection(String originCode,String originName,String destinationCode,String destinationName,String airline,String flightNr,String duration,String stops){
        this.originCode=originCode;
        this.originName=originName;
        this.destinationCode=destinationCode;
        this.destinationName=destinationName;
        this.airline=airline;
        this.flightNr=flightNr;
        this.duration=duration;
        this.stops=stops;
        departureTime = "13:20";
        arrivalTime = "14:50";
        aircraft = "A320";
        daysOfOperations = new Boolean[]{true, true, true, true, true, true, true};
    }

    public String getOriginCode() {
        return originCode;
    }

    public String getDestinationCode() {
        return destinationCode;
    }

    public String getOriginName() {
        return originName;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public String getOriginTerminal() {
        return originTerminal;
    }

    public String getDestinationTerminal() {
        return destinationTerminal;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getDuration() {
        return duration;
    }

    public String getAirline() {
        return airline;
    }

    public String getFlightNr() {
        return flightNr;
    }

    public String getAircraft() {
        return aircraft;
    }

    public String getDistance() {
        return distance;
    }

    public String getStops() {
        return stops;
    }

    public Boolean[] getDaysOfOperations() {
        return daysOfOperations;
    }

}
