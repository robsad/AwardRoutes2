package pl.robertsadlowski.awardroutes.app.data.rulesModule;

import java.util.List;
import java.util.Set;

import pl.robertsadlowski.awardroutes.app.gateaway.FormChoosen;

public interface ZoneFilter {
	void calculateZonesAndConfigureFilter(FormChoosen formChoosen);
    String getStartZone();
    String getEndZone();
}
