package pl.robertsadlowski.awardroutes.timetableModule.web;

public class RequestBodyCreator {

    public String getRequestBody(String fromCode, String toCode){
        return "initialSearch=&" +
                "returnFlights=&" +
                "Flights.From=&" +
                "Flights.FromCountryId=&" +
                "Flights.FromCityId=" + fromCode +"&" +
                "Flights.FromId=" + fromCode +"&" +
                "Flights.FromLocationType=1&" +
                "Flights.To=&" +
                "Flights.ToCountryId=&" +
                "Flights.ToCityId=" + toCode +"&" +
                "Flights.ToId=" + toCode +"&" +
                "Flights.ToLocationType=1&" +
                "Flights.DepartingOn=23%2F10%2F2017&" +
                "Flights.ReturningOn=&" +
                "Flights.CarrierId=&" +
                "Flights.Connections=false&" +
                "Flights.CodeShares=false&" +
                "Flights.ExpandDays=true&" +
                "Flights.ExpandDays=false";
    }

}
