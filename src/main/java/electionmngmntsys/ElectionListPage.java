package electionmngmntsys;

import electionmngmntsys.mhashmap.mHashMap;
import electionmngmntsys.mlinkedlist.mLinkedList;
import electionmngmntsys.models.Election;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;

import java.io.IOException;
import java.util.Optional;


public class ElectionListPage {

    //Front End
    public Button electionTab, politicianTab;
    public ChoiceBox <String> electionSort;
    public TextField electionSearchBar;
    public Canvas electionCanvas;
    public Label electionFilters;

    public ContextMenu validContextMenu =new ContextMenu(), invalidContextMenu =new ContextMenu();

    //Back End
    public mLinkedList <Election> mainList=new mLinkedList<>(), currentList=new mLinkedList<>();
    public mHashMap<Election, Integer> elections=new mHashMap<>();
    public GraphicsContext gc;
    public Launcher launcher;
    public ElectionEdit electionEdit;
    private String includeYear="", excludeYear="";
    private boolean localType=true, europeanType=true, presidentialType=true; //True -> include

    public void setLauncher(Launcher launcher) {
        this.launcher = launcher;
    }

    public void setElectionEdit(ElectionEdit electionEdit) {
        this.electionEdit = electionEdit;
    }

    @FXML
    public void initialize() {
        electionSort.getItems().addAll("Name Ascending", "Type Ascending", "Year Ascending", "Location Ascending", "Name Descending", "Type Descending", "Year Descending", "Location Descending");
        electionSort.setValue("Name Ascending");

        Utilities.initMap();
        initContextMenus();
        gc=electionCanvas.getGraphicsContext2D();

        electionCanvas.setOnMousePressed(event -> {
            if (validContextMenu.isShowing()) {
                validContextMenu.hide();
            }
            if (invalidContextMenu.isShowing()) {
                invalidContextMenu.hide();
            }
        });

        electionFilters.setOnContextMenuRequested(event -> {
            try {
                FXMLLoader filterLoader = new FXMLLoader(getClass().getResource("/electionFilterEdit.fxml"));
                DialogPane dialogContent = filterLoader.load();
                ElectionFilterEdit controller = filterLoader.getController();

                controller.setIncludeYearField(includeYear);
                controller.setExcludeYearField(excludeYear);
                controller.setLocalType(!localType);
                controller.setEuropeanType(!europeanType);
                controller.setPresidentialType(!presidentialType);
                Dialog <ButtonType> dialog = new Dialog<>();
                dialog.initOwner(Launcher.stage);
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.getDialogPane().setContent(dialogContent);
                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                Optional<ButtonType> result = dialog.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    includeYear=controller.getIncludeYearField().trim();
                    excludeYear=controller.getExcludeYearField().trim();
                    localType=!controller.getLocalType();
                    europeanType=!controller.getEuropeanType();
                    presidentialType=!controller.getPresidentialType();
                    String tmp="Active filters (right click to edit):";
                    if (!localType || !europeanType || !presidentialType) {
                        tmp+=" T";
                        if (!includeYear.isEmpty() || !excludeYear.isEmpty()) {
                            tmp+=", Y;";
                        }
                        else
                            tmp+=";";
                    }
                    else if (!includeYear.isEmpty() || !excludeYear.isEmpty()) {
                        tmp+=" Y;";
                    }
                    electionFilters.setText(tmp);
                    draw();
                }
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        electionSort.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            draw();
        });
        draw();
    }

    @FXML
    public void initContextMenus()
    {
        validContextMenu.getItems().clear();
        MenuItem add=new MenuItem("Add"), delete=new MenuItem("Delete"), update=new MenuItem("Update");
        add.setOnAction(event -> {
            launcher.switchScene("electionForm");
        });
        validContextMenu.getItems().add(add);
        electionCanvas.setOnContextMenuRequested(event -> {
            int index=getIndexOfMouse((int) event.getScreenX(), (int) event.getScreenY()-104); //top buttons and stuff
            if (index!=-1 && index<=currentList.size()-1) {
                delete.setOnAction(e -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.initOwner(Launcher.stage);
                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText(null);
                    alert.setContentText("Are you sure you want to delete this election?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        mainList.remove(currentList.get(index));
                    }
                    draw();
                });
                update.setOnAction(e -> {
                    Election tmp=currentList.get(index);
                    electionEdit.electionName.setText(tmp.getName());
                    electionEdit.electionLocation.setText(tmp.getLocation());
                    electionEdit.electionType.setValue(Utilities.electionTypeReverseMap.get(tmp.getType()).getValue());
                    electionEdit.electionWinnerCount.setValue(tmp.getNumOfWinners());
                    electionEdit.updateIndex=index;
                    mainList.remove(tmp);
                    launcher.switchScene("electionForm");
                });
                validContextMenu.getItems().add(delete);
                validContextMenu.getItems().add(update);
                validContextMenu.show(electionCanvas, event.getScreenX(), event.getScreenY());
            }
            else
                invalidContextMenu.show(electionCanvas, event.getScreenX(), event.getScreenY());
        });

        invalidContextMenu.getItems().clear();
        invalidContextMenu.getItems().add(add);
    }

    @FXML
    public void draw() {
        int x=50, y=20;
        filterAndSort();
        gc.setFill(Color.AQUAMARINE);
        Image image=new Image(getClass().getResourceAsStream("/images/election.jpg"));
        gc.fillRect(0, 0, electionCanvas.getWidth(), electionCanvas.getHeight());
        for (Election e: currentList) {
            gc.setFill(Color.WHITE);
            gc.fillRect(x, y, electionCanvas.getWidth()-100, 200);
            gc.drawImage(image, x+20, y+20, 160, 160);
            gc.setFill(Color.BLACK);
            gc.fillText("Election "+e.getName(), x+200, y+20);
            gc.fillText("Election Type: "+Utilities.electionTypeReverseMap.get(e.getType()).getValue(), x+200, y+52);
            gc.fillText("Election Location: "+e.getLocation(), x+200, y+82);
            gc.fillText("Date: "+e.getYearDate(), x+200, y+112);
            gc.fillText("Notable info: Nothing here yet lmao", x+200, y+142);
            y+=220;
        }
    }

    private void filterAndSort()
    {
        currentList=Utilities.copyList(mainList);
        filter(currentList);
        quickSort(currentList, 0, currentList.size()-1, electionSort.getValue());
    }

    private void filter(mLinkedList <Election> list)
    {
        mLinkedList <Election> newList = new mLinkedList<>();
        for (Election e: list)
        {
            boolean flag=false;
            switch (e.getType())
            {
                case 1:
                    if (!localType)
                        flag=true;
                    break;
                case 2:
                    if (!europeanType)
                        flag=true;
                    break;
                case 3:
                    if (!presidentialType)
                        flag=true;
                    break;
            }
            if (!includeYear.isEmpty()) {
                boolean flag2=false;
                for (int i = 0; i < Utilities.getCommaSeparatedStringSize(includeYear); i++) {
                    String tmp=Utilities.getValueFromCommaSeparatedString(i, includeYear);
                    if (Utilities.isValidInteger(tmp))
                    {
                        if (e.getYearDate().getYear()==Integer.parseInt(tmp))
                        {
                            flag2=true;
                            break;
                        }
                    }
                }
                if (!flag2)
                    flag=true;
            }
            if (!excludeYear.isEmpty()) {
                for (int i = 0; i < Utilities.getCommaSeparatedStringSize(excludeYear); i++) {
                    String tmp=Utilities.getValueFromCommaSeparatedString(i, excludeYear);
                    if (Utilities.isValidInteger(tmp))
                    {
                        if (e.getYearDate().getYear()==Integer.parseInt(tmp))
                        {
                            flag=true;
                            break;
                        }
                    }
                }
            }
            if (!flag)
                newList.add(e);
        }
        currentList=Utilities.copyList(newList);
    }

    private int compare(Election first, Election second, String criteria)
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

    private void quickSort(mLinkedList <Election> list, int start, int end, String criteria)
    {
        if (start>=end)
            return;
        if (start==end-1) {
            if (compare(list.get(start), list.get(end), criteria) == 1)
                list.swapNodes(start, end);
            return;
        }
        int i=start, j=end-1;
        Election pivot=list.get(end);
        while (i<=j)
        {
            if (compare(list.get(i), pivot, criteria) >=0)
            {
                while (i<=j)
                {
                    if (compare(list.get(j), pivot, criteria) <0)
                        break;
                    j--;
                }
                if (j>i)
                    list.swapNodes(i, j);
                else
                    break;
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
        if (x<50 || x>electionCanvas.getWidth()-50)
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
