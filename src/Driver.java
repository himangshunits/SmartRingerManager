import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Himangshu on 10/22/16.
 */
public class Driver {
    private static PeopleManager mPeopleManager = new PeopleManager();
    private static LocationManager mLocationManagaer = new LocationManager();

    /*
    * -Flow-

Enter place
List neighbors
Request call
Response call
...
exit place
    *
    * */

    // TODO : Add all the Defauls for the People and the location dictionaries.
    // TODO : HAndle all the faulty inputs froom WEB Service.
    // If there are others at the same place where you are and if their relationships are not known, assume they are strangers.


    private static JsonObject requestCall(int myId){
        try {
            URL url = new URL("http://yangtze.csc.ncsu.edu:9090/csc555/services.jsp?action=requestCall&userId=" + myId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            //conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            InputStream is = (InputStream) conn.getContent();
            BufferedReader br1 = new BufferedReader(new InputStreamReader(is));
            String line = null;
            StringBuffer sb = new StringBuffer();
            while((line = br1.readLine()) != null){
                sb.append(line);
            }
            String htmlContent = sb.toString();

            //System.out.println("Contetnt is = " + htmlContent);



            /*BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            String output;
            //System.out.println("Output from Server .... \n");
            output = br.readLine();*/


            Gson gson = new Gson();
            JsonElement element = gson.fromJson (htmlContent, JsonElement.class);
            JsonObject jsonObj = element.getAsJsonObject();
            conn.disconnect();
            return jsonObj;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static void enterPlace(String placeName, int myId, EnumCollection.RINGER_MODE myMode, EnumCollection.RINGER_MODE expectedMode){
        try {
            //?action=enterPlace&place=P&userId=U&myMode=M&expectedMode=E
            URL url = new URL("http://yangtze.csc.ncsu.edu:9090/csc555/services.jsp?action=enterPlace&place="
                    + placeName +"&userId=" + myId + "&myMode=" + myMode.toString() + "&expectedMode=" + expectedMode.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            //conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }
            return;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

    private static void exitPlace(int myId){
        try {
            //?action=exitPlace&userId=U
            URL url = new URL("http://yangtze.csc.ncsu.edu:9090/csc555/services.jsp?action=exitPlace&userId=" + myId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            //conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }
            return;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }


    private static List<NeighborInfo> getNeighbors(int myId){
        List<NeighborInfo> result = new LinkedList<>();
        try {
            URL url = new URL("http://yangtze.csc.ncsu.edu:9090/csc555/services.jsp?action=getNeighbors&userId=" + myId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            //conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            InputStream is = (InputStream) conn.getContent();
            BufferedReader br1 = new BufferedReader(new InputStreamReader(is));
            String line = null;
            StringBuffer sb = new StringBuffer();
            while((line = br1.readLine()) != null){
                sb.append(line);
            }
            String htmlContent = sb.toString();

            //System.out.println("Contetnt is = " + htmlContent);



            /*BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            String output;
            //System.out.println("Output from Server .... \n");
            output = br.readLine();*/


            Gson gson = new Gson();
            JsonElement element = gson.fromJson (htmlContent, JsonElement.class);
            JsonObject jsonObj = element.getAsJsonObject();
            conn.disconnect();
            //System.out.println(jsonObj);
            JsonArray adjList = (JsonArray)jsonObj.get("user");
            for(JsonElement item : adjList){
                JsonObject tempObj = (JsonObject)item;
                /*
                * {
                      "id": "1",
                      "name": "null",
                      "ringer-mode": "Silent",
                      "expected": "Silent"
                    }
                * */
                int id = Integer.parseInt(tempObj.get("id").getAsString());
                String name = tempObj.get("name").getAsString();
                EnumCollection.RINGER_MODE currMode = EnumCollection.RINGER_MODE.valueOf(tempObj.get("ringer-mode").getAsString());
                EnumCollection.RINGER_MODE expMode = EnumCollection.RINGER_MODE.valueOf(tempObj.get("expected").getAsString());
                NeighborInfo tempNeighbor = new NeighborInfo(id, name,
                        mPeopleManager.getRelTypeForName(name), mPeopleManager.getStrengthForName(name), currMode, expMode);
                result.add(tempNeighbor);
            }
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static boolean sendResponse(int callId, EnumCollection.RINGER_MODE predictedMode){

        return true;// Returns true on sucess
    }

    private static JsonObject getLogs(int myId){
        return null;
    }



    public static void main(String[] args){
        PeopleManager myPeople = new PeopleManager();
        /*System.out.println("Hello Ji!! I am a " + myPeople.getRelTypeForName("Hurin"));
        JsonObject jsnOb = requestCall(5031);
        System.out.println("Call ID = " + jsnOb.get("callId"));
        System.out.println("Caller Name = " + jsnOb.get("callerName"));
        System.out.println("Caller ID = " + jsnOb.get("callerId"));
        System.out.println("Call Reason = " + jsnOb.get("reason"));*/
        enterPlace("hunt", 5031, EnumCollection.RINGER_MODE.Vibrate, EnumCollection.RINGER_MODE.Loud);
        getNeighbors(5031);
        List<NeighborInfo> tempList = getNeighbors(5031);
        exitPlace(5031);
        enterPlace("oval", 5031, EnumCollection.RINGER_MODE.Vibrate, EnumCollection.RINGER_MODE.Loud);
        getNeighbors(5031);
        exitPlace(5031);
    }
}
