package electionmngmntsys;

import electionmngmntsys.mhashmap.mHashMap;
import electionmngmntsys.mlinkedlist.mLinkedList;
import electionmngmntsys.mlinkedlist.mNodeL;
import electionmngmntsys.models.Election;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Modality;

import java.util.LinkedList;
import java.util.Optional;


public class ElectionListPage {

    //Front End
    public Button electionTab, politicianTab;
    public ChoiceBox <String> electionFilterType, electionSort;
    public TextField electionSearchBar, electionFilterContent;
    public Canvas electionCanvas;

    public ContextMenu validContextMenu =new ContextMenu(), invalidContextMenu =new ContextMenu();

    //Back End
    public mLinkedList <Election> mainList=new mLinkedList<>(), currentList=new mLinkedList<>();
    public mHashMap<Election, Integer> elections=new mHashMap<>();
    public GraphicsContext gc;
    public Launcher launcher;
    public ElectionEdit electionEdit;

    public void setLauncher(Launcher launcher) {
        this.launcher = launcher;
    }

    public void setElectionEdit(ElectionEdit electionEdit) {
        this.electionEdit = electionEdit;
    }

    @FXML
    public void initialize() {
        electionFilterType.getItems().addAll("None", "Type", "Year", "Location");
        electionFilterType.setValue("None");

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

        electionFilterType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            draw();
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
            if (index!=-1 && index<=mainList.size()-1) {
                delete.setOnAction(e -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.initOwner(Launcher.stage);
                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText(null);
                    alert.setContentText("Are you sure you want to delete this election?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        mainList.remove(mainList.get(index).data);
                    }
                    draw();
                });
                update.setOnAction(e -> {
                    Election tmp=mainList.get(index).data;
                    electionEdit.electionName.setText(tmp.getName());
                    electionEdit.electionLocation.setText(tmp.getLocation());
                    electionEdit.electionType.setValue(Utilities.electionTypeReverseMap.get(tmp.getType()).getValue());
                    electionEdit.electionWinnerCount.setValue(tmp.getNumOfWinners());
                    electionEdit.updateIndex=index;
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
        quickSort(currentList, 0, currentList.size()-1, electionSort.getValue());
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
            if (compare(list.get(start).data, list.get(end).data, criteria) == 1)
                list.swapNodes(start, end);
            return;
        }
        int i=start, j=end-1;
        Election pivot=list.get(end).data;
        while (i<=j)
        {
            if (compare(list.get(i).data, pivot, criteria) >=0)
            {
                while (i<=j)
                {
                    if (compare(list.get(j).data, pivot, criteria) <0)
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
