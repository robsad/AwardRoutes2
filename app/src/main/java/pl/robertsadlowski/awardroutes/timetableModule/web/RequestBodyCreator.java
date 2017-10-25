package pl.robertsadlowski.awardroutes.timetableModule.web;

public class RequestBodyCreator {

    public String getRequestBody(String fromCode, String toCode, String date){
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
                "Flights.DepartingOn=" + date + "&" +
                "Flights.ReturningOn=&" +
                "Flights.CarrierId=&" +
                "Flights.Connections=false&" +
                "Flights.CodeShares=false&" +
                "Flights.ExpandDays=true&" +
                "Flights.ExpandDays=false";
    }

}
