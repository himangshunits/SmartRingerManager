import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import sun.reflect.annotation.ExceptionProxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Himangshu on 10/22/16.
 */
public class Driver {
    private static PeopleManager mPeopleManager = new PeopleManager();
    private static RingerManagerCore mRingerManagerCore = new RingerManagerCore();
    // TODO : Add all the Defauls for the People and the location dictionaries.
    // TODO : HAndle all the faulty inputs froom WEB Service.
    // If there are others at the same place where you are and if their relationships are not known, assume they are strangers.

    private static CallerInfo requestCall(int myId){
        System.out.println("Going to Request a call.");
        try {
            URL url = new URL("http://yangtze.csc.ncsu.edu:9090/csc555/services.jsp?action=requestCall&userId=" + myId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            //conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }
            /*InputStream is = (InputStream) conn.getContent();
            BufferedReader br1 = new BufferedReader(new InputStreamReader(is));
            String line = null;
            StringBuffer sb = new StringBuffer();
            while((line = br1.readLine()) != null){
                sb.append(line);
            }
            String htmlContent = sb.toString();*/

            //System.out.println("Contetnt is = " + htmlContent);
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            String nextLine;
            StringBuilder output = new StringBuilder();
            while((nextLine = br.readLine()) != null){
                output.append(nextLine);
            }
            Gson gson = new Gson();
            JsonElement element = gson.fromJson (output.toString().trim(), JsonElement.class);
            JsonObject jsonObj = element.getAsJsonObject();
            conn.disconnect();

            int callId = Integer.parseInt(jsonObj.get("callId").getAsString());
            int callerId = Integer.parseInt(jsonObj.get("callerId").getAsString());
            String name = jsonObj.get("callerName").getAsString();
            EnumCollection.RELATIONSHIP_TYPE type = mPeopleManager.getRelTypeForName(name);
            EnumCollection.STRENGTH_TYPE strength = mPeopleManager.getStrengthForName(name);
            EnumCollection.URGENCY_TYPE urg = EnumCollection.URGENCY_TYPE.valueOf(jsonObj.get("reason").getAsString().toUpperCase());
            CallerInfo result = new CallerInfo(callId, callerId, name, type, strength, urg);

            return result;
        } catch (MalformedURLException e) {
            System.out.println("Error is = " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error is = " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


    private static void enterPlace(String placeName, int myId, EnumCollection.RINGER_MODE myMode, EnumCollection.RINGER_MODE expectedMode){
        System.out.println("Going to Enter Place :: " + placeName);
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
            System.out.println("Error is = " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error is = " + e.getMessage());
            e.printStackTrace();
        }
        return;
    }

    private static void exitPlace(int myId){
        System.out.println("Going to Exit Place.");
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
            System.out.println("Error is = " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error is = " + e.getMessage());
            e.printStackTrace();
        }
        return;
    }


    private static List<NeighborInfo> getNeighbors(int myId){
        System.out.println("Going to request for neighbor info.");
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
            /*InputStream is = (InputStream) conn.getContent();
            BufferedReader br1 = new BufferedReader(new InputStreamReader(is));
            String line = null;
            StringBuffer sb = new StringBuffer();
            while((line = br1.readLine()) != null){
                sb.append(line);
            }
            String htmlContent = sb.toString();
            //System.out.println("Contetnt is = " + htmlContent);*/

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            StringBuilder output = new StringBuilder();
            String nextLine;
            while((nextLine = br.readLine()) != null){
                output.append(nextLine);
            }
            Gson gson = new Gson();
            JsonElement element = gson.fromJson (output.toString().trim(), JsonElement.class);
            JsonObject jsonObj = element.getAsJsonObject();
            conn.disconnect();
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
            System.out.println("Error is = " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error is = " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private static JsonObject sendResponseAndGetFeedback(int callId, EnumCollection.RINGER_MODE predictedMode){
        System.out.println("Sending Response and Getting feedbacks!");
        try {
            //?action=responseCall&callId=C&ringerMode=R
            URL url = new URL("http://yangtze.csc.ncsu.edu:9090/csc555/services.jsp?action=responseCall&callId=" + callId + "&ringerMode=" + predictedMode.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            //conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }
            /*InputStream is = (InputStream) conn.getContent();
            BufferedReader br1 = new BufferedReader(new InputStreamReader(is));
            String line = null;
            StringBuffer sb = new StringBuffer();
            while((line = br1.readLine()) != null){
                if(line.length() == 0)
                    continue;
                sb.append(line);
            }
            String htmlContent = sb.toString();*/
            //System.out.println("Contetnt is = " + htmlContent);

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            String nextline;
            StringBuilder output = new StringBuilder();
            while((nextline = br.readLine()) != null){
               output.append(nextline);
            }

            Gson gson = new Gson();
            JsonElement element = gson.fromJson (output.toString().trim(), JsonElement.class);
            JsonObject jsonObj = element.getAsJsonObject();
            conn.disconnect();
            return jsonObj;
        } catch (MalformedURLException e) {
            System.out.println("Error is = " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error is = " + e.getMessage());
            e.printStackTrace();
        }
        return null;// Returns true on success
    }

    private static JsonObject getLogs(int myId){
        //TODO: Not sure where to use this!
        return null;
    }


    private static void simulateOneFlow(int myId, EnumCollection.RINGER_MODE myRinderMode, EnumCollection.RINGER_MODE myExpectedMode, String place){
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
        // Enter the place with a phone with ringer mode myRingerMode and with expectation from neighbors myExpectedMode

        /*############################################################*/
        enterPlace(place, myId, myRinderMode, myExpectedMode);
        /*############################################################*/

        /*############################################################*/
        List<NeighborInfo> tempList = getNeighbors(myId);
        /*############################################################*/

        // Print the neighbors.
        System.out.println("The Neighbors Retrieved are ::");
        System.out.println(tempList);
        // Create a mapping of the users and IDs.
        HashMap<Integer, String> idNameMap = new HashMap<>();
        for (NeighborInfo item:tempList){
            idNameMap.put(item.id, item.name);
        }


        /*############################################################*/
        CallerInfo cInfo = requestCall(myId);
        /*############################################################*/

        System.out.println("The Caller's information below :: ");
        System.out.println(cInfo);



        idNameMap.put(cInfo.callerId, cInfo.name);
        // Predict the mode here! Get feedback
        EnumCollection.RINGER_MODE prediction = mRingerManagerCore.getRecommendedRingerMode(place, tempList, cInfo);

        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        System.out.println("######################################################################");
        System.out.println("The Prediction made from the Social Benefit Function = " + prediction);
        System.out.println("######################################################################");
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");


        /*############################################################*/
        JsonObject feedbacks = sendResponseAndGetFeedback(cInfo.callId, prediction);
        /*############################################################*/


        System.out.println("The Feedbacks Received :: ");
        System.out.println(feedbacks);



        /*############################################################*/
        exitPlace(myId);
        /*############################################################*/



        //Analyse Feedbacks and push the new decision of the Crowd.
        int idNew = Integer.parseInt(feedbacks.get("callId").getAsString());
        // Check if the Call ID and the Call ID from the feedback was the same@! Must be same.
        if(cInfo.callId != idNew){
            System.out.println("Feedback got for different Call!!!!!! Error");
        }
        List<FeedbackInfo> feedbackList = new LinkedList<>();
        JsonArray adjList = (JsonArray)feedbacks.get("user");
        for(JsonElement item : adjList){
            JsonObject tempObj = (JsonObject)item;
            int id = Integer.parseInt(tempObj.get("id").getAsString());
            EnumCollection.FEEDBACK_TYPE fType = EnumCollection.FEEDBACK_TYPE.valueOf(tempObj.get("feedback").getAsString().toUpperCase());
            feedbackList.add(new FeedbackInfo(id, fType));
        }

        System.out.println("Processing Feedbacks, going to push to the system and recreate the system.");
        /*############################################################*/
        mRingerManagerCore.pushFeedback(mRingerManagerCore.synthesizeAttributeVector(place, tempList, cInfo), prediction, feedbackList);
        System.out.println("Feedbacks successfully pushed! The Decision trees are updated.");
        //TODO: When you see new Data, be sure to add it to your Database.
        /*############################################################*/
    }



    private static void simulateRandomFlow(int iterations){
        LocationManager locaMgr = new LocationManager();
        while(iterations >= 0){
            System.out.println("The Iteration = " + iterations);
            simulateOneFlow(5031,EnumCollection.RINGER_MODE.getRandomEnum() ,
                    EnumCollection.RINGER_MODE.getRandomEnum(), locaMgr.getRandomPlacename());
            iterations--;
        }
    }


    public static void main(String[] args){
        char c;
        do{
            Scanner scan = new Scanner(System.in);
            System.out.println("Please Enter your's ID or type in 0 to Randomly Run once ! :: ");
            int id = scan.nextInt();
            if(id == 0){
                System.out.println("You have opted for randomization. How many runs do you want to simulate?(Max 1000)");
                scan.nextLine();
                int passes = scan.nextInt();
                simulateRandomFlow(passes);
                return;
            }
            System.out.println("You have Entered ID = " + id);

            scan.nextLine();
            System.out.println("Please Enter your's Current Ringer Mode. Use proper names as decided in the project description.(Loud, Vibrate, Silent) ");
            String currMode = scan.nextLine();
            System.out.println("You have Entered Current Mode as = " + currMode);



            System.out.println("Please Enter your's Expected Ringer Mode. Use proper names as decided in the project description.(Loud, Vibrate, Silent) ");
            String expMode = scan.nextLine();
            System.out.println("You have Expected Mode as = " + expMode);


            System.out.println("Please Enter the location you want to visit. Choose among (hunt, eb2, carmichael, oval, seminar, lab, meeting, party)");
            String location = scan.nextLine();
            System.out.println("You have location as = " + location);


            System.out.println("Going to Make a series of calls now.");
            simulateOneFlow(id, EnumCollection.RINGER_MODE.valueOf(currMode), EnumCollection.RINGER_MODE.valueOf(expMode), location);
            System.out.println("One Simulation Completed !");
            System.out.println("Please Press D + Enter to run again or any other key + Enter to exit.");
            c = scan.next().trim().charAt(0);
        } while(c == 'D');
    }
}
