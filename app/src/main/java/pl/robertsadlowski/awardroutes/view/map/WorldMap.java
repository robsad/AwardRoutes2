package pl.robertsadlowski.awardroutes.view.map;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.widget.LinearLayout;

import java.util.Set;

import pl.robertsadlowski.awardroutes.R;
import pl.robertsadlowski.awardroutes.app.data.airports.AbstractAirports;
import pl.robertsadlowski.awardroutes.app.data.entities.AirportsData;
import pl.robertsadlowski.awardroutes.app.gateaway.FormChoosen;
import pl.robertsadlowski.awardroutes.app.gateaway.FormPossibles;
import pl.robertsadlowski.awardroutes.app.logic.Container;

public class WorldMap {

    private Canvas canvas;
    private Paint paint;
    private Bitmap tlo;
    private Bitmap bg;
    private AbstractAirports airports;
    private int countainerSize;
    private int h = 756;
    private int d = 1720;

    public void initMap(LinearLayout ll, Resources res, int countainerSize, AbstractAirports airports) {
        this.countainerSize = countainerSize;
        this.airports=airports;
        paint = new Paint();
        tlo = BitmapFactory.decodeResource(res, R.drawable.world);
        int height = tlo.getHeight();
        int width = tlo.getWidth();
        double y = Math.sqrt(400000 / (((double) width) / height));
        double x = (y / height) * width;
        //Bitmap scaledBitmap = Bitmap.createScaledBitmap(tlo, (int) x, (int) y, true);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(tlo, 1720, 756, true);
        tlo.recycle();
        tlo = scaledBitmap;
        bg = Bitmap.createBitmap(1720, 756, Bitmap.Config.ARGB_8888);
        // bg = tlo.copy(Bitmap.Config.ARGB_8888, true);
        canvas = new Canvas(bg);
        ll.setBackground(new BitmapDrawable(res, bg));
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
    }

    private void putPoint(AirportsData airportData){
        int x = convertLon(airportData.getLon());
        int y = convertLat(airportData.getLat());
        paint.setARGB(255, 255, 0, 0);
        canvas.drawCircle(x, y, 12, paint);
    }

    private void putPointSmall(AirportsData airportData){
        int x = convertLon(airportData.getLon());
        int y = convertLat(airportData.getLat());
        paint.setARGB(255, 0, 255, 0);
        canvas.drawCircle(x, y, 6, paint);
    }

    private int convertLon(double lon) {
        int x =  (int) (((1750/360.0) * (180 + lon))-55);
        return x;
    }

    private int convertLat(double lat) {
        int y =  (int) (((870/180.0) * (90 - lat)));
        return y;
    }

    private void clear() {
        Paint clearPaint = new Paint();
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawRect(0,0,d,h,clearPaint);
        canvas.drawBitmap(tlo, 0, 0, null);
    }

}
