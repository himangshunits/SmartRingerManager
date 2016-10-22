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
    private LocationManager mLocationManagaer;
    private DecisionTree mDecisionTree;

    public RingerManagerCore(){
        mLocationManagaer = new LocationManager();
        mDecisionTree = new DecisionTree();
        // Make the initial decision tree
        // Train your Decision Tree, will be update later
        mDecisionTree.train(new File("data/vertebrate.psv"));
        //System.out.println(tree.getRootNode());
    }


    /* This API will help pushing the feedbacks, to update the mDecisionTree object*/
    public void pushFeedback(Map<String, Object> testVector, EnumCollection.FEEDBACK_TYPE newFeedback){

    }

    /* This private API will aggregate the Caller Info and give us a caller's expectation in {MUST_RECEIVE, SHOULD_RECEIVE} */
    private EnumCollection.CALLER_EXPECATION getCallerExpectation(CallerInfo caller){
        return null;
    }


    /* This API will aggregate the average expectations of all the neighbors and also their relationships
     * Gives us a final judgement from the crowd in EnumCollection.RINGER_MODE */
    private EnumCollection.RINGER_MODE getAggregatedNeigborJudgement(List<NeighborInfo> neighborList){
        return null;
    }


//getRecommendedRingerMode()


}
