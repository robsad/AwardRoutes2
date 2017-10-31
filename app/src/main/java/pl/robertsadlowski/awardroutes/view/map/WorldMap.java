package pl.robertsadlowski.awardroutes.view.map;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.widget.ImageView;

import java.util.Set;

import pl.robertsadlowski.awardroutes.R;
import pl.robertsadlowski.awardroutes.app.data.airports.Airports;
import pl.robertsadlowski.awardroutes.app.data.entities.AirportsData;
import pl.robertsadlowski.awardroutes.app.gateaway.FormChoosen;
import pl.robertsadlowski.awardroutes.app.gateaway.FormPossibles;
import pl.robertsadlowski.awardroutes.app.logic.Container;

public class WorldMap {

    private Canvas canvas;
    private Paint paint;
    private Bitmap background;
    private Bitmap bg;
    private Airports airports;
    private int countainerSize;
    private int h = 1056;
    private int d = 2400;
    private int hfix = (int) (h*1.152);
    private int dfix = (int) (d*1.017);
    private int offset = (int) (d*0.0321);

    public void initMap(ImageView img, Resources res, int countainerSize, Airports airports) {
        this.countainerSize = countainerSize;
        this.airports=airports;
        paint = new Paint();
        Bitmap backgroundResource = BitmapFactory.decodeResource(res, R.drawable.world);
        background = Bitmap.createScaledBitmap(backgroundResource, d, h, true);
        bg = Bitmap.createBitmap(d, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bg);
        img.setImageBitmap(bg);
    }


    public void setAirportOnMap(FormChoosen formChoosen, FormPossibles formPossibles) {
        clear();
        for (int i = 0; i < countainerSize; i++) {
            String choosenAirport = formChoosen.getAirport(i);
            if (choosenAirport.equals(Container.ANY_AIRPORT)) {
                Set<String> possibleAirports = formPossibles.getAirports(i);
                for (String airport : possibleAirports) {
                    if (!airport.equals(Container.ANY_AIRPORT)) {
                        AirportsData airportData = airports.getAirportByName(airport);
                        putPointSmall(airportData);
                    }
                }
            }
        }
        for (int i = 0; i < countainerSize; i++) {
            String choosenAirport = formChoosen.getAirport(i);
            if (!choosenAirport.equals(Container.ANY_AIRPORT)) {
                AirportsData airportData = airports.getAirportByName(choosenAirport);
                putPoint(airportData);
            }
        }
        for (int i = 1; i < countainerSize; i++) {
            String choosenAirportNow = formChoosen.getAirport(i);
            String choosenAirportLast = formChoosen.getAirport(i-1);
            if ((!choosenAirportNow.equals(Container.ANY_AIRPORT))&&(!choosenAirportLast.equals(Container.ANY_AIRPORT))) {
                AirportsData startAirportData = airports.getAirportByName(choosenAirportLast);
                AirportsData endAirportData = airports.getAirportByName(choosenAirportNow);
                drawArc(startAirportData,endAirportData);
            }
        }
    }

    private void putPoint(AirportsData airportData){
        int x = convertLon(airportData.getLon());
        int y = convertLat(airportData.getLat());
        paint.setARGB(255, 255, 0, 0);
        canvas.drawCircle(x, y, 12, paint);
    }

    private void drawArc(AirportsData startAirportData,AirportsData endAirportData) {
        int startX = convertLon(startAirportData.getLon());
        int startY = convertLat(startAirportData.getLat());
        int stopX = convertLon(endAirportData.getLon());
        int stopY = convertLat(endAirportData.getLat());
        GeoCoordinates midGeoCoordinates = GreatCircle.getHalfWayLong(startAirportData,endAirportData);
        int midX = convertLon(midGeoCoordinates.getLon());
        int midY = convertLat(midGeoCoordinates.getLat());
        paint.setARGB(255, 255, 0, 0);
        paint.setStrokeWidth(5);
        canvas.drawLine(startX, startY, midX, midY, paint);
        canvas.drawLine(midX, midY, stopX, stopY, paint);
    }

    private void putPointSmall(AirportsData airportData){
        int x = convertLon(airportData.getLon());
        int y = convertLat(airportData.getLat());
        paint.setARGB(255, 0, 255, 0);
        canvas.drawCircle(x, y, 6, paint);
    }

    private int convertLon(double lon) {
        int x =  (int) (((dfix/360.0) * (180 + lon))-offset);
        return x;
    }

    private int convertLat(double lat) {
        int y =  (int) (((hfix/180.0) * (90 - lat))-2);
        return y;
    }

    private void clear() {
        Paint clearPaint = new Paint();
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawRect(0,0,d,h,clearPaint);
        canvas.drawBitmap(background, 0, 0, null);
    }

}
