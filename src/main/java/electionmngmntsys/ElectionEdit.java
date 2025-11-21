package electionmngmntsys;

import electionmngmntsys.models.Election;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ElectionEdit {

    //Front End
    public TextField electionName, electionLocation;
    public DatePicker electionDate;
    public ChoiceBox<String> electionType;
    public Slider electionWinnerCount;
    public Label electionFormMainLabel;

    //Back End
    public Launcher launcher;
    public ElectionListPage electionListPage;

    public void setLauncher(Launcher launcher) {
        this.launcher = launcher;
    }

    public void setElectionListPage(ElectionListPage electionListPage) {
        this.electionListPage = electionListPage;
    }

    @FXML
    public void initialize() {
        electionType.getItems().addAll("Local", "European", "Presidential");
        electionType.setValue("Local");
    }

    @FXML
    public void submit() {
        Election tmp=new Election(electionName.getText(), Utilities.electionTypeMap.get(electionType.getValue()).getValue(), electionLocation.getText(), electionDate.getValue(), (int)electionWinnerCount.getValue());
        if (!electionListPage.elections.containsKey(tmp)) {
            electionListPage.mainList.add(tmp);
            launcher.switchScene("electionList");
        }
        else {
            electionFormMainLabel.setText("Election already exists!");
        }
    }

    @FXML
    public void back() {
        launcher.switchScene("electionList");
    }
}
