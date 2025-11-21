package electionmngmntsys;

import electionmngmntsys.mhashmap.mHashMap;
import electionmngmntsys.mlinkedlist.mLinkedList;
import electionmngmntsys.models.Election;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Modality;

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
        electionFilterType.getItems().addAll("Type", "Year", "Location");
        electionFilterType.setValue("Type");

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
                        mainList.remove(mainList.get(index));
                    }
                    draw();
                });
                update.setOnAction(e -> {
                    Election tmp=mainList.get(index);
                    electionEdit.electionName.setText(tmp.getName());
                    electionEdit.electionLocation.setText(tmp.getLocation());
                    electionEdit.electionType.setValue(Utilities.electionTypeReverseMap.get(tmp.getType()).getValue());
                    electionEdit.electionWinnerCount.setValue(tmp.getNumOfWinners());
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
        gc.setFill(Color.AQUAMARINE);
        Image image=new Image(getClass().getResourceAsStream("/images/election.jpg"));
        gc.fillRect(0, 0, electionCanvas.getWidth(), electionCanvas.getHeight());
        for (Election e: mainList) {
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
