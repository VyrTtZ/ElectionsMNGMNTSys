package electionmngmntsys.controllers;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import electionmngmntsys.Launcher;
import electionmngmntsys.Utilities;
import electionmngmntsys.mhashmap.mHashMap;
import electionmngmntsys.mlinkedlist.mLinkedList;
import electionmngmntsys.models.Candidate;
import electionmngmntsys.models.Election;
import electionmngmntsys.models.Politician;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Modality;

import java.io.*;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;


public class ListPage {

    ///ELECTION STUFF
    //Front End
    public ChoiceBox <String> sortCriteria, saveLoad;
    public TextField searchBar;
    public Canvas canvas;
    public Label filters, listInfo;
    public CheckBox changeSortingType;
    public ScrollPane scrollPane;

    public ContextMenu validContextMenu =new ContextMenu(), invalidContextMenu =new ContextMenu(), addContextMenu =new ContextMenu();

    //Election Back End
    public mLinkedList <Election> mainElectionList =new mLinkedList<>(), currentElectionList =new mLinkedList<>();
    public mHashMap<Election, Integer> elections=new mHashMap<>();
    public Election addingToElection;
    public ElectionEdit electionEdit;
    private String electionIncludeYear ="", electionExcludeYear ="";
    private boolean localType=true, europeanType=true, presidentialType=true; //True -> include
    private boolean electionUsingQuickSort =true, electionSerious =true;

    //Politician Back End
    public mLinkedList <Politician> mainPoliticianList=new mLinkedList<>(), currentPoliticianList =new mLinkedList<>();
    public mHashMap<Politician, Integer> politicians=new mHashMap<>();
    public Politician addingToPolitician;
    public PoliticianEdit politicianEdit;
    private String politicianIncludeYear ="", politicianExcludeYear ="", politicianIncludeParty="", politicianExcludeParty="";
    private boolean politicianUsingQuickSort=true, politicianSerious=true;

    //General
    public GraphicsContext gc;
    public Launcher launcher;
    public Button electionTab, politicianTab;
    public boolean election=true, adding=false;
    public Individual individual;

    public void setLauncher(Launcher launcher) {
        this.launcher = launcher;
    }

    public void setElectionEdit(ElectionEdit electionEdit) {
        this.electionEdit = electionEdit;
    }

    public void setPoliticianEdit(PoliticianEdit politicianEdit) {
        this.politicianEdit = politicianEdit;
    }

    public void setIndividual(Individual individual)
    {
        this.individual=individual;
    }

    @FXML
    public void initialize() {
        Utilities.initMap();
        gc = canvas.getGraphicsContext2D();

        reInit();
        canvas.setOnMousePressed(event -> {
            if (validContextMenu.isShowing()) {
                validContextMenu.hide();
            }
            if (invalidContextMenu.isShowing()) {
                invalidContextMenu.hide();
            }
        });
        sortCriteria.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            draw();
        });
        saveLoad.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (Objects.equals(saveLoad.getSelectionModel().getSelectedItem(), "Save"))
            {
                try {
                    save();
                    listInfo.setText("Saved!");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            else if (Objects.equals(saveLoad.getSelectionModel().getSelectedItem(), "Load"))
            {
                try {
                    load();
                    reInit();
                    draw();
                    listInfo.setText("Loaded!");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            draw();
        });
        saveLoad.getItems().clear();
        saveLoad.getItems().addAll("Nothing", "Save", "Load");
        saveLoad.setValue("Nothing");
        draw();
    }

    @FXML
    public void initSortingChoiceBox()
    {
        sortCriteria.getItems().clear();
        if (election)
        {
            sortCriteria.getItems().addAll("Name Ascending", "Type Ascending", "Year Ascending", "Location Ascending", "Name Descending", "Type Descending", "Year Descending", "Location Descending");
            sortCriteria.setValue("Name Ascending");
        }
        else
        {
            sortCriteria.getItems().addAll("Name Ascending", "Party Ascending", "Date of Birth Ascending", "Home County Ascending", "Name Descending", "Party Descending", "Date of Birth Descending", "Home County Descending");
            sortCriteria.setValue("Name Ascending");
        }
    }

    @FXML
    public void switchType()
    {
        election=!election;
        reInit();
        draw();
    }

    private void initTabs()
    {
        if (election && !adding) {
            electionTab.setDisable(true);
            politicianTab.setDisable(false);
            changeSortingType.setText("Search by Location");
        }
        else if (!election && !adding)
        {
            changeSortingType.setText("Search by Home");
            electionTab.setDisable(false);
            politicianTab.setDisable(true);
        }
        if (adding) {
            electionTab.setDisable(true);
            politicianTab.setDisable(true);
        }
    }

    private void reInit()
    {
        initTabs();
        initSortingChoiceBox();
        initContextMenus();
        filters.setOnMousePressed(event -> {
            try {
                FXMLLoader filterLoader;
                DialogPane dialogContent;
                Object controller;
                if (election) {
                    ElectionFilterEdit tmpController;
                    filterLoader = new FXMLLoader(getClass().getResource("/electionFilterEdit.fxml"));
                    dialogContent = filterLoader.load();
                    tmpController = filterLoader.getController();

                    tmpController.setIncludeYearField(electionIncludeYear);
                    tmpController.setExcludeYearField(electionExcludeYear);
                    tmpController.setLocalType(!localType);
                    tmpController.setEuropeanType(!europeanType);
                    tmpController.setPresidentialType(!presidentialType);
                    controller=tmpController;
                }
                else
                {
                    PoliticianFilterEdit tmpController;
                    filterLoader = new FXMLLoader(getClass().getResource("/politicianFilterEdit.fxml"));
                    dialogContent = filterLoader.load();
                    tmpController = filterLoader.getController();

                    tmpController.setIncludeYearField(politicianIncludeYear);
                    tmpController.setExcludeYearField(politicianExcludeYear);
                    tmpController.setIncludePartyField(politicianIncludeParty);
                    tmpController.setExcludePartyField(politicianExcludeParty);
                    controller=tmpController;

                }
                Dialog <ButtonType> dialog = new Dialog<>();
                dialog.initOwner(Launcher.stage);
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.getDialogPane().setContent(dialogContent);
                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                Optional<ButtonType> result = dialog.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    if (election) {
                        ElectionFilterEdit tmpController=(ElectionFilterEdit)controller;
                        electionIncludeYear = tmpController.getIncludeYearField().trim();
                        electionExcludeYear = tmpController.getExcludeYearField().trim();
                        localType = !tmpController.getLocalType();
                        europeanType = !tmpController.getEuropeanType();
                        presidentialType = !tmpController.getPresidentialType();
                    }
                    else
                    {
                        PoliticianFilterEdit tmpController=(PoliticianFilterEdit)controller;
                        politicianIncludeYear=tmpController.getIncludeYearField().trim();
                        politicianExcludeYear=tmpController.getExcludeYearField().trim();
                        politicianIncludeParty=tmpController.getIncludePartyField().trim();
                        politicianExcludeParty=tmpController.getExcludePartyField().trim();
                    }
                    String tmp = "Active filters (right click to edit):";
                    if (election) {
                        if (!localType || !europeanType || !presidentialType) {
                            tmp += " T";
                            if (!electionIncludeYear.isEmpty() || !electionExcludeYear.isEmpty())
                                tmp += ", Y;";
                            else
                                tmp += ";";
                        }
                        else if (!electionIncludeYear.isEmpty() || !electionExcludeYear.isEmpty())
                            tmp += " Y;";
                    }
                    else
                    {
                        if (!politicianIncludeYear.isEmpty() || !politicianExcludeYear.isEmpty())
                        {
                            tmp+=" Y";
                            if (!politicianIncludeParty.isEmpty() || !politicianExcludeParty.isEmpty())
                                tmp+=", P;";
                            else
                                tmp+=";";
                        }
                        else if (!politicianIncludeParty.isEmpty() || !politicianExcludeParty.isEmpty())
                            tmp+=" P;";
                    }
                    filters.setText(tmp);
                    draw();
                }
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    @FXML
    public void initContextMenus()
    {
        addContextMenu.getItems().clear();
        validContextMenu.getItems().clear();
        MenuItem add=new MenuItem("Add"), add1=new MenuItem("Add"), delete=new MenuItem("Delete"), update=new MenuItem("Update"), visit=new MenuItem("Visit"), back=new MenuItem("Back");
        add.setOnAction(event -> {
            if (election)
                launcher.switchScene("electionForm");
            else
                launcher.switchScene("politicianForm");
        });
        add1.setOnAction(event -> {
            if (election)
                launcher.switchScene("electionForm");
            else
                launcher.switchScene("politicianForm");
        });
        back.setOnAction(event -> {
            if (election)
            {
                individual.selectedPolitician=addingToPolitician;
                individual.selectedElection=null;
            }
            else
            {
                individual.selectedPolitician=null;
                individual.selectedElection=addingToElection;
            }
            individual.init();
            launcher.switchScene("individual");
        });
        validContextMenu.getItems().add(add);
        canvas.setOnContextMenuRequested(event -> {
            double scrollOffset=scrollPane.getVvalue()*Math.max(0, canvas.getHeight()-scrollPane.getViewportBounds().getHeight());
            double actualY=event.getScreenY()-104+scrollOffset;
            int index=getIndexOfMouse((int) event.getX(), (int) actualY); //scrolled
            MenuItem addTo=new MenuItem("Add");

            if (index!=-1 && ((election && index<= currentElectionList.size()-1) || (!election && index<=currentPoliticianList.size()-1)))  {
                addTo.setOnAction(e -> {
                    if (!election) {
                        if (!addingToElection.getDuplicateCheck().containsKey(currentPoliticianList.get(index))) {
                            addingToElection.getPoliticians().add(currentPoliticianList.get(index));
                            addingToElection.getDuplicateCheck().put(currentPoliticianList.get(index), 1);
                            addingToElection.getCandidates().add(new Candidate(currentPoliticianList.get(index), addingToElection, 0));
                            Politician curPol = currentPoliticianList.get(index);
                            int i = mainPoliticianList.getIndex(curPol);
                            mainPoliticianList.get(i).getElections().add(addingToElection);
                        }
                        else
                        {
                            listInfo.setText("That politician is in the election already!");
                        }
                    }
                    else
                    {
                        if (!addingToPolitician.getDuplicateCheck().containsKey(currentElectionList.get(index))) {
                            addingToPolitician.getDuplicateCheck().put(currentElectionList.get(index), 1);
                            addingToPolitician.getVotesList().add(0);
                            addingToPolitician.getAssociations().add(addingToPolitician.getParty());
                            addingToPolitician.getElections().add(currentElectionList.get(index));
                            int i=mainElectionList.getIndex(currentElectionList.get(index));
                            mainElectionList.get(i).getPoliticians().add(addingToPolitician);
                            mainElectionList.get(i).getCandidates().add(new Candidate(addingToPolitician, addingToElection, 0));
                        }
                        else
                        {
                            listInfo.setText("This politician is in that election already!");
                        }
                    }
                });
                delete.setOnAction(e -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.initOwner(Launcher.stage);
                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText(null);
                    if (election)
                        alert.setContentText("Are you sure you want to delete this election?");
                    else
                        alert.setContentText("Are you sure you want to delete this politician?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        if (election) {
                            mainElectionList.remove(currentElectionList.get(index));
                            elections.remove(currentElectionList.get(index));
                        }
                        else
                        {
                            mainPoliticianList.remove(currentPoliticianList.get(index));
                            politicians.remove(currentPoliticianList.get(index));
                        }
                    }
                    draw();
                });
                update.setOnAction(e -> {
                    if (election) {
                        Election tmp = currentElectionList.get(index);
                        electionEdit.electionName.setText(tmp.getName());
                        electionEdit.electionLocation.setText(tmp.getLocation());
                        electionEdit.electionType.setValue(Utilities.electionTypeReverseMap.get(tmp.getType()).getValue());
                        electionEdit.electionWinnerCount.setValue(tmp.getNumOfWinners());
                        electionEdit.updateIndex = mainElectionList.getIndex(tmp);
                        launcher.switchScene("electionForm");
                    }
                    else
                    {
                        Politician tmp = currentPoliticianList.get(index);
                        politicianEdit.politicianName.setText(tmp.getName());
                        politicianEdit.politicianHome.setText(tmp.getHomeCounty());
                        politicianEdit.politicianParty.setText(tmp.getParty());
                        politicianEdit.politicianURL.setText(tmp.getImageURL());
                        politicianEdit.updateIndex = mainPoliticianList.getIndex(tmp);
                        launcher.switchScene("politicianForm");
                    }
                });
                visit.setOnAction(e -> {
                    if (election)
                    {
                        individual.selectedElection=currentElectionList.get(index);
                        individual.selectedPolitician=null;
                        individual.election=true;
                    }
                    else
                    {
                        individual.selectedPolitician=currentPoliticianList.get(index);
                        individual.selectedElection=null;
                        individual.election=false;
                    }
                    individual.init();
                    launcher.switchScene("individual");
                });
                validContextMenu.getItems().add(visit);
                validContextMenu.getItems().add(delete);
                validContextMenu.getItems().add(update);

                addContextMenu.getItems().clear();
                addContextMenu.getItems().add(back);
                addContextMenu.getItems().add(addTo);
                if (!adding)
                    validContextMenu.show(canvas, event.getScreenX(), event.getScreenY());
                else
                    addContextMenu.show(canvas, event.getScreenX(), event.getScreenY());
            }
            else if (!adding)
                invalidContextMenu.show(canvas, event.getScreenX(), event.getScreenY());
            else {
                addContextMenu.getItems().clear();
                addContextMenu.getItems().add(back);
                addContextMenu.show(canvas, event.getScreenX(), event.getScreenY());
            }
        });

        invalidContextMenu.getItems().clear();
        invalidContextMenu.getItems().add(add1);

        invalidContextMenu.setStyle("-fx-font-size: 20px;");
        validContextMenu.setStyle("-fx-font-size: 20px;");
        addContextMenu.setStyle("-fx-font-size: 20px;");
    }

    @FXML
    public void draw() {
        int x=50, y=20;
        initTabs();
        searchFilterAndSort();
        String imagePath;
        if (election) {
            gc.setFill(Color.AQUAMARINE);
            if (electionSerious)
                imagePath = "/images/election.jpg";
            else
                imagePath = "/images/election.png";
        }
        else
        {
            gc.setFill(Color.LIGHTGOLDENRODYELLOW);
            if (politicianSerious)
                imagePath = "/images/politician.jpg";
            else
                imagePath = "/images/politician.gif";
        }
        Image image=new Image(getClass().getResourceAsStream(imagePath));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        if (election) {
            for (Election e : currentElectionList) {
                gc.setFill(Color.WHITE);
                gc.fillRect(x, y, canvas.getWidth() - 100, 200);
                gc.drawImage(image, x + 20, y + 20, 160, 160);
                gc.setFill(Color.BLACK);
                gc.fillText("Election " + e.getName(), x + 200, y + 20);
                gc.fillText("Election Type: " + Utilities.electionTypeReverseMap.get(e.getType()).getValue(), x + 200, y + 52);
                gc.fillText("Election Location: " + e.getLocation(), x + 200, y + 82);
                gc.fillText("Date: " + e.getYearDate(), x + 200, y + 112);
                gc.fillText("Notable info: Nothing here yet lmao", x + 200, y + 142);
                y += 220;
            }
        }
        else
        {
            for (Politician p : currentPoliticianList) {
                gc.setFill(Color.WHITE);
                gc.fillRect(x, y, canvas.getWidth() - 100, 200);
                gc.drawImage(image, x + 20, y + 20, 160, 160);
                gc.setFill(Color.BLACK);
                gc.fillText("Name: " + p.getName(), x + 200, y + 20);
                gc.fillText("Political Party: " + p.getParty(), x + 200, y + 52);
                gc.fillText("Home County: " + p.getHomeCounty(), x + 200, y + 82);
                gc.fillText("Date of Birth: " + p.getDateOfBirth(), x + 200, y + 112);
                gc.fillText("Notable info: Nothing here yet lmao", x + 200, y + 142);
                y += 220;
            }
        }
    }

    private void setInfo() {
        String text;
        if (!adding) {
            if (election) {
                if (electionUsingQuickSort)
                    text = "Using quick sort. Try typing bogo or lmao! Total elections count: ";
                else
                    text = "Using bogo sort. Try typing quick or lockin! Total elections count: ";
                text += mainElectionList.size();
                text += ". Current elections count: ";
                text += currentElectionList.size();
            } else {
                if (politicianUsingQuickSort)
                    text = "Using quick sort. Try typing bogo or lmao! Total politicians count: ";
                else
                    text = "Using bogo sort. Try typing quick or lockin! Total politicians count: ";
                text += mainPoliticianList.size();
                text += ". Current politicians count: ";
                text += currentPoliticianList.size();
            }
            text += ".";
        }
        else
        {
            if (!election)
                text="Adding to the election "+addingToElection.getName()+".";
            else
                text="Adding to the politician "+addingToPolitician.getName()+".";
        }
        listInfo.setText(text);
    }

    private void searchFilterAndSort()
    {
        if (election) {
            currentElectionList = Utilities.copyList(mainElectionList);
            search();
            filter();
            if (electionUsingQuickSort)
                quickSort(currentElectionList, 0, currentElectionList.size() - 1, sortCriteria.getValue());
            else
                bogoSort(currentElectionList, sortCriteria.getValue());
        }
        else
        {
            currentPoliticianList=Utilities.copyList(mainPoliticianList);
            search();
            filter();
            if (politicianUsingQuickSort)
                quickSort(currentPoliticianList, 0, currentPoliticianList.size() - 1, sortCriteria.getValue());
            else
                bogoSort(currentPoliticianList, sortCriteria.getValue());
        }
        setInfo();
    }

    private void search()
    {
        mLinkedList newList = new mLinkedList<>();
        if (searchBar.getText().isEmpty())
            return;
        if (searchBar.getText().equalsIgnoreCase("bogo") || searchBar.getText().equalsIgnoreCase("quick")) {
            if (election)
                electionUsingQuickSort = !electionUsingQuickSort;
            else
                politicianUsingQuickSort = !politicianUsingQuickSort;
        }
        if (searchBar.getText().equalsIgnoreCase("lmao") || searchBar.getText().equalsIgnoreCase("lockin")) {
            if (election)
                electionSerious = !electionSerious;
            else
                politicianSerious = !politicianSerious;
        }
        if (election) {
            for (Election e : currentElectionList) {
                if (changeSortingType.isSelected()) {
                    if (e.getLocation().contains(searchBar.getText()))
                        newList.add(e);
                }
                else {
                    if (e.getName().contains(searchBar.getText()))
                        newList.add(e);
                }
            }
            currentElectionList=Utilities.copyList(newList);
        }
        else
        {
            for (Politician p : currentPoliticianList) {
                if (changeSortingType.isSelected()) {
                    if (p.getHomeCounty().contains(searchBar.getText()))
                        newList.add(p);
                }
                else {
                    if (p.getName().contains(searchBar.getText()))
                        newList.add(p);
                }
            }
            currentPoliticianList=Utilities.copyList(newList);
        }
    }

    private void filter()
    {
        if (election) {
            mLinkedList<Election> newList = new mLinkedList<>();
            for (Election e : currentElectionList) {
                boolean flag = false;
                switch (e.getType()) {
                    case 1:
                        if (!localType)
                            flag = true;
                        break;
                    case 2:
                        if (!europeanType)
                            flag = true;
                        break;
                    case 3:
                        if (!presidentialType)
                            flag = true;
                        break;
                }
                if (!electionIncludeYear.isEmpty()) {
                    boolean flag2 = false;
                    for (int i = 0; i < Utilities.getCommaSeparatedStringSize(electionIncludeYear); i++) {
                        String tmp = Utilities.getValueFromCommaSeparatedString(i, electionIncludeYear);
                        if (Utilities.isValidInteger(tmp)) {
                            if (e.getYearDate().getYear() == Integer.parseInt(tmp)) {
                                flag2 = true;
                                break;
                            }
                        }
                    }
                    if (!flag2)
                        flag = true;
                }
                if (!electionExcludeYear.isEmpty()) {
                    for (int i = 0; i < Utilities.getCommaSeparatedStringSize(electionExcludeYear); i++) {
                        String tmp = Utilities.getValueFromCommaSeparatedString(i, electionExcludeYear);
                        if (Utilities.isValidInteger(tmp)) {
                            if (e.getYearDate().getYear() == Integer.parseInt(tmp)) {
                                flag = true;
                                break;
                            }
                        }
                    }
                }
                if (!flag)
                    newList.add(e);
            }
            currentElectionList = Utilities.copyList(newList);
        }
        else
        {
            mLinkedList<Politician> newList = new mLinkedList<>();
            for (Politician p : currentPoliticianList) {
                boolean flag = false;
                if (!politicianIncludeYear.isEmpty()) {
                    boolean flag2 = false;
                    for (int i = 0; i < Utilities.getCommaSeparatedStringSize(politicianIncludeYear); i++) {
                        String tmp = Utilities.getValueFromCommaSeparatedString(i, politicianIncludeYear);
                        if (Utilities.isValidInteger(tmp)) {
                            if (p.getDateOfBirth().getYear() == Integer.parseInt(tmp)) {
                                flag2 = true;
                                break;
                            }
                        }
                    }
                    if (!flag2)
                        flag = true;
                }
                if (!politicianExcludeYear.isEmpty()) {
                    for (int i = 0; i < Utilities.getCommaSeparatedStringSize(politicianExcludeYear); i++) {
                        String tmp = Utilities.getValueFromCommaSeparatedString(i, politicianExcludeYear);
                        if (Utilities.isValidInteger(tmp)) {
                            if (p.getDateOfBirth().getYear() == Integer.parseInt(tmp)) {
                                flag = true;
                                break;
                            }
                        }
                    }
                }
                if (!politicianIncludeParty.isEmpty()) {
                    boolean flag2 = false;
                    for (int i = 0; i < Utilities.getCommaSeparatedStringSize(politicianIncludeParty); i++) {
                        String tmp = Utilities.getValueFromCommaSeparatedString(i, politicianIncludeParty);
                        if (p.getParty().equalsIgnoreCase(tmp)) {
                            flag2 = true;
                            break;
                        }
                    }
                    if (!flag2)
                        flag = true;
                }
                if (!politicianExcludeParty.isEmpty()) {
                    for (int i = 0; i < Utilities.getCommaSeparatedStringSize(politicianExcludeParty); i++) {
                        String tmp = Utilities.getValueFromCommaSeparatedString(i, politicianExcludeParty);
                        if (p.getParty().equalsIgnoreCase(tmp)) {
                            flag = true;
                            break;
                        }
                    }
                }
                if (!flag)
                    newList.add(p);
            }
            currentPoliticianList = Utilities.copyList(newList);
        }
    }

    private int comparePoliticians(Politician first, Politician second, String criteria)
    {
        switch (criteria)
        {
            case "Name Ascending":
                return first.getName().compareToIgnoreCase(second.getName());
            case "Party Ascending":
                return first.getParty().compareToIgnoreCase(second.getParty());
            case "Date of Birth Ascending":
                return first.getDateOfBirth().compareTo(second.getDateOfBirth());
            case "Home County Ascending":
                return first.getHomeCounty().compareToIgnoreCase(second.getHomeCounty());
            case "Name Descending":
                return -first.getName().compareToIgnoreCase(second.getName());
            case "Party Descending":
                return -first.getParty().compareToIgnoreCase(second.getParty());
            case "Date of Birth Descending":
                return -first.getDateOfBirth().compareTo(second.getDateOfBirth());
            case "Home County Descending":
                return -first.getHomeCounty().compareToIgnoreCase(second.getHomeCounty());
        }
        return -1;
    }

    private void bogoSort(mLinkedList list, String criteria)
    {
        int iteration=0;
        mHashMap <mLinkedList, Integer> map=new mHashMap<>();
        while (!isSorted(list, criteria))
        {
            do
            {
                shuffleList(list);
            }
            while (map.containsKey(list));
            iteration++;
            if (iteration%10000==0)
                System.out.println("Iteration: "+iteration);
            if (iteration>=1000000000)
            {
                System.out.println("Unsorted, iteration count passed 1 billion, terminating...");
                return;
            }
        }
        System.out.println("Sorted in "+iteration+" iterations.");
    }

    private void shuffleList(mLinkedList list)
    {
        Random rand = new Random();
        for (int i= list.size()-1; i>=0; i--)
        {
            int j=rand.nextInt(i+1);
            list.swapNodes(i, j);
        }
    }

    private boolean isSorted(mLinkedList list, String criteria)
    {
        for (int i = 0; i < list.size()-1; i++) {
            if (election)
                if (Utilities.compareElections((Election) list.get(i), (Election) list.get(i + 1), criteria) < 0)
                    return false;
            else
                if (comparePoliticians((Politician) list.get(i), (Politician) list.get(i + 1), criteria) < 0)
                    return false;
        }
        return true;
    }

    private void quickSort(mLinkedList list, int start, int end, String criteria)
    {
        if (start>=end)
            return;
        if (start==end-1) {
            if (election) {
                if (Utilities.compareElections((Election) list.get(start), (Election) list.get(end), criteria) == 1)
                    list.swapNodes(start, end);
            }
            else
            {
                if (comparePoliticians((Politician) list.get(start), (Politician) list.get(end), criteria) == 1)
                    list.swapNodes(start, end);
            }
            return;
        }
        int i=start, j=end-1;
        Object pivot=list.get(end);
        while (i<=j)
        {
            if (election) {
                if (Utilities.compareElections((Election) list.get(i), (Election) pivot, criteria) >=0)
                {
                    while (i<=j)
                    {
                        if (Utilities.compareElections((Election) list.get(j), (Election) pivot, criteria) <0)
                            break;
                        j--;
                    }
                    if (j>i)
                        list.swapNodes(i, j);
                    else
                        break;
                }
            }
            else
            {
                if (comparePoliticians((Politician) list.get(i), (Politician) pivot, criteria) >=0)
                {
                    while (i<=j)
                    {
                        if (comparePoliticians((Politician) list.get(j), (Politician) pivot, criteria) <0)
                            break;
                        j--;
                    }
                    if (j>i)
                        list.swapNodes(i, j);
                    else
                        break;
                }
            }
            i++;
        }
        list.swapNodes(i, end);
        quickSort(list, start, i-1, criteria);
        quickSort(list, i+1, end, criteria);
    }

    public int getIndexOfMouse(int x, int y)
    {
        int index=y%220;
        if (x<50 || x> canvas.getWidth()-50)
            return -1;
        if (index==0)
            return y/220-1;
        else
        {
            int tmp=y/220;
            y-=tmp*220;
            if (y>=20)
                return tmp;
        }
        return -1;
    }

    public void save() throws Exception {
        XStream xstream=new XStream(new DomDriver());
        ObjectOutputStream out=xstream.createObjectOutputStream(new FileWriter("Elections.xml"));
        out.writeObject(mainElectionList);
        out.writeObject(mainPoliticianList);
        out.close();
    }

    public void load() throws Exception {
        Class<?>[] classes = new Class[] { ListPage.class, mLinkedList.class, Election.class, Politician.class, Candidate.class};

        //setting up the xstream object with default security and the above classes
        XStream xstream = new XStream(new DomDriver());
        XStream.setupDefaultSecurity(xstream);
        xstream.allowTypes(classes);

        //doing the actual serialisation to an XML file
        ObjectInputStream in = xstream.createObjectInputStream(new FileReader("Elections.xml"));
        mainElectionList=Utilities.copyList((mLinkedList <Election>) in.readObject());
        mainPoliticianList=Utilities.copyList((mLinkedList <Election>) in.readObject());
        in.close();
    }
}
