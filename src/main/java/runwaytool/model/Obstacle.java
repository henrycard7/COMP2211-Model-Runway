package runwaytool.model;

import javafx.util.Pair;

public class Obstacle {
    private String name;
    private float height;
    private float distanceFromCentre;
    private javafx.util.Pair<Designator, Float> distance0;
    private javafx.util.Pair<Designator, Float> distance1;
    protected float blastAllowance = 300f;
    private float width;
    private float length;

    public Obstacle(String name, float height, float distanceFromCentre, Pair<Designator, Float> distance0, Pair<Designator, Float> distance1, float width, float length) {
        this.name = name;
        this.height = height;
        this.distanceFromCentre = distanceFromCentre;
        this.distance0 = distance0;
        this.distance1 = distance1;
        this.width = width;
        this.length = length;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public float getHeight() {
        return height;
    }
    public void setHeight(float height) {
        this.height = height;
    }
    public float getDistanceFromCentre() {
        return distanceFromCentre;
    }
    public void setDistanceFromCentre(float distanceFromCentre) {
        this.distanceFromCentre = distanceFromCentre;
    }

    public void setDistance0(javafx.util.Pair<Designator, Float> pair) {
        this.distance0 = pair;
    }
    public javafx.util.Pair<Designator, Float> getDistance0() {
        return distance0;
    }
    public float getDistance0f(){
        return distance0.getValue();
    }
    public void setDistance1(Pair<Designator, Float> distance1) {
        this.distance1 = distance1;
    }
    public Pair<Designator, Float> getDistance1() {
        return distance1;
    }
    public float getDistance1f(){
        return distance1.getValue();
    }

    public float getBlastAllowance() {
        return blastAllowance;
    }
    public void setBlastAllowance(float blastAllowance) {
        this.blastAllowance = blastAllowance;
    }
    public float getWidth() {
        return width;
    }
    public void setWidth(float width) {
        this.width = width;
    }
    public float getLength() {
        return length;
    }
    public void setLength(float length) {
        this.length = length;
    }
}