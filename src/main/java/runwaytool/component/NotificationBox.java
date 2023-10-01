package runwaytool.component;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;

public class NotificationBox extends VBox {
    private Text message;
    private int threadCount = 0;
    public NotificationBox() {
        setVisible(false);
        setAlignment(Pos.CENTER);
        setMouseTransparent(true);
        setStyle("-fx-background-color: lightcoral;");
        TextFlow flow = new TextFlow();
        message = new Text();
        message.setFont(Font.font(Font.getDefault().getName(), FontWeight.BOLD, 20));
        message.setText("");
        flow.getChildren().add(message);
        flow.setTextAlignment(TextAlignment.CENTER);
        flow.setMinSize(400, 30);
        flow.setMaxSize(400, 110);
        getChildren().add(flow);
        setMinSize(400, 30);
    }

    public void sendMessage(String text) {
        //If text starts with '!' replace text and reset thread
        if (text.charAt(0) == '!') {
            message.setText(text.substring(1));
            setVisible(true);
            threadCount += 1;
            new Thread(new messageThread()).start();
        } else {
            //If text starts with '?' only display if no thread running
            if (text.charAt(0) == '?') {
                if (threadCount <= 0) {
                    message.setText(text.substring(1));
                    setVisible(true);
                    threadCount += 1;
                    new Thread(new messageThread()).start();
                }
            } else {
                //If text starts with '+' append it to current message
                if (text.charAt(0) == '+' && threadCount > 0) {
                    message.setText(message.getText() + " " + text.substring(1));
                    setVisible(true);
                    threadCount += 1;
                    new Thread(new messageThread()).start();
                } else {
                    if (text.charAt(0) == '+') {
                        message.setText(text.substring(1));
                    } else {
                        message.setText(text);
                    }
                    setVisible(true);
                    threadCount += 1;
                    new Thread(new messageThread()).start();
                }
            }
        }
    }

    public class messageThread implements  Runnable {
        public void run() {
            try {
                Thread.sleep(6000);
            } catch(Exception ignored) {}
            threadCount -= 1;
            if (threadCount <= 0) {
                setVisible(false);
                threadCount = 0;
            }
        }
    }
}