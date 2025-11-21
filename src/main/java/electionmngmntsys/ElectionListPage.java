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
import javafx.stage.Screen;


public class ElectionListPage {

    //Front End
    public Button electionTab, politicianTab;
    public ChoiceBox <String> electionFilterType, electionSort;
    public TextField electionSearchBar, electionFilterContent;
    public Canvas electionCanvas;

    public ContextMenu canvasContextMenu=new ContextMenu();

    //Back End
    public mLinkedList <Election> mainList=new mLinkedList<>(), currentList=new mLinkedList<>();
    public mHashMap<Election, Integer> elections=new mHashMap<>();
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
            launcher.switchScene("electionForm");
        });
        electionCanvas.setOnContextMenuRequested(event -> {
            canvasContextMenu.show(electionCanvas, event.getScreenX(), event.getScreenY());
        });
        electionCanvas.setOnMousePressed(event -> {
            if (canvasContextMenu.isShowing()) {
                canvasContextMenu.hide();
            }
        });
        Utilities.initMap();
        canvasContextMenu.getItems().add(add);
        gc=electionCanvas.getGraphicsContext2D();

        draw();
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
}
