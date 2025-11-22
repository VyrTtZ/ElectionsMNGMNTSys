package electionmngmntsys;

import electionmngmntsys.models.Election;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ElectionEdit {

    //Front End
    public TextField electionName, electionLocation, electionWinnerCountField;
    public DatePicker electionDate;
    public ChoiceBox<String> electionType;
    public Slider electionWinnerCount;
    public Label electionFormMainLabel, electionSliderReport;

    //Back End
    public int updateIndex=-1;
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

        electionWinnerCount.valueProperty().addListener(
                (observable, oldValue, newValue) -> electionSliderReport.setText(String.valueOf(newValue.intValue())));

        electionWinnerCountField.textProperty().addListener( (observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && Utilities.isValidInteger(newValue) && Integer.parseInt(newValue)>=0 && Integer.parseInt(newValue)<=10)
                electionWinnerCount.setValue(Integer.parseInt(newValue));});
    }

    @FXML
    public void submit() {
        Election tmp=new Election(electionName.getText(), Utilities.electionTypeMap.get(electionType.getValue()).getValue(), electionLocation.getText(), electionDate.getValue(), (int)electionWinnerCount.getValue());
        if (!electionListPage.elections.containsKey(tmp)) {
            electionListPage.mainList.add(tmp);
            electionListPage.elections.put(tmp, 1);
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
