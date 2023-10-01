package runwaytool.component;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import runwaytool.model.Designator;
import runwaytool.model.RunwayStripParameters;

public class SideComponent extends Canvas {

    private float[] TORA = new float[2];
    private float[] TODA = new float[2];
    private float[] ASDA = new float[2];
    private float[] LDA = new float[2];
    private float length;
    private float stripLength;
    private final float stripend = 60f;
    private Designator[] designators;
    float threshold0;
    float threshold1;
    public Color color1;

    private float xMeterScale;
    private float yMeterScale;

    public SideComponent(float width, float height, RunwayStripParameters rsp) {
        super(width,height);

        length = rsp.length;
        TORA[0] = rsp.runwayParametersList.get(0).TORA;
        TORA[1] = rsp.runwayParametersList.get(1).TORA;
        TODA[0] = rsp.runwayParametersList.get(0).TODA;
        TODA[1] = rsp.runwayParametersList.get(1).TODA;
        ASDA[0] = rsp.runwayParametersList.get(0).ASDA;
        ASDA[1] = rsp.runwayParametersList.get(1).ASDA;
        LDA[0] = rsp.runwayParametersList.get(0).LDA;
        LDA[1] = rsp.runwayParametersList.get(1).LDA;

        threshold0 = rsp.runwayParametersList.get(0).displacedThreshold;
        threshold1 = rsp.runwayParametersList.get(1).displacedThreshold;
        stripLength = stripend + length + stripend;

        xMeterScale = width / stripLength;

    }

    public float getXPlacement(float meters){
        return(xMeterScale*meters);
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

    public float getStripLength() {
        return stripLength;
    }

    public void setStripLength(float stripLength) {
        this.stripLength = stripLength;
    }

    public float getStripend() {
        return stripend;
    }

    public void setColor1(Color color1) {
        this.color1 = color1;
    }

    public void paint() {
        GraphicsContext gc = this.getGraphicsContext2D();

        //Background
        gc.setFill(new Color(0.81, 0.91, 0.94, 1));
        gc.fillRect(0,0,getWidth(),getHeight()*0.65);
        gc.setFill(new Color(0.59, 0.83, 0.61, 1));
        gc.fillRect(0,getHeight()*0.65,getWidth(),getHeight()-(getHeight()*0.65));

        //Runway
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(0,getHeight()*0.65,getWidth(),getHeight()*0.025);

        //Stripends
        gc.setFill(Color.MIDNIGHTBLUE);
        gc.fillRect(0,getHeight()*0.65,xMeterScale*stripend,getHeight()*0.025);
        gc.fillRect(xMeterScale*(stripend+length),getHeight()*0.65,xMeterScale*stripend,getHeight()*0.025);

    }

}
