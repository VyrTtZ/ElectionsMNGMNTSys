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
    public Button electionTab, politicianTab, changeVote;
    public ListView listView;
    public TextField voteField;
    public ContextMenu contextMenu1 =new ContextMenu(), contextMenu2 =new ContextMenu();

    //Back End
    public boolean election;
    public mLinkedList <Election> currentElections;
    public mLinkedList <Politician> currentPoliticians;
    public mLinkedList <Candidate> currentCandidates;
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
        sortType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            init();
        });
    }

    public void init()
    {
        if (election)
        {
            changeVote.setDisable(false);
            voteField.setDisable(false);
            setNameLabel(selectedElection.getName());
            setTypeParty("Type: "+Utilities.electionTypeReverseMap.get(selectedElection.getType()).getValue());
            setLocationHome("Location: "+selectedElection.getLocation());
            setCountURL("Winner Count: "+selectedElection.getNumOfWinners());
            setDateOfBirth("Date: "+selectedElection.getYearDate().toString());
            currentCandidates=selectedElection.getCandidates();
            currentPoliticians=selectedElection.getPoliticians();
            quickSort(currentCandidates, currentPoliticians, 0, currentCandidates.size()-1, sortType.getSelectionModel().getSelectedItem());
        }
        else
        {
            changeVote.setDisable(true);
            voteField.setDisable(true);
            setNameLabel(selectedPolitician.getName());
            setTypeParty("Party: "+selectedPolitician.getParty());
            setLocationHome("Home: "+selectedPolitician.getHomeCounty());
            setDateOfBirth("DOB: "+selectedPolitician.getDateOfBirth().toString());
            setCountURL("Image: "+selectedPolitician.getImageURL());
//            currentElections=selectedPolitician.getElections();
            quickSort(currentElections, null, 0, currentElections.size()-1, sortType.getSelectionModel().getSelectedItem());
        }
        assignRanking();
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
            sortType.getItems().addAll("Vote Ascending", "Name Ascending", "Home County Ascending", "Date of Birth Ascending", "Vote Descending", "Name Descending", "Home County Descending", "Date of Birth Descending");
            sortType.getSelectionModel().select("Vote Descending");
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
            MenuItem adding =new MenuItem("Add Content to this Table"), add=new MenuItem("Add Content to this Table"), visit=new MenuItem("Visit"), remove=new MenuItem("Remove from Election");
            adding.setOnAction(e -> {
                electionListPage.adding=true;
                if (election) {
                    electionListPage.addingToElection = selectedElection;
                    electionListPage.election=false;
                }
                else {
                    electionListPage.addingToPolitician = selectedPolitician;
                    electionListPage.election=true;
                }
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
            remove.setOnAction(e -> {
                if (election)
                {
                    int index=currentCandidates.getIndex((Candidate) listView.getSelectionModel().getSelectedItem());
                    currentCandidates.remove(currentCandidates.get(index));
                    currentPoliticians.remove(currentPoliticians.get(index));
                }
                else
                {
                    currentElections.remove(currentElections.get());
                }
            });
            visit.setOnAction(e -> {
                if (election)
                {

                }
            });
            contextMenu1.getItems().addAll(adding, visit);
            contextMenu2.getItems().add(add);
            contextMenu1.show(listView, event.getScreenX(), event.getScreenY());
        });
    }

    public void initListView()
    {
        listView.getItems().clear();
        if (election)
        {
            listView.setCellFactory(lv -> new ListCell<Candidate>() {
                protected void updateItem(Candidate candidate, boolean empty) {
                    super.updateItem(candidate, empty);
                    if (empty || candidate == null) {
                        setText(null);
                        setStyle("");
                    }
                    else
                    {
                        setText(candidate.toString());

                        if (candidate.getRanking()<=selectedElection.getNumOfWinners())
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
                listView.getItems().add(candidate);
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

    public void changeVotes()
    {
        if (listView.getSelectionModel().getSelectedItem() != null)
        {
            if (election)
            {
                if (Utilities.isValidInteger(voteField.getText()))
                {
                    ((Candidate)listView.getSelectionModel().getSelectedItem()).setVotes(Integer.parseInt(voteField.getText()));
                    assignRanking();
                    initListView();
                }
                else
                {
                    listInfo.setText("Invalid value!");
                }
            }
            else
            {
                //TODO
            }
        }
        else
            listInfo.setText("Select something!");
    }

    public void switchType()
    {
        //TODO
    }

    public void goBack()
    {
        currentPoliticians=null;
        currentCandidates=null;
        currentElections=null;
        selectedElection=null;
        selectedPolitician=null;
        launcher.switchScene("electionList");
    }

    public void editSelected()
    {

    }

    public void assignRanking()
    {
        if (election)
        {
            quickSort(currentCandidates, currentPoliticians, 0, currentCandidates.size()-1, "Vote Descending");
            int rank=1;
            for (Candidate candidate : currentCandidates)
            {
                candidate.setRanking(rank);
                rank++;
            }
        }
    }

    public void quickSort(mLinkedList toBeSorted, mLinkedList secondList, int start, int end, String criteria)
    {
        if (start>=end)
            return;
        if (start==end-1) {
            if (!election) {
                if (Utilities.compareElections((Election) toBeSorted.get(start), (Election) toBeSorted.get(end), criteria)>0) {
                    toBeSorted.swapNodes(start, end);
                    if (secondList!=null)
                        secondList.swapNodes(start, end);
                }
            }
            else
            {
                if (compareCandidates((Candidate) toBeSorted.get(start), (Candidate) toBeSorted.get(end), criteria)>0) {
                    toBeSorted.swapNodes(start, end);
                    if (secondList!=null)
                        secondList.swapNodes(start, end);
                }
            }
            return;
        }
        int i=start, j=end-1;
        Object pivot=toBeSorted.get(end);
        while (i<=j)
        {
            if (!election) {
                if (Utilities.compareElections((Election) toBeSorted.get(i), (Election) pivot, criteria) >=0)
                {
                    while (i<=j)
                    {
                        if (Utilities.compareElections((Election) toBeSorted.get(j), (Election) pivot, criteria) <0)
                            break;
                        j--;
                    }
                    if (j>i) {
                        toBeSorted.swapNodes(i, j);
                        if (secondList!=null)
                            secondList.swapNodes(i, j);
                    }
                    else
                        break;
                }
            }
            else
            {
                if (compareCandidates((Candidate) toBeSorted.get(i), (Candidate) pivot, criteria) >=0)
                {
                    while (i<=j)
                    {
                        if (compareCandidates((Candidate) toBeSorted.get(j), (Candidate) pivot, criteria) <0)
                            break;
                        j--;
                    }
                    if (j>i) {
                        toBeSorted.swapNodes(i, j);
                        if (secondList!=null)
                            secondList.swapNodes(i, j);
                    }
                    else
                        break;
                }
            }
            i++;
        }
        toBeSorted.swapNodes(i, end);
        if (secondList!=null)
            secondList.swapNodes(i, end);
        quickSort(toBeSorted, secondList, start, i-1, criteria);
        quickSort(toBeSorted, secondList, i+1, end, criteria);
    }

    private int compareCandidates(Candidate first, Candidate second, String criteria)
    {
        switch (criteria)
        {
            case "Name Ascending":
                return first.getName().compareToIgnoreCase(second.getName());
            case "Date of Birth Ascending":
                return first.getDateOfBirth().compareTo(second.getDateOfBirth());
            case "Home County Ascending":
                return first.getHomeCounty().compareToIgnoreCase(second.getHomeCounty());
            case "Vote Ascending":
                return Integer.compare(first.getVotes(), second.getVotes());
            case "Name Descending":
                return -first.getName().compareToIgnoreCase(second.getName());
            case "Date of Birth Descending":
                return -first.getDateOfBirth().compareTo(second.getDateOfBirth());
            case "Home County Descending":
                return -first.getHomeCounty().compareToIgnoreCase(second.getHomeCounty());
            case "Vote Descending":
                return -Integer.compare(first.getVotes(), second.getVotes());
        }
        return -1;
    }
}
