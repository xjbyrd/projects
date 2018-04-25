package com.cooksys.handler;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cooksys.model.Location;

/**
 * Returns all possible flight locations.
 * 
 */

@Component
@Scope("singleton")
public class LocationGenerator {
	private static final String TENNESSEE = "Tennessee";
	private static final String NASHVILLE = "Nashville";
	private static final String MEMPHIS = "Memphis";
	private static final String KNOXVILLE = "Knoxville";
	private static final String CHATTANOOGA = "Chattanooga";

	private static final String ARKANSAS = "Arkansas";
	private static final String LITTLEROCK = "Little Rock";
	private static final String FORTSMITH = "Fort Smith";
	private static final String JONESBORO = "Jonesboro";
	private static final String TEXARKANA = "Texarkana";

	private static final String LOUISIANA = "Louisiana";
	private static final String SHREVEPORT = "Shreveport";
	private static final String LAKECHARLES = "Lake Charles";
	private static final String BATONROUGE = "Baton Rouge";
	private static final String ALEXANDRIA = "Alexandria";

	private static final String MISSISSIPPI = "Mississippi";
	private static final String COLUMBUS = "Columbus";
	private static final String JACKSON = "Jackson";
	private static final String TUPELO = "Tupelo";
	private static final String HATTIESBURG = "Hattiesburg";

	private static final String ALABAMA = "Alabama";
	private static final String BIRMINGHAM = "Birmingham";
	private static final String JASPER = "Jasper";
	private static final String MONTGOMERY = "Montgomery";
	private static final String DOTHAN = "Dothan";

	private static final String GEORGIA = "Georgia";
	private static final String ATLANTA = "Atlanta";
	private static final String MACON = "Macon";
	private static final String ATHENS = "Athens";
	private static final String ALBANY = "Albany";
	
	private List<Location> locations;

	@PostConstruct
	public void init() {

		locations = new ArrayList<Location>(24);

		locations.add(new Location(TENNESSEE, NASHVILLE));
		locations.add(new Location(ARKANSAS, LITTLEROCK));
		locations.add(new Location(LOUISIANA, SHREVEPORT));
		locations.add(new Location(MISSISSIPPI, COLUMBUS));
		locations.add(new Location(ALABAMA, BIRMINGHAM));
		locations.add(new Location(GEORGIA, ATLANTA));

		locations.add(new Location(TENNESSEE, MEMPHIS));
		locations.add(new Location(TENNESSEE, KNOXVILLE));
		locations.add(new Location(TENNESSEE, CHATTANOOGA));

		locations.add(new Location(ARKANSAS, FORTSMITH));
		locations.add(new Location(ARKANSAS, JONESBORO));
		locations.add(new Location(ARKANSAS, TEXARKANA));

		locations.add(new Location(LOUISIANA, LAKECHARLES));
		locations.add(new Location(LOUISIANA, BATONROUGE));
		locations.add(new Location(LOUISIANA, ALEXANDRIA));

		locations.add(new Location(MISSISSIPPI, JACKSON));
		locations.add(new Location(MISSISSIPPI, TUPELO));
		locations.add(new Location(MISSISSIPPI, HATTIESBURG));

		locations.add(new Location(ALABAMA, JASPER));
		locations.add(new Location(ALABAMA, MONTGOMERY));
		locations.add(new Location(ALABAMA, DOTHAN));

		locations.add(new Location(GEORGIA, MACON));
		locations.add(new Location(GEORGIA, ATHENS));
		locations.add(new Location(GEORGIA, ALBANY));

	}

	public List<Location> getLocations()
	{
		return locations;
	}
	
}
