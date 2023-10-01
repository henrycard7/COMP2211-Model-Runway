package runwaytool.component;

import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ChooseFileButton extends Button {
    private List<ActionListener> listeners = new ArrayList<ActionListener>();
    private FileChooser fc;
    public String filePath;
    private int id;

    public ChooseFileButton(String text, int id) {
        super(text);
        this.id = id;
        fc = new FileChooser();
        configureFileChooser();
        setOnAction(event -> {
            File selectedFile = fc.showOpenDialog(getScene().getWindow());
            if (selectedFile != null) {
                this.filePath = selectedFile.getAbsolutePath();
                for (ActionListener listener : listeners) {
                    listener.actionPerformed(new ActionEvent(this, this.id, this.filePath));
                }
            }
        });
    }

    private void configureFileChooser() {
        fc.setTitle("choose an XML file");
        fc.setInitialDirectory(new File(System.getProperty("user.home")));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml"));
    }

    public void addListener(ActionListener listener) {
        listeners.add(listener);
    }
}