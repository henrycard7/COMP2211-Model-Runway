package runwaytool.component;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

public class Arrow extends Pane {

    private DoubleProperty startX = new SimpleDoubleProperty();
    private DoubleProperty startY = new SimpleDoubleProperty();
    private DoubleProperty endX = new SimpleDoubleProperty();
    private DoubleProperty endY = new SimpleDoubleProperty();

    private IntegerProperty val = new SimpleIntegerProperty();
    private Line line;
    private Polygon aHead;
    private Polygon aHead2;

    private Label text;
    private Boolean doubleArrow;


    public Arrow(double sx, double sy, double ex, double ey, String val, Boolean isTop, Boolean doubleArrow) {
        startXProperty().set(sx);
        startYProperty().set(sy);
        endXProperty().set(ex);
        endYProperty().set(ey);
        this.doubleArrow = doubleArrow;

        line = new Line();
        aHead = new Polygon();
        if (doubleArrow) {
            aHead2 = new Polygon();
            aHead2.getPoints().addAll(0.0, 6.0, 6.0, 0.0, -6.0, 0.0);
        }
        aHead.getPoints().addAll(0.0, 6.0, 6.0, 0.0, -6.0, 0.0);
        text = new Label(val + "m");

        if (isTop) {
            text.setPadding(new Insets((sy+ey)/2-15,(sx+ex)/2,ey-15,(sx+ex)/2));
            text.setTranslateY(-5);
        }
        else {
            text.setPadding(new Insets((sy+ey)/2,(sx+ex)/2,ey,(sx+ex)/2));
            text.setTranslateY(5);
        }

        if (doubleArrow) getChildren().addAll(line, aHead, aHead2,text);
        else getChildren().addAll(line,aHead,text);

        startX.addListener((observable, oldValue, newValue) -> updateTwoEnded());
        endX.addListener((observable, oldValue, newValue) -> updateTwoEnded());
        startY.addListener((observable, oldValue, newValue) -> updateTwoEnded());
        endY.addListener((observable, oldValue, newValue) -> updateTwoEnded());

        updateTwoEnded();
    }

    public DoubleProperty startXProperty() {
        return startX;
    }

    public DoubleProperty startYProperty() {
        return startY;
    }

    public DoubleProperty endXProperty() {
        return endX;
    }

    public DoubleProperty endYProperty() {
        return endY;
    }

    public void setStroke(javafx.scene.paint.Color color) {
        line.setStroke(color);
        aHead.setFill(color);
        aHead.setStroke(color);
        if(doubleArrow) {
            aHead2.setFill(color);
            aHead2.setStroke(color);
        }
    }

    public void setTextFill(Color color) {
        text.setTextFill(color);
    }

    public void setStrokeWidth(double width) {
        line.setStrokeWidth(width);
    }

    public void updateOneEnded() {
        line.setStartX(startX.get());
        line.setStartY(startY.get());
        line.setEndX(endX.get());
        line.setEndY(endY.get());

        double xDiff = endX.get() - startX.get();
        double yDiff = endY.get() - startY.get();
        double angle = Math.toDegrees(Math.atan2(yDiff, xDiff));

        aHead.setLayoutX(endX.get() - aHead.getBoundsInLocal().getWidth() / 2 + 6);
        aHead.setLayoutY(endY.get() - aHead.getBoundsInLocal().getHeight() / 2);
        aHead.setRotate(angle-90);

        if(yDiff==0 && endX.get() > startX.get()){
            aHead.setLayoutX(endX.get() - aHead.getBoundsInLocal().getWidth() / 2 + 4);
        } else if (yDiff==0 && startX.get() > endX.get()){
            aHead.setLayoutX(endX.get() - aHead.getBoundsInLocal().getWidth() / 2 + 8);
        } else if (xDiff==0 && startY.get() > endY.get()){
            aHead.setLayoutY(endY.get() - aHead.getBoundsInLocal().getHeight() / 2 +2);
        } else if (xDiff==0 && endY.get() > startY.get()){
            aHead.setLayoutY(endY.get() - aHead.getBoundsInLocal().getHeight() / 2 -2);
        }

    }

    public void updateTwoEnded() {
        line.setStartX(startX.get());
        line.setStartY(startY.get());
        line.setEndX(endX.get());
        line.setEndY(endY.get());

        double xDiff = endX.get() - startX.get();
        double yDiff = endY.get() - startY.get();
        double angle = Math.toDegrees(Math.atan2(yDiff, xDiff));

        aHead.setLayoutX(endX.get() - aHead.getBoundsInLocal().getWidth() / 2 + 6);
        aHead.setLayoutY(endY.get() - aHead.getBoundsInLocal().getHeight() / 2);
        aHead.setRotate(angle-90);
        if(doubleArrow) {
            aHead2.setLayoutX(startX.get() - aHead2.getBoundsInLocal().getWidth() / 2 +6);
            aHead2.setLayoutY(startY.get() - aHead2.getBoundsInLocal().getHeight() / 2);
            aHead2.setRotate(angle+90);
        }

        if(yDiff==0 && endX.get() > startX.get()){
            aHead.setLayoutX(endX.get() - aHead.getBoundsInLocal().getWidth() / 2 + 4);
            if (doubleArrow) aHead2.setLayoutX(startX.get() - aHead2.getBoundsInLocal().getWidth() / 2 +8);
        } else if (yDiff==0 && startX.get() > endX.get()){
            aHead.setLayoutX(endX.get() - aHead.getBoundsInLocal().getWidth() / 2 + 8);
            if (doubleArrow) aHead2.setLayoutX(startX.get() - aHead2.getBoundsInLocal().getWidth() / 2 +4);
        } else if (xDiff==0 && startY.get() > endY.get()){
            aHead.setLayoutY(endY.get() - aHead.getBoundsInLocal().getHeight() / 2 +2);
            if (doubleArrow) aHead2.setLayoutY(startY.get() - aHead2.getBoundsInLocal().getHeight() / 2 -2);
        } else if (xDiff==0 && endY.get() > startY.get()){
            aHead.setLayoutY(endY.get() - aHead.getBoundsInLocal().getHeight() / 2 -2);
            if (doubleArrow) aHead2.setLayoutY(startY.get() - aHead2.getBoundsInLocal().getHeight() / 2 +2);
        }

    }

    public void rotate(double val) {
        text.setRotate(val);
        text.setScaleX(1/(((0.2*Math.cos(Math.toRadians(2*val)))/1)+0.8));
        text.setScaleY(1/(((0.2*Math.cos(Math.toRadians(2*val)))/1)+0.8));
    }
}

    // Calculate endpoint coordinates using slope
   // double endX = startX + (isTop ? 1 : -1) * Math.sqrt(1 + slope * slope) * 50;
  //double endY = startY + slope * (isTop ? 1 : -1) * Math.sqrt(1 + 1/(slope * slope)) * 50;



        // Call existing constructor with endpoint coordinates
       //this(startX, startY, endX, endY, String.format("%.1f", slope), isTop, doubleArrow);
    //}

    //public Arrow(double startX, double startY, double slope, boolean isTop) {
    //    this(startX, startY, slope, isTop, false);
    //}
//}

//creates an arrow for the ALS slope with a positive slope value of 0.1
// Arrow alsArrow = new Arrow(100, 100, 0.2, false);
//creates an arrow for the TOCS slope with a negative slope value of -0.3 and points upward
// Arrow tocsArrow = new Arrow(200, 200, -0.3, true, true);
// 'true' value for the double arrow  = double-headed arrow.