package electionmngmntsys.controllers;

import electionmngmntsys.Launcher;
import electionmngmntsys.Utilities;
import electionmngmntsys.mlinkedlist.mLinkedList;
import electionmngmntsys.models.Candidate;
import electionmngmntsys.models.Election;
import electionmngmntsys.models.Politician;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class Individual {

    //Front End
    public Label nameLabel, listInfo, candidateElection, typeParty, locationHome, dateOfBirth, countURL;
    public ChoiceBox <String> sortType;
    public Button electionTab, politicianTab;
    public ListView listView;
    public ContextMenu contextMenu1 =new ContextMenu(), contextMenu2 =new ContextMenu();

    //Back End
    public boolean election;
    public mLinkedList <Election> mainElections, currentElections;
    public mLinkedList <Politician> mainPoliticians, currentPoliticians;
    public mLinkedList <Candidate> mainCandidates, currentCandidates;
    public Election selectedElection;
    public Politician selectedPolitician;
    public Launcher launcher;
    public ElectionListPage electionListPage;

    public void setLauncher(Launcher launcher) {
        this.launcher = launcher;
    }

    public void setElectionListPage(ElectionListPage electionListPage) {
        this.electionListPage = electionListPage;
    }

    public void setNameLabel (String name) {
        nameLabel.setText(name);
    }
    public void setListInfo (String listInfo) {
        this.listInfo.setText(listInfo);
    }
    public void setCandidateElection (String candidateElection) {
        this.candidateElection.setText(candidateElection);
    }
    public void setTypeParty (String typeParty) {
        this.typeParty.setText(typeParty);
    }
    public void setLocationHome (String locationHome) {
        this.locationHome.setText(locationHome);
    }
    public void setDateOfBirth (String dateOfBirth) {
        this.dateOfBirth.setText(dateOfBirth);
    }
    public void setCountURL (String countURL) {
        this.countURL.setText(countURL);
    }

    @FXML
    public void initialize()
    {
    }

    public void init()
    {
        if (election)
        {
            setNameLabel(selectedElection.getName());
            setTypeParty("Type: "+Utilities.electionTypeReverseMap.get(selectedElection.getType()).getValue());
            setLocationHome("Location: "+selectedElection.getLocation());
            setCountURL("Winner Count: "+selectedElection.getNumOfWinners());
            setDateOfBirth("Date: "+selectedElection.getYearDate().toString());
            mainCandidates=Utilities.copyList(selectedElection.getCandidates());
            mainPoliticians=Utilities.copyList(selectedElection.getPoliticians());
            currentCandidates=Utilities.copyList(mainCandidates);
            currentPoliticians=Utilities.copyList(mainPoliticians);
            quickSort(currentCandidates, currentPoliticians, 0, currentCandidates.size(), sortType.getSelectionModel().getSelectedItem());
        }
        else
        {
            setNameLabel(selectedPolitician.getName());
            setTypeParty("Party: "+selectedPolitician.getParty());
            setLocationHome("Home: "+selectedPolitician.getHomeCounty());
            setDateOfBirth("DOB: "+selectedPolitician.getDateOfBirth().toString());
            setCountURL("Image: "+selectedPolitician.getImageURL());
            currentElections=Utilities.copyList(mainElections);
            quickSort(currentElections, null, 0, currentElections.size(), sortType.getSelectionModel().getSelectedItem());
        }
        setLabelsAndSorting();
        initListView();
        initContextMenu();
    }
    
    public void setLabelsAndSorting()
    {
        if (election)
        {
            setCandidateElection("Candidates:");
            sortType.getItems().clear();
            sortType.getItems().addAll("Votes Ascending", "Name Ascending", "Home County Ascending", "Date of Birth Ascending", "Votes Descending", "Name Descending", "Home County Descending", "Date of Birth Descending");
            sortType.getSelectionModel().select("Votes Descending");
        }
        else
        {
            setCandidateElection("Elections:");
            sortType.getItems().clear();
            sortType.getItems().addAll("Name Ascending", "Location Ascending", "Date Ascending", "Name Descending", "Location Descending", "Date Descending");
            sortType.getSelectionModel().selectFirst();
        }
    }

    public void initContextMenu()
    {
        listView.setOnContextMenuRequested(event -> {
            contextMenu1.getItems().clear();
            contextMenu2.getItems().clear();
            MenuItem adding =new MenuItem("Add Content to this Table"), add=new MenuItem("Add Content to this Table"), visit=new MenuItem("Visit");
            adding.setOnAction(e -> {
                electionListPage.adding=true;
                if (election) {
                    electionListPage.addingToElection = selectedElection;
                }
                else
                    electionListPage.addingToPolitician=selectedPolitician;
                electionListPage.switchType();
                launcher.switchScene("electionList");
            });
            add.setOnAction(e -> {
                electionListPage.adding=true;
                if (election)
                    electionListPage.addingToElection=selectedElection;
                else
                    electionListPage.addingToPolitician=selectedPolitician;
                launcher.switchScene("electionList");
            });
            visit.setOnAction(e -> {
                if (election)
                {
                    //TODO
                }
            });
            contextMenu1.getItems().addAll(adding, visit);
            contextMenu2.getItems().add(add);
            contextMenu1.show(listView, event.getScreenX(), event.getScreenY());
            event.consume();
        });
    }

    public void initListView()
    {
        listView.getItems().clear();
        if (election)
        {
            listView.setCellFactory(lv -> new ListCell<Politician>() {
                protected void updateItem(Candidate candidate, boolean empty) {
                    super.updateItem(candidate, empty);
                    if (empty || candidate == null) {
                        setText(null);
                        setStyle("");
                    }
                    else
                    {
                        setText(candidate.toString());

                        if (true)
                        {
                            setStyle("-fx-background-color: gold; -fx-font-size: 20px; -fx-alignment: center;");
                        }
                        else
                        {
                            setStyle("-fx-font-size: 20px; -fx-alignment: center;");
                        }
                    }
                }
            });
            for (Candidate candidate : currentCandidates)
                listView.getItems().add(candidate.toString());
        }
        else
        {
            listView.setCellFactory(lv -> new ListCell<Election>() {
                protected void updateItem(Election election, boolean empty) {
                    super.updateItem(election, empty);
                    if (empty || election == null) {
                        setText(null);
                        setStyle("");
                    }
                    else
                    {
                        setText(election.toString());

                        if (true)
                        {
                            setStyle("-fx-background-color: gold; -fx-font-size: 20px; -fx-alignment: center;");
                        }
                        else
                        {
                            setStyle("-fx-font-size: 20px; -fx-alignment: center;");
                        }
                    }
                }
            });
            for (Election election : currentElections)
                listView.getItems().add(election);
        }
    }

    public void switchType()
    {

    }

    public void quickSort(mLinkedList toBeSorted, mLinkedList secondList, int start, int end, String criteria)
    {

    }
}
