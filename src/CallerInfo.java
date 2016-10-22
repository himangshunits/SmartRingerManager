/**
 * Created by Himangshu on 10/21/16.
 */
public class CallerInfo {
    public int id;
    public String name;
    public EnumCollection.RELATIONSHIP_TYPE type;
    public EnumCollection.STRENGTH_TYPE strength;

    public CallerInfo(int id, String name, EnumCollection.RELATIONSHIP_TYPE type, EnumCollection.STRENGTH_TYPE strength) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.strength = strength;
    }
}
