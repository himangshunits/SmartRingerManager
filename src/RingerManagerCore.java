import com.machine.learning.decisiontrees.DecisionTree;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by Himangshu Ranjan Borah on 10/21/16.
 *
 * This is the core class of the ringer manager which will be responsible for creating and maintaing the models needed for implementing the
 * Social Benefit Function which will decide the suggested ringer mode give the inout data.
 * The possible outcomes of the public main API getRecommendedRingerMode() will output a value from {SILENT, VIBRATE, LOUD}
 * The inputs of the function are as follows.
 * 1. Location
 * 2. Noise Level
 * 3. Neighbors - Type, Strength, Current Ringer Mode, Expected Ringer Mode
 * 4. CallerInfo - ID, RelationShipType, RelationshipStrength
 * 5. Urgency of the call.
 *
 * The inputs will be further aggregated to finally form one vector with 5 attributes that are given above.
 * The decision tree maintained internally by this class will finally give us the required prediction.
 * The internal Decision tree will be updated from the driver program by oushing the feedbacks gathered from the external world.
 * The inital DecisioTree maintained in the system is derived from some ground level condition action rules defined in the file
 * "intial_rules.psv" which is a pipe separated format of the Dataset used for initial training.
 * When a ned data point comes in, then we check to see if the point exists in the train data, if yes then replace
 * Or else add it to the corpus of the train data and build the tree again.
 * We use a library called DecisonTree for the tree implementations https://github.com/mostafacs/DecisionTree
 * which is freely distributed.
 */
public class RingerManagerCore {
    //private LocationManager mLocationManagaer;
    private DecisionTree mDecisionTree;

    public RingerManagerCore(){
        //mLocationManagaer = new LocationManager();
        mDecisionTree = new DecisionTree();
        // Make the initial decision tree
        // Train your Decision Tree, will be update later
        mDecisionTree.train(new File("data/intial_rules.psv"));
        //System.out.println(tree.getRootNode());
    }


    /* This API will help pushing the feedbacks, to update the mDecisionTree object*/
    public void pushFeedback(Map<String, Object> testVector, EnumCollection.FEEDBACK_TYPE newFeedback){
        // TODO : How do we get to know what was the last prediction based on which it was a feedbck, how do we set the target vales?
    }

    /* This private API will aggregate the Caller Info and give us a caller's expectation in {MUST_RECEIVE, SHOULD_RECEIVE} */
    private EnumCollection.CALLER_EXPECATION getCallerExpectation(CallerInfo caller){
        EnumCollection.STRENGTH_TYPE strength = caller.strength;
        EnumCollection.RELATIONSHIP_TYPE relType = caller.type;
        // Get the integer valus of the types and then do an equal linear combination fo them.
        // Maximum of STRENGTH is 10, Max of REL type is 15, both minimums are 1
        // Aggregated min, with fraction 0.5 is 10 * 0.5 + 15 * 0.5 = 12.5
        //  Aggregated min = 1 * 0.5 + 1 * 0.5 = 1
        // Dividing the range in 2, we have 1 - 6.75, 6.75 - 12.5 with SHOULD_RECEIVE, MUST_RECEIVE
        double alpha = 0.5; // This is the parameter which controls the divide between type and strength
        double combineStr = alpha * strength.getValue() + (1 - alpha) * relType.getValue();
        if(combineStr < 6.75) {
            return EnumCollection.CALLER_EXPECATION.SHOULD_RECEIVE;
        } else if(combineStr >= 6.75 && combineStr <= 12.5){
            return EnumCollection.CALLER_EXPECATION.MUST_RECEIVE;
        } else {
            System.out.println("getCallerExpectation : Never get here! The Agg value = " + combineStr);
            return null;
        }
    }


    /* This API will aggregate the average expectations of all the neighbors and also their relationships
     * Gives us a final judgement from the crowd in EnumCollection.RINGER_MODE */
    private EnumCollection.RINGER_MODE getAggregatedNeigborJudgement(List<NeighborInfo> neighborList){
        // Here first we do a majority voting for the ringer modes of the neighbors and the expected modes and take the highest voted.
        // Here we have N different neighbors, so we must normalise the values by N, so that the maximum range is fixed.
        // Max values of TYPE, STRENGTH, MODE, EXPE_MODE : 15, 10, 10, 10 and Min Values are 1,1,1,1
        // When we normalise by the size, then the max and the mins will remain in the range. No problem.
        // There are 4 different attributes, so we must give equal importance to all of them by a product with 0.25
        // Max value is 15 * 0.25 + 10 * 0.25 + 10 * 0.25 + 10 * 0.25 = 11.25; Min is = 1
        // So we divide the range into three to give the crowd's decision : 1 - 3.4, 3.4 - 7.8, 7.8 - 12.25
        double alpha = 0.25;
        int N = neighborList.size();
        double totalTypeRank = 0;
        double totalStrengthRank = 0;
        double totalCurrModeRank = 0;
        double totalExpectedModeRank = 0;
        for (NeighborInfo item: neighborList){
            totalTypeRank += item.type.getValue();
            totalStrengthRank += item.strength.getValue();
            totalCurrModeRank += item.currRingerMode.getValue();
            totalExpectedModeRank += item.expectedRingerMode.getValue();
        }

        // Normalize the ranks.
        totalTypeRank /= N;
        totalStrengthRank /= N;
        totalCurrModeRank /= N;
        totalExpectedModeRank /= N;

        // Find aggregated Rank
        double aggRank = alpha * totalTypeRank + alpha * totalStrengthRank + alpha * totalCurrModeRank + alpha * totalExpectedModeRank;
        if(aggRank < 3.4){
            return EnumCollection.RINGER_MODE.Silent;
        } else if (aggRank >= 3.4 && aggRank < 7.8) {
            return EnumCollection.RINGER_MODE.Vibrate;
        } else if (aggRank >= 7.8 && aggRank <= 11.25){
            return EnumCollection.RINGER_MODE.Loud;
        } else {
            System.out.println("getAggregatedNeigborJudgement :: Never Get here! The Agg value = " + aggRank);
            return null;
        }
    }




    /* This is the main public API , the so called SOCIAL BENEFIT FUNCTION which will determine the final ringer mode for a particular set of data of one call.
     * It takes all the parameters, aggregates some of those and finally forms the 5 main variables on which our main decision tree is based on.
      * The 5 attributes it will finally form are LOCATION_TYPE, NOISE LEVEL, NEIGHBOR'S JUDGEMENT, CALLER'S EXPECTATION, URGENCY */
    public EnumCollection.RINGER_MODE getRecommendedRingerMode(String location, List<NeighborInfo> neighborList, CallerInfo caller, EnumCollection.URGENCY_TYPE urgency){
        // MAke the vector, and the the return the prediction.
        return null;
    }


}
