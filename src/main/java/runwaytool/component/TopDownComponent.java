package runwaytool.component;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import runwaytool.model.Designator;
import runwaytool.model.RunwayStripParameters;

public class TopDownComponent extends Canvas {


    private float[] TORA = new float[2];
    private float[] TODA = new float[2];
    private float[] ASDA = new float[2];
    private float[] LDA = new float[2];
    private float length;
    private float stripLength;
    private float centerToClearedEnd;
    private float centerToClearedMid;
    private float clearedEndMin;
    private float clearedEndMax;
    private final float runwaywidth = 45f;
    private final float stripend = 60f; //Stripend = stopway
    private Designator[] designators;
    float threshold0;
    float threshold1;
//
//    private Color color1 = Color.DARKCYAN;
//    private Color color2 = Color.SEAGREEN;
//    private Color color3 = Color.MIDNIGHTBLUE;
    private Color color1;
    private Color color2;
    private Color color3;

    private float xMeterScale;
    private float yMeterScale;

    public TopDownComponent(float width, float height, RunwayStripParameters rsp) {
        super(width, height);

        length = rsp.length;
        TORA[0] = rsp.runwayParametersList.get(0).TORA;
        TORA[1] = rsp.runwayParametersList.get(1).TORA;
        TODA[0] = rsp.runwayParametersList.get(0).TODA;
        TODA[1] = rsp.runwayParametersList.get(1).TODA;
        ASDA[0] = rsp.runwayParametersList.get(0).ASDA;
        ASDA[1] = rsp.runwayParametersList.get(1).ASDA;
        LDA[0] = rsp.runwayParametersList.get(0).LDA;
        LDA[1] = rsp.runwayParametersList.get(1).LDA;
        designators = new Designator[2];
        designators[0] = rsp.runwayParametersList.get(0).designator;
        designators[1] = rsp.runwayParametersList.get(1).designator;

        centerToClearedEnd = rsp.centreToClearedEnd;
        centerToClearedMid = rsp.centreToClearedMid;
        clearedEndMin = rsp.clearedEndMin;
        clearedEndMax = rsp.clearedEndMax;

        threshold0 = rsp.runwayParametersList.get(0).displacedThreshold;
        threshold1 = rsp.runwayParametersList.get(1).displacedThreshold;
        stripLength = stripend + length + stripend;

        //System.out.println(TORA[0]+","+TODA[0]+","+ASDA[0]+","+LDA[0]);
        //System.out.println(TORA[1]+","+TODA[1]+","+ASDA[1]+","+LDA[1]);
        //System.out.println(ends[0]+","+ends[1]+","+stripLength);
        xMeterScale = (width * 0.9f / stripLength);
        yMeterScale = (height * 0.9f / 300);
    }


    public float getXPlacement(float meters) {
        return (xMeterScale * meters);
    }

    public float getYPlacement(float meters) {
        return (yMeterScale * meters);
    }

    public float[] getTORA() {
        return TORA;
    }

    public void setTORA(float[] TORA) {
        this.TORA = TORA;
    }

    public float[] getTODA() {
        return TODA;
    }

    public void setTODA(float[] TODA) {
        this.TODA = TODA;
    }

    public float[] getASDA() {
        return ASDA;
    }

    public void setASDA(float[] ASDA) {
        this.ASDA = ASDA;
    }

    public float[] getLDA() {
        return LDA;
    }

    public void setLDA(float[] LDA) {
        this.LDA = LDA;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getCenterToClearedEnd() {
        return centerToClearedEnd;
    }

    public void setCenterToClearedEnd(float centerToClearedEnd) {
        this.centerToClearedEnd = centerToClearedEnd;
    }

    public float getCenterToClearedMid() {
        return centerToClearedMid;
    }

    public void setCenterToClearedMid(float centerToClearedMid) {
        this.centerToClearedMid = centerToClearedMid;
    }

    public float getClearedEndMin() {
        return clearedEndMin;
    }

    public void setClearedEndMin(float clearedEndMin) {
        this.clearedEndMin = clearedEndMin;
    }

    public float getClearedEndMax() {
        return clearedEndMax;
    }

    public void setClearedEndMax(float clearedEndMax) {
        this.clearedEndMax = clearedEndMax;
    }

    public float getStripend() {
        return stripend;
    }

    public void paint() {
        GraphicsContext gc = this.getGraphicsContext2D();

        double rotCentreX = (getWidth() / 2);
        double rotCentreY = (getHeight() / 2);
        float yLength = (float) (this.getHeight() * 0.9);

        //Background
        gc.setFill(color2);
        gc.fillRect(0, 0, (getWidth()), (getHeight()));

        gc.translate((getWidth() * 0.05), (getHeight() * 0.05));
        //Non-Cleared & Graded area
        gc.setFill(color1);
        gc.fillRect(0, 0, (getWidth() * 0.9), (getHeight() * 0.9));
        //Cleared & Graded Centre
        gc.setFill(color3);
        gc.fillRect(0, (yMeterScale * centerToClearedEnd), (getWidth() * 0.9), (yMeterScale * centerToClearedEnd * 2));

        double[] xPoints = {(xMeterScale * (stripend + clearedEndMin)), (xMeterScale * (stripend + clearedEndMax)),
                (xMeterScale * (stripLength - (stripend + clearedEndMax))), (xMeterScale * (stripLength - (stripend + clearedEndMin)))};
        double[] yPoints1 = {(yMeterScale * centerToClearedEnd), (yMeterScale * (2 * (centerToClearedEnd) - centerToClearedMid))
                , (yMeterScale * (2 * (centerToClearedEnd) - centerToClearedMid)), (yMeterScale * centerToClearedEnd)};
        double[] yPoints2 = {(yMeterScale * (centerToClearedEnd * 3)), (yMeterScale * (centerToClearedEnd * 2 + centerToClearedMid))
                , (yMeterScale * (centerToClearedEnd * 2 + centerToClearedMid)), (yMeterScale * (centerToClearedEnd * 3))};
        //Cleared & Graded Top and Bottom
        gc.setFill(color3);
        gc.fillPolygon(xPoints, yPoints1, 4);
        gc.fillPolygon(xPoints, yPoints2, 4);

        float cwMaxLength = (float)(getWidth() * 0.05) + (xMeterScale * stripend);
        //Clearway0
        float clearway0Length = 400;
        float cw0SafeLength = Math.min(xMeterScale*clearway0Length, cwMaxLength);
        float clearway0Width = 80;
        gc.translate((getWidth() * -0.05), (getHeight() * -0.05));
        gc.setFill(Color.rgb(100, 100, 100));
        gc.fillRect(cwMaxLength - cw0SafeLength, (getHeight() * 0.05) + (yMeterScale * centerToClearedEnd * 2) - (yMeterScale*(clearway0Width/2))
                , cw0SafeLength, (yMeterScale * clearway0Width));
        //Clearway1
        float clearway1Length = 400;
        float cw1SafeLength = Math.min(xMeterScale*clearway1Length, cwMaxLength);
        float clearway1Width = 80;
        gc.setFill(Color.rgb(100, 100, 100));
        gc.fillRect(getWidth() - cwMaxLength, (getHeight() * 0.05) + (yMeterScale * centerToClearedEnd * 2) - (yMeterScale*(clearway1Width/2))
                , cw1SafeLength, (yMeterScale * clearway1Width));
        gc.translate((getWidth() * 0.05), (getHeight() * 0.05));

        float topOfRunway = (yMeterScale * ((centerToClearedEnd * 2) - (runwaywidth / 2)));
        //Runway
        gc.setFill(Color.DARKGRAY);
        gc.fillRect((xMeterScale * stripend), topOfRunway
                , (xMeterScale * length), (yMeterScale * runwaywidth));
        //Stopway0
        gc.setFill(Color.GRAY);
        gc.fillRect((xMeterScale * 0), topOfRunway
                , (xMeterScale * stripend), (yMeterScale * runwaywidth));
        //Stopway1
        gc.setFill(Color.GRAY);
        gc.fillRect((xMeterScale * (length+stripend)), topOfRunway
                , (xMeterScale * stripend), (yMeterScale * runwaywidth));

        //Centreline
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(getHeight() * 0.005);
        gc.setLineDashes(getWidth() * 0.01);
        gc.strokeLine((xMeterScale * (stripend + length * 0.06)), (yMeterScale * (centerToClearedEnd * 2))
                , (xMeterScale * (stripend + length * 0.94)), (yMeterScale * (centerToClearedEnd * 2)));

        //Left threshold
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(getHeight()*0.01);
        gc.setLineDashes(4, 4);
        gc.strokeLine(getXPlacement(threshold0+stripend),topOfRunway+(yMeterScale*2f)
                ,getXPlacement(threshold0+stripend),topOfRunway+(yMeterScale*runwaywidth)-(yMeterScale*2f));
        //Right threshold
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(getHeight()*0.01);
        gc.setLineDashes(4, 4);
        gc.strokeLine(getXPlacement(length-(threshold1-stripend)),topOfRunway+(yMeterScale*2f)
                ,getXPlacement(length-(threshold1-stripend)),topOfRunway+(yMeterScale*runwaywidth)-(yMeterScale*2f));

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        //Left designator
        Text leftAngle = new Text(designators[0].getDegreeTwoFigure());
        leftAngle.setFont(new Font(getHeight() * 0.05));
        leftAngle.setStroke(Color.BLACK);
        gc.drawImage(leftAngle.snapshot(parameters, null), xMeterScale * (stripend + (0.018 * length)), yMeterScale * (centerToClearedEnd * 2 - (runwaywidth / 2)));
        //Right designator
        Text rightAngle = new Text(designators[1].getDegreeTwoFigure());
        rightAngle.setFont(new Font(getHeight() * 0.05));
        rightAngle.setStroke(Color.BLACK);
        gc.drawImage(rightAngle.snapshot(parameters, null), xMeterScale * (stripend + (0.982 * length) - 100), yMeterScale * (centerToClearedEnd * 2 - (runwaywidth / 2)));
        //Left designator letter
        if (designators[0].getSuffix() != null) {
            Text suffixLeft = new Text(designators[0].getSuffix().name());
            suffixLeft.setFont(new Font(getHeight() * 0.05));
            suffixLeft.setStroke(Color.BLACK);
            gc.drawImage(suffixLeft.snapshot(parameters, null), xMeterScale * (stripend + (0.025 * length)), yMeterScale * centerToClearedEnd * 2);
        }
        //Right designator number
        if (designators[1].getSuffix() != null) {
            Text suffixRight = new Text(designators[1].getSuffix().name());
            suffixRight.setFont(new Font(getHeight() * 0.05));
            suffixRight.setStroke(Color.BLACK);
            gc.drawImage(suffixRight.snapshot(parameters, null), xMeterScale * (stripend + (0.988 * length) - 100), yMeterScale * centerToClearedEnd * 2);
        }
    }
    public Color getColour(){
        return color1;
    }

    public void setColor(Color color) {
        color1 = color;
        if (color.equals(Color.GRAY) ){
            color2 = Color.DARKGRAY;
            color3 = Color.BLACK;
        }else if(color.equals(Color.DARKCYAN)){
            color2 = Color.SEAGREEN;
            color3 = Color.MIDNIGHTBLUE;
        }else if(color.equals(Color.BLUE)){
            color2 = Color.ORANGE;
            color3 = Color.SKYBLUE;
        }
        this.getGraphicsContext2D().translate(-(getWidth() * 0.05), -(getHeight() * 0.05));
        paint();
    }
}