import java.util.HashMap;

/**
 * Created by Himangshu on 10/21/16.
 * This class takes care of the Location Services. Currently the Noise level is fixed for one location.
 * Going forward it will be dynamic.
 */
public class LocationManager {
    private HashMap<String, EnumCollection.NOISE_TYPE> locationToNoiseMap;
    private HashMap<String, EnumCollection.LOCATION_TYPE> locationToTypeMap;
    public LocationManager(){
        //hunt, eb2, carmichael, oval, seminar, lab, meeting, party.
        locationToNoiseMap = new HashMap<>();
        locationToTypeMap = new HashMap<>();

        locationToNoiseMap.put("hunt", EnumCollection.NOISE_TYPE.QUIET);
        locationToTypeMap.put("hunt", EnumCollection.LOCATION_TYPE.LIBRARY);

        locationToNoiseMap.put("eb2", EnumCollection.NOISE_TYPE.NORMAL);
        locationToTypeMap.put("eb2", EnumCollection.LOCATION_TYPE.CLASSROOM);

        locationToNoiseMap.put("carmichael", EnumCollection.NOISE_TYPE.NOISY);
        locationToTypeMap.put("carmichael", EnumCollection.LOCATION_TYPE.PARTY);

        locationToNoiseMap.put("oval", EnumCollection.NOISE_TYPE.NOISY);
        locationToTypeMap.put("oval", EnumCollection.LOCATION_TYPE.PARTY);

        locationToNoiseMap.put("seminar", EnumCollection.NOISE_TYPE.QUIET);
        locationToTypeMap.put("seminar", EnumCollection.LOCATION_TYPE.MEETING);

        locationToNoiseMap.put("lab", EnumCollection.NOISE_TYPE.QUIET);
        locationToTypeMap.put("lab", EnumCollection.LOCATION_TYPE.LAB);

        locationToNoiseMap.put("meeting", EnumCollection.NOISE_TYPE.QUIET);
        locationToTypeMap.put("meeting", EnumCollection.LOCATION_TYPE.MEETING);

        locationToNoiseMap.put("party", EnumCollection.NOISE_TYPE.NOISY);
        locationToTypeMap.put("party", EnumCollection.LOCATION_TYPE.PARTY);
    }

    public void addLocation(String name, EnumCollection.NOISE_TYPE noiseType, EnumCollection.LOCATION_TYPE locationType){
        locationToNoiseMap.put(name, noiseType);
        locationToTypeMap.put(name, locationType);
    }

    public EnumCollection.NOISE_TYPE getNoiseLevelForLocation(String locKey){
        if(locationToNoiseMap.containsKey(locKey))
            return locationToNoiseMap.get(locKey);
        else
            return EnumCollection.NOISE_TYPE.NORMAL;//TODO: Decide the default things
    }

    public EnumCollection.LOCATION_TYPE getLocationTypeForLocation(String locKey){
        if(locationToTypeMap.containsKey(locKey))
            return locationToTypeMap.get(locKey);
        else
            return EnumCollection.LOCATION_TYPE.OUTDOOR;
    }


}
