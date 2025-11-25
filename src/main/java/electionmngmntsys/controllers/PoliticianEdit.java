package electionmngmntsys.controllers;

import electionmngmntsys.Launcher;
import electionmngmntsys.models.Politician;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class PoliticianEdit {

    //Front End
    public TextField politicianName, politicianHome, politicianParty, politicianURL;
    public DatePicker politicianDateOfBirth;
    public Label politicianFormMainLabel;

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
    public void initialize() {}

    @FXML
    public void submit() {
        Politician tmp=new Politician(politicianName.getText(), politicianDateOfBirth.getValue(), politicianParty.getText(), politicianHome.getText(), politicianURL.getText());
        if (!electionListPage.politicians.containsKey(tmp)) {
            electionListPage.mainPoliticianList.add(tmp);
            electionListPage.politicians.put(tmp, 1);
            launcher.switchScene("electionList");
        }
        else {
            politicianFormMainLabel.setText("Politician already exists!");
        }
    }

    @FXML
    public void back() {
        launcher.switchScene("electionList");
    }
}
