/**
 * Created by Himangshu on 10/23/16.
 */
public class FeedbackInfo {
    int userID;
    EnumCollection.FEEDBACK_TYPE feedback;

    public FeedbackInfo(int userID, EnumCollection.FEEDBACK_TYPE feedback) {
        this.userID = userID;
        this.feedback = feedback;
    }
}
