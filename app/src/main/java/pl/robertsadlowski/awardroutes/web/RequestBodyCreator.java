package pl.robertsadlowski.awardroutes.web;

public class RequestBodyCreator {

    public String getRequestBody(String fromCode, String toCode){
        return "initialSearch=&" +
                "returnFlights=&" +
                "Flights.From=Tokyo&" +
                "Flights.FromCountryId=JP&" +
                "Flights.FromCityId=TYO&" +
                "Flights.FromId=TYO&" +
                "Flights.FromLocationType=1&" +
                "Flights.To=London&" +
                "Flights.ToCountryId=GB&" +
                "Flights.ToCityId=LON&" +
                "Flights.ToId=LON&" +
                "Flights.ToLocationType=1&" +
                "Flights.DepartingOn=22%2F10%2F2017&" +
                "Flights.ReturningOn=&" +
                "Flights.CarrierId=&" +
                "Flights.Connections=false&" +
                "Flights.CodeShares=false&" +
                "Flights.ExpandDays=true&" +
                "Flights.ExpandDays=false";
    }

}
