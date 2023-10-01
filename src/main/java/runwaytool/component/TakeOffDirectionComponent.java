package runwaytool.component;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;

public class TakeOffDirectionComponent extends Canvas {

    public Boolean facingLeft;
    public Boolean doubleArrowhead = false;

    public TakeOffDirectionComponent(double width, double height, int runwayIndex, boolean doubleArrow) {
        super(width,height);
        this.doubleArrowhead = doubleArrow;
        if(runwayIndex == 0) facingLeft = false;
        else facingLeft = true;
        paint();
    }

    public void paint(){
        GraphicsContext gc = getGraphicsContext2D();
        double startX;
        double endX;
        if(facingLeft){
            startX = getWidth() * 0.8;
            endX = getWidth() * 0.2;
        } else {
            startX = getWidth() * 0.2;
            endX = getWidth() * 0.8;
        }
        Arrow direction;
        if (doubleArrowhead) {
            direction = new Arrow(startX,getHeight()*0.5,endX,getHeight()*0.5,"direction",
                    true,true);
        } else {
            direction = new Arrow(startX,getHeight()*0.5,endX,getHeight()*0.5,"direction",
                    true,false);
        }
        direction.setStroke(Color.BLACK);
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        gc.drawImage(direction.snapshot(parameters, null),0,0);
    }

    public void paintHover(){
        GraphicsContext gc = getGraphicsContext2D();
        double startX;
        double endX;
        if(facingLeft){
            startX = getWidth() * 0.8;
            endX = getWidth() * 0.2;
        } else {
            startX = getWidth() * 0.2;
            endX = getWidth() * 0.8;
        }

        Arrow direction = new Arrow(startX,getHeight()*0.5,endX,getHeight()*0.5,"direction",
                true,false);
        direction.setStroke(Color.RED);

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);

        gc.drawImage(direction.snapshot(parameters, null),0,0);
    }
    public void clear(){
        getGraphicsContext2D().clearRect(0,0,this.getWidth(),this.getHeight());
    }
}
