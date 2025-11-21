package electionmngmntsys;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

public class ElectionEdit {

    //Front End
    public TextField electionName, electionLocation;
    public DatePicker electionDate;
    public ChoiceBox<String> electionType;
    public Slider electionWinnerCount;

    //Back End
    public Launcher launcher;

    public void setLauncher(Launcher launcher) {
        this.launcher = launcher;
    }

    @FXML
    public void initialize() {
        electionType.getItems().addAll("Election Type", "Local", "International");
        electionType.setValue("Election Type");
    }

    @FXML
    public void submit() {

    }

    @FXML
    public void back() {

    }
}
