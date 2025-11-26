package electionmngmntsys.controllers;

import electionmngmntsys.Launcher;
import electionmngmntsys.models.Candidate;
import electionmngmntsys.models.Election;
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
    public ListPage listPage;

    public void setLauncher(Launcher launcher) {
        this.launcher = launcher;
    }

    public void setElectionListPage(ListPage listPage) {
        this.listPage = listPage;
    }

    @FXML
    public void initialize() {}

    @FXML
    public void submit() {
        Politician tmp=new Politician(politicianName.getText(), politicianDateOfBirth.getValue(), politicianParty.getText(), politicianHome.getText(), politicianURL.getText());
        if (!listPage.politicians.containsKey(tmp)) {
            if (updateIndex!=-1) {
                Politician rmv = listPage.mainPoliticianList.get(updateIndex);
                listPage.politicians.remove(rmv);
                listPage.mainPoliticianList.get(updateIndex).set(tmp);
                for (Election election: listPage.mainPoliticianList.get(updateIndex).getElections()) {
                    int i=election.getPoliticians().getIndex(tmp);
                    election.getCandidates().get(i).set(new Candidate(tmp, election, 0));
                }
            }
            else
            {
                listPage.mainPoliticianList.add(tmp);
            }
            listPage.politicians.put(tmp, 1);
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
