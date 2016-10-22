import java.util.HashMap;

/**
 * Created by Himangshu on 10/21/16.
 * This class takes care of the Location Services. Currently the Noise level is fixed for one location.
 * Going forward it will be dynamic.
 */
public class LocationManager {
    private HashMap<String, EnumCollection.NOISE_TYPE> locationToNoiseMap;
    public LocationManager(){
        //hunt, eb2, carmichael, oval, seminar, lab, meeting, party.
        locationToNoiseMap.put("hunt", EnumCollection.NOISE_TYPE.QUIET);
        locationToNoiseMap.put("eb2", EnumCollection.NOISE_TYPE.NORMAL);
        locationToNoiseMap.put("carmichael", EnumCollection.NOISE_TYPE.NOISY);
        locationToNoiseMap.put("oval", EnumCollection.NOISE_TYPE.NOISY);
        locationToNoiseMap.put("seminar", EnumCollection.NOISE_TYPE.QUIET);
        locationToNoiseMap.put("lab", EnumCollection.NOISE_TYPE.QUIET);
        locationToNoiseMap.put("meeting", EnumCollection.NOISE_TYPE.QUIET);
        locationToNoiseMap.put("party", EnumCollection.NOISE_TYPE.NOISY);
    }

    public void addLocation(String name, EnumCollection.NOISE_TYPE noiseType){
        locationToNoiseMap.put(name, noiseType);
    }

    public EnumCollection.NOISE_TYPE getNoiseLevelForLocation(String locKey){
        return locationToNoiseMap.get(locKey);
    }


}
