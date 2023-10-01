package runwaytool.component;

import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ChooseDirectoryButton extends Button {
    private List<ActionListener> listeners = new ArrayList<ActionListener>();
    private DirectoryChooser dc;
    public String filePath;
    private int id;

    public ChooseDirectoryButton(String text, int id) {
        super(text);
        this.id = id;
        dc = new DirectoryChooser();
        configureDirectoryChooser();
        setOnAction(event -> {
            File selectedFile = dc.showDialog(getScene().getWindow());
            if (selectedFile != null) {
                this.filePath = selectedFile.getAbsolutePath();
                for (ActionListener listener : listeners) {
                    listener.actionPerformed(new ActionEvent(this, this.id, this.filePath));
                }
            }
        });
    }

    private void configureDirectoryChooser() {
        dc.setTitle("Choose a directory");
        dc.setInitialDirectory(new File(System.getProperty("user.home")));
    }

    public void addListener(ActionListener listener) {
        listeners.add(listener);
    }
}