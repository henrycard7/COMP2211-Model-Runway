package runwaytool.component;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class InfoPage {
    public static void display() {
        Stage subStage = new Stage();
        ScrollPane root = new ScrollPane();
        //Background Colour
        Background background = new Background(new BackgroundFill(Color.WHITE, null, null));
        root.setBackground(background);
        Scene scene = new Scene(root, 600, 400);
        subStage.setScene(scene);
        subStage.show();

        Text title = new Text("Help Page:");
        title.setFill(Color.WHITE);
        title.setFont(new Font(30));
        title.setFill(Color.WHITE);
        title.setStroke(Color.BLACK);
        title.setStrokeWidth(1.5);


        Label ASDALabel = new Label("Accelerate-Stop Distance Available (ASDA)");
        ASDALabel.setFont(Font.font("Arial", FontWeight.BOLD,15));
        ASDALabel.setTextFill(Color.DARKCYAN);

        Label ASDALabel2 = new Label("• The length of the runway (TORA) plus any Stopway (area that is not part of the TORA, but that can be used to\n   safely stop an aircraft in an emergency). This is the total distance available to the aircraft in case of an\n   aborted takeoff.");
        ASDALabel2.setTextFill(Color.DARKBLUE);

        Label ALSLabel = new Label("Approach Landing Surface (ALS)");
        ALSLabel.setFont(Font.font("Arial", FontWeight.BOLD,15));
        ALSLabel.setTextFill(Color.DARKCYAN);

        Label ALSLabel2 = new Label("• The surface formed between the top of the obstacle and the runway when taking into account the airport’s\n   minimum angle of descent.");
        ALSLabel2.setTextFill(Color.DARKBLUE);

        Label BPLabel = new Label("Blast Protection");
        BPLabel.setFont(Font.font("Arial", FontWeight.BOLD,15));
        BPLabel.setTextFill(Color.DARKCYAN);

        Label BPLabel2 = new Label("• A safety area behind an aircraft to prevent the engine blast from affecting any obstacles located behind it.");
        BPLabel2.setTextFill(Color.DARKBLUE);

        Label CLabel = new Label("Clearway");
        CLabel.setFont(Font.font("Arial", FontWeight.BOLD,15));
        CLabel.setTextFill(Color.DARKCYAN);

        Label CLabel2 = new Label("• An area beyond the end of the TORA, which may be used during an aircraft’s initial climb to a specified height.");
        CLabel2.setTextFill(Color.DARKBLUE);

        Label DTLabel = new Label("Displaced Threshold");
        DTLabel.setFont(Font.font("Arial", FontWeight.BOLD,15));
        DTLabel.setTextFill(Color.DARKCYAN);

        Label DTLabel2 = new Label("• A runway threshold located at a point other than the physical beginning or the end of the runway.\n   The displaced portion can be used for take-off but not for landing. A landing aircraft can use the displaced\n   area on the opposite end of the runway.");
        DTLabel2.setTextFill(Color.DARKBLUE);

        Label LDALabel = new Label("Landing Distance Available (LDA)");
        LDALabel.setFont(Font.font("Arial", FontWeight.BOLD,15));
        LDALabel.setTextFill(Color.DARKCYAN);

        Label LDALabel2 = new Label("• The length of the runway available for landing. The start of this is called the threshold and is typically the\n   same as the start of the TORA. A threshold may be displaced for operational reasons or because of a\n   temporary obstacle, in which case LDA and TORA can differ.");
        LDALabel2.setTextFill(Color.DARKBLUE);

        Label RESALabel = new Label("Runway End Safety Area (RESA)");
        RESALabel.setFont(Font.font("Arial", FontWeight.BOLD,15));
        RESALabel.setTextFill(Color.DARKCYAN);

        Label RESALabel2 = new Label("• An area symmetrical about the extended runway centreline and adjacent to the end of the strip primarily\n   intended to reduce the risk of damage to an aircraft undershooting or overrunning the runway.");
        RESALabel2.setTextFill(Color.DARKBLUE);

        Label SLabel = new Label("Stopway");
        SLabel.setFont(Font.font("Arial", FontWeight.BOLD,15));
        SLabel.setTextFill(Color.DARKCYAN);

        Label SLabel2 = new Label("• An area beyond the end of the TORA, which may be used in the case of an abandoned take-off so that\n   an aircraft can be safely brought to a stop.");
        SLabel2.setTextFill(Color.DARKBLUE);

        Label SELabel = new Label("Strip End");
        SELabel.setFont(Font.font("Arial", FontWeight.BOLD,15));
        SELabel.setTextFill(Color.DARKCYAN);

        Label SELabel2 = new Label("• An area between the end of the runway and the end of the runway strip");
        SELabel2.setTextFill(Color.DARKBLUE);

        Label TORAlabel = new Label("Take-Off Run Available (TORA)");
        TORAlabel.setFont(Font.font("Arial", FontWeight.BOLD,15));
        TORAlabel.setTextFill(Color.DARKCYAN);

        Label TORAlabel2 = new Label("• The length of the runway available for take-off. This is the total distance from the point where an aircraft\n   can commence its take-off to the point where the runway can no longer support the weight of the aircraft\n   under normal conditions (and where it should have left the runway during a normal take-off)");
        TORAlabel2.setTextFill(Color.DARKBLUE);

        Label TOCSLabel = new Label("Take-Off Climb Surface (TOCS)");
        TOCSLabel.setFont(Font.font("Arial", FontWeight.BOLD,15));
        TOCSLabel.setTextFill(Color.DARKCYAN);

        Label TOCSLabel2 = new Label("• The surface formed between the runway and the top of the obstacle when taking into account the\n   airport’s minimum angle of ascent. Runway Strip - An area enclosing a runway and any associated\n   stopway. Its purpose is to reduce the risk of damage to an aeroplane running off the runway and also to\n   protect aeroplanes flying over it during landing, balked landing or take-off.");
        TOCSLabel2.setTextFill(Color.DARKBLUE);

        Label TODALabel = new Label("Take-Off Distance Available (TODA)");
        TODALabel.setFont(Font.font("Arial", FontWeight.BOLD,15));
        TODALabel.setTextFill(Color.DARKCYAN);

        Label TODALabel2 = new Label("• the length of the runway (TORA) plus any Clearway (area beyond the runway that is considered free\n   from obstructions). This is the total distance an aircraft can safely utilise for its take-off and initial ascent.");
        TODALabel2.setTextFill(Color.DARKBLUE);




        // Disable the horizontal scrollbar
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // Change the background color of the ScrollPane
        root.setStyle("-fx-background-color: lightBlue;");

        VBox contents = new VBox(title,ASDALabel,ASDALabel2,ALSLabel,ALSLabel2,BPLabel,BPLabel2,CLabel,CLabel2,DTLabel,DTLabel2,LDALabel,LDALabel2,RESALabel,RESALabel2,SLabel,SLabel2,SELabel,SELabel2,TORAlabel,TORAlabel2,TOCSLabel,TOCSLabel2,TODALabel,TODALabel2);

        root.setContent(contents);

    }
}
