/**
 * Created by Himangshu on 10/21/16.
 */
public class EnumCollection {
    // All the high values mean very likely to be LOUD. And vice versa.
    // The class variable of the prediction.
    public enum RINGER_MODE {
        SILENT(1), VIBRATE(5), LOUD(10);
        private final int id;
        RINGER_MODE(int id) { this.id = id; }
        public int getValue() { return id;}
    };

    public enum RELATIONSHIP_TYPE {
        FAMILY(15), FRIEND(10), COLLEAGUE(5), STRANGER(1);
        private final int id;
        RELATIONSHIP_TYPE(int id) { this.id = id; }
        public int getValue() { return id;}
    };

    public enum STRENGTH_TYPE {
        HIGH(10), MEDIUM(5), LOW(1);
        private final int id;
        STRENGTH_TYPE(int id) { this.id = id; }
        public int getValue() { return id;}
    };

    public enum FEEDBACK_TYPE {POSITIVE, NEUTRAL, NEGATIVE};

    public enum NOISE_TYPE {
        QUIET(1), NORMAL(5), NOISY(10);
        private final int id;
        NOISE_TYPE(int id) { this.id = id; }
        public int getValue() { return id;}
    };

    // This is tentative list! May change depending on the domain knowledge of the experts.
    public enum LOCATION_TYPE {OUTDOOR, PARTY, LAB, CLASSROOM, LIBRARY, MEETING, HOSPITAL};


    public enum CALLER_EXPECATION {MUST_RECEIVE, SHOULD_RECEIVE};
    public enum URGENCY_TYPE {NONE, CASUAL, URGENT};
}
