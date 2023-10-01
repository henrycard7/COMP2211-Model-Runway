package runwaytool.component;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class InputTable extends GridPane {
    private TextField[][] textFields;
    private Label designator1Label;
    private Label designator2Label;
    private boolean inputOverride = false;
    public InputTable() {
        // Creating the labels for each column as per design
        Label[] columns = new Label[6];
        columns[0] = new Label(""); // Empty cell for top-left corner
        columns[1] = new Label("TORA");
        columns[2] = new Label("TODA");
        columns[3] = new Label("ASDA");
        columns[4] = new Label("LDA");
        columns[5] = new Label("DThresh");
        //adding the labels
        for (int i = 0; i < 6; i++) {
            this.add(columns[i], i, 0);
        }

        //Naming the rows for each column
        Label[] rows = new Label[2];
        designator1Label = new Label("09R");
        rows[0] = designator1Label;
        designator2Label = new Label("27L");
        rows[1] = designator2Label;
        //adding the labels
        for (int i = 0; i < 2; i++) {
            this.add(rows[i], 0, i + 1);
        }


        // Creating text fields for each of the inputs on the table
        textFields = new TextField[5][2];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                textFields[i][j] = new TextField();
                textFields[i][j].setPrefSize(50, 20);
                int finalI = i;
                int finalJ = j;
                //listener for text fields
                textFields[i][j].textProperty().addListener((observable, oldValue, inputValue) -> {
                    //checks for integer
                    if (!inputValue.matches("\\d*") && inputOverride == false) {
                        textFields[finalI][finalJ].setText(oldValue);
                    } else if (inputValue.isEmpty() && inputOverride == false) {
                        //make colour of box red
                        textFields[finalI][finalJ].setStyle("-fx-control-inner-background: red;");
                    } else {
                        if (inputOverride == false) {
                            //make colour of box white
                            textFields[finalI][finalJ].setStyle("-fx-control-inner-background: white;");
                        }
                    }
                    if (finalI == 4 && finalJ == 1) {
                        this.inputOverride = false;
                    }
                });
                this.add(textFields[i][j], i + 1, j + 1);
            }
        }

        //spacing
        this.setHgap(5);
        this.setVgap(5);
        this.setPadding(new javafx.geometry.Insets(0));
    }
    public String[][] getValues() {
        String[][] values = new String[5][2];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                values[i][j] = textFields[i][j].getText();
            }
        }
        return values;
    }

    public void setValues(String[][] values) {
        inputOverride = true;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                textFields[i][j].setText("1");
                textFields[i][j].setText(values[i][j]);
            }
        }
    }

    public void setLabels(String designator1, String designator2) {
        this.designator1Label.setText(designator1);
        this.designator2Label.setText(designator2);
    }

    public void setValuesToNothing(){
        //String[][] values = new String[5][2];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                textFields[i][j].setText(" ");
            }
        }
    }


}