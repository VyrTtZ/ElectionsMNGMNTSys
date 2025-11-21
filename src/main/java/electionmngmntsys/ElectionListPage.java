package electionmngmntsys;

import electionmngmntsys.mhashmap.mHashMap;
import electionmngmntsys.mlinkedlist.mLinkedList;
import electionmngmntsys.models.Election;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;


public class ElectionListPage {

    //Front End
    public Button electionTab, politicianTab;
    public ChoiceBox <String> electionFilterType, electionSort;
    public TextField electionSearchBar, electionFilterContent;
    public Canvas electionCanvas;

    public ContextMenu canvasContextMenu=new ContextMenu();

    //Back End
    public mLinkedList <Election> mainList, currentList;
    public mHashMap<Election, Integer> elections;
    public GraphicsContext gc;
    public Launcher launcher;

    public void setLauncher(Launcher launcher) {
        this.launcher = launcher;
    }

    @FXML
    public void initialize() {
        electionFilterType.getItems().addAll("Type", "Year", "Location");
        electionFilterType.setValue("Type");

        electionSort.getItems().addAll("Name Ascending", "Type Ascending", "Year Ascending", "Location Ascending", "Name Descending", "Type Descending", "Year Descending", "Location Descending");
        electionSort.setValue("Name Ascending");

        canvasContextMenu.getItems().clear();
        MenuItem add=new MenuItem("Add");
        add.setOnAction(event -> {

        });
        canvasContextMenu.getItems().add(add);
        gc=electionCanvas.getGraphicsContext2D();
        draw();
    }

    @FXML
    public void draw() {

    }
}
