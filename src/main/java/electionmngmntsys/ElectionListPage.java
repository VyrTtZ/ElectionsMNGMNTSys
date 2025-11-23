package electionmngmntsys;

import electionmngmntsys.mhashmap.mHashMap;
import electionmngmntsys.mlinkedlist.mLinkedList;
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

import java.io.IOException;
import java.util.Optional;
import java.util.Random;


public class ElectionListPage {

    ///ELECTION STUFF
    //Front End
    public ChoiceBox <String> sortCriteria;
    public TextField searchBar;
    public Canvas canvas;
    public Label filters, listInfo;
    public CheckBox changeSortingType;

    public ContextMenu validContextMenu =new ContextMenu(), invalidContextMenu =new ContextMenu();

    //Election Back End
    public mLinkedList <Election> mainElectionList =new mLinkedList<>(), currentElectionList =new mLinkedList<>();
    public mHashMap<Election, Integer> elections=new mHashMap<>();
    public GraphicsContext electionGc;
    public Launcher launcher;
    public ElectionEdit electionEdit;
    private String electionIncludeYear ="", electionExcludeYear ="";
    private boolean localType=true, europeanType=true, presidentialType=true; //True -> include
    private boolean electionUsingQuickSort =true, electionSerious =true;

    //Politician Back End
    public mLinkedList <Politician> mainPoliticianList=new mLinkedList<>(), currentPoliticianList =new mLinkedList<>();
    public mHashMap<Politician, Integer> politicians=new mHashMap<>();
    public PoliticianEdit politicianEdit;
    private String politicianIncludeYear ="", politicianExcludeYear ="", politicianIncludeParty="", politicianExcludeParty="";
    private boolean politicianUsingQuickSort=true, politicianSerious=true;

    //General
    public Button electionTab, politicianTab;
    public boolean election=true;

    public void setLauncher(Launcher launcher) {
        this.launcher = launcher;
    }

    public void setElectionEdit(ElectionEdit electionEdit) {
        this.electionEdit = electionEdit;
    }

    public void setPoliticianEdit(PoliticianEdit politicianEdit) {
        this.politicianEdit = politicianEdit;
    }

    @FXML
    public void initialize() {
        Utilities.initMap();
        electionGc= canvas.getGraphicsContext2D();

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
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            draw();
        });
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

    private void reInit()
    {
        if (election) {
            electionTab.setDisable(true);
            politicianTab.setDisable(false);
            changeSortingType.setText("Search by Location");
        }
        else
        {
            changeSortingType.setText("Search by Home");
            electionTab.setDisable(false);
            politicianTab.setDisable(true);
        }
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
        validContextMenu.getItems().clear();
        MenuItem add=new MenuItem("Add"), delete=new MenuItem("Delete"), update=new MenuItem("Update");
        add.setOnAction(event -> {
            if (election)
                launcher.switchScene("electionForm");
            else
                launcher.switchScene("politicianForm");
        });
        validContextMenu.getItems().add(add);
        canvas.setOnContextMenuRequested(event -> {
            int index=getIndexOfMouse((int) event.getScreenX(), (int) event.getScreenY()-104); //top buttons and stuff
            if (index!=-1 && ((election && index<= currentElectionList.size()-1) || (!election && index<=currentPoliticianList.size()-1)))  {
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
                        electionEdit.updateIndex = index;
                        mainElectionList.remove(tmp);
                        elections.remove(tmp);
                        launcher.switchScene("electionForm");
                    }
                    else
                    {
                        Politician tmp = currentPoliticianList.get(index);
                        politicianEdit.politicianName.setText(tmp.getName());
                        politicianEdit.politicianHome.setText(tmp.getHomeCountry());
                        politicianEdit.politicianParty.setText(tmp.getParty());
                        politicianEdit.politicianURL.setText(tmp.getImageURL());
                        politicianEdit.updateIndex = index;
                        mainPoliticianList.remove(tmp);
                        politicians.remove(tmp);
                        launcher.switchScene("politicianForm");
                    }
                });
                validContextMenu.getItems().add(delete);
                validContextMenu.getItems().add(update);
                validContextMenu.show(canvas, event.getScreenX(), event.getScreenY());
            }
            else
                invalidContextMenu.show(canvas, event.getScreenX(), event.getScreenY());
        });

        invalidContextMenu.getItems().clear();
        invalidContextMenu.getItems().add(add);

        invalidContextMenu.setStyle("-fx-font-size: 20px;");
        validContextMenu.setStyle("-fx-font-size: 20px;");
    }

    @FXML
    public void draw() {
        int x=50, y=20;
        searchFilterAndSort();
        String imagePath;
        if (election) {
            electionGc.setFill(Color.AQUAMARINE);
            if (electionSerious)
                imagePath = "/images/election.jpg";
            else
                imagePath = "/images/election.png";
        }
        else
        {
            electionGc.setFill(Color.LIGHTGOLDENRODYELLOW);
            if (politicianSerious)
                imagePath = "/images/politician.jpg";
            else
                imagePath = "/images/politician.gif";
        }
        Image image=new Image(getClass().getResourceAsStream(imagePath));
        electionGc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        if (election) {
            for (Election e : currentElectionList) {
                electionGc.setFill(Color.WHITE);
                electionGc.fillRect(x, y, canvas.getWidth() - 100, 200);
                electionGc.drawImage(image, x + 20, y + 20, 160, 160);
                electionGc.setFill(Color.BLACK);
                electionGc.fillText("Election " + e.getName(), x + 200, y + 20);
                electionGc.fillText("Election Type: " + Utilities.electionTypeReverseMap.get(e.getType()).getValue(), x + 200, y + 52);
                electionGc.fillText("Election Location: " + e.getLocation(), x + 200, y + 82);
                electionGc.fillText("Date: " + e.getYearDate(), x + 200, y + 112);
                electionGc.fillText("Notable info: Nothing here yet lmao", x + 200, y + 142);
                y += 220;
            }
        }
        else
        {
            for (Politician p : currentPoliticianList) {
                electionGc.setFill(Color.WHITE);
                electionGc.fillRect(x, y, canvas.getWidth() - 100, 200);
                electionGc.drawImage(image, x + 20, y + 20, 160, 160);
                electionGc.setFill(Color.BLACK);
                electionGc.fillText("Name: " + p.getName(), x + 200, y + 20);
                electionGc.fillText("Political Party: " + p.getParty(), x + 200, y + 52);
                electionGc.fillText("Home County: " + p.getHomeCountry(), x + 200, y + 82);
                electionGc.fillText("Date of Birth: " + p.getDateOfBirth(), x + 200, y + 112);
                electionGc.fillText("Notable info: Nothing here yet lmao", x + 200, y + 142);
                y += 220;
            }
        }
    }

    private void setInfo() {
        String text;
        if (election) {
            if (electionUsingQuickSort)
                text = "Using quick sort. Try typing bogo or lmao! Total elections count: ";
            else
                text = "Using bogo sort. Try typing quick or lockin! Total elections count: ";
            text += mainElectionList.size();
            text += ". Current elections count: ";
            text += currentElectionList.size();
            text += ".";
            listInfo.setText(text);
        }
        else
        {
            if (politicianUsingQuickSort)
                text = "Using quick sort. Try typing bogo or lmao! Total politicians count: ";
            else
                text = "Using bogo sort. Try typing quick or lockin! Total politicians count: ";
            text += mainPoliticianList.size();
            text += ". Current politicians count: ";
            text += currentPoliticianList.size();
            text += ".";
            listInfo.setText(text);
        }
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
                if (changeSortingType.isSelected())
                    if (e.getLocation().contains(searchBar.getText()))
                        newList.add(e);
                else
                    if (e.getName().contains(searchBar.getText()))
                        newList.add(e);
            }
            currentElectionList=Utilities.copyList(newList);
        }
        else
        {
            for (Politician p : currentPoliticianList) {
                if (changeSortingType.isSelected())
                    if (p.getHomeCountry().contains(searchBar.getText()))
                        newList.add(p);
                else
                    if (p.getName().contains(searchBar.getText()))
                        newList.add(p);
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
                return first.getHomeCountry().compareToIgnoreCase(second.getHomeCountry());
            case "Name Descending":
                return -first.getName().compareToIgnoreCase(second.getName());
            case "Party Descending":
                return -first.getParty().compareToIgnoreCase(second.getParty());
            case "Date of Birth Descending":
                return -first.getDateOfBirth().compareTo(second.getDateOfBirth());
            case "Home County Descending":
                return -first.getHomeCountry().compareToIgnoreCase(second.getHomeCountry());
        }
        return -1;
    }

    private int compareElections(Election first, Election second, String criteria)
    {
        switch (criteria)
        {
            case "Name Ascending":
                return first.getName().compareToIgnoreCase(second.getName());
            case "Type Ascending":
                return Utilities.electionTypeReverseMap.get(first.getType()).getValue().compareToIgnoreCase(Utilities.electionTypeReverseMap.get(second.getType()).getValue());
            case "Year Ascending":
                return first.getYearDate().compareTo(second.getYearDate());
            case "Location Ascending":
                return first.getLocation().compareToIgnoreCase(second.getLocation());
            case "Name Descending":
                return -first.getName().compareToIgnoreCase(second.getName());
            case "Type Descending":
                return -Utilities.electionTypeReverseMap.get(first.getType()).getValue().compareTo(Utilities.electionTypeReverseMap.get(second.getType()).getValue());
            case "Year Descending":
                return -first.getYearDate().compareTo(second.getYearDate());
            case "Location Descending":
                return -first.getLocation().compareTo(second.getLocation());
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
                if (compareElections((Election) list.get(i), (Election) list.get(i + 1), criteria) < 0)
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
                if (compareElections((Election) list.get(start), (Election) list.get(end), criteria) == 1)
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
                if (compareElections((Election) list.get(i), (Election) pivot, criteria) >=0)
                {
                    while (i<=j)
                    {
                        if (compareElections((Election) list.get(j), (Election) pivot, criteria) <0)
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
}
