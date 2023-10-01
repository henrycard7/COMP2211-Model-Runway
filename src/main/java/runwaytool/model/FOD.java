package runwaytool.model;

import javafx.util.Pair;

public class FOD extends Obstacle{
    public FOD(String name, float height, float distanceFromCentre, Pair<Designator, Float> distance0, Pair<Designator, Float> distance1, float blastAllowance, float width, float length) {
        super(name, height, distanceFromCentre, distance0, distance1, width, length);
        this.blastAllowance = blastAllowance;
    }
}